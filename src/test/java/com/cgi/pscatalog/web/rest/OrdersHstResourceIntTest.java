package com.cgi.pscatalog.web.rest;

import com.cgi.pscatalog.PsCatalogApp;

import com.cgi.pscatalog.domain.OrdersHst;
import com.cgi.pscatalog.repository.OrdersHstRepository;
import com.cgi.pscatalog.repository.search.OrdersHstSearchRepository;
import com.cgi.pscatalog.service.OrdersHstService;
import com.cgi.pscatalog.service.dto.OrdersHstDTO;
import com.cgi.pscatalog.service.mapper.OrdersHstMapper;
import com.cgi.pscatalog.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;


import static com.cgi.pscatalog.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the OrdersHstResource REST controller.
 *
 * @see OrdersHstResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PsCatalogApp.class)
public class OrdersHstResourceIntTest {

    private static final String DEFAULT_ORDER_REFERENCE = "AAAAAAAAAA";
    private static final String UPDATED_ORDER_REFERENCE = "BBBBBBBBBB";

    private static final Integer DEFAULT_CUSTOMER_ID = 1;
    private static final Integer UPDATED_CUSTOMER_ID = 2;

    private static final Integer DEFAULT_ORDER_STATUS_CODE = 1;
    private static final Integer UPDATED_ORDER_STATUS_CODE = 2;

    private static final Instant DEFAULT_ORDER_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ORDER_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_BILLING_ADDRESS = 1;
    private static final Integer UPDATED_BILLING_ADDRESS = 2;

    private static final Integer DEFAULT_DELIVERY_ADDRESS = 1;
    private static final Integer UPDATED_DELIVERY_ADDRESS = 2;

    private static final Instant DEFAULT_DELIVERY_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DELIVERY_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private OrdersHstRepository ordersHstRepository;

    @Autowired
    private OrdersHstMapper ordersHstMapper;

    @Autowired
    private OrdersHstService ordersHstService;

    /**
     * This repository is mocked in the com.cgi.pscatalog.repository.search test package.
     *
     * @see com.cgi.pscatalog.repository.search.OrdersHstSearchRepositoryMockConfiguration
     */
    @Autowired
    private OrdersHstSearchRepository mockOrdersHstSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restOrdersHstMockMvc;

