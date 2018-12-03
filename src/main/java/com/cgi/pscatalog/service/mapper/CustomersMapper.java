package com.cgi.pscatalog.service.mapper;

import com.cgi.pscatalog.domain.*;
import com.cgi.pscatalog.service.dto.CustomersDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Customers and its DTO CustomersDTO.
 */
@Mapper(componentModel = "spring", uses = {ProductsMapper.class})
public interface CustomersMapper extends EntityMapper<CustomersDTO, Customers> {


    @Mapping(target = "orders", ignore = true)
    @Mapping(target = "addresses", ignore = true)
    Customers toEntity(CustomersDTO customersDTO);

    default Customers fromId(Long id) {
        if (id == null) {
            return null;
        }
        Customers customers = new Customers();
        customers.setId(id);
        return customers;
    }
}
