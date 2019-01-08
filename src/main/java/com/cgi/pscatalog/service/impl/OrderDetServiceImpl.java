package com.cgi.pscatalog.service.impl;

import com.cgi.pscatalog.service.OrderDetService;
import com.cgi.pscatalog.domain.OrderDet;
import com.cgi.pscatalog.repository.OrderDetRepository;
import com.cgi.pscatalog.repository.search.OrderDetSearchRepository;
import com.cgi.pscatalog.service.dto.OrderDetDTO;
import com.cgi.pscatalog.service.mapper.OrderDetMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing OrderDet.
 */
@Service
@Transactional
public class OrderDetServiceImpl implements OrderDetService {

    private final Logger log = LoggerFactory.getLogger(OrderDetServiceImpl.class);

    private final OrderDetRepository orderDetRepository;

    private final OrderDetMapper orderDetMapper;

    private final OrderDetSearchRepository orderDetSearchRepository;

    public OrderDetServiceImpl(OrderDetRepository orderDetRepository, OrderDetMapper orderDetMapper, OrderDetSearchRepository orderDetSearchRepository) {
        this.orderDetRepository = orderDetRepository;
        this.orderDetMapper = orderDetMapper;
        this.orderDetSearchRepository = orderDetSearchRepository;
    }

    /**
     * Save a orderDet.
     *
     * @param orderDetDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public OrderDetDTO save(OrderDetDTO orderDetDTO) {
        log.debug("Request to save OrderDet : {}", orderDetDTO);

        OrderDet orderDet = orderDetMapper.toEntity(orderDetDTO);
        orderDet = orderDetRepository.save(orderDet);
        OrderDetDTO result = orderDetMapper.toDto(orderDet);
        orderDetSearchRepository.save(orderDet);
        return result;
    }

    /**
     * Get all the orderDets.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<OrderDetDTO> findAll(Pageable pageable) {
        log.debug("Request to get all OrderDets");
        return orderDetRepository.findAll(pageable)
            .map(orderDetMapper::toDto);
    }


    /**
     * Get one orderDet by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<OrderDetDTO> findOne(Long id) {
        log.debug("Request to get OrderDet : {}", id);
        return orderDetRepository.findById(id)
            .map(orderDetMapper::toDto);
    }

    /**
     * Delete the orderDet by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete OrderDet : {}", id);
        orderDetRepository.deleteById(id);
        orderDetSearchRepository.deleteById(id);
    }

    /**
     * Search for the orderDet corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<OrderDetDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of OrderDets for query {}", query);
        return orderDetSearchRepository.search(queryStringQuery(query), pageable)
            .map(orderDetMapper::toDto);
    }

	@Override
	@Transactional(readOnly = true)
	public Optional<OrderDetDTO> getOrderDetByOrderIdAndProductId(Long orderId, Long productId) {
        log.debug("Request to get OrderDet by Order Id {} And Product Id: {}:", orderId, productId);
        return orderDetRepository.findByOrderIdAndProductId(orderId, productId)
            .map(orderDetMapper::toDto);
	}
	
	@Override
	@Transactional(readOnly = true)
	public Page<OrderDetDTO> getAllByOrderId(Long id, Pageable pageable) {
        log.debug("Request to get all Order Details by Order Id");
        return orderDetRepository.findAllByOrderId(id, pageable)
            .map(orderDetMapper::toDto);
	}
}
