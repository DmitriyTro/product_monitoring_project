package com.springboot.product_monitoring.mappers;

import com.springboot.product_monitoring.dto.payload.response.PriceDynamicsResponse;
import com.springboot.product_monitoring.entities.Price;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface PriceDynamicsResponseMapper {

	PriceDynamicsResponse toPriceDynamicResponse(Price price);

	List<PriceDynamicsResponse> toPricesDynamicResponse(List<Price> prices);
}
