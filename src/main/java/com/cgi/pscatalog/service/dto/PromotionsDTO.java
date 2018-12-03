package com.cgi.pscatalog.service.dto;

import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Promotions entity.
 */
public class PromotionsDTO implements Serializable {

    private Long id;

    @NotNull
    private Long newProductPrice;

    @NotNull
    private Instant promotionStartDate;

    private Instant promotionExpiryDate;

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

    private Long productsId;

    private String productsProductName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNewProductPrice() {
        return newProductPrice;
    }

    public void setNewProductPrice(Long newProductPrice) {
        this.newProductPrice = newProductPrice;
    }

    public Instant getPromotionStartDate() {
        return promotionStartDate;
    }

    public void setPromotionStartDate(Instant promotionStartDate) {
        this.promotionStartDate = promotionStartDate;
    }

    public Instant getPromotionExpiryDate() {
        return promotionExpiryDate;
    }

    public void setPromotionExpiryDate(Instant promotionExpiryDate) {
        this.promotionExpiryDate = promotionExpiryDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Long getProductsId() {
        return productsId;
    }

    public void setProductsId(Long productsId) {
        this.productsId = productsId;
    }

    public String getProductsProductName() {
        return productsProductName;
    }

    public void setProductsProductName(String productsProductName) {
        this.productsProductName = productsProductName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PromotionsDTO promotionsDTO = (PromotionsDTO) o;
        if (promotionsDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), promotionsDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PromotionsDTO{" +
            "id=" + getId() +
            ", newProductPrice=" + getNewProductPrice() +
            ", promotionStartDate='" + getPromotionStartDate() + "'" +
            ", promotionExpiryDate='" + getPromotionExpiryDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", products=" + getProductsId() +
            ", products='" + getProductsProductName() + "'" +
            "}";
    }
}
