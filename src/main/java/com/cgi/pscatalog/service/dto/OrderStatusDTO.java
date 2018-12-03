package com.cgi.pscatalog.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the OrderStatus entity.
 */
public class OrderStatusDTO implements Serializable {

    private Long id;

    @NotNull
    private String orderStatusDescription;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderStatusDescription() {
        return orderStatusDescription;
    }

    public void setOrderStatusDescription(String orderStatusDescription) {
        this.orderStatusDescription = orderStatusDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        OrderStatusDTO orderStatusDTO = (OrderStatusDTO) o;
        if (orderStatusDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), orderStatusDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "OrderStatusDTO{" +
            "id=" + getId() +
            ", orderStatusDescription='" + getOrderStatusDescription() + "'" +
            "}";
    }
}
