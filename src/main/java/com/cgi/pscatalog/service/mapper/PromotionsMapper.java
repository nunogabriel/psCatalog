package com.cgi.pscatalog.service.mapper;

import com.cgi.pscatalog.domain.*;
import com.cgi.pscatalog.service.dto.PromotionsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Promotions and its DTO PromotionsDTO.
 */
@Mapper(componentModel = "spring", uses = {ProductsMapper.class})
public interface PromotionsMapper extends EntityMapper<PromotionsDTO, Promotions> {

    @Mapping(source = "products.id", target = "productsId")
    @Mapping(source = "products.productName", target = "productsProductName")
    PromotionsDTO toDto(Promotions promotions);

    @Mapping(source = "productsId", target = "products")
    Promotions toEntity(PromotionsDTO promotionsDTO);

    default Promotions fromId(Long id) {
        if (id == null) {
            return null;
        }
        Promotions promotions = new Promotions();
        promotions.setId(id);
        return promotions;
    }
}
