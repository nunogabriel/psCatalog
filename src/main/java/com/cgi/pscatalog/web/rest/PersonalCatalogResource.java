package com.cgi.pscatalog.web.rest;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

import com.cgi.pscatalog.config.Constants;
import com.cgi.pscatalog.domain.enumeration.ProductTypeEnum;
import com.cgi.pscatalog.security.SecurityUtils;
import com.cgi.pscatalog.service.AddressesService;
import com.cgi.pscatalog.service.CustomersService;
import com.cgi.pscatalog.service.OrderDetService;
import com.cgi.pscatalog.service.OrderStatusService;
import com.cgi.pscatalog.service.OrdersService;
import com.cgi.pscatalog.service.ProductsService;
import com.cgi.pscatalog.service.dto.AddressesDTO;
import com.cgi.pscatalog.service.dto.CustomersDTO;
import com.cgi.pscatalog.service.dto.OrderDetDTO;
import com.cgi.pscatalog.service.dto.OrderStatusDTO;
import com.cgi.pscatalog.service.dto.OrdersDTO;
import com.cgi.pscatalog.service.dto.PersonalCatalogDTO;
import com.cgi.pscatalog.service.dto.ProductsDTO;
import com.cgi.pscatalog.web.rest.errors.BadRequestAlertException;
import com.cgi.pscatalog.web.rest.errors.FirstCreateAddressException;
import com.cgi.pscatalog.web.rest.errors.FirstCreateCustomerException;
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

	private final ProductsService productsService;

    @Autowired
	private OrdersService ordersService;

    @Autowired
	private OrderDetService orderDetService;

    @Autowired
    private OrderStatusService orderStatusService;

    @Autowired
	private CustomersService customersService;

    @Autowired
    private AddressesService addressesService;

	public PersonalCatalogResource(ProductsService productsService) {
		this.productsService = productsService;
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

        String login = SecurityUtils.getCurrentUserLogin().get();

        // 1 - Verify if there is any PENDING order created for customer
        Page<OrdersDTO> pageOrdersDTO = ordersService.getAllByLoginAndOrderStatusPending(login, PageRequest.of(0, 1));

        List<OrdersDTO> listOrdersDTO = pageOrdersDTO.getContent();

        Long orderId = new Long(0);

        OrderDetResource ordersDetResource = new OrderDetResource(orderDetService);

        for (Iterator<OrdersDTO> iterator = listOrdersDTO.iterator(); iterator.hasNext();) {
        	OrdersDTO oldOrdersDTO = iterator.next();

        	orderId = oldOrdersDTO.getId();

            // 2 - Verify if the product already exists in Order Detail (get order detail by Order and Product identification)
        	ResponseEntity<OrderDetDTO> responseOrderDetDTO = ordersDetResource.getOrderDetByOrderIdAndProductId(orderId, personalCatalogDTO.getId());
        	OrderDetDTO orderDetDTO = responseOrderDetDTO.getBody();

        	if (orderDetDTO == null) {
    	        // Add Order Detail to the Order
            	OrderDetDTO newOrderDetDTO = new OrderDetDTO();
            	newOrderDetDTO.setOrderId(orderId);
            	newOrderDetDTO.setOrderQuantity(personalCatalogDTO.getOrderQuantity());
            	newOrderDetDTO.setUnitPrice(personalCatalogDTO.getProductPrice());
            	newOrderDetDTO.setProductId(personalCatalogDTO.getId());

    	        ordersDetResource.createOrderDet(newOrderDetDTO);
    	        // End Create Order Detail
            } else {
            	// Update quantity in Order Detail
            	orderDetDTO.setOrderQuantity(personalCatalogDTO.getOrderQuantity() + orderDetDTO.getOrderQuantity());
            	ordersDetResource.updateOrderDet(orderDetDTO);
            }

        	break;
        }

        if (orderId.longValue() == 0) {
        	// There is not any PENDING order created for customer
        	//
            // Get customer identification by login
            Optional<CustomersDTO> optionalCustomersDTO = customersService.getCustomersByLogin(login);

            Long customerId = new Long(0);

            if ( optionalCustomersDTO.isPresent() ) {
            	CustomersDTO customersDTO = optionalCustomersDTO.get();

            	customerId = customersDTO.getId();

            	log.debug("REST request to addBasket - customerId: {}", customerId);
            } else {
            	throw new BadRequestAlertException("You must create a customer first", ENTITY_NAME, "idnull");
            }

            // Get default address identification by login
            Long addressId = new Long(0);

            Page<AddressesDTO> pageAddressesDTO = addressesService.getAddressesByLogin(login, PageRequest.of(0, 1));
            List<AddressesDTO> listAddressesDTO = pageAddressesDTO.getContent();

            for (Iterator<AddressesDTO> iterator = listAddressesDTO.iterator(); iterator.hasNext();) {
    			AddressesDTO addressesDTO = iterator.next();
    			addressId = addressesDTO.getId();
    			break;
    		}

            if (addressId.longValue() == 0) {
                throw new FirstCreateAddressException(ENTITY_NAME);
            }

            log.debug("REST request to addBasket - addressId: {}", addressId);

            // Get Order Status Id of PENDING status
            OrderStatusResource orderStatusResource = new OrderStatusResource(orderStatusService);
            ResponseEntity<OrderStatusDTO> responseListOrderStatusDTO = orderStatusResource.getOrderStatusByDescription(Constants.ORDER_STATUS_PENDING);
            OrderStatusDTO orderStatusDTO = responseListOrderStatusDTO.getBody();
    		Long orderStatusId = orderStatusDTO.getId();

            if (orderStatusId.longValue() == 0) {
                throw new BadRequestAlertException("Bad configuration, configure order status first", ENTITY_NAME, "idnull");
            }

            log.debug("REST request to addBasket - orderStatusId: {}", orderStatusId);

	        // Start Create Order
	        OrdersDTO newOrdersDTO = new OrdersDTO();
	        newOrdersDTO.setCustomerId(customerId);
	        newOrdersDTO.setAddressId(addressId);
	        newOrdersDTO.setDeliveryAddressId(addressId);
	        newOrdersDTO.setOrderStatusId(orderStatusId);

	        OrdersResource ordersResource = new OrdersResource(ordersService);
	        ResponseEntity<OrdersDTO> newOrdersDTOAux = ordersResource.createOrders(newOrdersDTO);
	        orderId = newOrdersDTOAux.getBody().getId();
	        // End Create Order

	        // Add Order Detail to the Order
        	OrderDetDTO newOrderDetDTO = new OrderDetDTO();
        	newOrderDetDTO.setOrderId(orderId);
        	newOrderDetDTO.setOrderQuantity(personalCatalogDTO.getOrderQuantity());
        	newOrderDetDTO.setUnitPrice(personalCatalogDTO.getProductPrice());
        	newOrderDetDTO.setProductId(personalCatalogDTO.getId());

	        ordersDetResource.createOrderDet(newOrderDetDTO);
	        // End Create Order Detail
        }

        log.debug("REST request to addBasket - orderId: {}", orderId);

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

        // Get customer identification by login
        Optional<CustomersDTO> optionalCustomersDTO = customersService.getCustomersByLogin(SecurityUtils.getCurrentUserLogin().get());

        Long customerId = new Long(0);

        if ( optionalCustomersDTO.isPresent() ) {
        	CustomersDTO customersDTO = optionalCustomersDTO.get();

        	customerId = customersDTO.getId();

        	log.debug("REST request to getAllPersonalCatalog - customerId: {}", customerId);
        } else {
        	throw new FirstCreateCustomerException(ENTITY_NAME);
        }

        // Get all active products with or not with promotions by customer id
        Page<Object[]> page = productsService.getAllProductsWithPromotionsByCustomersId(customerId, pageable);

		List<PersonalCatalogDTO> listPersonalCatalogDTO = new ArrayList<PersonalCatalogDTO>();

		for (Iterator<Object[]> iterator = page.iterator(); iterator.hasNext();) {
			Object[] products = iterator.next();

			PersonalCatalogDTO personalCatalogDTO = new PersonalCatalogDTO();
			personalCatalogDTO.setId(((BigInteger)products[0]).longValue());
			personalCatalogDTO.setProductName((String)products[1]);
			personalCatalogDTO.setProductDescription((String)products[2]);
			personalCatalogDTO.setProductType(ProductTypeEnum.valueOf((String)products[3]));
			personalCatalogDTO.setProductImg((byte[])products[4]);
			personalCatalogDTO.setProductImgContentType((String)products[5]);
			personalCatalogDTO.setProductPrice((BigDecimal)products[6]);
			personalCatalogDTO.setOrderQuantity(new Integer(0));

			listPersonalCatalogDTO.add(personalCatalogDTO);
		}

		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/personalCatalog");

		return ResponseEntity.ok().headers(headers).body(listPersonalCatalogDTO);
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

		// Get customer identification by login
        Optional<CustomersDTO> optionalCustomersDTO = customersService.getCustomersByLogin(SecurityUtils.getCurrentUserLogin().get());

        Long customerId = new Long(0);

        if ( optionalCustomersDTO.isPresent() ) {
        	CustomersDTO customersDTO = optionalCustomersDTO.get();

        	customerId = customersDTO.getId();

        	log.debug("REST request to getPersonalCatalog - customerId: {}", customerId);
        } else {
        	throw new FirstCreateCustomerException(ENTITY_NAME);
        }

        // Get all active products with or not with promotions by customer id and product id
        Page<Object[]> page = productsService.getAllProductsWithPromotionsByCustomersIdAndProductId(customerId, id, PageRequest.of(0, 1));

		Optional<PersonalCatalogDTO> personalCatalogDTOOpt = Optional.empty();

		for (Iterator<Object[]> iterator = page.iterator(); iterator.hasNext();) {
			Object[] products = iterator.next();

			PersonalCatalogDTO personalCatalogDTO = new PersonalCatalogDTO();
			personalCatalogDTO.setId(((BigInteger)products[0]).longValue());
			personalCatalogDTO.setProductName((String)products[1]);
			personalCatalogDTO.setProductDescription((String)products[2]);
			personalCatalogDTO.setProductType(ProductTypeEnum.valueOf((String)products[3]));
			personalCatalogDTO.setProductImg((byte[])products[4]);
			personalCatalogDTO.setProductImgContentType((String)products[5]);
			personalCatalogDTO.setProductPrice((BigDecimal)products[6]);
			personalCatalogDTO.setOrderQuantity(new Integer(0));

			personalCatalogDTOOpt = Optional.of(personalCatalogDTO);
			break;
		}

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

		Page<ProductsDTO> page = productsService.search(query, pageable);

		List<ProductsDTO> listProductsDTO = page.getContent();
		List<PersonalCatalogDTO> listPersonalCatalogDTO = new ArrayList<PersonalCatalogDTO>();

		for (Iterator<ProductsDTO> iterator = listProductsDTO.iterator(); iterator.hasNext();) {
			ProductsDTO productsDTO = iterator.next();

			PersonalCatalogDTO personalCatalogDTO = new PersonalCatalogDTO();
			personalCatalogDTO.setId(productsDTO.getId());
			personalCatalogDTO.setProductName(productsDTO.getProductName());
			personalCatalogDTO.setProductDescription(productsDTO.getProductDescription());
			personalCatalogDTO.setProductType(productsDTO.getProductType());
			personalCatalogDTO.setProductImg(productsDTO.getProductImg());
			personalCatalogDTO.setProductImgContentType(productsDTO.getProductImgContentType());
			personalCatalogDTO.setProductPrice(productsDTO.getProductPrice());
			personalCatalogDTO.setOrderQuantity(new Integer(0));

			listPersonalCatalogDTO.add(personalCatalogDTO);
		}

		HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page,
				"/api/_search/personalCatalog");

		return new ResponseEntity<>(listPersonalCatalogDTO, headers, HttpStatus.OK);
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

        // Get customer identification by login
        Optional<CustomersDTO> optionalCustomersDTO = customersService.getCustomersByLogin(SecurityUtils.getCurrentUserLogin().get());

        if ( optionalCustomersDTO.isPresent() ) {
        	CustomersDTO customersDTO = optionalCustomersDTO.get();

            Set<ProductsDTO> oldSetProductsDTO = customersDTO.getProducts();

            // Find product
            Optional<ProductsDTO> productsDTOOpt = productsService.findOne(id);
            ProductsDTO productsDTO = null;

            // Add new product
    		if ( productsDTOOpt.isPresent() ) {
    			productsDTO = productsDTOOpt.get();

    			oldSetProductsDTO.remove(productsDTO);

    	        customersDTO.setProducts(oldSetProductsDTO);

    	        // Delete product from Personal Catalog
    	        CustomersResource customersResource = new CustomersResource(customersService);
    	        customersResource.updateCustomers(customersDTO);

    	        log.debug("REST request to delete personal catalog updateCustomers: {}", productsDTO.getId());

    	        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletePersonalAlert(ENTITY_NAME, productsDTO.getProductName())).build();
    		} else {
    			return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletePersonalDoesNotExistAlert(ENTITY_NAME, "")).build();
    		}
        } else {
        	throw new FirstCreateCustomerException(ENTITY_NAME);
        }
    }
}