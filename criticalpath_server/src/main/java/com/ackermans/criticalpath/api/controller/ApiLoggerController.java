package com.ackermans.criticalpath.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ackermans.criticalpath.common.dto.ApiResponse;
import com.ackermans.criticalpath.common.service.ResponseGenerator;
import com.ackermans.criticalpath.service.ApiLoggerService;

@RestController
@RequestMapping("/admin/apiLogger")
public class ApiLoggerController {

	@Autowired
	ApiLoggerService apiLoggerService;
	
	@GetMapping(path = "/searchByTransOrMsgId/{searchString}/{searchDate}")
	public ResponseEntity<ApiResponse> searchByTransOrMsgId(@PathVariable String searchString, @PathVariable String searchDate) {
		return ResponseGenerator.successResponse(apiLoggerService.findByTransOrMsgId(searchString , searchDate));
		
	}
}
