package com.cgi.pscatalog.web.rest;

import com.cgi.pscatalog.PsCatalogApp;

import com.cgi.pscatalog.domain.Addresses;
import com.cgi.pscatalog.domain.Countries;
import com.cgi.pscatalog.repository.AddressesRepository;
import com.cgi.pscatalog.repository.search.AddressesSearchRepository;
import com.cgi.pscatalog.service.AddressesService;
import com.cgi.pscatalog.service.dto.AddressesDTO;
import com.cgi.pscatalog.service.mapper.AddressesMapper;
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
 * Test class for the AddressesResource REST controller.
 *
 * @see AddressesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PsCatalogApp.class)
public class AddressesResourceIntTest {

    private static final String DEFAULT_ADDRESS_REFERENCE = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_REFERENCE = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_STREET_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_STREET_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_STATE = "AAAAAAAAAA";
    private static final String UPDATED_STATE = "BBBBBBBBBB";

    private static final String DEFAULT_ZIP_CODE = "AAAAAAAAAA";
    private static final String UPDATED_ZIP_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_ADDRESS_BEGIN_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ADDRESS_BEGIN_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_ADDRESS_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ADDRESS_END_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private AddressesRepository addressesRepository;

    @Autowired
    private AddressesMapper addressesMapper;

    @Autowired
    private AddressesService addressesService;

    /**
     * This repository is mocked in the com.cgi.pscatalog.repository.search test package.
     *
     * @see com.cgi.pscatalog.repository.search.AddressesSearchRepositoryMockConfiguration
     */
    @Autowired
    private AddressesSearchRepository mockAddressesSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAddressesMockMvc;

    private Addresses addresses;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AddressesResource addressesResource = new AddressesResource(addressesService);
        this.restAddressesMockMvc = MockMvcBuilders.standaloneSetup(addressesResource)
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
    public static Addresses createEntity(EntityManager em) {
        Addresses addresses = new Addresses()
            .addressReference(DEFAULT_ADDRESS_REFERENCE)
            .addressName(DEFAULT_ADDRESS_NAME)
            .streetAddress(DEFAULT_STREET_ADDRESS)
            .city(DEFAULT_CITY)
            .state(DEFAULT_STATE)
            .zipCode(DEFAULT_ZIP_CODE)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .addressBeginDate(DEFAULT_ADDRESS_BEGIN_DATE)
            .addressEndDate(DEFAULT_ADDRESS_END_DATE);
        // Add required entity
        Countries countries = CountriesResourceIntTest.createEntity(em);
        em.persist(countries);
        em.flush();
        addresses.setCountry(countries);
        return addresses;
    }

    @Before
    public void initTest() {
        addresses = createEntity(em);
    }

