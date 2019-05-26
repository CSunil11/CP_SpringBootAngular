package com.ackermans.criticalpath.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ackermans.criticalpath.entity.ApiLogger;
import com.ackermans.criticalpath.repository.ApiLoggerRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.qos.logback.classic.Logger;

@Service
public class ApiLoggerService {

	@Autowired
	ApiLoggerRepository apiLoggerRepository;
	
	private final org.apache.logging.log4j.Logger logger = LogManager.getFormatterLogger(ApiLoggerService.class);

	public void saveApiLog(Long transactionId, String module, Long thirdPartyId, String status, String actionBy, String remark, String msgId) {

		ApiLogger apiLogger = new ApiLogger();
		apiLogger.setTransactionId(transactionId);
		apiLogger.setModule(module);
		apiLogger.setThirdPartyId(thirdPartyId);
		apiLogger.setStatus(status);
		apiLogger.setActionBy(actionBy);
		apiLogger.setRemark(remark);
		apiLogger.setMsgId(msgId);
		
		ObjectMapper om = new ObjectMapper();
		String apiLoggerToString = null;
		
		try {
			apiLoggerToString = om.writeValueAsString(apiLogger);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			apiLoggerRepository.save(apiLogger);
			
		} catch (Exception e) {
			// TODO: handle exception
			logger.debug("Error while saving Apilogger object: "+apiLoggerToString);
			logger.debug("Error while saving Apilogger object: "+e);
		}
		
			
		
		
	}

	public List<ApiLogger> findByTransOrMsgId(String searchString , String searchDate) {
		
		List<ApiLogger> logs = new ArrayList<ApiLogger>();

		if(searchDate != null && !searchDate.equalsIgnoreCase("undefined"))
		{
			
			String formatedDate = null;
			try {
				Date stockTakeDt = new SimpleDateFormat("dd-mm-yyyy").parse(searchDate);
				SimpleDateFormat format1 = new SimpleDateFormat("yyyy-mm-dd");
				formatedDate = format1.format(stockTakeDt);
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
			
			if(!searchString.equalsIgnoreCase("undefined") && !searchString.equalsIgnoreCase("null") && !searchDate.equalsIgnoreCase("undefined"))
				logs = apiLoggerRepository.findByTransOrMsgIdAndCreatedDate(searchString, formatedDate);
			else
				logs = apiLoggerRepository.findByCreatedDate(formatedDate);
			
		}
		else if(searchString != null)
		{
			logs = apiLoggerRepository.findByTransOrMsgId(searchString);
		}
		
		Map<Object, List<ApiLogger>> logDetails = logs.stream()
				.collect(Collectors.groupingBy(p -> p.getTransactionId()));
		
		List<ApiLogger> apiList = new ArrayList(logDetails.values());
		return apiList;
	}
}
