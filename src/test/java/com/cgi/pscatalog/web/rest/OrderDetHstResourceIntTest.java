package com.cgi.pscatalog.web.rest;

import com.cgi.pscatalog.PsCatalogApp;

import com.cgi.pscatalog.domain.OrderDetHst;
import com.cgi.pscatalog.repository.OrderDetHstRepository;
import com.cgi.pscatalog.repository.search.OrderDetHstSearchRepository;
import com.cgi.pscatalog.service.OrderDetHstService;
import com.cgi.pscatalog.service.dto.OrderDetHstDTO;
import com.cgi.pscatalog.service.mapper.OrderDetHstMapper;
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

import java.math.BigDecimal;
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
 * Test class for the OrderDetHstResource REST controller.
 *
 * @see OrderDetHstResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PsCatalogApp.class)
public class OrderDetHstResourceIntTest {

    private static final Integer DEFAULT_ORDER_ID = 1;
    private static final Integer UPDATED_ORDER_ID = 2;

    private static final Integer DEFAULT_PRODUCT_ID = 1;
    private static final Integer UPDATED_PRODUCT_ID = 2;

    private static final Integer DEFAULT_ORDER_QUANTITY = 1;
    private static final Integer UPDATED_ORDER_QUANTITY = 2;

    private static final BigDecimal DEFAULT_UNIT_PRICE = new BigDecimal("1");
    private static final BigDecimal UPDATED_UNIT_PRICE = new BigDecimal("2");

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private OrderDetHstRepository orderDetHstRepository;

    @Autowired
    private OrderDetHstMapper orderDetHstMapper;

    @Autowired
    private OrderDetHstService orderDetHstService;

    /**
     * This repository is mocked in the com.cgi.pscatalog.repository.search test package.
     *
     * @see com.cgi.pscatalog.repository.search.OrderDetHstSearchRepositoryMockConfiguration
     */
    @Autowired
    private OrderDetHstSearchRepository mockOrderDetHstSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restOrderDetHstMockMvc;

    private OrderDetHst orderDetHst;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final OrderDetHstResource orderDetHstResource = new OrderDetHstResource(orderDetHstService);
        this.restOrderDetHstMockMvc = MockMvcBuilders.standaloneSetup(orderDetHstResource)
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
    public static OrderDetHst createEntity(EntityManager em) {
        OrderDetHst orderDetHst = new OrderDetHst()
            .orderId(DEFAULT_ORDER_ID)
            .productId(DEFAULT_PRODUCT_ID)
            .orderQuantity(DEFAULT_ORDER_QUANTITY)
            .unitPrice(DEFAULT_UNIT_PRICE)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return orderDetHst;
    }

    @Before
    public void initTest() {
        orderDetHst = createEntity(em);
    }

