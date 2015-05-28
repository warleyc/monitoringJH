package org.shm.monitoring.web.rest;

import org.shm.monitoring.Application;
import org.shm.monitoring.domain.ProjectConfiguration;
import org.shm.monitoring.repository.ProjectConfigurationRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ProjectConfigurationResource REST controller.
 *
 * @see ProjectConfigurationResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ProjectConfigurationResourceTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    private static final String DEFAULT_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_NAME = "UPDATED_TEXT";
    private static final String DEFAULT_URL = "SAMPLE_TEXT";
    private static final String UPDATED_URL = "UPDATED_TEXT";
    private static final String DEFAULT_POST = "SAMPLE_TEXT";
    private static final String UPDATED_POST = "UPDATED_TEXT";
    private static final String DEFAULT_PARAMETRE = "SAMPLE_TEXT";
    private static final String UPDATED_PARAMETRE = "UPDATED_TEXT";
    private static final String DEFAULT_REQUEST_METHOD = "SAMPLE_TEXT";
    private static final String UPDATED_REQUEST_METHOD = "UPDATED_TEXT";
    private static final String DEFAULT_HEADER = "SAMPLE_TEXT";
    private static final String UPDATED_HEADER = "UPDATED_TEXT";

    private static final Integer DEFAULT_FREQUENCE = 0;
    private static final Integer UPDATED_FREQUENCE = 1;
    private static final String DEFAULT_LOGIN = "SAMPLE_TEXT";
    private static final String UPDATED_LOGIN = "UPDATED_TEXT";
    private static final String DEFAULT_PASSWORD2 = "SAMPLE_TEXT";
    private static final String UPDATED_PASSWORD2 = "UPDATED_TEXT";
    private static final String DEFAULT_CHECK_MESSAGE = "SAMPLE_TEXT";
    private static final String UPDATED_CHECK_MESSAGE = "UPDATED_TEXT";

    private static final DateTime DEFAULT_LAST_ERROR = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_LAST_ERROR = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_LAST_ERROR_STR = dateTimeFormatter.print(DEFAULT_LAST_ERROR);

    private static final DateTime DEFAULT_LAST_SUCCES = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_LAST_SUCCES = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_LAST_SUCCES_STR = dateTimeFormatter.print(DEFAULT_LAST_SUCCES);

    private static final Boolean DEFAULT_ACTIF = false;
    private static final Boolean UPDATED_ACTIF = true;

    private static final Boolean DEFAULT_ALERT_SMS = false;
    private static final Boolean UPDATED_ALERT_SMS = true;

    private static final Boolean DEFAULT_ALERT_MAIL = false;
    private static final Boolean UPDATED_ALERT_MAIL = true;
    private static final String DEFAULT_EMAIL = "SAMPLE_TEXT";
    private static final String UPDATED_EMAIL = "UPDATED_TEXT";
    private static final String DEFAULT_CONTENT_TYPE = "SAMPLE_TEXT";
    private static final String UPDATED_CONTENT_TYPE = "UPDATED_TEXT";

    private static final Boolean DEFAULT_SOAP = false;
    private static final Boolean UPDATED_SOAP = true;

    @Inject
    private ProjectConfigurationRepository projectConfigurationRepository;

    private MockMvc restProjectConfigurationMockMvc;

    private ProjectConfiguration projectConfiguration;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ProjectConfigurationResource projectConfigurationResource = new ProjectConfigurationResource();
        ReflectionTestUtils.setField(projectConfigurationResource, "projectConfigurationRepository", projectConfigurationRepository);
        this.restProjectConfigurationMockMvc = MockMvcBuilders.standaloneSetup(projectConfigurationResource).build();
    }

    @Before
    public void initTest() {
        projectConfiguration = new ProjectConfiguration();
        projectConfiguration.setName(DEFAULT_NAME);
        projectConfiguration.setUrl(DEFAULT_URL);
        projectConfiguration.setPost(DEFAULT_POST);
        projectConfiguration.setParametre(DEFAULT_PARAMETRE);
        projectConfiguration.setRequestMethod(DEFAULT_REQUEST_METHOD);
        projectConfiguration.setHeader(DEFAULT_HEADER);
        projectConfiguration.setFrequence(DEFAULT_FREQUENCE);
        projectConfiguration.setLogin(DEFAULT_LOGIN);
        projectConfiguration.setPassword2(DEFAULT_PASSWORD2);
        projectConfiguration.setCheckMessage(DEFAULT_CHECK_MESSAGE);
        projectConfiguration.setLastError(DEFAULT_LAST_ERROR);
        projectConfiguration.setLastSucces(DEFAULT_LAST_SUCCES);
        projectConfiguration.setActif(DEFAULT_ACTIF);
        projectConfiguration.setAlertSMS(DEFAULT_ALERT_SMS);
        projectConfiguration.setAlertMail(DEFAULT_ALERT_MAIL);
        projectConfiguration.setEmail(DEFAULT_EMAIL);
        projectConfiguration.setContentType(DEFAULT_CONTENT_TYPE);
        projectConfiguration.setSoap(DEFAULT_SOAP);
    }

    @Test
    @Transactional
    public void createProjectConfiguration() throws Exception {
        int databaseSizeBeforeCreate = projectConfigurationRepository.findAll().size();

        // Create the ProjectConfiguration
        restProjectConfigurationMockMvc.perform(post("/api/projectConfigurations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(projectConfiguration)))
                .andExpect(status().isCreated());

        // Validate the ProjectConfiguration in the database
        List<ProjectConfiguration> projectConfigurations = projectConfigurationRepository.findAll();
        assertThat(projectConfigurations).hasSize(databaseSizeBeforeCreate + 1);
        ProjectConfiguration testProjectConfiguration = projectConfigurations.get(projectConfigurations.size() - 1);
        assertThat(testProjectConfiguration.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProjectConfiguration.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testProjectConfiguration.getPost()).isEqualTo(DEFAULT_POST);
        assertThat(testProjectConfiguration.getParametre()).isEqualTo(DEFAULT_PARAMETRE);
        assertThat(testProjectConfiguration.getRequestMethod()).isEqualTo(DEFAULT_REQUEST_METHOD);
        assertThat(testProjectConfiguration.getHeader()).isEqualTo(DEFAULT_HEADER);
        assertThat(testProjectConfiguration.getFrequence()).isEqualTo(DEFAULT_FREQUENCE);
        assertThat(testProjectConfiguration.getLogin()).isEqualTo(DEFAULT_LOGIN);
        assertThat(testProjectConfiguration.getPassword2()).isEqualTo(DEFAULT_PASSWORD2);
        assertThat(testProjectConfiguration.getCheckMessage()).isEqualTo(DEFAULT_CHECK_MESSAGE);
        assertThat(testProjectConfiguration.getLastError().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_LAST_ERROR);
        assertThat(testProjectConfiguration.getLastSucces().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_LAST_SUCCES);
        assertThat(testProjectConfiguration.getActif()).isEqualTo(DEFAULT_ACTIF);
        assertThat(testProjectConfiguration.getAlertSMS()).isEqualTo(DEFAULT_ALERT_SMS);
        assertThat(testProjectConfiguration.getAlertMail()).isEqualTo(DEFAULT_ALERT_MAIL);
        assertThat(testProjectConfiguration.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testProjectConfiguration.getContentType()).isEqualTo(DEFAULT_CONTENT_TYPE);
        assertThat(testProjectConfiguration.getSoap()).isEqualTo(DEFAULT_SOAP);
    }

    @Test
    @Transactional
    public void getAllProjectConfigurations() throws Exception {
        // Initialize the database
        projectConfigurationRepository.saveAndFlush(projectConfiguration);

        // Get all the projectConfigurations
        restProjectConfigurationMockMvc.perform(get("/api/projectConfigurations"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(projectConfiguration.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())))
                .andExpect(jsonPath("$.[*].post").value(hasItem(DEFAULT_POST.toString())))
                .andExpect(jsonPath("$.[*].parametre").value(hasItem(DEFAULT_PARAMETRE.toString())))
                .andExpect(jsonPath("$.[*].requestMethod").value(hasItem(DEFAULT_REQUEST_METHOD.toString())))
                .andExpect(jsonPath("$.[*].header").value(hasItem(DEFAULT_HEADER.toString())))
                .andExpect(jsonPath("$.[*].frequence").value(hasItem(DEFAULT_FREQUENCE)))
                .andExpect(jsonPath("$.[*].login").value(hasItem(DEFAULT_LOGIN.toString())))
                .andExpect(jsonPath("$.[*].password2").value(hasItem(DEFAULT_PASSWORD2.toString())))
                .andExpect(jsonPath("$.[*].checkMessage").value(hasItem(DEFAULT_CHECK_MESSAGE.toString())))
                .andExpect(jsonPath("$.[*].lastError").value(hasItem(DEFAULT_LAST_ERROR_STR)))
                .andExpect(jsonPath("$.[*].lastSucces").value(hasItem(DEFAULT_LAST_SUCCES_STR)))
                .andExpect(jsonPath("$.[*].actif").value(hasItem(DEFAULT_ACTIF.booleanValue())))
                .andExpect(jsonPath("$.[*].alertSMS").value(hasItem(DEFAULT_ALERT_SMS.booleanValue())))
                .andExpect(jsonPath("$.[*].alertMail").value(hasItem(DEFAULT_ALERT_MAIL.booleanValue())))
                .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
                .andExpect(jsonPath("$.[*].contentType").value(hasItem(DEFAULT_CONTENT_TYPE.toString())))
                .andExpect(jsonPath("$.[*].soap").value(hasItem(DEFAULT_SOAP.booleanValue())));
    }

    @Test
    @Transactional
    public void getProjectConfiguration() throws Exception {
        // Initialize the database
        projectConfigurationRepository.saveAndFlush(projectConfiguration);

        // Get the projectConfiguration
        restProjectConfigurationMockMvc.perform(get("/api/projectConfigurations/{id}", projectConfiguration.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(projectConfiguration.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()))
            .andExpect(jsonPath("$.post").value(DEFAULT_POST.toString()))
            .andExpect(jsonPath("$.parametre").value(DEFAULT_PARAMETRE.toString()))
            .andExpect(jsonPath("$.requestMethod").value(DEFAULT_REQUEST_METHOD.toString()))
            .andExpect(jsonPath("$.header").value(DEFAULT_HEADER.toString()))
            .andExpect(jsonPath("$.frequence").value(DEFAULT_FREQUENCE))
            .andExpect(jsonPath("$.login").value(DEFAULT_LOGIN.toString()))
            .andExpect(jsonPath("$.password2").value(DEFAULT_PASSWORD2.toString()))
            .andExpect(jsonPath("$.checkMessage").value(DEFAULT_CHECK_MESSAGE.toString()))
            .andExpect(jsonPath("$.lastError").value(DEFAULT_LAST_ERROR_STR))
            .andExpect(jsonPath("$.lastSucces").value(DEFAULT_LAST_SUCCES_STR))
            .andExpect(jsonPath("$.actif").value(DEFAULT_ACTIF.booleanValue()))
            .andExpect(jsonPath("$.alertSMS").value(DEFAULT_ALERT_SMS.booleanValue()))
            .andExpect(jsonPath("$.alertMail").value(DEFAULT_ALERT_MAIL.booleanValue()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.contentType").value(DEFAULT_CONTENT_TYPE.toString()))
            .andExpect(jsonPath("$.soap").value(DEFAULT_SOAP.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingProjectConfiguration() throws Exception {
        // Get the projectConfiguration
        restProjectConfigurationMockMvc.perform(get("/api/projectConfigurations/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProjectConfiguration() throws Exception {
        // Initialize the database
        projectConfigurationRepository.saveAndFlush(projectConfiguration);

		int databaseSizeBeforeUpdate = projectConfigurationRepository.findAll().size();

        // Update the projectConfiguration
        projectConfiguration.setName(UPDATED_NAME);
        projectConfiguration.setUrl(UPDATED_URL);
        projectConfiguration.setPost(UPDATED_POST);
        projectConfiguration.setParametre(UPDATED_PARAMETRE);
        projectConfiguration.setRequestMethod(UPDATED_REQUEST_METHOD);
        projectConfiguration.setHeader(UPDATED_HEADER);
        projectConfiguration.setFrequence(UPDATED_FREQUENCE);
        projectConfiguration.setLogin(UPDATED_LOGIN);
        projectConfiguration.setPassword2(UPDATED_PASSWORD2);
        projectConfiguration.setCheckMessage(UPDATED_CHECK_MESSAGE);
        projectConfiguration.setLastError(UPDATED_LAST_ERROR);
        projectConfiguration.setLastSucces(UPDATED_LAST_SUCCES);
        projectConfiguration.setActif(UPDATED_ACTIF);
        projectConfiguration.setAlertSMS(UPDATED_ALERT_SMS);
        projectConfiguration.setAlertMail(UPDATED_ALERT_MAIL);
        projectConfiguration.setEmail(UPDATED_EMAIL);
        projectConfiguration.setContentType(UPDATED_CONTENT_TYPE);
        projectConfiguration.setSoap(UPDATED_SOAP);
        restProjectConfigurationMockMvc.perform(put("/api/projectConfigurations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(projectConfiguration)))
                .andExpect(status().isOk());

        // Validate the ProjectConfiguration in the database
        List<ProjectConfiguration> projectConfigurations = projectConfigurationRepository.findAll();
        assertThat(projectConfigurations).hasSize(databaseSizeBeforeUpdate);
        ProjectConfiguration testProjectConfiguration = projectConfigurations.get(projectConfigurations.size() - 1);
        assertThat(testProjectConfiguration.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProjectConfiguration.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testProjectConfiguration.getPost()).isEqualTo(UPDATED_POST);
        assertThat(testProjectConfiguration.getParametre()).isEqualTo(UPDATED_PARAMETRE);
        assertThat(testProjectConfiguration.getRequestMethod()).isEqualTo(UPDATED_REQUEST_METHOD);
        assertThat(testProjectConfiguration.getHeader()).isEqualTo(UPDATED_HEADER);
        assertThat(testProjectConfiguration.getFrequence()).isEqualTo(UPDATED_FREQUENCE);
        assertThat(testProjectConfiguration.getLogin()).isEqualTo(UPDATED_LOGIN);
        assertThat(testProjectConfiguration.getPassword2()).isEqualTo(UPDATED_PASSWORD2);
        assertThat(testProjectConfiguration.getCheckMessage()).isEqualTo(UPDATED_CHECK_MESSAGE);
        assertThat(testProjectConfiguration.getLastError().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_LAST_ERROR);
        assertThat(testProjectConfiguration.getLastSucces().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_LAST_SUCCES);
        assertThat(testProjectConfiguration.getActif()).isEqualTo(UPDATED_ACTIF);
        assertThat(testProjectConfiguration.getAlertSMS()).isEqualTo(UPDATED_ALERT_SMS);
        assertThat(testProjectConfiguration.getAlertMail()).isEqualTo(UPDATED_ALERT_MAIL);
        assertThat(testProjectConfiguration.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testProjectConfiguration.getContentType()).isEqualTo(UPDATED_CONTENT_TYPE);
        assertThat(testProjectConfiguration.getSoap()).isEqualTo(UPDATED_SOAP);
    }

    @Test
    @Transactional
    public void deleteProjectConfiguration() throws Exception {
        // Initialize the database
        projectConfigurationRepository.saveAndFlush(projectConfiguration);

		int databaseSizeBeforeDelete = projectConfigurationRepository.findAll().size();

        // Get the projectConfiguration
        restProjectConfigurationMockMvc.perform(delete("/api/projectConfigurations/{id}", projectConfiguration.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ProjectConfiguration> projectConfigurations = projectConfigurationRepository.findAll();
        assertThat(projectConfigurations).hasSize(databaseSizeBeforeDelete - 1);
    }
}
