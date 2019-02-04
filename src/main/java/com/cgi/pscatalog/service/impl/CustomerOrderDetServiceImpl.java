package com.cgi.pscatalog.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cgi.pscatalog.security.SecurityUtils;
import com.cgi.pscatalog.service.CustomerOrderDetService;
import com.cgi.pscatalog.service.OrderDetService;
import com.cgi.pscatalog.service.OrdersService;
import com.cgi.pscatalog.service.ProductsService;
import com.cgi.pscatalog.service.dto.CustomerOrdersDetDTO;
import com.cgi.pscatalog.service.dto.OrderDetDTO;
import com.cgi.pscatalog.service.dto.OrdersDTO;
import com.cgi.pscatalog.service.dto.ProductsDTO;
import com.cgi.pscatalog.service.util.PageDataUtil;

/**
 * Service Implementation for managing OrderDet.
 */
@Service
@Transactional
public class CustomerOrderDetServiceImpl implements CustomerOrderDetService {

    private final Logger log = LoggerFactory.getLogger(CustomerOrderDetServiceImpl.class);

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private OrderDetService orderDetService;

    @Autowired
	private ProductsService productsService;

	@Override
	@Transactional(readOnly = true)
    public PageDataUtil<OrderDetDTO, CustomerOrdersDetDTO> getAllCustomerOrderDets(Pageable pageable) {
        // Get customer order details by order id and status different from PENDING
        Page<OrderDetDTO> page = orderDetService.getAllByLoginAndOrderStatus(SecurityUtils.getCurrentUserLogin().get(), pageable);

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

			listCustomerOrdersDetDTO.add(customerOrdersDetDTO);
		}

		// Set page information
		PageDataUtil<OrderDetDTO, CustomerOrdersDetDTO> pageDataUtil = new PageDataUtil<OrderDetDTO, CustomerOrdersDetDTO>();
		pageDataUtil.setPageInformation(page);
		pageDataUtil.setContent(listCustomerOrdersDetDTO);

		return pageDataUtil;
    }

	@Override
	@Transactional(readOnly = true)
    public PageDataUtil<OrderDetDTO, CustomerOrdersDetDTO> getAllCustomerOrderDetsByOrderId(Long orderId, Pageable pageable) {
        // Get customer order details by order id
        Page<OrderDetDTO> page = orderDetService.getAllByLoginAndOrderIdAndOrderStatus(SecurityUtils.getCurrentUserLogin().get(), orderId, pageable);

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

		// Set page information
		PageDataUtil<OrderDetDTO, CustomerOrdersDetDTO> pageDataUtil = new PageDataUtil<OrderDetDTO, CustomerOrdersDetDTO>();
		pageDataUtil.setPageInformation(page);
		pageDataUtil.setContent(listCustomerOrdersDetDTO);

		return pageDataUtil;
    }

	@Override
	@Transactional(readOnly = true)
    public Optional<CustomerOrdersDetDTO> getCustomerOrderDet(Long id) {
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

        return customerOrdersDetDTOOpt;
    }

	@Override
	@Transactional(readOnly = true)
    public PageDataUtil<OrderDetDTO, CustomerOrdersDetDTO> searchCustomerOrdersDet(String query, Pageable pageable) {
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

		// Set page information
		PageDataUtil<OrderDetDTO, CustomerOrdersDetDTO> pageDataUtil = new PageDataUtil<OrderDetDTO, CustomerOrdersDetDTO>();
		pageDataUtil.setPageInformation(page);
		pageDataUtil.setContent(listCustomerOrdersDetDTO);

		return pageDataUtil;
    }

	@Override
	@Transactional(readOnly = true)
    public BigDecimal getPendingOrderTotal() {
        Page<OrdersDTO> page = ordersService.getAllByLoginAndOrderStatusPending(SecurityUtils.getCurrentUserLogin().get(), PageRequest.of(0, 1));

        List<OrdersDTO> listOrdersDTO = page.getContent();

        BigDecimal orderTotal = new BigDecimal(0);

        if (listOrdersDTO.size() != 0) {
	        // Get customer order details by order id
	        orderTotal = orderDetService.getOrderTotalWithPromotions(listOrdersDTO.get(0).getId());
        }

        return orderTotal;
    }

	@Override
	@Transactional(readOnly = true)
    public BigDecimal getOrderTotalByOrderId(Long orderId) {
        // Get customer order details by order id
	    return orderDetService.getOrderTotal(orderId);
    }
}