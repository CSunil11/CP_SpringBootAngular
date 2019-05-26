package com.ackermans.criticalpath.service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.annotation.Aspect;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ackermans.criticalpath.aop.service.AuditService;
import com.ackermans.criticalpath.common.dto.CPException;
import com.ackermans.criticalpath.email.EmailSenderService;
import com.ackermans.criticalpath.email.MailContentBuilder;
import com.ackermans.criticalpath.entity.AckStockTakeCycle;
import com.ackermans.criticalpath.entity.StockTakeResult;
import com.ackermans.criticalpath.entity.Store;
import com.ackermans.criticalpath.enums.ErrorStatus;
import com.ackermans.criticalpath.repository.AckStockTakeCycleRepository;
import com.ackermans.criticalpath.repository.StockTakeResultRepository;
import com.ackermans.criticalpath.repository.StoreRepository;
import com.ackermans.criticalpath.repository.UserLoginRepository;
import com.ackermans.criticalpath.repository.UserRepository;
import com.ackermans.criticalpath.utils.Constants;
import com.api.criticalpath.dto.RequestApiDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@Transactional
public class StockTakeResultService {

	private final Logger logger = LogManager.getFormatterLogger();

	@Autowired
	private StockTakeResultRepository stockTakeResultRepository;

	@Autowired
	private ApiLoggerService apiLoggerService;

	@Autowired
	private StoreRepository storeRepository;

	@Autowired
	private AckStockTakeCycleRepository ackStockTakeCycleRepository;

	@Autowired
	private AckStockTakeCycleService ackStockTakeCycleService;

	@Autowired
	private PepstoresAPIClient pepstoresAPIClient;

	@Autowired
	private MailContentBuilder mailcontentbuilder;

	@Autowired
	private EmailSenderService emailsenderservice;

	@Autowired
	private UserLoginRepository userLoginRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AuditService auditService;

	private static final String MAIL_STATUS_UPDATE = "Approved Declined Status Update";
	
	private static final String ERROR_STATUS_UPDATE = "Your answer for stock take result could not be processed. Please try again later.";

	@Async
	public void saveImportedStockTakeResult(RequestApiDto apiDtoObj, Long transactionId) {

		if (apiDtoObj.getData().size() > 0) {
			for (int i = 0; i < apiDtoObj.getData().size(); i++) {

//				if(validateLocationDivision(transactionId, apiDtoObj.getData().get(i), apiDtoObj)) {

				StockTakeResult str = new StockTakeResult();
				ObjectMapper omOb = new ObjectMapper();
				try {
					str.setStockTakeResultObject(omOb.writeValueAsString(apiDtoObj));

					String branchNameReq = apiDtoObj.getData().get(i).getBranch_name().trim();
					if (branchNameReq != null) {
						Store storeFromDb = storeRepository.findByNameIgnoreCaseAndIsActiveTrue(branchNameReq);
						if (storeFromDb != null) {
							str.setStore(storeFromDb);
						} else {
							str.setStore(null);
						}
					} else {
						str.setStore(null);
					}

				} catch (Exception e) {
					str.setStockTakeResultObject(null);
				}

				try {
					stockTakeResultRepository.save(str);
					apiLoggerService.saveApiLog(transactionId, Constants.STOCKTAKERESULT, Long.valueOf(0),
							Constants.INSERTED, Constants.BYNAME, Constants.SUCCESSINS, null);
				} catch (Exception e) {
					apiLoggerService.saveApiLog(transactionId, Constants.STOCKTAKERESULT, Long.valueOf(0),
							Constants.FAILED, Constants.BYNAME, Constants.ERRORWHILESAVE, null);
				}

//				}
			}
		} else
			apiLoggerService.saveApiLog(transactionId, Constants.STOCKTAKERESULT, Long.valueOf(0), Constants.NOTFOUND,
					Constants.SKIPPED, "Data parameter is empty in request", null);
	}

