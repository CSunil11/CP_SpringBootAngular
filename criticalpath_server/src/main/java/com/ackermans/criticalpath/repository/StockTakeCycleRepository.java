package com.ackermans.criticalpath.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ackermans.criticalpath.admin.dto.StoreInterface;
import com.ackermans.criticalpath.dto.StockAckerResponse;
import com.ackermans.criticalpath.entity.StockTakeCycle;
import com.ackermans.criticalpath.entity.Store;
import com.ackermans.criticalpath.entity.User;

@Repository
public interface StockTakeCycleRepository extends JpaRepository<StockTakeCycle, Long> {

	/**
	 * Get All Active StockTakeCycle
	 * 
	 * @return
	 */
	List<StockTakeCycle> findAllByIsActiveTrue();
	
	/**
	 * Get All deleted StockTakeCycle
	 * 
	 * @return
	 */
	List<StockTakeCycle> findAllByIsActiveFalseAndIsDeleteTrue();
	
	/**
	 * Get total Count StockTakeCycle by name 
	 * @param name
	 * @return
	 */
	int countByNameIgnoreCase(String name);

	/**
	 * Get total Count StockTakeCycle by name but ignore id
	 * @param name
	 * @param ignoreId
	 * @return
	 */
	@Query("SELECT COUNT(*) FROM StockTakeCycle WHERE name = :name AND id != :ignoreId")
	long countByNameAndIgnoreId(String name, Long ignoreId);
	
	/**
	 * Search active stock take cycle of stores
	 * 
	 * @param storeId
	 * @return
	 */
//	List<StockTakeCycle> findByStores_IdAndIsActiveTrue(Long storeId);
	
	/**
	 * Get stock take cycle for given stores
	 * 
	 * @param stores
	 * @return
	 */
//	List<StockTakeCycle> findByIsActiveTrueAndIsDeleteFalseAndStoresIn(List<Store> stores);
	
	/**
	 * Get stock take cycle for given stores
	 * 
	 * @return
	 */
//	@Query(value = "select c.name as StoreName, b.name as cycleName" + 
//			"  from store_stock_take_cycle a " + 
//			" inner join stock_take_cycle b on a.stock_take_cycle_id = b.id " + 
//			" inner join store c on a.store_id = c.id" + 
//			" where b.is_active = 1 and" + 
//			" a.store_id = c.id" + 
//			" group by a.store_id " + 
//			" order by b.created_date_time asc", nativeQuery = true)
//	List<Object> getAllActiveStockTakeCyclePerStore();
	
	/**
	 * Get stock take cycle for given stores
	 * 
	 * @param storeId
	 * @return
	 */
//	@Query(value="select b.name as cycleName, b.id as cycleId, DATE_FORMAT(b.stoke_take_date, \"%d %M %Y\") as stockTakeDate, DATEDIFF(DATE_FORMAT(b.stoke_take_date, \"%Y-%m-%d\"),DATE_FORMAT(CURDATE(), \\\"%Y-%m-%d\\\")) as days, "
//			+ " c.name as storeName , c.id as storeId" + 
//			" from store_stock_take_cycle a " + 
//			" inner join stock_take_cycle b on a.stock_take_cycle_id = b.id " + 
//			" inner join store c on a.store_id = c.id " + 
//			" where b.is_active = 1 and " + 
//			" a.store_id = :storeId" + 
//			" order by b.stoke_take_date asc", nativeQuery = true)
//	List<StockAckerResponse> getActiveStockTakeCyclePerStore(Long storeId);
	
