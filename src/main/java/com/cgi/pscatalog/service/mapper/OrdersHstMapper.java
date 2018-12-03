package com.cgi.pscatalog.service.mapper;

import com.cgi.pscatalog.domain.*;
import com.cgi.pscatalog.service.dto.OrdersHstDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity OrdersHst and its DTO OrdersHstDTO.
 */
@Mapper(componentModel = "spring", uses = {OrdersMapper.class})
public interface OrdersHstMapper extends EntityMapper<OrdersHstDTO, OrdersHst> {

    @Mapping(source = "orders.id", target = "ordersId")
    @Mapping(source = "orders.orderReference", target = "ordersOrderReference")
    OrdersHstDTO toDto(OrdersHst ordersHst);

    @Mapping(source = "ordersId", target = "orders")
    OrdersHst toEntity(OrdersHstDTO ordersHstDTO);

    default OrdersHst fromId(Long id) {
        if (id == null) {
            return null;
        }
        OrdersHst ordersHst = new OrdersHst();
        ordersHst.setId(id);
        return ordersHst;
    }
}
