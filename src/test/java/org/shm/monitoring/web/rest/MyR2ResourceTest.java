package org.shm.monitoring.web.rest;

import org.shm.monitoring.Application;
import org.shm.monitoring.domain.MyR2;
import org.shm.monitoring.repository.MyR2Repository;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the MyR2Resource REST controller.
 *
 * @see MyR2Resource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class MyR2ResourceTest {

    private static final String DEFAULT_MY_FIELD2 = "SAMPLE_TEXT";
    private static final String UPDATED_MY_FIELD2 = "UPDATED_TEXT";

    @Inject
    private MyR2Repository myR2Repository;

    private MockMvc restMyR2MockMvc;

    private MyR2 myR2;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MyR2Resource myR2Resource = new MyR2Resource();
        ReflectionTestUtils.setField(myR2Resource, "myR2Repository", myR2Repository);
        this.restMyR2MockMvc = MockMvcBuilders.standaloneSetup(myR2Resource).build();
    }

    @Before
    public void initTest() {
        myR2 = new MyR2();
        myR2.setMyField2(DEFAULT_MY_FIELD2);
    }

    @Test
    @Transactional
    public void createMyR2() throws Exception {
        int databaseSizeBeforeCreate = myR2Repository.findAll().size();

        // Create the MyR2
        restMyR2MockMvc.perform(post("/api/myR2s")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(myR2)))
                .andExpect(status().isCreated());

        // Validate the MyR2 in the database
        List<MyR2> myR2s = myR2Repository.findAll();
        assertThat(myR2s).hasSize(databaseSizeBeforeCreate + 1);
        MyR2 testMyR2 = myR2s.get(myR2s.size() - 1);
        assertThat(testMyR2.getMyField2()).isEqualTo(DEFAULT_MY_FIELD2);
    }

    @Test
    @Transactional
    public void getAllMyR2s() throws Exception {
        // Initialize the database
        myR2Repository.saveAndFlush(myR2);

        // Get all the myR2s
        restMyR2MockMvc.perform(get("/api/myR2s"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(myR2.getId().intValue())))
                .andExpect(jsonPath("$.[*].myField2").value(hasItem(DEFAULT_MY_FIELD2.toString())));
    }

    @Test
    @Transactional
    public void getMyR2() throws Exception {
        // Initialize the database
        myR2Repository.saveAndFlush(myR2);

        // Get the myR2
        restMyR2MockMvc.perform(get("/api/myR2s/{id}", myR2.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(myR2.getId().intValue()))
            .andExpect(jsonPath("$.myField2").value(DEFAULT_MY_FIELD2.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMyR2() throws Exception {
        // Get the myR2
        restMyR2MockMvc.perform(get("/api/myR2s/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMyR2() throws Exception {
        // Initialize the database
        myR2Repository.saveAndFlush(myR2);

		int databaseSizeBeforeUpdate = myR2Repository.findAll().size();

        // Update the myR2
        myR2.setMyField2(UPDATED_MY_FIELD2);
        restMyR2MockMvc.perform(put("/api/myR2s")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(myR2)))
                .andExpect(status().isOk());

        // Validate the MyR2 in the database
        List<MyR2> myR2s = myR2Repository.findAll();
        assertThat(myR2s).hasSize(databaseSizeBeforeUpdate);
        MyR2 testMyR2 = myR2s.get(myR2s.size() - 1);
        assertThat(testMyR2.getMyField2()).isEqualTo(UPDATED_MY_FIELD2);
    }

    @Test
    @Transactional
    public void deleteMyR2() throws Exception {
        // Initialize the database
        myR2Repository.saveAndFlush(myR2);

		int databaseSizeBeforeDelete = myR2Repository.findAll().size();

        // Get the myR2
        restMyR2MockMvc.perform(delete("/api/myR2s/{id}", myR2.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<MyR2> myR2s = myR2Repository.findAll();
        assertThat(myR2s).hasSize(databaseSizeBeforeDelete - 1);
    }
}
