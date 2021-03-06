package com.cgi.pscatalog.web.rest;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cgi.pscatalog.service.CartOrderDetService;
import com.cgi.pscatalog.service.dto.CartOrderDetDTO;
import com.cgi.pscatalog.service.dto.OrderDetDTO;
import com.cgi.pscatalog.service.dto.OrdersDTO;
import com.cgi.pscatalog.service.util.PageDataUtil;
import com.cgi.pscatalog.web.rest.errors.BadRequestAlertException;
import com.cgi.pscatalog.web.rest.util.HeaderUtil;
import com.cgi.pscatalog.web.rest.util.PaginationUtil;
import com.codahale.metrics.annotation.Timed;

import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing OrderDet.
 */
@RestController
@RequestMapping("/api")
public class CartOrderDetResource {

    private final Logger log = LoggerFactory.getLogger(CartOrderDetResource.class);

    private static final String ENTITY_NAME = "cartOrderDet";

    private final CartOrderDetService cartOrderDetService;

    public CartOrderDetResource(CartOrderDetService cartOrderDetService) {
        this.cartOrderDetService = cartOrderDetService;
    }

    /**
     * GET  /cart-order-det/order/:addressId/:deliveryAddressId : Create a new cartOrderDet.
     *
     * @param cartOrderDetDTO the cartOrderDetDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cartOrderDetDTO, or with status 400 (Bad Request) if the orderDet has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @GetMapping("/cart-order-det/order/{addressId}/{deliveryAddressId}")
    @Timed
    public ResponseEntity<Void> createCartOrderDet(@PathVariable Long addressId, @PathVariable Long deliveryAddressId) throws URISyntaxException {
        log.debug("REST request to save CartOrderDet");

        // Create cart order detail
        OrdersDTO ordersDTO = cartOrderDetService.createCartOrderDet(addressId, deliveryAddressId, ENTITY_NAME);

		return ResponseEntity.ok().headers(HeaderUtil.createEntityOrderAlert(ENTITY_NAME, ordersDTO.getOrderReference())).build();
    }

    /**
     * PUT  /cart-order-det : Updates an existing cartOrderDet.
     *
     * @param cartOrderDetDTO the cartOrderDetDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cartOrderDetDTO,
     * or with status 400 (Bad Request) if the cartOrderDetDTO is not valid,
     * or with status 500 (Internal Server Error) if the cartOrderDetDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/cart-order-det")
    @Timed
    public ResponseEntity<CartOrderDetDTO> updateCartOrderDet(@Valid @RequestBody CartOrderDetDTO cartOrderDetDTO) throws URISyntaxException {
        log.debug("REST request to update CartOrderDet : {}", cartOrderDetDTO);

        if (cartOrderDetDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        // Update cart order detail
        cartOrderDetService.updateCartOrderDet(cartOrderDetDTO, ENTITY_NAME);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, cartOrderDetDTO.getProductProductName()))
            .body(cartOrderDetDTO);
    }

    /**
     * GET  /cart-order-det : get all the orderDets.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of orderDets in body
     */
    @GetMapping("/cart-order-det")
    @Timed
    public ResponseEntity<List<CartOrderDetDTO>> getAllCartOrderDets(Pageable pageable) throws URISyntaxException {
        log.debug("REST request to get a page of CartOrderDets");

        // Get all cart order detail
        PageDataUtil<OrderDetDTO, CartOrderDetDTO> pageDataUtil = cartOrderDetService.getAllCartOrderDets(pageable, ENTITY_NAME);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(pageDataUtil.getPageInformation(), "/api/cart-order-det");

        return ResponseEntity.ok().headers(headers).body(pageDataUtil.getContent());
    }

    /**
     * GET  /cart-order-det/:id : get the "id" orderDet.
     *
     * @param id the id of the cartOrderDetDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cartOrderDetDTO, or with status 404 (Not Found)
     */
    @GetMapping("/cart-order-det/{id}")
    @Timed
    public ResponseEntity<CartOrderDetDTO> getCartOrderDet(@PathVariable Long id) {
        log.debug("REST request to get CartOrderDet : {}", id);

        // Get cart order detail by id
        Optional<CartOrderDetDTO> cartOrderDetDTOOpt = cartOrderDetService.getCartOrderDet(id);

        return ResponseUtil.wrapOrNotFound(cartOrderDetDTOOpt);
    }

    /**
     * DELETE  /cart-order-det/:id : delete the "id" orderDet.
     *
     * @param id the id of the cartOrderDetDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/cart-order-det/{id}")
    @Timed
    public ResponseEntity<Void> deleteCartOrderDet(@PathVariable Long id) {
        log.debug("REST request to delete CatOrderDet : {}", id);

        // Delete cart order detail
        cartOrderDetService.deleteCartOrderDet(id);

        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, "")).build();
    }

    /**
     * SEARCH  /_search/cart-order-det?query=:query : search for the orderDet corresponding
     * to the query.
     *
     * @param query the query of the orderDet search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/cart-order-det")
    @Timed
    public ResponseEntity<List<CartOrderDetDTO>> searchCatOrderDets(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of CatOrderDets for query {}", query);

        // Search cart order detail
        PageDataUtil<OrderDetDTO, CartOrderDetDTO> pageDataUtil = cartOrderDetService.searchCatOrderDets(query, pageable);

        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, pageDataUtil.getPageInformation(), "/api/_search/cart-order-det");

        return new ResponseEntity<>(pageDataUtil.getContent(), headers, HttpStatus.OK);
    }
}