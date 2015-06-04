package org.shm.monitoring.service;

import org.joda.time.DateTime;
import org.shm.monitoring.domain.ProjectConfiguration;
import org.shm.monitoring.domain.Response;
import org.shm.monitoring.repository.ProjectConfigurationRepository;
import org.shm.monitoring.repository.ResponseRepository;
import org.shm.monitoring.web.dto.HttpResponse;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
public class ProjectConfigurationServices /*implements Callable<String>*/ {

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(ProjectConfigurationServices.class);


    @Inject
    private ProjectConfigurationRepository projectConfigurationRepository;

    @Inject
    private ResponseRepository responseRepository;


    @Inject
    private SendNotification sendNotification;


    public String call() {
        //testAndSendAlert(configuration);
        return "";
    }


    public boolean test() {
        List<ProjectConfiguration> projectConfigurations = projectConfigurationRepository.findAll();
        logger.info("projectConfigurations size" + projectConfigurations);
        for (ProjectConfiguration projectConfiguration : projectConfigurations) {
            if (projectConfiguration.getActif()) {
                testAndSendAlert(projectConfiguration);
            }
        }
        return true;
    }

    public boolean testFutur() {


        List<ProjectConfiguration> projectConfigurations = projectConfigurationRepository.findAll();
        List<Future<String>> task = new ArrayList<Future<String>>(projectConfigurations.size());

        ExecutorService executor = Executors.newFixedThreadPool(projectConfigurations.size());

        for (final ProjectConfiguration projectConfiguration : projectConfigurations) {
            if (projectConfiguration.getActif()) {
                Future<String> future = executor.submit(new CallableService<String>(projectConfiguration) {
                    public String call() {
                        testAndSendAlert(projectConfiguration);
                        return "ok";
                    }
                });
                task.add(future);

            }
        }

        boolean notFinish = true;
        while (notFinish) {
            notFinish = false;
            for (Future<String> future : task) {
                if (!future.isDone()) {
                    notFinish = true;
                }
            }

        }

        return true;
    }

    /**
     * @param projectConfiguration
     * @return
     */
    public HttpResponse testAndSendAlert(ProjectConfiguration projectConfiguration) {

        HttpResponse httpResponse = testAndSaveLog(projectConfiguration);

        if (!isSucces(httpResponse)) {
            //check if new error
            if (projectConfiguration.getLastError() == null || (projectConfiguration.getLastSucces() != null
                && projectConfiguration.getLastError().isBefore(projectConfiguration.getLastSucces()))) {
                // send message only if a new error
                sendNotification.send(projectConfiguration, httpResponse);
            }
            projectConfiguration.setLastError(new DateTime());
        } else {//sucess but check if  back to normal
            if (projectConfiguration.getLastError() != null && projectConfiguration.getLastSucces() != null && projectConfiguration.getLastError().isAfter(projectConfiguration.getLastSucces())) {
                // send message back to normal
                sendNotification.send(projectConfiguration, httpResponse);
            }
            projectConfiguration.setLastSucces(new DateTime());

        }
        projectConfigurationRepository.save(projectConfiguration);

        return httpResponse;

    }

    public HttpResponse testAndSaveLog(ProjectConfiguration projectConfiguration) {

        long start = System.currentTimeMillis();
        TestConnectionService testConnectionService = new TestConnectionService();
        HttpResponse httpResponse = testConnectionService.testUrl(projectConfiguration);

        long duration = System.currentTimeMillis() - start;

        httpResponse.setTime(duration);
        save(httpResponse);

        return httpResponse;
    }

    private void save(HttpResponse httpResponse) {
        Response log = new Response();
        log.setDate(DateTime.now());

        if (httpResponse != null && httpResponse.getResult() != null) {
            if (httpResponse.getResult().length() > 499) {
                log.setResponse(httpResponse.getResult().substring(0, 499));
            } else {
                log.setResponse(httpResponse.getResult());
            }
        }
        log.setDuration(httpResponse.getTime());
        log.setMessage(httpResponse.getResponseMessage());
        log.setCode(httpResponse.getCode());
        if (httpResponse.getProjectConfiguration() != null) {
            log.setConfigurationName(httpResponse.getProjectConfiguration().getName());
            log.setConfigurationId(httpResponse.getProjectConfiguration().getId());
        }

        if (isSucces(httpResponse)) {
            log.setType("INFO");
        } else {
            log.setType("ERROR");
        }

        try {
            responseRepository.save(log);
        } catch (Exception e) {
            // TODO PAS BO
            httpResponse.setResponseMessage(httpResponse.getResponseMessage() + " Sauvegarde KO " + e.getMessage());
        }

        sendStatistiques(httpResponse, log);


    }

