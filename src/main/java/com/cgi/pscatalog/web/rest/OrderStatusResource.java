package com.cgi.pscatalog.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cgi.pscatalog.service.OrderStatusService;
import com.cgi.pscatalog.service.dto.OrderStatusDTO;
import com.cgi.pscatalog.web.rest.errors.BadRequestAlertException;
import com.cgi.pscatalog.web.rest.util.HeaderUtil;
import com.cgi.pscatalog.web.rest.util.PaginationUtil;
import com.codahale.metrics.annotation.Timed;

import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing OrderStatus.
 */
@RestController
@RequestMapping("/api")
public class OrderStatusResource {

    private final Logger log = LoggerFactory.getLogger(OrderStatusResource.class);

    private static final String ENTITY_NAME = "orderStatus";

    private final OrderStatusService orderStatusService;

    public OrderStatusResource(OrderStatusService orderStatusService) {
        this.orderStatusService = orderStatusService;
    }

    /**
     * POST  /order-statuses : Create a new orderStatus.
     *
     * @param orderStatusDTO the orderStatusDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new orderStatusDTO, or with status 400 (Bad Request) if the orderStatus has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/order-statuses")
    @Timed
    public ResponseEntity<OrderStatusDTO> createOrderStatus(@Valid @RequestBody OrderStatusDTO orderStatusDTO) throws URISyntaxException {
        log.debug("REST request to save OrderStatus : {}", orderStatusDTO);
        if (orderStatusDTO.getId() != null) {
            throw new BadRequestAlertException("A new orderStatus cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrderStatusDTO result = orderStatusService.save(orderStatusDTO);
        return ResponseEntity.created(new URI("/api/order-statuses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /order-statuses : Updates an existing orderStatus.
     *
     * @param orderStatusDTO the orderStatusDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated orderStatusDTO,
     * or with status 400 (Bad Request) if the orderStatusDTO is not valid,
     * or with status 500 (Internal Server Error) if the orderStatusDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/order-statuses")
    @Timed
    public ResponseEntity<OrderStatusDTO> updateOrderStatus(@Valid @RequestBody OrderStatusDTO orderStatusDTO) throws URISyntaxException {
        log.debug("REST request to update OrderStatus : {}", orderStatusDTO);
        if (orderStatusDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        OrderStatusDTO result = orderStatusService.save(orderStatusDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, orderStatusDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /order-statuses : get all the orderStatuses.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of orderStatuses in body
     */
    @GetMapping("/order-statuses")
    @Timed
    public ResponseEntity<List<OrderStatusDTO>> getAllOrderStatuses(Pageable pageable) {
        log.debug("REST request to get a page of OrderStatuses");
        Page<OrderStatusDTO> page = orderStatusService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/order-statuses");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /order-statuses/:id : get the "id" orderStatus.
     *
     * @param id the id of the orderStatusDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the orderStatusDTO, or with status 404 (Not Found)
     */
    @GetMapping("/order-statuses/{id}")
    @Timed
    public ResponseEntity<OrderStatusDTO> getOrderStatus(@PathVariable Long id) {
        log.debug("REST request to get OrderStatus : {}", id);
        Optional<OrderStatusDTO> orderStatusDTO = orderStatusService.findOne(id);
        return ResponseUtil.wrapOrNotFound(orderStatusDTO);
    }

    /**
     * DELETE  /order-statuses/:id : delete the "id" orderStatus.
     *
     * @param id the id of the orderStatusDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/order-statuses/{id}")
    @Timed
    public ResponseEntity<Void> deleteOrderStatus(@PathVariable Long id) {
        log.debug("REST request to delete OrderStatus : {}", id);
        orderStatusService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/order-statuses?query=:query : search for the orderStatus corresponding
     * to the query.
     *
     * @param query the query of the orderStatus search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/order-statuses")
    @Timed
    public ResponseEntity<List<OrderStatusDTO>> searchOrderStatuses(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of OrderStatuses for query {}", query);
        Page<OrderStatusDTO> page = orderStatusService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/order-statuses");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
}