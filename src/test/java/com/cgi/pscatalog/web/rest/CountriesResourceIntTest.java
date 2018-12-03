package com.cgi.pscatalog.web.rest;

import com.cgi.pscatalog.PsCatalogApp;

import com.cgi.pscatalog.domain.Countries;
import com.cgi.pscatalog.repository.CountriesRepository;
import com.cgi.pscatalog.repository.search.CountriesSearchRepository;
import com.cgi.pscatalog.service.CountriesService;
import com.cgi.pscatalog.service.dto.CountriesDTO;
import com.cgi.pscatalog.service.mapper.CountriesMapper;
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
 * Test class for the CountriesResource REST controller.
 *
 * @see CountriesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PsCatalogApp.class)
public class CountriesResourceIntTest {

    private static final String DEFAULT_COUNTRY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY_NAME = "BBBBBBBBBB";

    @Autowired
    private CountriesRepository countriesRepository;

    @Autowired
    private CountriesMapper countriesMapper;

    @Autowired
    private CountriesService countriesService;

    /**
     * This repository is mocked in the com.cgi.pscatalog.repository.search test package.
     *
     * @see com.cgi.pscatalog.repository.search.CountriesSearchRepositoryMockConfiguration
     */
    @Autowired
    private CountriesSearchRepository mockCountriesSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCountriesMockMvc;

    private Countries countries;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CountriesResource countriesResource = new CountriesResource(countriesService);
        this.restCountriesMockMvc = MockMvcBuilders.standaloneSetup(countriesResource)
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
    public static Countries createEntity(EntityManager em) {
        Countries countries = new Countries()
            .countryName(DEFAULT_COUNTRY_NAME);
        return countries;
    }

    @Before
    public void initTest() {
        countries = createEntity(em);
    }

