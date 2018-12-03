package com.cgi.pscatalog.service.impl;

import com.cgi.pscatalog.service.SuppliersService;
import com.cgi.pscatalog.domain.Suppliers;
import com.cgi.pscatalog.repository.SuppliersRepository;
import com.cgi.pscatalog.repository.search.SuppliersSearchRepository;
import com.cgi.pscatalog.service.dto.SuppliersDTO;
import com.cgi.pscatalog.service.mapper.SuppliersMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Suppliers.
 */
@Service
@Transactional
public class SuppliersServiceImpl implements SuppliersService {

    private final Logger log = LoggerFactory.getLogger(SuppliersServiceImpl.class);

    private final SuppliersRepository suppliersRepository;

    private final SuppliersMapper suppliersMapper;

    private final SuppliersSearchRepository suppliersSearchRepository;

    public SuppliersServiceImpl(SuppliersRepository suppliersRepository, SuppliersMapper suppliersMapper, SuppliersSearchRepository suppliersSearchRepository) {
        this.suppliersRepository = suppliersRepository;
        this.suppliersMapper = suppliersMapper;
        this.suppliersSearchRepository = suppliersSearchRepository;
    }

    /**
     * Save a suppliers.
     *
     * @param suppliersDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public SuppliersDTO save(SuppliersDTO suppliersDTO) {
        log.debug("Request to save Suppliers : {}", suppliersDTO);

        Suppliers suppliers = suppliersMapper.toEntity(suppliersDTO);
        suppliers = suppliersRepository.save(suppliers);
        SuppliersDTO result = suppliersMapper.toDto(suppliers);
        suppliersSearchRepository.save(suppliers);
        return result;
    }

    /**
     * Get all the suppliers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SuppliersDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Suppliers");
        return suppliersRepository.findAll(pageable)
            .map(suppliersMapper::toDto);
    }


    /**
     * Get one suppliers by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<SuppliersDTO> findOne(Long id) {
        log.debug("Request to get Suppliers : {}", id);
        return suppliersRepository.findById(id)
            .map(suppliersMapper::toDto);
    }

    /**
     * Delete the suppliers by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Suppliers : {}", id);
        suppliersRepository.deleteById(id);
        suppliersSearchRepository.deleteById(id);
    }

    /**
     * Search for the suppliers corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SuppliersDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Suppliers for query {}", query);
        return suppliersSearchRepository.search(queryStringQuery(query), pageable)
            .map(suppliersMapper::toDto);
    }
}
