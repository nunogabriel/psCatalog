package com.cgi.pscatalog.service.mapper;

import com.cgi.pscatalog.domain.*;
import com.cgi.pscatalog.service.dto.CountriesDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Countries and its DTO CountriesDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CountriesMapper extends EntityMapper<CountriesDTO, Countries> {


    @Mapping(target = "addresses", ignore = true)
    Countries toEntity(CountriesDTO countriesDTO);

    default Countries fromId(Long id) {
        if (id == null) {
            return null;
        }
        Countries countries = new Countries();
        countries.setId(id);
        return countries;
    }
}
