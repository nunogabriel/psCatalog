package com.cgi.pscatalog.web.rest;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

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
import com.cgi.pscatalog.security.SecurityUtils;
import com.cgi.pscatalog.service.AddressesService;
import com.cgi.pscatalog.service.CustomersService;
import com.cgi.pscatalog.service.OrderDetService;
import com.cgi.pscatalog.service.OrderStatusService;
import com.cgi.pscatalog.service.OrdersService;
import com.cgi.pscatalog.service.ProductsService;
import com.cgi.pscatalog.service.dto.AddressesDTO;
import com.cgi.pscatalog.service.dto.CustomersDTO;
import com.cgi.pscatalog.service.dto.GeneralCatalogDTO;
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
 * REST controller for managing General Catalog.
 */
@RestController
@RequestMapping("/api")
public class GeneralCatalogResource {

	private final Logger log = LoggerFactory.getLogger(GeneralCatalogResource.class);
	
	private static final String ENTITY_NAME = "generalCatalog";

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

	public GeneralCatalogResource(ProductsService productsService) {
		this.productsService = productsService;
	}
	
    /**
     * PUT  /generalCatalog : Updates an existing products.
     *
     * @param generalCatalogDTO the generalCatalogDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated generalCatalogDTO,
     * or with status 400 (Bad Request) if the generalCatalogDTO is not valid,
     * or with status 500 (Internal Server Error) if the generalCatalogDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/generalCatalog")
    @Timed
    public ResponseEntity<GeneralCatalogDTO> updateGeneralCatalog(@Valid @RequestBody GeneralCatalogDTO generalCatalogDTO) throws URISyntaxException {
        log.debug("REST request to update General Catalog : {}", generalCatalogDTO);
        
        if (generalCatalogDTO.getId() == null) {
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
        
        log.debug("REST request to update General Catalog - customerId: {}", customerId);
        
        // Get Order Status Id of PENDING status
        OrderStatusResource orderStatusResource = new OrderStatusResource(orderStatusService);
        ResponseEntity<OrderStatusDTO> responseListOrderStatusDTO = orderStatusResource.getOrderStatusByDescription(Constants.ORDER_STATUS_PENDING);
        OrderStatusDTO orderStatusDTO = responseListOrderStatusDTO.getBody();
		Long orderStatusId = orderStatusDTO.getId();
        
        if (orderStatusId.longValue() == 0) {
            throw new BadRequestAlertException("Bad configuration, configure order status first", ENTITY_NAME, "idnull");
        }
        
        log.debug("REST request to update General Catalog - orderStatusId: {}", orderStatusId);
        
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
        
        log.debug("REST request to update General Catalog - addressId: {}", addressId);
                
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
        
        log.debug("REST request to update General Catalog - orderId: {}", orderId);
        
        // 2 - Verify if the product already exists in Order Detail (get order detail by Order and Product identification)
        OrderDetResource ordersDetResource = new OrderDetResource(orderDetService);
        OrderDetDTO orderDetDTO = null;
        
        if (oldOrdersDTO != null && oldOrdersDTO.getId() != 0) {
        	ResponseEntity<OrderDetDTO> responseOrderDetDTO = ordersDetResource.getOrderDetByOrderIdAndProductId(orderId, generalCatalogDTO.getId());
        	orderDetDTO = responseOrderDetDTO.getBody();
        }
        
        if (oldOrdersDTO == null || oldOrdersDTO.getId() == 0 || orderDetDTO == null) {
	        // Add Order Detail to the Order
        	OrderDetDTO newOrderDetDTO = new OrderDetDTO();
        	newOrderDetDTO.setOrderId(orderId);
        	newOrderDetDTO.setOrderQuantity(generalCatalogDTO.getOrderQuantity());
        	newOrderDetDTO.setUnitPrice(generalCatalogDTO.getProductPrice());
        	newOrderDetDTO.setProductId(generalCatalogDTO.getId());
	        
	        ordersDetResource.createOrderDet(newOrderDetDTO);
	        // End Create Order Detail
        } else {
        	// Update quantity in Order Detail
        	orderDetDTO.setOrderQuantity(generalCatalogDTO.getOrderQuantity() + orderDetDTO.getOrderQuantity());
        	ordersDetResource.updateOrderDet(orderDetDTO);
        }
        
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityAddBasketAlert(ENTITY_NAME, generalCatalogDTO.getProductName()))
            .body(generalCatalogDTO);
    }

	/**
	 * GET /generalCatalog : get all the general catalog.
	 *
	 * @param pageable the pagination information
	 * @return the ResponseEntity with status 200 (OK) and the list of products in
	 *         body
	 */
	@GetMapping("/generalCatalog")
	@Timed
	public ResponseEntity<List<GeneralCatalogDTO>> getAllGeneralCatalog(Pageable pageable) {
		log.debug("REST request to get a page of General Catalog");

		Page<ProductsDTO> page = productsService.findAll(pageable);

		List<ProductsDTO> listProductsDTO = page.getContent();
		List<GeneralCatalogDTO> listGeneralCatalogDTO = new ArrayList<GeneralCatalogDTO>();

		for (Iterator<ProductsDTO> iterator = listProductsDTO.iterator(); iterator.hasNext();) {
			ProductsDTO productsDTO = (ProductsDTO) iterator.next();

			GeneralCatalogDTO generalCatalogDTO = new GeneralCatalogDTO();
			generalCatalogDTO.setId(productsDTO.getId());
			generalCatalogDTO.setProductName(productsDTO.getProductName());
			generalCatalogDTO.setProductDescription(productsDTO.getProductDescription());
			generalCatalogDTO.setProductType(productsDTO.getProductType());
			generalCatalogDTO.setProductImg(productsDTO.getProductImg());
			generalCatalogDTO.setProductImgContentType(productsDTO.getProductImgContentType());
			generalCatalogDTO.setProductPrice(productsDTO.getProductPrice());
			generalCatalogDTO.setOrderQuantity(new Integer(0));

			listGeneralCatalogDTO.add(generalCatalogDTO);
		}

		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/generalCatalog");

		return ResponseEntity.ok().headers(headers).body(listGeneralCatalogDTO);
	}

