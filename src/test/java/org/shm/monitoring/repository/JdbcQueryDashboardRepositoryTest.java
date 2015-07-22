package org.shm.monitoring.repository;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.shm.monitoring.Application;
import org.shm.monitoring.domain.Response;
import org.shm.monitoring.domain.enumeration.ReponseTypeEnum;
import org.shm.monitoring.web.rest.dto.SerieDTO;
import org.shm.monitoring.web.rest.util.PaginationUtil;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
* Test class for the ResponseRepository class.
*
* @see ResponseRepository
*/
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class JdbcQueryDashboardRepositoryTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    private static final ReponseTypeEnum DEFAULT_TYPE = ReponseTypeEnum.ERROR;
    private static final ReponseTypeEnum UPDATED_TYPE = ReponseTypeEnum.INFO;
    private static final String DEFAULT_MESSAGE = "SAMPLE_TEXT";
    private static final String UPDATED_MESSAGE = "UPDATED_TEXT";
    private static final String DEFAULT_RESPONSE = "SAMPLE_TEXT";
    private static final String UPDATED_RESPONSE = "UPDATED_TEXT";

    private static final Integer DEFAULT_CODE = 0;
    private static final Integer UPDATED_CODE = 1;

    private static final Long DEFAULT_DURATION = 0L;
    private static final Long UPDATED_DURATION = 1L;
    private static final String DEFAULT_CONFIGURATION_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_CONFIGURATION_NAME = "UPDATED_TEXT";

    private static final Long DEFAULT_CONFIGURATION_ID = 0L;
    private static final Long UPDATED_CONFIGURATION_ID = 1L;

    private static final Boolean DEFAULT_EMAIL_SENT = false;
    private static final Boolean UPDATED_EMAIL_SENT = true;

    private static final DateTime DEFAULT_DATE = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_DATE = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_DATE_STR = dateTimeFormatter.print(DEFAULT_DATE);

    @Inject
    private JdbcQueryDashBoardRepository jdbcQueryDashBoardRepository;


    private Response response;

    @PostConstruct
    public void setup() {

    }

    @Before
    public void initTest() {
        response = new Response();
        response.setType(DEFAULT_TYPE);
        response.setMessage(DEFAULT_MESSAGE);
        response.setResponse(DEFAULT_RESPONSE);
        response.setCode(DEFAULT_CODE);
        response.setDuration(DEFAULT_DURATION);
        response.setConfigurationName(DEFAULT_CONFIGURATION_NAME);
        response.setConfigurationId(DEFAULT_CONFIGURATION_ID);
        response.setEmailSent(DEFAULT_EMAIL_SENT);
        response.setDate(DEFAULT_DATE);
    }

    @Test
    @Transactional
    public void selectSeries() throws Exception {



        List<SerieDTO> series=jdbcQueryDashBoardRepository.selectSeries();

        System.out.println("series size:"+series.size());
        for (SerieDTO serie:series){
            System.out.println("serie"+serie);
        }


    }



}