	public Map<String, Object> getStockTakeResult(Long storeId) {
		List<StockTakeResult> stockTakeResults = stockTakeResultRepository
				.findByStore_IdOrderByCreatedDateTimeDesc(storeId);
		Map<String, Object> cycleData = new HashMap();

		if (stockTakeResults.size() > 0) {
			String jsonStr = stockTakeResults.get(0).getStockTakeResultObject();

			JSONObject obj = new JSONObject(jsonStr);
			JSONArray jsonArray = obj.getJSONArray("data");
			JSONObject jsonObj = jsonArray.getJSONObject(0);

			logger.debug("JSON STR " + jsonStr);

			for (String resultStatusAttr : Constants.RESULT_STATUS_ATTR) {

				if (jsonObj.has(resultStatusAttr) && !jsonObj.get(resultStatusAttr).equals(null)) {
					logger.debug("jsonObj.get(" + resultStatusAttr + ") => " + jsonObj.get(resultStatusAttr));
					cycleData.put(resultStatusAttr, jsonObj.get(resultStatusAttr));
				} else
					cycleData.put(resultStatusAttr, "");
			}

			logger.debug("After setting up data to cycledata map");

			cycleData.put("status_sm", stockTakeResults.get(0).getStatusSm());
			cycleData.put("status_dsm", stockTakeResults.get(0).getStatusDsm());
			cycleData.put("status_ram", stockTakeResults.get(0).getStatusRam());
			cycleData.put("reason_dsm", stockTakeResults.get(0).getReasonDsm());
			cycleData.put("reason_sm", stockTakeResults.get(0).getReasonSm());
			cycleData.put("reason_ram", stockTakeResults.get(0).getReasonRam());
			cycleData.put("date_sm", stockTakeResults.get(0).getDateSm());
			cycleData.put("date_ram", stockTakeResults.get(0).getDateRam());
			cycleData.put("date_dsm", stockTakeResults.get(0).getDateDsm());
			cycleData.put("id", stockTakeResults.get(0).getId());

			String dateArray = (String) jsonObj.get("PrevStockCountDate");
			if (dateArray != null) {
				if (dateArray.trim().indexOf(" ") != -1) {
					String[] dates = dateArray.split(" ");
					
					String dateFormate = null;
					String dateTime = null;
					if(dates[0].contains("-") || dates[0].contains("/")) {
						dateFormate = dates[0];
						dateTime =  dates[1];
					} else {
						dateFormate = dates[1];
						dateTime =  dates[0];
					}
					
					cycleData.put("PrevStockCountDateTime", dateTime);

					try {
						Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateFormate);
						cycleData.put("PrevStockCountDate", date);
					} catch (Exception e) {
						try {
							Date date = new SimpleDateFormat("dd/MM/yyyy").parse(dateFormate);
							cycleData.put("PrevStockCountDate", date);
						} catch (ParseException e1) {
							logger.debug("Error while parse PrevStockCountDate formate:" + e.getMessage());
						}
					}
				} else {
					try {
						Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateArray.trim());
						cycleData.put("PrevStockCountDate", date);
					} catch (Exception e) {
						try {
							Date date = new SimpleDateFormat("dd/MM/yyyy").parse(dateArray.trim());
							cycleData.put("PrevStockCountDate", date);
						} catch (ParseException e1) {
							logger.debug("Error while parse PrevStockCountDate formate:" + e.getMessage());
						}
					}
					cycleData.put("PrevStockCountDateTime", "");
				}
			}

