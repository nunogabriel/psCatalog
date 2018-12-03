package com.cgi.pscatalog.web.rest;

import com.cgi.pscatalog.PsCatalogApp;

import com.cgi.pscatalog.domain.Suppliers;
import com.cgi.pscatalog.repository.SuppliersRepository;
import com.cgi.pscatalog.repository.search.SuppliersSearchRepository;
import com.cgi.pscatalog.service.SuppliersService;
import com.cgi.pscatalog.service.dto.SuppliersDTO;
import com.cgi.pscatalog.service.mapper.SuppliersMapper;
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
 * Test class for the SuppliersResource REST controller.
 *
 * @see SuppliersResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PsCatalogApp.class)
public class SuppliersResourceIntTest {

    private static final String DEFAULT_SUPPLIER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SUPPLIER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SUPPLIER_NIF = "AAAAAAAAAA";
    private static final String UPDATED_SUPPLIER_NIF = "BBBBBBBBBB";

    private static final String DEFAULT_SUPPLIER_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_SUPPLIER_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_SUPPLIER_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_SUPPLIER_PHONE = "BBBBBBBBBB";

    private static final Instant DEFAULT_SUPPLIER_BEGIN_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SUPPLIER_BEGIN_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_SUPPLIER_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SUPPLIER_END_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private SuppliersRepository suppliersRepository;

    @Autowired
    private SuppliersMapper suppliersMapper;

    @Autowired
    private SuppliersService suppliersService;

    /**
     * This repository is mocked in the com.cgi.pscatalog.repository.search test package.
     *
     * @see com.cgi.pscatalog.repository.search.SuppliersSearchRepositoryMockConfiguration
     */
    @Autowired
    private SuppliersSearchRepository mockSuppliersSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSuppliersMockMvc;

