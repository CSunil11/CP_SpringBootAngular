package com.ackermans.criticalpath.aop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ackermans.criticalpath.aop.service.AuditService;
import com.ackermans.criticalpath.common.dto.ApiResponse;
import com.ackermans.criticalpath.common.service.ResponseGenerator;
import com.ackermans.criticalpath.service.ApiLoggerService;

@RestController
@RequestMapping("/admin/aopLogger")
public class AopController {
	
	@Autowired
	AuditService auditService;
	
	@GetMapping(path = "/searchByDate/{searchDate}")
	public ResponseEntity<ApiResponse> searchByTransOrMsgId( @PathVariable String searchDate) {
		return ResponseGenerator.successResponse(auditService.findByDate(searchDate));
		
	}
	
}
