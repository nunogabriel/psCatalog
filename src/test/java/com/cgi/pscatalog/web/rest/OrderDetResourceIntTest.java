package com.cgi.pscatalog.web.rest;

import com.cgi.pscatalog.PsCatalogApp;

import com.cgi.pscatalog.domain.OrderDet;
import com.cgi.pscatalog.domain.Orders;
import com.cgi.pscatalog.domain.Products;
import com.cgi.pscatalog.repository.OrderDetRepository;
import com.cgi.pscatalog.repository.search.OrderDetSearchRepository;
import com.cgi.pscatalog.service.OrderDetService;
import com.cgi.pscatalog.service.dto.OrderDetDTO;
import com.cgi.pscatalog.service.mapper.OrderDetMapper;
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
 * Test class for the OrderDetResource REST controller.
 *
 * @see OrderDetResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PsCatalogApp.class)
public class OrderDetResourceIntTest {

    private static final Integer DEFAULT_ORDER_QUANTITY = 1;
    private static final Integer UPDATED_ORDER_QUANTITY = 2;

    private static final Long DEFAULT_UNIT_PRICE = 1L;
    private static final Long UPDATED_UNIT_PRICE = 2L;

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private OrderDetRepository orderDetRepository;

    @Autowired
    private OrderDetMapper orderDetMapper;

    @Autowired
    private OrderDetService orderDetService;

    /**
     * This repository is mocked in the com.cgi.pscatalog.repository.search test package.
     *
     * @see com.cgi.pscatalog.repository.search.OrderDetSearchRepositoryMockConfiguration
     */
    @Autowired
    private OrderDetSearchRepository mockOrderDetSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restOrderDetMockMvc;

    private OrderDet orderDet;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final OrderDetResource orderDetResource = new OrderDetResource(orderDetService);
        this.restOrderDetMockMvc = MockMvcBuilders.standaloneSetup(orderDetResource)
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
    public static OrderDet createEntity(EntityManager em) {
        OrderDet orderDet = new OrderDet()
            .orderQuantity(DEFAULT_ORDER_QUANTITY)
            .unitPrice(DEFAULT_UNIT_PRICE)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        // Add required entity
        Orders orders = OrdersResourceIntTest.createEntity(em);
        em.persist(orders);
        em.flush();
        orderDet.setOrder(orders);
        // Add required entity
        Products products = ProductsResourceIntTest.createEntity(em);
        em.persist(products);
        em.flush();
        orderDet.setProduct(products);
        return orderDet;
    }

    @Before
    public void initTest() {
        orderDet = createEntity(em);
    }

