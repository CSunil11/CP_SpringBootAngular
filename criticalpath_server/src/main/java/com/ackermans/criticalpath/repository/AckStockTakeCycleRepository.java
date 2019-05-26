package com.ackermans.criticalpath.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ackermans.criticalpath.admin.dto.StoreInterface;
import com.ackermans.criticalpath.dto.StockAckerResponse;
import com.ackermans.criticalpath.entity.AckStockTakeCycle;
import com.ackermans.criticalpath.entity.StockTakeCycle;
import com.ackermans.criticalpath.entity.Store;
import com.ackermans.criticalpath.entity.User;

@Repository
public interface AckStockTakeCycleRepository extends JpaRepository<AckStockTakeCycle, Long> {

	/**
	 * Get All Active StockTakeCycle
	 * 
	 * @return
	 */
	List<AckStockTakeCycle> findAllByIsActiveTrue();
	
	/**
	 * Get All deleted StockTakeCycle
	 * 
	 * @return
	 */
	List<AckStockTakeCycle> findAllByIsActiveFalseAndIsDeleteTrue();
	
	/**
	 * Get total Count StockTakeCycle by name 
	 * @param name
	 * @return
	 */
	int countByNameIgnoreCase(String name);

	/**
	 * Get stores for given DSM User
	 * 
	 * @param storeId
	 * @return
	 */
	@Query(value="SELECT s.name as name, s.id as id " +
			" FROM location_division ld " + 
			" INNER JOIN store s ON ld.id = s.location_division_id " + 
			" WHERE ld.user_id = :id", nativeQuery=true)
	List<Object> getAllActiveStoresPerDsmUser(Long id);

	@Query(value="SELECT s.name as name, s.id as id " +
			" FROM location_division ld " + 
			" INNER JOIN store s ON ld.id = s.location_division_id " + 
			" WHERE ld.user_id = (select id from user where user_login_id = :id)", nativeQuery=true)
	List<StoreInterface> getAllActiveStoresPerDsmUserInterface(Long id);
	
	@Query(value="SELECT s.name as name, s.id as id " +
			" FROM location_division ld " + 
			" INNER JOIN store s ON ld.id = s.location_division_id " + 
			" WHERE ld.ram_user_id = (select id from user where user_login_id = :id)", nativeQuery=true)
	List<StoreInterface> getAllActiveStoresPerRamUserInterface(Long id);

	List<AckStockTakeCycle> findByStore_IdAndIsActiveTrue(Long id);
	
	List<AckStockTakeCycle> findByStore_IdAndIsActiveTrueOrderByStokeStartDateAsc(Long id);

	
	//Write logic if Cycle available for the given Store_id and Stock_take_date >= current_date 
	@Query(value="SELECT *  FROM ack_stock_take_cycle" +
			" WHERE store_id =:storeId and " + 
			" is_active = 1", nativeQuery=true)
	AckStockTakeCycle findByStoreIdIfStillActive(Long storeId);
	
	@Query(value="SELECT *  FROM ack_stock_take_cycle" +
			" WHERE store_id =:storeId and CURDATE() between stoke_start_date and stoke_take_date and " + 
			" is_active = 1", nativeQuery=true)
	AckStockTakeCycle findByStoreIdIfAvailCurrently(Long storeId);
	
	@Query(value="SELECT *  FROM ack_stock_take_cycle" +
			" WHERE store_id =:storeId and  CURDATE() < stoke_take_date and is_active = 1 and is_completed=0 and id =:cycleId", nativeQuery=true)
	AckStockTakeCycle findByStoreIdIfStillActiveAndComplete(Long storeId, Long cycleId);

	@Query(value="SELECT * FROM ack_stock_take_cycle" +
			" WHERE CURDATE() between " + 
			" stoke_start_date and stoke_take_date", nativeQuery=true)
	List<AckStockTakeCycle> todayAllActiveCycle();
	
	/*
	 * @Query(value="SELECT *  FROM ack_stock_take_cycle" + " WHERE id =:id",
	 * nativeQuery=true)
	 */

	 @Query(value="SELECT DATE_FORMAT(stoke_take_date,'%Y-%m-%d') as stockdate, time as time FROM ack_stock_take_cycle WHERE id =:id",
	  nativeQuery=true)
	String findByManualId(Long id);
	 
	 @Query(value="SELECT * FROM ack_stock_take_cycle" +
				" WHERE CURDATE() < stoke_take_date "
				+ " and is_completed = 0", nativeQuery=true)
	 List<AckStockTakeCycle> findByStockTakeDateValidateWithCurDate();
}
