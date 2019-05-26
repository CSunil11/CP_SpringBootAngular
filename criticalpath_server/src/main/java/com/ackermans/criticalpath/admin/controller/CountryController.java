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
import com.ackermans.criticalpath.entity.Country;
import com.ackermans.criticalpath.enums.SuccessStatus;
import com.ackermans.criticalpath.service.CountryService;

@RestController
@RequestMapping("/admin/country")
public class CountryController {

	@Autowired
	private CountryService countryService;
	
	/**
	 * Get All Countries
	 * 
	 * @return
	 */
	@GetMapping("/all")
	public ResponseEntity<ApiResponse> getAll(OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(countryService.getAll());
	}
	
	/**
	 * Get All Active Countries
	 * 
	 * @return
	 */
	@GetMapping("/all-active")
	public ResponseEntity<ApiResponse> getAllActive(OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(countryService.getAllActive());
	}
	
	/**
	 * Get All is  delete Countries
	 * 
	 * @return
	 */
	@GetMapping("/all-deleteCountry")
	public ResponseEntity<ApiResponse> getAllDeleteCountry(OAuth2Authentication oAuth2Authentication) {
		return ResponseGenerator.successResponse(countryService.getAllDeletedCountry());
	}
	
	/**
	 * Search countries whose name starts with given search keyword
	 * 
	 * @param searchKeyword
	 * @return
	 */
	@GetMapping("/search/{searchKeyword}")
	public ResponseEntity<ApiResponse> search(@PathVariable String searchKeyword,OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(countryService.search(searchKeyword));
	}
	
	/**
	 * Search active countries whose name starts with given search keyword
	 * 
	 * @param searchKeyword
	 * @return
	 */
	@GetMapping("/search-active/{searchKeyword}")
	public ResponseEntity<ApiResponse> searchActive(@PathVariable String searchKeyword,OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(countryService.searchActive(searchKeyword));
	}
	
	/**
	 * Activate country by id
	 * 
	 * @param countryId
	 * @return
	 */
	@PostMapping("/activate")
	public ResponseEntity<ApiResponse> activate(@RequestParam Long countryId, OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(countryService.activate(countryId));
	}
	
	/**
	 * Deactivate country by id
	 * 
	 * @param countryId
	 * @return
	 */
	@PostMapping("/deactivate")
	public ResponseEntity<ApiResponse> deactivate(@RequestParam Long countryId,OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(countryService.deactivate(countryId));
	}
	
	/**
	 * Save or Update country
	 * 
	 * @param country
	 * @return
	 * @throws CPException 
	 */
	@PostMapping("/save")
	public ResponseEntity<ApiResponse> save(@RequestBody Country country,OAuth2Authentication oAuth2Authentication) throws CPException {
		
		return ResponseGenerator.successResponse(countryService.verifyAndSave(country));
	}
	
	/**
	 * Get single country data 
	 * 
	 * @param countryId
	 * @return
	 */
	@GetMapping("/getCountryData/{countryId}")
	public ResponseEntity<ApiResponse> getCountryData(@PathVariable Long countryId,OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(countryService.getCountryData(countryId));
	}
	
	/**
	 * Upload country data in bulk
	 * 
	 * @param country
	 * @return
	 * @throws CPException 
	 * @throws SQLException 
	 * @throws IOException 
	 */
	@PostMapping("/upload")
	public ResponseEntity<ApiResponse> upload(@RequestParam(value="data",required=false) MultipartFile data, OAuth2Authentication oAuth2Authentication) throws CPException, SQLException, IOException {
		
		return ResponseGenerator.successResponse(countryService.upload(data));
	}
	
	/**
	 * Delete record by id
	 * 
	 * @param countryId
	 * @return
	 * @throws CPException 
	 */
	@DeleteMapping("/deleteCountry/{countryId}")
	public ResponseEntity<ApiResponse> deleteCountry(@PathVariable Long countryId,OAuth2Authentication oAuth2Authentication) throws CPException {
		
		countryService.deleteCountry(countryId);
		return ResponseGenerator.successResponse(SuccessStatus.SUCCESS_DELETE);
	}
	
	/**
	 * Restore record by id
	 * 
	 * @param countryId
	 * @return
	 */
	@GetMapping("/restoreCountry/{countryId}")
	public ResponseEntity<ApiResponse> restoreCountry(@PathVariable Long countryId, OAuth2Authentication oAuth2Authentication) {
		
		countryService.restoreCountry(countryId);
		return ResponseGenerator.successResponse(SuccessStatus.SUCCESS_RESTORE);
	}
	
	/**
	 * Get all active Province by pages 
	 * 
	 * @param pageNumber
	 * @param oAuth2Authentication
	 * @return
	 */
	@GetMapping("/all-active/{pageNumber}")
	public ResponseEntity<ApiResponse> getAllActive(@PathVariable int pageNumber,OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(countryService.getAllActive(pageNumber));
	}
	
	/**
	 * Get all deleted Province by pages
	 * 
	 * @return
	 */
	@GetMapping("/all-deleteCountry/{pageNumber}")
	public ResponseEntity<ApiResponse> getAllDeleteProvince(@PathVariable int pageNumber, OAuth2Authentication oAuth2Authentication) {

		return ResponseGenerator.successResponse(countryService.getAllDeletedCountry(pageNumber));
	}
	
	/**
	 * Search active LocDiv whose name starts with given search keyword by pages
	 * 
	 * @param searchKeyword
	 * @return
	 */
	@GetMapping("/search-active/{searchKeyword}/{pageNumber}")
	public ResponseEntity<ApiResponse> searchActiveTrue(@PathVariable String searchKeyword, @PathVariable int pageNumber, OAuth2Authentication oAuth2Authentication) {

		return ResponseGenerator.successResponse(countryService.searchActiveTrue(searchKeyword, pageNumber));
	}
	
	/**
	 * Search active store whose name starts with given search keyword by pages
	 * 
	 * @param searchKeyword
	 * @return
	 */
	@GetMapping("/search-delete/{searchKeyword}/{pageNumber}")
	public ResponseEntity<ApiResponse> searchActiveFalse(@PathVariable String searchKeyword, @PathVariable int pageNumber, OAuth2Authentication oAuth2Authentication) {

		return ResponseGenerator.successResponse(countryService.searchActiveFalse(searchKeyword, pageNumber));
	}
}
