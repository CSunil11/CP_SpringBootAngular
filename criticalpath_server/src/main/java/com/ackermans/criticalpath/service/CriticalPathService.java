package com.ackermans.criticalpath.service;


import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.mail.MessagingException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;

import com.ackermans.criticalpath.common.dto.CPException;
import com.ackermans.criticalpath.email.EmailSenderService;
import com.ackermans.criticalpath.email.MailContentBuilder;
import com.ackermans.criticalpath.entity.CriticalPath;
import com.ackermans.criticalpath.entity.StockTakeCycle;
import com.ackermans.criticalpath.entity.Store;
import com.ackermans.criticalpath.entity.TaskStatus;
import com.ackermans.criticalpath.entity.User;
import com.ackermans.criticalpath.entity.UserLogin;
import com.ackermans.criticalpath.enums.ErrorStatus;
import com.ackermans.criticalpath.repository.CriticalPathRepository;
import com.ackermans.criticalpath.repository.StockTakeCycleRepository;
import com.ackermans.criticalpath.repository.StoreRepository;
import com.ackermans.criticalpath.repository.UserLoginRepository;
import com.ackermans.criticalpath.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;

@Service
public class CriticalPathService {

	private final Logger logger = LogManager.getFormatterLogger();
	
	@Autowired
	CriticalPathRepository criticalPathRepository;
	
	@Autowired
	private StoreRepository storeRepository;
	
	@Autowired
	private UserLoginService userLoginService;
	
	@Autowired
	private EmailSenderService emailsenderservice;
	
	@Autowired
	private MailContentBuilder mailcontentbuilder;
	
	@Autowired
	private StockTakeCycleRepository stockTakeCycleRepository;
	
	@Autowired
	private UserLoginRepository userLoginRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	private static final String MAIL_TASK_STATUS_UPDATE = "Critical Path - Update Task Status";
	
	/**
	 * Verify and Save Critical PAth
	 * @param criticalPath
	 * @return
	 * @throws CPException
	 */
	public CriticalPath verifyAndSave(CriticalPath criticalPath, OAuth2Authentication oAuth2Authentication) throws CPException {
		
		boolean isExist = false;

		// If there is id that means record needs to be updated
		// hence we should ignore that record while checking for existence
		if (criticalPath.getId() != null && criticalPath.getId() > 0) {
			isExist = this.isExist(criticalPath.getTitle(), criticalPath.getId());
		} else {
			isExist = this.isExist(criticalPath.getTitle());
		}

		// If record is unique then save it else throw exception
		if (!isExist) {
			UserLogin userLogin = userLoginService.findByEmail(oAuth2Authentication.getName());
			criticalPath.setCreatedBy(userLogin);
			criticalPathRepository.save(criticalPath);
		}
		else
			throw new CPException(ErrorStatus.DUPLICATE_RECORD);

		return criticalPath;
	}
	
	/**
	 * Check if record already exists for given stock take cycle name
	 * 
	 * @param name
	 * @return
	 */
	public boolean isExist(String title) {
		return criticalPathRepository.countByTitleIgnoreCase(title) > 0;
	}
	
	/**
	 * Check if record already exists for given stock take cycle name
	 * 
	 * @param name
	 * @return
	 */
	public boolean isExist(String title, Long ignoreId) {
		return criticalPathRepository.countByTitleAndIgnoreId(title, ignoreId) > 0;
	}

	/**
	 * Get all Critical PAth
	 * @return
	 */
	public List<CriticalPath> getAll(){
		return criticalPathRepository.findAll();
	}
	
	/**
	 * Get all active Critical PAth List
	 * 
	 * @return
	 */
	public List<CriticalPath> getAllActive() {
		
		List<CriticalPath> criticalPaths = criticalPathRepository.findAllByIsActiveTrue();
		
		for (CriticalPath criticalPath : criticalPaths) {
			for (Store store : criticalPath.getStores()) {
				System.out.println(store);
			}
		}
		
		if(criticalPaths.size()>0)
			return criticalPaths;
		else
			return null;
	}
	
	/**
	 * Get all deleted Critical PAth List
	 * 
	 * @return
	 */
	public List<CriticalPath> getAllDeletedCriticalPath() {
		
		List<CriticalPath> criticalPaths =  criticalPathRepository.findAllByIsActiveFalseAndIsDeleteTrue();
		
		for (CriticalPath criticalPath : criticalPaths) {
			for (Store store : criticalPath.getStores()) {
			}
		}
		
		if(criticalPaths.size()>0)
			return criticalPaths;
		else
			return null;
	}
	
