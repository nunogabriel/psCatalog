package com.cgi.pscatalog.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cgi.pscatalog.security.SecurityUtils;
import com.cgi.pscatalog.service.ProductsService;
import com.cgi.pscatalog.web.rest.errors.BadRequestAlertException;
import com.cgi.pscatalog.web.rest.util.HeaderUtil;
import com.cgi.pscatalog.web.rest.util.PaginationUtil;
import com.cgi.pscatalog.service.dto.ProductsDTO;
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
 * REST controller for managing Products.
 */
@RestController
@RequestMapping("/api")
public class ProductsResource {

    private final Logger log = LoggerFactory.getLogger(ProductsResource.class);

    private static final String ENTITY_NAME = "products";

    private final ProductsService productsService;

    public ProductsResource(ProductsService productsService) {
        this.productsService = productsService;
    }

    /**
     * POST  /products : Create a new products.
     *
     * @param productsDTO the productsDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new productsDTO, or with status 400 (Bad Request) if the products has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/products")
    @Timed
    public ResponseEntity<ProductsDTO> createProducts(@Valid @RequestBody ProductsDTO productsDTO) throws URISyntaxException {
        log.debug("REST request to save Products : {}", productsDTO);
        
        if (productsDTO.getId() != null) {
            throw new BadRequestAlertException("A new products cannot already have an ID", ENTITY_NAME, "idexists");
        }
        
        productsDTO.setCreatedBy((SecurityUtils.getCurrentUserLogin().isPresent())?(SecurityUtils.getCurrentUserLogin().get()):"anonymousUser");
        productsDTO.setCreatedDate(Instant.now());
        
        ProductsDTO result = productsService.save(productsDTO);
        
        return ResponseEntity.created(new URI("/api/products/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /products : Updates an existing products.
     *
     * @param productsDTO the productsDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated productsDTO,
     * or with status 400 (Bad Request) if the productsDTO is not valid,
     * or with status 500 (Internal Server Error) if the productsDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/products")
    @Timed
    public ResponseEntity<ProductsDTO> updateProducts(@Valid @RequestBody ProductsDTO productsDTO) throws URISyntaxException {
        log.debug("REST request to update Products : {}", productsDTO);
        
        if (productsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        
        productsDTO.setLastModifiedBy((SecurityUtils.getCurrentUserLogin().isPresent())?(SecurityUtils.getCurrentUserLogin().get()):"anonymousUser");
        productsDTO.setLastModifiedDate(Instant.now());
        
        ProductsDTO result = productsService.save(productsDTO);
        
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, productsDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /products : get all the products.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of products in body
     */
    @GetMapping("/products")
    @Timed
    public ResponseEntity<List<ProductsDTO>> getAllProducts(Pageable pageable) {
        log.debug("REST request to get a page of Products");
        Page<ProductsDTO> page = productsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/products");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /products/:id : get the "id" products.
     *
     * @param id the id of the productsDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the productsDTO, or with status 404 (Not Found)
     */
    @GetMapping("/products/{id}")
    @Timed
    public ResponseEntity<ProductsDTO> getProducts(@PathVariable Long id) {
        log.debug("REST request to get Products : {}", id);
        Optional<ProductsDTO> productsDTO = productsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productsDTO);
    }

    /**
     * DELETE  /products/:id : delete the "id" products.
     *
     * @param id the id of the productsDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/products/{id}")
    @Timed
    public ResponseEntity<Void> deleteProducts(@PathVariable Long id) {
        log.debug("REST request to delete Products : {}", id);
        productsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/products?query=:query : search for the products corresponding
     * to the query.
     *
     * @param query the query of the products search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/products")
    @Timed
    public ResponseEntity<List<ProductsDTO>> searchProducts(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Products for query {}", query);
        Page<ProductsDTO> page = productsService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/products");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
