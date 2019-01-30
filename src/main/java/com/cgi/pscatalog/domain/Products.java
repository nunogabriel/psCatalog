package com.cgi.pscatalog.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import com.cgi.pscatalog.domain.enumeration.ProductTypeEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A Products.
 */
@Entity
@Table(name = "products")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "products")
public class Products implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "product_description")
    private String productDescription;

    @NotNull
    @Column(name = "product_price", nullable = false)
    private BigDecimal productPrice;

    @NotNull
    @Column(name = "product_start_date", nullable = false)
    private Instant productStartDate;

    @Column(name = "product_end_date")
    private Instant productEndDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "product_type", nullable = false)
    private ProductTypeEnum productType;

    @Lob
    @Column(name = "product_img")
    private byte[] productImg;

    @Column(name = "product_img_content_type")
    private String productImgContentType;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_date")
    private Instant createdDate;

    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

    @OneToMany(mappedBy = "product")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<OrderDet> orderDets = new HashSet<>();
    @ManyToOne
    @JsonIgnoreProperties("products")
    private Suppliers supplier;

    @ManyToMany(mappedBy = "products")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<Customers> customers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public Products productName(String productName) {
        this.productName = productName;
        return this;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public Products productDescription(String productDescription) {
        this.productDescription = productDescription;
        return this;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public Products productPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
        return this;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    public Instant getProductStartDate() {
        return productStartDate;
    }

    public Products productStartDate(Instant productStartDate) {
        this.productStartDate = productStartDate;
        return this;
    }

    public void setProductStartDate(Instant productStartDate) {
        this.productStartDate = productStartDate;
    }

    public Instant getProductEndDate() {
        return productEndDate;
    }

    public Products productEndDate(Instant productEndDate) {
        this.productEndDate = productEndDate;
        return this;
    }

    public void setProductEndDate(Instant productEndDate) {
        this.productEndDate = productEndDate;
    }

    public ProductTypeEnum getProductType() {
        return productType;
    }

    public Products productType(ProductTypeEnum productType) {
        this.productType = productType;
        return this;
    }

    public void setProductType(ProductTypeEnum productType) {
        this.productType = productType;
    }

    public byte[] getProductImg() {
        return productImg;
    }

    public Products productImg(byte[] productImg) {
        this.productImg = productImg;
        return this;
    }

    public void setProductImg(byte[] productImg) {
        this.productImg = productImg;
    }

    public String getProductImgContentType() {
        return productImgContentType;
    }

    public Products productImgContentType(String productImgContentType) {
        this.productImgContentType = productImgContentType;
        return this;
    }

    public void setProductImgContentType(String productImgContentType) {
        this.productImgContentType = productImgContentType;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public Products createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public Products createdDate(Instant createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public Products lastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public Products lastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Set<OrderDet> getOrderDets() {
        return orderDets;
    }

    public Products orderDets(Set<OrderDet> orderDets) {
        this.orderDets = orderDets;
        return this;
    }

    public Products addOrderDet(OrderDet orderDet) {
        this.orderDets.add(orderDet);
        orderDet.setProduct(this);
        return this;
    }

    public Products removeOrderDet(OrderDet orderDet) {
        this.orderDets.remove(orderDet);
        orderDet.setProduct(null);
        return this;
    }

    public void setOrderDets(Set<OrderDet> orderDets) {
        this.orderDets = orderDets;
    }

    public Suppliers getSupplier() {
        return supplier;
    }

    public Products supplier(Suppliers suppliers) {
        this.supplier = suppliers;
        return this;
    }

    public void setSupplier(Suppliers suppliers) {
        this.supplier = suppliers;
    }

    public Set<Customers> getCustomers() {
        return customers;
    }

    public Products customers(Set<Customers> customers) {
        this.customers = customers;
        return this;
    }

    public Products addCustomers(Customers customers) {
        this.customers.add(customers);
        customers.getProducts().add(this);
        return this;
    }

    public Products removeCustomers(Customers customers) {
        this.customers.remove(customers);
        customers.getProducts().remove(this);
        return this;
    }

    public void setCustomers(Set<Customers> customers) {
        this.customers = customers;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Products products = (Products) o;
        if (products.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), products.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Products{" +
            "id=" + getId() +
            ", productName='" + getProductName() + "'" +
            ", productDescription='" + getProductDescription() + "'" +
            ", productPrice=" + getProductPrice() +
            ", productStartDate='" + getProductStartDate() + "'" +
            ", productEndDate='" + getProductEndDate() + "'" +
            ", productType='" + getProductType() + "'" +
            ", productImg='" + getProductImg() + "'" +
            ", productImgContentType='" + getProductImgContentType() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