			String stockCountDateArray = (String) jsonObj.get("StockCountDate");
			if (stockCountDateArray != null) {
				if (stockCountDateArray.trim().indexOf(" ") != -1) {
					String[] dates = stockCountDateArray.split(" ");
					
					String dateFormate = null;
					String dateTime = null;
					if(dates[0].contains("-") || dates[0].contains("/")) {
						dateFormate = dates[0];
						dateTime =  dates[1];
					} else {
						dateFormate = dates[1];
						dateTime =  dates[0];
					}
					cycleData.put("StockCountDateTime", dateTime);
					
					try {
						Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateFormate);
						cycleData.put("StockCountDate", date);
					} catch (Exception e) {
						try {
							Date date = new SimpleDateFormat("dd/MM/yyyy").parse(dateFormate);
							cycleData.put("StockCountDate", date);
						} catch (ParseException e1) {
							logger.debug("Error while parse StockCountDate formate:" + e.getMessage());
						}
					}
				} else {
					try {
						Date date = new SimpleDateFormat("yyyy-MM-dd").parse(stockCountDateArray.trim());
						cycleData.put("StockCountDate", date);
					} catch (Exception e) {
						try {
							Date date = new SimpleDateFormat("dd/MM/yyyy").parse(stockCountDateArray.trim());
							cycleData.put("StockCountDate", date);
						} catch (ParseException e1) {
							logger.debug("Error while parse StockCountDate formate:" + e.getMessage());
						}
					}
					cycleData.put("StockCountDateTime", "");
				}
			}

			String resultDateArray = (String) jsonObj.get("ResultDate");
			if (resultDateArray != null) {
				if (resultDateArray.trim().indexOf(" ") != -1) {
					String[] dates = resultDateArray.split(" ");
					
					String dateFormate = null;
					String dateTime = null;
					if(dates[0].contains("-") || dates[0].contains("/")) {
						dateFormate = dates[0];
						dateTime =  dates[1];
					} else {
						dateFormate = dates[1];
						dateTime =  dates[0];
					}
					
					try {
						Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateFormate);
						cycleData.put("ResultDate", date);
					} catch (Exception e) {
						try {
							Date date = new SimpleDateFormat("dd/MM/yyyy").parse(dateFormate);
							cycleData.put("ResultDate", date);
						} catch (ParseException e1) {
							logger.debug("Error while parse ResultDate formate:" + e.getMessage());
						}
					}
				} else {
					try {
						Date date = new SimpleDateFormat("yyyy-MM-dd").parse(resultDateArray.trim());
						cycleData.put("ResultDate", date);
					} catch (Exception e) {
						try {
							Date date = new SimpleDateFormat("dd/MM/yyyy").parse(resultDateArray.trim());
							cycleData.put("ResultDate", date);
						} catch (ParseException e1) {
							logger.debug("Error while parse ResultDate formate:" + e.getMessage());
						}
					}
				}
			}
				
			logger.debug("After setting up data to rResultData attr");

		} else {
			cycleData.put("id", null);
		}

		logger.debug("CycleData object has been prepared: " + cycleData);
		return cycleData;
	}

	public Object updateStockTakeResult(Long resultId, String reasonForDecline, String status, String userRole)
			throws CPException {
		StockTakeResult stockTakeResult = stockTakeResultRepository.findById(resultId).get();
		if (status.equalsIgnoreCase("A")) {
			if (userRole.equalsIgnoreCase("ROLE_SM_USER")) {
				stockTakeResult.setStatusSm("Approved");
				if (stockTakeResult.getReasonSm() != null) {
					stockTakeResult.setReasonSm(null);
				}
				stockTakeResult.setDateSm(new Date());
				stockTakeResultRepository.save(stockTakeResult);
			}
			if (userRole.equalsIgnoreCase("ROLE_DSM_USER")) {
				String time = ackStockTakeCycleRepository.findByStoreIdIfStillActive(stockTakeResult.getStore().getId())
						.getTime();
				Date date = ackStockTakeCycleRepository.findByStoreIdIfStillActive(stockTakeResult.getStore().getId())
						.getStokeTakeDate();
				String pattern = "MM-dd-yyyy";
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
				String stockTakeDateFormatted = simpleDateFormat.format(date);

				String approvedDateDsmFormatted = simpleDateFormat.format(new Date());

				try {
					if(sendXmlToPepstoreApiClientForDsm(stockTakeDateFormatted, stockTakeResult.getStore().getId(), time,
							approvedDateDsmFormatted, null, null)) {
					stockTakeResult.setStatusDsm("Approved");
					if (stockTakeResult.getReasonDsm() != null) {
						stockTakeResult.setReasonDsm(null);
					}
					stockTakeResult.setDateDsm(new Date());
					stockTakeResultRepository.save(stockTakeResult);
					}
					else
					{
						logger.debug("Error while Sending XML to pepstoreAPiClient for stockTakeResult status while updateStockTakeResult for store_id : "+stockTakeResult.getStore().getId());
						return ERROR_STATUS_UPDATE;
					}
					
				} catch (Exception e) {
					logger.debug("Error while Sending XML to pepstoreAPiClient for stockTakeResult status while updateStockTakeResult: " + e.getMessage());
					return ERROR_STATUS_UPDATE;
				}

			}
			if (userRole.equalsIgnoreCase("ROLE_RAM_USER")) {
				String time = ackStockTakeCycleRepository.findByStoreIdIfStillActive(stockTakeResult.getStore().getId())
						.getTime();
				Date date = ackStockTakeCycleRepository.findByStoreIdIfStillActive(stockTakeResult.getStore().getId())
						.getStokeTakeDate();
				String pattern = "MM-dd-yyyy";
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
				String stockTakeDateFormatted = simpleDateFormat.format(date);

				String approvedDateRamFormatted = simpleDateFormat.format(new Date());

				try {
					if(sendXmlToPepstoreApiClientForRam(stockTakeDateFormatted, stockTakeResult.getStore().getId(), time,
							approvedDateRamFormatted, null, null)) {
					stockTakeResult.setStatusRam("Approved");
					if (stockTakeResult.getReasonRam() != null) {
						stockTakeResult.setReasonRam(null);
					}
					stockTakeResult.setDateRam(new Date());
					stockTakeResultRepository.save(stockTakeResult);
					}
					else
					{
						logger.debug("Error while Sending XML to pepstoreAPiClient for stockTakeResult status while updateStockTakeResult with store_id: "+stockTakeResult.getStore().getId());
						return ERROR_STATUS_UPDATE;
					}
				} catch (Exception e) {
					logger.debug("Error while Sending XML to pepstoreAPiClient for stockTakeResult status: " + e.getMessage());
					return ERROR_STATUS_UPDATE;
				}
				
			}

		} else {
			if (userRole.equalsIgnoreCase("ROLE_SM_USER")) {
				stockTakeResult.setStatusSm("Declined");
				stockTakeResult.setReasonSm(reasonForDecline);
				stockTakeResult.setDateSm(new Date());
				stockTakeResultRepository.save(stockTakeResult);
			}
			if (userRole.equalsIgnoreCase("ROLE_DSM_USER")) {
				String time = ackStockTakeCycleRepository.findByStoreIdIfStillActive(stockTakeResult.getStore().getId())
						.getTime();
				Date date = ackStockTakeCycleRepository.findByStoreIdIfStillActive(stockTakeResult.getStore().getId())
						.getStokeTakeDate();
				String pattern = "MM-dd-yyyy";
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
				String stockTakeDateFormatted = simpleDateFormat.format(date);

				String rejectedDateDsmFormatted = simpleDateFormat.format(new Date());
				try {
					if(sendXmlToPepstoreApiClientForDsm(stockTakeDateFormatted, stockTakeResult.getStore().getId(), time, null,
							rejectedDateDsmFormatted, reasonForDecline)) {
					stockTakeResult.setStatusDsm("Declined");
					stockTakeResult.setReasonDsm(reasonForDecline);
					stockTakeResult.setDateDsm(new Date());
				    stockTakeResultRepository.save(stockTakeResult);
					}
					else
					{
						logger.debug("Error while Sending XML to pepstoreAPiClient for stockTakeResult status while updateStockTakeResult having store_id: " + stockTakeResult.getStore().getId());
						return ERROR_STATUS_UPDATE;
					}
				} catch (Exception e) {
					logger.debug("Error while Sending XML to pepstoreAPiClient for stockTakeResult status: " + e.getMessage());
					return ERROR_STATUS_UPDATE;
				}
				
			}
			if (userRole.equalsIgnoreCase("ROLE_RAM_USER")) {
				stockTakeResult.setStatusRam("Declined");
				stockTakeResult.setReasonRam(reasonForDecline);
				stockTakeResult.setDateRam(new Date());
			}
		}
		Date stockTakeDate = ackStockTakeCycleRepository.findByStoreIdIfStillActive(stockTakeResult.getStore().getId())
				.getStokeTakeDate();
		String pattern = "dd-MM-yyyy";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		String stockTakeDateFormate = simpleDateFormat.format(stockTakeDate);

		try {
			auditService.apprDecStatusUpdateAdvice(status, ackStockTakeCycleRepository
					.findByStoreIdIfStillActive(stockTakeResult.getStore().getId()).getName());
		} catch (Exception e) {
			logger.debug("Error in updateStockTakeResult while call audit service " + e.getMessage());
		}

		try {
			sendEmailApproveDeclineStatus(stockTakeResult.getStore().getId(), stockTakeDateFormate, status, userRole);
		} catch (CPException e) {
			e.printStackTrace();
		}
		 return null;
	}

	private boolean sendXmlToPepstoreApiClientForDsm(String stockTakeDate, Long storeId, String time,
			String approvedDSMDate, String rejectedDSMDate, String rejectedDSMReason) {

		Long companyTpId = null;
		try {
			companyTpId = storeRepository.findById(storeId).get().getBrand().getThirdPartyId();
		} catch (Exception e) {
		}

		String branchCode = null;
		try {
			branchCode = storeRepository.findById(storeId).get().getCode().trim();
		} catch (Exception e) {
		}

		Long ramTpId = null;
		try {
			ramTpId = storeRepository.findById(storeId).get().getRegionalManagers().getThirdPartyId();
		} catch (Exception e) {
		}

		Long dsmTpId = null;
		try {
			dsmTpId = storeRepository.findById(storeId).get().getLocationDivision().getDivisionalSalesManagers()
					.getThirdPartyId();
		} catch (Exception e) {
		}

		return pepstoresAPIClient.sendStockTakeResultStatusOfDSM(companyTpId, branchCode, ramTpId, dsmTpId, stockTakeDate,
				time, approvedDSMDate, rejectedDSMDate, rejectedDSMReason);

	}

	private boolean sendXmlToPepstoreApiClientForRam(String stockTakeDate, Long storeId, String time,
			String approvedRAMDate, String rejectedRAMDate, String rejectedRAMReason) {

		Long companyTpId = null;
		try {
			companyTpId = storeRepository.findById(storeId).get().getBrand().getThirdPartyId();
		} catch (Exception e) {
		}

		String branchCode = null;
		try {
			branchCode = storeRepository.findById(storeId).get().getCode().trim();
		} catch (Exception e) {
		}

		Long ramTpId = null;
		try {
			ramTpId = storeRepository.findById(storeId).get().getRegionalManagers().getThirdPartyId();
		} catch (Exception e) {
		}

		Long dsmTpId = null;
		try {
			dsmTpId = storeRepository.findById(storeId).get().getLocationDivision().getDivisionalSalesManagers()
					.getThirdPartyId();
		} catch (Exception e) {
		}

		return pepstoresAPIClient.sendStockTakeResultStatusOfRAM(companyTpId, branchCode, ramTpId, dsmTpId, stockTakeDate,
				time, approvedRAMDate, rejectedRAMDate, rejectedRAMReason);
	}

	public Object updateDeclineStatusRam(JsonNode ramDeclineJson) {
		Long resultId = ramDeclineJson.get("resultId").asLong();
		Long ackCycleId = ramDeclineJson.get("ackCycleId").asLong();
		String ramTime = ramDeclineJson.get("ramTime").asText();
		String ramReason = ramDeclineJson.get("ramReason").asText();
		String date = ramDeclineJson.get("stockTakeDate").asText();
		Date stockTakeDt = null;
		Date startDate = null;
		try {
			stockTakeDt = new SimpleDateFormat("dd/MM/yyyy").parse(date);
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
		
		Long store_id = ackStockTakeCycleRepository.findById(ackCycleId).get().getStore().getId();
		try {
			startDate = ackStockTakeCycleService.calculateStartDate(ackCycleId, date, store_id);
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}

		StockTakeResult stockTakeResult = stockTakeResultRepository.findById(resultId).get();
		AckStockTakeCycle ackStockTakeCycle = ackStockTakeCycleRepository.findById(ackCycleId).get();

		String time = ackStockTakeCycleRepository.findByStoreIdIfStillActive(stockTakeResult.getStore().getId())
				.getTime();
		Date dateNew = ackStockTakeCycleRepository.findByStoreIdIfStillActive(stockTakeResult.getStore().getId())
				.getStokeTakeDate();
		String pattern = "MM-dd-yyyy";
		SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat(pattern);
		String stockTakeDateFormatted = simpleDateFormat1.format(dateNew);
		String approvedDateRamFormatted = simpleDateFormat1.format(new Date());
		String stockTakeDateFormattedForEmail = simpleDateFormat1.format(dateNew);

		try {
			if(sendXmlToPepstoreApiClientForRam(stockTakeDateFormatted, stockTakeResult.getStore().getId(), time, null,
					approvedDateRamFormatted, ramReason)) {
			
			stockTakeResult.setStatusRam("Declined");
			stockTakeResult.setReasonRam(ramReason);
			stockTakeResult.setDateRam(new Date());
			stockTakeResultRepository.save(stockTakeResult);
			
			ackStockTakeCycle.setStokeStartDate(startDate);
			ackStockTakeCycle.setStokeTakeDate(stockTakeDt);
			ackStockTakeCycle.setTime(ramTime);
			ackStockTakeCycleRepository.save(ackStockTakeCycle);
			}
			else
			{
				logger.debug("Error while Sending XML to pepstoreAPiClient for stockTakeResult status while updateDeclineStatusRam for store_id: : "+stockTakeResult.getStore().getId());
				return ERROR_STATUS_UPDATE;
			}
			
		} catch (Exception e) {
			logger.debug("Error while Sending XML to pepstoreAPiClient for stockTakeResult status: " + e.getMessage());
			return ERROR_STATUS_UPDATE;
		}
	
		try {
			auditService.apprDecStatusUpdateAdvice("D", ackStockTakeCycle.getName());
		} catch (Exception e) {
			logger.debug("Error in updateStockTakeResult while call audit service " + e.getMessage());
		}

		try {
			sendEmailApproveDeclineStatus(stockTakeResult.getStore().getId(), stockTakeDateFormattedForEmail, "D",
					"ROLE_RAM_USER");
		} catch (CPException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void sendEmailApproveDeclineStatus(Long storeId, String StockTakeDate, String status, String userRole)
			throws CPException {

		List<String> userEmail = new ArrayList<String>();
		String statusupdateUserName = "";
		if (userRole.equalsIgnoreCase("ROLE_SM_USER")) {
			userEmail.add(userLoginRepository.getRamEmail(storeId));
			userEmail.add(userLoginRepository.getDsmEmail(storeId));
			statusupdateUserName = userRepository
					.findOneByUserLogin_Email(userRepository.findByStore_Id(storeId).getUserLogin().getEmail())
					.getName();
		}
		if (userRole.equalsIgnoreCase("ROLE_DSM_USER")) {
			userEmail.add(userLoginRepository.getRamEmail(storeId));
			userEmail.add(userRepository.findByStore_Id(storeId).getUserLogin().getEmail());
			statusupdateUserName = userRepository.findOneByUserLogin_Email(userLoginRepository.getDsmEmail(storeId))
					.getName();
		}
		if (userRole.equalsIgnoreCase("ROLE_RAM_USER")) {
			userEmail.add(userLoginRepository.getDsmEmail(storeId));
			userEmail.add(userRepository.findByStore_Id(storeId).getUserLogin().getEmail());
			statusupdateUserName = userRepository.findOneByUserLogin_Email(userLoginRepository.getRamEmail(storeId))
					.getName();
		}

		String message = "";
		String message1 = "";
		String htmlContent = "";

//		for(int i =0; i<userEmail.size();i++) {
		if (userEmail.get(0).toString() != null) {
			try {
				String userName = userRepository.findOneByUserLogin_Email(userEmail.get(0).toString()).getName();
				String updateStatus = "";
				if (status.equalsIgnoreCase("A")) {
					updateStatus = "approved";
				} else {
					updateStatus = "declined";
				}
				String storename = storeRepository.findById(storeId).get().getName();
				message = statusupdateUserName + " has been " + updateStatus + " the cycle of " + storename
						+ " having stock take date: " + StockTakeDate + ".";
				if (userName != null)
					htmlContent = mailcontentbuilder.buildSendApprovrDecStatus(userName, message);

				emailsenderservice.sendApproveDecStatus(userEmail, MAIL_STATUS_UPDATE, htmlContent, null, null, null);

			} catch (IOException | MessagingException e) {
				throw new CPException(ErrorStatus.EMAIL_SEND_FAILED, userEmail.get(0).toString());
			}
		} else {
			throw new CPException(ErrorStatus.EMAIL_NOT_FOUND, userEmail.get(0).toString());
		}
//		}

		try {
			auditService.generateAuditMessageForSendEmail(userEmail, "Stock result");
		} catch (Exception e) {
			logger.debug("Error in sendEmailUser while call audit service " + e.getMessage());
		}

	}

}