    private Suppliers suppliers;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SuppliersResource suppliersResource = new SuppliersResource(suppliersService);
        this.restSuppliersMockMvc = MockMvcBuilders.standaloneSetup(suppliersResource)
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
    public static Suppliers createEntity(EntityManager em) {
        Suppliers suppliers = new Suppliers()
            .supplierName(DEFAULT_SUPPLIER_NAME)
            .supplierNif(DEFAULT_SUPPLIER_NIF)
            .supplierEmail(DEFAULT_SUPPLIER_EMAIL)
            .supplierPhone(DEFAULT_SUPPLIER_PHONE)
            .supplierBeginDate(DEFAULT_SUPPLIER_BEGIN_DATE)
            .supplierEndDate(DEFAULT_SUPPLIER_END_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return suppliers;
    }

    @Before
    public void initTest() {
        suppliers = createEntity(em);
    }

    @Test
    @Transactional
    public void createSuppliers() throws Exception {
        int databaseSizeBeforeCreate = suppliersRepository.findAll().size();

        // Create the Suppliers
        SuppliersDTO suppliersDTO = suppliersMapper.toDto(suppliers);
        restSuppliersMockMvc.perform(post("/api/suppliers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(suppliersDTO)))
            .andExpect(status().isCreated());

        // Validate the Suppliers in the database
        List<Suppliers> suppliersList = suppliersRepository.findAll();
        assertThat(suppliersList).hasSize(databaseSizeBeforeCreate + 1);
        Suppliers testSuppliers = suppliersList.get(suppliersList.size() - 1);
        assertThat(testSuppliers.getSupplierName()).isEqualTo(DEFAULT_SUPPLIER_NAME);
        assertThat(testSuppliers.getSupplierNif()).isEqualTo(DEFAULT_SUPPLIER_NIF);
        assertThat(testSuppliers.getSupplierEmail()).isEqualTo(DEFAULT_SUPPLIER_EMAIL);
        assertThat(testSuppliers.getSupplierPhone()).isEqualTo(DEFAULT_SUPPLIER_PHONE);
        assertThat(testSuppliers.getSupplierBeginDate()).isEqualTo(DEFAULT_SUPPLIER_BEGIN_DATE);
        assertThat(testSuppliers.getSupplierEndDate()).isEqualTo(DEFAULT_SUPPLIER_END_DATE);
        assertThat(testSuppliers.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testSuppliers.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testSuppliers.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testSuppliers.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);

        // Validate the Suppliers in Elasticsearch
        verify(mockSuppliersSearchRepository, times(1)).save(testSuppliers);
    }

    @Test
    @Transactional
    public void createSuppliersWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = suppliersRepository.findAll().size();

        // Create the Suppliers with an existing ID
        suppliers.setId(1L);
        SuppliersDTO suppliersDTO = suppliersMapper.toDto(suppliers);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSuppliersMockMvc.perform(post("/api/suppliers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(suppliersDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Suppliers in the database
        List<Suppliers> suppliersList = suppliersRepository.findAll();
        assertThat(suppliersList).hasSize(databaseSizeBeforeCreate);

        // Validate the Suppliers in Elasticsearch
        verify(mockSuppliersSearchRepository, times(0)).save(suppliers);
    }

    @Test
    @Transactional
    public void checkSupplierNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = suppliersRepository.findAll().size();
        // set the field null
        suppliers.setSupplierName(null);

        // Create the Suppliers, which fails.
        SuppliersDTO suppliersDTO = suppliersMapper.toDto(suppliers);

        restSuppliersMockMvc.perform(post("/api/suppliers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(suppliersDTO)))
            .andExpect(status().isBadRequest());

        List<Suppliers> suppliersList = suppliersRepository.findAll();
        assertThat(suppliersList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSupplierNifIsRequired() throws Exception {
        int databaseSizeBeforeTest = suppliersRepository.findAll().size();
        // set the field null
        suppliers.setSupplierNif(null);

        // Create the Suppliers, which fails.
        SuppliersDTO suppliersDTO = suppliersMapper.toDto(suppliers);

        restSuppliersMockMvc.perform(post("/api/suppliers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(suppliersDTO)))
            .andExpect(status().isBadRequest());

        List<Suppliers> suppliersList = suppliersRepository.findAll();
        assertThat(suppliersList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSupplierEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = suppliersRepository.findAll().size();
        // set the field null
        suppliers.setSupplierEmail(null);

        // Create the Suppliers, which fails.
        SuppliersDTO suppliersDTO = suppliersMapper.toDto(suppliers);

        restSuppliersMockMvc.perform(post("/api/suppliers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(suppliersDTO)))
            .andExpect(status().isBadRequest());

        List<Suppliers> suppliersList = suppliersRepository.findAll();
        assertThat(suppliersList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSupplierPhoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = suppliersRepository.findAll().size();
        // set the field null
        suppliers.setSupplierPhone(null);

        // Create the Suppliers, which fails.
        SuppliersDTO suppliersDTO = suppliersMapper.toDto(suppliers);

        restSuppliersMockMvc.perform(post("/api/suppliers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(suppliersDTO)))
            .andExpect(status().isBadRequest());

        List<Suppliers> suppliersList = suppliersRepository.findAll();
        assertThat(suppliersList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSupplierBeginDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = suppliersRepository.findAll().size();
        // set the field null
        suppliers.setSupplierBeginDate(null);

        // Create the Suppliers, which fails.
        SuppliersDTO suppliersDTO = suppliersMapper.toDto(suppliers);

        restSuppliersMockMvc.perform(post("/api/suppliers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(suppliersDTO)))
            .andExpect(status().isBadRequest());

        List<Suppliers> suppliersList = suppliersRepository.findAll();
        assertThat(suppliersList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSuppliers() throws Exception {
        // Initialize the database
        suppliersRepository.saveAndFlush(suppliers);

        // Get all the suppliersList
        restSuppliersMockMvc.perform(get("/api/suppliers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(suppliers.getId().intValue())))
            .andExpect(jsonPath("$.[*].supplierName").value(hasItem(DEFAULT_SUPPLIER_NAME.toString())))
            .andExpect(jsonPath("$.[*].supplierNif").value(hasItem(DEFAULT_SUPPLIER_NIF.toString())))
            .andExpect(jsonPath("$.[*].supplierEmail").value(hasItem(DEFAULT_SUPPLIER_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].supplierPhone").value(hasItem(DEFAULT_SUPPLIER_PHONE.toString())))
            .andExpect(jsonPath("$.[*].supplierBeginDate").value(hasItem(DEFAULT_SUPPLIER_BEGIN_DATE.toString())))
            .andExpect(jsonPath("$.[*].supplierEndDate").value(hasItem(DEFAULT_SUPPLIER_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getSuppliers() throws Exception {
        // Initialize the database
        suppliersRepository.saveAndFlush(suppliers);

        // Get the suppliers
        restSuppliersMockMvc.perform(get("/api/suppliers/{id}", suppliers.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(suppliers.getId().intValue()))
            .andExpect(jsonPath("$.supplierName").value(DEFAULT_SUPPLIER_NAME.toString()))
            .andExpect(jsonPath("$.supplierNif").value(DEFAULT_SUPPLIER_NIF.toString()))
            .andExpect(jsonPath("$.supplierEmail").value(DEFAULT_SUPPLIER_EMAIL.toString()))
            .andExpect(jsonPath("$.supplierPhone").value(DEFAULT_SUPPLIER_PHONE.toString()))
            .andExpect(jsonPath("$.supplierBeginDate").value(DEFAULT_SUPPLIER_BEGIN_DATE.toString()))
            .andExpect(jsonPath("$.supplierEndDate").value(DEFAULT_SUPPLIER_END_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSuppliers() throws Exception {
        // Get the suppliers
        restSuppliersMockMvc.perform(get("/api/suppliers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSuppliers() throws Exception {
        // Initialize the database
        suppliersRepository.saveAndFlush(suppliers);

        int databaseSizeBeforeUpdate = suppliersRepository.findAll().size();

        // Update the suppliers
        Suppliers updatedSuppliers = suppliersRepository.findById(suppliers.getId()).get();
        // Disconnect from session so that the updates on updatedSuppliers are not directly saved in db
        em.detach(updatedSuppliers);
        updatedSuppliers
            .supplierName(UPDATED_SUPPLIER_NAME)
            .supplierNif(UPDATED_SUPPLIER_NIF)
            .supplierEmail(UPDATED_SUPPLIER_EMAIL)
            .supplierPhone(UPDATED_SUPPLIER_PHONE)
            .supplierBeginDate(UPDATED_SUPPLIER_BEGIN_DATE)
            .supplierEndDate(UPDATED_SUPPLIER_END_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        SuppliersDTO suppliersDTO = suppliersMapper.toDto(updatedSuppliers);

        restSuppliersMockMvc.perform(put("/api/suppliers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(suppliersDTO)))
            .andExpect(status().isOk());

        // Validate the Suppliers in the database
        List<Suppliers> suppliersList = suppliersRepository.findAll();
        assertThat(suppliersList).hasSize(databaseSizeBeforeUpdate);
        Suppliers testSuppliers = suppliersList.get(suppliersList.size() - 1);
        assertThat(testSuppliers.getSupplierName()).isEqualTo(UPDATED_SUPPLIER_NAME);
        assertThat(testSuppliers.getSupplierNif()).isEqualTo(UPDATED_SUPPLIER_NIF);
        assertThat(testSuppliers.getSupplierEmail()).isEqualTo(UPDATED_SUPPLIER_EMAIL);
        assertThat(testSuppliers.getSupplierPhone()).isEqualTo(UPDATED_SUPPLIER_PHONE);
        assertThat(testSuppliers.getSupplierBeginDate()).isEqualTo(UPDATED_SUPPLIER_BEGIN_DATE);
        assertThat(testSuppliers.getSupplierEndDate()).isEqualTo(UPDATED_SUPPLIER_END_DATE);
        assertThat(testSuppliers.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testSuppliers.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testSuppliers.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testSuppliers.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);

        // Validate the Suppliers in Elasticsearch
        verify(mockSuppliersSearchRepository, times(1)).save(testSuppliers);
    }

    @Test
    @Transactional
    public void updateNonExistingSuppliers() throws Exception {
        int databaseSizeBeforeUpdate = suppliersRepository.findAll().size();

        // Create the Suppliers
        SuppliersDTO suppliersDTO = suppliersMapper.toDto(suppliers);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSuppliersMockMvc.perform(put("/api/suppliers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(suppliersDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Suppliers in the database
        List<Suppliers> suppliersList = suppliersRepository.findAll();
        assertThat(suppliersList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Suppliers in Elasticsearch
        verify(mockSuppliersSearchRepository, times(0)).save(suppliers);
    }

    @Test
    @Transactional
    public void deleteSuppliers() throws Exception {
        // Initialize the database
        suppliersRepository.saveAndFlush(suppliers);

        int databaseSizeBeforeDelete = suppliersRepository.findAll().size();

        // Get the suppliers
        restSuppliersMockMvc.perform(delete("/api/suppliers/{id}", suppliers.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Suppliers> suppliersList = suppliersRepository.findAll();
        assertThat(suppliersList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Suppliers in Elasticsearch
        verify(mockSuppliersSearchRepository, times(1)).deleteById(suppliers.getId());
    }

    @Test
    @Transactional
    public void searchSuppliers() throws Exception {
        // Initialize the database
        suppliersRepository.saveAndFlush(suppliers);
        when(mockSuppliersSearchRepository.search(queryStringQuery("id:" + suppliers.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(suppliers), PageRequest.of(0, 1), 1));
        // Search the suppliers
        restSuppliersMockMvc.perform(get("/api/_search/suppliers?query=id:" + suppliers.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(suppliers.getId().intValue())))
            .andExpect(jsonPath("$.[*].supplierName").value(hasItem(DEFAULT_SUPPLIER_NAME)))
            .andExpect(jsonPath("$.[*].supplierNif").value(hasItem(DEFAULT_SUPPLIER_NIF)))
            .andExpect(jsonPath("$.[*].supplierEmail").value(hasItem(DEFAULT_SUPPLIER_EMAIL)))
            .andExpect(jsonPath("$.[*].supplierPhone").value(hasItem(DEFAULT_SUPPLIER_PHONE)))
            .andExpect(jsonPath("$.[*].supplierBeginDate").value(hasItem(DEFAULT_SUPPLIER_BEGIN_DATE.toString())))
            .andExpect(jsonPath("$.[*].supplierEndDate").value(hasItem(DEFAULT_SUPPLIER_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Suppliers.class);
        Suppliers suppliers1 = new Suppliers();
        suppliers1.setId(1L);
        Suppliers suppliers2 = new Suppliers();
        suppliers2.setId(suppliers1.getId());
        assertThat(suppliers1).isEqualTo(suppliers2);
        suppliers2.setId(2L);
        assertThat(suppliers1).isNotEqualTo(suppliers2);
        suppliers1.setId(null);
        assertThat(suppliers1).isNotEqualTo(suppliers2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SuppliersDTO.class);
        SuppliersDTO suppliersDTO1 = new SuppliersDTO();
        suppliersDTO1.setId(1L);
        SuppliersDTO suppliersDTO2 = new SuppliersDTO();
        assertThat(suppliersDTO1).isNotEqualTo(suppliersDTO2);
        suppliersDTO2.setId(suppliersDTO1.getId());
        assertThat(suppliersDTO1).isEqualTo(suppliersDTO2);
        suppliersDTO2.setId(2L);
        assertThat(suppliersDTO1).isNotEqualTo(suppliersDTO2);
        suppliersDTO1.setId(null);
        assertThat(suppliersDTO1).isNotEqualTo(suppliersDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(suppliersMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(suppliersMapper.fromId(null)).isNull();
    }
}
