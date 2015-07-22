package org.shm.monitoring.service;

import com.codahale.metrics.annotation.Timed;
import org.joda.time.DateTime;
import org.shm.monitoring.domain.enumeration.ReponseTypeEnum;
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
    private ProjectConfigurationServices projectConfigurationServices;


    @Inject
    private StatistiqueServices statistiqueServices;


    @Scheduled(cron = "0 */5 * * * *")
    @Timed
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
        projectConfigurationServices.test();

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

    //@Scheduled(cron = "0 */2 * * * *")
    @Scheduled(cron = "0 0 5 * * *")
    public void purgeInfo() {
        log.info("purge INFO 20 days ");
        purgeAndSendNotification(ReponseTypeEnum.INFO, 20);
    }

    @Scheduled(cron = "0 0 6 * * *")
    //@Scheduled(cron = "0 */5 * * * *")
    public void purgeError() {
        log.info("Purge des logs de type erreur au bout d'un ans!");
        purgeAndSendNotification(ReponseTypeEnum.ERROR, 365);
    }


    public void purgeAndSendNotification(ReponseTypeEnum type, int duree) {

        log.info("Purge");
        long start = System.currentTimeMillis();

        try {
            Long nb = purget(type, duree);
            long end = System.currentTimeMillis();
            long time = end - start;
            sendNotification.sendNotification("Purge de :" + nb + " logs en "
                + time + " secondes type:" + type + " duree:" + duree);
            log.info("Purge de :" + nb + " logs en " + time
                + " secondes");

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public Long purget() {
        return purget(ReponseTypeEnum.INFO, 6);
    }

    public Long purget(ReponseTypeEnum type, int duree) {
        DateTime date = DateTime.now();
        date.minusDays(duree);
        return responseRepository.deleteByTypeAndDateBefore(type, date);
    }

}
