package com.springboot.product_monitoring.controllers;

import com.springboot.product_monitoring.csv.CSVCustomParser;
import com.springboot.product_monitoring.dto.payload.response.MessageResponse;
import com.springboot.product_monitoring.exceptions.csv.CsvCustomExceptionHandler;
import com.springboot.product_monitoring.services.CSVService;
import com.springboot.product_monitoring.services.impl.CSVServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@CrossOrigin(origins = "*", maxAge = 3600)
@CsvCustomExceptionHandler
@RestController
@RequestMapping("/api/admin/csv")
public class CSVRestController {

	private final CSVService fileService;

	@Autowired
	public CSVRestController(CSVServiceImpl fileService) {
		this.fileService = fileService;
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping(value = "/upload")
	public ResponseEntity<MessageResponse> uploadFile(@RequestParam("file") MultipartFile file) {
		String message = "";

		if (CSVCustomParser.hasCSVFormat(file)) {
			try {
				fileService.save(file);

				message = "Uploaded the file successfully: " + file.getOriginalFilename();
				return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(message));
			} catch (Exception e) {
				message = "Could not upload the file: " + file.getOriginalFilename() + "!";
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new MessageResponse(message));
			}
		}

		message = "Please upload a csv file!";
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(message));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping(value = "/download")
	public ResponseEntity<Resource> getFile() {
		String fileName = "prices.csv";
		InputStreamResource file = new InputStreamResource(fileService.load());

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename = " + fileName)
				.contentType(MediaType.parseMediaType("application/csv"))
				.body(file);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping(value = "/download/date")
	public ResponseEntity<Resource> getFileByDateBetweenAndProductId(
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date to,
			@RequestParam int productId,
			@RequestParam int storeId) {

		String fileName = "prices.csv";
		InputStreamResource file = new InputStreamResource(fileService.pricesToCSVByDateBetweenAndProductIdAndStoreId(
				from, to, productId, storeId));

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename = " + fileName)
				.contentType(MediaType.parseMediaType("application/csv"))
				.body(file);
	}
}