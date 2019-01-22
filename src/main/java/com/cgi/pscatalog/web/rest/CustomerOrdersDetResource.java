package com.cgi.pscatalog.web.rest;

import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cgi.pscatalog.security.SecurityUtils;
import com.cgi.pscatalog.service.OrderDetService;
import com.cgi.pscatalog.service.OrdersService;
import com.cgi.pscatalog.service.ProductsService;
import com.cgi.pscatalog.service.dto.CustomerOrdersDetDTO;
import com.cgi.pscatalog.service.dto.OrderDetDTO;
import com.cgi.pscatalog.service.dto.OrdersDTO;
import com.cgi.pscatalog.service.dto.ProductsDTO;
import com.cgi.pscatalog.web.rest.util.PaginationUtil;
import com.codahale.metrics.annotation.Timed;

import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing OrderDet.
 */
@RestController
@RequestMapping("/api")
public class CustomerOrdersDetResource {

    private final Logger log = LoggerFactory.getLogger(CustomerOrdersDetResource.class);

    private final OrderDetService orderDetService;

    @Autowired
	private ProductsService productsService;

    @Autowired
	private OrdersService ordersService;

    public CustomerOrdersDetResource(OrderDetService orderDetService) {
        this.orderDetService = orderDetService;
    }

    /**
     * GET  /customer-orders-det : get all the orderDets.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of orderDets in body
     */
    @GetMapping("/customer-orders-det")
    @Timed
    public ResponseEntity<List<CustomerOrdersDetDTO>> getAllCustomerOrderDets(Pageable pageable) throws URISyntaxException {
        log.debug("REST request to get a page of CustomerOrderDets");

		List<CustomerOrdersDetDTO> listCustomerOrdersDetDTO = new ArrayList<CustomerOrdersDetDTO>();

        // Get customer order details by order id and status different from PENDING
        Page<OrderDetDTO> pageOrderDetDTO = orderDetService.getAllByLoginAndOrderStatus(SecurityUtils.getCurrentUserLogin().get(), pageable);

        List<OrderDetDTO> listOrderDetDTO = pageOrderDetDTO.getContent();

		for (Iterator<OrderDetDTO> iterator = listOrderDetDTO.iterator(); iterator.hasNext();) {
			OrderDetDTO orderDetDTO = iterator.next();

			CustomerOrdersDetDTO customerOrdersDetDTO = new CustomerOrdersDetDTO();
			customerOrdersDetDTO.setId(orderDetDTO.getId());
			customerOrdersDetDTO.setOrderId(orderDetDTO.getOrderId());
			customerOrdersDetDTO.setOrderOrderReference(orderDetDTO.getOrderOrderReference());
			customerOrdersDetDTO.setOrderQuantity(orderDetDTO.getOrderQuantity());
			customerOrdersDetDTO.setProductId(orderDetDTO.getProductId());
			customerOrdersDetDTO.setProductProductName(orderDetDTO.getProductProductName());
			customerOrdersDetDTO.setUnitPrice(orderDetDTO.getUnitPrice());
			customerOrdersDetDTO.setTotalPrice(orderDetDTO.getUnitPrice().multiply(new BigDecimal(orderDetDTO.getOrderQuantity())));

			listCustomerOrdersDetDTO.add(customerOrdersDetDTO);
		}

		Page<CustomerOrdersDetDTO> page = new PageImpl<CustomerOrdersDetDTO>(listCustomerOrdersDetDTO, pageable, listCustomerOrdersDetDTO.size());

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/customer-orders-det");

        return ResponseEntity.ok().headers(headers).body(listCustomerOrdersDetDTO);
    }

