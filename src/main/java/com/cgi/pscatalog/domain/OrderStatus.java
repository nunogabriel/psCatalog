package com.cgi.pscatalog.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A OrderStatus.
 */
@Entity
@Table(name = "order_status")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "orderstatus")
public class OrderStatus implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "order_status_description", nullable = false)
    private String orderStatusDescription;

    @OneToMany(mappedBy = "orderStatus")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Orders> orders = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderStatusDescription() {
        return orderStatusDescription;
    }

    public OrderStatus orderStatusDescription(String orderStatusDescription) {
        this.orderStatusDescription = orderStatusDescription;
        return this;
    }

    public void setOrderStatusDescription(String orderStatusDescription) {
        this.orderStatusDescription = orderStatusDescription;
    }

    public Set<Orders> getOrders() {
        return orders;
    }

    public OrderStatus orders(Set<Orders> orders) {
        this.orders = orders;
        return this;
    }

    public OrderStatus addOrders(Orders orders) {
        this.orders.add(orders);
        orders.setOrderStatus(this);
        return this;
    }

    public OrderStatus removeOrders(Orders orders) {
        this.orders.remove(orders);
        orders.setOrderStatus(null);
        return this;
    }

    public void setOrders(Set<Orders> orders) {
        this.orders = orders;
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
        OrderStatus orderStatus = (OrderStatus) o;
        if (orderStatus.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), orderStatus.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "OrderStatus{" +
            "id=" + getId() +
            ", orderStatusDescription='" + getOrderStatusDescription() + "'" +
            "}";
    }
}
