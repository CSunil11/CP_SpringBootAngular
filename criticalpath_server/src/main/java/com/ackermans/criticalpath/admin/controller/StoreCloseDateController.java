package com.ackermans.criticalpath.admin.controller;

import java.text.ParseException;

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

import com.ackermans.criticalpath.admin.dto.StoreCloseDateRequest;
import com.ackermans.criticalpath.common.dto.ApiResponse;
import com.ackermans.criticalpath.common.dto.CPException;
import com.ackermans.criticalpath.common.service.ResponseGenerator;
import com.ackermans.criticalpath.enums.SuccessStatus;
import com.ackermans.criticalpath.service.StoreCloseDateService;

@RestController
@RequestMapping("/admin/storeclosedate")
public class StoreCloseDateController {

	@Autowired
	private StoreCloseDateService storeCloseDateService;

	/**
	 * Save or Update Store Close Date
	 * 
	 * @param storeCloseDateRequest
	 * @return
	 * @throws CPException 
	 * @throws ParseException 
	 */
	@PostMapping("/save")
	public ResponseEntity<ApiResponse> save(@RequestBody StoreCloseDateRequest storeCloseDateRequest, OAuth2Authentication oAuth2Authentication) throws CPException, ParseException {
		
		return ResponseGenerator.successResponse(storeCloseDateService.verifyAndSave(storeCloseDateRequest));
	}
	
	/**
	 * Get all StoreCloseDate
	 * @return
	 */
	@GetMapping("/all")
	public ResponseEntity<ApiResponse> getAll(OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(storeCloseDateService.getAll());
	}
	
	/**
	 * Get StoreClose by date
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	@GetMapping("/search/{date}")
	public ResponseEntity<ApiResponse> search(@PathVariable String date, OAuth2Authentication oAuth2Authentication) throws ParseException {
		
		return ResponseGenerator.successResponse(storeCloseDateService.getByCloseDate(date));
	}
	
	/**
	 * Get All Active Store Close Date List
	 * 
	 * @return
	 */
	@GetMapping("/all-active")
	public ResponseEntity<ApiResponse> getAllActive(OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(storeCloseDateService.getAllActive());
	}
	
	/**
	 * Get All deleted Store Close Date List
	 * 
	 * @return
	 */
	@GetMapping("/all-delete")
	public ResponseEntity<ApiResponse> getAllDeletedStoreClosed(OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(storeCloseDateService.getAllDeletedStoreClosed());
	}
	
	/**
	 * Activate Store close Date By Id
	 * 
	 * @param storeCloseId
	 * @return
	 */
	@PostMapping("/activate")
	public ResponseEntity<ApiResponse> activate(@RequestParam Long storeCloseId) {
		
		return ResponseGenerator.successResponse(storeCloseDateService.activate(storeCloseId));
	}
	
	/**
	 * Deactivate StoreCloseDate By Id
	 * 
	 * @param storeCloseId
	 * @return
	 */
	@PostMapping("/deactivate")
	public ResponseEntity<ApiResponse> deactivate(@RequestParam Long storeCloseId, OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(storeCloseDateService.deactivate(storeCloseId));
	}
	
	/**
	 * Get StoreCloseDate List By StoreID
	 * 
	 * @param storeId
	 * @return
	 */
	@GetMapping("/getbystore/{storeId}")
	public ResponseEntity<ApiResponse> getByCountry(@PathVariable Long storeId, OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(storeCloseDateService.getByStore(storeId));
	}
	
	/**
	 * get closed date count by date
	 * @param storeId
	 * @param newdate
	 * @param stokedate
	 * @param oAuth2Authentication
	 * @return
	 */
	@GetMapping("/getbystore/{storeId}/{newdate}/{stokedate}")
	public ResponseEntity<ApiResponse> getByDate(@PathVariable Long storeId, 
			@PathVariable String newdate, 
			@PathVariable String stokedate, 
			OAuth2Authentication oAuth2Authentication) {
		
		int check = storeCloseDateService.getByDate(storeId , newdate ,stokedate);
		
		return ResponseGenerator.successResponse(check);
	}
	
	/**
	 * Get Closed Date list by date
	 * 
	 * @param storeId
	 * @param newdate
	 * @param stokedate
	 * @param oAuth2Authentication
	 * @return
	 */
	@GetMapping("/getcloseddatebystore/{storeId}/{newdate}/{stokedate}")
	public ResponseEntity<ApiResponse> getClosedDateListByDate(@PathVariable Long storeId, 
			@PathVariable String newdate, 
			@PathVariable String stokedate, 
			OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(storeCloseDateService.getClosedDateListByDate(storeId , newdate ,stokedate));
	}
	
	/**
	 * Get StoreCloseDate List By StoreID
	 * 
	 * @param closeDateId
	 * @return
	 */
	@GetMapping("/getStoreClosedData/{closedDateId}")
	public ResponseEntity<ApiResponse> getStoreClosedData(@PathVariable Long closedDateId, OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(storeCloseDateService.getStoreClosedData(closedDateId));
	}
	
	/**
	 * Delete closed Date by id
	 * 
	 * @param closeDateId
	 * @return
	 * @throws CPException 
	 */
	@DeleteMapping("/deleteCloseDate/{closeDateId}")
	public ResponseEntity<ApiResponse> deleteCloseDate(@PathVariable Long closeDateId, OAuth2Authentication oAuth2Authentication) throws CPException {
		
		storeCloseDateService.deleteCloseDate(closeDateId);
		return ResponseGenerator.successResponse(SuccessStatus.SUCCESS_DELETE);
	}
	
	/**
	 * Restore Event by id
	 * 
	 * @param eventId
	 * @return
	 */
	@GetMapping("/restoreCloseDate/{closeDateId}")
	public ResponseEntity<ApiResponse> restoreCloseDate(@PathVariable Long closeDateId, OAuth2Authentication oAuth2Authentication) {
		
		storeCloseDateService.restoreCloseDate(closeDateId);
		return ResponseGenerator.successResponse(SuccessStatus.SUCCESS_RESTORE);
	}
}
