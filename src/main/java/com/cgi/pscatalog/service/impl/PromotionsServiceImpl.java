package com.cgi.pscatalog.service.impl;

import com.cgi.pscatalog.service.PromotionsService;
import com.cgi.pscatalog.domain.Promotions;
import com.cgi.pscatalog.repository.PromotionsRepository;
import com.cgi.pscatalog.repository.search.PromotionsSearchRepository;
import com.cgi.pscatalog.service.dto.PromotionsDTO;
import com.cgi.pscatalog.service.mapper.PromotionsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Promotions.
 */
@Service
@Transactional
public class PromotionsServiceImpl implements PromotionsService {

    private final Logger log = LoggerFactory.getLogger(PromotionsServiceImpl.class);

    private final PromotionsRepository promotionsRepository;

    private final PromotionsMapper promotionsMapper;

    private final PromotionsSearchRepository promotionsSearchRepository;

    public PromotionsServiceImpl(PromotionsRepository promotionsRepository, PromotionsMapper promotionsMapper, PromotionsSearchRepository promotionsSearchRepository) {
        this.promotionsRepository = promotionsRepository;
        this.promotionsMapper = promotionsMapper;
        this.promotionsSearchRepository = promotionsSearchRepository;
    }

    /**
     * Save a promotions.
     *
     * @param promotionsDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public PromotionsDTO save(PromotionsDTO promotionsDTO) {
        log.debug("Request to save Promotions : {}", promotionsDTO);

        Promotions promotions = promotionsMapper.toEntity(promotionsDTO);
        promotions = promotionsRepository.save(promotions);
        PromotionsDTO result = promotionsMapper.toDto(promotions);
        promotionsSearchRepository.save(promotions);
        return result;
    }

    /**
     * Get all the promotions.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PromotionsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Promotions");
        return promotionsRepository.findAll(pageable)
            .map(promotionsMapper::toDto);
    }


    /**
     * Get one promotions by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<PromotionsDTO> findOne(Long id) {
        log.debug("Request to get Promotions : {}", id);
        return promotionsRepository.findById(id)
            .map(promotionsMapper::toDto);
    }

    /**
     * Delete the promotions by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Promotions : {}", id);
        promotionsRepository.deleteById(id);
        promotionsSearchRepository.deleteById(id);
    }

    /**
     * Search for the promotions corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PromotionsDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Promotions for query {}", query);
        return promotionsSearchRepository.search(queryStringQuery(query), pageable)
            .map(promotionsMapper::toDto);
    }
}
