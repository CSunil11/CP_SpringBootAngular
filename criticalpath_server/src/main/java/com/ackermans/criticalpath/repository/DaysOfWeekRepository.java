package com.ackermans.criticalpath.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ackermans.criticalpath.entity.DaysOfWeek;

@Repository
public interface DaysOfWeekRepository extends JpaRepository<DaysOfWeek, Long> {

	/**
	 * Get total Count daysOfWeek by name 
	 * @param name
	 * @return
	 */
	int countByNameIgnoreCase(String name);
	
	/**
	 * Get total Count daysOfWeek by name but ignore id
	 * @param name
	 * @param ignoreId
	 * @return
	 */
	@Query("SELECT COUNT(*) FROM DaysOfWeek WHERE name = :name AND id != :ignoreId")
	int countByNameAndIgnoreId(String name, Long ignoreId);

	/**
	 * Get All Active daysOfWeek
	 * @return
	 */
	List<DaysOfWeek> findAllByIsActiveTrue();

	/**
	 * Get All deleted daysOfWeek
	 * @return
	 */
	List<DaysOfWeek> findAllByIsActiveFalseAndIsDeleteTrue();

}
