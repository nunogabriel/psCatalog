package com.cgi.pscatalog.web.rest;

import com.cgi.pscatalog.PsCatalogApp;

import com.cgi.pscatalog.domain.Promotions;
import com.cgi.pscatalog.repository.PromotionsRepository;
import com.cgi.pscatalog.repository.search.PromotionsSearchRepository;
import com.cgi.pscatalog.service.PromotionsService;
import com.cgi.pscatalog.service.dto.PromotionsDTO;
import com.cgi.pscatalog.service.mapper.PromotionsMapper;
import com.cgi.pscatalog.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;


import static com.cgi.pscatalog.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the PromotionsResource REST controller.
 *
 * @see PromotionsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PsCatalogApp.class)
public class PromotionsResourceIntTest {

    private static final BigDecimal DEFAULT_NEW_PRODUCT_PRICE = new BigDecimal("1");
    private static final BigDecimal UPDATED_NEW_PRODUCT_PRICE = new BigDecimal("2");

    private static final Instant DEFAULT_PROMOTION_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PROMOTION_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_PROMOTION_EXPIRY_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PROMOTION_EXPIRY_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private PromotionsRepository promotionsRepository;

    @Autowired
    private PromotionsMapper promotionsMapper;

    @Autowired
    private PromotionsService promotionsService;

    /**
     * This repository is mocked in the com.cgi.pscatalog.repository.search test package.
     *
     * @see com.cgi.pscatalog.repository.search.PromotionsSearchRepositoryMockConfiguration
     */
    @Autowired
    private PromotionsSearchRepository mockPromotionsSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPromotionsMockMvc;

