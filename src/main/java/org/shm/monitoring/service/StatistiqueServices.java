package org.shm.monitoring.service;

import org.shm.monitoring.domain.ProjectConfiguration;
import org.shm.monitoring.domain.Response;
import org.shm.monitoring.domain.enumeration.RequestMethodEnum;
import org.shm.monitoring.helper.stat.Event;
import org.shm.monitoring.helper.stat.NetworkRequestUtil;
import org.shm.monitoring.web.dto.HttpResponse;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.Callable;

@Service
public class StatistiqueServices /*implements Callable<String>*/ {

    private static final String URL_PROJECT = "supervisionws.appspot.com";
    private static final String GA_ACOUNT = "UA-19185735-1";

    public String call() {
        // testAndSendAlert(configuration);
        return "";
    }

    public void sendStatistiques(HttpResponse httpResponse, Response log) throws UnsupportedEncodingException {

        // http://www.google-analytics.com/__utm.gif?utmwv=4.8.6&utmn=1902151949&utmhn=repondeur-plus.sfr.fr&utmcs=UTF-8&utmsr=1280x1024&utmsc=24-bit&utmul=fr&utmje=1&utmfl=10.0%20r32&utmdt=SFR%20%3A%20Votre%20R%C3%A9pondeur%20Intelligent&utmhid=44551784&utmr=-&utmp=%2F&utmac=UA-10861286-9&utmcc=__utma%3D238984195.1427487604.1284437890.1287553044.1288037890.3%3B%2B__utmz%3D238984195.1284437890.1.1.utmcsr%3Dsfr.fr%7Cutmccn%3D(referral)%7Cutmcmd%3Dreferral%7Cutmcct%3D%2Fservices%2F%3B&utmu=D
        // http://www.google-analytics.com/__utm.gif?utmwv=4.8.6&utmn=2105157773&utmhn=repondeur-plus.sfr.fr&utmcs=UTF-8&utmsr=1280x1024&utmsc=24-bit&utmul=fr&utmje=1&utmfl=10.0%20r32&utmdt=SFR%20%3A%20Votre%20R%C3%A9pondeur%20Intelligent&utmhid=44551784&utmr=-&utmp=%2F&utmac=UA-10861286-9&utmcc=__utma%3D238984195.1427487604.1284437890.1287553044.1288037890.3%3B%2B__utmz%3D238984195.1284437890.1.1.utmcsr%3Dsfr.fr%7Cutmccn%3D(referral)%7Cutmcmd%3Dreferral%7Cutmcct%3D%2Fservices%2F%3B&utmu=T
        // utme=5(Action*Activer%20Annonce)&utmt=event

        // http://www.google-analytics.com/__utm.gif?utmwv=4.8.6&utmn=2105157773&utmhn=repondeur-plus.sfr.fr&utmt=event&utme=5(Action*Activer%20Annonce)&utmcs=UTF-8&utmsr=1280x1024&utmsc=24-bit&utmul=fr&utmje=1&utmfl=10.0%20r32&utmdt=SFR%20%3A%20Votre%20R%C3%A9pondeur%20Intelligent&utmhid=44551784&utmr=-&utmp=%2F&utmac=UA-10861286-9&utmcc=__utma%3D238984195.1427487604.1284437890.1287553044.1288037890.3%3B%2B__utmz%3D238984195.1284437890.1.1.utmcsr%3Dsfr.fr%7Cutmccn%3D(referral)%7Cutmcmd%3Dreferral%7Cutmcct%3D%2Fservices%2F%3B&utmu=T

        TestConnectionService testConnectionService = new TestConnectionService();

        String url = "";
        if (httpResponse.getProjectConfiguration() != null) {
            url = httpResponse.getProjectConfiguration().getUrl();
        }

        /**
         * Event event = new Event(long eventId, int userId, String accountId,
         * int randomVal, int timestampFirst, int timestampPrevious, int
         * timestampCurrent, int visits, String category, String action, String
         * label, int value, int screenWidth, int screenHeight)
         */
        String accountId = GA_ACOUNT;
        int randomVal = (int) (Math.random() * 0x7fffffff);
        int timestampFirst = 0;
        int timestampPrevious = 0;
        int timestampCurrent = 0;
        int visits = 0;
        String category = log.getType().name();
        String action = URLEncoder.encode(url, "UTF-8");
        String label = null;
        int value =  httpResponse.getDuration().intValue();
        int screenWidth = 1280;
        int screenHeight = 1024;
        Event event = new Event(1, 1, accountId, randomVal, timestampFirst,
            timestampPrevious, timestampCurrent, visits, category, action,
            label, value, screenWidth, screenHeight);
        String referrer = URL_PROJECT;
        String targetUrl = NetworkRequestUtil.constructPageviewRequestPath(
            event, referrer);

        String codeGoogle = GA_ACOUNT;
        String utmhn = URL_PROJECT;
        // String utme="5(Action*Activer%20Annonce)";
        //

        String utme = "5(" + log.getType() + "*" + URLEncoder.encode(url, "UTF-8")
            + ")(" + httpResponse.getDuration() + ")";

        targetUrl = "http://www.google-analytics.com/__utm.gif?utmwv=4.8.6&utmn=1308786022&utmhn="
            + utmhn
            + "&utmt=event&utme="
            + utme
            + "&utmcs=UTF-8&utmsr=1280x1024&utmsc=24-bit&utmul=fr&utmje=1&utmfl=10.0%20r32&utmdt=Supervisiont&utmhid=1928203529&utmr=-&utmp=%2F&utmac="
            + codeGoogle
            + "&utmcc=__utma%3D238984195.1427487604.1284437890.1284437890.1287553044.2%3B%2B__utmz%3D238984195.1284437890.1.1.utmcsr%3Dsfr.fr%7Cutmccn%3D(referral)%7Cutmcmd%3Dreferral%7Cutmcct%3D%2Fservices%2F%3B&utmu=T";

        ProjectConfiguration projectConfiguration = new ProjectConfiguration();
        projectConfiguration.setUrl(targetUrl);
        projectConfiguration.setRequestMethod(RequestMethodEnum.GET);
        //HttpResponse httpResponseGoogle =
        testConnectionService.testUrl(projectConfiguration);
    }

