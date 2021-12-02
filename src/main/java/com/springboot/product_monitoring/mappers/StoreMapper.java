package com.springboot.product_monitoring.mappers;

import com.springboot.product_monitoring.dto.StoreDTO;
import com.springboot.product_monitoring.entities.Store;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface StoreMapper {

	StoreDTO toStoreDTO(Store store);

	List<StoreDTO> toStoresDTOs(List<Store> stores);
}