    @Test
    @Transactional
    public void createCountries() throws Exception {
        int databaseSizeBeforeCreate = countriesRepository.findAll().size();

        // Create the Countries
        CountriesDTO countriesDTO = countriesMapper.toDto(countries);
        restCountriesMockMvc.perform(post("/api/countries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(countriesDTO)))
            .andExpect(status().isCreated());

        // Validate the Countries in the database
        List<Countries> countriesList = countriesRepository.findAll();
        assertThat(countriesList).hasSize(databaseSizeBeforeCreate + 1);
        Countries testCountries = countriesList.get(countriesList.size() - 1);
        assertThat(testCountries.getCountryName()).isEqualTo(DEFAULT_COUNTRY_NAME);

        // Validate the Countries in Elasticsearch
        verify(mockCountriesSearchRepository, times(1)).save(testCountries);
    }

    @Test
    @Transactional
    public void createCountriesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = countriesRepository.findAll().size();

        // Create the Countries with an existing ID
        countries.setId(1L);
        CountriesDTO countriesDTO = countriesMapper.toDto(countries);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCountriesMockMvc.perform(post("/api/countries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(countriesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Countries in the database
        List<Countries> countriesList = countriesRepository.findAll();
        assertThat(countriesList).hasSize(databaseSizeBeforeCreate);

        // Validate the Countries in Elasticsearch
        verify(mockCountriesSearchRepository, times(0)).save(countries);
    }

    @Test
    @Transactional
    public void checkCountryNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = countriesRepository.findAll().size();
        // set the field null
        countries.setCountryName(null);

        // Create the Countries, which fails.
        CountriesDTO countriesDTO = countriesMapper.toDto(countries);

        restCountriesMockMvc.perform(post("/api/countries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(countriesDTO)))
            .andExpect(status().isBadRequest());

        List<Countries> countriesList = countriesRepository.findAll();
        assertThat(countriesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCountries() throws Exception {
        // Initialize the database
        countriesRepository.saveAndFlush(countries);

        // Get all the countriesList
        restCountriesMockMvc.perform(get("/api/countries?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(countries.getId().intValue())))
            .andExpect(jsonPath("$.[*].countryName").value(hasItem(DEFAULT_COUNTRY_NAME.toString())));
    }
    
    @Test
    @Transactional
    public void getCountries() throws Exception {
        // Initialize the database
        countriesRepository.saveAndFlush(countries);

        // Get the countries
        restCountriesMockMvc.perform(get("/api/countries/{id}", countries.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(countries.getId().intValue()))
            .andExpect(jsonPath("$.countryName").value(DEFAULT_COUNTRY_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCountries() throws Exception {
        // Get the countries
        restCountriesMockMvc.perform(get("/api/countries/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCountries() throws Exception {
        // Initialize the database
        countriesRepository.saveAndFlush(countries);

        int databaseSizeBeforeUpdate = countriesRepository.findAll().size();

        // Update the countries
        Countries updatedCountries = countriesRepository.findById(countries.getId()).get();
        // Disconnect from session so that the updates on updatedCountries are not directly saved in db
        em.detach(updatedCountries);
        updatedCountries
            .countryName(UPDATED_COUNTRY_NAME);
        CountriesDTO countriesDTO = countriesMapper.toDto(updatedCountries);

        restCountriesMockMvc.perform(put("/api/countries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(countriesDTO)))
            .andExpect(status().isOk());

        // Validate the Countries in the database
        List<Countries> countriesList = countriesRepository.findAll();
        assertThat(countriesList).hasSize(databaseSizeBeforeUpdate);
        Countries testCountries = countriesList.get(countriesList.size() - 1);
        assertThat(testCountries.getCountryName()).isEqualTo(UPDATED_COUNTRY_NAME);

        // Validate the Countries in Elasticsearch
        verify(mockCountriesSearchRepository, times(1)).save(testCountries);
    }

    @Test
    @Transactional
    public void updateNonExistingCountries() throws Exception {
        int databaseSizeBeforeUpdate = countriesRepository.findAll().size();

        // Create the Countries
        CountriesDTO countriesDTO = countriesMapper.toDto(countries);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCountriesMockMvc.perform(put("/api/countries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(countriesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Countries in the database
        List<Countries> countriesList = countriesRepository.findAll();
        assertThat(countriesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Countries in Elasticsearch
        verify(mockCountriesSearchRepository, times(0)).save(countries);
    }

    @Test
    @Transactional
    public void deleteCountries() throws Exception {
        // Initialize the database
        countriesRepository.saveAndFlush(countries);

        int databaseSizeBeforeDelete = countriesRepository.findAll().size();

        // Get the countries
        restCountriesMockMvc.perform(delete("/api/countries/{id}", countries.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Countries> countriesList = countriesRepository.findAll();
        assertThat(countriesList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Countries in Elasticsearch
        verify(mockCountriesSearchRepository, times(1)).deleteById(countries.getId());
    }

    @Test
    @Transactional
    public void searchCountries() throws Exception {
        // Initialize the database
        countriesRepository.saveAndFlush(countries);
        when(mockCountriesSearchRepository.search(queryStringQuery("id:" + countries.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(countries), PageRequest.of(0, 1), 1));
        // Search the countries
        restCountriesMockMvc.perform(get("/api/_search/countries?query=id:" + countries.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(countries.getId().intValue())))
            .andExpect(jsonPath("$.[*].countryName").value(hasItem(DEFAULT_COUNTRY_NAME)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Countries.class);
        Countries countries1 = new Countries();
        countries1.setId(1L);
        Countries countries2 = new Countries();
        countries2.setId(countries1.getId());
        assertThat(countries1).isEqualTo(countries2);
        countries2.setId(2L);
        assertThat(countries1).isNotEqualTo(countries2);
        countries1.setId(null);
        assertThat(countries1).isNotEqualTo(countries2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CountriesDTO.class);
        CountriesDTO countriesDTO1 = new CountriesDTO();
        countriesDTO1.setId(1L);
        CountriesDTO countriesDTO2 = new CountriesDTO();
        assertThat(countriesDTO1).isNotEqualTo(countriesDTO2);
        countriesDTO2.setId(countriesDTO1.getId());
        assertThat(countriesDTO1).isEqualTo(countriesDTO2);
        countriesDTO2.setId(2L);
        assertThat(countriesDTO1).isNotEqualTo(countriesDTO2);
        countriesDTO1.setId(null);
        assertThat(countriesDTO1).isNotEqualTo(countriesDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(countriesMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(countriesMapper.fromId(null)).isNull();
    }
}
