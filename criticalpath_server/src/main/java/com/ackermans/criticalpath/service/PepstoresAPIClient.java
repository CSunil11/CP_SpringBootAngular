package com.ackermans.criticalpath.service;

import java.io.IOException;
import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.ackermans.criticalpath.utils.Constants;
import com.ackermans.criticalpath.utils.DateUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class PepstoresAPIClient {
	
	@Autowired
	private ApiLoggerService apiLoggerService;
	
	private Logger logger = LogManager.getFormatterLogger(getClass());
	
	private final String AUTH_URL = "https://api-gw-qa.pepstores.com/token";
	
//	private final String ENDPOINT_URL = "https://api-gw-qa.pepstores.com/Ukusa_Inbound_Queue_API/1.0.0";
	private final String ENDPOINT_URL = "https://api-gw-qa.pepstores.com:443/UkusaInboundAPI/1.0.0"; 
	
	private final String USERNAME = "5SQthOfkkUlNGq4ibZxcRtCyFHka";
	
	private final String PASSWORD = "i1ejBE2SNHrkAy4mJnutXGIyG1Qa";

	@Autowired
	private RestTemplate restTemplate;

	/**
	 * Submit the Stock Take Date
	 * 
	 * @param date
	 */
	public boolean sendCycleCompletionDate(Long companyId, String branchId, Long ramId, Long dsmId, String cycleCompletionDate, String amOrPm) {

		return this.makeStockTakeDateCall(companyId, branchId, ramId, dsmId, cycleCompletionDate, amOrPm, "CREATE");
	}
	
	/**
	 * @param companyId
	 * @param branchId
	 * @param ramId
	 * @param dsmId
	 * @param cycleCompletionDate
	 * @param amOrPm
	 * @return
	 */
	public boolean sendCycleWhileUpdateForTime(Long companyId, String branchId, Long ramId, Long dsmId, String cycleCompletionDate, String amOrPm) {

		return this.makeStockTakeDateCall(companyId, branchId, ramId, dsmId, cycleCompletionDate, amOrPm, "UPDATE");
	}

	
	/**
	 * Delete stock take cycle
	 * 
	 * @param date
	 */
	public boolean sendDeleteCycle(Long companyId, String branchCode, Long ramId, Long dsmId, String cycleCompletionDate, String amOrPm) {
		
		return this.makeStockTakeDateCall(companyId, branchCode, ramId, dsmId, cycleCompletionDate, amOrPm, "DELETE");
	}
	
	/**
	 * Submit stock take result of DSM
	 * 
	 * @param companyId
	 * @param branchCode
	 * @param ramId
	 * @param dsmId
	 * @param cycleCompletionDate
	 * @param amOrPm
	 * @param approvedDSMDate
	 * @param rejectedDSMDate
	 * @param rejectedDSMReason
	 */
	public boolean sendStockTakeResultStatusOfDSM(Long companyId, String branchCode, Long ramId, Long dsmId, 
			String cycleCompletionDate, String amOrPm, 
			String approvedDSMDate, String rejectedDSMDate, String rejectedDSMReason) {
		
		return this.makeStockTakeResultCall(companyId, branchCode, ramId, dsmId, cycleCompletionDate, amOrPm, approvedDSMDate, rejectedDSMDate, rejectedDSMReason, null, null, null);
	}
	
	/**
	 * Submit stock take result of RAM
	 * 
	 * @param companyId
	 * @param branchCode
	 * @param ramId
	 * @param dsmId
	 * @param cycleCompletionDate
	 * @param amOrPm
	 * @param approvedRAMDate
	 * @param rejectedRAMDate
	 * @param rejectedRAMReason
	 */
	public boolean sendStockTakeResultStatusOfRAM(Long companyId, String branchCode, Long ramId, Long dsmId, 
			String cycleCompletionDate, String amOrPm,
			String approvedRAMDate, String rejectedRAMDate, String rejectedRAMReason) {
		
		return this.makeStockTakeResultCall(companyId, branchCode, ramId, dsmId, cycleCompletionDate, amOrPm, null, null, null, approvedRAMDate, rejectedRAMDate, rejectedRAMReason);
	}
	
	/**
	 * Submit stock take result of DSM and RAM
	 * 
	 * @param companyId
	 * @param branchCode
	 * @param ramId
	 * @param dsmId
	 * @param cycleCompletionDate
	 * @param amOrPm
	 * @param approvedRAMDate
	 * @param rejectedRAMDate
	 * @param rejectedRAMReason
	 */
	public void sendStockTakeResultStatusOfDSMAndRAM(Long companyId, String branchCode, Long ramId, Long dsmId, 
			String cycleCompletionDate, String amOrPm,
			String approvedDSMDate, String rejectedDSMDate, String rejectedDSMReason,
			String approvedRAMDate, String rejectedRAMDate, String rejectedRAMReason) {
		
		this.makeStockTakeResultCall(companyId, branchCode, ramId, dsmId, cycleCompletionDate, amOrPm, null, null, null, approvedRAMDate, rejectedRAMDate, rejectedRAMReason);
	}
	
	private boolean makeStockTakeResultCall(Long companyId, String branchCode, Long ramId, Long dsmId, 
			String cycleCompletionDate, String amOrPm, 
			String approvedDSMDate, String rejectedDSMDate, String rejectedDSMReason,
			String approvedRAMDate, String rejectedRAMDate, String rejectedRAMReason) {
		
		Long transactionId = this.getTransactionId();
		
		apiLoggerService.saveApiLog(transactionId, Constants.NA, Long.valueOf(0), Constants.SENDING, Constants.BYTHIRDPARTYID, "CompanyId: "+companyId+", BranchId: "+branchCode+", Cycle stock take date: "+cycleCompletionDate+", and Time is "+amOrPm +" will be published", Constants.NA);
		String accessToken = this.authenticate(transactionId);
		
		String requestXML = this.buildRequestXMLToSubmitResult(companyId, branchCode, ramId, dsmId, cycleCompletionDate, amOrPm, approvedDSMDate, rejectedDSMDate, rejectedDSMReason, approvedRAMDate, rejectedRAMDate, rejectedRAMReason);
		
		HttpHeaders headers = new HttpHeaders();

		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.set("Authorization", "Bearer "+accessToken);
		headers.set("SOAPAction", "submit");
		headers.set("Content-Type", "text/xml");
		headers.set("Accept", "text/xml; charset=UTF-8");
		
		logger.debug("SOAP Request :: "+requestXML);
		apiLoggerService.saveApiLog(transactionId, Constants.NA, Long.valueOf(0), Constants.SENDING, Constants.BYTHIRDPARTYID, "Request XML generated: "+requestXML, Constants.NA);
		HttpEntity<String> request = new HttpEntity<String>(requestXML, headers);
		
		try {
			
			ResponseEntity<String> response  = restTemplate.postForEntity(URI.create(ENDPOINT_URL), request, String.class);
			logger.debug("XML Response " + response.getBody());
			
			if(response.getBody() != null) {
				apiLoggerService.saveApiLog(transactionId, Constants.NA, Long.valueOf(0), Constants.SENT, Constants.BYTHIRDPARTYID, "Response generated: "+response.getBody(), Constants.NA);
				return true;
			}
			else {
				apiLoggerService.saveApiLog(transactionId, Constants.NA, Long.valueOf(0), Constants.FAILED, Constants.BYTHIRDPARTYID, "Some issue happened with response : ", Constants.NA);
				return false;
			}
			
		} catch (Exception e) {
			logger.error("Error while submitting delete stock cycle :: ", e);
			apiLoggerService.saveApiLog(transactionId, Constants.NA, Long.valueOf(0), Constants.FAILED, Constants.BYTHIRDPARTYID, "Error generated while getting response: "+e.getMessage(), Constants.NA);
		}
		return false;
	}
	
	/**
	 * Make call to submit date for create and to delete
	 * 
	 * @param companyId
	 * @param branchCode
	 * @param ramId
	 * @param dsmId
	 * @param cycleCompletionDate
	 * @param amOrPm
	 * @param transactionType
	 */
	private boolean makeStockTakeDateCall(Long companyId, String branchCode, Long ramId, Long dsmId, String cycleCompletionDate, String amOrPm, String transactionType) {

		Long transactionId = this.getTransactionId();
		
		apiLoggerService.saveApiLog(transactionId, Constants.NA, Long.valueOf(0), Constants.SENDING, Constants.BYTHIRDPARTYID, "CompanyId: "+companyId+", BranchId: "+branchCode+", Cycle stock take date: "+cycleCompletionDate+", and Time is "+amOrPm +" will be published", Constants.NA);
		String accessToken = this.authenticate(transactionId);
		
		String requestXML = this.buildRequestXMLToSubmitDate(companyId, branchCode, ramId, dsmId, cycleCompletionDate, amOrPm, transactionType);
		
		HttpHeaders headers = new HttpHeaders();

		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.set("Authorization", "Bearer "+accessToken);
		headers.set("SOAPAction", "submit");
		headers.set("Content-Type", "text/xml");
		headers.set("Accept", "text/xml; charset=UTF-8");
		
		logger.debug("SOAP Request :: "+requestXML);
		apiLoggerService.saveApiLog(transactionId, Constants.NA, Long.valueOf(0), Constants.SENDING, Constants.BYTHIRDPARTYID, "Request XML generated: "+requestXML, Constants.NA);
		HttpEntity<String> request = new HttpEntity<String>(requestXML, headers);
		
		try {
			
			ResponseEntity<String> response  = restTemplate.postForEntity(URI.create(ENDPOINT_URL), request, String.class);
			logger.debug("XML Response " + response.getBody());
			
			if(response.getBody() != null) {
				apiLoggerService.saveApiLog(transactionId, Constants.NA, Long.valueOf(0), Constants.SENT, Constants.BYTHIRDPARTYID, "Response generated: "+response.getBody(), Constants.NA);
				return true;
			}
			else {
				apiLoggerService.saveApiLog(transactionId, Constants.NA, Long.valueOf(0), Constants.FAILED, Constants.BYTHIRDPARTYID, "Some issue happened with response : ", Constants.NA);
				return false;
			}
			
		} catch (Exception e) {
			logger.error("Error while submitting stock take date :: ", e);
			apiLoggerService.saveApiLog(transactionId, Constants.NA, Long.valueOf(0), Constants.FAILED, Constants.BYTHIRDPARTYID, "Error generated while getting response: "+e.getMessage(), Constants.NA);
			return false;
		}
	}
	
	/**
	 * Make authentication call to get access token
	 */
	private String authenticate(Long transactionId) {

		HttpHeaders headers = new HttpHeaders();

		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.set("Authorization", "Basic "+getEncodedCredentails());

		MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
		map.add("grant_type", "client_credentials");

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

		ResponseEntity<String> response = restTemplate.postForEntity(AUTH_URL, request, String.class);
		
		logger.debug("Auth Response == "+ response);
		logger.debug("Auth Response == "+ response.getBody());
		
		ObjectMapper mapper = new ObjectMapper();
	    String accessToken = null;
		try {
			JsonNode actualObj = mapper.readTree(response.getBody());
			logger.debug("Access Token == "+ actualObj.get("access_token").asText());
			accessToken = actualObj.get("access_token").asText();
			
			apiLoggerService.saveApiLog(transactionId, Constants.NA, Long.valueOf(0), Constants.SENDING, Constants.BYTHIRDPARTYID, "Access token generated: "+accessToken, Constants.NA);
		} catch (IOException e) {
			logger.error("Error while parsing auth response :: ", e);
			apiLoggerService.saveApiLog(transactionId, Constants.NA, Long.valueOf(0), Constants.FAILED, Constants.BYTHIRDPARTYID, "Error while generating access token: "+e.getMessage(), Constants.NA);
		}
		
		return accessToken;
	}
	
	/**
	 * Get Base64 encoded string for username and password
	 * 
	 * @param args
	 * @return
	 */
	private String getEncodedCredentails() {
		String authStr = USERNAME +":"+ PASSWORD;
		return Base64
				.getEncoder()
				.encodeToString(authStr.getBytes());
	}
	
	/**
	 * Get current timestamp as string
	 * 
	 * @return
	 */
	private Long getTransactionId() {
		
		Long transactionId = UUID.randomUUID().getMostSignificantBits();
		if(transactionId<0)
			transactionId = 0-transactionId;
		System.out.println("transactionId: "+transactionId);
		
		return transactionId;
	}
	
	/**
	 * Prepare SOAP Request XML
	 * 
	 * @param companyId
	 * @param branchCode
	 * @param ramId
	 * @param dsmId
	 * @param cycleCompletionDate
	 * @param amOrPm
	 * @return
	 */
	private String buildRequestXMLToSubmitDate(Long companyId, String branchCode, Long ramId, Long dsmId, String cycleCompletionDate, String amOrPm, String transactionType) {
		StringBuffer request = new StringBuffer();
		
		//SOAP Header
		request.append("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"><soapenv:Header xmlns:meta=\"http://schema.star-group.co.za/integration/Meta\"><meta:Meta><meta:MessageType>http://schema.star-group.co.za/codemaven/1.0/StockCount</meta:MessageType><meta:EntityCode>ACK</meta:EntityCode>");
		/*
		 * <meta:TransactionType>CREATE</meta:TransactionType>
		 * 1) We will send an create for every new stock count date that is created.
		 * 2) We will send a delete everytime a stock count is cancelled or deleted.
		 */
		request.append("<meta:TransactionType>");
		request.append(transactionType);
		request.append("</meta:TransactionType>");
		request.append("<meta:SrcSystemCode>SCA</meta:SrcSystemCode><meta:SrcSystemInstanceCode>ACK</meta:SrcSystemInstanceCode>");
		
		//unique message id to track request
		request.append("<meta:MessageId>");
		request.append(getTransactionId()+"");
		request.append("</meta:MessageId>");
		
		//creation date is today's date & time
		request.append("<meta:CreationTime>");
		request.append(DateUtils.getCurrentDateInISOFormat());
		request.append("</meta:CreationTime>");
		
		request.append("<meta:MessageSequence>-1</meta:MessageSequence><meta:DestSystemCode>UKU</meta:DestSystemCode><meta:DestSystemInstanceCode>ACK</meta:DestSystemInstanceCode></meta:Meta></soapenv:Header>");
		
		//SOAP Body
		request.append("<soapenv:Body xmlns=\"http://schema.star-group.co.za/codemaven/1.0/StockCount\"><StockCount>");
		
		request.append("<CompanyIdNo>");
		request.append(companyId);
		request.append("</CompanyIdNo>");
		
		request.append("<BranchNo>");
		request.append(branchCode);
		request.append("</BranchNo>");
		
		request.append("<RamIdNo>");
		request.append(ramId);
		request.append("</RamIdNo>");
		
		request.append("<DsmIdNo>");
		request.append(dsmId);
		request.append("</DsmIdNo>");
		
//		String dateStr = DateUtils.dateToString(cycleCompletionDate);
//		request.append("<CompanyStockCountDate>");
//		request.append(amOrPm.equalsIgnoreCase("AM") ? dateStr+"T00:00:01.0000" : dateStr+"T12:00:01.0000");
//		request.append("</CompanyStockCountDate>");
		
//		String dateStr = DateUtils.dateToString(cycleCompletionDate);
		
		SimpleDateFormat dtSample = new SimpleDateFormat("dd/mm/yyyy");
		SimpleDateFormat dtSample1 = new SimpleDateFormat("mm-dd-yyyy");
		SimpleDateFormat dtSample2 = new SimpleDateFormat("mm/dd/yyyy");
		SimpleDateFormat requiredDateFormat = new SimpleDateFormat("yyyy-mm-dd");
		Date dateParsed = null;

		try {
			dateParsed = dtSample.parse(cycleCompletionDate);
		} catch (ParseException e) {

			try {
				dateParsed = dtSample1.parse(cycleCompletionDate);
			} catch (Exception e2) {
				
				try {
					dateParsed = dtSample2.parse(cycleCompletionDate);
				} catch (Exception e3) {
					logger.debug("CycleCompletionDate is null");
					e.printStackTrace();
				}
			}
		}

		try {
			cycleCompletionDate = requiredDateFormat.format(dateParsed);
		} catch (Exception e) {
			cycleCompletionDate = null;
		}
		
		request.append("<StockCountDate>");
		request.append(amOrPm.equalsIgnoreCase("AM") ? cycleCompletionDate+"T00:00:01.0000" : cycleCompletionDate+"T12:00:01.0000");
		request.append("</StockCountDate>");
		
		request.append("<AmPmInd>");
		request.append(amOrPm.toUpperCase());
		request.append("</AmPmInd>");
		
		request.append("</StockCount></soapenv:Body></soapenv:Envelope>");
		
		return request.toString();
	}
	
	/**
	 * Prepare SOAL request XML for submitting stock take result
	 * 
	 * @param companyId
	 * @param branchCode
	 * @param ramId
	 * @param dsmId
	 * @param cycleCompletionDate
	 * @param amOrPm
	 * @param approvedDate
	 * @return
	 */
	private String buildRequestXMLToSubmitResult(Long companyId, String branchCode, Long ramId, Long dsmId, 
			String cycleCompletionDate, String amOrPm, 
			String approvedDSMDate, String rejectedDSMDate, String rejectedDSMReason,
			String approvedRAMDate, String rejectedRAMDate, String rejectedRAMReason) {
		
		StringBuffer request = new StringBuffer();
		
		//SOAP Header
		request.append("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"><soapenv:Header xmlns:meta=\"http://schema.star-group.co.za/integration/Meta\"><meta:Meta><meta:MessageType>http://schema.star-group.co.za/codemaven/1.0/StockCount</meta:MessageType><meta:EntityCode>ACK</meta:EntityCode><meta:TransactionType>STATUS</meta:TransactionType><meta:SrcSystemCode>SCA</meta:SrcSystemCode><meta:SrcSystemInstanceCode>ACK</meta:SrcSystemInstanceCode>");
		
		//unique message id to track request
		request.append("<meta:MessageId>");
		request.append(getTransactionId()+"");
		request.append("</meta:MessageId>");
		
		//creation date is today's date & time
		request.append("<meta:CreationTime>");
		request.append(DateUtils.getCurrentDateInISOFormat());
		request.append("</meta:CreationTime>");
		
		request.append("<meta:MessageSequence>-1</meta:MessageSequence><meta:DestSystemCode>UKU</meta:DestSystemCode><meta:DestSystemInstanceCode>ACK</meta:DestSystemInstanceCode></meta:Meta></soapenv:Header>");
		
		//SOAP Body
		request.append("<soapenv:Body xmlns=\"http://schema.star-group.co.za/codemaven/1.0/StockCount\"><StockCount>");
		
		request.append("<CompanyIdNo>");
		request.append(companyId);
		request.append("</CompanyIdNo>");
		
		request.append("<BranchNo>");
		request.append(branchCode);
		request.append("</BranchNo>");
		
		request.append("<RamIdNo>");
		request.append(ramId);
		request.append("</RamIdNo>");
		
		request.append("<DsmIdNo>");
		request.append(dsmId);
		request.append("</DsmIdNo>");
		
		SimpleDateFormat dtSample = new SimpleDateFormat("dd/mm/yyyy");
		SimpleDateFormat dtSample1 = new SimpleDateFormat("mm-dd-yyyy");
		SimpleDateFormat dtSample2 = new SimpleDateFormat("mm/dd/yyyy");
		SimpleDateFormat requiredDateFormat = new SimpleDateFormat("yyyy-mm-dd");
		Date dateParsed = null;

		try {
			dateParsed = dtSample.parse(cycleCompletionDate);
		} catch (ParseException e) {

			try {
				dateParsed = dtSample1.parse(cycleCompletionDate);
			} catch (Exception e2) {
				
				try {
					dateParsed = dtSample2.parse(cycleCompletionDate);
				} catch (Exception e3) {
					logger.debug("CycleCompletionDate is null");
					e.printStackTrace();
				}
			}
		}

		try {
			cycleCompletionDate = requiredDateFormat.format(dateParsed);
		} catch (Exception e) {
			cycleCompletionDate = null;
		}
		
		request.append("<StockCountDate>");
		request.append(amOrPm.equalsIgnoreCase("AM") ? cycleCompletionDate+"T00:00:01.0000" : cycleCompletionDate+"T12:00:01.0000");
		request.append("</StockCountDate>");
		
		request.append("<AmPmInd>");
		request.append(amOrPm.toUpperCase());
		request.append("</AmPmInd>");
		
		
		request.append("<ApproveDsmDate>");
		approvedDSMDate = convertDateAlongWithTime(approvedDSMDate, dtSample, dtSample1, dtSample2, requiredDateFormat);
		if(approvedDSMDate!=null)
			request.append(amOrPm.equalsIgnoreCase("AM") ? approvedDSMDate+"T00:00:01.0000" : approvedDSMDate+"T12:00:01.0000");
		else
			request.append(approvedDSMDate);
		request.append("</ApproveDsmDate>");
		
		
		request.append("<RejectDsmDate>");
		rejectedDSMDate = convertDateAlongWithTime(rejectedDSMDate, dtSample, dtSample1, dtSample2, requiredDateFormat);
		if(rejectedDSMDate != null)
			request.append(amOrPm.equalsIgnoreCase("AM") ? rejectedDSMDate+"T00:00:01.0000" : rejectedDSMDate+"T12:00:01.0000");
		else
			request.append(rejectedDSMDate);
		request.append("</RejectDsmDate>");
		
		
		request.append("<RejectDsmReason>");
		request.append(rejectedDSMReason);
		request.append("</RejectDsmReason>");
		
		request.append("<ApproveRamDate>");
		approvedRAMDate = convertDateAlongWithTime(approvedRAMDate, dtSample, dtSample1, dtSample2, requiredDateFormat);
		if(approvedRAMDate != null)
			request.append(amOrPm.equalsIgnoreCase("AM") ? approvedRAMDate+"T00:00:01.0000" : approvedRAMDate+"T12:00:01.0000");
		else
			request.append(approvedRAMDate);
		request.append("</ApproveRamDate>");
		
		request.append("<RejectRamDate>");
		rejectedRAMDate = convertDateAlongWithTime(rejectedRAMDate, dtSample, dtSample1, dtSample2, requiredDateFormat);
		if(rejectedRAMDate != null)
			request.append(amOrPm.equalsIgnoreCase("AM") ? rejectedRAMDate+"T00:00:01.0000" : rejectedRAMDate+"T12:00:01.0000");
		else
			request.append(rejectedRAMDate);
		request.append("</RejectRamDate>");
		
		request.append("<RejectRamReason>");
		request.append(rejectedRAMReason);
		request.append("</RejectRamReason>");
		
		request.append("</StockCount></soapenv:Body></soapenv:Envelope>");
		
		return request.toString();
	}

	private String convertDateAlongWithTime(String rejectedDSMDate, SimpleDateFormat dtSample,
			SimpleDateFormat dtSample1, SimpleDateFormat dtSample2, SimpleDateFormat requiredDateFormat) {
		Date dateParsedForRejectDsmDate = null;

		try {
			dateParsedForRejectDsmDate = dtSample.parse(rejectedDSMDate);
		} catch (Exception e) {

			try {
				dateParsedForRejectDsmDate = dtSample1.parse(rejectedDSMDate);
			} catch (Exception e2) {
				
				try {
					dateParsedForRejectDsmDate = dtSample2.parse(rejectedDSMDate);
				} catch (Exception e3) {
					logger.debug("rejectedDSMDate is null");
				}
			}
		}

		try {
			rejectedDSMDate = requiredDateFormat.format(dateParsedForRejectDsmDate);
		} catch (Exception e) {
			rejectedDSMDate = null;
		}
		return rejectedDSMDate;
	}
}
