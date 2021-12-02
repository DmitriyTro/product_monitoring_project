package com.springboot.product_monitoring.repositories;

import com.springboot.product_monitoring.entities.Price;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface PriceRepository extends JpaRepository<Price, Integer> {

	Price findFirstByProduct_IdAndStore_IdOrderByDateDesc(int productId, int storeId);

	List<Price> findPricesByDateBetweenAndProduct_IdAndStore_Id(Date from, Date to, int productId, int storeId);

	Page<Price> findPricesByProduct_IdAndStore_Id(int productId, int storeId, Pageable pageable);
}
