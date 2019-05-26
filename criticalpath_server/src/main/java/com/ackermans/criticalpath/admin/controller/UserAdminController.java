package com.ackermans.criticalpath.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ackermans.criticalpath.admin.dto.UserRequest;
import com.ackermans.criticalpath.common.dto.ApiResponse;
import com.ackermans.criticalpath.common.dto.CPException;
import com.ackermans.criticalpath.common.service.ResponseGenerator;
import com.ackermans.criticalpath.enums.SuccessStatus;
import com.ackermans.criticalpath.enums.UserRole;
import com.ackermans.criticalpath.repository.UserLoginRepository;
import com.ackermans.criticalpath.service.UserService;

@RestController
@RequestMapping("/admin/user")
public class UserAdminController {
	
	@Autowired 
	UserService userService;
	
	@Autowired 
	UserLoginRepository userLoginRepository;
	
	/**
	 * Get all User by Brands
	 * 
	 * @return
	 */
	@GetMapping("/getAllByBrand/{brandId}/{pageNumber}")
	public ResponseEntity<ApiResponse> getAllByBrand(@PathVariable Long brandId,@PathVariable int pageNumber, OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(userService.getAllByBrand(brandId, pageNumber));
	}
	
	/**
	 * Get all active User by Brands
	 * 
	 * @return
	 */
	@GetMapping("/getAllActiveByBrand/{brandId}/{pageNumber}/{searchRole}")
	public ResponseEntity<ApiResponse> getAllActiveByBrand(@PathVariable Long brandId,@PathVariable int pageNumber,@PathVariable String searchRole, OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(userService.getAllActiveByBrand(brandId, pageNumber, searchRole));
	}
	
	/**
	 * Get all active User by Brands
	 * 
	 * @return
	 */
	@GetMapping("/getAllDeleteByBrand/{brandId}/{pageNumber}/{searchRole}")
	public ResponseEntity<ApiResponse> getAllDeletedUserByBrand(@PathVariable Long brandId,@PathVariable int pageNumber, @PathVariable String searchRole, OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(userService.getAllDeletedUserByBrand(brandId, pageNumber, searchRole));
	}
	
	/**
	 * Save or Update User
	 * 
	 * @param userRequest
	 * @return
	 * @throws CPException 
	 */
	@PostMapping("/save")
	public ResponseEntity<ApiResponse> save(@RequestBody UserRequest userRequest, OAuth2Authentication oAuth2Authentication) throws CPException {

		return ResponseGenerator.successResponse(userService.verifyAndSave(userRequest));
	}
	
	/**
	 * Search from users of given brand whose name or email starts with given search string
	 * 
	 * @param searchstr
	 * @param brandId
	 * @return
	 */
	@GetMapping("/search/{searchstr}/{brandId}/{pageNumber}/{searchRole}")
	public ResponseEntity<ApiResponse> search(@PathVariable String searchstr, @PathVariable Long brandId,@PathVariable int pageNumber, @PathVariable String searchRole, OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(userService.search(searchstr, brandId, pageNumber));
	}
	
	/**
	 * Search from active users of given brand whose name or email starts with given search string
	 * 
	 * @param searchstr
	 * @param brandId
	 * @return
	 */
	@GetMapping("/search-active/{searchstr}/{brandId}/{pageNumber}/{searchRole}")
	public ResponseEntity<ApiResponse> searchActiveBrandUser(@PathVariable String searchstr, @PathVariable Long brandId,@PathVariable int pageNumber, @PathVariable String searchRole, OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(userService.searchActiveBrandUser(searchstr, brandId, pageNumber, searchRole));
	}
	
	/**
	 * Search from deleted users of given brand whose name or email starts with given search string
	 * 
	 * @param searchstr
	 * @param brandId
	 * @return
	 */
	@GetMapping("/search-delete/{searchstr}/{brandId}/{pageNumber}/{searchRole}")
	public ResponseEntity<ApiResponse> searchDeletedBrandUser(@PathVariable String searchstr, @PathVariable Long brandId,@PathVariable int pageNumber, @PathVariable String searchRole, OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(userService.searchDeletedBrandUser(searchstr, brandId, pageNumber, searchRole));
	}
	
	/**
	 * Get single brand user data 
	 * 
	 * @param brandUserId
	 * @return
	 */
	@GetMapping("/getBrandUserData/{brandUserId}")
	public ResponseEntity<ApiResponse> getBrandUserData(@PathVariable Long brandUserId, OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(userService.getBrandUserData(brandUserId));
	}
	
	/**
	 * Delete user
	 * 
	 * @param userId
	 * @return
	 * @throws CPException 
	 */
	@DeleteMapping("/deleteUser/{userId}")
	public ResponseEntity<ApiResponse> deleteBrand(@PathVariable Long userId, OAuth2Authentication oAuth2Authentication) throws CPException {
		
		userService.deleteUser(userId);
		return ResponseGenerator.successResponse(SuccessStatus.SUCCESS_DELETE);
	}
	
	/**
	 * Restore user
	 * 
	 * @param userId
	 * @return
	 */
	@GetMapping("/restoreUser/{userId}")
	public ResponseEntity<ApiResponse> restoreBrand(@PathVariable Long userId, OAuth2Authentication oAuth2Authentication) {
		
		userService.restoreUser(userId);
		return ResponseGenerator.successResponse(SuccessStatus.SUCCESS_RESTORE);
	}
	
	/**
	 * Get all active Divisional Sales Manager
	 * 
	 * @return
	 * @throws CPException
	 */
	@GetMapping("/all-active/dsm")
	public ResponseEntity<ApiResponse> getAllActiveDSMUsers() throws CPException {
		
		return ResponseGenerator.successResponse(userService.getAllActiveByRole(UserRole.ROLE_DSM_USER));
	}
	
	/**
	 * Get all active Divisional Sales Managers who are not assigned to and Location Division 
	 * 
	 * @return
	 * @throws CPException
	 */
	@GetMapping("/unassigned/dsm")
	public ResponseEntity<ApiResponse> getAllActiveUnassignedDSMUsers() throws CPException {
		
		return ResponseGenerator.successResponse(userService.getUnassignedDSM());
	}
	
	/**
	 * Get all active RAM user
	 * 
	 * @return
	 * @throws CPException
	 */
	@GetMapping("/all-active/ram")
	public ResponseEntity<ApiResponse> getAllActiveRAMUsers() throws CPException {
		
		return ResponseGenerator.successResponse(userService.getAllActiveByRole(UserRole.ROLE_RAM_USER));
	}
	
	@GetMapping("/logout")
	public ResponseEntity<ApiResponse> logOutUser(OAuth2Authentication oAuth2Authentication) throws CPException {
		userService.logOutUser();
		return ResponseGenerator.successResponse(SuccessStatus.SUCCESS_LOGOUT);
	}
}