	/**
	 * Get stock take cycle status for given stores
	 * 
	 * @param storeId
	 * @return
	 */
//	@Query(value="select s.id as storeId, s.name as storeName, stc.id as cycleId, stc.name as cycleName, count(*) as totCp" + 
//			" from store_stock_take_cycle sstc" + 
//			" inner join stock_take_cycle stc on sstc.stock_take_cycle_id = stc.id " + 
//			" inner join store s on sstc.store_id = s.id " + 
//			" inner join critical_path cp on cp.cycle_id = stc.id " + 
//			" inner join task_status ts on ts.id = cp.status_id " + 
//			" where stc.is_active = 1 " + 
//			" and cp.status_id != 5" +
//			" and sstc.store_id = :storeId" + 
//			" group by stc.id" + 
//			" order by stc.created_date_time asc", nativeQuery = true)
//	List<StockAckerResponse> getStockTakeCycleStatusPerStoreForTot(String storeId);
	
//	@Query(value="select s.id as storeId, s.name as storeName, stc.id as cycleId, stc.name as cycleName, count(*) as totCompCp" + 
//			" from store_stock_take_cycle sstc" + 
//			" inner join stock_take_cycle stc on sstc.stock_take_cycle_id = stc.id " + 
//			" inner join store s on sstc.store_id = s.id " + 
//			" inner join critical_path cp on cp.cycle_id = stc.id " + 
//			" inner join task_status ts on ts.id = cp.status_id " + 
//			" where stc.is_active = 1 " + 
//			" and sstc.store_id = :storeId" + 
//			" and cp.status_id = 6" +
//			" group by stc.id" + 
//			" order by stc.created_date_time asc", nativeQuery = true)
//	List<StockAckerResponse> getStockTakeCycleStatusPerStoreForComp(String storeId);
	
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
	
//	@Query(value="SELECT s.name as name, s.id as id " +
//			" FROM location_division ld " + 
//			" INNER JOIN store s ON ld.id = s.location_division_id " + 
//			" WHERE ld.ram_user_id = (select id from user where user_login_id = :id)", nativeQuery=true)
	@Query(value="SELECT s.name as name, s.id as id FROM" +
			" store s " + 
			" WHERE s.ram_user_id = (select id from user where user_login_id = :id)", nativeQuery=true)
	List<StoreInterface> getAllActiveStoresPerRamUserInterface(Long id);
	
	/**
	 * Get stock take cycle for given stores for DSM User
	 * DSM User, All Stores, StoreId=-1
	 * 
	 * @param storeId
	 * @return
	 */
//	@Query(value="select  stc.name as cycleName, stc.id as cycleId, DATE_FORMAT(stc.stoke_take_date, \"%d %M %Y\") as stockTakeDate, "
//			+ "DATEDIFF(DATE_FORMAT(stc.stoke_take_date, \"%Y-%m-%d\"),DATE_FORMAT(CURDATE(), \\\"%Y-%m-%d\\\")) as days ,"
//			+ "s.name as storeName , s.id as storeId from critical_path cp " + 
//			" right join stock_take_cycle stc on stc.id = cp.cycle_id " + 
//			" inner join  store_stock_take_cycle sstc on sstc.stock_take_cycle_id = stc.id " + 
//			" inner join store s on  sstc.store_id = s.id " + 
//			" where " + 
//			" stc.is_active = 1 " + 
//			" and s.location_division_id=(SELECT id FROM location_division WHERE user_id = (select id from user where user_login_id = :userId)) " + 
//			" group by stc.id" +
//			" order by s.name asc ", nativeQuery=true)
//	List<StockAckerResponse> getAllActiveStockTakeCycleDsmUserSortedByName(Long userId);
	
//	@Query(value="select  stc.name as cycleName, stc.id as cycleId, DATE_FORMAT(stc.stoke_take_date, \"%d %M %Y\") as stockTakeDate, "
//			+ "DATEDIFF(DATE_FORMAT(stc.stoke_take_date, \"%Y-%m-%d\"),DATE_FORMAT(CURDATE(), \\\"%Y-%m-%d\\\")) as days ,"
//			+ "s.name as storeName , s.id as storeId from critical_path cp " + 
//			" right join stock_take_cycle stc on stc.id = cp.cycle_id " + 
//			" inner join  store_stock_take_cycle sstc on sstc.stock_take_cycle_id = stc.id " + 
//			" inner join store s on  sstc.store_id = s.id " + 
//			" where " + 
//			" stc.is_active = 1 " + 
//			" and s.location_division_id=(SELECT id FROM location_division WHERE user_id = (select id from user where user_login_id = :userId)) " + 
//			" group by stc.id" +
//			" order by stc.stoke_take_date asc ", nativeQuery=true)
//	List<StockAckerResponse> getAllActiveStockTakeCycleDsmUserSortedByTakeDate(Long userId);
	
