package com.ackermans.criticalpath.admin.controller;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
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
import com.ackermans.criticalpath.service.AckCriticalPathService;
import com.ackermans.criticalpath.service.StockTakeResultService;
import com.fasterxml.jackson.databind.JsonNode;


@RestController
@RequestMapping("/user/ackCriticalPath")
public class AckCriticalPathController {

	@Autowired
	private AckCriticalPathService ackCriticalPathService;
	
	@Autowired
	private StockTakeResultService stockTakeResultService;
	
	/**
	 * Get Critical PAth List By criticalPathId
	 * 
	 * @param criticalPathId
	 * @return
	 * @throws ParseException 
	 */
	@GetMapping("/getAllByAckCycleId/{stockTakeCycleId}")
	public ResponseEntity<ApiResponse> getAckCriticalPathData(@PathVariable Long stockTakeCycleId, OAuth2Authentication oAuth2Authentication) throws ParseException {
		ackCriticalPathService.getTodayActiveTask(stockTakeCycleId);
		return ResponseGenerator.successResponse(ackCriticalPathService.getAckCriticalPathData(stockTakeCycleId));
	}
	
	@PostMapping("/updateAckTaskStatus")
	public ResponseEntity<ApiResponse> save(@RequestBody JsonNode request) throws CPException {
		
		try {
			//If request is null then it's invalid request
			if(request != null) {
				
//				Long criticalPathid = request.get("criticalPathid").asLong();
//				String completedBy = request.get("completedby").asText();
//				String completeDate = request.get("taskCompleteDate").asText();
//				Long statusid = request.get("statusid").asLong();			

				return  ResponseGenerator.successResponse(ackCriticalPathService.updateAckCriticalPathStatus(request));
			} else {
				throw new CPException(ErrorStatus.INVALID_REQUEST);
			}
		} catch (Exception ex) {
			
			throw new CPException(ErrorStatus.INVALID_REQUEST);
		}
	
	}
	
	/**
	 * Get StockTakeResult By storeId
	 * 
	 * @param storeId
	 * @return
	 * @throws ParseException 
	 */
	@GetMapping("/getStockTakeResult/{storeId}")
	public ResponseEntity<ApiResponse> getStockTakeResult(@PathVariable Long storeId, OAuth2Authentication oAuth2Authentication) throws ParseException {
		return ResponseGenerator.successResponse(stockTakeResultService.getStockTakeResult(storeId));
	}
	
	/**
	 * updateStatus of  StockTakeResult
	 * 
	 * @param storeId
	 * @return
	 * @throws ParseException 
	 * @throws CPException 
	 */
	@GetMapping("/updateStatus/{resultId}/{reasonForDecline}/{status}/{userRole}")
	public ResponseEntity<ApiResponse> getStockTakeResult(@PathVariable Long resultId, @PathVariable String reasonForDecline,
			@PathVariable String status, @PathVariable String userRole, OAuth2Authentication oAuth2Authentication) throws ParseException, CPException {
		return ResponseGenerator.successResponse(stockTakeResultService.updateStockTakeResult(resultId ,reasonForDecline,status ,userRole));
	}
	
	@PostMapping("/updateDeclineStatusByRAM")
	public ResponseEntity<ApiResponse> updateDeclineStatusRam(@RequestBody JsonNode ramDeclineJson) throws CPException {
		
		try {
			//If request is null then it's invalid request
			if(ramDeclineJson != null) {
				
//				Long criticalPathid = request.get("criticalPathid").asLong();
//				String completedBy = request.get("completedby").asText();
//				String completeDate = request.get("taskCompleteDate").asText();
//				Long statusid = request.get("statusid").asLong();			

				return  ResponseGenerator.successResponse(stockTakeResultService.updateDeclineStatusRam(ramDeclineJson));
			} else {
				throw new CPException(ErrorStatus.INVALID_REQUEST);
			}
		} catch (Exception ex) {
			
			throw new CPException(ErrorStatus.INVALID_REQUEST);
		}
	
	}
	
}
