package com.cgi.pscatalog.service.mapper;

import com.cgi.pscatalog.domain.*;
import com.cgi.pscatalog.service.dto.OrderDetDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity OrderDet and its DTO OrderDetDTO.
 */
@Mapper(componentModel = "spring", uses = {OrdersMapper.class, ProductsMapper.class})
public interface OrderDetMapper extends EntityMapper<OrderDetDTO, OrderDet> {

    @Mapping(source = "order.id", target = "orderId")
    @Mapping(source = "order.orderReference", target = "orderOrderReference")
    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.productName", target = "productProductName")
    OrderDetDTO toDto(OrderDet orderDet);

    @Mapping(source = "orderId", target = "order")
    @Mapping(source = "productId", target = "product")
    OrderDet toEntity(OrderDetDTO orderDetDTO);

    default OrderDet fromId(Long id) {
        if (id == null) {
            return null;
        }
        OrderDet orderDet = new OrderDet();
        orderDet.setId(id);
        return orderDet;
    }
}