    @Test
    @Transactional
    public void createOrderDetHst() throws Exception {
        int databaseSizeBeforeCreate = orderDetHstRepository.findAll().size();

        // Create the OrderDetHst
        OrderDetHstDTO orderDetHstDTO = orderDetHstMapper.toDto(orderDetHst);
        restOrderDetHstMockMvc.perform(post("/api/order-det-hsts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orderDetHstDTO)))
            .andExpect(status().isCreated());

        // Validate the OrderDetHst in the database
        List<OrderDetHst> orderDetHstList = orderDetHstRepository.findAll();
        assertThat(orderDetHstList).hasSize(databaseSizeBeforeCreate + 1);
        OrderDetHst testOrderDetHst = orderDetHstList.get(orderDetHstList.size() - 1);
        assertThat(testOrderDetHst.getOrderId()).isEqualTo(DEFAULT_ORDER_ID);
        assertThat(testOrderDetHst.getProductId()).isEqualTo(DEFAULT_PRODUCT_ID);
        assertThat(testOrderDetHst.getOrderQuantity()).isEqualTo(DEFAULT_ORDER_QUANTITY);
        assertThat(testOrderDetHst.getUnitPrice()).isEqualTo(DEFAULT_UNIT_PRICE);
        assertThat(testOrderDetHst.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testOrderDetHst.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testOrderDetHst.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testOrderDetHst.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);

        // Validate the OrderDetHst in Elasticsearch
        verify(mockOrderDetHstSearchRepository, times(1)).save(testOrderDetHst);
    }

    @Test
    @Transactional
    public void createOrderDetHstWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = orderDetHstRepository.findAll().size();

        // Create the OrderDetHst with an existing ID
        orderDetHst.setId(1L);
        OrderDetHstDTO orderDetHstDTO = orderDetHstMapper.toDto(orderDetHst);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrderDetHstMockMvc.perform(post("/api/order-det-hsts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orderDetHstDTO)))
            .andExpect(status().isBadRequest());

        // Validate the OrderDetHst in the database
        List<OrderDetHst> orderDetHstList = orderDetHstRepository.findAll();
        assertThat(orderDetHstList).hasSize(databaseSizeBeforeCreate);

        // Validate the OrderDetHst in Elasticsearch
        verify(mockOrderDetHstSearchRepository, times(0)).save(orderDetHst);
    }

    @Test
    @Transactional
    public void checkOrderIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderDetHstRepository.findAll().size();
        // set the field null
        orderDetHst.setOrderId(null);

        // Create the OrderDetHst, which fails.
        OrderDetHstDTO orderDetHstDTO = orderDetHstMapper.toDto(orderDetHst);

        restOrderDetHstMockMvc.perform(post("/api/order-det-hsts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orderDetHstDTO)))
            .andExpect(status().isBadRequest());

        List<OrderDetHst> orderDetHstList = orderDetHstRepository.findAll();
        assertThat(orderDetHstList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkProductIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderDetHstRepository.findAll().size();
        // set the field null
        orderDetHst.setProductId(null);

        // Create the OrderDetHst, which fails.
        OrderDetHstDTO orderDetHstDTO = orderDetHstMapper.toDto(orderDetHst);

        restOrderDetHstMockMvc.perform(post("/api/order-det-hsts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orderDetHstDTO)))
            .andExpect(status().isBadRequest());

        List<OrderDetHst> orderDetHstList = orderDetHstRepository.findAll();
        assertThat(orderDetHstList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkOrderQuantityIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderDetHstRepository.findAll().size();
        // set the field null
        orderDetHst.setOrderQuantity(null);

        // Create the OrderDetHst, which fails.
        OrderDetHstDTO orderDetHstDTO = orderDetHstMapper.toDto(orderDetHst);

        restOrderDetHstMockMvc.perform(post("/api/order-det-hsts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orderDetHstDTO)))
            .andExpect(status().isBadRequest());

        List<OrderDetHst> orderDetHstList = orderDetHstRepository.findAll();
        assertThat(orderDetHstList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUnitPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderDetHstRepository.findAll().size();
        // set the field null
        orderDetHst.setUnitPrice(null);

        // Create the OrderDetHst, which fails.
        OrderDetHstDTO orderDetHstDTO = orderDetHstMapper.toDto(orderDetHst);

        restOrderDetHstMockMvc.perform(post("/api/order-det-hsts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orderDetHstDTO)))
            .andExpect(status().isBadRequest());

        List<OrderDetHst> orderDetHstList = orderDetHstRepository.findAll();
        assertThat(orderDetHstList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllOrderDetHsts() throws Exception {
        // Initialize the database
        orderDetHstRepository.saveAndFlush(orderDetHst);

        // Get all the orderDetHstList
        restOrderDetHstMockMvc.perform(get("/api/order-det-hsts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderDetHst.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderId").value(hasItem(DEFAULT_ORDER_ID)))
            .andExpect(jsonPath("$.[*].productId").value(hasItem(DEFAULT_PRODUCT_ID)))
            .andExpect(jsonPath("$.[*].orderQuantity").value(hasItem(DEFAULT_ORDER_QUANTITY)))
            .andExpect(jsonPath("$.[*].unitPrice").value(hasItem(DEFAULT_UNIT_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getOrderDetHst() throws Exception {
        // Initialize the database
        orderDetHstRepository.saveAndFlush(orderDetHst);

        // Get the orderDetHst
        restOrderDetHstMockMvc.perform(get("/api/order-det-hsts/{id}", orderDetHst.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(orderDetHst.getId().intValue()))
            .andExpect(jsonPath("$.orderId").value(DEFAULT_ORDER_ID))
            .andExpect(jsonPath("$.productId").value(DEFAULT_PRODUCT_ID))
            .andExpect(jsonPath("$.orderQuantity").value(DEFAULT_ORDER_QUANTITY))
            .andExpect(jsonPath("$.unitPrice").value(DEFAULT_UNIT_PRICE.intValue()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingOrderDetHst() throws Exception {
        // Get the orderDetHst
        restOrderDetHstMockMvc.perform(get("/api/order-det-hsts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOrderDetHst() throws Exception {
        // Initialize the database
        orderDetHstRepository.saveAndFlush(orderDetHst);

        int databaseSizeBeforeUpdate = orderDetHstRepository.findAll().size();

        // Update the orderDetHst
        OrderDetHst updatedOrderDetHst = orderDetHstRepository.findById(orderDetHst.getId()).get();
        // Disconnect from session so that the updates on updatedOrderDetHst are not directly saved in db
        em.detach(updatedOrderDetHst);
        updatedOrderDetHst
            .orderId(UPDATED_ORDER_ID)
            .productId(UPDATED_PRODUCT_ID)
            .orderQuantity(UPDATED_ORDER_QUANTITY)
            .unitPrice(UPDATED_UNIT_PRICE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        OrderDetHstDTO orderDetHstDTO = orderDetHstMapper.toDto(updatedOrderDetHst);

        restOrderDetHstMockMvc.perform(put("/api/order-det-hsts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orderDetHstDTO)))
            .andExpect(status().isOk());

        // Validate the OrderDetHst in the database
        List<OrderDetHst> orderDetHstList = orderDetHstRepository.findAll();
        assertThat(orderDetHstList).hasSize(databaseSizeBeforeUpdate);
        OrderDetHst testOrderDetHst = orderDetHstList.get(orderDetHstList.size() - 1);
        assertThat(testOrderDetHst.getOrderId()).isEqualTo(UPDATED_ORDER_ID);
        assertThat(testOrderDetHst.getProductId()).isEqualTo(UPDATED_PRODUCT_ID);
        assertThat(testOrderDetHst.getOrderQuantity()).isEqualTo(UPDATED_ORDER_QUANTITY);
        assertThat(testOrderDetHst.getUnitPrice()).isEqualTo(UPDATED_UNIT_PRICE);
        assertThat(testOrderDetHst.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testOrderDetHst.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testOrderDetHst.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testOrderDetHst.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);

        // Validate the OrderDetHst in Elasticsearch
        verify(mockOrderDetHstSearchRepository, times(1)).save(testOrderDetHst);
    }

    @Test
    @Transactional
    public void updateNonExistingOrderDetHst() throws Exception {
        int databaseSizeBeforeUpdate = orderDetHstRepository.findAll().size();

        // Create the OrderDetHst
        OrderDetHstDTO orderDetHstDTO = orderDetHstMapper.toDto(orderDetHst);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderDetHstMockMvc.perform(put("/api/order-det-hsts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orderDetHstDTO)))
            .andExpect(status().isBadRequest());

        // Validate the OrderDetHst in the database
        List<OrderDetHst> orderDetHstList = orderDetHstRepository.findAll();
        assertThat(orderDetHstList).hasSize(databaseSizeBeforeUpdate);

        // Validate the OrderDetHst in Elasticsearch
        verify(mockOrderDetHstSearchRepository, times(0)).save(orderDetHst);
    }

    @Test
    @Transactional
    public void deleteOrderDetHst() throws Exception {
        // Initialize the database
        orderDetHstRepository.saveAndFlush(orderDetHst);

        int databaseSizeBeforeDelete = orderDetHstRepository.findAll().size();

        // Get the orderDetHst
        restOrderDetHstMockMvc.perform(delete("/api/order-det-hsts/{id}", orderDetHst.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<OrderDetHst> orderDetHstList = orderDetHstRepository.findAll();
        assertThat(orderDetHstList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the OrderDetHst in Elasticsearch
        verify(mockOrderDetHstSearchRepository, times(1)).deleteById(orderDetHst.getId());
    }

    @Test
    @Transactional
    public void searchOrderDetHst() throws Exception {
        // Initialize the database
        orderDetHstRepository.saveAndFlush(orderDetHst);
        when(mockOrderDetHstSearchRepository.search(queryStringQuery("id:" + orderDetHst.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(orderDetHst), PageRequest.of(0, 1), 1));
        // Search the orderDetHst
        restOrderDetHstMockMvc.perform(get("/api/_search/order-det-hsts?query=id:" + orderDetHst.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderDetHst.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderId").value(hasItem(DEFAULT_ORDER_ID)))
            .andExpect(jsonPath("$.[*].productId").value(hasItem(DEFAULT_PRODUCT_ID)))
            .andExpect(jsonPath("$.[*].orderQuantity").value(hasItem(DEFAULT_ORDER_QUANTITY)))
            .andExpect(jsonPath("$.[*].unitPrice").value(hasItem(DEFAULT_UNIT_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderDetHst.class);
        OrderDetHst orderDetHst1 = new OrderDetHst();
        orderDetHst1.setId(1L);
        OrderDetHst orderDetHst2 = new OrderDetHst();
        orderDetHst2.setId(orderDetHst1.getId());
        assertThat(orderDetHst1).isEqualTo(orderDetHst2);
        orderDetHst2.setId(2L);
        assertThat(orderDetHst1).isNotEqualTo(orderDetHst2);
        orderDetHst1.setId(null);
        assertThat(orderDetHst1).isNotEqualTo(orderDetHst2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderDetHstDTO.class);
        OrderDetHstDTO orderDetHstDTO1 = new OrderDetHstDTO();
        orderDetHstDTO1.setId(1L);
        OrderDetHstDTO orderDetHstDTO2 = new OrderDetHstDTO();
        assertThat(orderDetHstDTO1).isNotEqualTo(orderDetHstDTO2);
        orderDetHstDTO2.setId(orderDetHstDTO1.getId());
        assertThat(orderDetHstDTO1).isEqualTo(orderDetHstDTO2);
        orderDetHstDTO2.setId(2L);
        assertThat(orderDetHstDTO1).isNotEqualTo(orderDetHstDTO2);
        orderDetHstDTO1.setId(null);
        assertThat(orderDetHstDTO1).isNotEqualTo(orderDetHstDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(orderDetHstMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(orderDetHstMapper.fromId(null)).isNull();
    }
}
