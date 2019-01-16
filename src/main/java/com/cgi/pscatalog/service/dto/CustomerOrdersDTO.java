package com.cgi.pscatalog.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

import javax.validation.constraints.NotNull;

/**
 * A DTO for the Orders entity.
 */
public class CustomerOrdersDTO implements Serializable {

    private Long id;

    @NotNull
    private String orderReference;

    @NotNull
    private Instant orderDate;

    private Instant deliveryDate;

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

    private Long orderStatusId;

    private String orderStatusOrderStatusDescription;

    private Long addressId;

    private String addressAddressReference;

    private Long customerId;

    private String customerCustomerName;

    private Long deliveryAddressId;

    private String deliveryAddressAddressReference;

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

    public Instant getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Instant orderDate) {
        this.orderDate = orderDate;
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

    public Long getOrderStatusId() {
        return orderStatusId;
    }

    public void setOrderStatusId(Long orderStatusId) {
        this.orderStatusId = orderStatusId;
    }

    public String getOrderStatusOrderStatusDescription() {
        return orderStatusOrderStatusDescription;
    }

    public void setOrderStatusOrderStatusDescription(String orderStatusOrderStatusDescription) {
        this.orderStatusOrderStatusDescription = orderStatusOrderStatusDescription;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressesId) {
        this.addressId = addressesId;
    }

    public String getAddressAddressReference() {
        return addressAddressReference;
    }

    public void setAddressAddressReference(String addressesAddressReference) {
        this.addressAddressReference = addressesAddressReference;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customersId) {
        this.customerId = customersId;
    }

    public String getCustomerCustomerName() {
        return customerCustomerName;
    }

    public void setCustomerCustomerName(String customersCustomerName) {
        this.customerCustomerName = customersCustomerName;
    }

    public Long getDeliveryAddressId() {
        return deliveryAddressId;
    }

    public void setDeliveryAddressId(Long addressesId) {
        this.deliveryAddressId = addressesId;
    }

    public String getDeliveryAddressAddressReference() {
        return deliveryAddressAddressReference;
    }

    public void setDeliveryAddressAddressReference(String addressesAddressReference) {
        this.deliveryAddressAddressReference = addressesAddressReference;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CustomerOrdersDTO ordersDTO = (CustomerOrdersDTO) o;
        if (ordersDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), ordersDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CustomerOrdersDTO{" +
            "id=" + getId() +
            ", orderReference='" + getOrderReference() + "'" +
            ", orderDate='" + getOrderDate() + "'" +
            ", deliveryDate='" + getDeliveryDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", orderStatus=" + getOrderStatusId() +
            ", orderStatus='" + getOrderStatusOrderStatusDescription() + "'" +
            ", address=" + getAddressId() +
            ", address='" + getAddressAddressReference() + "'" +
            ", customer=" + getCustomerId() +
            ", customer='" + getCustomerCustomerName() + "'" +
            ", deliveryAddress=" + getDeliveryAddressId() +
            ", deliveryAddress='" + getDeliveryAddressAddressReference() + "'" +
            "}";
    }
}