    public void sendStatistiques() {

        TestConnectionService testConnectionService = new TestConnectionService();

        /**
         * Event event = new Event(long eventId, int userId, String accountId,
         * int randomVal, int timestampFirst, int timestampPrevious, int
         * timestampCurrent, int visits, String category, String action, String
         * label, int value, int screenWidth, int screenHeight)
         */
        String accountId = GA_ACOUNT;
        int randomVal = (int) (Math.random() * 0x7fffffff);
        int timestampFirst = 0;
        int timestampPrevious = 0;
        int timestampCurrent = 0;
        int visits = 0;
        String category = null;
        String action = null;
        String label = null;
        int value = 0;
        int screenWidth = 1280;
        int screenHeight = 1024;
        Event event = new Event(1, 1, accountId, randomVal, timestampFirst,
            timestampPrevious, timestampCurrent, visits, category, action,
            label, value, screenWidth, screenHeight);
        String referrer = URL_PROJECT;
        String targetUrl = "http://www.google-analytics.com"
            + NetworkRequestUtil.constructPageviewRequestPath(event,
            referrer);

        ProjectConfiguration projectConfiguration = new ProjectConfiguration();
        projectConfiguration.setUrl(targetUrl);
        projectConfiguration.setRequestMethod(RequestMethodEnum.GET);
        //HttpResponse httpResponseGoogle =
        testConnectionService.testUrl(projectConfiguration);
    }

    public void sendStatistiquesEventWithValue(long time) {

        // http://www.google-analytics.com/__utm.gif?utmwv=4.8.6&utmn=1902151949&utmhn=repondeur-plus.sfr.fr&utmcs=UTF-8&utmsr=1280x1024&utmsc=24-bit&utmul=fr&utmje=1&utmfl=10.0%20r32&utmdt=SFR%20%3A%20Votre%20R%C3%A9pondeur%20Intelligent&utmhid=44551784&utmr=-&utmp=%2F&utmac=UA-10861286-9&utmcc=__utma%3D238984195.1427487604.1284437890.1287553044.1288037890.3%3B%2B__utmz%3D238984195.1284437890.1.1.utmcsr%3Dsfr.fr%7Cutmccn%3D(referral)%7Cutmcmd%3Dreferral%7Cutmcct%3D%2Fservices%2F%3B&utmu=D
        // http://www.google-analytics.com/__utm.gif?utmwv=4.8.6&utmn=2105157773&utmhn=repondeur-plus.sfr.fr&utmcs=UTF-8&utmsr=1280x1024&utmsc=24-bit&utmul=fr&utmje=1&utmfl=10.0%20r32&utmdt=SFR%20%3A%20Votre%20R%C3%A9pondeur%20Intelligent&utmhid=44551784&utmr=-&utmp=%2F&utmac=UA-10861286-9&utmcc=__utma%3D238984195.1427487604.1284437890.1287553044.1288037890.3%3B%2B__utmz%3D238984195.1284437890.1.1.utmcsr%3Dsfr.fr%7Cutmccn%3D(referral)%7Cutmcmd%3Dreferral%7Cutmcct%3D%2Fservices%2F%3B&utmu=T
        // utme=5(Action*Activer%20Annonce)&utmt=event

        // http://www.google-analytics.com/__utm.gif?utmwv=4.8.6&utmn=2105157773&utmhn=repondeur-plus.sfr.fr&utmt=event&utme=5(Action*Activer%20Annonce)&utmcs=UTF-8&utmsr=1280x1024&utmsc=24-bit&utmul=fr&utmje=1&utmfl=10.0%20r32&utmdt=SFR%20%3A%20Votre%20R%C3%A9pondeur%20Intelligent&utmhid=44551784&utmr=-&utmp=%2F&utmac=UA-10861286-9&utmcc=__utma%3D238984195.1427487604.1284437890.1287553044.1288037890.3%3B%2B__utmz%3D238984195.1284437890.1.1.utmcsr%3Dsfr.fr%7Cutmccn%3D(referral)%7Cutmcmd%3Dreferral%7Cutmcct%3D%2Fservices%2F%3B&utmu=T

        TestConnectionService testConnectionService = new TestConnectionService();
        String codeGoogle = GA_ACOUNT;
        String utmhn = URL_PROJECT;

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
        projectConfiguration.setRequestMethod(RequestMethodEnum.GET);
        //HttpResponse httpResponseGoogle =
        testConnectionService.testUrl(projectConfiguration);
    }

