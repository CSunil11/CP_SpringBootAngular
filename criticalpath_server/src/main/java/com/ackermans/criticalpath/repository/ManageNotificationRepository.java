package com.ackermans.criticalpath.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ackermans.criticalpath.entity.ManageNotification;


@Repository
public interface ManageNotificationRepository extends JpaRepository<ManageNotification,Long> {
	
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
	@Query("SELECT COUNT(*) FROM ManageNotification WHERE name = :name AND id != :ignoreId")
	long countByNameAndIgnoreId(String name, Long ignoreId);


	/**
	 * Get All Active ManageNotification List
	 * 
	 * @return
	 */
	List<ManageNotification> findAllByIsActiveTrue();

	/**
	 * Get All deleted ManageNotification List
	 * @return
	 */
	List<ManageNotification> findAllByIsActiveFalseAndIsDeleteTrue();
	
	/**
	 * Get All Active Event List By Fix size Row 
	 * 
	 * @param pageable
	 * @return
	 */
	Page<ManageNotification> findAllByIsActiveTrue(Pageable pageable);


	


}