    private boolean isSucces(HttpResponse httpResponse) {
        return httpResponse.getCode() == 200
            && checkMessage(httpResponse.getProjectConfiguration().getCheckMessage(),
            httpResponse);
    }

    private void sendStatistiques(HttpResponse httpResponse, Response log) {


        // http://www.google-analytics.com/__utm.gif?utmwv=4.8.6&utmn=1902151949&utmhn=repondeur-plus.sfr.fr&utmcs=UTF-8&utmsr=1280x1024&utmsc=24-bit&utmul=fr&utmje=1&utmfl=10.0%20r32&utmdt=SFR%20%3A%20Votre%20R%C3%A9pondeur%20Intelligent&utmhid=44551784&utmr=-&utmp=%2F&utmac=UA-10861286-9&utmcc=__utma%3D238984195.1427487604.1284437890.1287553044.1288037890.3%3B%2B__utmz%3D238984195.1284437890.1.1.utmcsr%3Dsfr.fr%7Cutmccn%3D(referral)%7Cutmcmd%3Dreferral%7Cutmcct%3D%2Fservices%2F%3B&utmu=D
        // http://www.google-analytics.com/__utm.gif?utmwv=4.8.6&utmn=2105157773&utmhn=repondeur-plus.sfr.fr&utmcs=UTF-8&utmsr=1280x1024&utmsc=24-bit&utmul=fr&utmje=1&utmfl=10.0%20r32&utmdt=SFR%20%3A%20Votre%20R%C3%A9pondeur%20Intelligent&utmhid=44551784&utmr=-&utmp=%2F&utmac=UA-10861286-9&utmcc=__utma%3D238984195.1427487604.1284437890.1287553044.1288037890.3%3B%2B__utmz%3D238984195.1284437890.1.1.utmcsr%3Dsfr.fr%7Cutmccn%3D(referral)%7Cutmcmd%3Dreferral%7Cutmcct%3D%2Fservices%2F%3B&utmu=T
        // utme=5(Action*Activer%20Annonce)&utmt=event

        // http://www.google-analytics.com/__utm.gif?utmwv=4.8.6&utmn=2105157773&utmhn=repondeur-plus.sfr.fr&utmt=event&utme=5(Action*Activer%20Annonce)&utmcs=UTF-8&utmsr=1280x1024&utmsc=24-bit&utmul=fr&utmje=1&utmfl=10.0%20r32&utmdt=SFR%20%3A%20Votre%20R%C3%A9pondeur%20Intelligent&utmhid=44551784&utmr=-&utmp=%2F&utmac=UA-10861286-9&utmcc=__utma%3D238984195.1427487604.1284437890.1287553044.1288037890.3%3B%2B__utmz%3D238984195.1284437890.1.1.utmcsr%3Dsfr.fr%7Cutmccn%3D(referral)%7Cutmcmd%3Dreferral%7Cutmcct%3D%2Fservices%2F%3B&utmu=T

        TestConnectionService testConnectionService = new TestConnectionService();
        String codeGoogle = "UA-19185735-1";
        String utmhn = "supervisionws.appspot.com";
        // String utme="5(Action*Activer%20Annonce)";
        String url = "";
        if (httpResponse.getProjectConfiguration() != null) {
            url = httpResponse.getProjectConfiguration().getUrl();
        }

        String utme = "";
        try {
            utme = "5(" + log.getType() + "*" + URLEncoder.encode(url, "UTF-8")
                + ")(" + httpResponse.getTime() + ")";
        } catch (UnsupportedEncodingException e) {
            logger.error("Erreur sur l'encodage", e);

        }

        String targetUrl = "http://www.google-analytics.com/__utm.gif?utmwv=4.8.6&utmn=1308786022&utmhn="
            + utmhn
            + "&utmt=event&utme="
            + utme
            + "&utmcs=UTF-8&utmsr=1280x1024&utmsc=24-bit&utmul=fr&utmje=1&utmfl=10.0%20r32&utmdt=Supervisiont&utmhid=1928203529&utmr=-&utmp=%2F&utmac="
            + codeGoogle
            + "&utmcc=__utma%3D238984195.1427487604.1284437890.1284437890.1287553044.2%3B%2B__utmz%3D238984195.1284437890.1.1.utmcsr%3Dsfr.fr%7Cutmccn%3D(referral)%7Cutmcmd%3Dreferral%7Cutmcct%3D%2Fservices%2F%3B&utmu=T";

        ProjectConfiguration projectConfiguration = new ProjectConfiguration();
        projectConfiguration.setUrl(targetUrl);
        projectConfiguration.setRequestMethod("GET");
        //HttpResponse httpResponseGoogle =
        testConnectionService.testUrl(projectConfiguration);
    }

