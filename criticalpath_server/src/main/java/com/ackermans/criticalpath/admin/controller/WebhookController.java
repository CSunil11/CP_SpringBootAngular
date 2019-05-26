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
import com.ackermans.criticalpath.entity.Webhook;
import com.ackermans.criticalpath.enums.SuccessStatus;
import com.ackermans.criticalpath.service.WebhookService;

@RestController
@RequestMapping("/admin/webhook")
public class WebhookController {

	@Autowired
	private WebhookService webhookService;
	
	/**
	 * Save or Update Webhook
	 * 
	 * @param webhook
	 * @return
	 * @throws CPException 
	 */
	@PostMapping("/save")
	public ResponseEntity<ApiResponse> save(@RequestBody Webhook webhook,OAuth2Authentication oAuth2Authentication) throws CPException {
		
		return ResponseGenerator.successResponse(webhookService.verifyAndSave(webhook));
	}
	
	/**
	 * Get all Webhooks
	 * 
	 * @return
	 */
	@GetMapping("/all")
	public ResponseEntity<ApiResponse> getAll(OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(webhookService.getAll());
	}
	
	/**
	 * Get all Webhooks by pages
	 * 
	 * @return
	 */
	@GetMapping("/all/{pageNumber}")
	public ResponseEntity<ApiResponse> getAll(@PathVariable int pageNumber,OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(webhookService.getAll(pageNumber));
	}
	
	/**
	 * Get all active webhooks
	 * 
	 * @return
	 */
	@GetMapping("/all-active")
	public ResponseEntity<ApiResponse> getAllActive(OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(webhookService.getAllActive());
	}
	
	/**
	 * Get all deleted webhooks
	 * 
	 * @return
	 */
	@GetMapping("/all-delete")
	public ResponseEntity<ApiResponse> getAllDeletedWebhook(OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(webhookService.getAllDeletedWebhook());
	}
	
	/**
	 * Get all active webhooks by pages
	 * 
	 * @return
	 */
	@GetMapping("/all-active/{pageNumber}")
	public ResponseEntity<ApiResponse> getAllActive(@PathVariable int pageNumber,OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(webhookService.getAllActive(pageNumber));
	}
	
	/**
	 * Activate Webhook By Id
	 * 
	 * @param webhookId
	 * @return
	 */
	@PostMapping("/activate")
	public ResponseEntity<ApiResponse> activate(@RequestParam Long webhookId, OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(webhookService.activate(webhookId));
	}
	
	/**
	 * Deactivate Webhook By Id
	 * 
	 * @param webhookId
	 * @return
	 */
	@PostMapping("/deactivate")
	public ResponseEntity<ApiResponse> deactivate(@RequestParam Long webhookId, OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(webhookService.deactivate(webhookId));
	}
	
	
	/**
	 * Search webhook by URL starting with
	 * 
	 * @param searchKeyword
	 * @return
	 */
	@GetMapping("/search/{searchKeyword}")
	public ResponseEntity<ApiResponse> search(@PathVariable String searchKeyword, OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(webhookService.search(searchKeyword));
	}
	
	/**
	 * Get Webhook List By event Id
	 * 
	 * @param eventId
	 * @return
	 */
	@GetMapping("/getbyevent/{eventId}")
	public ResponseEntity<ApiResponse> getByEvent(@PathVariable Long eventId, OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(webhookService.getByEvent(eventId));
	}
	
	/**
	 * Get single webhook data 
	 * 
	 * @param webhookId
	 * @return
	 */
	@GetMapping("/getWebhookData/{webhookId}")
	public ResponseEntity<ApiResponse> getWebhookData(@PathVariable Long webhookId, OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(webhookService.get(webhookId));
	}
	
	/**
	 * Get Webhook By Webhook Id
	 * 
	 * @param storeId
	 * @return
	 */
	@GetMapping("/get/{webhookId}")
	public ResponseEntity<ApiResponse> getByStore(@PathVariable Long webhookId,OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(webhookService.get(webhookId));
	}
	
	/**
	 * Delete Webhook by id
	 * 
	 * @param webhookId
	 * @return
	 */
	@DeleteMapping("/deleteWebhook/{webhookId}")
	public ResponseEntity<ApiResponse> deleteWebhook(@PathVariable Long webhookId,OAuth2Authentication oAuth2Authentication) {
		
		webhookService.deleteWebhook(webhookId);
		return ResponseGenerator.successResponse(SuccessStatus.SUCCESS_DELETE);
	}
	
	/**
	 * Restore Event by id
	 * 
	 * @param eventId
	 * @return
	 */
	@GetMapping("/restoreWebhook/{webhookId}")
	public ResponseEntity<ApiResponse> restoreWebhook(@PathVariable Long webhookId, OAuth2Authentication oAuth2Authentication) {
		
		webhookService.restoreWebhook(webhookId);
		return ResponseGenerator.successResponse(SuccessStatus.SUCCESS_RESTORE);
	}
}
