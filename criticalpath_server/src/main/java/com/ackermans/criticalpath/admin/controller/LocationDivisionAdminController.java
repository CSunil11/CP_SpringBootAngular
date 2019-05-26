package com.ackermans.criticalpath.admin.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import com.ackermans.criticalpath.enums.ErrorStatus;
import com.ackermans.criticalpath.enums.SuccessStatus;
import com.ackermans.criticalpath.service.LocationDivisionService;
import com.fasterxml.jackson.databind.JsonNode;

@RestController
@RequestMapping("/admin/locationdivision/")
public class LocationDivisionAdminController {
	
	private static final Logger logger = LogManager.getFormatterLogger(LocationDivisionAdminController.class);
	
	@Autowired
	private LocationDivisionService locationDivisionService;
	
	/**
	 * Return all location divisions
	 * 
	 * @return
	 * @throws CPException
	 */
	@GetMapping("/all")
	public ResponseEntity<ApiResponse> getAll() throws CPException {

		return ResponseGenerator.successResponse(locationDivisionService.getAll());
	}
	
	/**
	 * Return all active location divisions
	 * 
	 * @return
	 * @throws CPException
	 */
	@GetMapping("/all-active")
	public ResponseEntity<ApiResponse> getAllActive() throws CPException {

		return ResponseGenerator.successResponse(locationDivisionService.getAllActive());
	}
	
	/**
	 * Return all active location divisions
	 * 
	 * @return
	 * @throws CPException
	 */
	@GetMapping("/all-active/{pageNumber}")
	public ResponseEntity<ApiResponse> getAllActive(@PathVariable int pageNumber, OAuth2Authentication oAuth2Authentication) throws CPException {

		return ResponseGenerator.successResponse(locationDivisionService.getAllActive(pageNumber));
	}

	/**
	 * Return all delete location divisions
	 * 
	 * @return
	 * @throws CPException
	 */
	@GetMapping("/all-deleteLocationDivision")
	public ResponseEntity<ApiResponse> getAllDeleteLocationDivision() throws CPException {

		return ResponseGenerator.successResponse(locationDivisionService.getAllDeletedLocationDivi());
	}
	
	/**
	 * Get all deleted locationDivision by pages
	 * 
	 * @return
	 */
	@GetMapping("/all-deleteLocationDivision/{pageNumber}")
	public ResponseEntity<ApiResponse> getAllDeletedLocDiv(@PathVariable int pageNumber, OAuth2Authentication oAuth2Authentication) {

		return ResponseGenerator.successResponse(locationDivisionService.getAllDeletedLocDiv(pageNumber));
	}
	
	/**
	 * Save location division
	 * 
	 * @param locationDivision
	 * @return
	 * @throws CPException
	 */
	@PostMapping("/save")
	public ResponseEntity<ApiResponse> save(@RequestBody JsonNode request) throws CPException {
		
		try {
			//If request is null then it's invalid request
			if(request != null) {
				
				Long id = request.get("id") != null ? request.get("id").asLong() : 0L;
				String name = request.get("name").asText();
				Long uid = request.get("uid").asLong();
				/* Long ramuid = request.get("ramuid").asLong(); */
				Long cid = request.get("cid").asLong();
				Long pid = request.get("pid").asLong();
				boolean isActive = request.get("status").asBoolean();
				
				return ResponseGenerator.successResponse(locationDivisionService.verifyAndSave(id, name, uid, cid, pid, isActive));
			} else {
				throw new CPException(ErrorStatus.INVALID_REQUEST);
			}
		} catch (Exception ex) {
			logger.error("Error while saving location divsion :: ", ex);
			throw new CPException(ErrorStatus.INVALID_REQUEST);
		}
	}

	/**
	 * Delete record for given id
	 * 
	 * @param locationDivisionId
	 * @return
	 * @throws CPException
	 */
	@DeleteMapping("/delete/{locationDivisionId}")
	public ResponseEntity<ApiResponse> delete(@PathVariable Long locationDivisionId) throws CPException {
		
		//If location division id is null or not greater than zero then it's invalid request
		if(locationDivisionId != null && locationDivisionId > 0) {
			locationDivisionService.delete(locationDivisionId);
			return ResponseGenerator.successResponse(SuccessStatus.SUCCESS_DELETE);
		} else {
			throw new CPException(ErrorStatus.INVALID_REQUEST);
		}
	}
	
	/**
	 * Restore record for given id
	 * 
	 * @param locationDivisionId
	 * @return
	 * @throws CPException
	 */
	@GetMapping("/restore/{locationDivisionId}")
	public ResponseEntity<ApiResponse> restore(@PathVariable Long locationDivisionId) throws CPException {
		
		//If location division id is null or not greater than zero then it's invalid request
		if(locationDivisionId != null && locationDivisionId > 0) {
			locationDivisionService.restore(locationDivisionId);
			return ResponseGenerator.successResponse(SuccessStatus.SUCCESS_RESTORE);
		} else {
			throw new CPException(ErrorStatus.INVALID_REQUEST);
		}
	}	
	
	/**
	 * Get single province data 
	 * 
	 * @param countryId
	 * @return
	 */
	@GetMapping("/getLocationDivisionData/{locationDivisionId}")
	public ResponseEntity<ApiResponse> getEventData(@PathVariable Long locationDivisionId, OAuth2Authentication oAuth2Authentication) {	
		return ResponseGenerator.successResponse(locationDivisionService.getLocationDivisionData(locationDivisionId));
	}
	
	/**
	 *Get locationDivision of a province
	 * 
	 * @param provinceId
	 * @return
	 */
	@GetMapping("/getLocationDivisionOfProvince/{provinceId}")
	public ResponseEntity<ApiResponse> getProvinceOfCountry(@PathVariable Long provinceId, OAuth2Authentication oAuth2Authentication) {

		return ResponseGenerator.successResponse(locationDivisionService.getLocationDivisionOfProvince(provinceId));
	}
	
	/**
	 * Search active LocDiv whose name starts with given search keyword by pages
	 * 
	 * @param searchKeyword
	 * @return
	 */
	@GetMapping("/search-active/{searchKeyword}/{pageNumber}")
	public ResponseEntity<ApiResponse> searchActiveTrue(@PathVariable String searchKeyword, @PathVariable int pageNumber, OAuth2Authentication oAuth2Authentication) {

		return ResponseGenerator.successResponse(locationDivisionService.searchActiveTrue(searchKeyword, pageNumber));
	}
	
	/**
	 * Search active store whose name starts with given search keyword by pages
	 * 
	 * @param searchKeyword
	 * @return
	 */
	@GetMapping("/search-delete/{searchKeyword}/{pageNumber}")
	public ResponseEntity<ApiResponse> searchActiveFalse(@PathVariable String searchKeyword, @PathVariable int pageNumber, OAuth2Authentication oAuth2Authentication) {

		return ResponseGenerator.successResponse(locationDivisionService.searchActiveFalse(searchKeyword, pageNumber));
	}
}
