package com.springboot.product_monitoring.controllers;

import com.springboot.product_monitoring.dto.PriceDTO;
import com.springboot.product_monitoring.dto.payload.response.MessageResponse;
import com.springboot.product_monitoring.dto.payload.response.PriceDynamicsResponse;
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
			@PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
		return priceService.findAllPrices(pageable);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping(value = "/prices/delete/{id}")
	public ResponseEntity<MessageResponse> deleteById(@PathVariable(name = "id") int id) {
		priceService.deleteById(id);
		return ResponseEntity.ok(new MessageResponse("Price deleted successfully!"));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping(value = "/prices/save")
	public ResponseEntity<PriceDTO> savePriceWithProductIdAndStoreId(@Valid @RequestBody Price price) {
		return new ResponseEntity<>(priceService.savePriceWithProductNameAndStoreName(price), HttpStatus.CREATED);
	}

	@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
	@GetMapping(value = "/prices/equals")
	public ResponseEntity<PriceDTO> findPricesByProductIdAndStoreIdAndReturnGreatest(
			@RequestParam int productId,
			@RequestParam int firstStoreId,
			@RequestParam int secondStoreId) {
		return new ResponseEntity<>(priceService.findPricesByProductIdAndStoreIdAndReturnGreatest(
				productId, firstStoreId, secondStoreId), HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
	@GetMapping(value = "/prices/list/dynamics")
	public Page<PriceDynamicsResponse> findPriceDynamicsByProductIdAndStoreId(
			@PageableDefault(sort = "id", direction = Sort.Direction.ASC)
			@RequestParam int productId,
			@RequestParam int storeId,
			Pageable pageable) {
		return priceService.findPriceDynamicsByProductIdAndStoreId(productId, storeId, pageable);
	}
}