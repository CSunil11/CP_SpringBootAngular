package com.ackermans.criticalpath.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ackermans.criticalpath.common.dto.ApiResponse;
import com.ackermans.criticalpath.common.dto.CPException;
import com.ackermans.criticalpath.common.service.ResponseGenerator;
import com.ackermans.criticalpath.entity.DaysOfWeek;
import com.ackermans.criticalpath.entity.Event;
import com.ackermans.criticalpath.enums.SuccessStatus;
import com.ackermans.criticalpath.service.DaysOfWeekService;

@RestController
@RequestMapping("/admin/daysofweek/")
public class DaysOfWeekController {

	@Autowired
	private DaysOfWeekService daysOfWeekService;
	
	/**
	 * Get all days of week
	 * 
	 * @return
	 */
	@GetMapping("/all")
	public ResponseEntity<ApiResponse> getAll(OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(daysOfWeekService.all());
	}
	
	/**
	 * Get All Active days of week
	 * 
	 * @return
	 */
	@GetMapping("/all-active")
	public ResponseEntity<ApiResponse> getAllActive(OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(daysOfWeekService.getAllActive());
	}
	
	/**
	 * Get All deleted days of week
	 * 
	 * @return
	 */
	@GetMapping("/all-delete")
	public ResponseEntity<ApiResponse> getAllDeleteDaysOfWeek(OAuth2Authentication oAuth2Authentication) {
		return ResponseGenerator.successResponse(daysOfWeekService.getAllDeletedDaysOfWeek());
	}
	
	/**
	 * Get single daysOfWeek data by id
	 * 
	 * @param oAuth2Authentication
	 * @param daysOfWeekId
	 * @return
	 */
	@GetMapping("/getDaysOfWeekData/{daysOfWeekId}")
	public ResponseEntity<ApiResponse> getEventData(@PathVariable Long daysOfWeekId, OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(daysOfWeekService.getDaysOfWeekData(daysOfWeekId));
	}
	
	/**
	 * Update daysOfWeek
	 * 
	 * @param daysOfWeek
	 * @param oAuth2Authentication
	 * @return
	 * @throws CPException
	 */
	@PostMapping("/save")
	public ResponseEntity<ApiResponse> save(@RequestBody DaysOfWeek daysOfWeek, OAuth2Authentication oAuth2Authentication) throws CPException {
		return ResponseGenerator.successResponse(daysOfWeekService.verifyAndSave(daysOfWeek));
	}
	
	/**
	 * Delete DaysOfWeek by id
	 * 
	 * @param daysOfWeekId
	 * @param oAuth2Authentication
	 * @return
	 */
	@DeleteMapping("/deleteDaysOfWeek/{DaysOfWeekId}")
	public ResponseEntity<ApiResponse> deleteDaysOfWeek(@PathVariable Long DaysOfWeekId,OAuth2Authentication oAuth2Authentication) {
		daysOfWeekService.deleteDaysOfWeek(DaysOfWeekId);
		return ResponseGenerator.successResponse(SuccessStatus.SUCCESS_DELETE);
	}
	
	/**
	 * Restore DaysOfWeek by id
	 * 
	 * @param DaysOfWeekId
	 * @return
	 */
	@GetMapping("/restoreDaysOfWeek/{DaysOfWeekId}")
	public ResponseEntity<ApiResponse> restoreDaysOfWeek(@PathVariable Long DaysOfWeekId, OAuth2Authentication oAuth2Authentication) {
		
		daysOfWeekService.restoreDaysOfWeek(DaysOfWeekId);
		return ResponseGenerator.successResponse(SuccessStatus.SUCCESS_RESTORE);
	}
}
