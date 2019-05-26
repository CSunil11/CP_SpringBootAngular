package com.ackermans.criticalpath.admin.controller;


import java.io.IOException;

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
import com.ackermans.criticalpath.enums.SuccessStatus;
import com.ackermans.criticalpath.service.ProvinceService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


@RestController
@RequestMapping("/admin/province")
public class ProvinceAdminController {
	
	@Autowired
	private ProvinceService provinceService;
	
	@Autowired
	private ObjectMapper mapper;
	/**
	 * Get All Province
	 * 
	 * @param oAuth2Authentication
	 * @return
	 */
	@GetMapping("/all")
	public ResponseEntity<ApiResponse> getAll(OAuth2Authentication oAuth2Authentication) {
		return ResponseGenerator.successResponse(provinceService.getAll());
	}
	
	/**
	 * Get all Province by pages
	 * 
	 * @param pageNumber
	 * @param oAuth2Authentication
	 * @return
	 */
	@GetMapping("/all/{pageNumber}")
	public ResponseEntity<ApiResponse> getAll(@PathVariable int pageNumber,OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(provinceService.getAll(pageNumber));
	}
	
	/**
	 * Get all active Province
	 * 
	 * @param oAuth2Authentication
	 * @return
	 */
	@GetMapping("/all-active")
	public ResponseEntity<ApiResponse> getAllActive(OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(provinceService.getAllActive());
	}
	
	/**
	 * Get All is  delete Province
	 * 
	 * @return
	 */
	@GetMapping("/all-deleteProvince")
	public ResponseEntity<ApiResponse> getAllDeleteProvince(OAuth2Authentication oAuth2Authentication) {
		return ResponseGenerator.successResponse(provinceService.getAllDeleteProvince());
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
		
		return ResponseGenerator.successResponse(provinceService.getAllActive(pageNumber));
	}
	
	/**
	 * Get all deleted Province by pages
	 * 
	 * @return
	 */
	@GetMapping("/all-deleteProvince/{pageNumber}")
	public ResponseEntity<ApiResponse> getAllDeleteProvince(@PathVariable int pageNumber, OAuth2Authentication oAuth2Authentication) {

		return ResponseGenerator.successResponse(provinceService.getAllDeletedProvince(pageNumber));
	}
	
	/**
	 * Search province whose name starts with given search keyword
	 * @param searchKeyword
	 * @param oAuth2Authentication
	 * @return
	 */
	@GetMapping("/search/{searchKeyword}")
	public ResponseEntity<ApiResponse> search(@PathVariable String searchKeyword,OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(provinceService.search(searchKeyword));
	}
	
	/**
	 * Search province whose name starts with given search keyword
	 * 
	 * @param searchKeyword
	 * @return
	 */
	@GetMapping("/search-active/{searchKeyword}")
	public ResponseEntity<ApiResponse> searchActive(@PathVariable String searchKeyword,OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(provinceService.searchActive(searchKeyword));
	}
	
	/**
	 * Search active LocDiv whose name starts with given search keyword by pages
	 * 
	 * @param searchKeyword
	 * @return
	 */
	@GetMapping("/search-active/{searchKeyword}/{pageNumber}")
	public ResponseEntity<ApiResponse> searchActiveTrue(@PathVariable String searchKeyword, @PathVariable int pageNumber, OAuth2Authentication oAuth2Authentication) {

		return ResponseGenerator.successResponse(provinceService.searchActiveTrue(searchKeyword, pageNumber));
	}
	
	/**
	 * Search active store whose name starts with given search keyword by pages
	 * 
	 * @param searchKeyword
	 * @return
	 */
	@GetMapping("/search-delete/{searchKeyword}/{pageNumber}")
	public ResponseEntity<ApiResponse> searchActiveFalse(@PathVariable String searchKeyword, @PathVariable int pageNumber, OAuth2Authentication oAuth2Authentication) {

		return ResponseGenerator.successResponse(provinceService.searchActiveFalse(searchKeyword, pageNumber));
	}
	
	/**
	 * Activate province by id
	 * 
	 * @param eventId
	 * @return
	 */
	@PostMapping("/activate")
	public ResponseEntity<ApiResponse> activate(@RequestParam Long provinceId, OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(provinceService.activate(provinceId));
	}
	
	/**
	 * Deactivate province by id
	 * 
	 * @param eventId
	 * @return
	 */
	@PostMapping("/deactivate")
	public ResponseEntity<ApiResponse> deactivate(@RequestParam Long provinceId, OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(provinceService.deactivate(provinceId));
	}
	
	/**
	 * Save or Update Province
	 * 
	 * @param event
	 * @return
	 * @throws CPException 
	 * @throws IOException 
	 */
	@PostMapping("/save")
	public ResponseEntity<ApiResponse> save(@RequestBody String province, OAuth2Authentication oAuth2Authentication) throws CPException, IOException, NullPointerException {
		
		JsonNode jsonNode = mapper.readTree(province);
		Long provinceId;
		
		if(jsonNode.get("id") == null) {
			 provinceId = (long) 0;
		}else {
			 provinceId = jsonNode.get("id").asLong();
		}
		
		String provinceName = jsonNode.get("name").asText().trim();
		Long countryId = jsonNode.get("cid").asLong();
		Boolean isActive = jsonNode.get("isActive").asBoolean();
	
		return ResponseGenerator.successResponse(provinceService.verifyAndSave(countryId, provinceId, provinceName, isActive));
	}
	
	/**
	 * Verify name exist or not
	 * 
	 * @param name
	 * @return
	 */
	@GetMapping("/isexist/{name}")
	public ResponseEntity<ApiResponse> isExist(@PathVariable String name,OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(provinceService.isExists(name));
	}
	
	/**
	 * Get single province data 
	 * 
	 * @param countryId
	 * @return
	 */
	@GetMapping("/getProvinceData/{provinceId}")
	public ResponseEntity<ApiResponse> getEventData(@PathVariable Long provinceId, OAuth2Authentication oAuth2Authentication) {	
		return ResponseGenerator.successResponse(provinceService.getProvinceData(provinceId));
	}
	
	/**
	 * Delete province by id
	 * 
	 * @param provinceId
	 * @return
	 */
	@DeleteMapping("/deleteProvince/{provinceId}")
	public ResponseEntity<ApiResponse> deleteEvent(@PathVariable Long provinceId,OAuth2Authentication oAuth2Authentication) {
		
		provinceService.deleteProvince(provinceId);
		return ResponseGenerator.successResponse(SuccessStatus.SUCCESS_DELETE);
	}
	
	/**
	 * Restore province by id
	 * 
	 * @param provinceId
	 * @return
	 */
	@GetMapping("/restoreProvince/{provinceId}")
	public ResponseEntity<ApiResponse> restoreEvent(@PathVariable Long provinceId, OAuth2Authentication oAuth2Authentication) {
		
		provinceService.restoreProvince(provinceId);
		return ResponseGenerator.successResponse(SuccessStatus.SUCCESS_RESTORE);
	}
	
	/**
	 *Get province of a country
	 * 
	 * @param provinceId
	 * @return
	 */
	@GetMapping("/getProvinceOfCountry/{countryId}")
	public ResponseEntity<ApiResponse> getProvinceOfCountry(@PathVariable Long countryId, OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(provinceService.getProvinceOfCountry(countryId));
	}
}
