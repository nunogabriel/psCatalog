package com.cgi.pscatalog.service;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cgi.pscatalog.service.dto.OrderDetDTO;

/**
 * Service Interface for managing OrderDet.
 */
public interface OrderDetService {

    /**
     * Save a orderDet.
     *
     * @param orderDetDTO the entity to save
     * @return the persisted entity
     */
    OrderDetDTO save(OrderDetDTO orderDetDTO);

    /**
     * Get all the orderDets.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<OrderDetDTO> findAll(Pageable pageable);


    /**
     * Get the "id" orderDet.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<OrderDetDTO> findOne(Long id);

    /**
     * Delete the "id" orderDet.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the orderDet corresponding to the query.
     *
     * @param query the query of the search
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<OrderDetDTO> search(String query, Pageable pageable);

	Optional<OrderDetDTO> getOrderDetByOrderIdAndProductId(Long orderId, Long productId);

	Page<OrderDetDTO> getAllByOrderId(Long id, Pageable pageable);

	Page<OrderDetDTO> getAllByLoginAndOrderStatus(String login, Pageable pageable);

	Page<OrderDetDTO> getAllByLoginAndOrderStatusPending(String login, Pageable pageable);

	Page<OrderDetDTO> getAllByLoginAndOrderIdAndOrderStatus(String login, Long orderId, Pageable pageable);

	BigDecimal getOrderTotal(Long orderId);

	BigDecimal getOrderTotalWithPromotions(Long orderId);
}
