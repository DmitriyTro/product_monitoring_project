package com.springboot.product_monitoring.mappers;


import com.springboot.product_monitoring.dto.ProductDTO;
import com.springboot.product_monitoring.entities.Product;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface ProductMapper {

	ProductDTO toProductDTO(Product product);

	List<ProductDTO> toProductsDTOs(List<Product> products);
}
