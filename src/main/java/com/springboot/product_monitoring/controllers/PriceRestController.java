package com.springboot.product_monitoring.controllers;

import com.springboot.product_monitoring.dto.PriceDTO;
import com.springboot.product_monitoring.dto.payload.response.MessageResponse;
import com.springboot.product_monitoring.entities.Price;
import com.springboot.product_monitoring.exceptions.price.PriceCustomExceptionHandler;
import com.springboot.product_monitoring.exceptions.product.ProductCustomExceptionHandler;
import com.springboot.product_monitoring.exceptions.store.StoreCustomExceptionHandler;
import com.springboot.product_monitoring.services.PriceService;
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
@PriceCustomExceptionHandler
@ProductCustomExceptionHandler
@StoreCustomExceptionHandler
@RestController
@RequestMapping("/api/auth")
public class PriceRestController {

	private final PriceService priceService;

	@Autowired
	public PriceRestController(PriceService priceService) {
		this.priceService = priceService;
	}

	@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
	@GetMapping(value = "/prices/{id}")
	public ResponseEntity<PriceDTO> findPriceById(@PathVariable(name = "id") int id) {
		return new ResponseEntity<>(priceService.findPriceById(id), HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
	@GetMapping(value = "/prices/list")
	public Page<PriceDTO> findAllPrices(
			@PageableDefault(sort = "id", direction = Sort.Direction.ASC, size = 10) Pageable pageable) {
		return priceService.findAllPrices(pageable);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping(value = "/prices/delete/{id}")
	public ResponseEntity deleteById(@PathVariable(name = "id") int id) {
		priceService.deleteById(id);
		return ResponseEntity.ok(new MessageResponse("Price deleted successfully!"));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping(value = "/prices/save/product/{productId}/store/{storeId}")
	public ResponseEntity<PriceDTO> savePrice(@Valid @RequestBody Price price,
	                                          @PathVariable(name = "productId") int productId,
	                                          @PathVariable(name = "storeId") int storeId) {
		return new ResponseEntity<>(priceService.savePriceWithProductIdAndStoreId(price, productId, storeId), HttpStatus.CREATED);
	}
}