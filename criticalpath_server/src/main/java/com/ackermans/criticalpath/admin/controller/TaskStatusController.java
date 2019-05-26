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
import com.ackermans.criticalpath.entity.TaskStatus;
import com.ackermans.criticalpath.enums.SuccessStatus;
import com.ackermans.criticalpath.service.TaskStatusService;

@RestController
@RequestMapping("/admin/taskstatus/")
public class TaskStatusController {
	
	@Autowired
	private TaskStatusService statusService;

	/**
	 * Get all the task status
	 * 
	 * @return
	 */
	@GetMapping("/all")
	public ResponseEntity<ApiResponse> getAll(OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(statusService.getAll());
	}
	
	/**
	 * Get all the active task status
	 * 
	 * @return
	 */
	@GetMapping("/all-active")
	public ResponseEntity<ApiResponse> getAllActive(OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(statusService.getAllActive());
	}
	
	/**
	 * Get all the delete task status
	 * 
	 * @return
	 */
	@GetMapping("/all-deleteTaskStatus")
	public ResponseEntity<ApiResponse> getAllDeleteTaskStatus(OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(statusService.getAllDeletedTaskStatus());
	}
	
	/**
	 * Check if task status already exists for given name
	 * 
	 * @param name
	 * @return
	 */
	@GetMapping("/isexist/{name}")
	public ResponseEntity<ApiResponse> isExist(@PathVariable String name, OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(statusService.isExists(name));
	}
	
	/**
	 * Save or Update task status
	 * 
	 * @param taskStatus
	 * @return
	 * @throws CPException 
	 */
	@PostMapping("/save")
	public ResponseEntity<ApiResponse> save(@RequestBody TaskStatus taskStatus,OAuth2Authentication oAuth2Authentication) throws CPException {
		
		return ResponseGenerator.successResponse(statusService.verifyAndSave(taskStatus));
	}
	
	/**
	 * Get Task Status data
	 * 
	 * @param taskStatusId
	 * @return
	 */
	@GetMapping("/getTaskStatusData/{taskStatusId}")
	public ResponseEntity<ApiResponse> getTaskStatusData(@PathVariable Long taskStatusId,OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(statusService.getTaskStatusData(taskStatusId));
	}
	
	/**
	 * Upload task data in bulk
	 * 
	 * @param task
	 * @return
	 * @throws CPException 
	 * @throws SQLException 
	 * @throws IOException 
	 */
	@PostMapping("/upload")
	public ResponseEntity<ApiResponse> upload(@RequestParam(value="data",required=false) MultipartFile data, OAuth2Authentication oAuth2Authentication) throws CPException, SQLException, IOException {
		
		return ResponseGenerator.successResponse(statusService.upload(data));
	}
	
	/**
	 * Delete record by id
	 * 
	 * @param taskStatusId
	 * @return
	 * @throws CPException 
	 */
	@DeleteMapping("/deleteTaskStatus/{taskStatusId}")
	public ResponseEntity<ApiResponse> deleteTaskStatus(@PathVariable Long taskStatusId,OAuth2Authentication oAuth2Authentication) throws CPException {
		
		statusService.deleteTaskStatus(taskStatusId);
		return ResponseGenerator.successResponse(SuccessStatus.SUCCESS_DELETE);
	}
	
	/**
	 * Restore record by id
	 * 
	 * @param taskStatusId
	 * @return
	 */
	@GetMapping("/restoreTaskStatus/{taskStatusId}")
	public ResponseEntity<ApiResponse> restoreTaskStatus(@PathVariable Long taskStatusId, OAuth2Authentication oAuth2Authentication) {
		
		statusService.restoreTaskStatus(taskStatusId);
		return ResponseGenerator.successResponse(SuccessStatus.SUCCESS_RESTORE);
	}
}
