package com.cgi.pscatalog.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cgi.pscatalog.security.SecurityUtils;
import com.cgi.pscatalog.service.OrderDetService;
import com.cgi.pscatalog.web.rest.errors.BadRequestAlertException;
import com.cgi.pscatalog.web.rest.util.HeaderUtil;
import com.cgi.pscatalog.web.rest.util.PaginationUtil;
import com.cgi.pscatalog.service.dto.OrderDetDTO;
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
import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing OrderDet.
 */
@RestController
@RequestMapping("/api")
public class OrderDetResource {

    private final Logger log = LoggerFactory.getLogger(OrderDetResource.class);

    private static final String ENTITY_NAME = "orderDet";

    private final OrderDetService orderDetService;

    public OrderDetResource(OrderDetService orderDetService) {
        this.orderDetService = orderDetService;
    }

    /**
     * POST  /order-dets : Create a new orderDet.
     *
     * @param orderDetDTO the orderDetDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new orderDetDTO, or with status 400 (Bad Request) if the orderDet has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/order-dets")
    @Timed
    public ResponseEntity<OrderDetDTO> createOrderDet(@Valid @RequestBody OrderDetDTO orderDetDTO) throws URISyntaxException {
        log.debug("REST request to save OrderDet : {}", orderDetDTO);
        
        if (orderDetDTO.getId() != null) {
            throw new BadRequestAlertException("A new orderDet cannot already have an ID", ENTITY_NAME, "idexists");
        }
        
        orderDetDTO.setCreatedBy((SecurityUtils.getCurrentUserLogin().isPresent())?(SecurityUtils.getCurrentUserLogin().get()):"anonymousUser");
        orderDetDTO.setCreatedDate(Instant.now());
        
        OrderDetDTO result = orderDetService.save(orderDetDTO);
        
        return ResponseEntity.created(new URI("/api/order-dets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /order-dets : Updates an existing orderDet.
     *
     * @param orderDetDTO the orderDetDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated orderDetDTO,
     * or with status 400 (Bad Request) if the orderDetDTO is not valid,
     * or with status 500 (Internal Server Error) if the orderDetDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/order-dets")
    @Timed
    public ResponseEntity<OrderDetDTO> updateOrderDet(@Valid @RequestBody OrderDetDTO orderDetDTO) throws URISyntaxException {
        log.debug("REST request to update OrderDet : {}", orderDetDTO);
        
        if (orderDetDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        
        orderDetDTO.setLastModifiedBy((SecurityUtils.getCurrentUserLogin().isPresent())?(SecurityUtils.getCurrentUserLogin().get()):"anonymousUser");
        orderDetDTO.setLastModifiedDate(Instant.now());
        
        OrderDetDTO result = orderDetService.save(orderDetDTO);
        
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, orderDetDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /order-dets : get all the orderDets.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of orderDets in body
     */
    @GetMapping("/order-dets")
    @Timed
    public ResponseEntity<List<OrderDetDTO>> getAllOrderDets(Pageable pageable) {
        log.debug("REST request to get a page of OrderDets");
        Page<OrderDetDTO> page = orderDetService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/order-dets");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /order-dets/:id : get the "id" orderDet.
     *
     * @param id the id of the orderDetDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the orderDetDTO, or with status 404 (Not Found)
     */
    @GetMapping("/order-dets/{id}")
    @Timed
    public ResponseEntity<OrderDetDTO> getOrderDet(@PathVariable Long id) {
        log.debug("REST request to get OrderDet : {}", id);
        Optional<OrderDetDTO> orderDetDTO = orderDetService.findOne(id);
        return ResponseUtil.wrapOrNotFound(orderDetDTO);
    }

    /**
     * DELETE  /order-dets/:id : delete the "id" orderDet.
     *
     * @param id the id of the orderDetDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/order-dets/{id}")
    @Timed
    public ResponseEntity<Void> deleteOrderDet(@PathVariable Long id) {
        log.debug("REST request to delete OrderDet : {}", id);
        orderDetService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/order-dets?query=:query : search for the orderDet corresponding
     * to the query.
     *
     * @param query the query of the orderDet search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/order-dets")
    @Timed
    public ResponseEntity<List<OrderDetDTO>> searchOrderDets(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of OrderDets for query {}", query);
        Page<OrderDetDTO> page = orderDetService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/order-dets");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
