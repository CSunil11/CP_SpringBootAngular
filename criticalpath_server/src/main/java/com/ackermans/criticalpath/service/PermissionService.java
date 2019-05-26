package com.ackermans.criticalpath.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ackermans.criticalpath.entity.Permission;
import com.ackermans.criticalpath.repository.PermissionRepository;

@Service
public class PermissionService {

	@Autowired
	private PermissionRepository permissionRepository;
	
	public List<Permission> getAllPermissions() {
		return  permissionRepository.findAllByOrderByDisplayOrderAsc();
	}
}