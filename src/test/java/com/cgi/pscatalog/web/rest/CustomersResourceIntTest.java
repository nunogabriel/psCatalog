package com.cgi.pscatalog.web.rest;

import com.cgi.pscatalog.PsCatalogApp;

import com.cgi.pscatalog.domain.Customers;
import com.cgi.pscatalog.repository.CustomersRepository;
import com.cgi.pscatalog.repository.search.CustomersSearchRepository;
import com.cgi.pscatalog.service.CustomersService;
import com.cgi.pscatalog.service.dto.CustomersDTO;
import com.cgi.pscatalog.service.mapper.CustomersMapper;
import com.cgi.pscatalog.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import static com.cgi.pscatalog.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.cgi.pscatalog.domain.enumeration.GenderEnum;
/**
 * Test class for the CustomersResource REST controller.
 *
 * @see CustomersResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PsCatalogApp.class)
public class CustomersResourceIntTest {

    private static final String DEFAULT_CUSTOMER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOMER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CUSTOMER_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOMER_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_CUSTOMER_NIF = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOMER_NIF = "BBBBBBBBBB";

    private static final String DEFAULT_CUSTOMER_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOMER_PHONE = "BBBBBBBBBB";

    private static final GenderEnum DEFAULT_CUSTOMER_GENDER = GenderEnum.MASCULINO;
    private static final GenderEnum UPDATED_CUSTOMER_GENDER = GenderEnum.FEMININO;

    private static final Instant DEFAULT_CUSTOMER_BEGIN_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CUSTOMER_BEGIN_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_CUSTOMER_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CUSTOMER_END_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LOGIN = "AAAAAAAAAA";
    private static final String UPDATED_LOGIN = "BBBBBBBBBB";

    @Autowired
    private CustomersRepository customersRepository;

    @Mock
    private CustomersRepository customersRepositoryMock;

    @Autowired
    private CustomersMapper customersMapper;

    @Mock
    private CustomersService customersServiceMock;

    @Autowired
    private CustomersService customersService;

    /**
     * This repository is mocked in the com.cgi.pscatalog.repository.search test package.
     *
     * @see com.cgi.pscatalog.repository.search.CustomersSearchRepositoryMockConfiguration
     */
    @Autowired
    private CustomersSearchRepository mockCustomersSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCustomersMockMvc;

    private Customers customers;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CustomersResource customersResource = new CustomersResource(customersService);
        this.restCustomersMockMvc = MockMvcBuilders.standaloneSetup(customersResource)
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
    public static Customers createEntity(EntityManager em) {
        Customers customers = new Customers()
            .customerName(DEFAULT_CUSTOMER_NAME)
            .customerEmail(DEFAULT_CUSTOMER_EMAIL)
            .customerNif(DEFAULT_CUSTOMER_NIF)
            .customerPhone(DEFAULT_CUSTOMER_PHONE)
            .customerGender(DEFAULT_CUSTOMER_GENDER)
            .customerBeginDate(DEFAULT_CUSTOMER_BEGIN_DATE)
            .customerEndDate(DEFAULT_CUSTOMER_END_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .login(DEFAULT_LOGIN);
        return customers;
    }

    @Before
    public void initTest() {
        customers = createEntity(em);
    }

    @Test
    @Transactional
    public void createCustomers() throws Exception {
        int databaseSizeBeforeCreate = customersRepository.findAll().size();

        // Create the Customers
        CustomersDTO customersDTO = customersMapper.toDto(customers);
        restCustomersMockMvc.perform(post("/api/customers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customersDTO)))
            .andExpect(status().isCreated());

        // Validate the Customers in the database
        List<Customers> customersList = customersRepository.findAll();
        assertThat(customersList).hasSize(databaseSizeBeforeCreate + 1);
        Customers testCustomers = customersList.get(customersList.size() - 1);
        assertThat(testCustomers.getCustomerName()).isEqualTo(DEFAULT_CUSTOMER_NAME);
        assertThat(testCustomers.getCustomerEmail()).isEqualTo(DEFAULT_CUSTOMER_EMAIL);
        assertThat(testCustomers.getCustomerNif()).isEqualTo(DEFAULT_CUSTOMER_NIF);
        assertThat(testCustomers.getCustomerPhone()).isEqualTo(DEFAULT_CUSTOMER_PHONE);
        assertThat(testCustomers.getCustomerGender()).isEqualTo(DEFAULT_CUSTOMER_GENDER);
        assertThat(testCustomers.getCustomerBeginDate()).isEqualTo(DEFAULT_CUSTOMER_BEGIN_DATE);
        assertThat(testCustomers.getCustomerEndDate()).isEqualTo(DEFAULT_CUSTOMER_END_DATE);
        assertThat(testCustomers.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testCustomers.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testCustomers.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testCustomers.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testCustomers.getLogin()).isEqualTo(DEFAULT_LOGIN);

        // Validate the Customers in Elasticsearch
        verify(mockCustomersSearchRepository, times(1)).save(testCustomers);
    }

    @Test
    @Transactional
    public void createCustomersWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = customersRepository.findAll().size();

        // Create the Customers with an existing ID
        customers.setId(1L);
        CustomersDTO customersDTO = customersMapper.toDto(customers);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustomersMockMvc.perform(post("/api/customers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customersDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Customers in the database
        List<Customers> customersList = customersRepository.findAll();
        assertThat(customersList).hasSize(databaseSizeBeforeCreate);

        // Validate the Customers in Elasticsearch
        verify(mockCustomersSearchRepository, times(0)).save(customers);
    }

    @Test
    @Transactional
    public void checkCustomerNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = customersRepository.findAll().size();
        // set the field null
        customers.setCustomerName(null);

        // Create the Customers, which fails.
        CustomersDTO customersDTO = customersMapper.toDto(customers);

        restCustomersMockMvc.perform(post("/api/customers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customersDTO)))
            .andExpect(status().isBadRequest());

        List<Customers> customersList = customersRepository.findAll();
        assertThat(customersList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCustomerEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = customersRepository.findAll().size();
        // set the field null
        customers.setCustomerEmail(null);

        // Create the Customers, which fails.
        CustomersDTO customersDTO = customersMapper.toDto(customers);

        restCustomersMockMvc.perform(post("/api/customers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customersDTO)))
            .andExpect(status().isBadRequest());

        List<Customers> customersList = customersRepository.findAll();
        assertThat(customersList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCustomerBeginDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = customersRepository.findAll().size();
        // set the field null
        customers.setCustomerBeginDate(null);

        // Create the Customers, which fails.
        CustomersDTO customersDTO = customersMapper.toDto(customers);

        restCustomersMockMvc.perform(post("/api/customers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customersDTO)))
            .andExpect(status().isBadRequest());

        List<Customers> customersList = customersRepository.findAll();
        assertThat(customersList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLoginIsRequired() throws Exception {
        int databaseSizeBeforeTest = customersRepository.findAll().size();
        // set the field null
        customers.setLogin(null);

        // Create the Customers, which fails.
        CustomersDTO customersDTO = customersMapper.toDto(customers);

        restCustomersMockMvc.perform(post("/api/customers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customersDTO)))
            .andExpect(status().isBadRequest());

        List<Customers> customersList = customersRepository.findAll();
        assertThat(customersList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCustomers() throws Exception {
        // Initialize the database
        customersRepository.saveAndFlush(customers);

        // Get all the customersList
        restCustomersMockMvc.perform(get("/api/customers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customers.getId().intValue())))
            .andExpect(jsonPath("$.[*].customerName").value(hasItem(DEFAULT_CUSTOMER_NAME.toString())))
            .andExpect(jsonPath("$.[*].customerEmail").value(hasItem(DEFAULT_CUSTOMER_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].customerNif").value(hasItem(DEFAULT_CUSTOMER_NIF.toString())))
            .andExpect(jsonPath("$.[*].customerPhone").value(hasItem(DEFAULT_CUSTOMER_PHONE.toString())))
            .andExpect(jsonPath("$.[*].customerGender").value(hasItem(DEFAULT_CUSTOMER_GENDER.toString())))
            .andExpect(jsonPath("$.[*].customerBeginDate").value(hasItem(DEFAULT_CUSTOMER_BEGIN_DATE.toString())))
            .andExpect(jsonPath("$.[*].customerEndDate").value(hasItem(DEFAULT_CUSTOMER_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].login").value(hasItem(DEFAULT_LOGIN.toString())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllCustomersWithEagerRelationshipsIsEnabled() throws Exception {
        CustomersResource customersResource = new CustomersResource(customersServiceMock);
        when(customersServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restCustomersMockMvc = MockMvcBuilders.standaloneSetup(customersResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restCustomersMockMvc.perform(get("/api/customers?eagerload=true"))
        .andExpect(status().isOk());

        verify(customersServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllCustomersWithEagerRelationshipsIsNotEnabled() throws Exception {
        CustomersResource customersResource = new CustomersResource(customersServiceMock);
            when(customersServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restCustomersMockMvc = MockMvcBuilders.standaloneSetup(customersResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restCustomersMockMvc.perform(get("/api/customers?eagerload=true"))
        .andExpect(status().isOk());

            verify(customersServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getCustomers() throws Exception {
        // Initialize the database
        customersRepository.saveAndFlush(customers);

        // Get the customers
        restCustomersMockMvc.perform(get("/api/customers/{id}", customers.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(customers.getId().intValue()))
            .andExpect(jsonPath("$.customerName").value(DEFAULT_CUSTOMER_NAME.toString()))
            .andExpect(jsonPath("$.customerEmail").value(DEFAULT_CUSTOMER_EMAIL.toString()))
            .andExpect(jsonPath("$.customerNif").value(DEFAULT_CUSTOMER_NIF.toString()))
            .andExpect(jsonPath("$.customerPhone").value(DEFAULT_CUSTOMER_PHONE.toString()))
            .andExpect(jsonPath("$.customerGender").value(DEFAULT_CUSTOMER_GENDER.toString()))
            .andExpect(jsonPath("$.customerBeginDate").value(DEFAULT_CUSTOMER_BEGIN_DATE.toString()))
            .andExpect(jsonPath("$.customerEndDate").value(DEFAULT_CUSTOMER_END_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.login").value(DEFAULT_LOGIN.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCustomers() throws Exception {
        // Get the customers
        restCustomersMockMvc.perform(get("/api/customers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCustomers() throws Exception {
        // Initialize the database
        customersRepository.saveAndFlush(customers);

        int databaseSizeBeforeUpdate = customersRepository.findAll().size();

        // Update the customers
        Customers updatedCustomers = customersRepository.findById(customers.getId()).get();
        // Disconnect from session so that the updates on updatedCustomers are not directly saved in db
        em.detach(updatedCustomers);
        updatedCustomers
            .customerName(UPDATED_CUSTOMER_NAME)
            .customerEmail(UPDATED_CUSTOMER_EMAIL)
            .customerNif(UPDATED_CUSTOMER_NIF)
            .customerPhone(UPDATED_CUSTOMER_PHONE)
            .customerGender(UPDATED_CUSTOMER_GENDER)
            .customerBeginDate(UPDATED_CUSTOMER_BEGIN_DATE)
            .customerEndDate(UPDATED_CUSTOMER_END_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .login(UPDATED_LOGIN);
        CustomersDTO customersDTO = customersMapper.toDto(updatedCustomers);

        restCustomersMockMvc.perform(put("/api/customers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customersDTO)))
            .andExpect(status().isOk());

        // Validate the Customers in the database
        List<Customers> customersList = customersRepository.findAll();
        assertThat(customersList).hasSize(databaseSizeBeforeUpdate);
        Customers testCustomers = customersList.get(customersList.size() - 1);
        assertThat(testCustomers.getCustomerName()).isEqualTo(UPDATED_CUSTOMER_NAME);
        assertThat(testCustomers.getCustomerEmail()).isEqualTo(UPDATED_CUSTOMER_EMAIL);
        assertThat(testCustomers.getCustomerNif()).isEqualTo(UPDATED_CUSTOMER_NIF);
        assertThat(testCustomers.getCustomerPhone()).isEqualTo(UPDATED_CUSTOMER_PHONE);
        assertThat(testCustomers.getCustomerGender()).isEqualTo(UPDATED_CUSTOMER_GENDER);
        assertThat(testCustomers.getCustomerBeginDate()).isEqualTo(UPDATED_CUSTOMER_BEGIN_DATE);
        assertThat(testCustomers.getCustomerEndDate()).isEqualTo(UPDATED_CUSTOMER_END_DATE);
        assertThat(testCustomers.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testCustomers.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testCustomers.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testCustomers.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testCustomers.getLogin()).isEqualTo(UPDATED_LOGIN);

        // Validate the Customers in Elasticsearch
        verify(mockCustomersSearchRepository, times(1)).save(testCustomers);
    }

    @Test
    @Transactional
    public void updateNonExistingCustomers() throws Exception {
        int databaseSizeBeforeUpdate = customersRepository.findAll().size();

        // Create the Customers
        CustomersDTO customersDTO = customersMapper.toDto(customers);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomersMockMvc.perform(put("/api/customers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customersDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Customers in the database
        List<Customers> customersList = customersRepository.findAll();
        assertThat(customersList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Customers in Elasticsearch
        verify(mockCustomersSearchRepository, times(0)).save(customers);
    }

    @Test
    @Transactional
    public void deleteCustomers() throws Exception {
        // Initialize the database
        customersRepository.saveAndFlush(customers);

        int databaseSizeBeforeDelete = customersRepository.findAll().size();

        // Get the customers
        restCustomersMockMvc.perform(delete("/api/customers/{id}", customers.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Customers> customersList = customersRepository.findAll();
        assertThat(customersList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Customers in Elasticsearch
        verify(mockCustomersSearchRepository, times(1)).deleteById(customers.getId());
    }

    @Test
    @Transactional
    public void searchCustomers() throws Exception {
        // Initialize the database
        customersRepository.saveAndFlush(customers);
        when(mockCustomersSearchRepository.search(queryStringQuery("id:" + customers.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(customers), PageRequest.of(0, 1), 1));
        // Search the customers
        restCustomersMockMvc.perform(get("/api/_search/customers?query=id:" + customers.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customers.getId().intValue())))
            .andExpect(jsonPath("$.[*].customerName").value(hasItem(DEFAULT_CUSTOMER_NAME)))
            .andExpect(jsonPath("$.[*].customerEmail").value(hasItem(DEFAULT_CUSTOMER_EMAIL)))
            .andExpect(jsonPath("$.[*].customerNif").value(hasItem(DEFAULT_CUSTOMER_NIF)))
            .andExpect(jsonPath("$.[*].customerPhone").value(hasItem(DEFAULT_CUSTOMER_PHONE)))
            .andExpect(jsonPath("$.[*].customerGender").value(hasItem(DEFAULT_CUSTOMER_GENDER.toString())))
            .andExpect(jsonPath("$.[*].customerBeginDate").value(hasItem(DEFAULT_CUSTOMER_BEGIN_DATE.toString())))
            .andExpect(jsonPath("$.[*].customerEndDate").value(hasItem(DEFAULT_CUSTOMER_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].login").value(hasItem(DEFAULT_LOGIN)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Customers.class);
        Customers customers1 = new Customers();
        customers1.setId(1L);
        Customers customers2 = new Customers();
        customers2.setId(customers1.getId());
        assertThat(customers1).isEqualTo(customers2);
        customers2.setId(2L);
        assertThat(customers1).isNotEqualTo(customers2);
        customers1.setId(null);
        assertThat(customers1).isNotEqualTo(customers2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomersDTO.class);
        CustomersDTO customersDTO1 = new CustomersDTO();
        customersDTO1.setId(1L);
        CustomersDTO customersDTO2 = new CustomersDTO();
        assertThat(customersDTO1).isNotEqualTo(customersDTO2);
        customersDTO2.setId(customersDTO1.getId());
        assertThat(customersDTO1).isEqualTo(customersDTO2);
        customersDTO2.setId(2L);
        assertThat(customersDTO1).isNotEqualTo(customersDTO2);
        customersDTO1.setId(null);
        assertThat(customersDTO1).isNotEqualTo(customersDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(customersMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(customersMapper.fromId(null)).isNull();
    }
}