    public HttpResponse sendPageViewStatistiques() {

        // http://www.google-analytics.com/__utm.gif?utmwv=4.8.6&utmn=1902151949&utmhn=repondeur-plus.sfr.fr&utmcs=UTF-8&utmsr=1280x1024&utmsc=24-bit&utmul=fr&utmje=1&utmfl=10.0%20r32&utmdt=SFR%20%3A%20Votre%20R%C3%A9pondeur%20Intelligent&utmhid=44551784&utmr=-&utmp=%2F&utmac=UA-10861286-9&utmcc=__utma%3D238984195.1427487604.1284437890.1287553044.1288037890.3%3B%2B__utmz%3D238984195.1284437890.1.1.utmcsr%3Dsfr.fr%7Cutmccn%3D(referral)%7Cutmcmd%3Dreferral%7Cutmcct%3D%2Fservices%2F%3B&utmu=D
        // http://www.google-analytics.com/__utm.gif?utmwv=4.8.6&utmn=2105157773&utmhn=repondeur-plus.sfr.fr&utmcs=UTF-8&utmsr=1280x1024&utmsc=24-bit&utmul=fr&utmje=1&utmfl=10.0%20r32&utmdt=SFR%20%3A%20Votre%20R%C3%A9pondeur%20Intelligent&utmhid=44551784&utmr=-&utmp=%2F&utmac=UA-10861286-9&utmcc=__utma%3D238984195.1427487604.1284437890.1287553044.1288037890.3%3B%2B__utmz%3D238984195.1284437890.1.1.utmcsr%3Dsfr.fr%7Cutmccn%3D(referral)%7Cutmcmd%3Dreferral%7Cutmcct%3D%2Fservices%2F%3B&utmu=T
        // utme=5(Action*Activer%20Annonce)&utmt=event

        // http://www.google-analytics.com/__utm.gif?utmwv=4.8.6&utmn=2105157773&utmhn=repondeur-plus.sfr.fr&utmt=event&utme=5(Action*Activer%20Annonce)&utmcs=UTF-8&utmsr=1280x1024&utmsc=24-bit&utmul=fr&utmje=1&utmfl=10.0%20r32&utmdt=SFR%20%3A%20Votre%20R%C3%A9pondeur%20Intelligent&utmhid=44551784&utmr=-&utmp=%2F&utmac=UA-10861286-9&utmcc=__utma%3D238984195.1427487604.1284437890.1287553044.1288037890.3%3B%2B__utmz%3D238984195.1284437890.1.1.utmcsr%3Dsfr.fr%7Cutmccn%3D(referral)%7Cutmcmd%3Dreferral%7Cutmcct%3D%2Fservices%2F%3B&utmu=T

        TestConnectionService testConnectionService = new TestConnectionService();
        String codeGoogle = GA_ACOUNT;
        String utmhn = URL_PROJECT;

        String targetUrl = "http://www.google-analytics.com/__utm.gif?utmwv=4.8.6&utmn=1308786022&utmhn="
            + utmhn
            + "&utmt=event"
            + "&utmcs=UTF-8&utmsr=1280x1024&utmsc=24-bit&utmul=fr&utmje=1&utmfl=10.0%20r32&utmdt=Supervisiont&utmhid=1928203529&utmr=-&utmp=%2F&utmac="
            + codeGoogle
            + "&utmcc=__utma%3D238984195.1427487604.1284437890.1284437890.1287553044.2%3B%2B__utmz%3D238984195.1284437890.1.1.utmcsr%3Dsfr.fr%7Cutmccn%3D(referral)%7Cutmcmd%3Dreferral%7Cutmcct%3D%2Fservices%2F%3B&utmu=D";

        ProjectConfiguration projectConfiguration = new ProjectConfiguration();
        projectConfiguration.setUrl(targetUrl);
        projectConfiguration.setRequestMethod(RequestMethodEnum.GET);

        return testConnectionService.testUrl(projectConfiguration);
    }

}
