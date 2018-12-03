package com.cgi.pscatalog.service.dto;

import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import com.cgi.pscatalog.domain.enumeration.GenderEnum;

/**
 * A DTO for the Customers entity.
 */
public class CustomersDTO implements Serializable {

    private Long id;

    @NotNull
    private String customerName;

    @NotNull
    private String customerEmail;

    private String customerNif;

    private String customerPhone;

    private GenderEnum customerGender;

    @NotNull
    private Instant customerBeginDate;

    private Instant customerEndDate;

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

    private Set<ProductsDTO> products = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerNif() {
        return customerNif;
    }

    public void setCustomerNif(String customerNif) {
        this.customerNif = customerNif;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public GenderEnum getCustomerGender() {
        return customerGender;
    }

    public void setCustomerGender(GenderEnum customerGender) {
        this.customerGender = customerGender;
    }

    public Instant getCustomerBeginDate() {
        return customerBeginDate;
    }

    public void setCustomerBeginDate(Instant customerBeginDate) {
        this.customerBeginDate = customerBeginDate;
    }

    public Instant getCustomerEndDate() {
        return customerEndDate;
    }

    public void setCustomerEndDate(Instant customerEndDate) {
        this.customerEndDate = customerEndDate;
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

    public Set<ProductsDTO> getProducts() {
        return products;
    }

    public void setProducts(Set<ProductsDTO> products) {
        this.products = products;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CustomersDTO customersDTO = (CustomersDTO) o;
        if (customersDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), customersDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CustomersDTO{" +
            "id=" + getId() +
            ", customerName='" + getCustomerName() + "'" +
            ", customerEmail='" + getCustomerEmail() + "'" +
            ", customerNif='" + getCustomerNif() + "'" +
            ", customerPhone='" + getCustomerPhone() + "'" +
            ", customerGender='" + getCustomerGender() + "'" +
            ", customerBeginDate='" + getCustomerBeginDate() + "'" +
            ", customerEndDate='" + getCustomerEndDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
