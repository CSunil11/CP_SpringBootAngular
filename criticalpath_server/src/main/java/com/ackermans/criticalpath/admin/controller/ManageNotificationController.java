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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ackermans.criticalpath.common.dto.ApiResponse;
import com.ackermans.criticalpath.common.dto.CPException;
import com.ackermans.criticalpath.common.service.ResponseGenerator;
import com.ackermans.criticalpath.entity.Country;
import com.ackermans.criticalpath.entity.ManageNotification;
import com.ackermans.criticalpath.enums.SuccessStatus;
import com.ackermans.criticalpath.service.ManageNotificationService;


@RestController
@RequestMapping("/admin/managenotification")
public class ManageNotificationController {
	
	@Autowired
	private ManageNotificationService manageNotificationService;
	
	/**
	 * Get All managenotification
	 * 
	 * @return
	 */
	@GetMapping("/all")
	public ResponseEntity<ApiResponse> getAll(OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(manageNotificationService.getAll());
	}
	
	/**
	 * Get All Active managenotification
	 * 
	 * @return
	 */
	@GetMapping("/all-active")
	public ResponseEntity<ApiResponse> getAllActive(OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(manageNotificationService.getAllActive());
	}
	
	/**
	 * Get All is  delete managenotification
	 * 
	 * @return
	 */
	@GetMapping("/all-deleteManageNotification")
	public ResponseEntity<ApiResponse> getAllDeleteCountry(OAuth2Authentication oAuth2Authentication) {
		return ResponseGenerator.successResponse(manageNotificationService.getAllDeletedManageNotification());
	}
	
	/**
	 * Delete record by id
	 * 
	 * @param ManageNotificationId
	 * @return
	 * @throws CPException 
	 */
	@DeleteMapping("/deleteManageNotification/{ManageNotificationId}")
	public ResponseEntity<ApiResponse> deleteCountry(@PathVariable Long ManageNotificationId,OAuth2Authentication oAuth2Authentication) throws CPException {
		
		manageNotificationService.deleteManageNotification(ManageNotificationId);
		return ResponseGenerator.successResponse(SuccessStatus.SUCCESS_DELETE);
	}
	
	/**
	 * Restore record by id
	 * 
	 * @param ManageNotificationId
	 * @return
	 */
	@GetMapping("/restoreManageNotification/{ManageNotificationId}")
	public ResponseEntity<ApiResponse> restoreCountry(@PathVariable Long ManageNotificationId, OAuth2Authentication oAuth2Authentication) {
		
		manageNotificationService.restoreManageNotification(ManageNotificationId);
		return ResponseGenerator.successResponse(SuccessStatus.SUCCESS_RESTORE);
	}
	
	/**
	 * Activate ManageNotification by id
	 * 
	 * @param ManageNotificationId
	 * @return
	 */
	@PostMapping("/activate")
	public ResponseEntity<ApiResponse> activate(@RequestParam Long ManageNotificationId, OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(manageNotificationService.activate(ManageNotificationId));
	}
	
	/**
	 * Deactivate ManageNotification by id
	 * 
	 * @param ManageNotificationId
	 * @return
	 */
	@PostMapping("/deactivate")
	public ResponseEntity<ApiResponse> deactivate(@RequestParam Long ManageNotificationId,OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(manageNotificationService.deactivate(ManageNotificationId));
	}
	
	/**
	 * Save or Update ManageNotification
	 * 
	 * @param ManageNotification
	 * @return
	 * @throws CPException 
	 */
	@PostMapping("/save")
	public ResponseEntity<ApiResponse> save(@RequestBody ManageNotification manageNotification,OAuth2Authentication oAuth2Authentication) throws CPException {
		
		return ResponseGenerator.successResponse(manageNotificationService.verifyAndSave(manageNotification));
	}
	
	/**
	 * Get single ManageNotification data 
	 * 
	 * @param ManageNotificationId
	 * @return
	 */
	@GetMapping("/getManageNotificationData/{ManageNotificationId}")
	public ResponseEntity<ApiResponse> getCountryData(@PathVariable Long ManageNotificationId,OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(manageNotificationService.getManageNotificationData(ManageNotificationId));
	}

}
