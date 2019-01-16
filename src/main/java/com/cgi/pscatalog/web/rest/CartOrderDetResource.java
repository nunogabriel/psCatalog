package com.cgi.pscatalog.web.rest;

import java.net.URISyntaxException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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

import com.cgi.pscatalog.config.Constants;
import com.cgi.pscatalog.security.SecurityUtils;
import com.cgi.pscatalog.service.CustomersService;
import com.cgi.pscatalog.service.OrderDetService;
import com.cgi.pscatalog.service.OrderStatusService;
import com.cgi.pscatalog.service.OrdersService;
import com.cgi.pscatalog.service.dto.CartOrderDetDTO;
import com.cgi.pscatalog.service.dto.CustomersDTO;
import com.cgi.pscatalog.service.dto.OrderDetDTO;
import com.cgi.pscatalog.service.dto.OrderStatusDTO;
import com.cgi.pscatalog.service.dto.OrdersDTO;
import com.cgi.pscatalog.web.rest.errors.BadRequestAlertException;
import com.cgi.pscatalog.web.rest.errors.FirstCreateCustomerException;
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

    private final OrderDetService orderDetService;

    @Autowired
	private CustomersService customersService;

    @Autowired
	private OrdersService ordersService;

    @Autowired
    private OrderStatusService orderStatusService;

    public CartOrderDetResource(OrderDetService orderDetService) {
        this.orderDetService = orderDetService;
    }

    /**
     * GET  /cart-order-det/order : Create a new cartOrderDet.
     *
     * @param cartOrderDetDTO the cartOrderDetDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cartOrderDetDTO, or with status 400 (Bad Request) if the orderDet has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @GetMapping("/cart-order-det/order")
    @Timed
    public ResponseEntity<Void> createCartOrderDet() throws URISyntaxException {
        log.debug("REST request to save CartOrderDet");


        // Get customer identification by login
        CustomersResource customersResource = new CustomersResource(customersService);
        ResponseEntity<CustomersDTO> responseCustomersDTO = customersResource.getCustomersByLogin(SecurityUtils.getCurrentUserLogin().get());
        CustomersDTO customersDTO = responseCustomersDTO.getBody();
		Long customerId = customersDTO.getId();

        if (customerId.longValue() == 0) {
            throw new BadRequestAlertException("You must create a customer first", ENTITY_NAME, "idnull");
        }

        log.debug("REST request to getAllCartOrderDets - customerId: {}", customerId);

        // Get Order Status Id of PENDING status
        OrderStatusResource orderStatusResource = new OrderStatusResource(orderStatusService);
        ResponseEntity<OrderStatusDTO> responseListOrderStatusDTO = orderStatusResource.getOrderStatusByDescription(Constants.ORDER_STATUS_PENDING);
        OrderStatusDTO orderStatusDTO = responseListOrderStatusDTO.getBody();
		Long orderStatusId = orderStatusDTO.getId();

        if (orderStatusId.longValue() == 0) {
            throw new BadRequestAlertException("Bad configuration, configure order status first", ENTITY_NAME, "idnull");
        }

        log.debug("REST request to getAllCartOrderDets - orderStatusId: {}", orderStatusId);

        // 1 - Verify if there is any order created for customer (get Order by Customer identification)
        OrdersResource ordersResource = new OrdersResource(ordersService);
        ResponseEntity<OrdersDTO> responseOrdersDTO = ordersResource.getOrdersByCustomerIdAndOrderStatusId(customerId, orderStatusId);
        OrdersDTO ordersDTO = responseOrdersDTO.getBody();

        OrdersDTO ordersDTOAux = new OrdersDTO();

        if (ordersDTO == null || ordersDTO.getId() == 0) {

        } else {
	        // Get Order Status Id of NEW status
	        orderStatusResource = new OrderStatusResource(orderStatusService);
	        responseListOrderStatusDTO = orderStatusResource.getOrderStatusByDescription(Constants.ORDER_STATUS_NEW);
	        orderStatusDTO = responseListOrderStatusDTO.getBody();
			orderStatusId = orderStatusDTO.getId();

	        if (orderStatusId.longValue() == 0) {
	            throw new BadRequestAlertException("Bad configuration, configure order status first", ENTITY_NAME, "idnull");
	        }

	        log.debug("REST request to createCartOrderDet - orderStatusId: {}", orderStatusId);

			ordersDTO.setOrderStatusId(orderStatusId);
			ordersDTO.setLastModifiedBy((SecurityUtils.getCurrentUserLogin().isPresent())?(SecurityUtils.getCurrentUserLogin().get()):"anonymousUser");
			ordersDTO.setLastModifiedDate(Instant.now());

			ordersDTOAux = ordersService.save(ordersDTO);
        }

		return ResponseEntity.ok().headers(HeaderUtil.createEntityOrderAlert(ENTITY_NAME, ordersDTOAux.getOrderReference())).build();
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

        OrderDetDTO orderDetDTO = new OrderDetDTO();
        orderDetDTO.setId(cartOrderDetDTO.getId());
        orderDetDTO.setOrderId(cartOrderDetDTO.getOrderId());
        orderDetDTO.setOrderOrderReference(cartOrderDetDTO.getOrderOrderReference());
        orderDetDTO.setOrderQuantity(cartOrderDetDTO.getOrderQuantity());
        orderDetDTO.setProductId(cartOrderDetDTO.getProductId());
        orderDetDTO.setProductProductName(cartOrderDetDTO.getProductProductName());
        orderDetDTO.setUnitPrice(cartOrderDetDTO.getUnitPrice());
        orderDetDTO.setLastModifiedBy((SecurityUtils.getCurrentUserLogin().isPresent())?(SecurityUtils.getCurrentUserLogin().get()):"anonymousUser");
        orderDetDTO.setLastModifiedDate(Instant.now());

        orderDetService.save(orderDetDTO);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, cartOrderDetDTO.getId().toString()))
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

        // Get customer identification by login
        CustomersResource customersResource = new CustomersResource(customersService);
        ResponseEntity<CustomersDTO> responseCustomersDTO = customersResource.getCustomersByLogin(SecurityUtils.getCurrentUserLogin().get());
        CustomersDTO customersDTO = responseCustomersDTO.getBody();

        if (customersDTO == null) {
            throw new FirstCreateCustomerException(ENTITY_NAME);
        }

		Long customerId = customersDTO.getId();

        log.debug("REST request to getAllCartOrderDets - customerId: {}", customerId);

        // Get Order Status Id of PENDING status
        OrderStatusResource orderStatusResource = new OrderStatusResource(orderStatusService);
        ResponseEntity<OrderStatusDTO> responseListOrderStatusDTO = orderStatusResource.getOrderStatusByDescription(Constants.ORDER_STATUS_PENDING);
        OrderStatusDTO orderStatusDTO = responseListOrderStatusDTO.getBody();
		Long orderStatusId = orderStatusDTO.getId();

        if (orderStatusId.longValue() == 0) {
            throw new BadRequestAlertException("Bad configuration, configure order status first", ENTITY_NAME, "idnull");
        }

        log.debug("REST request to getAllCartOrderDets - orderStatusId: {}", orderStatusId);

        // 1 - Verify if there is any order created for customer (get Order by Customer identification)
        OrdersResource ordersResource = new OrdersResource(ordersService);
        ResponseEntity<OrdersDTO> responseOrdersDTO = ordersResource.getOrdersByCustomerIdAndOrderStatusId(customerId, orderStatusId);
        OrdersDTO ordersDTO = responseOrdersDTO.getBody();

        Long orderId = new Long(0);
		List<CartOrderDetDTO> listCartOrderDetDTO = new ArrayList<CartOrderDetDTO>();

        if (ordersDTO == null || ordersDTO.getId() == 0) {

        } else {
        	orderId = ordersDTO.getId();

            // Get customer order details by order id
            Page<OrderDetDTO> pageOrderDetDTO = orderDetService.getAllByOrderId(orderId, pageable);

            List<OrderDetDTO> listOrderDetDTO = pageOrderDetDTO.getContent();

    		for (Iterator<OrderDetDTO> iterator = listOrderDetDTO.iterator(); iterator.hasNext();) {
    			OrderDetDTO orderDetDTO = iterator.next();

    			CartOrderDetDTO cartOrderDetDTO = new CartOrderDetDTO();
    			cartOrderDetDTO.setId(orderDetDTO.getId());
    			cartOrderDetDTO.setOrderId(orderDetDTO.getOrderId());
    			cartOrderDetDTO.setOrderOrderReference(orderDetDTO.getOrderOrderReference());
    			cartOrderDetDTO.setOrderQuantity(orderDetDTO.getOrderQuantity());
    			cartOrderDetDTO.setProductId(orderDetDTO.getProductId());
    			cartOrderDetDTO.setProductProductName(orderDetDTO.getProductProductName());
    			cartOrderDetDTO.setUnitPrice(orderDetDTO.getUnitPrice());

    			listCartOrderDetDTO.add(cartOrderDetDTO);
    		}
        }

        log.debug("REST request to getAllCartOrderDets - orderId: {}", orderId);

		Page<CartOrderDetDTO> page = new PageImpl<CartOrderDetDTO>(listCartOrderDetDTO, pageable, listCartOrderDetDTO.size());

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/cart-order-det");

        return ResponseEntity.ok().headers(headers).body(listCartOrderDetDTO);
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

        Optional<OrderDetDTO> orderDetDTOOpt = orderDetService.findOne(id);

        Optional<CartOrderDetDTO> cartOrderDetDTOOpt = Optional.empty();

		if (orderDetDTOOpt.isPresent()) {
			OrderDetDTO orderDetDTO = orderDetDTOOpt.get();

			CartOrderDetDTO cartOrderDetDTO = new CartOrderDetDTO();
			cartOrderDetDTO.setId(orderDetDTO.getId());
			cartOrderDetDTO.setOrderId(orderDetDTO.getOrderId());
			cartOrderDetDTO.setOrderOrderReference(orderDetDTO.getOrderOrderReference());
			cartOrderDetDTO.setOrderQuantity(orderDetDTO.getOrderQuantity());
			cartOrderDetDTO.setProductId(orderDetDTO.getProductId());
			cartOrderDetDTO.setProductProductName(orderDetDTO.getProductProductName());
			cartOrderDetDTO.setUnitPrice(orderDetDTO.getUnitPrice());

			cartOrderDetDTOOpt = Optional.of(cartOrderDetDTO);
		}

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

        orderDetService.delete(id);

        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
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

        Page<OrderDetDTO> page = orderDetService.search(query, pageable);

		List<OrderDetDTO> listOrderDetDTO = page.getContent();
		List<CartOrderDetDTO> listCartOrderDetDTO = new ArrayList<CartOrderDetDTO>();

		for (Iterator<OrderDetDTO> iterator = listOrderDetDTO.iterator(); iterator.hasNext();) {
			OrderDetDTO orderDetDTO = iterator.next();

			CartOrderDetDTO cartOrderDetDTO = new CartOrderDetDTO();
			cartOrderDetDTO.setId(orderDetDTO.getId());
			cartOrderDetDTO.setOrderId(orderDetDTO.getOrderId());
			cartOrderDetDTO.setOrderOrderReference(orderDetDTO.getOrderOrderReference());
			cartOrderDetDTO.setOrderQuantity(orderDetDTO.getOrderQuantity());
			cartOrderDetDTO.setProductId(orderDetDTO.getProductId());
			cartOrderDetDTO.setProductProductName(orderDetDTO.getProductProductName());
			cartOrderDetDTO.setUnitPrice(orderDetDTO.getUnitPrice());

			listCartOrderDetDTO.add(cartOrderDetDTO);
		}

        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/cat-order-det");

        return new ResponseEntity<>(listCartOrderDetDTO, headers, HttpStatus.OK);
    }
}