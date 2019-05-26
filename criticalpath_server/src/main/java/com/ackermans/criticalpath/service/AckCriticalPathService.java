package com.ackermans.criticalpath.service;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.mail.MessagingException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ackermans.criticalpath.aop.service.AuditService;
import com.ackermans.criticalpath.common.dto.CPException;
import com.ackermans.criticalpath.email.EmailSenderService;
import com.ackermans.criticalpath.email.MailContentBuilder;
import com.ackermans.criticalpath.entity.AckCriticalPath;
import com.ackermans.criticalpath.entity.AckStockTakeCycle;
import com.ackermans.criticalpath.entity.TaskStatus;
import com.ackermans.criticalpath.entity.User;
import com.ackermans.criticalpath.enums.ErrorStatus;
import com.ackermans.criticalpath.repository.AckCriticalPathRepository;
import com.ackermans.criticalpath.repository.AckStockTakeCycleRepository;
import com.ackermans.criticalpath.repository.StoreCloseDateRepository;
import com.ackermans.criticalpath.repository.StoreRepository;
import com.ackermans.criticalpath.repository.TaskStatusRepository;
import com.ackermans.criticalpath.repository.UserLoginRepository;
import com.ackermans.criticalpath.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;

@Service
@Transactional
public class AckCriticalPathService {
	
	@Autowired
	private AckCriticalPathRepository ackCriticalPathRepository;
	
	@Autowired
	private AckStockTakeCycleRepository ackStockTakeCycleRepository;
	
	@Autowired
	private TaskStatusRepository taskStatusRepository;
	
	@Autowired
	private UserLoginRepository userLoginRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private StoreCloseDateRepository storeCloseDateRepository;
	
	@Autowired
	private AckStockTakeCycleService ackStockTakeCycleService;
	
	@Autowired
	private StoreRepository storeRepository;
	
	@Autowired
	private MailContentBuilder mailcontentbuilder;
	
	@Autowired
	private EmailSenderService emailsenderservice;
	
	@Autowired
	private AuditService auditService;
	
	private final Logger logger = LogManager.getFormatterLogger(AckCriticalPathService.class);
	
	private static final String MAIL_TASK_STATUS_UPDATE = "Critical Path - Update Task Status";

	/**
	 * Get criticalPath by stockTakeCycleId
	 * 
	 * @param stockTakeCycleId
	 * @return
	 */
	public List<AckCriticalPath> getAckCriticalPathData(Long stockTakeCycleId) {
		
		return ackCriticalPathRepository.findByCycleIdWithStatusisNotDisable(stockTakeCycleId);
	}

	public Object updateAckCriticalPathStatus(JsonNode request) throws ParseException, CPException {
		Long criticalPathid = request.get("criticalPathid").asLong();
		String completedBy = request.get("completedby").asText();
		String completeDate = request.get("taskCompleteDate").asText();
		Long statusid = request.get("statusid").asLong();
		Long storeId = request.get("storeId").asLong();
		Long cycleId = request.get("cycleId").asLong();
		String criticalPathName = request.get("criticalPathName").asText();
		String storeName = request.get("storeName").asText();
		
		AckCriticalPath criticalPath = ackCriticalPathRepository.getOne(criticalPathid);
		TaskStatus taskStatus = new TaskStatus();
		taskStatus.setId(statusid);
		criticalPath.setStatus(taskStatus);
		criticalPath.setCompletedBy(completedBy);
		
		Date date = new SimpleDateFormat("dd/MM/yyyy").parse(completeDate);
		criticalPath.setCompleteDate(date);
		updateAckCycleIsComplete(cycleId,criticalPathid);
		
		try {
			auditService.generateAuditMessageForTaskComplete(criticalPath.getTitle());
		} catch (Exception e) {
			logger.debug("Error in updateAckCriticalPathStatus while call audit service: "+e.getMessage());
		}
		
		try {
			this.sendEmailUser(criticalPathName,completedBy,completeDate,cycleId,storeId,storeName);
		} catch (Exception e) {
			logger.debug("Error in updateAckCriticalPathStatus while send email: "+e.getMessage());
		}
		
		
		return ackCriticalPathRepository.save(criticalPath);
	}
	
