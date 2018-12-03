package com.cgi.pscatalog.service.dto;

import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the OrdersHst entity.
 */
public class OrdersHstDTO implements Serializable {

    private Long id;

    @NotNull
    private String orderReference;

    @NotNull
    private Integer customerId;

    @NotNull
    private Integer orderStatusCode;

    @NotNull
    private Instant orderDate;

    @NotNull
    private Integer billingAddress;

    @NotNull
    private Integer deliveryAddress;

    private Instant deliveryDate;

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

    private Long ordersId;

    private String ordersOrderReference;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderReference() {
        return orderReference;
    }

    public void setOrderReference(String orderReference) {
        this.orderReference = orderReference;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getOrderStatusCode() {
        return orderStatusCode;
    }

    public void setOrderStatusCode(Integer orderStatusCode) {
        this.orderStatusCode = orderStatusCode;
    }

    public Instant getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Instant orderDate) {
        this.orderDate = orderDate;
    }

    public Integer getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(Integer billingAddress) {
        this.billingAddress = billingAddress;
    }

    public Integer getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(Integer deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public Instant getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Instant deliveryDate) {
        this.deliveryDate = deliveryDate;
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

    public Long getOrdersId() {
        return ordersId;
    }

    public void setOrdersId(Long ordersId) {
        this.ordersId = ordersId;
    }

    public String getOrdersOrderReference() {
        return ordersOrderReference;
    }

    public void setOrdersOrderReference(String ordersOrderReference) {
        this.ordersOrderReference = ordersOrderReference;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        OrdersHstDTO ordersHstDTO = (OrdersHstDTO) o;
        if (ordersHstDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), ordersHstDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "OrdersHstDTO{" +
            "id=" + getId() +
            ", orderReference='" + getOrderReference() + "'" +
            ", customerId=" + getCustomerId() +
            ", orderStatusCode=" + getOrderStatusCode() +
            ", orderDate='" + getOrderDate() + "'" +
            ", billingAddress=" + getBillingAddress() +
            ", deliveryAddress=" + getDeliveryAddress() +
            ", deliveryDate='" + getDeliveryDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", orders=" + getOrdersId() +
            ", orders='" + getOrdersOrderReference() + "'" +
            "}";
    }
}
