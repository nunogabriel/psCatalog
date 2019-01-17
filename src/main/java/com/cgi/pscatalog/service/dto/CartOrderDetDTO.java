package com.cgi.pscatalog.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

import javax.persistence.Lob;
import javax.validation.constraints.NotNull;

import com.cgi.pscatalog.domain.enumeration.ProductTypeEnum;

/**
 * A DTO for the OrderDet entity.
 */
public class CartOrderDetDTO implements Serializable {

    /**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private Long id;

    @NotNull
    private Integer orderQuantity;

    @NotNull
    private BigDecimal unitPrice;

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

    private Long orderId;

    private String orderOrderReference;

    private Long productId;

    private String productProductName;

    private String productDescription;

    @NotNull
    private ProductTypeEnum productType;

    @Lob
    private byte[] productImg;
    private String productImgContentType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(Integer orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
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

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long ordersId) {
        this.orderId = ordersId;
    }

    public String getOrderOrderReference() {
        return orderOrderReference;
    }

    public void setOrderOrderReference(String ordersOrderReference) {
        this.orderOrderReference = ordersOrderReference;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productsId) {
        this.productId = productsId;
    }

    public String getProductProductName() {
        return productProductName;
    }

    public void setProductProductName(String productsProductName) {
        this.productProductName = productsProductName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public ProductTypeEnum getProductType() {
        return productType;
    }

    public void setProductType(ProductTypeEnum productType) {
        this.productType = productType;
    }

    public byte[] getProductImg() {
        return productImg;
    }

    public void setProductImg(byte[] productImg) {
        this.productImg = productImg;
    }

    public String getProductImgContentType() {
        return productImgContentType;
    }

    public void setProductImgContentType(String productImgContentType) {
        this.productImgContentType = productImgContentType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CartOrderDetDTO cartOrderDetDTO = (CartOrderDetDTO) o;

        if (cartOrderDetDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), cartOrderDetDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CartOrderDetDTO{" +
            "id=" + getId() +
            ", orderQuantity=" + getOrderQuantity() +
            ", unitPrice=" + getUnitPrice() +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", orderId=" + getOrderId() +
            ", orderReference='" + getOrderOrderReference() + "'" +
            ", productId=" + getProductId() +
            ", productName='" + getProductProductName() + "'" +
            ", productDescription='" + getProductDescription() + "'" +
            ", productType='" + getProductType() + "'" +
            ", productImg='" + getProductImg() + "'" +
            "}";
    }
}
