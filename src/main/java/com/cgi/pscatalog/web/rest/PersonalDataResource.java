package com.cgi.pscatalog.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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

import com.cgi.pscatalog.security.SecurityUtils;
import com.cgi.pscatalog.service.AddressesService;
import com.cgi.pscatalog.service.CustomersService;
import com.cgi.pscatalog.service.OrderDetService;
import com.cgi.pscatalog.service.OrdersService;
import com.cgi.pscatalog.service.dto.AddressesDTO;
import com.cgi.pscatalog.service.dto.CustomersDTO;
import com.cgi.pscatalog.service.dto.OrderDetDTO;
import com.cgi.pscatalog.service.dto.OrdersDTO;
import com.cgi.pscatalog.service.dto.PersonalDataDTO;
import com.cgi.pscatalog.web.rest.errors.BadRequestAlertException;
import com.cgi.pscatalog.web.rest.util.HeaderUtil;
import com.cgi.pscatalog.web.rest.util.PaginationUtil;
import com.codahale.metrics.annotation.Timed;

import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing Personal Data.
 */
@RestController
@RequestMapping("/api")
public class PersonalDataResource {

    private final Logger log = LoggerFactory.getLogger(PersonalDataResource.class);

    private static final String ENTITY_NAME = "personalData";

    private final CustomersService customersService;

    @Autowired
    private OrderDetService orderDetService;

    @Autowired
    private AddressesService addressesService;

    @Autowired
    private OrdersService ordersService;

    public PersonalDataResource(CustomersService customersService) {
        this.customersService = customersService;
    }