    @Test
    @Transactional
    public void createOrderDet() throws Exception {
        int databaseSizeBeforeCreate = orderDetRepository.findAll().size();

        // Create the OrderDet
        OrderDetDTO orderDetDTO = orderDetMapper.toDto(orderDet);
        restOrderDetMockMvc.perform(post("/api/order-dets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orderDetDTO)))
            .andExpect(status().isCreated());

        // Validate the OrderDet in the database
        List<OrderDet> orderDetList = orderDetRepository.findAll();
        assertThat(orderDetList).hasSize(databaseSizeBeforeCreate + 1);
        OrderDet testOrderDet = orderDetList.get(orderDetList.size() - 1);
        assertThat(testOrderDet.getOrderQuantity()).isEqualTo(DEFAULT_ORDER_QUANTITY);
        assertThat(testOrderDet.getUnitPrice()).isEqualTo(DEFAULT_UNIT_PRICE);
        assertThat(testOrderDet.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testOrderDet.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testOrderDet.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testOrderDet.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);

        // Validate the OrderDet in Elasticsearch
        verify(mockOrderDetSearchRepository, times(1)).save(testOrderDet);
    }

    @Test
    @Transactional
    public void createOrderDetWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = orderDetRepository.findAll().size();

        // Create the OrderDet with an existing ID
        orderDet.setId(1L);
        OrderDetDTO orderDetDTO = orderDetMapper.toDto(orderDet);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrderDetMockMvc.perform(post("/api/order-dets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orderDetDTO)))
            .andExpect(status().isBadRequest());

        // Validate the OrderDet in the database
        List<OrderDet> orderDetList = orderDetRepository.findAll();
        assertThat(orderDetList).hasSize(databaseSizeBeforeCreate);

        // Validate the OrderDet in Elasticsearch
        verify(mockOrderDetSearchRepository, times(0)).save(orderDet);
    }

    @Test
    @Transactional
    public void checkOrderQuantityIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderDetRepository.findAll().size();
        // set the field null
        orderDet.setOrderQuantity(null);

        // Create the OrderDet, which fails.
        OrderDetDTO orderDetDTO = orderDetMapper.toDto(orderDet);

        restOrderDetMockMvc.perform(post("/api/order-dets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orderDetDTO)))
            .andExpect(status().isBadRequest());

        List<OrderDet> orderDetList = orderDetRepository.findAll();
        assertThat(orderDetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUnitPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderDetRepository.findAll().size();
        // set the field null
        orderDet.setUnitPrice(null);

        // Create the OrderDet, which fails.
        OrderDetDTO orderDetDTO = orderDetMapper.toDto(orderDet);

        restOrderDetMockMvc.perform(post("/api/order-dets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orderDetDTO)))
            .andExpect(status().isBadRequest());

        List<OrderDet> orderDetList = orderDetRepository.findAll();
        assertThat(orderDetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllOrderDets() throws Exception {
        // Initialize the database
        orderDetRepository.saveAndFlush(orderDet);

        // Get all the orderDetList
        restOrderDetMockMvc.perform(get("/api/order-dets?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderDet.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderQuantity").value(hasItem(DEFAULT_ORDER_QUANTITY)))
            .andExpect(jsonPath("$.[*].unitPrice").value(hasItem(DEFAULT_UNIT_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getOrderDet() throws Exception {
        // Initialize the database
        orderDetRepository.saveAndFlush(orderDet);

        // Get the orderDet
        restOrderDetMockMvc.perform(get("/api/order-dets/{id}", orderDet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(orderDet.getId().intValue()))
            .andExpect(jsonPath("$.orderQuantity").value(DEFAULT_ORDER_QUANTITY))
            .andExpect(jsonPath("$.unitPrice").value(DEFAULT_UNIT_PRICE.intValue()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingOrderDet() throws Exception {
        // Get the orderDet
        restOrderDetMockMvc.perform(get("/api/order-dets/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOrderDet() throws Exception {
        // Initialize the database
        orderDetRepository.saveAndFlush(orderDet);

        int databaseSizeBeforeUpdate = orderDetRepository.findAll().size();

        // Update the orderDet
        OrderDet updatedOrderDet = orderDetRepository.findById(orderDet.getId()).get();
        // Disconnect from session so that the updates on updatedOrderDet are not directly saved in db
        em.detach(updatedOrderDet);
        updatedOrderDet
            .orderQuantity(UPDATED_ORDER_QUANTITY)
            .unitPrice(UPDATED_UNIT_PRICE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        OrderDetDTO orderDetDTO = orderDetMapper.toDto(updatedOrderDet);

        restOrderDetMockMvc.perform(put("/api/order-dets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orderDetDTO)))
            .andExpect(status().isOk());

        // Validate the OrderDet in the database
        List<OrderDet> orderDetList = orderDetRepository.findAll();
        assertThat(orderDetList).hasSize(databaseSizeBeforeUpdate);
        OrderDet testOrderDet = orderDetList.get(orderDetList.size() - 1);
        assertThat(testOrderDet.getOrderQuantity()).isEqualTo(UPDATED_ORDER_QUANTITY);
        assertThat(testOrderDet.getUnitPrice()).isEqualTo(UPDATED_UNIT_PRICE);
        assertThat(testOrderDet.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testOrderDet.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testOrderDet.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testOrderDet.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);

        // Validate the OrderDet in Elasticsearch
        verify(mockOrderDetSearchRepository, times(1)).save(testOrderDet);
    }

    @Test
    @Transactional
    public void updateNonExistingOrderDet() throws Exception {
        int databaseSizeBeforeUpdate = orderDetRepository.findAll().size();

        // Create the OrderDet
        OrderDetDTO orderDetDTO = orderDetMapper.toDto(orderDet);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderDetMockMvc.perform(put("/api/order-dets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orderDetDTO)))
            .andExpect(status().isBadRequest());

        // Validate the OrderDet in the database
        List<OrderDet> orderDetList = orderDetRepository.findAll();
        assertThat(orderDetList).hasSize(databaseSizeBeforeUpdate);

        // Validate the OrderDet in Elasticsearch
        verify(mockOrderDetSearchRepository, times(0)).save(orderDet);
    }

    @Test
    @Transactional
    public void deleteOrderDet() throws Exception {
        // Initialize the database
        orderDetRepository.saveAndFlush(orderDet);

        int databaseSizeBeforeDelete = orderDetRepository.findAll().size();

        // Get the orderDet
        restOrderDetMockMvc.perform(delete("/api/order-dets/{id}", orderDet.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<OrderDet> orderDetList = orderDetRepository.findAll();
        assertThat(orderDetList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the OrderDet in Elasticsearch
        verify(mockOrderDetSearchRepository, times(1)).deleteById(orderDet.getId());
    }

    @Test
    @Transactional
    public void searchOrderDet() throws Exception {
        // Initialize the database
        orderDetRepository.saveAndFlush(orderDet);
        when(mockOrderDetSearchRepository.search(queryStringQuery("id:" + orderDet.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(orderDet), PageRequest.of(0, 1), 1));
        // Search the orderDet
        restOrderDetMockMvc.perform(get("/api/_search/order-dets?query=id:" + orderDet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderDet.getId().intValue())))
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
        TestUtil.equalsVerifier(OrderDet.class);
        OrderDet orderDet1 = new OrderDet();
        orderDet1.setId(1L);
        OrderDet orderDet2 = new OrderDet();
        orderDet2.setId(orderDet1.getId());
        assertThat(orderDet1).isEqualTo(orderDet2);
        orderDet2.setId(2L);
        assertThat(orderDet1).isNotEqualTo(orderDet2);
        orderDet1.setId(null);
        assertThat(orderDet1).isNotEqualTo(orderDet2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderDetDTO.class);
        OrderDetDTO orderDetDTO1 = new OrderDetDTO();
        orderDetDTO1.setId(1L);
        OrderDetDTO orderDetDTO2 = new OrderDetDTO();
        assertThat(orderDetDTO1).isNotEqualTo(orderDetDTO2);
        orderDetDTO2.setId(orderDetDTO1.getId());
        assertThat(orderDetDTO1).isEqualTo(orderDetDTO2);
        orderDetDTO2.setId(2L);
        assertThat(orderDetDTO1).isNotEqualTo(orderDetDTO2);
        orderDetDTO1.setId(null);
        assertThat(orderDetDTO1).isNotEqualTo(orderDetDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(orderDetMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(orderDetMapper.fromId(null)).isNull();
    }
}