    /**
     * GET  /customer-orders-det/by-order-id : get all the orderDets by order id.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of orderDets in body
     */
    @GetMapping("/customer-orders-det/by-order-id")
    @Timed
    public ResponseEntity<List<CustomerOrdersDetDTO>> getAllCustomerOrderDetsByOrderId(@RequestParam Long orderId, Pageable pageable) throws URISyntaxException {
        log.debug("REST request to get a page of getAllCustomerOrderDetsByOrderId by order id");

		List<CustomerOrdersDetDTO> listCustomerOrdersDetDTO = new ArrayList<CustomerOrdersDetDTO>();

        // Get customer order details by order id
        Page<OrderDetDTO> pageOrderDetDTO = orderDetService.getAllByLoginAndOrderIdAndOrderStatus(SecurityUtils.getCurrentUserLogin().get(), orderId, pageable);

        List<OrderDetDTO> listOrderDetDTO = pageOrderDetDTO.getContent();

		for (Iterator<OrderDetDTO> iterator = listOrderDetDTO.iterator(); iterator.hasNext();) {
			OrderDetDTO orderDetDTO = iterator.next();

			CustomerOrdersDetDTO customerOrdersDetDTO = new CustomerOrdersDetDTO();
			customerOrdersDetDTO.setId(orderDetDTO.getId());
			customerOrdersDetDTO.setOrderId(orderDetDTO.getOrderId());
			customerOrdersDetDTO.setOrderOrderReference(orderDetDTO.getOrderOrderReference());
			customerOrdersDetDTO.setOrderQuantity(orderDetDTO.getOrderQuantity());
			customerOrdersDetDTO.setProductId(orderDetDTO.getProductId());
			customerOrdersDetDTO.setProductProductName(orderDetDTO.getProductProductName());
			customerOrdersDetDTO.setUnitPrice(orderDetDTO.getUnitPrice());
			customerOrdersDetDTO.setTotalPrice(orderDetDTO.getUnitPrice().multiply(new BigDecimal(orderDetDTO.getOrderQuantity())));

			Optional<ProductsDTO> productsDTOopt = productsService.findOne(orderDetDTO.getProductId());

			if (productsDTOopt.isPresent()) {
				ProductsDTO productsDTO = productsDTOopt.get();

				customerOrdersDetDTO.setProductDescription(productsDTO.getProductDescription());
				customerOrdersDetDTO.setProductType(productsDTO.getProductType());
				customerOrdersDetDTO.setProductImg(productsDTO.getProductImg());
				customerOrdersDetDTO.setProductImgContentType(productsDTO.getProductImgContentType());
			}

			listCustomerOrdersDetDTO.add(customerOrdersDetDTO);
		}

		Page<CustomerOrdersDetDTO> page = new PageImpl<CustomerOrdersDetDTO>(listCustomerOrdersDetDTO, pageable, listCustomerOrdersDetDTO.size());

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/customer-orders-det");

        return ResponseEntity.ok().headers(headers).body(listCustomerOrdersDetDTO);
    }

    /**
     * GET  /customer-orders-det/:id : get the "id" orderDet.
     *
     * @param id the id of the customerOrdersDetDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the customerOrdersDetDTO, or with status 404 (Not Found)
     */
    @GetMapping("/customer-orders-det/{id}")
    @Timed
    public ResponseEntity<CustomerOrdersDetDTO> getCustomerOrderDet(@PathVariable Long id) {
        log.debug("REST request to get CustomerOrderDet : {}", id);

        Optional<OrderDetDTO> orderDetDTOOpt = orderDetService.findOne(id);

        Optional<CustomerOrdersDetDTO> customerOrdersDetDTOOpt = Optional.empty();

		if (orderDetDTOOpt.isPresent()) {
			OrderDetDTO orderDetDTO = orderDetDTOOpt.get();

			CustomerOrdersDetDTO customerOrdersDetDTO = new CustomerOrdersDetDTO();
			customerOrdersDetDTO.setId(orderDetDTO.getId());
			customerOrdersDetDTO.setOrderId(orderDetDTO.getOrderId());
			customerOrdersDetDTO.setOrderOrderReference(orderDetDTO.getOrderOrderReference());
			customerOrdersDetDTO.setOrderQuantity(orderDetDTO.getOrderQuantity());
			customerOrdersDetDTO.setProductId(orderDetDTO.getProductId());
			customerOrdersDetDTO.setProductProductName(orderDetDTO.getProductProductName());
			customerOrdersDetDTO.setUnitPrice(orderDetDTO.getUnitPrice());
			customerOrdersDetDTO.setTotalPrice(orderDetDTO.getUnitPrice().multiply(new BigDecimal(orderDetDTO.getOrderQuantity())));

			Optional<ProductsDTO> productsDTOopt = productsService.findOne(orderDetDTO.getProductId());

			if (productsDTOopt.isPresent()) {
				ProductsDTO productsDTO = productsDTOopt.get();

				customerOrdersDetDTO.setProductDescription(productsDTO.getProductDescription());
				customerOrdersDetDTO.setProductType(productsDTO.getProductType());
				customerOrdersDetDTO.setProductImg(productsDTO.getProductImg());
				customerOrdersDetDTO.setProductImgContentType(productsDTO.getProductImgContentType());
			}

			customerOrdersDetDTOOpt = Optional.of(customerOrdersDetDTO);
		}

        return ResponseUtil.wrapOrNotFound(customerOrdersDetDTOOpt);
    }

