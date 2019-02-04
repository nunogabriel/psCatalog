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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cgi.pscatalog.service.PersonalCatalogService;
import com.cgi.pscatalog.service.dto.PersonalCatalogDTO;
import com.cgi.pscatalog.service.dto.ProductsDTO;
import com.cgi.pscatalog.service.util.PageDataUtil;
import com.cgi.pscatalog.web.rest.errors.BadRequestAlertException;
import com.cgi.pscatalog.web.rest.util.HeaderUtil;
import com.cgi.pscatalog.web.rest.util.PaginationUtil;
import com.codahale.metrics.annotation.Timed;

import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing Personal Catalog.
 */
@RestController
@RequestMapping("/api")
public class PersonalCatalogResource {

	private final Logger log = LoggerFactory.getLogger(PersonalCatalogResource.class);

	private static final String ENTITY_NAME = "personalCatalog";

	private final PersonalCatalogService personalCatalogService;

	public PersonalCatalogResource(PersonalCatalogService personalCatalogService) {
		this.personalCatalogService = personalCatalogService;
	}

    /**
     * PUT  /personalCatalog : add product to shopping card.
     *
     * @param personalCatalogDTO the personalCatalogDTO to update
     *
     * @return the ResponseEntity with status 200 (OK) and with body the updated personalCatalogDTO,
     * or with status 400 (Bad Request) if the personalCatalogDTO is not valid,
     * or with status 500 (Internal Server Error) if the personalCatalogDTO couldn't be updated
     *
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/personalCatalog")
    @Timed
    public ResponseEntity<PersonalCatalogDTO> addBasket(@Valid @RequestBody PersonalCatalogDTO personalCatalogDTO) throws URISyntaxException {
        log.debug("REST request to addBasket : {}", personalCatalogDTO);

        if (personalCatalogDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        // Add product to basket
        personalCatalogService.addBasket(personalCatalogDTO, ENTITY_NAME);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityAddBasketAlert(ENTITY_NAME, personalCatalogDTO.getProductName()))
            .body(personalCatalogDTO);
    }

	/**
	 * GET /personalCatalog : get all the personal catalog.
	 *
	 * @param pageable the pagination information
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of products in
	 *         body
	 */
	@GetMapping("/personalCatalog")
	@Timed
	public ResponseEntity<List<PersonalCatalogDTO>> getAllPersonalCatalog(Pageable pageable) {
		log.debug("REST request to get a page of Personal Catalog");

		// Get all general catalog
		PageDataUtil<Object[], PersonalCatalogDTO> pageDataUtil = personalCatalogService.getAllPersonalCatalog(pageable, ENTITY_NAME);

		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(pageDataUtil.getPageInformation(), "/api/personalCatalog");

		return ResponseEntity.ok().headers(headers).body(pageDataUtil.getContent());
	}

	/**
	 * GET /personalCatalog/:id : get the "id" personal catalog.
	 *
	 * @param id the id of the personalCatalogDTO to retrieve
	 *
	 * @return the ResponseEntity with status 200 (OK) and with body the
	 *         personalCatalogDTO, or with status 404 (Not Found)
	 */
	@GetMapping("/personalCatalog/{id}")
	@Timed
	public ResponseEntity<PersonalCatalogDTO> getPersonalCatalog(@PathVariable Long id) {
		log.debug("REST request to get Personal Catalog : {}", id);

		// Get personal catalog by id
        Optional<PersonalCatalogDTO> personalCatalogDTOOpt = personalCatalogService.getPersonalCatalog(id, ENTITY_NAME);

		return ResponseUtil.wrapOrNotFound(personalCatalogDTOOpt);
	}

	/**
	 * SEARCH /_search/personalCatalog?query=:query : search for the personal catalog
	 * corresponding to the query.
	 *
	 * @param query    the query of the personal catalog search
	 * @param pageable the pagination information
	 *
	 * @return the result of the search
	 */
	@GetMapping("/_search/personalCatalog")
	@Timed
	public ResponseEntity<List<PersonalCatalogDTO>> searchPersonalCatalog(@RequestParam String query, Pageable pageable) {
		log.debug("REST request to search for a page of Personal Catalog for query {}", query);

		// Search general catalog
		PageDataUtil<ProductsDTO, PersonalCatalogDTO> pageDataUtil = personalCatalogService.searchPersonalCatalog(query, pageable);

		HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, pageDataUtil.getPageInformation(),
				"/api/_search/personalCatalog");

		return new ResponseEntity<>(pageDataUtil.getContent(), headers, HttpStatus.OK);
	}

    /**
     * GET /personalCatalog/:id/deletePersonal : delete the "id" products.
     *
     * @param id the id of the productsDTO to delete
     *
     * @return the ResponseEntity with status 200 (OK)
     *
     * @throws URISyntaxException
     */
	@GetMapping("/personalCatalog/{id}/deletePersonal")
    @Timed
    public ResponseEntity<Void> deletePersonalCatalog(@PathVariable Long id) throws URISyntaxException {
        log.debug("REST request to delete personal catalog : {}", id);

        // Delete product from personal catalog
        String productName = personalCatalogService.deletePersonalCatalog(id, ENTITY_NAME);

        if ( productName.equals("-1") ) {
        	return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletePersonalDoesNotExistAlert(ENTITY_NAME, "")).build();
        } else {
        	return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletePersonalAlert(ENTITY_NAME, productName)).build();
        }
    }
}