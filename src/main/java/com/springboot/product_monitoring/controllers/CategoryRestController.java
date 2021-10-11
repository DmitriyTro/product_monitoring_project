package com.springboot.product_monitoring.controllers;

import com.springboot.product_monitoring.dto.CategoryDTO;
import com.springboot.product_monitoring.dto.payload.response.MessageResponse;
import com.springboot.product_monitoring.entities.Category;
import com.springboot.product_monitoring.exceptions.category.CategoryCustomExceptionHandler;
import com.springboot.product_monitoring.exceptions.product.ProductCustomExceptionHandler;
import com.springboot.product_monitoring.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@CategoryCustomExceptionHandler
@ProductCustomExceptionHandler
@RestController
@RequestMapping("/api/auth")
public class CategoryRestController {

	private final CategoryService categoryService;

	@Autowired
	public CategoryRestController(CategoryService categoryService) {
		this.categoryService = categoryService;
	}

	@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
	@GetMapping(value = "/categories/{id}")
	public ResponseEntity<CategoryDTO> findCategoryById(@PathVariable(name = "id") int id) {
		return new ResponseEntity<>(categoryService.findCategoryById(id), HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
	@GetMapping(value = "/categories/name/{categoryName}")
	public ResponseEntity<CategoryDTO> findByCategoryName(@PathVariable(name = "categoryName") String categoryName) {
		return new ResponseEntity<>(categoryService.findByCategoryName(categoryName), HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
	@GetMapping(value = "/categories/list")
	public Page<CategoryDTO> findAllCategories(
			@PageableDefault(sort = "id", direction = Sort.Direction.ASC, size = 10) Pageable pageable) {
		return categoryService.findAllCategories(pageable);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping(value = "/categories/delete/{id}")
	public ResponseEntity deleteById(@PathVariable(name = "id") int id) {
		categoryService.deleteById(id);
		return ResponseEntity.ok(new MessageResponse("Category deleted successfully!"));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping(value = "/categories/save")
	public ResponseEntity<CategoryDTO> saveCategory(@Valid @RequestBody Category category) {
		return new ResponseEntity<>(categoryService.saveCategory(category), HttpStatus.CREATED);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping(value = "/categories/{categoryId}/products/{productId}")
	public ResponseEntity<CategoryDTO> addProductToCategory(
			@PathVariable int categoryId, @PathVariable int productId) {
		return new ResponseEntity<>(categoryService.addProductToCategory(categoryId, productId), HttpStatus.OK);
	}
}
