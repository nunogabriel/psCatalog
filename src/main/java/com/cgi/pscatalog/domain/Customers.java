package com.cgi.pscatalog.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

import com.cgi.pscatalog.domain.enumeration.GenderEnum;

/**
 * A Customers.
 */
@Entity
@Table(name = "customers")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "customers")
public class Customers implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "customer_name", nullable = false)
    private String customerName;

    @NotNull
    @Column(name = "customer_email", nullable = false)
    private String customerEmail;

    @Column(name = "customer_nif")
    private String customerNif;

    @Column(name = "customer_phone")
    private String customerPhone;

    @Enumerated(EnumType.STRING)
    @Column(name = "customer_gender")
    private GenderEnum customerGender;

    @NotNull
    @Column(name = "customer_begin_date", nullable = false)
    private Instant customerBeginDate;

    @Column(name = "customer_end_date")
    private Instant customerEndDate;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_date")
    private Instant createdDate;

    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

    @OneToMany(mappedBy = "customer")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Orders> orders = new HashSet<>();
    @OneToMany(mappedBy = "customer")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Addresses> addresses = new HashSet<>();
    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "customers_products",
               joinColumns = @JoinColumn(name = "customers_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "products_id", referencedColumnName = "id"))
    private Set<Products> products = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public Customers customerName(String customerName) {
        this.customerName = customerName;
        return this;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public Customers customerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
        return this;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerNif() {
        return customerNif;
    }

    public Customers customerNif(String customerNif) {
        this.customerNif = customerNif;
        return this;
    }

    public void setCustomerNif(String customerNif) {
        this.customerNif = customerNif;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public Customers customerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
        return this;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public GenderEnum getCustomerGender() {
        return customerGender;
    }

    public Customers customerGender(GenderEnum customerGender) {
        this.customerGender = customerGender;
        return this;
    }

    public void setCustomerGender(GenderEnum customerGender) {
        this.customerGender = customerGender;
    }

    public Instant getCustomerBeginDate() {
        return customerBeginDate;
    }

    public Customers customerBeginDate(Instant customerBeginDate) {
        this.customerBeginDate = customerBeginDate;
        return this;
    }

    public void setCustomerBeginDate(Instant customerBeginDate) {
        this.customerBeginDate = customerBeginDate;
    }

    public Instant getCustomerEndDate() {
        return customerEndDate;
    }

    public Customers customerEndDate(Instant customerEndDate) {
        this.customerEndDate = customerEndDate;
        return this;
    }

    public void setCustomerEndDate(Instant customerEndDate) {
        this.customerEndDate = customerEndDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public Customers createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public Customers createdDate(Instant createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public Customers lastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public Customers lastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Set<Orders> getOrders() {
        return orders;
    }

    public Customers orders(Set<Orders> orders) {
        this.orders = orders;
        return this;
    }

    public Customers addOrders(Orders orders) {
        this.orders.add(orders);
        orders.setCustomer(this);
        return this;
    }

    public Customers removeOrders(Orders orders) {
        this.orders.remove(orders);
        orders.setCustomer(null);
        return this;
    }

    public void setOrders(Set<Orders> orders) {
        this.orders = orders;
    }

    public Set<Addresses> getAddresses() {
        return addresses;
    }

    public Customers addresses(Set<Addresses> addresses) {
        this.addresses = addresses;
        return this;
    }

    public Customers addAddresses(Addresses addresses) {
        this.addresses.add(addresses);
        addresses.setCustomer(this);
        return this;
    }

    public Customers removeAddresses(Addresses addresses) {
        this.addresses.remove(addresses);
        addresses.setCustomer(null);
        return this;
    }

    public void setAddresses(Set<Addresses> addresses) {
        this.addresses = addresses;
    }

    public Set<Products> getProducts() {
        return products;
    }

    public Customers products(Set<Products> products) {
        this.products = products;
        return this;
    }

    public Customers addProducts(Products products) {
        this.products.add(products);
        products.getCustomers().add(this);
        return this;
    }

    public Customers removeProducts(Products products) {
        this.products.remove(products);
        products.getCustomers().remove(this);
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
        Customers customers = (Customers) o;
        if (customers.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), customers.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Customers{" +
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