	/**
	 * GET /generalCatalog/:id : get the "id" general catalog.
	 *
	 * @param id the id of the generalCatalogDTO to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the
	 *         generalCatalogDTO, or with status 404 (Not Found)
	 */
	@GetMapping("/generalCatalog/{id}")
	@Timed
	public ResponseEntity<GeneralCatalogDTO> getGeneralCatalog(@PathVariable Long id) {
		log.debug("REST request to get General Catalog : {}", id);

		Optional<ProductsDTO> productsDTOOpt = productsService.findOne(id);

		Optional<GeneralCatalogDTO> generalCatalogDTOOpt = Optional.empty();

		if (productsDTOOpt.isPresent()) {
			ProductsDTO productsDTO = productsDTOOpt.get();

			GeneralCatalogDTO generalCatalogDTO = new GeneralCatalogDTO();
			generalCatalogDTO.setId(productsDTO.getId());
			generalCatalogDTO.setProductName(productsDTO.getProductName());
			generalCatalogDTO.setProductDescription(productsDTO.getProductDescription());
			generalCatalogDTO.setProductType(productsDTO.getProductType());
			generalCatalogDTO.setProductImg(productsDTO.getProductImg());
			generalCatalogDTO.setProductImgContentType(productsDTO.getProductImgContentType());
			generalCatalogDTO.setProductPrice(productsDTO.getProductPrice());
			generalCatalogDTO.setOrderQuantity(new Integer(0));

			generalCatalogDTOOpt = Optional.of(generalCatalogDTO);
		}

		return ResponseUtil.wrapOrNotFound(generalCatalogDTOOpt);
	}

	/**
	 * SEARCH /_search/generalCatalog?query=:query : search for the general catalog
	 * corresponding to the query.
	 *
	 * @param query    the query of the general catalog search
	 * @param pageable the pagination information
	 * @return the result of the search
	 */
	@GetMapping("/_search/generalCatalog")
	@Timed
	public ResponseEntity<List<GeneralCatalogDTO>> searchGeneralCatalog(@RequestParam String query, Pageable pageable) {
		log.debug("REST request to search for a page of General Catalog for query {}", query);

		Page<ProductsDTO> page = productsService.search(query, pageable);

		List<ProductsDTO> listProductsDTO = page.getContent();
		List<GeneralCatalogDTO> listGeneralCatalogDTO = new ArrayList<GeneralCatalogDTO>();

		for (Iterator<ProductsDTO> iterator = listProductsDTO.iterator(); iterator.hasNext();) {
			ProductsDTO productsDTO = (ProductsDTO) iterator.next();

			GeneralCatalogDTO generalCatalogDTO = new GeneralCatalogDTO();
			generalCatalogDTO.setId(productsDTO.getId());
			generalCatalogDTO.setProductName(productsDTO.getProductName());
			generalCatalogDTO.setProductDescription(productsDTO.getProductDescription());
			generalCatalogDTO.setProductType(productsDTO.getProductType());
			generalCatalogDTO.setProductImg(productsDTO.getProductImg());
			generalCatalogDTO.setProductImgContentType(productsDTO.getProductImgContentType());
			generalCatalogDTO.setProductPrice(productsDTO.getProductPrice());
			generalCatalogDTO.setOrderQuantity(new Integer(0));

			listGeneralCatalogDTO.add(generalCatalogDTO);
		}

		HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page,
				"/api/_search/generalCatalog");

		return new ResponseEntity<>(listGeneralCatalogDTO, headers, HttpStatus.OK);
	}

}