    /**
     * POST  /personalData : Create a new personal data.
     *
     * @param personalDataDTO the personalDataDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new personalDataDTO, or with status 400 (Bad Request) if the customers has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/personalData")
    @Timed
    public ResponseEntity<PersonalDataDTO> createPersonalData(@Valid @RequestBody PersonalDataDTO personalDataDTO) throws URISyntaxException {
        log.debug("REST request to save personal data : {}", personalDataDTO);

        if (personalDataDTO.getId() != null) {
            throw new BadRequestAlertException("A new personal data cannot already have an ID", ENTITY_NAME, "idexists");
        }

        CustomersDTO customersDTO = new CustomersDTO();
        customersDTO.setCustomerEmail(personalDataDTO.getCustomerEmail());
        customersDTO.setCustomerGender(personalDataDTO.getCustomerGender());
        customersDTO.setCustomerName(personalDataDTO.getCustomerName());
        customersDTO.setCustomerNif(personalDataDTO.getCustomerNif());
        customersDTO.setCustomerPhone(personalDataDTO.getCustomerPhone());
        customersDTO.setCustomerBeginDate(Instant.now());
        customersDTO.setCustomerEndDate(null);
        customersDTO.setLogin(SecurityUtils.getCurrentUserLogin().get());
        customersDTO.setCreatedBy((SecurityUtils.getCurrentUserLogin().isPresent())?(SecurityUtils.getCurrentUserLogin().get()):"anonymousUser");
        customersDTO.setCreatedDate(Instant.now());

        CustomersDTO customersDTOAux = customersService.save(customersDTO);

        personalDataDTO.setId(customersDTOAux.getId());

        return ResponseEntity.created(new URI("/api/personalData/" + personalDataDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, ""))
            .body(personalDataDTO);
    }

    /**
     * PUT  /personalData : Updates an existing personal data.
     *
     * @param personalDataDTO the personalDataDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated personalDataDTO,
     * or with status 400 (Bad Request) if the personalDataDTO is not valid,
     * or with status 500 (Internal Server Error) if the personalDataDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/personalData")
    @Timed
    public ResponseEntity<PersonalDataDTO> updatePersonalData(@Valid @RequestBody PersonalDataDTO personalDataDTO) throws URISyntaxException {
        log.debug("REST request to update personal data : {}", personalDataDTO);

        if (personalDataDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        // Get login
        String login = SecurityUtils.getCurrentUserLogin().get();

        // Get customer id
        Long customerId = personalDataDTO.getId();

        // Verify if already exists orders with PENDING Status
        Page<OrdersDTO> pageOrdersPending = ordersService.getAllByLoginAndCustomerIdAndOrderStatusPending(login, customerId, PageRequest.of(0, 1));

        List<OrdersDTO> listOrdersDTOPending = pageOrdersPending.getContent();

        // Verify if already exists orders with status different from PENDING
        Page<OrdersDTO> pageOrders = ordersService.getAllByLoginAndCustomerIdAndOrderStatus(login, customerId, PageRequest.of(0, 1000));

        List<OrdersDTO> listOrdersDTO = pageOrders.getContent();

        //
        if ( listOrdersDTOPending.size() == 0 && listOrdersDTO.size() == 0 ) {
        	// There are not orders
	        Optional<CustomersDTO> customersDTOOpt = customersService.findOne(customerId);

			if (customersDTOOpt.isPresent()) {
				CustomersDTO customersDTO = customersDTOOpt.get();

		        customersDTO.setCustomerEmail(personalDataDTO.getCustomerEmail());
		        customersDTO.setCustomerGender(personalDataDTO.getCustomerGender());
		        customersDTO.setCustomerName(personalDataDTO.getCustomerName());
		        customersDTO.setCustomerNif(personalDataDTO.getCustomerNif());
		        customersDTO.setCustomerPhone(personalDataDTO.getCustomerPhone());
		        customersDTO.setCustomerBeginDate(personalDataDTO.getCustomerBeginDate());
		        customersDTO.setLogin(SecurityUtils.getCurrentUserLogin().get());
		        customersDTO.setLastModifiedBy(login);
		        customersDTO.setLastModifiedDate(Instant.now());

		        customersService.save(customersDTO);
			}
        } else {
        	// Create new customer with customer products associated
            CustomersDTO customersDTONew = new CustomersDTO();
            customersDTONew.setCustomerEmail(personalDataDTO.getCustomerEmail());
            customersDTONew.setCustomerGender(personalDataDTO.getCustomerGender());
            customersDTONew.setCustomerName(personalDataDTO.getCustomerName());
            customersDTONew.setCustomerNif(personalDataDTO.getCustomerNif());
            customersDTONew.setCustomerPhone(personalDataDTO.getCustomerPhone());
            customersDTONew.setCustomerBeginDate(personalDataDTO.getCustomerBeginDate());
            customersDTONew.setCustomerEndDate(null);
            customersDTONew.setLogin(SecurityUtils.getCurrentUserLogin().get());
            customersDTONew.setCreatedBy(personalDataDTO.getCreatedBy());
            customersDTONew.setCreatedDate(personalDataDTO.getCreatedDate());
            customersDTONew.setProducts(personalDataDTO.getProducts());

            customersDTONew = customersService.save(customersDTONew);

			// Delete old customer and remove products from it (delete records from customer_products table)
            Optional<CustomersDTO> customersDTOOptOld = customersService.findOne(customerId);

    		if (customersDTOOptOld.isPresent()) {
    			CustomersDTO customersDTOOld = customersDTOOptOld.get();

    			customersDTOOld.setCustomerEndDate(Instant.now());
    			customersDTOOld.setLastModifiedBy(login);
    			customersDTOOld.setLastModifiedDate(Instant.now());

    			if ( listOrdersDTO.size() == 0 ) {
    				// If the are not orders with status different from PENDING
    				customersDTOOld.setProducts(null);
    			}

    	        customersService.save(customersDTOOld);
    		}

        	// Add new addresses with new customer data
			Page<AddressesDTO> pageAddressesDTO = addressesService.getAddressesByLoginAndCustomerId(login, customerId, PageRequest.of(0, 1000));
			List<AddressesDTO> listAddressesDTO = pageAddressesDTO.getContent();

			Map<Long,Long> mapOldNewAddresses = new HashMap<Long,Long>();

			for (Iterator<AddressesDTO> iteratorAddressesDTO = listAddressesDTO.iterator(); iteratorAddressesDTO.hasNext();) {
				AddressesDTO addressesDTO = iteratorAddressesDTO.next();

		        // Add new addresses
		        AddressesDTO addressesDTONew = new AddressesDTO();
		        addressesDTONew.setAddressName(addressesDTO.getAddressName());
		        addressesDTONew.setAddressReference(addressesDTO.getAddressReference());
		        addressesDTONew.setCity(addressesDTO.getCity());
		        addressesDTONew.setCountryCountryName(addressesDTO.getCountryCountryName());
		        addressesDTONew.setCountryId(addressesDTO.getCountryId());
		        addressesDTONew.setCustomerId(customersDTONew.getId());
		        addressesDTONew.setPhoneNumber(addressesDTO.getPhoneNumber());
		        addressesDTONew.setState(addressesDTO.getState());
		        addressesDTONew.setStreetAddress(addressesDTO.getStreetAddress());
		        addressesDTONew.setZipCode(addressesDTO.getZipCode());
		        addressesDTONew.setCreatedBy(addressesDTO.getCreatedBy());
		        addressesDTONew.setCreatedDate(addressesDTO.getCreatedDate());

		        addressesDTONew = addressesService.save(addressesDTONew);

		        mapOldNewAddresses.put(addressesDTO.getId(), addressesDTONew.getId());

				// Delete old addresses = Update address_end_date
		        addressesDTO.setAddressEndDate(Instant.now());
		        addressesDTO.setLastModifiedBy(login);
		        addressesDTO.setLastModifiedDate(Instant.now());

		        addressesService.save(addressesDTO);
			}

        	if ( listOrdersDTOPending.size() != 0 ) {
        		// Update PENDING orders with new customer data
	        	for (Iterator<OrdersDTO> iterator = listOrdersDTOPending.iterator(); iterator.hasNext();) {
					OrdersDTO ordersDTO = iterator.next();

					ordersDTO.setCustomerId(customersDTONew.getId());

					if ( mapOldNewAddresses.containsKey(ordersDTO.getAddressId()) ) {
						ordersDTO.setAddressId(mapOldNewAddresses.get(ordersDTO.getAddressId()));
					}

					if ( mapOldNewAddresses.containsKey(ordersDTO.getDeliveryAddressId()) ) {
						ordersDTO.setDeliveryAddressId(mapOldNewAddresses.get(ordersDTO.getDeliveryAddressId()));
					}

					ordersService.save(ordersDTO);
					break;
				}
        	}
        }

        return ResponseEntity.ok()
	            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, ""))
	            .body(personalDataDTO);
    }

    /**
     * GET  /personalData : get all the personal data.
     *
     * @param pageable the pagination information
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many)
     * @return the ResponseEntity with status 200 (OK) and the list of customers in body
     */
    @GetMapping("/personalData")
    @Timed
    public ResponseEntity<List<PersonalDataDTO>> getAllPersonalData(Pageable pageable, @RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get a page of personal data");

        // Get customer identification by login
        Optional<CustomersDTO> optionalCustomersDTO = customersService.getCustomersByLogin(SecurityUtils.getCurrentUserLogin().get());

        List<PersonalDataDTO> listPersonalDataDTO = new ArrayList<PersonalDataDTO>();

        if ( optionalCustomersDTO.isPresent() ) {
        	CustomersDTO customersDTO = optionalCustomersDTO.get();

    		PersonalDataDTO personalDataDTO = new PersonalDataDTO();
    		personalDataDTO.setId(customersDTO.getId());
    		personalDataDTO.setCustomerEmail(customersDTO.getCustomerEmail());
    		personalDataDTO.setCustomerGender(customersDTO.getCustomerGender());
    		personalDataDTO.setCustomerName(customersDTO.getCustomerName());
    		personalDataDTO.setCustomerNif(customersDTO.getCustomerNif());
    		personalDataDTO.setCustomerPhone(customersDTO.getCustomerPhone());
    		personalDataDTO.setCustomerBeginDate(customersDTO.getCustomerBeginDate());

    		listPersonalDataDTO.add(personalDataDTO);
        }

		Page<PersonalDataDTO> page = new PageImpl<PersonalDataDTO>(listPersonalDataDTO, pageable, listPersonalDataDTO.size());

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, String.format("/api/personalData?eagerload=%b", eagerload));

        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /personalData/:id : get the "id" personal data.
     *
     * @param id the id of the personalDataDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the personalDataDTO, or with status 404 (Not Found)
     */
    @GetMapping("/personalData/{id}")
    @Timed
    public ResponseEntity<PersonalDataDTO> getPersonalData(@PathVariable Long id) {
        log.debug("REST request to get personal data : {}", id);

        Optional<CustomersDTO> customersDTOOpt = customersService.findOne(id);

        Optional<PersonalDataDTO> personalDataDTOOpt = Optional.empty();

		if (customersDTOOpt.isPresent()) {
			CustomersDTO customersDTO = customersDTOOpt.get();

			PersonalDataDTO personalDataDTO = new PersonalDataDTO();
			personalDataDTO.setId(customersDTO.getId());
			personalDataDTO.setCustomerEmail(customersDTO.getCustomerEmail());
			personalDataDTO.setCustomerGender(customersDTO.getCustomerGender());
			personalDataDTO.setCustomerName(customersDTO.getCustomerName());
			personalDataDTO.setCustomerNif(customersDTO.getCustomerNif());
			personalDataDTO.setCustomerPhone(customersDTO.getCustomerPhone());
			personalDataDTO.setCustomerBeginDate(customersDTO.getCustomerBeginDate());

			personalDataDTOOpt = Optional.of(personalDataDTO);
		}

        return ResponseUtil.wrapOrNotFound(personalDataDTOOpt);
    }

