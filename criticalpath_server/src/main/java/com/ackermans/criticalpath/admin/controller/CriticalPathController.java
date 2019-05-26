package com.ackermans.criticalpath.admin.controller;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
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
import com.ackermans.criticalpath.entity.CriticalPath;
import com.ackermans.criticalpath.enums.ErrorStatus;
import com.ackermans.criticalpath.enums.SuccessStatus;
import com.ackermans.criticalpath.service.CriticalPathService;
import com.fasterxml.jackson.databind.JsonNode;

@RestController
@RequestMapping("/admin/criticalPath")
public class CriticalPathController {

	@Autowired
	CriticalPathService criticalPathService;
	
	/**
	 * Save or Update Critical Path
	 * 
	 * @param criticalPath
	 * @return
	 * @throws CPException 
	 * @throws ParseException 
	 */
	@PostMapping("/save")
	public ResponseEntity<ApiResponse> save(@RequestBody CriticalPath criticalPath, OAuth2Authentication oAuth2Authentication) throws CPException, ParseException {
		
		return ResponseGenerator.successResponse(criticalPathService.verifyAndSave(criticalPath, oAuth2Authentication));
	}
	
	/**
	 * Get all CriticalPath
	 * @return
	 */
	@GetMapping("/all")
	public ResponseEntity<ApiResponse> getAll(OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(criticalPathService.getAll());
	}
	
	/**
	 * Get All Active Critical PAth List
	 * 
	 * @return
	 */
	@GetMapping("/all-active")
	public ResponseEntity<ApiResponse> getAllActive(OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(criticalPathService.getAllActive());
	}
	
	/**
	 * Get All Deleted Critical PAth List
	 * 
	 * @return
	 */
	@GetMapping("/all-delete")
	public ResponseEntity<ApiResponse> getAllDeletedCriticalPath(OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(criticalPathService.getAllDeletedCriticalPath());
	}
	
	/**
	 * Get Critical PAth List By criticalPathId
	 * 
	 * @param criticalPathId
	 * @return
	 */
	@GetMapping("/getCriticalPathData/{criticalPathId}")
	public ResponseEntity<ApiResponse> getCriticalPathData(@PathVariable Long criticalPathId, OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(criticalPathService.getCriticalPathData(criticalPathId));
	}
	
	/**
	 * Delete Critical PAth
	 * 
	 * @param criticalPathId
	 * @return
	 * @throws CPException 
	 */
	@DeleteMapping("/deleteCriticalPath/{criticalPathId}")
	public ResponseEntity<ApiResponse> deleteCriticalPath(@PathVariable Long criticalPathId, OAuth2Authentication oAuth2Authentication) throws CPException {
		
		criticalPathService.deleteCriticalPath(criticalPathId);
		return ResponseGenerator.successResponse(SuccessStatus.SUCCESS_DELETE);
	}
	
	/**
	 * Restore Critical PAth
	 * 
	 * @param criticalPathId
	 * @return
	 */
	@GetMapping("/restoreCriticalPath/{criticalPathId}")
	public ResponseEntity<ApiResponse> restoreCriticalPath(@PathVariable Long criticalPathId, OAuth2Authentication oAuth2Authentication) {
		
		criticalPathService.restoreCriticalPath(criticalPathId);
		return ResponseGenerator.successResponse(SuccessStatus.SUCCESS_RESTORE);
	}
	
	/**
	 * Get criticalPath of stockTakeCycle
	 * 
	 * @param stockTakeCycleId
	 * @param oAuth2Authentication
	 * @return
	 */
	@GetMapping("/getCriticalPathOfStockTakeCycle/{stockTakeCycleId}")
	public ResponseEntity<ApiResponse> getCriticalPathOfStockTakeCycle(@PathVariable Long stockTakeCycleId, OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(criticalPathService.getCriticalPathOfStockTakeCycle(stockTakeCycleId));
	}
	
	@PostMapping("/updateCriticalPathStatus")
	public ResponseEntity<ApiResponse> save(@RequestBody JsonNode request) throws CPException {
		
		try {
			//If request is null then it's invalid request
			if(request != null) {
				
				return  ResponseGenerator.successResponse(criticalPathService.updateCriticalPathStatus(request));
			} else {
				throw new CPException(ErrorStatus.INVALID_REQUEST);
			}
		} catch (Exception ex) {
			
			throw new CPException(ErrorStatus.INVALID_REQUEST);
		}
	}
}