    public void sendStatistiquesEventWithValue(long time) {


        // http://www.google-analytics.com/__utm.gif?utmwv=4.8.6&utmn=1902151949&utmhn=repondeur-plus.sfr.fr&utmcs=UTF-8&utmsr=1280x1024&utmsc=24-bit&utmul=fr&utmje=1&utmfl=10.0%20r32&utmdt=SFR%20%3A%20Votre%20R%C3%A9pondeur%20Intelligent&utmhid=44551784&utmr=-&utmp=%2F&utmac=UA-10861286-9&utmcc=__utma%3D238984195.1427487604.1284437890.1287553044.1288037890.3%3B%2B__utmz%3D238984195.1284437890.1.1.utmcsr%3Dsfr.fr%7Cutmccn%3D(referral)%7Cutmcmd%3Dreferral%7Cutmcct%3D%2Fservices%2F%3B&utmu=D
        // http://www.google-analytics.com/__utm.gif?utmwv=4.8.6&utmn=2105157773&utmhn=repondeur-plus.sfr.fr&utmcs=UTF-8&utmsr=1280x1024&utmsc=24-bit&utmul=fr&utmje=1&utmfl=10.0%20r32&utmdt=SFR%20%3A%20Votre%20R%C3%A9pondeur%20Intelligent&utmhid=44551784&utmr=-&utmp=%2F&utmac=UA-10861286-9&utmcc=__utma%3D238984195.1427487604.1284437890.1287553044.1288037890.3%3B%2B__utmz%3D238984195.1284437890.1.1.utmcsr%3Dsfr.fr%7Cutmccn%3D(referral)%7Cutmcmd%3Dreferral%7Cutmcct%3D%2Fservices%2F%3B&utmu=T
        // utme=5(Action*Activer%20Annonce)&utmt=event

        // http://www.google-analytics.com/__utm.gif?utmwv=4.8.6&utmn=2105157773&utmhn=repondeur-plus.sfr.fr&utmt=event&utme=5(Action*Activer%20Annonce)&utmcs=UTF-8&utmsr=1280x1024&utmsc=24-bit&utmul=fr&utmje=1&utmfl=10.0%20r32&utmdt=SFR%20%3A%20Votre%20R%C3%A9pondeur%20Intelligent&utmhid=44551784&utmr=-&utmp=%2F&utmac=UA-10861286-9&utmcc=__utma%3D238984195.1427487604.1284437890.1287553044.1288037890.3%3B%2B__utmz%3D238984195.1284437890.1.1.utmcsr%3Dsfr.fr%7Cutmccn%3D(referral)%7Cutmcmd%3Dreferral%7Cutmcct%3D%2Fservices%2F%3B&utmu=T

        TestConnectionService testConnectionService = new TestConnectionService();
        String codeGoogle = "UA-19185735-1";
        String utmhn = "supervisionws.appspot.com";


        String utme = "5(Time*End)(" + time + ")";

        String targetUrl = "http://www.google-analytics.com/__utm.gif?utmwv=4.8.6&utmn=1308786022&utmhn="
            + utmhn
            + "&utmt=event&utme="
            + utme
            + "&utmcs=UTF-8&utmsr=1280x1024&utmsc=24-bit&utmul=fr&utmje=1&utmfl=10.0%20r32&utmdt=Supervisiont&utmhid=1928203529&utmr=-&utmp=%2F&utmac="
            + codeGoogle
            + "&utmcc=__utma%3D238984195.1427487604.1284437890.1284437890.1287553044.2%3B%2B__utmz%3D238984195.1284437890.1.1.utmcsr%3Dsfr.fr%7Cutmccn%3D(referral)%7Cutmcmd%3Dreferral%7Cutmcct%3D%2Fservices%2F%3B&utmu=T";

        ProjectConfiguration projectConfiguration = new ProjectConfiguration();
        projectConfiguration.setUrl(targetUrl);
        projectConfiguration.setRequestMethod("GET");
        //HttpResponse httpResponseGoogle =
        testConnectionService
            .testUrl(projectConfiguration);
    }