    /**
     * DELETE  /personalData/:id : delete the "id" personal data.
     *
     * @param id the id of the personalDataDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/personalData/{id}")
    @Timed
    public ResponseEntity<Void> deletePersonalData(@PathVariable Long id) {
        log.debug("REST request to delete Customers : {}", id);

        if ( id == null ) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        // Get login
        String login = SecurityUtils.getCurrentUserLogin().get();

        // Verify if already exists orders with status different from PENDING
        Page<OrdersDTO> pageOrders = ordersService.getAllByLoginAndCustomerIdAndOrderStatus(login, id, PageRequest.of(0, 1000));

        List<OrdersDTO> listOrdersDTO = pageOrders.getContent();

        // Delete old customer and remove products from it (delete records from customer_products table)
        Optional<CustomersDTO> customersDTOOpt = customersService.findOne(id);

		if (customersDTOOpt.isPresent()) {
			CustomersDTO customersDTOOld = customersDTOOpt.get();

			// Delete old customer and remove products from it (delete records from customer_products table)
			customersDTOOld.setCustomerEndDate(Instant.now());
			customersDTOOld.setLastModifiedBy(login);
			customersDTOOld.setLastModifiedDate(Instant.now());

	        if ( listOrdersDTO.size() == 0 ) {
				// If the are not orders with status different from PENDING
				customersDTOOld.setProducts(null);
			}

	        customersService.save(customersDTOOld);
		}

		// Delete addresses = Update address_end_date
		Page<AddressesDTO> pageAddressesDTO = addressesService.getAddressesByLoginAndCustomerId(login, id, PageRequest.of(0, 1000));
		List<AddressesDTO> listAddressesDTO = pageAddressesDTO.getContent();

		for (Iterator<AddressesDTO> iteratorAddressesDTO = listAddressesDTO.iterator(); iteratorAddressesDTO.hasNext();) {
			AddressesDTO addressesDTO = iteratorAddressesDTO.next();

	        addressesDTO.setAddressEndDate(Instant.now());
	        addressesDTO.setLastModifiedBy(login);
	        addressesDTO.setLastModifiedDate(Instant.now());

	        addressesService.save(addressesDTO);
		}

        // Verify if already exists orders with PENDING Status
        Page<OrdersDTO> pageOrdersDTOPending = ordersService.getAllByLoginAndCustomerIdAndOrderStatusPending(login, id, PageRequest.of(0, 1));

        List<OrdersDTO> listOrdersDTOPending = pageOrdersDTOPending.getContent();

        for (Iterator<OrdersDTO> iterator = listOrdersDTOPending.iterator(); iterator.hasNext();) {
			OrdersDTO ordersDTOPending = iterator.next();

			// Delete all order details from PENDING order
			Page<OrderDetDTO> pageOrderDetDTOPending = orderDetService.getAllByOrderId(ordersDTOPending.getId(), PageRequest.of(0, 1000));

			List<OrderDetDTO> listOrderDetDTOPending = pageOrderDetDTOPending.getContent();

			for (Iterator<OrderDetDTO> iterator2 = listOrderDetDTOPending.iterator(); iterator2.hasNext();) {
				OrderDetDTO orderDetDTOPending = iterator2.next();

				orderDetService.delete(orderDetDTOPending.getId());
			}

			// Delete PENDING order
			ordersService.delete(ordersDTOPending.getId());
			break;
		}

        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, "")).build();
    }

    /**
     * SEARCH  /_search/personalData?query=:query : search for the personal data corresponding
     * to the query.
     *
     * @param query the query of the personal data search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/personalData")
    @Timed
    public ResponseEntity<List<PersonalDataDTO>> searchPersonalData(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of personal data for query {}", query);

        Page<CustomersDTO> page = customersService.search(query, pageable);

		List<CustomersDTO> listCustomersDTO = page.getContent();
		List<PersonalDataDTO> listPersonalDataDTO = new ArrayList<PersonalDataDTO>();

		for (Iterator<CustomersDTO> iterator = listCustomersDTO.iterator(); iterator.hasNext();) {
			CustomersDTO customersDTO = iterator.next();

			PersonalDataDTO personalDataDTO = new PersonalDataDTO();
			personalDataDTO.setId(customersDTO.getId());
			personalDataDTO.setCustomerEmail(customersDTO.getCustomerEmail());
			personalDataDTO.setCustomerGender(customersDTO.getCustomerGender());
			personalDataDTO.setCustomerName(customersDTO.getCustomerName());
			personalDataDTO.setCustomerNif(customersDTO.getCustomerNif());
			personalDataDTO.setCustomerPhone(customersDTO.getCustomerPhone());
			personalDataDTO.setCustomerBeginDate(customersDTO.getCustomerBeginDate());

			listPersonalDataDTO.add(personalDataDTO);
		}

        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/personalData");

        return new ResponseEntity<>(listPersonalDataDTO, headers, HttpStatus.OK);
    }
}
