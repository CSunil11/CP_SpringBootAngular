package com.ackermans.criticalpath.admin.controller;

import java.text.ParseException;
import java.util.ArrayList;

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
import com.ackermans.criticalpath.service.StockTakeCycleService;
import com.fasterxml.jackson.databind.JsonNode;

@RestController
@RequestMapping("/admin/stockTakeCycle")
public class StockTakeCycleController {

	@Autowired
	private StockTakeCycleService stockTakeCycleService;
	
	/**
	 * Save or Update Stock Take Cycle
	 * 
	 * @param stockTakeCycle
	 * @return
	 * @throws CPException 
	 * @throws ParseException 
	 */
	@PostMapping("/save")
	public ResponseEntity<ApiResponse> save(@RequestBody JsonNode request, OAuth2Authentication oAuth2Authentication) throws CPException, ParseException {
		
		return ResponseGenerator.successResponse(stockTakeCycleService.verifyAndSave(request, oAuth2Authentication));
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
	@GetMapping("/all-active-per-store")
	public ResponseEntity<ApiResponse> getAllActiveStockTakeCyclePerStore(OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(stockTakeCycleService.getAllActiveStockTakeCyclePerStore());
	}
	
	/**
	 * Get All Active Stock Take Cycle List with Store name
	 * 
	 * @return
	 */
	@GetMapping("/cycles-per-store/{storeId}")
	public ResponseEntity<ApiResponse> getStockTakeCyclePerStore(@PathVariable Long storeId, OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(stockTakeCycleService.getActiveStockTakeCyclePerStore(storeId));
	}
	
	/**
	 * Get All Active Stock Take Cycle Status List with Store id
	 * 
	 * @return
	 */
	@PostMapping("/cycles-per-store/status")
	public ResponseEntity<ApiResponse> getStockTakeCycleStatusPerStore(@RequestBody String storeData, OAuth2Authentication oAuth2Authentication) {
		
		System.out.println("storeData: "+storeData);		// storeData: 2/2-4,2-2,2-1
		
		String storeId = "";
		ArrayList<String> list=new ArrayList<String>();
		
		String arrayFromStoreData[]= storeData.split("/");
		for (int i=0 ; i<arrayFromStoreData.length; i++){
			if(arrayFromStoreData[i] != null) {
				if(i == 0)
					storeId = arrayFromStoreData[i].toString();
				else if(i == 1)
				{
					String strTemp = arrayFromStoreData[i].toString();
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
		return ResponseGenerator.successResponse(stockTakeCycleService.getStockTakeCycleStatusPerStore(storeId, list));
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
	 * Get All Active Stock Take Cycle for Selected DSM User
	 * 
	 * @return
	 */
	@GetMapping("/stock-cycle-dsm-user/{userId}/{role}/{sortBy}")
	public ResponseEntity<ApiResponse> getAllActiveStockTakeCycleDsmUser(@PathVariable Long userId, @PathVariable String role, @PathVariable int sortBy ,OAuth2Authentication oAuth2Authentication) {
		
		return ResponseGenerator.successResponse(stockTakeCycleService.getAllActiveStockTakeCycleDsmUser(userId, role, sortBy));
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
	@GetMapping("/getStockTakeCycleData/{stockTakeCycleId}")
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
}
