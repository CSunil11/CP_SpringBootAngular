package com.ackermans.criticalpath.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ackermans.criticalpath.entity.Webhook;

@Repository
public interface WebhookRepositoy extends JpaRepository<Webhook, Long> {

	/**
	 * Get total Count Webhook by url
	 * 
	 * @param url
	 * @return
	 */
	int countByUrlIgnoreCase(String url);

	/**
	 * Get total Count Webhook by url but ignore id
	 * 
	 * @param url
	 * @param id
	 * @return
	 */
	@Query("SELECT COUNT(*) FROM Webhook WHERE url = :url AND id != :id")
	int countByUrlAndIgnoreId(String url, Long id);

	/**
	 * Get All Active Webhook List
	 * 
	 * @return
	 */
	List<Webhook> findAllByIsActiveTrue();
	
	/**
	 * Get All deleted Webhook List
	 * @return
	 */
	List<Webhook> findAllByIsActiveFalseAndIsDeleteTrue();
	
	/**
	 * Get All Active With Fix Size Row
	 * 
	 * @param pageNumber
	 * @return
	 */
	Page<Webhook> findAllByIsActiveTrue(Pageable pageable);

	/**
	 * Get List By Searching parameter
	 * 
	 * @return
	 */
	List<Webhook> findByUrlIgnoreCaseStartingWith(String url);

	/**
	 * Get Webhook By EventId
	 * 
	 * @param eventId
	 * @return
	 */
	List<Webhook> findByEvent_Id(Long eventId);

	

}