	/**
	 * Get stock take cycle for given stores for DSM User
	 * DSM User, All Stores, StoreId=-1
	 * 
	 * @param storeId
	 * @return
	 */
//	@Query(value="select  stc.name as cycleName, stc.id as cycleId, DATE_FORMAT(stc.stoke_take_date, \"%d %M %Y\") as stockTakeDate, " + 
//			" DATEDIFF(DATE_FORMAT(stc.stoke_take_date, \\\"%Y-%m-%d\\\"),DATE_FORMAT(CURDATE(), \\\"%Y-%m-%d\\\")) as days ,"
//			+ "s.name as storeName , s.id as storeId from critical_path cp " + 
//			" right join stock_take_cycle stc on stc.id = cp.cycle_id " + 
//			" inner join  store_stock_take_cycle sstc on sstc.stock_take_cycle_id = stc.id " + 
//			" inner join store s on  sstc.store_id = s.id " + 
//			" where " + 
//			" stc.is_active = 1 " + 
//			" and s.location_division_id in (SELECT id FROM location_division WHERE ram_user_id = (select id from user where user_login_id = :userId)) " + 
//			" group by stc.id , s.id " +
//			" order by s.name asc ", nativeQuery=true)
//	List<StockAckerResponse> getAllActiveStockTakeCycleRamUserByName(Long userId);
	
//	@Query(value="select  stc.name as cycleName, stc.id as cycleId, DATE_FORMAT(stc.stoke_take_date, \"%d %M %Y\") as stockTakeDate, " + 
//			" DATEDIFF(DATE_FORMAT(stc.stoke_take_date, \\\"%Y-%m-%d\\\"),DATE_FORMAT(CURDATE(), \\\"%Y-%m-%d\\\")) as days ,"
//			+ "s.name as storeName , s.id as storeId from critical_path cp " + 
//			" right join stock_take_cycle stc on stc.id = cp.cycle_id " + 
//			" inner join  store_stock_take_cycle sstc on sstc.stock_take_cycle_id = stc.id " + 
//			" inner join store s on  sstc.store_id = s.id " + 
//			" where " + 
//			" stc.is_active = 1 " + 
//			" and s.location_division_id in (SELECT id FROM location_division WHERE ram_user_id = (select id from user where user_login_id = :userId)) " + 
//			" group by stc.id , s.id " +
//			" order by stc.stoke_take_date asc ", nativeQuery=true)
//	List<StockAckerResponse> getAllActiveStockTakeCycleRamUserByStockDate(Long userId);
	
	/**
	 * Get All Active Stock Take Cycle Status for Selected DSM User
	 * 
	 * @return
	 */
//	@Query(value="select stc.name as cycleName, stc.id as cycleId, DATE_FORMAT(stc.stoke_take_date, \"%d %M %Y\") as stockTakeDate, "
//			+ " DATEDIFF(DATE_FORMAT(stc.stoke_take_date, \"%Y-%m-%d\"),DATE_FORMAT(CURDATE(), \\\"%Y-%m-%d\\\")) as days, "
//			+ "s.name as storeName, s.id as storeId, ts.name as status  from critical_path cp " + 
//			" right join stock_take_cycle stc on stc.id = cp.cycle_id " + 
//			" inner join  store_stock_take_cycle sstc on sstc.stock_take_cycle_id = stc.id " + 
//			" inner join store s on  sstc.store_id = s.id " + 
//			" inner join task_status ts on ts.id = cp.status_id " + 
//			" where " + 
//			" stc.is_active = 1 " + 
//			" and s.location_division_id=(SELECT id FROM location_division WHERE user_id = (select id from user where user_login_id = :userId)) " +
//			" group by stc.id " +
//			" order by stc.stoke_take_date asc ", nativeQuery=true)
//	List<StockAckerResponse> getAllActiveStockTakeCycleStatusDsmUserByStockTake(Long userId);
	
	/**
	 * Get All Active Stock Take Cycle Status for Selected DSM User
	 * 
	 * @return
	 */
//	@Query(value="select s.id as storeId, stc.id as cycleId, count(*) as totCp from critical_path cp " + 
//			" left join stock_take_cycle stc on stc.id = cp.cycle_id " + 
//			" inner join store_stock_take_cycle sstc on sstc.stock_take_cycle_id = stc.id " + 
//			" inner join store s on  sstc.store_id = s.id " + 
//			" inner join task_status ts on ts.id = cp.status_id " + 
//			" where " + 
//			" stc.is_active = 1 " +
//			" and cp.status_id != 5" +
//			" and s.location_division_id=(SELECT id FROM location_division WHERE user_id = (select id from user where user_login_id = :userId))" + 
//			" group by stc.id" + 
//			" order by s.name asc", nativeQuery=true)
//	List<StockAckerResponse> getAllActiveStockTakeCycleStatusDsmUserByNameForTot(String userId);
	
//	@Query(value="select s.id as storeId, stc.id as cycleId, count(*) as totCp from critical_path cp " + 
//			" left join stock_take_cycle stc on stc.id = cp.cycle_id " + 
//			" inner join store_stock_take_cycle sstc on sstc.stock_take_cycle_id = stc.id " + 
//			" inner join store s on  sstc.store_id = s.id " + 
//			" inner join task_status ts on ts.id = cp.status_id " + 
//			" where " + 
//			" stc.is_active = 1 " +
//			" and cp.status_id != 5" +
//			" and s.location_division_id in (SELECT id FROM location_division WHERE ram_user_id = (select id from user where user_login_id = :userId))" + 
//			" group by stc.id" + 
//			" order by s.name asc", nativeQuery=true)
//	List<StockAckerResponse> getAllActiveStockTakeCycleStatusRamUserByNameForTot(String userId);
	
