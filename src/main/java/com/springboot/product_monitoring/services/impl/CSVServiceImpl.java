package com.springboot.product_monitoring.services.impl;

import com.springboot.product_monitoring.csv.CSVParser;
import com.springboot.product_monitoring.entities.Price;
import com.springboot.product_monitoring.repositories.PriceRepository;
import com.springboot.product_monitoring.services.CSVService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@Service
public class CSVServiceImpl implements CSVService {

	private final PriceRepository priceRepository;
	private final CSVParser csvParser;

	@Autowired
	public CSVServiceImpl(PriceRepository priceRepository, CSVParser csvParser) {
		this.priceRepository = priceRepository;
		this.csvParser = csvParser;
	}

	public void save(MultipartFile file) {

		try {
			List<Price> prices = csvParser.csvToPrices(file.getInputStream());
			priceRepository.saveAll(prices);
		} catch (IOException e) {
			throw new RuntimeException("Fail to store csv data: " + e.getMessage());
		}
	}

	public ByteArrayInputStream load() {

		return csvParser.pricesToCSV(priceRepository.findAll());
	}
}