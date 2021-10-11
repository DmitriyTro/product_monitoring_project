package com.springboot.product_monitoring.controllers;

import com.springboot.product_monitoring.dto.StoreDTO;
import com.springboot.product_monitoring.dto.payload.response.MessageResponse;
import com.springboot.product_monitoring.entities.Store;
import com.springboot.product_monitoring.exceptions.store.StoreCustomExceptionHandler;
import com.springboot.product_monitoring.services.StoreService;
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
@StoreCustomExceptionHandler
@RestController
@RequestMapping("/api/auth")
public class StoreRestController {

	private final StoreService storeService;

	@Autowired
	public StoreRestController(StoreService storeService) {
		this.storeService = storeService;
	}

	@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
	@GetMapping(value = "/stores/{id}")
	public ResponseEntity<StoreDTO> findStoreById(@PathVariable(name = "id") int id) {
		return new ResponseEntity<>(storeService.findStoreById(id), HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
	@GetMapping(value = "/stores/name/{storeName}")
	public ResponseEntity<StoreDTO> findByStoreName(@PathVariable(name = "storeName") String storeName) {
		return new ResponseEntity<>(storeService.findByStoreName(storeName), HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
	@GetMapping(value = "/stores/list")
	public Page<StoreDTO> findAllStores(
			@PageableDefault(sort = "id", direction = Sort.Direction.ASC, size = 10) Pageable pageable) {
		return storeService.findAllStores(pageable);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping(value = "/stores/delete/{id}")
	public ResponseEntity deleteById(@PathVariable(name = "id") int id) {
		storeService.deleteById(id);
		return ResponseEntity.ok(new MessageResponse("Store deleted successfully!"));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping(value = "/stores/save")
	public ResponseEntity<StoreDTO> saveStore(@Valid @RequestBody Store store) {
		return new ResponseEntity<>(storeService.saveStore(store), HttpStatus.CREATED);
	}
}