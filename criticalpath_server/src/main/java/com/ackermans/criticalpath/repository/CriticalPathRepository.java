package com.ackermans.criticalpath.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ackermans.criticalpath.entity.CriticalPath;

@Repository
public interface CriticalPathRepository extends JpaRepository<CriticalPath, Long> {

	/**
	 * Get All Active Critical Path
	 * 
	 * @return
	 */
	List<CriticalPath> findAllByIsActiveTrue();
	
	/**
	 * Get All Deleted Critical Path
	 * 
	 * @return
	 */
	List<CriticalPath> findAllByIsActiveFalseAndIsDeleteTrue();
	
	
	/**
	 * Get total Count Critical Path by title 
	 * @param name
	 * @return
	 */
	int countByTitleIgnoreCase(String title);

	/**
	 * Get total Count Critical Path by title but ignore id
	 * @param title
	 * @param ignoreId
	 * @return
	 */
	@Query("SELECT COUNT(*) FROM CriticalPath WHERE title = :title AND id != :ignoreId")
	long countByTitleAndIgnoreId(String title, Long ignoreId);
	
	/**
	 * Get active critical path by task status id
	 * 
	 * @paramm taskStatusId
	 * @return
	 */
	List<CriticalPath> findByStatusIdAndIsActiveTrue(Long taskStatusId);

	/**
	 *  Get criticalPath by stockTakeCycleId
	 *  
	 * @param stockTakeCycleId
	 * @return
	 */
	@Query(value = "SELECT * FROM critical_path  where cycle_id =:stockTakeCycleId and status_id != 5 and is_active = 1 ORDER BY start_day desc", nativeQuery = true)
	List<CriticalPath> findByCycleIdWithStatusisNotDisable(Long stockTakeCycleId);
	
	@Query(value = "SELECT count(*) FROM critical_path " + 
			" where cycle_id =:stockTakeCycleId " + 
			" and status_id in (6, 2)" + 
			" and is_active = 1 " + 
			" ORDER BY start_day desc", nativeQuery = true)
	long countCompAckCpWithStatusisNotDisable(Long stockTakeCycleId);
	
	@Query(value = "SELECT count(*) FROM critical_path " + 
			" where cycle_id =:stockTakeCycleId " + 
			" and is_active = 1 " + 
			" ORDER BY start_day desc", nativeQuery = true)
	long countTotAckCpWithStatusisNotDisable(Long stockTakeCycleId);

}
