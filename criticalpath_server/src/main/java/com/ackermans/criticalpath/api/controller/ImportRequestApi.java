package com.ackermans.criticalpath.api.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ackermans.criticalpath.service.ApiLoggerService;

import com.ackermans.criticalpath.service.BrandService;
import com.ackermans.criticalpath.service.LocationDivisionService;
import com.ackermans.criticalpath.service.ProvinceService;
import com.ackermans.criticalpath.service.StockTakeCycleService;
import com.ackermans.criticalpath.service.StockTakeResultService;
import com.ackermans.criticalpath.service.StoreService;
import com.ackermans.criticalpath.service.UserService;
import com.ackermans.criticalpath.utils.Constants;
import com.api.criticalpath.dto.RequestApiDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/data")
public class ImportRequestApi {

	@Autowired
	ProvinceService provinceService;
	
	@Autowired
	BrandService brandService;
	
	@Autowired
	StoreService storeService;
	
	@Autowired
	LocationDivisionService locationDivisionService;
	
	@Autowired
	StockTakeResultService stockTakeResultService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	private ApiLoggerService apiLoggerService;

	@PostMapping(path = "/update")
	public String apiRequestFetcher(@RequestBody RequestApiDto apiDto) {
		
		Long transactionId = UUID.randomUUID().getMostSignificantBits();
		if(transactionId<0)
			transactionId = 0-transactionId;
		System.out.println(transactionId);
		
		ObjectMapper om = new ObjectMapper();
		
		try {
			apiLoggerService.saveApiLog(transactionId, Constants.NA, Long.valueOf(0), Constants.RECEIVED, Constants.NA, om.writeValueAsString(apiDto), apiDto.getMsgId());
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		switch (apiDto.getType()) {
		case "province": {
			provinceService.saveImportedProvince(apiDto, transactionId);
			break;
		}
		case "brand": {
			brandService.saveImportedBrand(apiDto, transactionId);
			break;
		}
		case "store": {
			storeService.saveImportedStore(apiDto, transactionId);
			break;
		}
		case "location_division": {
			locationDivisionService.saveImportedLocDiv(apiDto, transactionId);
			break;
		}
		case "user_sm":
		case "user_dsm":
		case "user_ram": {
			userService.saveImportedUser(apiDto, transactionId);
			break;
		}
		
		case "stock_take_result": {
			stockTakeResultService.saveImportedStockTakeResult(apiDto, transactionId);
			break;
		}

		default:
			return "Invalid data found";
		}

		return "Data has been received successfully, Your transaction id is "+transactionId;
	}
}
