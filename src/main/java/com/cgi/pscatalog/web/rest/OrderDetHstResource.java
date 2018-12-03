package com.cgi.pscatalog.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cgi.pscatalog.service.OrderDetHstService;
import com.cgi.pscatalog.web.rest.errors.BadRequestAlertException;
import com.cgi.pscatalog.web.rest.util.HeaderUtil;
import com.cgi.pscatalog.web.rest.util.PaginationUtil;
import com.cgi.pscatalog.service.dto.OrderDetHstDTO;
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
 * REST controller for managing OrderDetHst.
 */
@RestController
@RequestMapping("/api")
public class OrderDetHstResource {

    private final Logger log = LoggerFactory.getLogger(OrderDetHstResource.class);

    private static final String ENTITY_NAME = "orderDetHst";

    private final OrderDetHstService orderDetHstService;

    public OrderDetHstResource(OrderDetHstService orderDetHstService) {
        this.orderDetHstService = orderDetHstService;
    }

    /**
     * POST  /order-det-hsts : Create a new orderDetHst.
     *
     * @param orderDetHstDTO the orderDetHstDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new orderDetHstDTO, or with status 400 (Bad Request) if the orderDetHst has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/order-det-hsts")
    @Timed
    public ResponseEntity<OrderDetHstDTO> createOrderDetHst(@Valid @RequestBody OrderDetHstDTO orderDetHstDTO) throws URISyntaxException {
        log.debug("REST request to save OrderDetHst : {}", orderDetHstDTO);
        if (orderDetHstDTO.getId() != null) {
            throw new BadRequestAlertException("A new orderDetHst cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrderDetHstDTO result = orderDetHstService.save(orderDetHstDTO);
        return ResponseEntity.created(new URI("/api/order-det-hsts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /order-det-hsts : Updates an existing orderDetHst.
     *
     * @param orderDetHstDTO the orderDetHstDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated orderDetHstDTO,
     * or with status 400 (Bad Request) if the orderDetHstDTO is not valid,
     * or with status 500 (Internal Server Error) if the orderDetHstDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/order-det-hsts")
    @Timed
    public ResponseEntity<OrderDetHstDTO> updateOrderDetHst(@Valid @RequestBody OrderDetHstDTO orderDetHstDTO) throws URISyntaxException {
        log.debug("REST request to update OrderDetHst : {}", orderDetHstDTO);
        if (orderDetHstDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        OrderDetHstDTO result = orderDetHstService.save(orderDetHstDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, orderDetHstDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /order-det-hsts : get all the orderDetHsts.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of orderDetHsts in body
     */
    @GetMapping("/order-det-hsts")
    @Timed
    public ResponseEntity<List<OrderDetHstDTO>> getAllOrderDetHsts(Pageable pageable) {
        log.debug("REST request to get a page of OrderDetHsts");
        Page<OrderDetHstDTO> page = orderDetHstService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/order-det-hsts");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /order-det-hsts/:id : get the "id" orderDetHst.
     *
     * @param id the id of the orderDetHstDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the orderDetHstDTO, or with status 404 (Not Found)
     */
    @GetMapping("/order-det-hsts/{id}")
    @Timed
    public ResponseEntity<OrderDetHstDTO> getOrderDetHst(@PathVariable Long id) {
        log.debug("REST request to get OrderDetHst : {}", id);
        Optional<OrderDetHstDTO> orderDetHstDTO = orderDetHstService.findOne(id);
        return ResponseUtil.wrapOrNotFound(orderDetHstDTO);
    }

    /**
     * DELETE  /order-det-hsts/:id : delete the "id" orderDetHst.
     *
     * @param id the id of the orderDetHstDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/order-det-hsts/{id}")
    @Timed
    public ResponseEntity<Void> deleteOrderDetHst(@PathVariable Long id) {
        log.debug("REST request to delete OrderDetHst : {}", id);
        orderDetHstService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/order-det-hsts?query=:query : search for the orderDetHst corresponding
     * to the query.
     *
     * @param query the query of the orderDetHst search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/order-det-hsts")
    @Timed
    public ResponseEntity<List<OrderDetHstDTO>> searchOrderDetHsts(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of OrderDetHsts for query {}", query);
        Page<OrderDetHstDTO> page = orderDetHstService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/order-det-hsts");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
