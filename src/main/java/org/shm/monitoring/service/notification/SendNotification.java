package org.shm.monitoring.service.notification;


import org.shm.monitoring.domain.ProjectConfiguration;
import org.shm.monitoring.web.dto.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.inject.Inject;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class SendNotification {

    private static final Logger log = LoggerFactory.getLogger(SendNotification.class);



    @Inject
    TemplateEngine templateEngine;

    /**
     *
     */
    public boolean send(ProjectConfiguration projectConfiguration, HttpResponse httpResponse) {

        String subject=null;
        try {


            if (httpResponse.getCode() != 200) {
                subject="[ERROR][" + projectConfiguration.getName() + "] code:" + httpResponse.getCode() + " message:" + httpResponse.getResponseMessage();
            } else {
                // retries = warning
                if (httpResponse.getRetries() == 0) {
                    subject="[OK][" + projectConfiguration.getName() + "] is back to normal";
                } else {
                    subject="[WARNING][" + projectConfiguration.getName() + "]  is back to normal but after " + httpResponse.getRetries() + " fail";
                }
            }


            final Context ctx = new Context(Locale.ENGLISH);
            ctx.setVariable("httpResponse", httpResponse);
            ctx.setVariable("configuration", projectConfiguration);
            // Create the HTML body using Thymeleaf
            String htmlBody = this.templateEngine.process("report.html", ctx);

            String email= projectConfiguration !=null? projectConfiguration.getEmail():null;
            sendMessage(email,subject, htmlBody);

        } catch (Exception e) {
            log.warn("E-mail could not be sent to user '{}', exception is: {}", subject, e.getMessage());
        }

        return true;
    }

    private void sendMessage(String email,String subject, String htmlBody) throws MessagingException,
        UnsupportedEncodingException  {

        Message msg = getMessage(email);

        Multipart mp = new MimeMultipart();

        MimeBodyPart htmlPart = new MimeBodyPart();
        htmlPart.setContent(htmlBody, "text/html");
        mp.addBodyPart(htmlPart);

        msg.setContent(mp);

        // msg.setText(msgBody);
        Transport.send(msg);
    }

    public void sendNotification(String subject)  {


        try {
            Message msg = getMessage(null);
            Date date = Calendar.getInstance().getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("E MM/dd/yyyy HH:mm:ss.SSS");
            msg.setSubject(subject + sdf.format(date));
            msg.setText("test");
            Transport.send(msg);

        } catch (Exception e) {
            log.warn("E-mail could not be sent to user '{}', exception is: {}", subject, e.getMessage());
        }

    }

    /**
     *
     */
    public void sendNotification() {

        sendNotification("Notification: ");

    }

    /**
     *
     */
    public void sendReport(List<ProjectConfiguration> projectConfigurations) throws  UnsupportedEncodingException, MessagingException {

        String email=null;
        String subject="Report";


        final Context ctx = new Context(Locale.ENGLISH);
        ctx.setVariable("name", "recipientName");
        ctx.setVariable("subscriptionDate", new Date());
        ctx.setVariable("hobbies", Arrays.asList("Cinema", "Sports", "Music"));
        ctx.setVariable("imageResourceName", "imageResourceName"); // so that we can reference it from HTML
        ctx.setVariable("configurations", projectConfigurations);

        log.info("Before");
        // Create the HTML body using Thymeleaf
        final String htmlContent = this.templateEngine.process("report.html", ctx);

        sendMessage( email,subject, htmlContent);

    }


    private Message getMessage(String email) throws MessagingException,
        UnsupportedEncodingException {

        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress("nicolas.crawley@gmail.com", "Admin Monitoring"));
        msg.addRecipient(Message.RecipientType.TO, new InternetAddress("nicolas.crawley@gmail.com", "Mr. Drako"));
        if (email != null) {
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(email, ""));
        }

        return msg;
    }



}
