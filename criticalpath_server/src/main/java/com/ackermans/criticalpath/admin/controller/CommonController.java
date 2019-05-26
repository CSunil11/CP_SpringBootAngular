package com.ackermans.criticalpath.admin.controller;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ackermans.criticalpath.common.dto.ApiResponse;
import com.ackermans.criticalpath.common.dto.CPException;
import com.ackermans.criticalpath.common.service.ResponseGenerator;
import com.ackermans.criticalpath.entity.Permission;
import com.ackermans.criticalpath.entity.UserLogin;
import com.ackermans.criticalpath.enums.ErrorStatus;
import com.ackermans.criticalpath.service.PermissionService;
import com.ackermans.criticalpath.service.UserLoginService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@CrossOrigin
@RestController
@RequestMapping("/common")
public class CommonController {
	
	private final Logger logger = LogManager.getFormatterLogger(CommonController.class);

	@Autowired
	private UserLoginService userLoginService;
	
	@Autowired
	private PermissionService permissionService;
	
	@Autowired
	private ObjectMapper mapper;
	
	/**
	 * Get user by access token
	 * 
	 * @param authentication
	 * @return
	 * @throws ResourceNotFoundException
	 */
	@PostMapping("/getUserLogin")
	public UserLogin getUserLogin(OAuth2Authentication authentication)  {
		
		UserLogin userLogin = userLoginService.findByEmail(authentication.getName());
		
		return userLogin;
	}
	
	/**
	 * Get all permissions
	 * 
	 * @return
	 */
	@PostMapping("/getAllPermissions")
	public List<Permission> getAllPermissions(OAuth2Authentication authentication)  {
		
		return permissionService.getAllPermissions();
	}

	/**
	 * create API for UserId and Password Validate
	 * 
	 * @param request
	 * @return
	 * @throws CPException
	 */
	@PostMapping("/password/validate")
	public ResponseEntity<ApiResponse> passwordmatch(@RequestBody String request) throws CPException  {

		try {
			JsonNode jsonNode = mapper.readTree(request);
			Long userId = jsonNode.get("uid").asLong();
			String currentPassword = jsonNode.get("crrpwd").asText().trim();
			
			//Check if user id and current password not blank, else throw exception
			if(userId != null && currentPassword != null && userId > 0L && currentPassword.length() > 0)
				return ResponseGenerator.successResponse(userLoginService.isCurrentPasswordValid(userId, currentPassword));
			else
				throw new CPException(ErrorStatus.INVALID_REQUEST);
			
		} catch (IOException e) {
			logger.error("Error while parsing JSON request :: ", e);
			throw new CPException(ErrorStatus.INVALID_REQUEST);
		}
	}
	
	/**
	 * create API for UserLogin Password change
	 * 
	 * @param requestpasswordchange
	 * @return
	 * @throws CPException
	 */
	@PostMapping("/password/change")
	public ResponseEntity<ApiResponse> passwordchange(@RequestBody String requestpasswordchange) throws CPException  {

		try {
			JsonNode jsonNode = mapper.readTree(requestpasswordchange);
			Long userId = jsonNode.get("uid").asLong();
			String oldpassword = jsonNode.get("oldpwd").asText().trim();
			String newpassword = jsonNode.get("newpwd").asText().trim();
			
			//Check if user id and old password and new passwordnot blank, else throw exception
			if(userId != null && oldpassword != null && userId > 0L && oldpassword.length() > 0&&
					newpassword != null && userId > 0L && newpassword.length() > 0) {
				
				boolean matchpassword = userLoginService.isCurrentPasswordValid(userId, oldpassword);
				
				// check matchpassword is false else update new password
				if(!matchpassword) {
					throw new CPException(ErrorStatus.INVALID_CURRENT_PASSWORD);
				}	
					return ResponseGenerator.successResponse(userLoginService.updatePassword(userId, newpassword));
			}				
			else
				throw new CPException(ErrorStatus.INVALID_REQUEST);
			
		} catch (IOException e) {
			logger.error("Error while parsing JSON request :: ", e);
			throw new CPException(ErrorStatus.INVALID_REQUEST);
		}
	}
	
	/**
	 * API for forgot password
	 * 
	 * @param email
	 * @return
	 * @throws CPException
	 */
	@GetMapping("/password/forgot/{email}")
	public ResponseEntity<ApiResponse> forgotPassword(@PathVariable String email) throws CPException  {		
		return ResponseGenerator.successResponse(userLoginService.forgotPassword(email));
	}
		
	/**
	 * API to verify reset password URL
	 * 
	 * @param resetToken
	 * @return
	 * @throws CPException
	 */
	@GetMapping("/resetpassword/{resetToken}")
	public ResponseEntity<ApiResponse> verifyResetPasswordURL(@PathVariable String resetToken) throws CPException  {
		if(resetToken != null && resetToken.length()> 0) {		
			try {
				String decodeurl = URLDecoder.decode(resetToken, "UTF8");
				return ResponseGenerator.successResponse(userLoginService.isValidResetToken(decodeurl));
			} catch (UnsupportedEncodingException e) {
				logger.error("Error while decoding reset token :: ", e);
				throw new CPException(ErrorStatus.INVALID_RESET_LINK);
			}
			
		} else
			throw new CPException(ErrorStatus.INVALID_RESET_LINK);		
	}
	
	/**
	 * API to reset password
	 * 
	 * @param requestresetpassword
	 * @return
	 * @throws CPException
	 */
	@PostMapping("/password/reset")
	public ResponseEntity<ApiResponse> resetPassword(@RequestBody String requestResetPassword) throws CPException  {

		try {
			JsonNode jsonNode = mapper.readTree(requestResetPassword);
			String email = jsonNode.get("email").asText().trim();
			String newpwd = jsonNode.get("newpwd").asText().trim();
			
			//Check if email and new password not blank, else throw exception
			if(email != null && newpwd != null && email.length() > 0 && newpwd.length() > 0)
				return ResponseGenerator.successResponse(userLoginService.updatePassword(email, newpwd));
			else
				throw new CPException(ErrorStatus.INVALID_REQUEST);
			
		} catch (Exception e) {
			logger.error("Error while parsing JSON request :: ", e);
			throw new CPException(ErrorStatus.INVALID_REQUEST);
		}
	}
	
}
