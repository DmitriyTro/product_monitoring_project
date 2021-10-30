package com.springboot.product_monitoring.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;

public interface CSVService {

	void save (MultipartFile file);

	ByteArrayInputStream load();
}
