package com.cgi.pscatalog.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * Products and Services Catalog jh file
 */
@ApiModel(description = "Products and Services Catalog jh file")
@Entity
@Table(name = "suppliers")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "suppliers")
public class Suppliers implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "supplier_name", nullable = false)
    private String supplierName;

    @NotNull
    @Column(name = "supplier_nif", nullable = false)
    private String supplierNif;

    @NotNull
    @Column(name = "supplier_email", nullable = false)
    private String supplierEmail;

    @NotNull
    @Column(name = "supplier_phone", nullable = false)
    private String supplierPhone;

    @NotNull
    @Column(name = "supplier_begin_date", nullable = false)
    private Instant supplierBeginDate;

    @Column(name = "supplier_end_date")
    private Instant supplierEndDate;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_date")
    private Instant createdDate;

    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

    @OneToMany(mappedBy = "supplier")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Addresses> addresses = new HashSet<>();
    @OneToMany(mappedBy = "supplier")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Products> products = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public Suppliers supplierName(String supplierName) {
        this.supplierName = supplierName;
        return this;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getSupplierNif() {
        return supplierNif;
    }

    public Suppliers supplierNif(String supplierNif) {
        this.supplierNif = supplierNif;
        return this;
    }

    public void setSupplierNif(String supplierNif) {
        this.supplierNif = supplierNif;
    }

    public String getSupplierEmail() {
        return supplierEmail;
    }

    public Suppliers supplierEmail(String supplierEmail) {
        this.supplierEmail = supplierEmail;
        return this;
    }

    public void setSupplierEmail(String supplierEmail) {
        this.supplierEmail = supplierEmail;
    }

    public String getSupplierPhone() {
        return supplierPhone;
    }

    public Suppliers supplierPhone(String supplierPhone) {
        this.supplierPhone = supplierPhone;
        return this;
    }

    public void setSupplierPhone(String supplierPhone) {
        this.supplierPhone = supplierPhone;
    }

    public Instant getSupplierBeginDate() {
        return supplierBeginDate;
    }

    public Suppliers supplierBeginDate(Instant supplierBeginDate) {
        this.supplierBeginDate = supplierBeginDate;
        return this;
    }

    public void setSupplierBeginDate(Instant supplierBeginDate) {
        this.supplierBeginDate = supplierBeginDate;
    }

    public Instant getSupplierEndDate() {
        return supplierEndDate;
    }

    public Suppliers supplierEndDate(Instant supplierEndDate) {
        this.supplierEndDate = supplierEndDate;
        return this;
    }

    public void setSupplierEndDate(Instant supplierEndDate) {
        this.supplierEndDate = supplierEndDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public Suppliers createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public Suppliers createdDate(Instant createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public Suppliers lastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public Suppliers lastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Set<Addresses> getAddresses() {
        return addresses;
    }

    public Suppliers addresses(Set<Addresses> addresses) {
        this.addresses = addresses;
        return this;
    }

    public Suppliers addAddresses(Addresses addresses) {
        this.addresses.add(addresses);
        addresses.setSupplier(this);
        return this;
    }

    public Suppliers removeAddresses(Addresses addresses) {
        this.addresses.remove(addresses);
        addresses.setSupplier(null);
        return this;
    }

    public void setAddresses(Set<Addresses> addresses) {
        this.addresses = addresses;
    }

    public Set<Products> getProducts() {
        return products;
    }

    public Suppliers products(Set<Products> products) {
        this.products = products;
        return this;
    }

    public Suppliers addProducts(Products products) {
        this.products.add(products);
        products.setSupplier(this);
        return this;
    }

    public Suppliers removeProducts(Products products) {
        this.products.remove(products);
        products.setSupplier(null);
        return this;
    }

    public void setProducts(Set<Products> products) {
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
        Suppliers suppliers = (Suppliers) o;
        if (suppliers.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), suppliers.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Suppliers{" +
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