	private void updateAckCycleIsComplete(Long cycleId, Long criticalPathid) {
		long countTask = ackCriticalPathRepository.updateAckCycleIsComplete(cycleId, criticalPathid);
		if(countTask == 0) {
			AckStockTakeCycle ackStockTakeCycle = ackStockTakeCycleRepository.getOne(cycleId);
			ackStockTakeCycle.setIsCompleted(true);
			ackStockTakeCycleRepository.save(ackStockTakeCycle);
		}
	}

	@Async
	public void sendEmailUser(String criticalPathName, String completedBy, String completeDate, Long cycleId,
			Long storeId, String storeName) throws CPException {
		
		AckStockTakeCycle ackstockTakeCycle = ackStockTakeCycleRepository.getOne(cycleId);
	
		String cycleName = ackstockTakeCycle.getName();
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
					String smUser = userRepository.findSmUserNameByStoreId(storeId);
					if(user != null)
						userName = user.getName();
//					message =  "criticalPath "+criticalPathName+" updated by "+completedBy+"for storeName is "+ storeName +"completeDate is "+ completeDate;
					message = criticalPathName + " has been completed by " + smUser;
					message1 =	" Task is being done by "+ completedBy+" on "+completeDate+".";
					if(userName != null)
						htmlContent = mailcontentbuilder.buildSendCriticalPathUpdate(userName, message ,message1);
					
					emailsenderservice.send (userEmail.get(i).toString(), MAIL_TASK_STATUS_UPDATE, htmlContent);

				} catch (IOException | MessagingException e) {
					throw new CPException(ErrorStatus.EMAIL_SEND_FAILED, userEmail.get(i).toString());
				}
			} else {
				throw new CPException(ErrorStatus.EMAIL_NOT_FOUND, userEmail.get(i).toString());
			}
		}

		try {
			auditService.generateAuditMessageForSendEmail(userEmail, "task completed");
		} catch (Exception e) {
			logger.debug("Error in sendEmailUser while call audit service " + e.getMessage());
		}

	}

	public void getTodayActiveTask(Long stockTakeCycleId) throws ParseException {

		List<String> daysOfList = new ArrayList<String>();
		daysOfList.add("");
		daysOfList.add("Sunday");
		daysOfList.add("Monday");
		daysOfList.add("Tuesday");
		daysOfList.add("Wednesday");
		daysOfList.add("Thursday");
		daysOfList.add("Friday");
		daysOfList.add("Saturday");
		List<Integer> daysIndexList = new ArrayList<Integer>();

		String stockList = ackStockTakeCycleRepository.findByManualId(stockTakeCycleId);
		String[] stockArray = stockList.split(",");
		String ackStockTakeCycle1 = stockArray[0];
		
		AckStockTakeCycle ackStockTakeCycle = ackStockTakeCycleRepository.getOne(stockTakeCycleId);
		List<AckCriticalPath> criticalPathList = ackCriticalPathRepository.findByCycle_Id(ackStockTakeCycle.getId());
		String donotCountDays = ackStockTakeCycle.getDoNotCount();
		List<String>  donotCountDaystemp = new ArrayList<String>();
		if(donotCountDays != null) {
			donotCountDaystemp = Arrays.asList(donotCountDays.split(", "));
		}
	
		for (int i = 0; i < daysOfList.size(); i++) {
			for (int j = 0; j < donotCountDaystemp.size(); j++) {
				if (daysOfList.get(i).equals(donotCountDaystemp.get(j))) {
					daysIndexList.add(i);
				}
			}
			
		}

		for (AckCriticalPath criticalPath : criticalPathList) {
			List<Object> donotCountList = new ArrayList<Object>();
			List<Object> allDateList = new ArrayList<Object>();

			Calendar startdt = Calendar.getInstance();

			Date cycleStockTakeDate1 = new SimpleDateFormat("yyyy-MM-dd").parse(ackStockTakeCycle1);
			startdt.setTime(cycleStockTakeDate1);
			int taskStartDay = criticalPath.getStartDay() - 1;
			startdt.add(Calendar.DAY_OF_MONTH, -taskStartDay);
			Calendar takeDt = Calendar.getInstance();
			takeDt.setTime(cycleStockTakeDate1);

			int doNotCountDay = 0;
			while (takeDt.after(startdt)) {
				for(int i = 0; i< daysIndexList.size();i++) {
					if (startdt.get(Calendar.DAY_OF_WEEK) == daysIndexList.get(i)) {
						doNotCountDay++;
					}
		       	 }
				startdt.add(Calendar.DATE, 1);
			}

			int notcount = criticalPath.getStartDay() + doNotCountDay - 1;
			startdt.add(Calendar.DAY_OF_MONTH, -notcount);

			List<Object> closeDateList = storeCloseDateRepository.closedDateByStoreId(ackStockTakeCycle.getStore().getId(), startdt, takeDt);
			int closedate = closeDateList.size();
			startdt.add(Calendar.DAY_OF_MONTH, -closedate);

			int afterCloseDateCalc = 0;
			SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");

			while (takeDt.after(startdt)) {
				for(int i = 0; i< daysIndexList.size();i++) {
					if (startdt.get(Calendar.DAY_OF_WEEK) == daysIndexList.get(i)) {
						String formatted = format1.format(startdt.getTime());
						donotCountList.add(formatted);
						afterCloseDateCalc++;
					}
		       	 }
				startdt.add(Calendar.DATE, 1);
			}
			for (Object CloseDate : closeDateList) {
				donotCountList.add(CloseDate);
			}

			int notcounttotal = criticalPath.getStartDay() + afterCloseDateCalc + closedate - 1;
			startdt.add(Calendar.DAY_OF_MONTH, -notcounttotal);

			takeDt.add(Calendar.DATE, 1);
			while (startdt.before(takeDt)) {
				String formatted = format1.format(startdt.getTime());
				allDateList.add(formatted);
				startdt.add(Calendar.DATE, 1);
			}
			takeDt.add(Calendar.DATE, -1);

			for (int i = 0; i < donotCountList.size(); i++) {
				if (allDateList.contains(donotCountList.get(i)))
					allDateList.remove(donotCountList.get(i));
			}

			List<Object> pathDateList = new ArrayList<Object>();
			try {
				for (int i = 0; i < criticalPath.getLength(); i++) {
					if (allDateList.get(i) != null)
						pathDateList.add(allDateList.get(i));
				}
			} catch (Exception e) {

			}
//			for (int i = 0; i < criticalPath.getLength(); i++) {
//				if (allDateList.get(i) != null)
//					pathDateList.add(allDateList.get(i));
//			}
			Date currentDate = new Date();
			String todayDate = format1.format(currentDate);

			String name = "In Progress";
			TaskStatus taskStatus = taskStatusRepository.findByNameIgnoreCase(name);

			String lastdate = pathDateList.get(pathDateList.size() - 1).toString();
			String name1 = "Incomplete";
			Date lastDate = new SimpleDateFormat("yyyy-MM-dd").parse(lastdate);
			Date todaydate = new SimpleDateFormat("yyyy-MM-dd").parse(todayDate);
			if (pathDateList.contains(todayDate)) {
				if (criticalPath.getStatus().getId() != 2 && criticalPath.getStatus().getId() != 6) {
					criticalPath.setStatus(taskStatus);
				}
			} else if (lastDate.before(todaydate)) {

				TaskStatus taskStatus1 = taskStatusRepository.findByNameIgnoreCase(name1);

				if (criticalPath.getStatus().getId() == 3) {
					criticalPath.setStatus(taskStatus1);
				}
			}
		}
	}

}
