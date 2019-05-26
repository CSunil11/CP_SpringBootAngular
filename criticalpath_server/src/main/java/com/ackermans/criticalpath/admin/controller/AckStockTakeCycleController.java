package com.ackermans.criticalpath.admin.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

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
import com.ackermans.criticalpath.entity.StockTakeCycle;
import com.ackermans.criticalpath.enums.SuccessStatus;
import com.ackermans.criticalpath.service.AckStockTakeCycleService;
import com.fasterxml.jackson.databind.JsonNode;

@RestController
@RequestMapping("/user/ackStockTakeCycle")
public class AckStockTakeCycleController {

	@Autowired
	private AckStockTakeCycleService stockTakeCycleService;
	
	/**
	 * Save or Update Stock Take Cycle
	 * 
	 * @param stockTakeCycle
	 * @return
	 * @throws CPException 
	 * @throws ParseException 
	 */
	@PostMapping("/save")
	public ResponseEntity<ApiResponse> save(@RequestBody StockTakeCycle stockTakeCycle, OAuth2Authentication oAuth2Authentication) throws CPException, ParseException {
		
//		return ResponseGenerator.successResponse(stockTakeCycleService.verifyAndSave(stockTakeCycle, oAuth2Authentication));
		return null;
	}
	
	/**
	 * Get all StockTakeCycle
	 * @return
	 */
	@GetMapping("/all")
	public ResponseEntity<ApiResponse> getAll(OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(stockTakeCycleService.getAll());
	}
	
	/**
	 * Get All Active Stock Take Cycle List
	 * 
	 * @return
	 */
	@GetMapping("/all-active")
	public ResponseEntity<ApiResponse> getAllActive(OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(stockTakeCycleService.getAllActive());
	}
	
	/**
	 * Get All Active Stock Take Cycle List with Store name
	 * 
	 * @return
	 */
	@GetMapping("/store-per-dsm-user/{id}/{role}")
	public ResponseEntity<ApiResponse> getAllActiveStoresPerDsmUser(@PathVariable Long id, @PathVariable String role, OAuth2Authentication oAuth2Authentication) {
		
		//return ResponseGenerator.successResponse(stockTakeCycleService.getAllActiveStoresPerDsmUser(id));
		return ResponseGenerator.successResponse(stockTakeCycleService.getAllActiveStoresPerDsmUserInterface(id, role));
		
	}
	
	/**
	 * Get All Active AckStock Take Cycle for Selected DSM User
	 * 
	 * @return
	 */
	@GetMapping("/stock-cycle-dsm-user/{userId}/{role}/{sortBy}/{storeId}")
	public ResponseEntity<ApiResponse> getAllActiveStockTakeCycleDsmUser(@PathVariable Long userId, @PathVariable String role, @PathVariable int sortBy, @PathVariable Long storeId, OAuth2Authentication oAuth2Authentication) {
		System.out.println("ddddddd.......");
		return ResponseGenerator.successResponse(stockTakeCycleService.getAllActiveStockTakeCycleDsmUser(userId, role, sortBy, storeId));
	}
	
	/**
	 * Get All Active Stock Take Cycle Status for Selected DSM User
	 * 
	 * @return
	 */
	@PostMapping("/stock-cycle-dsm-user/status")
	public ResponseEntity<ApiResponse> getAllActiveStockTakeCycleStatusDsmUser(@RequestBody String ls, OAuth2Authentication oAuth2Authentication) {
		
		System.out.println("ls: "+ls);	// 50/ROLE_DSM_USER/1/2-1,2-2,2-4,5-3
		String userId = "";
		String role = "";
		int sortBy = 1;
		ArrayList<String> list=new ArrayList<String>();
		
		String arrayFromLs[]= ls.split("/");
		for (int i=0 ; i<arrayFromLs.length; i++){
			if(arrayFromLs[i] != null) {
				if(i == 0)
					userId = arrayFromLs[i].toString();
				else if(i == 1)
					role = arrayFromLs[i].toString();
				else if(i == 2)
					sortBy = Integer.parseInt(arrayFromLs[i].toString());
				else if(i == 3)
				{
					String strTemp = arrayFromLs[i].toString();
					if(strTemp != null)
					{
						System.out.println("strTemp: "+strTemp);
						String arrayFromStr[]= strTemp.split(",");
						for(String str : arrayFromStr)
							list.add(str);
					}
				}
			}
		      
		}
		
		System.out.println("list: "+list);
		
//		return null;
		return ResponseGenerator.successResponse(stockTakeCycleService.getAllActiveStockTakeCycleStatusDsmUser(userId, role, sortBy, list));
	}
	
