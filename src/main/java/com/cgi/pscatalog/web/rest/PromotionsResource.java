package com.cgi.pscatalog.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cgi.pscatalog.security.SecurityUtils;
import com.cgi.pscatalog.service.PromotionsService;
import com.cgi.pscatalog.web.rest.errors.BadRequestAlertException;
import com.cgi.pscatalog.web.rest.util.HeaderUtil;
import com.cgi.pscatalog.web.rest.util.PaginationUtil;
import com.cgi.pscatalog.service.dto.PromotionsDTO;
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
 * REST controller for managing Promotions.
 */
@RestController
@RequestMapping("/api")
public class PromotionsResource {

    private final Logger log = LoggerFactory.getLogger(PromotionsResource.class);

    private static final String ENTITY_NAME = "promotions";

    private final PromotionsService promotionsService;

    public PromotionsResource(PromotionsService promotionsService) {
        this.promotionsService = promotionsService;
    }

    /**
     * POST  /promotions : Create a new promotions.
     *
     * @param promotionsDTO the promotionsDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new promotionsDTO, or with status 400 (Bad Request) if the promotions has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/promotions")
    @Timed
    public ResponseEntity<PromotionsDTO> createPromotions(@Valid @RequestBody PromotionsDTO promotionsDTO) throws URISyntaxException {
        log.debug("REST request to save Promotions : {}", promotionsDTO);
        
        if (promotionsDTO.getId() != null) {
            throw new BadRequestAlertException("A new promotions cannot already have an ID", ENTITY_NAME, "idexists");
        }
        
        promotionsDTO.setCreatedBy((SecurityUtils.getCurrentUserLogin().isPresent())?(SecurityUtils.getCurrentUserLogin().get()):"anonymousUser");
        promotionsDTO.setCreatedDate(Instant.now());
        
        PromotionsDTO result = promotionsService.save(promotionsDTO);
        
        return ResponseEntity.created(new URI("/api/promotions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /promotions : Updates an existing promotions.
     *
     * @param promotionsDTO the promotionsDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated promotionsDTO,
     * or with status 400 (Bad Request) if the promotionsDTO is not valid,
     * or with status 500 (Internal Server Error) if the promotionsDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/promotions")
    @Timed
    public ResponseEntity<PromotionsDTO> updatePromotions(@Valid @RequestBody PromotionsDTO promotionsDTO) throws URISyntaxException {
        log.debug("REST request to update Promotions : {}", promotionsDTO);
        
        if (promotionsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        
        promotionsDTO.setLastModifiedBy((SecurityUtils.getCurrentUserLogin().isPresent())?(SecurityUtils.getCurrentUserLogin().get()):"anonymousUser");
        promotionsDTO.setLastModifiedDate(Instant.now());
        
        PromotionsDTO result = promotionsService.save(promotionsDTO);
        
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, promotionsDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /promotions : get all the promotions.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of promotions in body
     */
    @GetMapping("/promotions")
    @Timed
    public ResponseEntity<List<PromotionsDTO>> getAllPromotions(Pageable pageable) {
        log.debug("REST request to get a page of Promotions");
        Page<PromotionsDTO> page = promotionsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/promotions");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /promotions/:id : get the "id" promotions.
     *
     * @param id the id of the promotionsDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the promotionsDTO, or with status 404 (Not Found)
     */
    @GetMapping("/promotions/{id}")
    @Timed
    public ResponseEntity<PromotionsDTO> getPromotions(@PathVariable Long id) {
        log.debug("REST request to get Promotions : {}", id);
        Optional<PromotionsDTO> promotionsDTO = promotionsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(promotionsDTO);
    }

    /**
     * DELETE  /promotions/:id : delete the "id" promotions.
     *
     * @param id the id of the promotionsDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/promotions/{id}")
    @Timed
    public ResponseEntity<Void> deletePromotions(@PathVariable Long id) {
        log.debug("REST request to delete Promotions : {}", id);
        promotionsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/promotions?query=:query : search for the promotions corresponding
     * to the query.
     *
     * @param query the query of the promotions search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/promotions")
    @Timed
    public ResponseEntity<List<PromotionsDTO>> searchPromotions(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Promotions for query {}", query);
        Page<PromotionsDTO> page = promotionsService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/promotions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
