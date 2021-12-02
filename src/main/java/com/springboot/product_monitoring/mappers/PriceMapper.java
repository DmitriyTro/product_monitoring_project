package com.springboot.product_monitoring.mappers;

import com.springboot.product_monitoring.dto.PriceDTO;
import com.springboot.product_monitoring.entities.Price;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface PriceMapper {

	PriceDTO toPriceDTO(Price price);

	List<PriceDTO> toPricesDTOs(List<Price> prices);
}
