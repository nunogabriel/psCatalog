package com.cgi.pscatalog.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cgi.pscatalog.service.OrdersHstService;
import com.cgi.pscatalog.web.rest.errors.BadRequestAlertException;
import com.cgi.pscatalog.web.rest.util.HeaderUtil;
import com.cgi.pscatalog.web.rest.util.PaginationUtil;
import com.cgi.pscatalog.service.dto.OrdersHstDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing OrdersHst.
 */
@RestController
@RequestMapping("/api")
public class OrdersHstResource {

    private final Logger log = LoggerFactory.getLogger(OrdersHstResource.class);

    private static final String ENTITY_NAME = "ordersHst";

    private final OrdersHstService ordersHstService;

    public OrdersHstResource(OrdersHstService ordersHstService) {
        this.ordersHstService = ordersHstService;
    }

    /**
     * POST  /orders-hsts : Create a new ordersHst.
     *
     * @param ordersHstDTO the ordersHstDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new ordersHstDTO, or with status 400 (Bad Request) if the ordersHst has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/orders-hsts")
    @Timed
    public ResponseEntity<OrdersHstDTO> createOrdersHst(@Valid @RequestBody OrdersHstDTO ordersHstDTO) throws URISyntaxException {
        log.debug("REST request to save OrdersHst : {}", ordersHstDTO);
        if (ordersHstDTO.getId() != null) {
            throw new BadRequestAlertException("A new ordersHst cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrdersHstDTO result = ordersHstService.save(ordersHstDTO);
        return ResponseEntity.created(new URI("/api/orders-hsts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /orders-hsts : Updates an existing ordersHst.
     *
     * @param ordersHstDTO the ordersHstDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated ordersHstDTO,
     * or with status 400 (Bad Request) if the ordersHstDTO is not valid,
     * or with status 500 (Internal Server Error) if the ordersHstDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/orders-hsts")
    @Timed
    public ResponseEntity<OrdersHstDTO> updateOrdersHst(@Valid @RequestBody OrdersHstDTO ordersHstDTO) throws URISyntaxException {
        log.debug("REST request to update OrdersHst : {}", ordersHstDTO);
        if (ordersHstDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        OrdersHstDTO result = ordersHstService.save(ordersHstDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, ordersHstDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /orders-hsts : get all the ordersHsts.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of ordersHsts in body
     */
    @GetMapping("/orders-hsts")
    @Timed
    public ResponseEntity<List<OrdersHstDTO>> getAllOrdersHsts(Pageable pageable) {
        log.debug("REST request to get a page of OrdersHsts");
        Page<OrdersHstDTO> page = ordersHstService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/orders-hsts");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /orders-hsts/:id : get the "id" ordersHst.
     *
     * @param id the id of the ordersHstDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the ordersHstDTO, or with status 404 (Not Found)
     */
    @GetMapping("/orders-hsts/{id}")
    @Timed
    public ResponseEntity<OrdersHstDTO> getOrdersHst(@PathVariable Long id) {
        log.debug("REST request to get OrdersHst : {}", id);
        Optional<OrdersHstDTO> ordersHstDTO = ordersHstService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ordersHstDTO);
    }

    /**
     * DELETE  /orders-hsts/:id : delete the "id" ordersHst.
     *
     * @param id the id of the ordersHstDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/orders-hsts/{id}")
    @Timed
    public ResponseEntity<Void> deleteOrdersHst(@PathVariable Long id) {
        log.debug("REST request to delete OrdersHst : {}", id);
        ordersHstService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/orders-hsts?query=:query : search for the ordersHst corresponding
     * to the query.
     *
     * @param query the query of the ordersHst search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/orders-hsts")
    @Timed
    public ResponseEntity<List<OrdersHstDTO>> searchOrdersHsts(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of OrdersHsts for query {}", query);
        Page<OrdersHstDTO> page = ordersHstService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/orders-hsts");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
