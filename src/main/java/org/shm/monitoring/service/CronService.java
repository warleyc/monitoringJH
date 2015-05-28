package org.shm.monitoring.service;

import org.joda.time.DateTime;
import org.shm.monitoring.domain.Response;
import org.shm.monitoring.repository.ResponseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.inject.Inject;


@Service
public class CronService {

    private final Logger log = LoggerFactory.getLogger(CronService.class);


    @Inject
    private Environment env;
    @Inject
    private SendNotification sendNotification;

    @Inject
    private ResponseRepository responseRepository;
    @Inject
    private TestServices testServices;



    @Inject
    private StatistiqueServices statistiqueServices;





    @Scheduled(cron = "0 */5 * * * *")
    public void monitoring() {
        log.info("monitoring every 5 minutes");
/*
        long start = System.currentTimeMillis();
        try {
            testServices.sendPageViewStatistiques();
            statistiqueServices.sendStatistiques();
            //sendNotification.sendNotification();
        } catch (Exception e) {
            log.error("checkWebServices Error :", e);
        }
*/
        testServices.test();

/*
        long end = System.currentTimeMillis();
        long time = end - start;
        try {
            testServices.sendStatistiquesEventWithValue(time);
        } catch (Exception e) {
            log.error("checkWebServices Error sendStatistiquesEventWithValue:", e);
        }
        */

        log.info("monitoring end");


    }
    @Scheduled(cron = "0 */2 * * * *")
   // @Scheduled(cron = "* * 5 * * *")
    public void purgeInfo() {
        log.info("purge INFO 20 days ");
        purge("INFO", 20);
    }

    //@Scheduled(cron = "* * 6 * * *")
    @Scheduled(cron = "0 */5 * * * *")
    public void purgeError() {
        log.info("Purge des logs de type erreur au bout d'un ans!");
        purge("ERROR", 365);
    }


    public String purge(String type,
                        int duree) {

        log.info("Purge");
        long start = System.currentTimeMillis();

        try {

            int nb = purget(type, duree);
            long end = System.currentTimeMillis();
            long time = end - start;
            sendNotification.sendNotification("Purge de :" + nb + " logs en "
                + time + " secondes type:" + type + " duree:" + duree);
            log.info("Purge de :" + nb + " logs en " + time
                + " secondes");

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return "redirect:/welcome.do";
    }

    public int purget() {
        return purget("INFO", 6);
    }

    public int purget(String type, int duree) {

        String find = "delete From " + Response.class.getName()
            + " t where type= :type and  date < :date ";
        DateTime date = DateTime.now();
        date.minusDays(duree);
        //return
            //responseRepository.deleteByTypeAndDateBefore(type, date);
        responseRepository.deleteByType(type);
        return 0;
    }

}
