package org.shm.monitoring.web.rest;

import org.shm.monitoring.Application;
import org.shm.monitoring.domain.MyR1;
import org.shm.monitoring.repository.MyR1Repository;

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
 * Test class for the MyR1Resource REST controller.
 *
 * @see MyR1Resource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class MyR1ResourceTest {

    private static final String DEFAULT_MY_FIELD = "SAMPLE_TEXT";
    private static final String UPDATED_MY_FIELD = "UPDATED_TEXT";

    @Inject
    private MyR1Repository myR1Repository;

    private MockMvc restMyR1MockMvc;

    private MyR1 myR1;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MyR1Resource myR1Resource = new MyR1Resource();
        ReflectionTestUtils.setField(myR1Resource, "myR1Repository", myR1Repository);
        this.restMyR1MockMvc = MockMvcBuilders.standaloneSetup(myR1Resource).build();
    }

    @Before
    public void initTest() {
        myR1 = new MyR1();
        myR1.setMyField(DEFAULT_MY_FIELD);
    }

    @Test
    @Transactional
    public void createMyR1() throws Exception {
        int databaseSizeBeforeCreate = myR1Repository.findAll().size();

        // Create the MyR1
        restMyR1MockMvc.perform(post("/api/myR1s")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(myR1)))
                .andExpect(status().isCreated());

        // Validate the MyR1 in the database
        List<MyR1> myR1s = myR1Repository.findAll();
        assertThat(myR1s).hasSize(databaseSizeBeforeCreate + 1);
        MyR1 testMyR1 = myR1s.get(myR1s.size() - 1);
        assertThat(testMyR1.getMyField()).isEqualTo(DEFAULT_MY_FIELD);
    }

    @Test
    @Transactional
    public void getAllMyR1s() throws Exception {
        // Initialize the database
        myR1Repository.saveAndFlush(myR1);

        // Get all the myR1s
        restMyR1MockMvc.perform(get("/api/myR1s"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(myR1.getId().intValue())))
                .andExpect(jsonPath("$.[*].myField").value(hasItem(DEFAULT_MY_FIELD.toString())));
    }

    @Test
    @Transactional
    public void getMyR1() throws Exception {
        // Initialize the database
        myR1Repository.saveAndFlush(myR1);

        // Get the myR1
        restMyR1MockMvc.perform(get("/api/myR1s/{id}", myR1.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(myR1.getId().intValue()))
            .andExpect(jsonPath("$.myField").value(DEFAULT_MY_FIELD.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMyR1() throws Exception {
        // Get the myR1
        restMyR1MockMvc.perform(get("/api/myR1s/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMyR1() throws Exception {
        // Initialize the database
        myR1Repository.saveAndFlush(myR1);

		int databaseSizeBeforeUpdate = myR1Repository.findAll().size();

        // Update the myR1
        myR1.setMyField(UPDATED_MY_FIELD);
        restMyR1MockMvc.perform(put("/api/myR1s")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(myR1)))
                .andExpect(status().isOk());

        // Validate the MyR1 in the database
        List<MyR1> myR1s = myR1Repository.findAll();
        assertThat(myR1s).hasSize(databaseSizeBeforeUpdate);
        MyR1 testMyR1 = myR1s.get(myR1s.size() - 1);
        assertThat(testMyR1.getMyField()).isEqualTo(UPDATED_MY_FIELD);
    }

    @Test
    @Transactional
    public void deleteMyR1() throws Exception {
        // Initialize the database
        myR1Repository.saveAndFlush(myR1);

		int databaseSizeBeforeDelete = myR1Repository.findAll().size();

        // Get the myR1
        restMyR1MockMvc.perform(delete("/api/myR1s/{id}", myR1.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<MyR1> myR1s = myR1Repository.findAll();
        assertThat(myR1s).hasSize(databaseSizeBeforeDelete - 1);
    }
}
