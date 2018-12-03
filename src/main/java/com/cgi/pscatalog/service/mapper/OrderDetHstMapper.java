package com.cgi.pscatalog.service.mapper;

import com.cgi.pscatalog.domain.*;
import com.cgi.pscatalog.service.dto.OrderDetHstDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity OrderDetHst and its DTO OrderDetHstDTO.
 */
@Mapper(componentModel = "spring", uses = {OrderDetMapper.class})
public interface OrderDetHstMapper extends EntityMapper<OrderDetHstDTO, OrderDetHst> {

    @Mapping(source = "orderDet.id", target = "orderDetId")
    OrderDetHstDTO toDto(OrderDetHst orderDetHst);

    @Mapping(source = "orderDetId", target = "orderDet")
    OrderDetHst toEntity(OrderDetHstDTO orderDetHstDTO);

    default OrderDetHst fromId(Long id) {
        if (id == null) {
            return null;
        }
        OrderDetHst orderDetHst = new OrderDetHst();
        orderDetHst.setId(id);
        return orderDetHst;
    }
}
