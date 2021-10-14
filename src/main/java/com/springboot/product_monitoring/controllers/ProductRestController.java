package com.springboot.product_monitoring.controllers;

import com.springboot.product_monitoring.dto.ProductDTO;
import com.springboot.product_monitoring.dto.payload.response.MessageResponse;
import com.springboot.product_monitoring.entities.Product;
import com.springboot.product_monitoring.exceptions.category.CategoryCustomExceptionHandler;
import com.springboot.product_monitoring.exceptions.product.ProductCustomExceptionHandler;
import com.springboot.product_monitoring.services.ProductService;
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
@ProductCustomExceptionHandler
@CategoryCustomExceptionHandler
@RestController
@RequestMapping("/api/auth")
public class ProductRestController {

	private final ProductService productService;

	@Autowired
	public ProductRestController(ProductService productService) {
		this.productService = productService;
	}

	@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
	@GetMapping(value = "/products/{id}")
	public ResponseEntity<ProductDTO> findProductById(@PathVariable(name = "id") int id) {
		return new ResponseEntity<>(productService.findProductById(id), HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
	@GetMapping(value = "/products/name/{productName}")
	public ResponseEntity<ProductDTO> findByProductName(@PathVariable(name = "productName") String productName) {
		return new ResponseEntity<>(productService.findByProductName(productName), HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
	@GetMapping(value = "/products/list")
	public Page<ProductDTO> findAllProducts(
			@PageableDefault(sort = "id", direction = Sort.Direction.ASC, size = 10) Pageable pageable) {
		return productService.findAllProducts(pageable);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping(value = "/products/delete/{id}")
	public ResponseEntity<MessageResponse> deleteById(@PathVariable(name = "id") int id) {
		productService.deleteById(id);
		return ResponseEntity.ok(new MessageResponse("Product deleted successfully!"));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping(value = "/products/save")
	public ResponseEntity<ProductDTO> saveProductWithCategory(@Valid @RequestBody Product product) {
		return new ResponseEntity<>(productService.saveProductWithCategory(product), HttpStatus.OK);
	}
}