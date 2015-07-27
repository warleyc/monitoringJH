package org.shm.monitoring.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.shm.monitoring.Application;
import org.shm.monitoring.domain.ProjectConfiguration;
import org.shm.monitoring.web.dto.HttpResponse;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;

import javax.inject.Inject;
import java.util.Locale;
import java.util.logging.Logger;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration

public class TestSendNotificationTest {

    private static final Logger log = Logger.getLogger(TestSendNotificationTest.class.getName());


    @Inject
    SendNotification sendNotification;


    @Test
    public void testSendNotification() {

        try {
        sendNotification.sendNotification();
            System.out.println("afuck");
        } catch (Throwable e) {

            e.printStackTrace();
        }
        System.out.println("fuck");
        //assertThat(persistentTokenRepository.findByUser(admin)).hasSize(existingCount + 2);
        //assertThat(persistentTokenRepository.findByUser(admin)).hasSize(existingCount + 1);
    }


    public void testSendNotification2() {

        try {
            sendNotification.sendNotification();
            System.out.println("afuck2");
        } catch (Throwable e) {

            e.printStackTrace();
        }
        System.out.println("fuck2");
        //assertThat(persistentTokenRepository.findByUser(admin)).hasSize(existingCount + 2);
        //assertThat(persistentTokenRepository.findByUser(admin)).hasSize(existingCount + 1);
    }

    @Test
    public void testGetHtmlBody() {

        ProjectConfiguration projectConfiguration= new ProjectConfiguration();
        projectConfiguration.setName("Nicolas web site");

        HttpResponse httpResponse = new HttpResponse ();

        httpResponse.setCode(200);
        httpResponse.setDuration(356L);
        httpResponse.setMessage("mon mesage");
        httpResponse.setResponse(" response");
        httpResponse.setStackTrace(" stacktrace");




        String htmlbody=sendNotification.getHtmlBody(projectConfiguration,httpResponse);
        System.out.println("htmlbody : "+htmlbody);
    }

}
