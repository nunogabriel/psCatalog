package com.cgi.pscatalog.service.dto;

import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Suppliers entity.
 */
public class SuppliersDTO implements Serializable {

    private Long id;

    @NotNull
    private String supplierName;

    @NotNull
    private String supplierNif;

    @NotNull
    private String supplierEmail;

    @NotNull
    private String supplierPhone;

    @NotNull
    private Instant supplierBeginDate;

    private Instant supplierEndDate;

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getSupplierNif() {
        return supplierNif;
    }

    public void setSupplierNif(String supplierNif) {
        this.supplierNif = supplierNif;
    }

    public String getSupplierEmail() {
        return supplierEmail;
    }

    public void setSupplierEmail(String supplierEmail) {
        this.supplierEmail = supplierEmail;
    }

    public String getSupplierPhone() {
        return supplierPhone;
    }

    public void setSupplierPhone(String supplierPhone) {
        this.supplierPhone = supplierPhone;
    }

    public Instant getSupplierBeginDate() {
        return supplierBeginDate;
    }

    public void setSupplierBeginDate(Instant supplierBeginDate) {
        this.supplierBeginDate = supplierBeginDate;
    }

    public Instant getSupplierEndDate() {
        return supplierEndDate;
    }

    public void setSupplierEndDate(Instant supplierEndDate) {
        this.supplierEndDate = supplierEndDate;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SuppliersDTO suppliersDTO = (SuppliersDTO) o;
        if (suppliersDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), suppliersDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SuppliersDTO{" +
            "id=" + getId() +
            ", supplierName='" + getSupplierName() + "'" +
            ", supplierNif='" + getSupplierNif() + "'" +
            ", supplierEmail='" + getSupplierEmail() + "'" +
            ", supplierPhone='" + getSupplierPhone() + "'" +
            ", supplierBeginDate='" + getSupplierBeginDate() + "'" +
            ", supplierEndDate='" + getSupplierEndDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
