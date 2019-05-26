package com.ackermans.criticalpath.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ackermans.criticalpath.entity.Permission;

public interface PermissionRepository extends JpaRepository<Permission, Long>{

	/**
	 * Fetch all permissions by order
	 * 
	 * @return
	 */
	List<Permission> findAllByOrderByDisplayOrderAsc();

}
