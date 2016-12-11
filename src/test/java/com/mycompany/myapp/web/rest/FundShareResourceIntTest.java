package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.FunduniversApp;

import com.mycompany.myapp.domain.FundShare;
import com.mycompany.myapp.repository.FundShareRepository;
import com.mycompany.myapp.service.FundShareService;
import com.mycompany.myapp.repository.search.FundShareSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the FundShareResource REST controller.
 *
 * @see FundShareResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FunduniversApp.class)
public class FundShareResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    @Inject
    private FundShareRepository fundShareRepository;

    @Inject
    private FundShareService fundShareService;

    @Inject
    private FundShareSearchRepository fundShareSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restFundShareMockMvc;

    private FundShare fundShare;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FundShareResource fundShareResource = new FundShareResource();
        ReflectionTestUtils.setField(fundShareResource, "fundShareService", fundShareService);
        this.restFundShareMockMvc = MockMvcBuilders.standaloneSetup(fundShareResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FundShare createEntity(EntityManager em) {
        FundShare fundShare = new FundShare()
                .name(DEFAULT_NAME);
        return fundShare;
    }

    @Before
    public void initTest() {
        fundShareSearchRepository.deleteAll();
        fundShare = createEntity(em);
    }

    @Test
    @Transactional
    public void createFundShare() throws Exception {
        int databaseSizeBeforeCreate = fundShareRepository.findAll().size();

        // Create the FundShare

        restFundShareMockMvc.perform(post("/api/fund-shares")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(fundShare)))
                .andExpect(status().isCreated());

        // Validate the FundShare in the database
        List<FundShare> fundShares = fundShareRepository.findAll();
        assertThat(fundShares).hasSize(databaseSizeBeforeCreate + 1);
        FundShare testFundShare = fundShares.get(fundShares.size() - 1);
        assertThat(testFundShare.getName()).isEqualTo(DEFAULT_NAME);

        // Validate the FundShare in ElasticSearch
        FundShare fundShareEs = fundShareSearchRepository.findOne(testFundShare.getId());
        assertThat(fundShareEs).isEqualToComparingFieldByField(testFundShare);
    }

    @Test
    @Transactional
    public void getAllFundShares() throws Exception {
        // Initialize the database
        fundShareRepository.saveAndFlush(fundShare);

        // Get all the fundShares
        restFundShareMockMvc.perform(get("/api/fund-shares?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(fundShare.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getFundShare() throws Exception {
        // Initialize the database
        fundShareRepository.saveAndFlush(fundShare);

        // Get the fundShare
        restFundShareMockMvc.perform(get("/api/fund-shares/{id}", fundShare.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(fundShare.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingFundShare() throws Exception {
        // Get the fundShare
        restFundShareMockMvc.perform(get("/api/fund-shares/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFundShare() throws Exception {
        // Initialize the database
        fundShareService.save(fundShare);

        int databaseSizeBeforeUpdate = fundShareRepository.findAll().size();

        // Update the fundShare
        FundShare updatedFundShare = fundShareRepository.findOne(fundShare.getId());
        updatedFundShare
                .name(UPDATED_NAME);

        restFundShareMockMvc.perform(put("/api/fund-shares")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedFundShare)))
                .andExpect(status().isOk());

        // Validate the FundShare in the database
        List<FundShare> fundShares = fundShareRepository.findAll();
        assertThat(fundShares).hasSize(databaseSizeBeforeUpdate);
        FundShare testFundShare = fundShares.get(fundShares.size() - 1);
        assertThat(testFundShare.getName()).isEqualTo(UPDATED_NAME);

        // Validate the FundShare in ElasticSearch
        FundShare fundShareEs = fundShareSearchRepository.findOne(testFundShare.getId());
        assertThat(fundShareEs).isEqualToComparingFieldByField(testFundShare);
    }

    @Test
    @Transactional
    public void deleteFundShare() throws Exception {
        // Initialize the database
        fundShareService.save(fundShare);

        int databaseSizeBeforeDelete = fundShareRepository.findAll().size();

        // Get the fundShare
        restFundShareMockMvc.perform(delete("/api/fund-shares/{id}", fundShare.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean fundShareExistsInEs = fundShareSearchRepository.exists(fundShare.getId());
        assertThat(fundShareExistsInEs).isFalse();

        // Validate the database is empty
        List<FundShare> fundShares = fundShareRepository.findAll();
        assertThat(fundShares).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchFundShare() throws Exception {
        // Initialize the database
        fundShareService.save(fundShare);

        // Search the fundShare
        restFundShareMockMvc.perform(get("/api/_search/fund-shares?query=id:" + fundShare.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fundShare.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
}
