package com.ackermans.criticalpath.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ackermans.criticalpath.common.dto.ApiResponse;
import com.ackermans.criticalpath.common.dto.CPException;
import com.ackermans.criticalpath.common.service.ResponseGenerator;
import com.ackermans.criticalpath.service.StockTakeCycleService;
import com.ackermans.criticalpath.service.UserLoginService;

@RestController("userStockTakeCycleController")
@RequestMapping("/user/stocktakecycle")
public class StockTakeCycleController {
	
	private static final Logger logger = LogManager.getFormatterLogger(StockTakeCycleController.class);
	
	@Autowired
	private StockTakeCycleService stockTakeCycleService;
	
	/**
	 * API for get stock take cycle
	 * 
	 * @param oAuth2Authentication
	 * @return
	 * @throws CPException
	 */
	@GetMapping("/getall")
	public ResponseEntity<ApiResponse> getAllStockTakeCycle(OAuth2Authentication oAuth2Authentication) throws CPException  {
		return ResponseGenerator.successResponse(stockTakeCycleService.getByUserEmail(oAuth2Authentication.getName()));	
	}

}
