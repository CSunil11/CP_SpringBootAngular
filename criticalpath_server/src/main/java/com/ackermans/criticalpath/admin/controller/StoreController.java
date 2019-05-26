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
import com.ackermans.criticalpath.entity.Store;
import com.ackermans.criticalpath.enums.SuccessStatus;
import com.ackermans.criticalpath.service.StoreService;

@RestController
@RequestMapping("/admin/store")
public class StoreController {

	@Autowired
	private StoreService storeService;
	
	/**
	 * Save or Update Store
	 * 
	 * @param store
	 * @return
	 * @throws CPException 
	 */
	@PostMapping("/save")
	public ResponseEntity<ApiResponse> save(@RequestBody Store store, OAuth2Authentication oAuth2Authentication) throws CPException {
		
		return ResponseGenerator.successResponse(storeService.verifyAndSave(store));
	}
	
	/**
	 * Get all stores
	 * 
	 * @return
	 */
	@GetMapping("/all")
	public ResponseEntity<ApiResponse> getAll(OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(storeService.getAll());
	}
	
	/**
	 * Get all stores by pages
	 * 
	 * @return
	 */
	@GetMapping("/all/{pageNumber}")
	public ResponseEntity<ApiResponse> getAll(@PathVariable int pageNumber, OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(storeService.getAll(pageNumber));
	}
	
	/**
	 * Get all active stores
	 * 
	 * @return
	 */
	@GetMapping("/all-active")
	public ResponseEntity<ApiResponse> getAllActive(OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(storeService.getAllActive());
	}
	
	/**
	 * Get all active stores by pages
	 * 
	 * @return
	 */
	@GetMapping("/all-active/{pageNumber}")
	public ResponseEntity<ApiResponse> getAllActive(@PathVariable int pageNumber, OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(storeService.getAllActive(pageNumber));
	}
	
	/**
	 * Get all deleted stores by pages
	 * 
	 * @return
	 */
	@GetMapping("/all-delete/{pageNumber}")
	public ResponseEntity<ApiResponse> getAllDeletedBrand(@PathVariable int pageNumber, OAuth2Authentication oAuth2Authentication) {

		return ResponseGenerator.successResponse(storeService.getAllDeletedStore(pageNumber));
	}
	
	/**
	 * Activate Store By Id
	 * 
	 * @param storeId
	 * @return
	 */
	@PostMapping("/activate")
	public ResponseEntity<ApiResponse> activate(@RequestParam Long storeId, OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(storeService.activate(storeId));
	}
	
	/**
	 * Deactivate Store By Id
	 * 
	 * @param storeId
	 * @return
	 */
	@PostMapping("/deactivate")
	public ResponseEntity<ApiResponse> deactivate(@RequestParam Long storeId, OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(storeService.deactivate(storeId));
	}
	
	
	/**
	 * Search store by name
	 * 
	 * @param searchKeyword
	 * @return
	 */
	@GetMapping("/search/{searchKeyword}")
	public ResponseEntity<ApiResponse> search(@PathVariable String searchKeyword, OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(storeService.search(searchKeyword));
	}
	
	/**
	 * Search active store whose name starts with given search keyword by pages
	 * 
	 * @param searchKeyword
	 * @return
	 */
	@GetMapping("/search/{searchKeyword}/{pageNumber}")
	public ResponseEntity<ApiResponse> searchActive(@PathVariable String searchKeyword, @PathVariable int pageNumber, OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(storeService.searchActive(searchKeyword, pageNumber));
	}
	
	/**
	 * Search active store whose name starts with given search keyword by pages
	 * 
	 * @param searchKeyword
	 * @return
	 */
	@GetMapping("/search-active/{searchKeyword}/{pageNumber}")
	public ResponseEntity<ApiResponse> searchActiveTrue(@PathVariable String searchKeyword, @PathVariable int pageNumber, OAuth2Authentication oAuth2Authentication) {

		return ResponseGenerator.successResponse(storeService.searchActiveTrue(searchKeyword, pageNumber));
	}
	
	/**
	 * Search active store whose name starts with given search keyword by pages
	 * 
	 * @param searchKeyword
	 * @return
	 */
	@GetMapping("/search-delete/{searchKeyword}/{pageNumber}")
	public ResponseEntity<ApiResponse> searchActiveFalse(@PathVariable String searchKeyword, @PathVariable int pageNumber, OAuth2Authentication oAuth2Authentication) {

		return ResponseGenerator.successResponse(storeService.searchActiveFalse(searchKeyword, pageNumber));
	}
	
	/**
	 * Get Store By code
	 * 
	 * @param code
	 * @return
	 */
	@GetMapping("/getbycode/{code}")
	public ResponseEntity<ApiResponse> getByCode(@PathVariable String code, OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(storeService.getByCode(code));
	}
	
	/**
	 * Get Store List By country Id
	 * 
	 * @param countryId
	 * @return
	 */
	@GetMapping("/getbycountry/{countryId}")
	public ResponseEntity<ApiResponse> getByCountry(@PathVariable Long countryId, OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(storeService.getByCountry(countryId));
	}
	
	/**
	 * Get Store List By Brand Id
	 * 
	 * @param brandId
	 * @return
	 */
	@GetMapping("/getbybrand/{brandId}")
	public ResponseEntity<ApiResponse> getByBrand(@PathVariable Long brandId, OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(storeService.getByBrand(brandId));
	}
	
	/**
	 * Get Store By Store Id
	 * 
	 * @param storeId
	 * @return
	 */
	@GetMapping("/get/{storeId}")
	public ResponseEntity<ApiResponse> getByStore(@PathVariable Long storeId, OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(storeService.get(storeId));
	}
	
	/**
	 * Delete Store by id
	 * 
	 * @param storeId
	 * @return
	 * @throws CPException 
	 */
	@DeleteMapping("/deleteStore/{storeId}")
	public ResponseEntity<ApiResponse> deleteStore(@PathVariable Long storeId, OAuth2Authentication oAuth2Authentication) throws CPException {
		
		storeService.deleteStore(storeId);
		return ResponseGenerator.successResponse(SuccessStatus.SUCCESS_DELETE);
	}
	
	/**
	 * Restore Event by id
	 * 
	 * @param eventId
	 * @return
	 */
	@GetMapping("/restoreStore/{storeId}")
	public ResponseEntity<ApiResponse> restoreStore(@PathVariable Long storeId, OAuth2Authentication oAuth2Authentication) {
		
		storeService.restoreStore(storeId);
		return ResponseGenerator.successResponse(SuccessStatus.SUCCESS_RESTORE);
	}
}
