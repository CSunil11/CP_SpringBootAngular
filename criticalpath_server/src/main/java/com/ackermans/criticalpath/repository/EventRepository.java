package com.ackermans.criticalpath.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ackermans.criticalpath.entity.Event;

@Repository
public interface EventRepository extends JpaRepository<Event,Long> {

	/**
	 * Get total Count event by name 
	 * @param name
	 * @return
	 */
	int countByNameIgnoreCase(String name);

	/**
	 * Get total Count event by name but ignore id
	 * @param name
	 * @param ignoreId
	 * @return
	 */
	@Query("SELECT COUNT(*) FROM Event WHERE name = :name AND id != :ignoreId")
	long countByNameAndIgnoreId(String name, Long ignoreId);

	/**
	 * Get List By Searching parameter
	 * @param string
	 * @return
	 */
	List<Event> findByNameIgnoreCaseStartingWith(String string);

	/**
	 * Get All Active Event List
	 * 
	 * @return
	 */
	List<Event> findAllByIsActiveTrue();

	/**
	 * Get All deleted Event List
	 * @return
	 */
	List<Event> findAllByIsActiveFalseAndIsDeleteTrue();
	
	/**
	 * Get All Active Event List By Fix size Row 
	 * 
	 * @param pageable
	 * @return
	 */
	Page<Event> findAllByIsActiveTrue(Pageable pageable);

	/**
	 * Get Active Event List By Searching Value
	 * 
	 * @param searchStr
	 * @return
	 */
	List<Event> findByNameIgnoreCaseStartingWithAndIsActiveTrue(String searchStr);
	
	/**
	 * Get Active Event By Name
	 * 
	 * @param eventName
	 * @return
	 */
	Event findByNameIgnoreCase(String eventName);

	

}
