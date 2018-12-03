package com.cgi.pscatalog.service.impl;

import com.cgi.pscatalog.service.CountriesService;
import com.cgi.pscatalog.domain.Countries;
import com.cgi.pscatalog.repository.CountriesRepository;
import com.cgi.pscatalog.repository.search.CountriesSearchRepository;
import com.cgi.pscatalog.service.dto.CountriesDTO;
import com.cgi.pscatalog.service.mapper.CountriesMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Countries.
 */
@Service
@Transactional
public class CountriesServiceImpl implements CountriesService {

    private final Logger log = LoggerFactory.getLogger(CountriesServiceImpl.class);

    private final CountriesRepository countriesRepository;

    private final CountriesMapper countriesMapper;

    private final CountriesSearchRepository countriesSearchRepository;

    public CountriesServiceImpl(CountriesRepository countriesRepository, CountriesMapper countriesMapper, CountriesSearchRepository countriesSearchRepository) {
        this.countriesRepository = countriesRepository;
        this.countriesMapper = countriesMapper;
        this.countriesSearchRepository = countriesSearchRepository;
    }

    /**
     * Save a countries.
     *
     * @param countriesDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public CountriesDTO save(CountriesDTO countriesDTO) {
        log.debug("Request to save Countries : {}", countriesDTO);

        Countries countries = countriesMapper.toEntity(countriesDTO);
        countries = countriesRepository.save(countries);
        CountriesDTO result = countriesMapper.toDto(countries);
        countriesSearchRepository.save(countries);
        return result;
    }

    /**
     * Get all the countries.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CountriesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Countries");
        return countriesRepository.findAll(pageable)
            .map(countriesMapper::toDto);
    }


    /**
     * Get one countries by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CountriesDTO> findOne(Long id) {
        log.debug("Request to get Countries : {}", id);
        return countriesRepository.findById(id)
            .map(countriesMapper::toDto);
    }

    /**
     * Delete the countries by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Countries : {}", id);
        countriesRepository.deleteById(id);
        countriesSearchRepository.deleteById(id);
    }

    /**
     * Search for the countries corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CountriesDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Countries for query {}", query);
        return countriesSearchRepository.search(queryStringQuery(query), pageable)
            .map(countriesMapper::toDto);
    }
}