	/**
	 * Finds the total completed tasks for the selected user
	 * @param userId
	 * @return
	 */
//	@Query(value="select s.id as storeId, s.name as storeName, stc.id as cycleId, stc.name as cycleName, count(*) as totCompCp from critical_path cp " + 
//			" left join stock_take_cycle stc on stc.id = cp.cycle_id " + 
//			" inner join store_stock_take_cycle sstc on sstc.stock_take_cycle_id = stc.id " + 
//			" inner join store s on  sstc.store_id = s.id " + 
//			" inner join task_status ts on ts.id = cp.status_id " + 
//			" where " + 
//			" stc.is_active = 1 " + 
//			" and cp.status_id = 6" + 
//			" and s.location_division_id=(SELECT id FROM location_division WHERE user_id = (select id from user where user_login_id = :userId))" + 
//			" group by stc.id" + 
//			" order by s.name asc", nativeQuery=true)
//	List<StockAckerResponse> getAllActiveStockTakeCycleStatusDsmUserByNameForComp(String userId);
	
//	@Query(value="select s.id as storeId, s.name as storeName, stc.id as cycleId, stc.name as cycleName, count(*) as totCompCp from critical_path cp " + 
//			" left join stock_take_cycle stc on stc.id = cp.cycle_id " + 
//			" inner join store_stock_take_cycle sstc on sstc.stock_take_cycle_id = stc.id " + 
//			" inner join store s on  sstc.store_id = s.id " + 
//			" inner join task_status ts on ts.id = cp.status_id " + 
//			" where " + 
//			" stc.is_active = 1 " + 
//			" and cp.status_id = 6" + 
//			" and s.location_division_id in (SELECT id FROM location_division WHERE ram_user_id = (select id from user where user_login_id = :userId))" + 
//			" group by stc.id" + 
//			" order by s.name asc", nativeQuery=true)
//	List<StockAckerResponse> getAllActiveStockTakeCycleStatusRamUserByNameForComp(String userId);
	
	/**
	 * Get All Active Stock Take Cycle Status for Selected DSM User
	 * 
	 * @return
	 */
//	@Query(value="select stc.name as cycleName, stc.id as cycleId, s.name as storeName , s.id as storeId, ts.name as status  from critical_path cp " + 
//			" right join stock_take_cycle stc on stc.id = cp.cycle_id " + 
//			" inner join  store_stock_take_cycle sstc on sstc.stock_take_cycle_id = stc.id " + 
//			" inner join store s on  sstc.store_id = s.id " + 
//			" inner join task_status ts on ts.id = cp.status_id " + 
//			" where " + 
//			" stc.is_active = 1 " + 
//			" and s.location_division_id in (SELECT id FROM location_division WHERE ram_user_id = (select id from user where user_login_id = :userId)) "
//			+ "group by stc.id "
//			+ "order by s.name asc ", nativeQuery=true)
//	List<StockAckerResponse> getAllActiveStockTakeCycleStatusRamUserByName(Long userId);
	
	/**
	 * Get All Active Stock Take Cycle Status for Selected DSM User
	 * 
	 * @return
	 */
//	@Query(value="select stc.name as cycleName, stc.id as cycleId, s.name as storeName , s.id as storeId, ts.name as status  from critical_path cp " + 
//			" right join stock_take_cycle stc on stc.id = cp.cycle_id " + 
//			" inner join  store_stock_take_cycle sstc on sstc.stock_take_cycle_id = stc.id " + 
//			" inner join store s on  sstc.store_id = s.id " + 
//			" inner join task_status ts on ts.id = cp.status_id " + 
//			" where " + 
//			" stc.is_active = 1 " + 
//			" and s.location_division_id in (SELECT id FROM location_division WHERE ram_user_id = (select id from user where user_login_id = :userId)) "
//			+ "group by stc.id "
//			+ "order by stc.stoke_take_date asc ", nativeQuery=true)
//	List<StockAckerResponse> getAllActiveStockTakeCycleStatusRamUserByStockDate(Long userId);

//	@Query(value="select c.id as storeId , c.name as storeName, "
//			+ "b.id as cycleId , b.stoke_take_date as stockTakeDate , b.length , b.do_not_count" + 
//			" from store_stock_take_cycle a " + 
//			" inner join stock_take_cycle b on a.stock_take_cycle_id = b.id " + 
//			" inner join store c on a.store_id = c.id " + 
//			" where b.is_active = 1 and " + 
//			" c.id = (SELECT store_id FROM user where user_login_id =:userId) LIMIT 1"  
//			, nativeQuery = true)
//	List<Object> getStockTakeCycleByUser(Long userId);
	
}
