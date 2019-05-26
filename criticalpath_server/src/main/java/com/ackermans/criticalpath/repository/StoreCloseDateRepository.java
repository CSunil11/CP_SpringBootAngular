package com.ackermans.criticalpath.repository;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ackermans.criticalpath.entity.CloseDate;

@Repository
public interface StoreCloseDateRepository extends JpaRepository<CloseDate, Long> {

	/**
	 * Get List Of CloseDate by Date
	 * 
	 * @param parse
	 * @return
	 */
	List<CloseDate> findByCloseDate(Date date);

	/**
	 * Get All Active CloseDate
	 * 
	 * @return
	 */
	List<CloseDate> findAllByIsActiveTrue();
	
	/**
	 * Get All Deleted CloseDate
	 * 
	 * @return
	 */
	List<CloseDate> findAllByIsActiveFalseAndIsDeleteTrue();

	/**
	 * Get CloseDate By Store Id
	 * 
	 * @param storeId
	 * @return
	 */
	List<CloseDate> findByStores_Id(Long storeId);

	/**
	 * Get total Count By Name and CloseDate
	 *  
	 * @param parse
	 * @return
	 */
	int countByNameOrCloseDate(String name,Date date);

	/**
	 * Get total count by name or close date but ignore record having given id
	 * 
	 * @param closeDate
	 * @param ignoreId
	 * @return
	 */
	@Query("SELECT COUNT(*) FROM CloseDate WHERE (name = :name OR closeDate = :closeDate) AND id != :ignoreId")
	int countByCloseDateAndIgnoreId(String name, Date closeDate, Long ignoreId);
	
	/**
	 * Deletes close date id in store close date  
	 * @param closDateId
	 */
	@Modifying
	@Query(value="DELETE FROM store_close_date WHERE close_date_id = ?1", nativeQuery=true)
	void deleteStoreCloseDate(Long closDateId);

	/**
	 * Search active closed date of stores
	 * 
	 * @param storeId
	 * @return
	 */
	List<CloseDate> findByStores_IdAndIsActiveTrue(Long storeId );

	
	/**
	 * Get count of closed date by date
	 * @param storeId
	 * @param newdate
	 * @param stokedate
	 * @return
	 */
	@Query(value="select COUNT(*) from close_date where close_date between :newdate AND :stokedate and id in (select close_date_id from store_close_date where store_id=:storeId)" ,nativeQuery=true)
	int findByStoresDate(Long storeId,String newdate ,String stokedate);

	/**
	 *  Get closed date list by date
	 * @param storeId
	 * @param newdate
	 * @param stokedate
	 * @return
	 */
	@Query(value="select DATE_FORMAT(close_date,'%Y-%m-%d') from close_date where close_date between :newdate AND :stokedate and id in (select close_date_id from store_close_date where store_id=:storeId)" ,nativeQuery=true)
	List closedDateByStoresDate(Long storeId, String newdate, String stokedate);
	
	@Query(value="select DATE_FORMAT(close_date,'%Y-%m-%d') from close_date where close_date between :newdate AND :stokedate and id in (select close_date_id from store_close_date where store_id=:storeId)" ,nativeQuery=true)
	List closedDateByStoreId(Long storeId, Calendar newdate, Calendar stokedate);

}
