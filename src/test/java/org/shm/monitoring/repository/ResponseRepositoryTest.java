package org.shm.monitoring.repository;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.shm.monitoring.Application;
import org.shm.monitoring.domain.Response;
import org.shm.monitoring.security.AuthoritiesConstants;
import org.shm.monitoring.security.SecurityUtils;
import org.shm.monitoring.web.rest.ResponseResource;
import org.shm.monitoring.web.rest.TestUtil;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
* Test class for the ResponseRepository class.
*
* @see ResponseRepository
*/
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ResponseRepositoryTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    private static final String DEFAULT_TYPE = "ERROR";
    private static final String UPDATED_TYPE = "INFO";
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
    private ResponseRepository responseRepository;


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
    public void removeByType() throws Exception {


        responseRepository.findAll();

        int databaseSizeBeforeCreate = responseRepository.findAll().size();

        responseRepository.deleteAll();
        databaseSizeBeforeCreate = responseRepository.findAll().size();
        assertThat(databaseSizeBeforeCreate ).isEqualTo(0);
        responseRepository.save(response);
        databaseSizeBeforeCreate = responseRepository.findAll().size();
        assertThat(databaseSizeBeforeCreate ).isEqualTo(1);
        List<Response> list=responseRepository.removeByType("ERROR");
        assertThat(list.size() ).isEqualTo(1);

        databaseSizeBeforeCreate = responseRepository.findAll().size();
        assertThat(databaseSizeBeforeCreate ).isEqualTo(0);
    }


    @Test
    @Transactional
    public void deleteByType() throws Exception {


        responseRepository.findAll();

        int databaseSizeBeforeCreate = responseRepository.findAll().size();

        responseRepository.deleteAll();
        databaseSizeBeforeCreate = responseRepository.findAll().size();
        assertThat(databaseSizeBeforeCreate ).isEqualTo(0);
        responseRepository.save(response);
        databaseSizeBeforeCreate = responseRepository.findAll().size();
        assertThat(databaseSizeBeforeCreate ).isEqualTo(1);
        Long count=responseRepository.deleteByType("ERROR");
        assertThat(count ).isEqualTo(1);
        databaseSizeBeforeCreate = responseRepository.findAll().size();
        assertThat(databaseSizeBeforeCreate ).isEqualTo(0);
    }

    @Test
         @Transactional
         public void deleteByDateBefore() throws Exception {


        responseRepository.findAll();

        int databaseSizeBeforeCreate = responseRepository.findAll().size();

        responseRepository.deleteAll();
        databaseSizeBeforeCreate = responseRepository.findAll().size();
        assertThat(databaseSizeBeforeCreate ).isEqualTo(0);
        responseRepository.save(response);
        databaseSizeBeforeCreate = responseRepository.findAll().size();
        assertThat(databaseSizeBeforeCreate ).isEqualTo(1);
        Long count=responseRepository.deleteByDateBefore(DateTime.now().minusDays(-1));
        assertThat(count ).isEqualTo(1);
        databaseSizeBeforeCreate = responseRepository.findAll().size();
        assertThat(databaseSizeBeforeCreate ).isEqualTo(0);
    }



    @Test
    @Transactional
    public void deleteByTypeAndDateBefore() throws Exception {


        responseRepository.findAll();

        int databaseSizeBeforeCreate = responseRepository.findAll().size();

        responseRepository.deleteAll();
        databaseSizeBeforeCreate = responseRepository.findAll().size();
        assertThat(databaseSizeBeforeCreate ).isEqualTo(0);
        responseRepository.save(response);
        databaseSizeBeforeCreate = responseRepository.findAll().size();
        assertThat(databaseSizeBeforeCreate ).isEqualTo(1);
        Long count=responseRepository.deleteByTypeAndDateBefore("ERROR",DateTime.now().minusDays(-1));
        assertThat(count ).isEqualTo(1);
        databaseSizeBeforeCreate = responseRepository.findAll().size();
        assertThat(databaseSizeBeforeCreate ).isEqualTo(0);
    }


}
