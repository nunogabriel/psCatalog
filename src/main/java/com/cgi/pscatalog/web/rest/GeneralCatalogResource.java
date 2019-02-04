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

import com.cgi.pscatalog.service.GeneralCatalogService;
import com.cgi.pscatalog.service.dto.GeneralCatalogDTO;
import com.cgi.pscatalog.service.dto.ProductsDTO;
import com.cgi.pscatalog.service.util.PageDataUtil;
import com.cgi.pscatalog.web.rest.errors.BadRequestAlertException;
import com.cgi.pscatalog.web.rest.errors.ProductAlreadyInPersonalCatalogException;
import com.cgi.pscatalog.web.rest.util.HeaderUtil;
import com.cgi.pscatalog.web.rest.util.PaginationUtil;
import com.codahale.metrics.annotation.Timed;

import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing General Catalog.
 */
@RestController
@RequestMapping("/api")
public class GeneralCatalogResource {

	private final Logger log = LoggerFactory.getLogger(GeneralCatalogResource.class);

	private static final String ENTITY_NAME = "generalCatalog";

	private final GeneralCatalogService generalCatalogService;

	public GeneralCatalogResource(GeneralCatalogService generalCatalogService) {
		this.generalCatalogService = generalCatalogService;
	}

    /**
     * PUT  /generalCatalog : add product to shopping card.
     *
     * @param generalCatalogDTO the generalCatalogDTO to update
     *
     * @return the ResponseEntity with status 200 (OK) and with body the updated generalCatalogDTO,
     * or with status 400 (Bad Request) if the generalCatalogDTO is not valid,
     * or with status 500 (Internal Server Error) if the generalCatalogDTO couldn't be updated
     *
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/generalCatalog")
    @Timed
    public ResponseEntity<GeneralCatalogDTO> addBasket(@Valid @RequestBody GeneralCatalogDTO generalCatalogDTO) throws URISyntaxException {
        log.debug("REST request to addBasket : {}", generalCatalogDTO);

        if (generalCatalogDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        // Add product to basket
        generalCatalogService.addBasket(generalCatalogDTO, ENTITY_NAME);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityAddBasketAlert(ENTITY_NAME, generalCatalogDTO.getProductName()))
            .body(generalCatalogDTO);
    }

	/**
	 * GET /generalCatalog : get all the general catalog.
	 *
	 * @param pageable the pagination information
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of products in
	 *         body
	 */
	@GetMapping("/generalCatalog")
	@Timed
	public ResponseEntity<List<GeneralCatalogDTO>> getAllGeneralCatalog(Pageable pageable) {
		log.debug("REST request to get a page of General Catalog");

		// Get all general catalog
		PageDataUtil<Object[], GeneralCatalogDTO> pageDataUtil = generalCatalogService.getAllGeneralCatalog(pageable, ENTITY_NAME);

		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(pageDataUtil.getPageInformation(), "/api/generalCatalog");

		return ResponseEntity.ok().headers(headers).body(pageDataUtil.getContent());
	}

	/**
	 * GET /generalCatalog/:id : get the "id" general catalog.
	 *
	 * @param id the id of the generalCatalogDTO to retrieve
	 *
	 * @return the ResponseEntity with status 200 (OK) and with body the
	 *         generalCatalogDTO, or with status 404 (Not Found)
	 */
	@GetMapping("/generalCatalog/{id}")
	@Timed
	public ResponseEntity<GeneralCatalogDTO> getGeneralCatalog(@PathVariable Long id) {
		log.debug("REST request to get General Catalog : {}", id);

        // Get general catalog by id
        Optional<GeneralCatalogDTO> generalCatalogDTOOpt = generalCatalogService.getGeneralCatalog(id);

		return ResponseUtil.wrapOrNotFound(generalCatalogDTOOpt);
	}

	/**
	 * SEARCH /_search/generalCatalog?query=:query : search for the general catalog
	 * corresponding to the query.
	 *
	 * @param query    the query of the general catalog search
	 * @param pageable the pagination information
	 *
	 * @return the result of the search
	 */
	@GetMapping("/_search/generalCatalog")
	@Timed
	public ResponseEntity<List<GeneralCatalogDTO>> searchGeneralCatalog(@RequestParam String query, Pageable pageable) {
		log.debug("REST request to search for a page of General Catalog for query {}", query);

        // Search general catalog
		PageDataUtil<ProductsDTO, GeneralCatalogDTO> pageDataUtil = generalCatalogService.searchGeneralCatalog(query, pageable);

		HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, pageDataUtil.getPageInformation(),
				"/api/_search/generalCatalog");

		return new ResponseEntity<>(pageDataUtil.getContent(), headers, HttpStatus.OK);
	}

    /**
     * GET /generalCatalog/:id/addPersonal : delete the "id" products.
     *
     * @param id the id of the productsDTO to delete
     *
     * @return the ResponseEntity with status 200 (OK)
     *
     * @throws URISyntaxException
     */
	@GetMapping("/generalCatalog/{id}/addPersonal")
    @Timed
    public ResponseEntity<Void> addPersonalCatalog(@PathVariable Long id) throws URISyntaxException {
        log.debug("REST request to add personal catalog : id {}", id);

        String productName = "";

        try {
	        // Add product to personal catalog
	        productName = generalCatalogService.addPersonalCatalog(id, ENTITY_NAME);
        } catch (ProductAlreadyInPersonalCatalogException e) {
        	return ResponseEntity.ok().headers(HeaderUtil.createEntityAddPersonalAlreadyAlert(ENTITY_NAME, e.getMessage())).build();
        }

        log.debug("REST request to add personal catalog: product name {}", productName);

        return ResponseEntity.ok().headers(HeaderUtil.createEntityAddPersonalAlert(ENTITY_NAME, productName)).build();
    }
}