    /**
     * GET  /customer-orders-det/orderTotal : get the "id" orderDet.
     *
     * @param id the id of the customerOrdersDetDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the customerOrdersDetDTO, or with status 404 (Not Found)
     */
    @GetMapping("/customer-orders-det/orderTotal")
    @Timed
    public BigDecimal getOrderTotal() {
        log.debug("REST request to get getOrderTotal");

        //
        Page<OrdersDTO> page = ordersService.getAllByLoginAndOrderStatusPending(SecurityUtils.getCurrentUserLogin().get(), PageRequest.of(0, 1));

        List<OrdersDTO> listOrdersDTO = page.getContent();

        BigDecimal orderTotal = new BigDecimal(0);

        if (listOrdersDTO.size() != 0) {
	        // Get customer order details by order id
	        orderTotal = orderDetService.getOrderTotal(listOrdersDTO.get(0).getId());
        }

        return orderTotal;
    }

    /**
     * SEARCH  /_search/customer-orders-det?query=:query : search for the orderDet corresponding
     * to the query.
     *
     * @param query the query of the orderDet search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/customer-orders-det")
    @Timed
    public ResponseEntity<List<CustomerOrdersDetDTO>> searchCustomerOrdersDet(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of searchCustomerOrdersDet for query {}", query);

        Page<OrderDetDTO> page = orderDetService.search(query, pageable);

		List<OrderDetDTO> listOrderDetDTO = page.getContent();
		List<CustomerOrdersDetDTO> listCustomerOrdersDetDTO = new ArrayList<CustomerOrdersDetDTO>();

		for (Iterator<OrderDetDTO> iterator = listOrderDetDTO.iterator(); iterator.hasNext();) {
			OrderDetDTO orderDetDTO = iterator.next();

			CustomerOrdersDetDTO customerOrdersDetDTO = new CustomerOrdersDetDTO();
			customerOrdersDetDTO.setId(orderDetDTO.getId());
			customerOrdersDetDTO.setOrderId(orderDetDTO.getOrderId());
			customerOrdersDetDTO.setOrderOrderReference(orderDetDTO.getOrderOrderReference());
			customerOrdersDetDTO.setOrderQuantity(orderDetDTO.getOrderQuantity());
			customerOrdersDetDTO.setProductId(orderDetDTO.getProductId());
			customerOrdersDetDTO.setProductProductName(orderDetDTO.getProductProductName());
			customerOrdersDetDTO.setUnitPrice(orderDetDTO.getUnitPrice());
			customerOrdersDetDTO.setTotalPrice(orderDetDTO.getUnitPrice().multiply(new BigDecimal(orderDetDTO.getOrderQuantity())));

			Optional<ProductsDTO> productsDTOopt = productsService.findOne(orderDetDTO.getProductId());

			if (productsDTOopt.isPresent()) {
				ProductsDTO productsDTO = productsDTOopt.get();

				customerOrdersDetDTO.setProductDescription(productsDTO.getProductDescription());
				customerOrdersDetDTO.setProductType(productsDTO.getProductType());
				customerOrdersDetDTO.setProductImg(productsDTO.getProductImg());
				customerOrdersDetDTO.setProductImgContentType(productsDTO.getProductImgContentType());
			}

			listCustomerOrdersDetDTO.add(customerOrdersDetDTO);
		}

        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/cat-order-det");

        return new ResponseEntity<>(listCustomerOrdersDetDTO, headers, HttpStatus.OK);
    }
}