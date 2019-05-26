package com.ackermans.criticalpath.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ackermans.criticalpath.entity.StockTakeResult;

@Repository
public interface StockTakeResultRepository extends JpaRepository<StockTakeResult, Long> {

	List<StockTakeResult> findByStore_IdOrderByCreatedDateTimeDesc(Long storeId);
	

}