    private OrdersHst ordersHst;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final OrdersHstResource ordersHstResource = new OrdersHstResource(ordersHstService);
        this.restOrdersHstMockMvc = MockMvcBuilders.standaloneSetup(ordersHstResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrdersHst createEntity(EntityManager em) {
        OrdersHst ordersHst = new OrdersHst()
            .orderReference(DEFAULT_ORDER_REFERENCE)
            .customerId(DEFAULT_CUSTOMER_ID)
            .orderStatusCode(DEFAULT_ORDER_STATUS_CODE)
            .orderDate(DEFAULT_ORDER_DATE)
            .billingAddress(DEFAULT_BILLING_ADDRESS)
            .deliveryAddress(DEFAULT_DELIVERY_ADDRESS)
            .deliveryDate(DEFAULT_DELIVERY_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return ordersHst;
    }

    @Before
    public void initTest() {
        ordersHst = createEntity(em);
    }

    @Test
    @Transactional
    public void createOrdersHst() throws Exception {
        int databaseSizeBeforeCreate = ordersHstRepository.findAll().size();

        // Create the OrdersHst
        OrdersHstDTO ordersHstDTO = ordersHstMapper.toDto(ordersHst);
        restOrdersHstMockMvc.perform(post("/api/orders-hsts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ordersHstDTO)))
            .andExpect(status().isCreated());

        // Validate the OrdersHst in the database
        List<OrdersHst> ordersHstList = ordersHstRepository.findAll();
        assertThat(ordersHstList).hasSize(databaseSizeBeforeCreate + 1);
        OrdersHst testOrdersHst = ordersHstList.get(ordersHstList.size() - 1);
        assertThat(testOrdersHst.getOrderReference()).isEqualTo(DEFAULT_ORDER_REFERENCE);
        assertThat(testOrdersHst.getCustomerId()).isEqualTo(DEFAULT_CUSTOMER_ID);
        assertThat(testOrdersHst.getOrderStatusCode()).isEqualTo(DEFAULT_ORDER_STATUS_CODE);
        assertThat(testOrdersHst.getOrderDate()).isEqualTo(DEFAULT_ORDER_DATE);
        assertThat(testOrdersHst.getBillingAddress()).isEqualTo(DEFAULT_BILLING_ADDRESS);
        assertThat(testOrdersHst.getDeliveryAddress()).isEqualTo(DEFAULT_DELIVERY_ADDRESS);
        assertThat(testOrdersHst.getDeliveryDate()).isEqualTo(DEFAULT_DELIVERY_DATE);
        assertThat(testOrdersHst.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testOrdersHst.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testOrdersHst.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testOrdersHst.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);

        // Validate the OrdersHst in Elasticsearch
        verify(mockOrdersHstSearchRepository, times(1)).save(testOrdersHst);
    }

    @Test
    @Transactional
    public void createOrdersHstWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = ordersHstRepository.findAll().size();

        // Create the OrdersHst with an existing ID
        ordersHst.setId(1L);
        OrdersHstDTO ordersHstDTO = ordersHstMapper.toDto(ordersHst);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrdersHstMockMvc.perform(post("/api/orders-hsts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ordersHstDTO)))
            .andExpect(status().isBadRequest());

        // Validate the OrdersHst in the database
        List<OrdersHst> ordersHstList = ordersHstRepository.findAll();
        assertThat(ordersHstList).hasSize(databaseSizeBeforeCreate);

        // Validate the OrdersHst in Elasticsearch
        verify(mockOrdersHstSearchRepository, times(0)).save(ordersHst);
    }

    @Test
    @Transactional
    public void checkOrderReferenceIsRequired() throws Exception {
        int databaseSizeBeforeTest = ordersHstRepository.findAll().size();
        // set the field null
        ordersHst.setOrderReference(null);

        // Create the OrdersHst, which fails.
        OrdersHstDTO ordersHstDTO = ordersHstMapper.toDto(ordersHst);

        restOrdersHstMockMvc.perform(post("/api/orders-hsts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ordersHstDTO)))
            .andExpect(status().isBadRequest());

        List<OrdersHst> ordersHstList = ordersHstRepository.findAll();
        assertThat(ordersHstList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCustomerIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = ordersHstRepository.findAll().size();
        // set the field null
        ordersHst.setCustomerId(null);

        // Create the OrdersHst, which fails.
        OrdersHstDTO ordersHstDTO = ordersHstMapper.toDto(ordersHst);

        restOrdersHstMockMvc.perform(post("/api/orders-hsts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ordersHstDTO)))
            .andExpect(status().isBadRequest());

        List<OrdersHst> ordersHstList = ordersHstRepository.findAll();
        assertThat(ordersHstList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkOrderStatusCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = ordersHstRepository.findAll().size();
        // set the field null
        ordersHst.setOrderStatusCode(null);

        // Create the OrdersHst, which fails.
        OrdersHstDTO ordersHstDTO = ordersHstMapper.toDto(ordersHst);

        restOrdersHstMockMvc.perform(post("/api/orders-hsts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ordersHstDTO)))
            .andExpect(status().isBadRequest());

        List<OrdersHst> ordersHstList = ordersHstRepository.findAll();
        assertThat(ordersHstList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkOrderDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = ordersHstRepository.findAll().size();
        // set the field null
        ordersHst.setOrderDate(null);

        // Create the OrdersHst, which fails.
        OrdersHstDTO ordersHstDTO = ordersHstMapper.toDto(ordersHst);

        restOrdersHstMockMvc.perform(post("/api/orders-hsts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ordersHstDTO)))
            .andExpect(status().isBadRequest());

        List<OrdersHst> ordersHstList = ordersHstRepository.findAll();
        assertThat(ordersHstList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBillingAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = ordersHstRepository.findAll().size();
        // set the field null
        ordersHst.setBillingAddress(null);

        // Create the OrdersHst, which fails.
        OrdersHstDTO ordersHstDTO = ordersHstMapper.toDto(ordersHst);

        restOrdersHstMockMvc.perform(post("/api/orders-hsts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ordersHstDTO)))
            .andExpect(status().isBadRequest());

        List<OrdersHst> ordersHstList = ordersHstRepository.findAll();
        assertThat(ordersHstList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDeliveryAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = ordersHstRepository.findAll().size();
        // set the field null
        ordersHst.setDeliveryAddress(null);

        // Create the OrdersHst, which fails.
        OrdersHstDTO ordersHstDTO = ordersHstMapper.toDto(ordersHst);

        restOrdersHstMockMvc.perform(post("/api/orders-hsts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ordersHstDTO)))
            .andExpect(status().isBadRequest());

        List<OrdersHst> ordersHstList = ordersHstRepository.findAll();
        assertThat(ordersHstList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllOrdersHsts() throws Exception {
        // Initialize the database
        ordersHstRepository.saveAndFlush(ordersHst);

        // Get all the ordersHstList
        restOrdersHstMockMvc.perform(get("/api/orders-hsts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ordersHst.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderReference").value(hasItem(DEFAULT_ORDER_REFERENCE.toString())))
            .andExpect(jsonPath("$.[*].customerId").value(hasItem(DEFAULT_CUSTOMER_ID)))
            .andExpect(jsonPath("$.[*].orderStatusCode").value(hasItem(DEFAULT_ORDER_STATUS_CODE)))
            .andExpect(jsonPath("$.[*].orderDate").value(hasItem(DEFAULT_ORDER_DATE.toString())))
            .andExpect(jsonPath("$.[*].billingAddress").value(hasItem(DEFAULT_BILLING_ADDRESS)))
            .andExpect(jsonPath("$.[*].deliveryAddress").value(hasItem(DEFAULT_DELIVERY_ADDRESS)))
            .andExpect(jsonPath("$.[*].deliveryDate").value(hasItem(DEFAULT_DELIVERY_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getOrdersHst() throws Exception {
        // Initialize the database
        ordersHstRepository.saveAndFlush(ordersHst);

        // Get the ordersHst
        restOrdersHstMockMvc.perform(get("/api/orders-hsts/{id}", ordersHst.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(ordersHst.getId().intValue()))
            .andExpect(jsonPath("$.orderReference").value(DEFAULT_ORDER_REFERENCE.toString()))
            .andExpect(jsonPath("$.customerId").value(DEFAULT_CUSTOMER_ID))
            .andExpect(jsonPath("$.orderStatusCode").value(DEFAULT_ORDER_STATUS_CODE))
            .andExpect(jsonPath("$.orderDate").value(DEFAULT_ORDER_DATE.toString()))
            .andExpect(jsonPath("$.billingAddress").value(DEFAULT_BILLING_ADDRESS))
            .andExpect(jsonPath("$.deliveryAddress").value(DEFAULT_DELIVERY_ADDRESS))
            .andExpect(jsonPath("$.deliveryDate").value(DEFAULT_DELIVERY_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingOrdersHst() throws Exception {
        // Get the ordersHst
        restOrdersHstMockMvc.perform(get("/api/orders-hsts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOrdersHst() throws Exception {
        // Initialize the database
        ordersHstRepository.saveAndFlush(ordersHst);

        int databaseSizeBeforeUpdate = ordersHstRepository.findAll().size();

        // Update the ordersHst
        OrdersHst updatedOrdersHst = ordersHstRepository.findById(ordersHst.getId()).get();
        // Disconnect from session so that the updates on updatedOrdersHst are not directly saved in db
        em.detach(updatedOrdersHst);
        updatedOrdersHst
            .orderReference(UPDATED_ORDER_REFERENCE)
            .customerId(UPDATED_CUSTOMER_ID)
            .orderStatusCode(UPDATED_ORDER_STATUS_CODE)
            .orderDate(UPDATED_ORDER_DATE)
            .billingAddress(UPDATED_BILLING_ADDRESS)
            .deliveryAddress(UPDATED_DELIVERY_ADDRESS)
            .deliveryDate(UPDATED_DELIVERY_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        OrdersHstDTO ordersHstDTO = ordersHstMapper.toDto(updatedOrdersHst);

        restOrdersHstMockMvc.perform(put("/api/orders-hsts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ordersHstDTO)))
            .andExpect(status().isOk());

        // Validate the OrdersHst in the database
        List<OrdersHst> ordersHstList = ordersHstRepository.findAll();
        assertThat(ordersHstList).hasSize(databaseSizeBeforeUpdate);
        OrdersHst testOrdersHst = ordersHstList.get(ordersHstList.size() - 1);
        assertThat(testOrdersHst.getOrderReference()).isEqualTo(UPDATED_ORDER_REFERENCE);
        assertThat(testOrdersHst.getCustomerId()).isEqualTo(UPDATED_CUSTOMER_ID);
        assertThat(testOrdersHst.getOrderStatusCode()).isEqualTo(UPDATED_ORDER_STATUS_CODE);
        assertThat(testOrdersHst.getOrderDate()).isEqualTo(UPDATED_ORDER_DATE);
        assertThat(testOrdersHst.getBillingAddress()).isEqualTo(UPDATED_BILLING_ADDRESS);
        assertThat(testOrdersHst.getDeliveryAddress()).isEqualTo(UPDATED_DELIVERY_ADDRESS);
        assertThat(testOrdersHst.getDeliveryDate()).isEqualTo(UPDATED_DELIVERY_DATE);
        assertThat(testOrdersHst.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testOrdersHst.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testOrdersHst.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testOrdersHst.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);

        // Validate the OrdersHst in Elasticsearch
        verify(mockOrdersHstSearchRepository, times(1)).save(testOrdersHst);
    }

    @Test
    @Transactional
    public void updateNonExistingOrdersHst() throws Exception {
        int databaseSizeBeforeUpdate = ordersHstRepository.findAll().size();

        // Create the OrdersHst
        OrdersHstDTO ordersHstDTO = ordersHstMapper.toDto(ordersHst);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrdersHstMockMvc.perform(put("/api/orders-hsts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ordersHstDTO)))
            .andExpect(status().isBadRequest());

        // Validate the OrdersHst in the database
        List<OrdersHst> ordersHstList = ordersHstRepository.findAll();
        assertThat(ordersHstList).hasSize(databaseSizeBeforeUpdate);

        // Validate the OrdersHst in Elasticsearch
        verify(mockOrdersHstSearchRepository, times(0)).save(ordersHst);
    }

    @Test
    @Transactional
    public void deleteOrdersHst() throws Exception {
        // Initialize the database
        ordersHstRepository.saveAndFlush(ordersHst);

        int databaseSizeBeforeDelete = ordersHstRepository.findAll().size();

        // Get the ordersHst
        restOrdersHstMockMvc.perform(delete("/api/orders-hsts/{id}", ordersHst.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<OrdersHst> ordersHstList = ordersHstRepository.findAll();
        assertThat(ordersHstList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the OrdersHst in Elasticsearch
        verify(mockOrdersHstSearchRepository, times(1)).deleteById(ordersHst.getId());
    }

    @Test
    @Transactional
    public void searchOrdersHst() throws Exception {
        // Initialize the database
        ordersHstRepository.saveAndFlush(ordersHst);
        when(mockOrdersHstSearchRepository.search(queryStringQuery("id:" + ordersHst.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(ordersHst), PageRequest.of(0, 1), 1));
        // Search the ordersHst
        restOrdersHstMockMvc.perform(get("/api/_search/orders-hsts?query=id:" + ordersHst.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ordersHst.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderReference").value(hasItem(DEFAULT_ORDER_REFERENCE)))
            .andExpect(jsonPath("$.[*].customerId").value(hasItem(DEFAULT_CUSTOMER_ID)))
            .andExpect(jsonPath("$.[*].orderStatusCode").value(hasItem(DEFAULT_ORDER_STATUS_CODE)))
            .andExpect(jsonPath("$.[*].orderDate").value(hasItem(DEFAULT_ORDER_DATE.toString())))
            .andExpect(jsonPath("$.[*].billingAddress").value(hasItem(DEFAULT_BILLING_ADDRESS)))
            .andExpect(jsonPath("$.[*].deliveryAddress").value(hasItem(DEFAULT_DELIVERY_ADDRESS)))
            .andExpect(jsonPath("$.[*].deliveryDate").value(hasItem(DEFAULT_DELIVERY_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrdersHst.class);
        OrdersHst ordersHst1 = new OrdersHst();
        ordersHst1.setId(1L);
        OrdersHst ordersHst2 = new OrdersHst();
        ordersHst2.setId(ordersHst1.getId());
        assertThat(ordersHst1).isEqualTo(ordersHst2);
        ordersHst2.setId(2L);
        assertThat(ordersHst1).isNotEqualTo(ordersHst2);
        ordersHst1.setId(null);
        assertThat(ordersHst1).isNotEqualTo(ordersHst2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrdersHstDTO.class);
        OrdersHstDTO ordersHstDTO1 = new OrdersHstDTO();
        ordersHstDTO1.setId(1L);
        OrdersHstDTO ordersHstDTO2 = new OrdersHstDTO();
        assertThat(ordersHstDTO1).isNotEqualTo(ordersHstDTO2);
        ordersHstDTO2.setId(ordersHstDTO1.getId());
        assertThat(ordersHstDTO1).isEqualTo(ordersHstDTO2);
        ordersHstDTO2.setId(2L);
        assertThat(ordersHstDTO1).isNotEqualTo(ordersHstDTO2);
        ordersHstDTO1.setId(null);
        assertThat(ordersHstDTO1).isNotEqualTo(ordersHstDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(ordersHstMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(ordersHstMapper.fromId(null)).isNull();
    }
}
