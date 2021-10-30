package com.springboot.product_monitoring.csv;

import com.springboot.product_monitoring.entities.Price;
import com.springboot.product_monitoring.repositories.ProductRepository;
import com.springboot.product_monitoring.repositories.StoreRepository;
import org.apache.commons.csv.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class CSVParser {

	public static String TYPE = "text/csv";

	private final ProductRepository productRepository;
	private final StoreRepository storeRepository;

	@Autowired
	public CSVParser(ProductRepository productRepository, StoreRepository storeRepository) {
		this.productRepository = productRepository;
		this.storeRepository = storeRepository;
	}

	public static boolean hasCSVFormat(MultipartFile file) {

		return TYPE.equals(file.getContentType());
	}

	public List<Price> csvToPrices(InputStream is) {
		try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
		     org.apache.commons.csv.CSVParser csvParser = new org.apache.commons.csv.CSVParser(fileReader,
				     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {

			List<Price> prices = new ArrayList<>();

			Iterable<CSVRecord> csvRecords = csvParser.getRecords();

			for (CSVRecord csvRecord : csvRecords) {
				Price price = new Price(
						Integer.parseInt(csvRecord.get("unitPrice")),
						Timestamp.valueOf(csvRecord.get("date")),
						productRepository.getById(Integer.parseInt(csvRecord.get("product"))),
						storeRepository.getById(Integer.parseInt(csvRecord.get("store"))));

				prices.add(price);
			}

			return prices;
		} catch (IOException e) {
			throw new RuntimeException("Fail to parse CSV file: " + e.getMessage());
		}
	}

	public ByteArrayInputStream pricesToCSV(List<Price> prices) {
		final CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);

		try (ByteArrayOutputStream out = new ByteArrayOutputStream();
		     CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format)) {
			for (Price price : prices) {
				List<String> data = Arrays.asList(
						String.valueOf(price.getId()),
						String.valueOf(price.getUnitPrice()),
						String.valueOf(price.getDate()),
						String.valueOf(price.getProduct().getProductName()),
						String.valueOf(price.getStore().getStoreName()));

				csvPrinter.printRecord(data);
			}

			csvPrinter.flush();
			return new ByteArrayInputStream(out.toByteArray());
		} catch (IOException e) {
			throw new RuntimeException("Fail to import data to CSV file: " + e.getMessage());
		}
	}
}