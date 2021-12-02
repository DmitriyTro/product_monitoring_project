package com.springboot.product_monitoring.mappers;

import com.springboot.product_monitoring.dto.CategoryDTO;
import com.springboot.product_monitoring.entities.Category;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {

	CategoryDTO toCategoryDTO(Category category);

	List<CategoryDTO> toCategoriesDTOs(List<Category> categories);
}
