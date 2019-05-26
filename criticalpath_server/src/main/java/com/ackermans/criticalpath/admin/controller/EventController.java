package com.ackermans.criticalpath.admin.controller;

import java.io.IOException;
import java.sql.SQLException;

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
import org.springframework.web.multipart.MultipartFile;

import com.ackermans.criticalpath.common.dto.ApiResponse;
import com.ackermans.criticalpath.common.dto.CPException;
import com.ackermans.criticalpath.common.service.ResponseGenerator;
import com.ackermans.criticalpath.entity.Event;
import com.ackermans.criticalpath.enums.SuccessStatus;
import com.ackermans.criticalpath.service.EventService;

@RestController
@RequestMapping("/admin/event")
public class EventController {

	@Autowired
	private EventService eventService;
	
	/**
	 * Get all Events
	 * 
	 * @return
	 */
	@GetMapping("/all")
	public ResponseEntity<ApiResponse> getAll(OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(eventService.getAll());
	}
	
	/**
	 * Get all Events by pages
	 * 
	 * @return
	 */
	@GetMapping("/all/{pageNumber}")
	public ResponseEntity<ApiResponse> getAll(@PathVariable int pageNumber,OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(eventService.getAll(pageNumber));
	}
	
	/**
	 * Get all active Events
	 * 
	 * @return
	 */
	@GetMapping("/all-active")
	public ResponseEntity<ApiResponse> getAllActive(OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(eventService.getAllActive());
	}
	
	/**
	 * Get all deleted Events
	 * 
	 * @return
	 */
	@GetMapping("/all-delete")
	public ResponseEntity<ApiResponse> getAllDelete(OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(eventService.getAllDeletedEvent());
	}
	
	/**
	 * Get all active Events by pages 
	 * 
	 * @return
	 */
	@GetMapping("/all-active/{pageNumber}")
	public ResponseEntity<ApiResponse> getAllActive(@PathVariable int pageNumber,OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(eventService.getAllActive(pageNumber));
	}
	
	/**
	 * Search event whose name starts with given search keyword
	 * 
	 * @param searchKeyword
	 * @return
	 */
	@GetMapping("/search/{searchKeyword}")
	public ResponseEntity<ApiResponse> search(@PathVariable String searchKeyword,OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(eventService.search(searchKeyword));
	}
	
	/**
	 * Search event whose name starts with given search keyword
	 * 
	 * @param searchKeyword
	 * @return
	 */
	@GetMapping("/search-active/{searchKeyword}")
	public ResponseEntity<ApiResponse> searchActive(@PathVariable String searchKeyword,OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(eventService.searchActive(searchKeyword));
	}
	
	/**
	 * Activate Event by id
	 * 
	 * @param eventId
	 * @return
	 */
	@PostMapping("/activate")
	public ResponseEntity<ApiResponse> activate(@RequestParam Long eventId, OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(eventService.activate(eventId));
	}
	
	/**
	 * Deactivate Event by id
	 * 
	 * @param eventId
	 * @return
	 */
	@PostMapping("/deactivate")
	public ResponseEntity<ApiResponse> deactivate(@RequestParam Long eventId, OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(eventService.deactivate(eventId));
	}
	
	/**
	 * Save or Update Events
	 * 
	 * @param event
	 * @return
	 * @throws CPException 
	 */
	@PostMapping("/save")
	public ResponseEntity<ApiResponse> save(@RequestBody Event event, OAuth2Authentication oAuth2Authentication) throws CPException {
		
		return ResponseGenerator.successResponse(eventService.verifyAndSave(event));
	}
	
	/**
	 * Verify name exist or not
	 * 
	 * @param name
	 * @return
	 */
	@GetMapping("/isexist/{name}")
	public ResponseEntity<ApiResponse> isExist(@PathVariable String name,OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(eventService.isExists(name));
	}
	
	/**
	 * Get single country data 
	 * 
	 * @param countryId
	 * @return
	 */
	@GetMapping("/getEventData/{eventId}")
	public ResponseEntity<ApiResponse> getEventData(@PathVariable Long eventId, OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(eventService.getEventData(eventId));
	}
	
	/**
	 * Delete Event by id
	 * 
	 * @param eventId
	 * @return
	 */
	@DeleteMapping("/deleteEvent/{eventId}")
	public ResponseEntity<ApiResponse> deleteEvent(@PathVariable Long eventId,OAuth2Authentication oAuth2Authentication) {
		
		eventService.deleteEvent(eventId);
		return ResponseGenerator.successResponse(SuccessStatus.SUCCESS_DELETE);
	}
	
	/**
	 * Restore Event by id
	 * 
	 * @param eventId
	 * @return
	 */
	@GetMapping("/restoreEvent/{eventId}")
	public ResponseEntity<ApiResponse> restoreEvent(@PathVariable Long eventId, OAuth2Authentication oAuth2Authentication) {
		
		eventService.restoreEvent(eventId);
		return ResponseGenerator.successResponse(SuccessStatus.SUCCESS_RESTORE);
	}
	
	/**
	 * Upload event data in bulk
	 * 
	 * @param event
	 * @return
	 * @throws CPException 
	 * @throws SQLException 
	 * @throws IOException 
	 */
	@PostMapping("/upload")
	public ResponseEntity<ApiResponse> upload(@RequestParam(value="data",required=false) MultipartFile data, OAuth2Authentication oAuth2Authentication) throws CPException, SQLException, IOException {
		
		return ResponseGenerator.successResponse(eventService.upload(data));
	}
}
