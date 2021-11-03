package com.springboot.product_monitoring.services.impl;

import com.springboot.product_monitoring.csv.CSVCustomParser;
import com.springboot.product_monitoring.entities.Price;
import com.springboot.product_monitoring.repositories.PriceRepository;
import com.springboot.product_monitoring.services.CSVService;
import com.springboot.product_monitoring.services.PriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
public class CSVServiceImpl implements CSVService {

	private final PriceRepository priceRepository;
	private final CSVCustomParser csvCustomParser;
	private final PriceService priceService;

	@Autowired
	public CSVServiceImpl(PriceRepository priceRepository, CSVCustomParser csvCustomParser, PriceService priceService) {
		this.priceRepository = priceRepository;
		this.csvCustomParser = csvCustomParser;
		this.priceService = priceService;
	}

	@Override
	public void save(MultipartFile file) {

		try {
			List<Price> prices = csvCustomParser.csvToPrices(file.getInputStream());
			priceRepository.saveAll(prices);
		} catch (IOException e) {
			throw new RuntimeException("Fail to store csv data: " + e.getMessage());
		}
	}

	@Override
	public ByteArrayInputStream load() {

		return csvCustomParser.pricesToCSV(priceRepository.findAll());
	}

	@Override
	public ByteArrayInputStream pricesToCSVByDateBetweenAndProductIdAndStoreId(
			Date from, Date to, int productId, int storeId) {

		return csvCustomParser.pricesToCSVByDateBetween(priceService.findAllByDateBetweenAndProductIdAndStoreId(
				from, to, productId, storeId));
	}
}