package com.ackermans.criticalpath.aop.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ackermans.criticalpath.aop.repo.AuditRepository;
import com.ackermans.criticalpath.entity.AuditLog;
import com.ackermans.criticalpath.entity.User;
import com.ackermans.criticalpath.repository.UserRepository;

@Service
@Transactional
public class AuditService {

	@Autowired
	private AuditRepository auditRepository;

	@Autowired
	private UserRepository userRepository;

	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	String dateTime = dateFormat.format(new Date()).toString();
	String uName = null;
	String moduleName = null;
	String moduleItemName = null;
	
	private final Logger logger = LogManager.getFormatterLogger(AuditService.class);
	
	public void saveAuditMessage(String auditMessage) {

		getUserNameForLog();
		
		AuditLog auditLog = new AuditLog();
		if(auditMessage != null && uName != null)
		{
			try {
				auditLog.setMessage(auditMessage);
				auditLog.setActionBy(uName);
				logger.debug(auditLog);
				auditRepository.save(auditLog);
			} catch (Exception e) {
				logger.debug("Error while calling saveAuditMessage: "+e.getMessage());
			}
		}
	}

	public String generateAuditMessage(String requestUrl) {
		
		String logMessage = null;
		try {
			logger.debug("generateAuditMessageForDeleteRestore_1");
			String methodName  =null;
			String action =null;
			
			//Getting baseEntity info
			if(requestUrl != null)
			{
				logger.debug("generateAuditMessageForDeleteRestore_2");
				String[] entityNameArr = 	requestUrl.split("/");
				moduleName = entityNameArr[2];
				methodName = entityNameArr[3];
				if(methodName.contains("delete")) {
					action = "deleted";
				}else if(methodName.contains("restore")) {
					action = "restored";
				}else if(methodName.contains("save") || methodName.contains("Save")) {
					action = "updated";
				}
			}
			
			getUserNameForLog();
			
			logger.debug("generateAuditMessageForDeleteRestore_3: "+uName);
			
			//As per customer's request
			if(moduleName.equalsIgnoreCase("store"))
				moduleName="branch";
			else if(moduleName.equalsIgnoreCase("brand"))
				moduleName="company";
			else if(moduleName.contains("closed"))
				moduleName="branch closed date";
			
			if(uName != null)
				logMessage = dateTime +": " + uName + " has "+ action + " " + moduleName ;
			
			logger.debug("generateAuditMessageForDeleteRestore_4: "+logMessage);
			logger.debug(logMessage);
			
		} catch (Exception e) {
			logger.debug("Error while calling generateAuditMessage: "+e.getMessage());
		}
		
		return logMessage;
	}

	public String apprDecStatusUpdateAdvice(String status , String cycleName) {
		String logMessage = null;
		try {
			String action =null;
			if(status.equalsIgnoreCase("A")) {
				action ="Approved";
			} else {
				action ="Declined";
			}
			getUserNameForLog();
			if(uName != null)
				logMessage = dateTime +": " + uName + " has "+ "been " +action+" stock take result cycle name "+ cycleName ;
			
			logger.debug(logMessage);
			saveAuditMessage(logMessage);
		} catch (Exception e) {
			logger.debug("Error while calling apprDecStatusUpdateAdvice: "+e.getMessage());
		}
		return logMessage;
	}

	private void getUserNameForLog() {
		String userEmailFromContext = SecurityContextHolder.getContext().getAuthentication().getName();
		try {
			if(userEmailFromContext != null)
				if(userEmailFromContext.contains("admin")) {
					uName ="admin";
				}else {
					User userObj = userRepository.findOneByUserLogin_Email(userEmailFromContext);
					if(userObj != null) {
					uName = userObj.getName();
					}
				}
		}catch (Exception e) {
			logger.debug("Error While calling getUserNameForLog: "+e.getMessage());
		}
	}

	public String generateAuditMessageForTaskComplete(String TaskName) {
		String logMessage = null;
		try {
			getUserNameForLog();
			
			if(uName != null)
				logMessage = dateTime +": " + uName + " has been completed Task of "+TaskName ;
			
			logger.debug(logMessage);
			saveAuditMessage(logMessage);
		} catch (Exception e) {
			logger.debug("Error while calling generateAuditMessageForTaskComplete: "+e.getMessage());
		}
		return logMessage;
	}

	public String generateAuditMessageForAddCycle(String cycleName) {
		String logMessage = null;
		try {
			getUserNameForLog();
			if(uName != null)
				logMessage = dateTime +": " + uName + " added new cycle "+cycleName ;
			
			logger.debug(logMessage);
			saveAuditMessage(logMessage);
		} catch (Exception e) {
			logger.debug("Error while calling generateAuditMessageForAddCycle: "+e.getMessage());
		}
		return logMessage;
	}

	public String generateAuditMessageForSendEmail(List userEmail , String content) {
		String logMessage = null;
		try {
			getUserNameForLog();
			
			if(uName != null & userEmail.size()>0)
				logMessage = dateTime +": Email sent for "+ content + " from: " +uName +", to: "+userEmail;
			else if(uName != null)
				logMessage = dateTime +": Email sent for "+ content + " from: " +uName ;
				
			logger.debug(logMessage);
			saveAuditMessage(logMessage);
		} catch (Exception e) {
			logger.debug("Error while calling generateAuditMessageForSendEmail: "+e.getMessage());
		}
		return logMessage;
	}

	public String generateAuditMessageForLogin(String userEmail) {
		String logMessage = null;
		//Getting user info
		try {
			if(userEmail != null)
				if(userEmail.contains("admin")) {
					uName ="admin";
				}else {
					User userObj = userRepository.findOneByUserLogin_Email(userEmail);
					if(userObj != null) {
					uName = userObj.getName();
					}
				}
		}catch (Exception e) {
			logger.debug("Error While calling generateAuditMessageForLogin: "+e.getMessage());
		}		
		
		getUserNameForLog();
		if(uName != null)
			logMessage = dateTime +": " +uName +" tried to login. ";
		
		logger.debug(logMessage);
		saveAuditMessage(logMessage);
		return logMessage;
		
	}

	public String generateAuditMessageForUserLogOut() {
		String logMessage = null;
		try {
			getUserNameForLog();
			if(uName != null)
				logMessage = dateTime +": " +uName +" tried to logout. ";
			
			logger.debug(logMessage);
			saveAuditMessage(logMessage);
		} catch (Exception e) {
			logger.debug("Error while calling generateAuditMessageForUserLogOut: "+e.getMessage());
		}
		return logMessage;
	}

	public Object findByDate(String searchDate) {
		
		String formatedDate = null;
		try {
			Date stockTakeDt = new SimpleDateFormat("dd-mm-yyyy").parse(searchDate);
			SimpleDateFormat format1 = new SimpleDateFormat("yyyy-mm-dd");
			formatedDate = format1.format(stockTakeDt);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		
		List<AuditLog> aoplogs = new ArrayList<AuditLog>();
		try {
			if(formatedDate != null && !formatedDate.equalsIgnoreCase("undefined")) {
				aoplogs = auditRepository.findByCreatedDateTime(formatedDate);
			}
		} catch (Exception e) {
			logger.debug("Error while calling findByDate: "+e.getMessage());
		}
		
		return aoplogs;
	}
	
}
