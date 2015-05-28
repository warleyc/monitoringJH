package org.shm.monitoring.service;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

/**
 * Created by crawleyn on 11/04/2014.
 */
@Service
public class ThymeleafService {

    @Inject
    TemplateEngine templateEngine;



    public void sendEmail(final String recipientName, final String recipientEmail, final String imageResourceName,
                     final byte[] imageBytes, final String imageContentType, final Locale locale)
            throws MessagingException {
        final Context ctx = new Context(locale);
        ctx.setVariable("name", recipientName);
        ctx.setVariable("subscriptionDate", new Date());
        ctx.setVariable("hobbies", Arrays.asList("Cinema", "Sports", "Music"));
        ctx.setVariable("imageResourceName", imageResourceName); // so that we can reference it from HTML

        // Prepare message using a Spring helper
        final MimeMessage mimeMessage = null;//this.mailSender.createMimeMessage();
        final MimeMessageHelper message =
                new MimeMessageHelper(mimeMessage, true, "UTF-8"); // true = multipart
        message.setSubject("Example HTML email with inline image");
        message.setFrom("thymeleaf@example.com");
        message.setTo(recipientEmail);

        // Create the HTML body using Thymeleaf
        final String htmlContent = this.templateEngine.process("email-inlineimage.html", ctx);
        message.setText(htmlContent, true); // true = isHtml

        // Add the inline image, referenced from the HTML code as "cid:${imageResourceName}"
        final InputStreamSource imageSource = new ByteArrayResource(imageBytes);
        message.addInline(imageResourceName, imageSource, imageContentType);

        // Send mail
       // this.mailSender.send(mimeMessage);


    }

}
