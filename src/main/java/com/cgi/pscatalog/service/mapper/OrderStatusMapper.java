package com.cgi.pscatalog.service.mapper;

import com.cgi.pscatalog.domain.*;
import com.cgi.pscatalog.service.dto.OrderStatusDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity OrderStatus and its DTO OrderStatusDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface OrderStatusMapper extends EntityMapper<OrderStatusDTO, OrderStatus> {


    @Mapping(target = "orders", ignore = true)
    OrderStatus toEntity(OrderStatusDTO orderStatusDTO);

    default OrderStatus fromId(Long id) {
        if (id == null) {
            return null;
        }
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setId(id);
        return orderStatus;
    }
}
