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
import com.ackermans.criticalpath.entity.Brand;
import com.ackermans.criticalpath.enums.SuccessStatus;
import com.ackermans.criticalpath.service.BrandService;

@RestController
@RequestMapping("/admin/brand")
public class BrandController {

	@Autowired
	private BrandService brandService;
	
	/**
	 * Get all Brands
	 * 
	 * @return
	 */
	@GetMapping("/all")
	public ResponseEntity<ApiResponse> getAll(OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(brandService.getAll());
	}
	
	/**
	 * Get all Brands by pages
	 * 
	 * @return
	 */
	@GetMapping("/all/{pageNumber}")
	public ResponseEntity<ApiResponse> getAll(@PathVariable int pageNumber, OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(brandService.getAll(pageNumber));
	}
	
	/**
	 * Get all active Brands
	 * 
	 * @return
	 */
	@GetMapping("/all-active")
	public ResponseEntity<ApiResponse> getAllActive(OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(brandService.getAllActive());
	}

	/**
	 * Get all active Brands by pages
	 * 
	 * @return
	 */
	@GetMapping("/all-active/{pageNumber}")
	public ResponseEntity<ApiResponse> getAllActive(@PathVariable int pageNumber, OAuth2Authentication oAuth2Authentication) {

		return ResponseGenerator.successResponse(brandService.getAllActive(pageNumber));
	}
	
	/**
	 * Get all deleted Brands by pages
	 * 
	 * @return
	 */
	@GetMapping("/all-delete/{pageNumber}")
	public ResponseEntity<ApiResponse> getAllDeletedBrand(@PathVariable int pageNumber, OAuth2Authentication oAuth2Authentication) {

		return ResponseGenerator.successResponse(brandService.getAllDeletedBrand(pageNumber));
	}
	
	/**
	 * Save or Update Brand
	 * 
	 * @param brand
	 * @return
	 * @throws CPException 
	 */
	@PostMapping("/save")
	public ResponseEntity<ApiResponse> save(@RequestBody Brand brand, OAuth2Authentication oAuth2Authentication) throws CPException {

		return ResponseGenerator.successResponse(brandService.verifyAndSave(brand));
	}
	
	/**
	 * Search brands whose name starts with given search keyword
	 * 
	 * @param searchKeyword
	 * @return
	 */
	@GetMapping("/search/{searchKeyword}")
	public ResponseEntity<ApiResponse> search(@PathVariable String searchKeyword, OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(brandService.search(searchKeyword));
	}
	
	/**
	 * Search active brands whose name starts with given search keyword by pages
	 * 
	 * @param searchKeyword
	 * @return
	 */
	@GetMapping("/search/{searchKeyword}/{pageNumber}")
	public ResponseEntity<ApiResponse> searchActive(@PathVariable String searchKeyword, @PathVariable int pageNumber, OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(brandService.searchActive(searchKeyword, pageNumber));
	}
	
	/**
	 * Search active brands whose name starts with given search keyword by pages
	 * 
	 * @param searchKeyword
	 * @return
	 */
	@GetMapping("/search-active/{searchKeyword}/{pageNumber}")
	public ResponseEntity<ApiResponse> searchActiveTrue(@PathVariable String searchKeyword, @PathVariable int pageNumber, OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(brandService.searchActiveTrue(searchKeyword, pageNumber));
	}
	
	/**
	 * Search active brands whose name starts with given search keyword by pages
	 * 
	 * @param searchKeyword
	 * @return
	 */
	@GetMapping("/search-delete/{searchKeyword}/{pageNumber}")
	public ResponseEntity<ApiResponse> searchActiveFalse(@PathVariable String searchKeyword, @PathVariable int pageNumber, OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(brandService.searchActiveFalse(searchKeyword, pageNumber));
	}
	
	/**
	 * Get Brand by id
	 * 
	 * @param brandId
	 * @return
	 */
	@GetMapping("/get/{brandId}")
	public ResponseEntity<ApiResponse> getByBrand(@PathVariable Long brandId, OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(brandService.get(brandId));
	}
	
	/**
	 * Get single country data 
	 * 
	 * @param countryId
	 * @return
	 */
	@GetMapping("/getBrandData/{brandId}")
	public ResponseEntity<ApiResponse> getBrandData(@PathVariable Long brandId, OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(brandService.get(brandId));
	}
	
	/**
	 * Delete Store by id
	 * 
	 * @param storeId
	 * @return
	 * @throws CPException 
	 */
	@DeleteMapping("/deleteBrand/{brandId}")
	public ResponseEntity<ApiResponse> deleteBrand(@PathVariable Long brandId, OAuth2Authentication oAuth2Authentication) throws CPException {
		
		brandService.deleteBrand(brandId);
		return ResponseGenerator.successResponse(SuccessStatus.SUCCESS_DELETE);
	}
	
	/**
	 * Restore Event by id
	 * 
	 * @param eventId
	 * @return
	 */
	@GetMapping("/restoreBrand/{brandId}")
	public ResponseEntity<ApiResponse> restoreBrand(@PathVariable Long brandId, OAuth2Authentication oAuth2Authentication) {
		
		brandService.restoreBrand(brandId);
		return ResponseGenerator.successResponse(SuccessStatus.SUCCESS_RESTORE);
	}
}
