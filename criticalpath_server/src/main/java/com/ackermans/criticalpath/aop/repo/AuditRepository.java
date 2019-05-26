package com.ackermans.criticalpath.aop.repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.ackermans.criticalpath.entity.AuditLog;

@Repository
public interface AuditRepository extends JpaRepository<AuditLog, Long> {

	@Query("SELECT a FROM AuditLog a where "
			+ " DATE_FORMAT(a.createdDateTime,'%Y-%m-%d') = DATE_FORMAT(:searchDate, '%Y-%m-%d')"
			+ " order by createdDateTime desc")
	public List<AuditLog> findByCreatedDateTime(String searchDate);
	

}