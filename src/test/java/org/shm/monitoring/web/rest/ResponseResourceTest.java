package org.shm.monitoring.web.rest;

import org.shm.monitoring.Application;
import org.shm.monitoring.domain.Response;
import org.shm.monitoring.repository.ResponseRepository;

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

import org.shm.monitoring.domain.enumeration.ReponseTypeEnum;

/**
 * Test class for the ResponseResource REST controller.
 *
 * @see ResponseResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ResponseResourceTest {

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

    private static final Boolean DEFAULT_EMAIL_SENT = false;
    private static final Boolean UPDATED_EMAIL_SENT = true;

    private static final DateTime DEFAULT_DATE = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_DATE = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_DATE_STR = dateTimeFormatter.print(DEFAULT_DATE);
    private static final String DEFAULT_STACK_TRACE = "SAMPLE_TEXT";
    private static final String UPDATED_STACK_TRACE = "UPDATED_TEXT";

    @Inject
    private ResponseRepository responseRepository;

    private MockMvc restResponseMockMvc;

    private Response response;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ResponseResource responseResource = new ResponseResource();
        ReflectionTestUtils.setField(responseResource, "responseRepository", responseRepository);
        this.restResponseMockMvc = MockMvcBuilders.standaloneSetup(responseResource).build();
    }

    @Before
    public void initTest() {
        response = new Response();
        response.setType(DEFAULT_TYPE);
        response.setMessage(DEFAULT_MESSAGE);
        response.setResponse(DEFAULT_RESPONSE);
        response.setCode(DEFAULT_CODE);
        response.setDuration(DEFAULT_DURATION);
        response.setEmailSent(DEFAULT_EMAIL_SENT);
        response.setDate(DEFAULT_DATE);
        response.setStackTrace(DEFAULT_STACK_TRACE);
    }

    @Test
    @Transactional
    public void createResponse() throws Exception {
        int databaseSizeBeforeCreate = responseRepository.findAll().size();

        // Create the Response
        restResponseMockMvc.perform(post("/api/responses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(response)))
                .andExpect(status().isCreated());

        // Validate the Response in the database
        List<Response> responses = responseRepository.findAll();
        assertThat(responses).hasSize(databaseSizeBeforeCreate + 1);
        Response testResponse = responses.get(responses.size() - 1);
        assertThat(testResponse.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testResponse.getMessage()).isEqualTo(DEFAULT_MESSAGE);
        assertThat(testResponse.getResponse()).isEqualTo(DEFAULT_RESPONSE);
        assertThat(testResponse.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testResponse.getDuration()).isEqualTo(DEFAULT_DURATION);
        assertThat(testResponse.getEmailSent()).isEqualTo(DEFAULT_EMAIL_SENT);
        assertThat(testResponse.getDate().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_DATE);
        assertThat(testResponse.getStackTrace()).isEqualTo(DEFAULT_STACK_TRACE);
    }

    @Test
    @Transactional
    public void getAllResponses() throws Exception {
        // Initialize the database
        responseRepository.saveAndFlush(response);

        // Get all the responses
        restResponseMockMvc.perform(get("/api/responses"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(response.getId().intValue())))
                .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
                .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE.toString())))
                .andExpect(jsonPath("$.[*].response").value(hasItem(DEFAULT_RESPONSE.toString())))
                .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
                .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION.intValue())))
                .andExpect(jsonPath("$.[*].emailSent").value(hasItem(DEFAULT_EMAIL_SENT.booleanValue())))
                .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE_STR)))
                .andExpect(jsonPath("$.[*].stackTrace").value(hasItem(DEFAULT_STACK_TRACE.toString())));
    }

    @Test
    @Transactional
    public void getResponse() throws Exception {
        // Initialize the database
        responseRepository.saveAndFlush(response);

        // Get the response
        restResponseMockMvc.perform(get("/api/responses/{id}", response.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(response.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.message").value(DEFAULT_MESSAGE.toString()))
            .andExpect(jsonPath("$.response").value(DEFAULT_RESPONSE.toString()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.duration").value(DEFAULT_DURATION.intValue()))
            .andExpect(jsonPath("$.emailSent").value(DEFAULT_EMAIL_SENT.booleanValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE_STR))
            .andExpect(jsonPath("$.stackTrace").value(DEFAULT_STACK_TRACE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingResponse() throws Exception {
        // Get the response
        restResponseMockMvc.perform(get("/api/responses/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateResponse() throws Exception {
        // Initialize the database
        responseRepository.saveAndFlush(response);

		int databaseSizeBeforeUpdate = responseRepository.findAll().size();

        // Update the response
        response.setType(UPDATED_TYPE);
        response.setMessage(UPDATED_MESSAGE);
        response.setResponse(UPDATED_RESPONSE);
        response.setCode(UPDATED_CODE);
        response.setDuration(UPDATED_DURATION);
        response.setEmailSent(UPDATED_EMAIL_SENT);
        response.setDate(UPDATED_DATE);
        response.setStackTrace(UPDATED_STACK_TRACE);
        restResponseMockMvc.perform(put("/api/responses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(response)))
                .andExpect(status().isOk());

        // Validate the Response in the database
        List<Response> responses = responseRepository.findAll();
        assertThat(responses).hasSize(databaseSizeBeforeUpdate);
        Response testResponse = responses.get(responses.size() - 1);
        assertThat(testResponse.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testResponse.getMessage()).isEqualTo(UPDATED_MESSAGE);
        assertThat(testResponse.getResponse()).isEqualTo(UPDATED_RESPONSE);
        assertThat(testResponse.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testResponse.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testResponse.getEmailSent()).isEqualTo(UPDATED_EMAIL_SENT);
        assertThat(testResponse.getDate().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_DATE);
        assertThat(testResponse.getStackTrace()).isEqualTo(UPDATED_STACK_TRACE);
    }

    @Test
    @Transactional
    public void deleteResponse() throws Exception {
        // Initialize the database
        responseRepository.saveAndFlush(response);

		int databaseSizeBeforeDelete = responseRepository.findAll().size();

        // Get the response
        restResponseMockMvc.perform(delete("/api/responses/{id}", response.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Response> responses = responseRepository.findAll();
        assertThat(responses).hasSize(databaseSizeBeforeDelete - 1);
    }
}
