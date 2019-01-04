package com.cgi.pscatalog.web.rest;

import com.cgi.pscatalog.PsCatalogApp;

import com.cgi.pscatalog.domain.Products;
import com.cgi.pscatalog.repository.ProductsRepository;
import com.cgi.pscatalog.repository.search.ProductsSearchRepository;
import com.cgi.pscatalog.service.ProductsService;
import com.cgi.pscatalog.service.dto.ProductsDTO;
import com.cgi.pscatalog.service.mapper.ProductsMapper;
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
import org.springframework.util.Base64Utils;

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

import com.cgi.pscatalog.domain.enumeration.ProductTypeEnum;
/**
 * Test class for the ProductsResource REST controller.
 *
 * @see ProductsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PsCatalogApp.class)
public class ProductsResourceIntTest {

    private static final String DEFAULT_PRODUCT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PRODUCT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_DESCRIPTION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_PRODUCT_PRICE = new BigDecimal("1");
    private static final BigDecimal UPDATED_PRODUCT_PRICE = new BigDecimal("2");

    private static final Instant DEFAULT_PRODUCT_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PRODUCT_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_PRODUCT_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PRODUCT_END_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final ProductTypeEnum DEFAULT_PRODUCT_TYPE = ProductTypeEnum.PRODUTO;
    private static final ProductTypeEnum UPDATED_PRODUCT_TYPE = ProductTypeEnum.SERVICO;

    private static final byte[] DEFAULT_PRODUCT_IMG = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PRODUCT_IMG = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PRODUCT_IMG_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PRODUCT_IMG_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private ProductsRepository productsRepository;

    @Autowired
    private ProductsMapper productsMapper;

    @Autowired
    private ProductsService productsService;

    /**
     * This repository is mocked in the com.cgi.pscatalog.repository.search test package.
     *
     * @see com.cgi.pscatalog.repository.search.ProductsSearchRepositoryMockConfiguration
     */
    @Autowired
    private ProductsSearchRepository mockProductsSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restProductsMockMvc;

    private Products products;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProductsResource productsResource = new ProductsResource(productsService);
        this.restProductsMockMvc = MockMvcBuilders.standaloneSetup(productsResource)
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
    public static Products createEntity(EntityManager em) {
        Products products = new Products()
            .productName(DEFAULT_PRODUCT_NAME)
            .productDescription(DEFAULT_PRODUCT_DESCRIPTION)
            .productPrice(DEFAULT_PRODUCT_PRICE)
            .productStartDate(DEFAULT_PRODUCT_START_DATE)
            .productEndDate(DEFAULT_PRODUCT_END_DATE)
            .productType(DEFAULT_PRODUCT_TYPE)
            .productImg(DEFAULT_PRODUCT_IMG)
            .productImgContentType(DEFAULT_PRODUCT_IMG_CONTENT_TYPE)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return products;
    }

    @Before
    public void initTest() {
        products = createEntity(em);
    }

    @Test
    @Transactional
    public void createProducts() throws Exception {
        int databaseSizeBeforeCreate = productsRepository.findAll().size();

        // Create the Products
        ProductsDTO productsDTO = productsMapper.toDto(products);
        restProductsMockMvc.perform(post("/api/products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productsDTO)))
            .andExpect(status().isCreated());

        // Validate the Products in the database
        List<Products> productsList = productsRepository.findAll();
        assertThat(productsList).hasSize(databaseSizeBeforeCreate + 1);
        Products testProducts = productsList.get(productsList.size() - 1);
        assertThat(testProducts.getProductName()).isEqualTo(DEFAULT_PRODUCT_NAME);
        assertThat(testProducts.getProductDescription()).isEqualTo(DEFAULT_PRODUCT_DESCRIPTION);
        assertThat(testProducts.getProductPrice()).isEqualTo(DEFAULT_PRODUCT_PRICE);
        assertThat(testProducts.getProductStartDate()).isEqualTo(DEFAULT_PRODUCT_START_DATE);
        assertThat(testProducts.getProductEndDate()).isEqualTo(DEFAULT_PRODUCT_END_DATE);
        assertThat(testProducts.getProductType()).isEqualTo(DEFAULT_PRODUCT_TYPE);
        assertThat(testProducts.getProductImg()).isEqualTo(DEFAULT_PRODUCT_IMG);
        assertThat(testProducts.getProductImgContentType()).isEqualTo(DEFAULT_PRODUCT_IMG_CONTENT_TYPE);
        assertThat(testProducts.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testProducts.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testProducts.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testProducts.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);

        // Validate the Products in Elasticsearch
        verify(mockProductsSearchRepository, times(1)).save(testProducts);
    }

    @Test
    @Transactional
    public void createProductsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = productsRepository.findAll().size();

        // Create the Products with an existing ID
        products.setId(1L);
        ProductsDTO productsDTO = productsMapper.toDto(products);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductsMockMvc.perform(post("/api/products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Products in the database
        List<Products> productsList = productsRepository.findAll();
        assertThat(productsList).hasSize(databaseSizeBeforeCreate);

        // Validate the Products in Elasticsearch
        verify(mockProductsSearchRepository, times(0)).save(products);
    }

    @Test
    @Transactional
    public void checkProductNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = productsRepository.findAll().size();
        // set the field null
        products.setProductName(null);

        // Create the Products, which fails.
        ProductsDTO productsDTO = productsMapper.toDto(products);

        restProductsMockMvc.perform(post("/api/products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productsDTO)))
            .andExpect(status().isBadRequest());

        List<Products> productsList = productsRepository.findAll();
        assertThat(productsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkProductPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = productsRepository.findAll().size();
        // set the field null
        products.setProductPrice(null);

        // Create the Products, which fails.
        ProductsDTO productsDTO = productsMapper.toDto(products);

        restProductsMockMvc.perform(post("/api/products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productsDTO)))
            .andExpect(status().isBadRequest());

        List<Products> productsList = productsRepository.findAll();
        assertThat(productsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkProductStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = productsRepository.findAll().size();
        // set the field null
        products.setProductStartDate(null);

        // Create the Products, which fails.
        ProductsDTO productsDTO = productsMapper.toDto(products);

        restProductsMockMvc.perform(post("/api/products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productsDTO)))
            .andExpect(status().isBadRequest());

        List<Products> productsList = productsRepository.findAll();
        assertThat(productsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkProductTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = productsRepository.findAll().size();
        // set the field null
        products.setProductType(null);

        // Create the Products, which fails.
        ProductsDTO productsDTO = productsMapper.toDto(products);

        restProductsMockMvc.perform(post("/api/products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productsDTO)))
            .andExpect(status().isBadRequest());

        List<Products> productsList = productsRepository.findAll();
        assertThat(productsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProducts() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productsList
        restProductsMockMvc.perform(get("/api/products?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(products.getId().intValue())))
            .andExpect(jsonPath("$.[*].productName").value(hasItem(DEFAULT_PRODUCT_NAME.toString())))
            .andExpect(jsonPath("$.[*].productDescription").value(hasItem(DEFAULT_PRODUCT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].productPrice").value(hasItem(DEFAULT_PRODUCT_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].productStartDate").value(hasItem(DEFAULT_PRODUCT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].productEndDate").value(hasItem(DEFAULT_PRODUCT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].productType").value(hasItem(DEFAULT_PRODUCT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].productImgContentType").value(hasItem(DEFAULT_PRODUCT_IMG_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].productImg").value(hasItem(Base64Utils.encodeToString(DEFAULT_PRODUCT_IMG))))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getProducts() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get the products
        restProductsMockMvc.perform(get("/api/products/{id}", products.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(products.getId().intValue()))
            .andExpect(jsonPath("$.productName").value(DEFAULT_PRODUCT_NAME.toString()))
            .andExpect(jsonPath("$.productDescription").value(DEFAULT_PRODUCT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.productPrice").value(DEFAULT_PRODUCT_PRICE.intValue()))
            .andExpect(jsonPath("$.productStartDate").value(DEFAULT_PRODUCT_START_DATE.toString()))
            .andExpect(jsonPath("$.productEndDate").value(DEFAULT_PRODUCT_END_DATE.toString()))
            .andExpect(jsonPath("$.productType").value(DEFAULT_PRODUCT_TYPE.toString()))
            .andExpect(jsonPath("$.productImgContentType").value(DEFAULT_PRODUCT_IMG_CONTENT_TYPE))
            .andExpect(jsonPath("$.productImg").value(Base64Utils.encodeToString(DEFAULT_PRODUCT_IMG)))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingProducts() throws Exception {
        // Get the products
        restProductsMockMvc.perform(get("/api/products/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProducts() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        int databaseSizeBeforeUpdate = productsRepository.findAll().size();

        // Update the products
        Products updatedProducts = productsRepository.findById(products.getId()).get();
        // Disconnect from session so that the updates on updatedProducts are not directly saved in db
        em.detach(updatedProducts);
        updatedProducts
            .productName(UPDATED_PRODUCT_NAME)
            .productDescription(UPDATED_PRODUCT_DESCRIPTION)
            .productPrice(UPDATED_PRODUCT_PRICE)
            .productStartDate(UPDATED_PRODUCT_START_DATE)
            .productEndDate(UPDATED_PRODUCT_END_DATE)
            .productType(UPDATED_PRODUCT_TYPE)
            .productImg(UPDATED_PRODUCT_IMG)
            .productImgContentType(UPDATED_PRODUCT_IMG_CONTENT_TYPE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        ProductsDTO productsDTO = productsMapper.toDto(updatedProducts);

        restProductsMockMvc.perform(put("/api/products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productsDTO)))
            .andExpect(status().isOk());

        // Validate the Products in the database
        List<Products> productsList = productsRepository.findAll();
        assertThat(productsList).hasSize(databaseSizeBeforeUpdate);
        Products testProducts = productsList.get(productsList.size() - 1);
        assertThat(testProducts.getProductName()).isEqualTo(UPDATED_PRODUCT_NAME);
        assertThat(testProducts.getProductDescription()).isEqualTo(UPDATED_PRODUCT_DESCRIPTION);
        assertThat(testProducts.getProductPrice()).isEqualTo(UPDATED_PRODUCT_PRICE);
        assertThat(testProducts.getProductStartDate()).isEqualTo(UPDATED_PRODUCT_START_DATE);
        assertThat(testProducts.getProductEndDate()).isEqualTo(UPDATED_PRODUCT_END_DATE);
        assertThat(testProducts.getProductType()).isEqualTo(UPDATED_PRODUCT_TYPE);
        assertThat(testProducts.getProductImg()).isEqualTo(UPDATED_PRODUCT_IMG);
        assertThat(testProducts.getProductImgContentType()).isEqualTo(UPDATED_PRODUCT_IMG_CONTENT_TYPE);
        assertThat(testProducts.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testProducts.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testProducts.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testProducts.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);

        // Validate the Products in Elasticsearch
        verify(mockProductsSearchRepository, times(1)).save(testProducts);
    }

    @Test
    @Transactional
    public void updateNonExistingProducts() throws Exception {
        int databaseSizeBeforeUpdate = productsRepository.findAll().size();

        // Create the Products
        ProductsDTO productsDTO = productsMapper.toDto(products);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductsMockMvc.perform(put("/api/products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Products in the database
        List<Products> productsList = productsRepository.findAll();
        assertThat(productsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Products in Elasticsearch
        verify(mockProductsSearchRepository, times(0)).save(products);
    }

    @Test
    @Transactional
    public void deleteProducts() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        int databaseSizeBeforeDelete = productsRepository.findAll().size();

        // Get the products
        restProductsMockMvc.perform(delete("/api/products/{id}", products.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Products> productsList = productsRepository.findAll();
        assertThat(productsList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Products in Elasticsearch
        verify(mockProductsSearchRepository, times(1)).deleteById(products.getId());
    }

    @Test
    @Transactional
    public void searchProducts() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);
        when(mockProductsSearchRepository.search(queryStringQuery("id:" + products.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(products), PageRequest.of(0, 1), 1));
        // Search the products
        restProductsMockMvc.perform(get("/api/_search/products?query=id:" + products.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(products.getId().intValue())))
            .andExpect(jsonPath("$.[*].productName").value(hasItem(DEFAULT_PRODUCT_NAME)))
            .andExpect(jsonPath("$.[*].productDescription").value(hasItem(DEFAULT_PRODUCT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].productPrice").value(hasItem(DEFAULT_PRODUCT_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].productStartDate").value(hasItem(DEFAULT_PRODUCT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].productEndDate").value(hasItem(DEFAULT_PRODUCT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].productType").value(hasItem(DEFAULT_PRODUCT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].productImgContentType").value(hasItem(DEFAULT_PRODUCT_IMG_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].productImg").value(hasItem(Base64Utils.encodeToString(DEFAULT_PRODUCT_IMG))))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Products.class);
        Products products1 = new Products();
        products1.setId(1L);
        Products products2 = new Products();
        products2.setId(products1.getId());
        assertThat(products1).isEqualTo(products2);
        products2.setId(2L);
        assertThat(products1).isNotEqualTo(products2);
        products1.setId(null);
        assertThat(products1).isNotEqualTo(products2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductsDTO.class);
        ProductsDTO productsDTO1 = new ProductsDTO();
        productsDTO1.setId(1L);
        ProductsDTO productsDTO2 = new ProductsDTO();
        assertThat(productsDTO1).isNotEqualTo(productsDTO2);
        productsDTO2.setId(productsDTO1.getId());
        assertThat(productsDTO1).isEqualTo(productsDTO2);
        productsDTO2.setId(2L);
        assertThat(productsDTO1).isNotEqualTo(productsDTO2);
        productsDTO1.setId(null);
        assertThat(productsDTO1).isNotEqualTo(productsDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(productsMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(productsMapper.fromId(null)).isNull();
    }
}
