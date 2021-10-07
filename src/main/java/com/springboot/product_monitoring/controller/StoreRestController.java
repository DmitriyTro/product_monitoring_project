package com.springboot.product_monitoring.controller;

import com.springboot.product_monitoring.dto.StoreDTO;
import com.springboot.product_monitoring.entity.Store;
import com.springboot.product_monitoring.exception.stores.StoresCustomExceptionHandler;
import com.springboot.product_monitoring.mappers.StoreMapper;
import com.springboot.product_monitoring.payload.response.MessageResponse;
import com.springboot.product_monitoring.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@StoresCustomExceptionHandler
@RestController
@RequestMapping("/api/auth")
public class StoreRestController {

	private final StoreService storeService;
	private final StoreMapper storeMapper;

	@Autowired
	public StoreRestController(StoreService storeService, StoreMapper storeMapper) {
		this.storeService = storeService;
		this.storeMapper = storeMapper;
	}

	@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
	@GetMapping(value = "/stores/{id}")
	public ResponseEntity<StoreDTO> findStoreById(@PathVariable(name = "id") int id) {
		Store result = storeService.findStoreById(id).get();
		return new ResponseEntity<>(storeMapper.toDTO(result), HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
	@GetMapping(value = "/stores/name/{storeName}")
	public ResponseEntity<StoreDTO> findByStoreName(@PathVariable(name = "storeName") String storeName) {
		Store result = storeService.findByStoreName(storeName).get();
		return new ResponseEntity<>(storeMapper.toDTO(result), HttpStatus.OK);

	}

	@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
	@GetMapping(value = "/stores/list")
	public ResponseEntity<List<StoreDTO>> findAllStores() {
		List<Store> stores = storeService.findAllStores();
		return new ResponseEntity<>(storeMapper.toDTOList(stores), HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping(value = "/stores/delete/{id}")
	public ResponseEntity deleteById(@PathVariable(name = "id") int id) {
		storeService.deleteById(id);
		return ResponseEntity.ok(new MessageResponse("Store deleted successfully!"));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping(value = "/stores/save")
	public ResponseEntity<StoreDTO> saveStore(@RequestBody Store store) {
		Store result = storeService.saveStore(store);
		return new ResponseEntity<>(storeMapper.toDTO(result), HttpStatus.OK);
	}
}