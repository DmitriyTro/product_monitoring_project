package com.springboot.product_monitoring.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.Date;

public interface CSVService {

	void save (MultipartFile file);

	ByteArrayInputStream load();

	ByteArrayInputStream pricesToCSVByDateBetweenAndProductIdAndStoreId(Date from, Date to, int productId, int storeId);
}
