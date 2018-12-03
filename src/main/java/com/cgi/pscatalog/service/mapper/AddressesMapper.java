package com.cgi.pscatalog.service.mapper;

import com.cgi.pscatalog.domain.*;
import com.cgi.pscatalog.service.dto.AddressesDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Addresses and its DTO AddressesDTO.
 */
@Mapper(componentModel = "spring", uses = {CustomersMapper.class, SuppliersMapper.class, CountriesMapper.class})
public interface AddressesMapper extends EntityMapper<AddressesDTO, Addresses> {

    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "customer.customerName", target = "customerCustomerName")
    @Mapping(source = "supplier.id", target = "supplierId")
    @Mapping(source = "supplier.supplierName", target = "supplierSupplierName")
    @Mapping(source = "country.id", target = "countryId")
    @Mapping(source = "country.countryName", target = "countryCountryName")
    AddressesDTO toDto(Addresses addresses);

    @Mapping(target = "orders", ignore = true)
    @Mapping(source = "customerId", target = "customer")
    @Mapping(source = "supplierId", target = "supplier")
    @Mapping(source = "countryId", target = "country")
    Addresses toEntity(AddressesDTO addressesDTO);

    default Addresses fromId(Long id) {
        if (id == null) {
            return null;
        }
        Addresses addresses = new Addresses();
        addresses.setId(id);
        return addresses;
    }
}
