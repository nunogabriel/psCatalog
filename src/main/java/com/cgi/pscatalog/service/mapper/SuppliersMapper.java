package com.cgi.pscatalog.service.mapper;

import com.cgi.pscatalog.domain.*;
import com.cgi.pscatalog.service.dto.SuppliersDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Suppliers and its DTO SuppliersDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SuppliersMapper extends EntityMapper<SuppliersDTO, Suppliers> {


    @Mapping(target = "addresses", ignore = true)
    @Mapping(target = "products", ignore = true)
    Suppliers toEntity(SuppliersDTO suppliersDTO);

    default Suppliers fromId(Long id) {
        if (id == null) {
            return null;
        }
        Suppliers suppliers = new Suppliers();
        suppliers.setId(id);
        return suppliers;
    }
}
