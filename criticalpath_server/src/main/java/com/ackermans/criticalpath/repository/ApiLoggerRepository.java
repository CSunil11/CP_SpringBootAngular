package com.ackermans.criticalpath.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ackermans.criticalpath.entity.ApiLogger;

@Repository
public interface ApiLoggerRepository extends JpaRepository<ApiLogger, Long> {

	/**
	 * Search records containing transaction or msg id
	 * 
	 * @param searchString
	 * @return
	 */
	@Query("SELECT a FROM ApiLogger a where lower(a.msgId) like lower(concat('%', :searchString, '%')) or lower(a.transactionId) like lower(concat('%', :searchString, '%')) order by createdDateTime desc")
	public List<ApiLogger> findByTransOrMsgId(String searchString);

	public List<ApiLogger> findByCreatedDateTime(Date searchDateFormate);
	
	@Query("SELECT a FROM ApiLogger a where (lower(a.msgId) like lower(concat('%', :searchString, '%')) or lower(a.transactionId) like lower(concat('%', :searchString, '%')) ) AND "
			+ " DATE_FORMAT(a.createdDateTime,'%Y-%m-%d') = DATE_FORMAT(:searchDate, '%Y-%m-%d')"
			+ " order by createdDateTime desc")
			
	public List<ApiLogger> findByTransOrMsgIdAndCreatedDate(String searchString ,String searchDate);

	@Query("SELECT a FROM ApiLogger a where "
			+ " DATE_FORMAT(a.createdDateTime,'%Y-%m-%d') = DATE_FORMAT(:searchDate, '%Y-%m-%d')"
			+ " order by createdDateTime desc")
	public List<ApiLogger> findByCreatedDate(String searchDate);
	
}
