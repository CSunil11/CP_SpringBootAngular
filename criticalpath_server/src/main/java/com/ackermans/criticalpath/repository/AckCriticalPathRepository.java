package com.ackermans.criticalpath.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ackermans.criticalpath.entity.AckCriticalPath;

@Repository
public interface AckCriticalPathRepository extends JpaRepository<AckCriticalPath, Long>{

	@Query(value = "SELECT * FROM ack_critical_path  where cycle_id =:stockTakeCycleId and status_id != 5 and is_active = 1 ORDER BY start_day desc", nativeQuery = true)
	List<AckCriticalPath> findByCycleIdWithStatusisNotDisable(Long stockTakeCycleId);
	
	@Query(value = "SELECT count(*) FROM ack_critical_path " + 
			" where cycle_id =:stockTakeCycleId " + 
			" and status_id in (6, 2)" + 
			" and is_active = 1 " + 
			" ORDER BY start_day desc", nativeQuery = true)
	long countCompAckCpWithStatusisNotDisable(Long stockTakeCycleId);
	
	@Query(value = "SELECT count(*) FROM ack_critical_path " + 
			" where cycle_id =:stockTakeCycleId " + 
			" and is_active = 1 " + 
			" ORDER BY start_day desc", nativeQuery = true)
	long countTotAckCpWithStatusisNotDisable(Long stockTakeCycleId);
	
	List<AckCriticalPath> findByCycle_Id(Long stockTakeCycleId);
	
	@Query(value = "SELECT * FROM ack_critical_path  where cycle_id =:stockTakeCycleId and status_id != 6 and status_id != 2 and is_active = 1 ORDER BY start_day desc", nativeQuery = true)
	List<AckCriticalPath> findByCycleIdWithStatusisNotDoneAndComplete(Long stockTakeCycleId);
	
	@Query(value = "SELECT count(*) FROM ack_critical_path  where cycle_id =:stockTakeCycleId and completed_by IS NULL and  id != :TaskId", nativeQuery = true)
	long updateAckCycleIsComplete(Long stockTakeCycleId , Long TaskId);
	

}
