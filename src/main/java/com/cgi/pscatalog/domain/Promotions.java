package com.cgi.pscatalog.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A Promotions.
 */
@Entity
@Table(name = "promotions")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "promotions")
public class Promotions implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "new_product_price", nullable = false)
    private Long newProductPrice;

    @NotNull
    @Column(name = "promotion_start_date", nullable = false)
    private Instant promotionStartDate;

    @Column(name = "promotion_expiry_date")
    private Instant promotionExpiryDate;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_date")
    private Instant createdDate;

    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

    @OneToOne    @JoinColumn(unique = true)
    private Products products;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNewProductPrice() {
        return newProductPrice;
    }

    public Promotions newProductPrice(Long newProductPrice) {
        this.newProductPrice = newProductPrice;
        return this;
    }

    public void setNewProductPrice(Long newProductPrice) {
        this.newProductPrice = newProductPrice;
    }

    public Instant getPromotionStartDate() {
        return promotionStartDate;
    }

    public Promotions promotionStartDate(Instant promotionStartDate) {
        this.promotionStartDate = promotionStartDate;
        return this;
    }

    public void setPromotionStartDate(Instant promotionStartDate) {
        this.promotionStartDate = promotionStartDate;
    }

    public Instant getPromotionExpiryDate() {
        return promotionExpiryDate;
    }

    public Promotions promotionExpiryDate(Instant promotionExpiryDate) {
        this.promotionExpiryDate = promotionExpiryDate;
        return this;
    }

    public void setPromotionExpiryDate(Instant promotionExpiryDate) {
        this.promotionExpiryDate = promotionExpiryDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public Promotions createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public Promotions createdDate(Instant createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public Promotions lastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public Promotions lastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Products getProducts() {
        return products;
    }

    public Promotions products(Products products) {
        this.products = products;
        return this;
    }

    public void setProducts(Products products) {
        this.products = products;
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
        Promotions promotions = (Promotions) o;
        if (promotions.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), promotions.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Promotions{" +
            "id=" + getId() +
            ", newProductPrice=" + getNewProductPrice() +
            ", promotionStartDate='" + getPromotionStartDate() + "'" +
            ", promotionExpiryDate='" + getPromotionExpiryDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
