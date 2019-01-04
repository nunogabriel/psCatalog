package com.cgi.pscatalog.web.rest;

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
import org.springframework.data.domain.PageImpl;
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
import com.cgi.pscatalog.security.SecurityUtils;
import com.cgi.pscatalog.service.AddressesService;
import com.cgi.pscatalog.service.CustomersService;
import com.cgi.pscatalog.service.OrderDetService;
import com.cgi.pscatalog.service.OrderStatusService;
import com.cgi.pscatalog.service.OrdersService;
import com.cgi.pscatalog.service.ProductsService;
import com.cgi.pscatalog.service.dto.AddressesDTO;
import com.cgi.pscatalog.service.dto.CustomersDTO;
import com.cgi.pscatalog.service.dto.PersonalCatalogDTO;
import com.cgi.pscatalog.service.dto.OrderDetDTO;
import com.cgi.pscatalog.service.dto.OrderStatusDTO;
import com.cgi.pscatalog.service.dto.OrdersDTO;
import com.cgi.pscatalog.service.dto.ProductsDTO;
import com.cgi.pscatalog.web.rest.errors.BadRequestAlertException;
import com.cgi.pscatalog.web.rest.util.HeaderUtil;
import com.cgi.pscatalog.web.rest.util.PaginationUtil;
import com.codahale.metrics.annotation.Timed;
import com.cgi.pscatalog.web.rest.OrdersResource;
import com.cgi.pscatalog.web.rest.OrderDetResource;

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
        
        // Get customer identification by login
        CustomersResource customersResource = new CustomersResource(customersService);
        ResponseEntity<CustomersDTO> responseCustomersDTO = customersResource.getCustomersByLogin(SecurityUtils.getCurrentUserLogin().get());
        CustomersDTO customersDTO = responseCustomersDTO.getBody();
		Long customerId = customersDTO.getId();
		
        if (customerId.longValue() == 0) {
            throw new BadRequestAlertException("You must create a customer first", ENTITY_NAME, "idnull");
        }
        
        log.debug("REST request to addBasket - customerId: {}", customerId);
        
        // Get Order Status Id of PENDING status
        OrderStatusResource orderStatusResource = new OrderStatusResource(orderStatusService);
        ResponseEntity<OrderStatusDTO> responseListOrderStatusDTO = orderStatusResource.getOrderStatusByDescription(Constants.ORDER_STATUS_PENDING);
        OrderStatusDTO orderStatusDTO = responseListOrderStatusDTO.getBody();
		Long orderStatusId = orderStatusDTO.getId();
        
        if (orderStatusId.longValue() == 0) {
            throw new BadRequestAlertException("Bad configuration, configure order status first", ENTITY_NAME, "idnull");
        }
        
        log.debug("REST request to addBasket - orderStatusId: {}", orderStatusId);
        
        // Get address identification by customer identification
        Long addressId = new Long(0);
        
        AddressesResource addressesResource = new AddressesResource(addressesService);
        ResponseEntity<List<AddressesDTO>> responseListAddressesDTO = addressesResource.getAddressesByCustomerId(customerId, PageRequest.of(0, 1));
        List<AddressesDTO> listAddressesDTO = responseListAddressesDTO.getBody();
        
        for (Iterator<AddressesDTO> iterator = listAddressesDTO.iterator(); iterator.hasNext();) {
			AddressesDTO addressesDTO = (AddressesDTO) iterator.next();
			addressId = addressesDTO.getId();
			break;
		}
        
        if (addressId.longValue() == 0) {
            throw new BadRequestAlertException("You must add a address first", ENTITY_NAME, "idnull");
        }
        
        log.debug("REST request to addBasket - addressId: {}", addressId);
                
        // 1 - Verify if there is any order created for customer (get Order by Customer identification)
        OrdersResource ordersResource = new OrdersResource(ordersService);
        ResponseEntity<OrdersDTO> responseOrdersDTO = ordersResource.getOrdersByCustomerIdAndOrderStatusId(customerId, orderStatusId);
        OrdersDTO oldOrdersDTO = responseOrdersDTO.getBody();

        Long orderId = new Long(0);
        
        if (oldOrdersDTO == null || oldOrdersDTO.getId() == 0) {
	        // Start Create Order
	        OrdersDTO newOrdersDTO = new OrdersDTO();
	        newOrdersDTO.setCustomerId(customerId);
	        newOrdersDTO.setAddressId(addressId);
	        newOrdersDTO.setDeliveryAddressId(addressId);
	        newOrdersDTO.setOrderStatusId(orderStatusId);
	        
	        ResponseEntity<OrdersDTO> newOrdersDTOAux = ordersResource.createOrders(newOrdersDTO);
	        orderId = newOrdersDTOAux.getBody().getId();
	        // End Create Order
        } else {
        	orderId = oldOrdersDTO.getId();
        }
        
        log.debug("REST request to addBasket - orderId: {}", orderId);
        
        // 2 - Verify if the product already exists in Order Detail (get order detail by Order and Product identification)
        OrderDetResource ordersDetResource = new OrderDetResource(orderDetService);
        OrderDetDTO orderDetDTO = null;
        
        if (oldOrdersDTO != null && oldOrdersDTO.getId() != 0) {
        	ResponseEntity<OrderDetDTO> responseOrderDetDTO = ordersDetResource.getOrderDetByOrderIdAndProductId(orderId, personalCatalogDTO.getId());
        	orderDetDTO = responseOrderDetDTO.getBody();
        }
        
        if (oldOrdersDTO == null || oldOrdersDTO.getId() == 0 || orderDetDTO == null) {
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
        CustomersResource customersResource = new CustomersResource(customersService);
        ResponseEntity<CustomersDTO> responseCustomersDTO = customersResource.getCustomersByLogin(SecurityUtils.getCurrentUserLogin().get());
        CustomersDTO customersDTO = responseCustomersDTO.getBody();
        Set<ProductsDTO> oldSetProductsDTO = customersDTO.getProducts();
        
		List<PersonalCatalogDTO> listPersonalCatalogDTO = new ArrayList<PersonalCatalogDTO>();

		for (Iterator<ProductsDTO> iterator = oldSetProductsDTO.iterator(); iterator.hasNext();) {
			ProductsDTO productsDTO = (ProductsDTO) iterator.next();

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
		
		Page<PersonalCatalogDTO> page = new PageImpl<PersonalCatalogDTO>(listPersonalCatalogDTO, pageable, listPersonalCatalogDTO.size());

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

		Optional<ProductsDTO> productsDTOOpt = productsService.findOne(id);

		Optional<PersonalCatalogDTO> personalCatalogDTOOpt = Optional.empty();

		if (productsDTOOpt.isPresent()) {
			ProductsDTO productsDTO = productsDTOOpt.get();

			PersonalCatalogDTO personalCatalogDTO = new PersonalCatalogDTO();
			personalCatalogDTO.setId(productsDTO.getId());
			personalCatalogDTO.setProductName(productsDTO.getProductName());
			personalCatalogDTO.setProductDescription(productsDTO.getProductDescription());
			personalCatalogDTO.setProductType(productsDTO.getProductType());
			personalCatalogDTO.setProductImg(productsDTO.getProductImg());
			personalCatalogDTO.setProductImgContentType(productsDTO.getProductImgContentType());
			personalCatalogDTO.setProductPrice(productsDTO.getProductPrice());
			personalCatalogDTO.setOrderQuantity(new Integer(0));

			personalCatalogDTOOpt = Optional.of(personalCatalogDTO);
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
			ProductsDTO productsDTO = (ProductsDTO) iterator.next();

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
        CustomersResource customersResource = new CustomersResource(customersService);
        ResponseEntity<CustomersDTO> responseCustomersDTO = customersResource.getCustomersByLogin(SecurityUtils.getCurrentUserLogin().get());
        CustomersDTO customersDTO = responseCustomersDTO.getBody();
        Set<ProductsDTO> oldSetProductsDTO = customersDTO.getProducts();
        
        // Find product
        Optional<ProductsDTO> productsDTOOpt = productsService.findOne(id);
        ProductsDTO productsDTO = null;
        
        // Add new product
		if (productsDTOOpt.isPresent()) {
			productsDTO = productsDTOOpt.get();
			oldSetProductsDTO.remove(productsDTO);

	        customersDTO.setProducts(oldSetProductsDTO);
		        
	        // Delete product from Personal Catalog
	        customersResource.updateCustomers(customersDTO);
		        
	        log.debug("REST request to delete personal catalog updateCustomers: {}", productsDTO.getId());

	        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletePersonalAlert(ENTITY_NAME, productsDTO.getProductName())).build();
		} else {
			return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletePersonalDoesNotExistAlert(ENTITY_NAME, "")).build();
		}
    }
}