    public void sendPageViewStatistiques() {


        // http://www.google-analytics.com/__utm.gif?utmwv=4.8.6&utmn=1902151949&utmhn=repondeur-plus.sfr.fr&utmcs=UTF-8&utmsr=1280x1024&utmsc=24-bit&utmul=fr&utmje=1&utmfl=10.0%20r32&utmdt=SFR%20%3A%20Votre%20R%C3%A9pondeur%20Intelligent&utmhid=44551784&utmr=-&utmp=%2F&utmac=UA-10861286-9&utmcc=__utma%3D238984195.1427487604.1284437890.1287553044.1288037890.3%3B%2B__utmz%3D238984195.1284437890.1.1.utmcsr%3Dsfr.fr%7Cutmccn%3D(referral)%7Cutmcmd%3Dreferral%7Cutmcct%3D%2Fservices%2F%3B&utmu=D
        // http://www.google-analytics.com/__utm.gif?utmwv=4.8.6&utmn=2105157773&utmhn=repondeur-plus.sfr.fr&utmcs=UTF-8&utmsr=1280x1024&utmsc=24-bit&utmul=fr&utmje=1&utmfl=10.0%20r32&utmdt=SFR%20%3A%20Votre%20R%C3%A9pondeur%20Intelligent&utmhid=44551784&utmr=-&utmp=%2F&utmac=UA-10861286-9&utmcc=__utma%3D238984195.1427487604.1284437890.1287553044.1288037890.3%3B%2B__utmz%3D238984195.1284437890.1.1.utmcsr%3Dsfr.fr%7Cutmccn%3D(referral)%7Cutmcmd%3Dreferral%7Cutmcct%3D%2Fservices%2F%3B&utmu=T
        // utme=5(Action*Activer%20Annonce)&utmt=event

        // http://www.google-analytics.com/__utm.gif?utmwv=4.8.6&utmn=2105157773&utmhn=repondeur-plus.sfr.fr&utmt=event&utme=5(Action*Activer%20Annonce)&utmcs=UTF-8&utmsr=1280x1024&utmsc=24-bit&utmul=fr&utmje=1&utmfl=10.0%20r32&utmdt=SFR%20%3A%20Votre%20R%C3%A9pondeur%20Intelligent&utmhid=44551784&utmr=-&utmp=%2F&utmac=UA-10861286-9&utmcc=__utma%3D238984195.1427487604.1284437890.1287553044.1288037890.3%3B%2B__utmz%3D238984195.1284437890.1.1.utmcsr%3Dsfr.fr%7Cutmccn%3D(referral)%7Cutmcmd%3Dreferral%7Cutmcct%3D%2Fservices%2F%3B&utmu=T

        TestConnectionService testConnectionService = new TestConnectionService();


        String codeGoogle = "UA-19185735-1";
        String utmhn = "supervisionws.appspot.com";

        String targetUrl = "http://www.google-analytics.com/__utm.gif?utmwv=4.8.6&utmn=1308786022&utmhn="
            + utmhn
            + "&utmt=event"
            + "&utmcs=UTF-8&utmsr=1280x1024&utmsc=24-bit&utmul=fr&utmje=1&utmfl=10.0%20r32&utmdt=Supervisiont&utmhid=1928203529&utmr=-&utmp=%2F&utmac="
            + codeGoogle
            + "&utmcc=__utma%3D238984195.1427487604.1284437890.1284437890.1287553044.2%3B%2B__utmz%3D238984195.1284437890.1.1.utmcsr%3Dsfr.fr%7Cutmccn%3D(referral)%7Cutmcmd%3Dreferral%7Cutmcct%3D%2Fservices%2F%3B&utmu=D";

        ProjectConfiguration projectConfiguration = new ProjectConfiguration();
        projectConfiguration.setUrl(targetUrl);
        projectConfiguration.setRequestMethod("GET");
        //HttpResponse httpResponseGoogle =
        testConnectionService.testUrl(projectConfiguration);
    }

    private boolean checkMessage(String string, HttpResponse httpResponse) {
        return string == null
            || (httpResponse.getResult() != null && httpResponse
            .getResult().contains(string));
    }

}