	/**
	 * Get All Active deleted Take Cycle List
	 * 
	 * @return
	 */
	@GetMapping("/all-delete")
	public ResponseEntity<ApiResponse> getAllDeletedStockTakeCycle(OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(stockTakeCycleService.getAllDeletedStockTakeCycle());
	}
	
	/**
	 * Get Stock Take Cycle List By stockTakeCycleId
	 * 
	 * @param stockTakeCycleId
	 * @return
	 */
	@GetMapping("/getAckCycleById/{stockTakeCycleId}")
	public ResponseEntity<ApiResponse> getStockTakeCycleData(@PathVariable Long stockTakeCycleId, OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(stockTakeCycleService.getStockTakeCycleData(stockTakeCycleId));
	}
	
	/**
	 * Delete Stock Take Cycle
	 * 
	 * @param stockTakeCycleId
	 * @return
	 * @throws CPException 
	 */
	@DeleteMapping("/deleteStockTakeCycle/{stockTakeCycleId}")
	public ResponseEntity<ApiResponse> deleteStockTakeCycle(@PathVariable Long stockTakeCycleId, OAuth2Authentication oAuth2Authentication) throws CPException {
		
		stockTakeCycleService.deleteStockTakeCycle(stockTakeCycleId);
		return ResponseGenerator.successResponse(SuccessStatus.SUCCESS_DELETE);
	}
	
	/**
	 * Hides the Stock take cycle from Ackerman portal
	 * @param stockTakeCycleId
	 * @param oAuth2Authentication
	 * @return
	 * @throws CPException
	 */
	@DeleteMapping("/hideStockTakeCycle/{stockTakeCycleId}")
	public ResponseEntity<ApiResponse> hideStockTakeCycle(@PathVariable Long stockTakeCycleId, OAuth2Authentication oAuth2Authentication) throws CPException {
		
		stockTakeCycleService.hideStockTakeCycle(stockTakeCycleId);
		return ResponseGenerator.successResponse(SuccessStatus.SUCCESS_DELETE);
	}
	
	/**
	 * Restore Stock Take Cycle
	 * 
	 * @param stockTakeCycleId
	 * @return
	 */
	@GetMapping("/restoreStockTakeCycle/{stockTakeCycleId}")
	public ResponseEntity<ApiResponse> restoreStockTakeCycle(@PathVariable Long stockTakeCycleId, OAuth2Authentication oAuth2Authentication) {
		
		stockTakeCycleService.restoreStockTakeCycle(stockTakeCycleId);
		return ResponseGenerator.successResponse(SuccessStatus.SUCCESS_RESTORE);
	}
	
	/**
	 * Save stockTakeDate 
	 * 
	 * @param stockTakeCycleId
	 * @param stockTakeDate
	 * @param oAuth2Authentication
	 * @return
	 * @throws ParseException 
	 */
	@GetMapping("/add-stockTakeDate/{stockTakeCycleId}/{stockTakeDate}")
	public ResponseEntity<ApiResponse> addStockTakeDate(@PathVariable Long stockTakeCycleId, @PathVariable String stockTakeDate,  OAuth2Authentication oAuth2Authentication) throws ParseException {
		
		stockTakeCycleService.saveStockTakeDate(stockTakeCycleId , stockTakeDate);
		return ResponseGenerator.successResponse(SuccessStatus.SUCCESS_RESTORE);
	}
	
	/**
	 *  Get stockTakeCycle by user id
	 * @param userId
	 * @return
	 * @throws ParseException
	 */
	@GetMapping("/StockTakeCycleByUser/{userId}")
	public ResponseEntity<ApiResponse> addStockTakeDate(@PathVariable Long userId) throws ParseException {
		System.out.println("userId..." +userId);
		
		return ResponseGenerator.successResponse(stockTakeCycleService.getStockTakeCycleByUser(userId));
	}
	