	/**
	 * Get Critical PAth By criticalPathId
	 * @param criticalPathId
	 * @return
	 */
	public Object getCriticalPathData(Long criticalPathId) {
		
		Optional<CriticalPath> criticalPath = criticalPathRepository.findById(criticalPathId);
		
//		Hibernate.initialize(criticalPath.get().getCycle().getStores());
		
		Optional<StockTakeCycle> stockCycle = stockTakeCycleRepository.findById(criticalPath.get().getCycle().getId());

		List<Store> storeOfCycle = storeRepository.findByStockTakeCycle_Id(stockCycle.get().getId());

		Map cycleData = new HashMap();
//		cycleData.put("cycle", stockCycle);
		cycleData.put("storeOfCycle", storeOfCycle);

		Hibernate.initialize(criticalPath.get().getStores());
		cycleData.put("criticalPath", criticalPath);
		
		Set<Store> stores = criticalPath.get().getStores();
		
		int n = stores.size();
	    List<Store> aList = new ArrayList<Store>(n);
	    aList.addAll(stores);
	    
		for (Store store : aList) {
			System.out.println(store);
		}
		
		if(aList.size() > 0)
			return cycleData;
		  else return null;
	}
	
	/**
	 * Deletes the record
	 * @param criticalPathId
	 * @return
	 * @throws CPException 
	 */
	public void deleteCriticalPath(Long criticalPathId) throws CPException {
		
		CriticalPath criticalPath = criticalPathRepository.getOne(criticalPathId);
		criticalPath.setIsActive(false);
		criticalPath.setIsDelete(true);
		criticalPathRepository.save(criticalPath);
	}

	/**
	 * Restores the record
	 * @param criticalPathId
	 */
	public void restoreCriticalPath(Long criticalPathId) {
		CriticalPath criticalPath = criticalPathRepository.getOne(criticalPathId);
		criticalPath.setIsActive(true);
		criticalPath.setIsDelete(false);
		criticalPathRepository.save(criticalPath);
		return ;
	}

	/**
	 * Get criticalPath by stockTakeCycleId
	 * 
	 * @param stockTakeCycleId
	 * @return
	 */
	public List<CriticalPath> getCriticalPathOfStockTakeCycle(Long stockTakeCycleId) {
		
		return criticalPathRepository.findByCycleIdWithStatusisNotDisable(stockTakeCycleId);
	}

	public CriticalPath updateCriticalPathStatus(JsonNode request) throws ParseException, CPException {
		Long criticalPathid = request.get("criticalPathid").asLong();
		String completedBy = request.get("completedby").asText();
		String completeDate = request.get("taskCompleteDate").asText();
		Long statusid = request.get("statusid").asLong();
		Long storeId = request.get("storeId").asLong();
		Long cycleId = request.get("cycleId").asLong();
		String criticalPathName = request.get("criticalPathName").asText();
		String storeName = request.get("storeName").asText();
		
		CriticalPath criticalPath = criticalPathRepository.getOne(criticalPathid);
		TaskStatus taskStatus = new TaskStatus();
		taskStatus.setId(statusid);
		criticalPath.setStatus(taskStatus);
		criticalPath.setCompletedBy(completedBy);
		
		Date date = new SimpleDateFormat("dd/MM/yyyy").parse(completeDate);
		criticalPath.setCompleteDate(date);
		
		this.sendEmailUser(criticalPathName,completedBy,completeDate,cycleId,storeId,storeName);
		
		return criticalPathRepository.save(criticalPath);
		
	}

	@Async
	public void sendEmailUser(String criticalPathName, String completedBy, String completeDate, Long cycleId,
			Long storeId, String storeName) throws CPException {
		
		StockTakeCycle stockTakeCycle = stockTakeCycleRepository.getOne(cycleId);
	
		String cycleName = stockTakeCycle.getName();
		System.out.println(cycleName);
		List userEmail = userLoginRepository.getDsmAndRamEmail(storeId);
		String userName = "";
		String message = "";
		String message1 = "";
		String htmlContent = "";

		for(int i =0; i<userEmail.size();i++) {
			if (userEmail.get(i).toString() != null) {
				try {
					User user = userRepository.findOneByUserLogin_Email(userEmail.get(i).toString());
					
					if(user != null)
						userName = user.getName();
					
					message =  "criticalPath "+criticalPathName+" updated by "+completedBy+"for storeName is "+ storeName +"completeDate is "+ completeDate;
					
					if(userName != null)
						htmlContent = mailcontentbuilder.buildSendCriticalPathUpdate(userName, message , message1);
					
					emailsenderservice.send (userEmail.get(i).toString(), MAIL_TASK_STATUS_UPDATE, htmlContent);
					
				} catch (IOException | MessagingException e) {
					throw new CPException(ErrorStatus.EMAIL_SEND_FAILED, userEmail.get(i).toString());
				}
			}
			else {
				throw new CPException(ErrorStatus.EMAIL_NOT_FOUND,  userEmail.get(i).toString());
			}
		}
				
	}
}
