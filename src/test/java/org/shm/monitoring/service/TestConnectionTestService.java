package org.shm.monitoring.service;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.shm.monitoring.Application;
import org.shm.monitoring.domain.ProjectConfiguration;
import org.shm.monitoring.domain.enumeration.RequestMethodEnum;
import org.shm.monitoring.web.dto.HttpResponse;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.logging.Logger;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
@Transactional
public class TestConnectionTestService {

    private static final Logger log = Logger.getLogger(TestConnectionTestService.class.getName());

    //@Inject
    TestConnectionService testConnectionService = new TestConnectionService();

    //@Autowired
   // private TestServices testServices;

    @Test
    public void testUrl() {


        //System.setProperty("https.proxyHost", "202.27.42.27");
        //System.setProperty("https.proxyPort", "8080");

        ProjectConfiguration projectConfiguration = new ProjectConfiguration();

        projectConfiguration.setName("test");
        //configuration.setUrl("https://www.nzls.org.nz/RegistrationDB/webservice/");
        projectConfiguration.setUrl("https://localhost:446/RegistrationDB/webservice/");
        //configuration.setUrl("https://wlgdevnzlsreg03.wlgsp.infinity.co.nz/RegistrationDB/");
        projectConfiguration.setActif(true);
        projectConfiguration.setRequestMethod(RequestMethodEnum.POST);



        String post="<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:sch=\"http://www.nzls.org.nz/RegistrationDB/schema\">"+
        "<soapenv:Header>"+
        "<wsse:Security soapenv:mustUnderstand=\"1\" xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\">"+
        "<wsse:UsernameToken>"+
        //"<wsse:Username>CLEWebServicesUser</wsse:Username>"+
        //"<wsse:Password>yua81zmp</wsse:Password>"+
        "<wsse:Username>CLEWebServicesUser</wsse:Username>"+
        "<wsse:Password>password</wsse:Password>"+
        "</wsse:UsernameToken>"+
        "</wsse:Security>"+
        "</soapenv:Header>"+
        "<soapenv:Body>"+
        "<sch:GetLawyersRequest>"+
        "<sch:SurnameOrFirstName/>"+
        "<sch:Name>rees</sch:Name>"+
        "<sch:Town/>"+
        "<sch:Postcode/>"+
        "<sch:AreaOfPracticeCode/>"+
        "<sch:LanguageCode/>"+
        "</sch:GetLawyersRequest>"+
        "</soapenv:Body>"+
        "</soapenv:Envelope>";

        projectConfiguration.setPost(new String(post));

        projectConfiguration.setContentType( "text/xml ; charset=utf-8");
        projectConfiguration.setSoap(true);
        HttpResponse res= testConnectionService.testUrl(projectConfiguration);

        System.out.println(res);
    }




}
