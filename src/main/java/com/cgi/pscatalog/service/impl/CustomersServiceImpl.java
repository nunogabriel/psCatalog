package com.cgi.pscatalog.service.impl;

import com.cgi.pscatalog.service.CustomersService;
import com.cgi.pscatalog.domain.Customers;
import com.cgi.pscatalog.repository.CustomersRepository;
import com.cgi.pscatalog.repository.search.CustomersSearchRepository;
import com.cgi.pscatalog.service.dto.CustomersDTO;
import com.cgi.pscatalog.service.mapper.CustomersMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Customers.
 */
@Service
@Transactional
public class CustomersServiceImpl implements CustomersService {

    private final Logger log = LoggerFactory.getLogger(CustomersServiceImpl.class);

    private final CustomersRepository customersRepository;

    private final CustomersMapper customersMapper;

    private final CustomersSearchRepository customersSearchRepository;

    public CustomersServiceImpl(CustomersRepository customersRepository, CustomersMapper customersMapper, CustomersSearchRepository customersSearchRepository) {
        this.customersRepository = customersRepository;
        this.customersMapper = customersMapper;
        this.customersSearchRepository = customersSearchRepository;
    }

    /**
     * Save a customers.
     *
     * @param customersDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public CustomersDTO save(CustomersDTO customersDTO) {
        log.debug("Request to save Customers : {}", customersDTO);

        Customers customers = customersMapper.toEntity(customersDTO);
        customers = customersRepository.save(customers);
        CustomersDTO result = customersMapper.toDto(customers);
        customersSearchRepository.save(customers);
        return result;
    }

    /**
     * Get all the customers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CustomersDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Customers");
        return customersRepository.findAll(pageable)
            .map(customersMapper::toDto);
    }

    /**
     * Get all the Customers with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    public Page<CustomersDTO> findAllWithEagerRelationships(Pageable pageable) {
        return customersRepository.findAllWithEagerRelationships(pageable).map(customersMapper::toDto);
    }
    

    /**
     * Get one customers by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CustomersDTO> findOne(Long id) {
        log.debug("Request to get Customers : {}", id);
        return customersRepository.findOneWithEagerRelationships(id)
            .map(customersMapper::toDto);
    }

    /**
     * Delete the customers by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Customers : {}", id);
        customersRepository.deleteById(id);
        customersSearchRepository.deleteById(id);
    }

    /**
     * Search for the customers corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CustomersDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Customers for query {}", query);
        return customersSearchRepository.search(queryStringQuery(query), pageable)
            .map(customersMapper::toDto);
    }
}