	@GetMapping("/isActiveCycleAvail/{userId}")
	public ResponseEntity<ApiResponse> isActiveCycleAvail(@PathVariable Long userId) throws ParseException {
		
		return  ResponseGenerator.successResponse(stockTakeCycleService.isActiveCycleAvail(userId));
		
	}
	
	@GetMapping("/addCycle/{cycleId}/{startDate}/{stockTakeDate}/{storeId}/{time}")
	public ResponseEntity<ApiResponse> addAckCycle(@PathVariable Long cycleId, @PathVariable String startDate, @PathVariable String stockTakeDate,
			@PathVariable Long storeId, @PathVariable String time) throws ParseException {
		
		return  ResponseGenerator.successResponse(stockTakeCycleService.addAckCycle(cycleId,startDate,stockTakeDate ,storeId, time));
		
	}
	
	@PostMapping("/calculate-startdate")
	public ResponseEntity<ApiResponse> calculateStartDate(@RequestBody List<Object> dataList) throws ParseException {
		System.out.println("start date " +dataList );
		return  ResponseGenerator.successResponse(stockTakeCycleService.calculateStartDate(Long.parseLong(dataList.get(0).toString()),dataList.get(1).toString(),Long.parseLong(dataList.get(2).toString())));
		
	}
	
	@GetMapping("/getalldatelist/{stockTakeCycleId}/{stockTakeDate}")
	public ResponseEntity<ApiResponse> getAllDateList(@PathVariable Long stockTakeCycleId, @PathVariable String stockTakeDate) throws ParseException {
		System.out.println("start date " +stockTakeDate );
		return  ResponseGenerator.successResponse(stockTakeCycleService.getAllDateList(stockTakeCycleId,stockTakeDate));
		
	}
	
	@PostMapping("/calculate-multiple-startdate")
	public ResponseEntity<ApiResponse> calulateMultipleStartDate(@RequestBody List<Object> selectedStoreList ) throws ParseException {
		System.out.println("stockTake  date " +selectedStoreList );
		return  ResponseGenerator.successResponse(stockTakeCycleService.calculateMultipleStartDate(selectedStoreList));
		
	}
	
	@PostMapping("/add-multiple-startdate")
	public ResponseEntity<ApiResponse> addMultipleStartDate(@RequestBody List<Object> selectedStoreList ) throws ParseException {
		System.out.println("stockTake  date " +selectedStoreList );
		return  ResponseGenerator.successResponse(stockTakeCycleService.addMultipleStartDate(selectedStoreList));
	}
		
	/**
	 * Delete old and create new stock take
	 * 
	 * @param request
	 * @return
	 * @throws CPException 
	 * @throws ParseException 
	 */
	@PostMapping("/deleteAndCreateNewStockTake")
	public ResponseEntity<ApiResponse> deleteAndCreateNewStockTake(@RequestBody JsonNode request, OAuth2Authentication oAuth2Authentication) throws CPException, ParseException {
		
//		stockTakeCycleService.deleteAndCreateNewStockTake(request);
		return  ResponseGenerator.successResponse(stockTakeCycleService.deleteAndCreateNewStockTake(request));
	}
	
	@GetMapping("/getUpcomingCycleStartDate/{storeId}")
	public ResponseEntity<ApiResponse> getUpcomingCycleStartDate(@PathVariable Long storeId, OAuth2Authentication oAuth2Authentication) {
		return  ResponseGenerator.successResponse(stockTakeCycleService.getUpcomingCycleStartDate(storeId));
	}
	
	@PostMapping("/getStoreStockTakeDate")
	public ResponseEntity<ApiResponse> getStoreStockTakeDate(@RequestBody List<String> storeSelectedList ) throws ParseException {
		System.out.println("stockTake  date " +storeSelectedList );
		return  ResponseGenerator.successResponse(stockTakeCycleService.getStoreStockTakeDate(storeSelectedList));
	}
	
	@PostMapping("/editNewStockTake")
	public ResponseEntity<ApiResponse> editNewStockTake(@RequestBody JsonNode request, OAuth2Authentication oAuth2Authentication) throws CPException, ParseException {
		
		return  ResponseGenerator.successResponse(stockTakeCycleService.editNewStockTake(request));
	}
}