    @Test
    @Transactional
    public void createAddresses() throws Exception {
        int databaseSizeBeforeCreate = addressesRepository.findAll().size();

        // Create the Addresses
        AddressesDTO addressesDTO = addressesMapper.toDto(addresses);
        restAddressesMockMvc.perform(post("/api/addresses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(addressesDTO)))
            .andExpect(status().isCreated());

        // Validate the Addresses in the database
        List<Addresses> addressesList = addressesRepository.findAll();
        assertThat(addressesList).hasSize(databaseSizeBeforeCreate + 1);
        Addresses testAddresses = addressesList.get(addressesList.size() - 1);
        assertThat(testAddresses.getAddressReference()).isEqualTo(DEFAULT_ADDRESS_REFERENCE);
        assertThat(testAddresses.getAddressName()).isEqualTo(DEFAULT_ADDRESS_NAME);
        assertThat(testAddresses.getStreetAddress()).isEqualTo(DEFAULT_STREET_ADDRESS);
        assertThat(testAddresses.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testAddresses.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testAddresses.getZipCode()).isEqualTo(DEFAULT_ZIP_CODE);
        assertThat(testAddresses.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testAddresses.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testAddresses.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testAddresses.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testAddresses.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testAddresses.getAddressBeginDate()).isEqualTo(DEFAULT_ADDRESS_BEGIN_DATE);
        assertThat(testAddresses.getAddressEndDate()).isEqualTo(DEFAULT_ADDRESS_END_DATE);

        // Validate the Addresses in Elasticsearch
        verify(mockAddressesSearchRepository, times(1)).save(testAddresses);
    }

    @Test
    @Transactional
    public void createAddressesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = addressesRepository.findAll().size();

        // Create the Addresses with an existing ID
        addresses.setId(1L);
        AddressesDTO addressesDTO = addressesMapper.toDto(addresses);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAddressesMockMvc.perform(post("/api/addresses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(addressesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Addresses in the database
        List<Addresses> addressesList = addressesRepository.findAll();
        assertThat(addressesList).hasSize(databaseSizeBeforeCreate);

        // Validate the Addresses in Elasticsearch
        verify(mockAddressesSearchRepository, times(0)).save(addresses);
    }

    @Test
    @Transactional
    public void checkAddressReferenceIsRequired() throws Exception {
        int databaseSizeBeforeTest = addressesRepository.findAll().size();
        // set the field null
        addresses.setAddressReference(null);

        // Create the Addresses, which fails.
        AddressesDTO addressesDTO = addressesMapper.toDto(addresses);

        restAddressesMockMvc.perform(post("/api/addresses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(addressesDTO)))
            .andExpect(status().isBadRequest());

        List<Addresses> addressesList = addressesRepository.findAll();
        assertThat(addressesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAddressNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = addressesRepository.findAll().size();
        // set the field null
        addresses.setAddressName(null);

        // Create the Addresses, which fails.
        AddressesDTO addressesDTO = addressesMapper.toDto(addresses);

        restAddressesMockMvc.perform(post("/api/addresses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(addressesDTO)))
            .andExpect(status().isBadRequest());

        List<Addresses> addressesList = addressesRepository.findAll();
        assertThat(addressesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStreetAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = addressesRepository.findAll().size();
        // set the field null
        addresses.setStreetAddress(null);

        // Create the Addresses, which fails.
        AddressesDTO addressesDTO = addressesMapper.toDto(addresses);

        restAddressesMockMvc.perform(post("/api/addresses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(addressesDTO)))
            .andExpect(status().isBadRequest());

        List<Addresses> addressesList = addressesRepository.findAll();
        assertThat(addressesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCityIsRequired() throws Exception {
        int databaseSizeBeforeTest = addressesRepository.findAll().size();
        // set the field null
        addresses.setCity(null);

        // Create the Addresses, which fails.
        AddressesDTO addressesDTO = addressesMapper.toDto(addresses);

        restAddressesMockMvc.perform(post("/api/addresses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(addressesDTO)))
            .andExpect(status().isBadRequest());

        List<Addresses> addressesList = addressesRepository.findAll();
        assertThat(addressesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStateIsRequired() throws Exception {
        int databaseSizeBeforeTest = addressesRepository.findAll().size();
        // set the field null
        addresses.setState(null);

        // Create the Addresses, which fails.
        AddressesDTO addressesDTO = addressesMapper.toDto(addresses);

        restAddressesMockMvc.perform(post("/api/addresses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(addressesDTO)))
            .andExpect(status().isBadRequest());

        List<Addresses> addressesList = addressesRepository.findAll();
        assertThat(addressesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkZipCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = addressesRepository.findAll().size();
        // set the field null
        addresses.setZipCode(null);

        // Create the Addresses, which fails.
        AddressesDTO addressesDTO = addressesMapper.toDto(addresses);

        restAddressesMockMvc.perform(post("/api/addresses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(addressesDTO)))
            .andExpect(status().isBadRequest());

        List<Addresses> addressesList = addressesRepository.findAll();
        assertThat(addressesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAddressBeginDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = addressesRepository.findAll().size();
        // set the field null
        addresses.setAddressBeginDate(null);

        // Create the Addresses, which fails.
        AddressesDTO addressesDTO = addressesMapper.toDto(addresses);

        restAddressesMockMvc.perform(post("/api/addresses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(addressesDTO)))
            .andExpect(status().isBadRequest());

        List<Addresses> addressesList = addressesRepository.findAll();
        assertThat(addressesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAddresses() throws Exception {
        // Initialize the database
        addressesRepository.saveAndFlush(addresses);

        // Get all the addressesList
        restAddressesMockMvc.perform(get("/api/addresses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(addresses.getId().intValue())))
            .andExpect(jsonPath("$.[*].addressReference").value(hasItem(DEFAULT_ADDRESS_REFERENCE.toString())))
            .andExpect(jsonPath("$.[*].addressName").value(hasItem(DEFAULT_ADDRESS_NAME.toString())))
            .andExpect(jsonPath("$.[*].streetAddress").value(hasItem(DEFAULT_STREET_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
            .andExpect(jsonPath("$.[*].zipCode").value(hasItem(DEFAULT_ZIP_CODE.toString())))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].addressBeginDate").value(hasItem(DEFAULT_ADDRESS_BEGIN_DATE.toString())))
            .andExpect(jsonPath("$.[*].addressEndDate").value(hasItem(DEFAULT_ADDRESS_END_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getAddresses() throws Exception {
        // Initialize the database
        addressesRepository.saveAndFlush(addresses);

        // Get the addresses
        restAddressesMockMvc.perform(get("/api/addresses/{id}", addresses.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(addresses.getId().intValue()))
            .andExpect(jsonPath("$.addressReference").value(DEFAULT_ADDRESS_REFERENCE.toString()))
            .andExpect(jsonPath("$.addressName").value(DEFAULT_ADDRESS_NAME.toString()))
            .andExpect(jsonPath("$.streetAddress").value(DEFAULT_STREET_ADDRESS.toString()))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY.toString()))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()))
            .andExpect(jsonPath("$.zipCode").value(DEFAULT_ZIP_CODE.toString()))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.addressBeginDate").value(DEFAULT_ADDRESS_BEGIN_DATE.toString()))
            .andExpect(jsonPath("$.addressEndDate").value(DEFAULT_ADDRESS_END_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAddresses() throws Exception {
        // Get the addresses
        restAddressesMockMvc.perform(get("/api/addresses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAddresses() throws Exception {
        // Initialize the database
        addressesRepository.saveAndFlush(addresses);

        int databaseSizeBeforeUpdate = addressesRepository.findAll().size();

        // Update the addresses
        Addresses updatedAddresses = addressesRepository.findById(addresses.getId()).get();
        // Disconnect from session so that the updates on updatedAddresses are not directly saved in db
        em.detach(updatedAddresses);
        updatedAddresses
            .addressReference(UPDATED_ADDRESS_REFERENCE)
            .addressName(UPDATED_ADDRESS_NAME)
            .streetAddress(UPDATED_STREET_ADDRESS)
            .city(UPDATED_CITY)
            .state(UPDATED_STATE)
            .zipCode(UPDATED_ZIP_CODE)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .addressBeginDate(UPDATED_ADDRESS_BEGIN_DATE)
            .addressEndDate(UPDATED_ADDRESS_END_DATE);
        AddressesDTO addressesDTO = addressesMapper.toDto(updatedAddresses);

        restAddressesMockMvc.perform(put("/api/addresses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(addressesDTO)))
            .andExpect(status().isOk());

        // Validate the Addresses in the database
        List<Addresses> addressesList = addressesRepository.findAll();
        assertThat(addressesList).hasSize(databaseSizeBeforeUpdate);
        Addresses testAddresses = addressesList.get(addressesList.size() - 1);
        assertThat(testAddresses.getAddressReference()).isEqualTo(UPDATED_ADDRESS_REFERENCE);
        assertThat(testAddresses.getAddressName()).isEqualTo(UPDATED_ADDRESS_NAME);
        assertThat(testAddresses.getStreetAddress()).isEqualTo(UPDATED_STREET_ADDRESS);
        assertThat(testAddresses.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testAddresses.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testAddresses.getZipCode()).isEqualTo(UPDATED_ZIP_CODE);
        assertThat(testAddresses.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testAddresses.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testAddresses.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testAddresses.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testAddresses.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testAddresses.getAddressBeginDate()).isEqualTo(UPDATED_ADDRESS_BEGIN_DATE);
        assertThat(testAddresses.getAddressEndDate()).isEqualTo(UPDATED_ADDRESS_END_DATE);

        // Validate the Addresses in Elasticsearch
        verify(mockAddressesSearchRepository, times(1)).save(testAddresses);
    }

    @Test
    @Transactional
    public void updateNonExistingAddresses() throws Exception {
        int databaseSizeBeforeUpdate = addressesRepository.findAll().size();

        // Create the Addresses
        AddressesDTO addressesDTO = addressesMapper.toDto(addresses);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAddressesMockMvc.perform(put("/api/addresses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(addressesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Addresses in the database
        List<Addresses> addressesList = addressesRepository.findAll();
        assertThat(addressesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Addresses in Elasticsearch
        verify(mockAddressesSearchRepository, times(0)).save(addresses);
    }

    @Test
    @Transactional
    public void deleteAddresses() throws Exception {
        // Initialize the database
        addressesRepository.saveAndFlush(addresses);

        int databaseSizeBeforeDelete = addressesRepository.findAll().size();

        // Get the addresses
        restAddressesMockMvc.perform(delete("/api/addresses/{id}", addresses.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Addresses> addressesList = addressesRepository.findAll();
        assertThat(addressesList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Addresses in Elasticsearch
        verify(mockAddressesSearchRepository, times(1)).deleteById(addresses.getId());
    }

    @Test
    @Transactional
    public void searchAddresses() throws Exception {
        // Initialize the database
        addressesRepository.saveAndFlush(addresses);
        when(mockAddressesSearchRepository.search(queryStringQuery("id:" + addresses.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(addresses), PageRequest.of(0, 1), 1));
        // Search the addresses
        restAddressesMockMvc.perform(get("/api/_search/addresses?query=id:" + addresses.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(addresses.getId().intValue())))
            .andExpect(jsonPath("$.[*].addressReference").value(hasItem(DEFAULT_ADDRESS_REFERENCE)))
            .andExpect(jsonPath("$.[*].addressName").value(hasItem(DEFAULT_ADDRESS_NAME)))
            .andExpect(jsonPath("$.[*].streetAddress").value(hasItem(DEFAULT_STREET_ADDRESS)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE)))
            .andExpect(jsonPath("$.[*].zipCode").value(hasItem(DEFAULT_ZIP_CODE)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].addressBeginDate").value(hasItem(DEFAULT_ADDRESS_BEGIN_DATE.toString())))
            .andExpect(jsonPath("$.[*].addressEndDate").value(hasItem(DEFAULT_ADDRESS_END_DATE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Addresses.class);
        Addresses addresses1 = new Addresses();
        addresses1.setId(1L);
        Addresses addresses2 = new Addresses();
        addresses2.setId(addresses1.getId());
        assertThat(addresses1).isEqualTo(addresses2);
        addresses2.setId(2L);
        assertThat(addresses1).isNotEqualTo(addresses2);
        addresses1.setId(null);
        assertThat(addresses1).isNotEqualTo(addresses2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AddressesDTO.class);
        AddressesDTO addressesDTO1 = new AddressesDTO();
        addressesDTO1.setId(1L);
        AddressesDTO addressesDTO2 = new AddressesDTO();
        assertThat(addressesDTO1).isNotEqualTo(addressesDTO2);
        addressesDTO2.setId(addressesDTO1.getId());
        assertThat(addressesDTO1).isEqualTo(addressesDTO2);
        addressesDTO2.setId(2L);
        assertThat(addressesDTO1).isNotEqualTo(addressesDTO2);
        addressesDTO1.setId(null);
        assertThat(addressesDTO1).isNotEqualTo(addressesDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(addressesMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(addressesMapper.fromId(null)).isNull();
    }
}