    private Promotions promotions;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PromotionsResource promotionsResource = new PromotionsResource(promotionsService);
        this.restPromotionsMockMvc = MockMvcBuilders.standaloneSetup(promotionsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Promotions createEntity(EntityManager em) {
        Promotions promotions = new Promotions()
            .newProductPrice(DEFAULT_NEW_PRODUCT_PRICE)
            .promotionStartDate(DEFAULT_PROMOTION_START_DATE)
            .promotionExpiryDate(DEFAULT_PROMOTION_EXPIRY_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return promotions;
    }

    @Before
    public void initTest() {
        promotions = createEntity(em);
    }

    @Test
    @Transactional
    public void createPromotions() throws Exception {
        int databaseSizeBeforeCreate = promotionsRepository.findAll().size();

        // Create the Promotions
        PromotionsDTO promotionsDTO = promotionsMapper.toDto(promotions);
        restPromotionsMockMvc.perform(post("/api/promotions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(promotionsDTO)))
            .andExpect(status().isCreated());

        // Validate the Promotions in the database
        List<Promotions> promotionsList = promotionsRepository.findAll();
        assertThat(promotionsList).hasSize(databaseSizeBeforeCreate + 1);
        Promotions testPromotions = promotionsList.get(promotionsList.size() - 1);
        assertThat(testPromotions.getNewProductPrice()).isEqualTo(DEFAULT_NEW_PRODUCT_PRICE);
        assertThat(testPromotions.getPromotionStartDate()).isEqualTo(DEFAULT_PROMOTION_START_DATE);
        assertThat(testPromotions.getPromotionExpiryDate()).isEqualTo(DEFAULT_PROMOTION_EXPIRY_DATE);
        assertThat(testPromotions.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testPromotions.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testPromotions.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testPromotions.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);

        // Validate the Promotions in Elasticsearch
        verify(mockPromotionsSearchRepository, times(1)).save(testPromotions);
    }

    @Test
    @Transactional
    public void createPromotionsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = promotionsRepository.findAll().size();

        // Create the Promotions with an existing ID
        promotions.setId(1L);
        PromotionsDTO promotionsDTO = promotionsMapper.toDto(promotions);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPromotionsMockMvc.perform(post("/api/promotions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(promotionsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Promotions in the database
        List<Promotions> promotionsList = promotionsRepository.findAll();
        assertThat(promotionsList).hasSize(databaseSizeBeforeCreate);

        // Validate the Promotions in Elasticsearch
        verify(mockPromotionsSearchRepository, times(0)).save(promotions);
    }

    @Test
    @Transactional
    public void checkNewProductPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = promotionsRepository.findAll().size();
        // set the field null
        promotions.setNewProductPrice(null);

        // Create the Promotions, which fails.
        PromotionsDTO promotionsDTO = promotionsMapper.toDto(promotions);

        restPromotionsMockMvc.perform(post("/api/promotions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(promotionsDTO)))
            .andExpect(status().isBadRequest());

        List<Promotions> promotionsList = promotionsRepository.findAll();
        assertThat(promotionsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPromotionStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = promotionsRepository.findAll().size();
        // set the field null
        promotions.setPromotionStartDate(null);

        // Create the Promotions, which fails.
        PromotionsDTO promotionsDTO = promotionsMapper.toDto(promotions);

        restPromotionsMockMvc.perform(post("/api/promotions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(promotionsDTO)))
            .andExpect(status().isBadRequest());

        List<Promotions> promotionsList = promotionsRepository.findAll();
        assertThat(promotionsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPromotions() throws Exception {
        // Initialize the database
        promotionsRepository.saveAndFlush(promotions);

        // Get all the promotionsList
        restPromotionsMockMvc.perform(get("/api/promotions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(promotions.getId().intValue())))
            .andExpect(jsonPath("$.[*].newProductPrice").value(hasItem(DEFAULT_NEW_PRODUCT_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].promotionStartDate").value(hasItem(DEFAULT_PROMOTION_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].promotionExpiryDate").value(hasItem(DEFAULT_PROMOTION_EXPIRY_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getPromotions() throws Exception {
        // Initialize the database
        promotionsRepository.saveAndFlush(promotions);

        // Get the promotions
        restPromotionsMockMvc.perform(get("/api/promotions/{id}", promotions.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(promotions.getId().intValue()))
            .andExpect(jsonPath("$.newProductPrice").value(DEFAULT_NEW_PRODUCT_PRICE.intValue()))
            .andExpect(jsonPath("$.promotionStartDate").value(DEFAULT_PROMOTION_START_DATE.toString()))
            .andExpect(jsonPath("$.promotionExpiryDate").value(DEFAULT_PROMOTION_EXPIRY_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPromotions() throws Exception {
        // Get the promotions
        restPromotionsMockMvc.perform(get("/api/promotions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePromotions() throws Exception {
        // Initialize the database
        promotionsRepository.saveAndFlush(promotions);

        int databaseSizeBeforeUpdate = promotionsRepository.findAll().size();

        // Update the promotions
        Promotions updatedPromotions = promotionsRepository.findById(promotions.getId()).get();
        // Disconnect from session so that the updates on updatedPromotions are not directly saved in db
        em.detach(updatedPromotions);
        updatedPromotions
            .newProductPrice(UPDATED_NEW_PRODUCT_PRICE)
            .promotionStartDate(UPDATED_PROMOTION_START_DATE)
            .promotionExpiryDate(UPDATED_PROMOTION_EXPIRY_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        PromotionsDTO promotionsDTO = promotionsMapper.toDto(updatedPromotions);

        restPromotionsMockMvc.perform(put("/api/promotions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(promotionsDTO)))
            .andExpect(status().isOk());

        // Validate the Promotions in the database
        List<Promotions> promotionsList = promotionsRepository.findAll();
        assertThat(promotionsList).hasSize(databaseSizeBeforeUpdate);
        Promotions testPromotions = promotionsList.get(promotionsList.size() - 1);
        assertThat(testPromotions.getNewProductPrice()).isEqualTo(UPDATED_NEW_PRODUCT_PRICE);
        assertThat(testPromotions.getPromotionStartDate()).isEqualTo(UPDATED_PROMOTION_START_DATE);
        assertThat(testPromotions.getPromotionExpiryDate()).isEqualTo(UPDATED_PROMOTION_EXPIRY_DATE);
        assertThat(testPromotions.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testPromotions.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testPromotions.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testPromotions.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);

        // Validate the Promotions in Elasticsearch
        verify(mockPromotionsSearchRepository, times(1)).save(testPromotions);
    }

    @Test
    @Transactional
    public void updateNonExistingPromotions() throws Exception {
        int databaseSizeBeforeUpdate = promotionsRepository.findAll().size();

        // Create the Promotions
        PromotionsDTO promotionsDTO = promotionsMapper.toDto(promotions);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPromotionsMockMvc.perform(put("/api/promotions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(promotionsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Promotions in the database
        List<Promotions> promotionsList = promotionsRepository.findAll();
        assertThat(promotionsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Promotions in Elasticsearch
        verify(mockPromotionsSearchRepository, times(0)).save(promotions);
    }

    @Test
    @Transactional
    public void deletePromotions() throws Exception {
        // Initialize the database
        promotionsRepository.saveAndFlush(promotions);

        int databaseSizeBeforeDelete = promotionsRepository.findAll().size();

        // Get the promotions
        restPromotionsMockMvc.perform(delete("/api/promotions/{id}", promotions.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Promotions> promotionsList = promotionsRepository.findAll();
        assertThat(promotionsList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Promotions in Elasticsearch
        verify(mockPromotionsSearchRepository, times(1)).deleteById(promotions.getId());
    }

    @Test
    @Transactional
    public void searchPromotions() throws Exception {
        // Initialize the database
        promotionsRepository.saveAndFlush(promotions);
        when(mockPromotionsSearchRepository.search(queryStringQuery("id:" + promotions.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(promotions), PageRequest.of(0, 1), 1));
        // Search the promotions
        restPromotionsMockMvc.perform(get("/api/_search/promotions?query=id:" + promotions.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(promotions.getId().intValue())))
            .andExpect(jsonPath("$.[*].newProductPrice").value(hasItem(DEFAULT_NEW_PRODUCT_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].promotionStartDate").value(hasItem(DEFAULT_PROMOTION_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].promotionExpiryDate").value(hasItem(DEFAULT_PROMOTION_EXPIRY_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Promotions.class);
        Promotions promotions1 = new Promotions();
        promotions1.setId(1L);
        Promotions promotions2 = new Promotions();
        promotions2.setId(promotions1.getId());
        assertThat(promotions1).isEqualTo(promotions2);
        promotions2.setId(2L);
        assertThat(promotions1).isNotEqualTo(promotions2);
        promotions1.setId(null);
        assertThat(promotions1).isNotEqualTo(promotions2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PromotionsDTO.class);
        PromotionsDTO promotionsDTO1 = new PromotionsDTO();
        promotionsDTO1.setId(1L);
        PromotionsDTO promotionsDTO2 = new PromotionsDTO();
        assertThat(promotionsDTO1).isNotEqualTo(promotionsDTO2);
        promotionsDTO2.setId(promotionsDTO1.getId());
        assertThat(promotionsDTO1).isEqualTo(promotionsDTO2);
        promotionsDTO2.setId(2L);
        assertThat(promotionsDTO1).isNotEqualTo(promotionsDTO2);
        promotionsDTO1.setId(null);
        assertThat(promotionsDTO1).isNotEqualTo(promotionsDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(promotionsMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(promotionsMapper.fromId(null)).isNull();
    }
}
