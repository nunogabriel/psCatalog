package com.cgi.pscatalog.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.cgi.pscatalog.domain.Products;
import com.cgi.pscatalog.service.dto.ProductsDTO;

/**
 * Mapper for the entity Products and its DTO ProductsDTO.
 */
@Mapper(componentModel = "spring", uses = {SuppliersMapper.class})
public interface ProductsMapper extends EntityMapper<ProductsDTO, Products> {

    @Override
	@Mapping(source = "supplier.id", target = "supplierId")
    @Mapping(source = "supplier.supplierName", target = "supplierSupplierName")
    ProductsDTO toDto(Products products);

    @Override
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
