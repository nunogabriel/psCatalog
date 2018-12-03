package com.cgi.pscatalog.service.mapper;

import com.cgi.pscatalog.domain.*;
import com.cgi.pscatalog.service.dto.ProductsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Products and its DTO ProductsDTO.
 */
@Mapper(componentModel = "spring", uses = {SuppliersMapper.class})
public interface ProductsMapper extends EntityMapper<ProductsDTO, Products> {

    @Mapping(source = "supplier.id", target = "supplierId")
    @Mapping(source = "supplier.supplierName", target = "supplierSupplierName")
    ProductsDTO toDto(Products products);

    @Mapping(target = "orderDets", ignore = true)
    @Mapping(source = "supplierId", target = "supplier")
    @Mapping(target = "customers", ignore = true)
    Products toEntity(ProductsDTO productsDTO);

    default Products fromId(Long id) {
        if (id == null) {
            return null;
        }
        Products products = new Products();
        products.setId(id);
        return products;
    }
}
