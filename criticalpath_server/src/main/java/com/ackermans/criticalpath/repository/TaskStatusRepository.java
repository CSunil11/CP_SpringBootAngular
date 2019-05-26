package com.ackermans.criticalpath.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ackermans.criticalpath.entity.Store;
import com.ackermans.criticalpath.entity.TaskStatus;

@Repository
public interface TaskStatusRepository extends JpaRepository<TaskStatus, Long> {

	/**
	 * Get number of records which has given name but ignore case
	 * 
	 * @param name
	 * @return
	 */
	long countByNameIgnoreCase(String name);
	
	/**
	 * Get number of records which has given name but ignore case 
	 * and id should not be equal to ignoreId
	 * 
	 * @param name
	 * @param ignoreId
	 * @return
	 */
	@Query("SELECT COUNT(*) FROM TaskStatus WHERE name = :name AND id != :ignoreId")
	long countByNameAndIgnoreId(String name, Long ignoreId);
	
	/**
	 * Find by name but ignore case
	 * 
	 * @param name
	 * @return
	 */
	TaskStatus findByNameIgnoreCase(String name);	
	
	/**
	 * Get all Active Task Status
	 * @return
	 */
	List<TaskStatus> findAllByIsActiveTrue();

	/**
	 * Get all deleted Task Status
	 * @return
	 */
	List<TaskStatus> findAllByIsActiveFalseAndIsDeleteTrue();

}
