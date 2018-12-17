package com.cgi.pscatalog.service.mapper;

import com.cgi.pscatalog.domain.*;
import com.cgi.pscatalog.service.dto.OrdersDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Orders and its DTO OrdersDTO.
 */
@Mapper(componentModel = "spring", uses = {OrderStatusMapper.class, AddressesMapper.class, CustomersMapper.class})
public interface OrdersMapper extends EntityMapper<OrdersDTO, Orders> {

    @Mapping(source = "orderStatus.id", target = "orderStatusId")
    @Mapping(source = "orderStatus.orderStatusDescription", target = "orderStatusOrderStatusDescription")
    @Mapping(source = "address.id", target = "addressId")
    @Mapping(source = "address.addressReference", target = "addressAddressReference")
    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "customer.customerName", target = "customerCustomerName")
    @Mapping(source = "deliveryAddress.id", target = "deliveryAddressId")
    @Mapping(source = "deliveryAddress.addressReference", target = "deliveryAddressAddressReference")
    OrdersDTO toDto(Orders orders);

    @Mapping(target = "orderDets", ignore = true)
    @Mapping(source = "orderStatusId", target = "orderStatus")
    @Mapping(source = "addressId", target = "address")
    @Mapping(source = "customerId", target = "customer")
    @Mapping(source = "deliveryAddressId", target = "deliveryAddress")
    Orders toEntity(OrdersDTO ordersDTO);

    default Orders fromId(Long id) {
        if (id == null) {
            return null;
        }
        Orders orders = new Orders();
        orders.setId(id);
        return orders;
    }
}
