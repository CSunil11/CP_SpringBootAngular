package com.ackermans.criticalpath.api.controller;

import java.io.IOException;
import java.net.PasswordAuthentication;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import javax.mail.MessagingException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ackermans.criticalpath.aop.service.AuditService;
import com.ackermans.criticalpath.common.dto.CPException;
import com.ackermans.criticalpath.email.EmailSenderService;
import com.ackermans.criticalpath.email.MailContentBuilder;
import com.ackermans.criticalpath.entity.AckCriticalPath;
import com.ackermans.criticalpath.entity.AckStockTakeCycle;
import com.ackermans.criticalpath.entity.ManageNotification;
import com.ackermans.criticalpath.entity.User;
import com.ackermans.criticalpath.entity.UserLogin;
import com.ackermans.criticalpath.enums.ErrorStatus;
import com.ackermans.criticalpath.repository.AckCriticalPathRepository;
import com.ackermans.criticalpath.repository.AckStockTakeCycleRepository;
import com.ackermans.criticalpath.repository.ManageNotificationRepository;
import com.ackermans.criticalpath.repository.UserLoginRepository;
import com.ackermans.criticalpath.repository.UserRepository;
import com.ackermans.criticalpath.service.AckStockTakeCycleService;

@Component
public class NotificationEmailScheduler {
	
	@Autowired
	AckStockTakeCycleRepository ackStockTakeCycleRepository;
	
	@Autowired
	AckStockTakeCycleService ackStockTakeCycleService;
	
	@Autowired
	UserRepository userRepository ;
	
	@Autowired
	UserLoginRepository userLoginRepository;
	
	@Autowired
	AckCriticalPathRepository ackCriticalPathRepository;
	
	@Autowired
	private MailContentBuilder mailcontentbuilder;
	
	@Autowired
	private EmailSenderService emailsenderservice;
	
	@Autowired
	private ManageNotificationRepository manageNotificationRepository;
	
	@Autowired
	private AuditService auditService;

	String pattern = "dd-MM-yyyy";
	
	private final Logger logger = LogManager.getFormatterLogger(NotificationEmailScheduler.class);
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

	//Executes at every 60 Seconds
//	@Scheduled(cron = "0/60 * * * * ?")
	
	//Executes at every 10 Minutes
//	@Scheduled(cron = "* 10 * * * ?")
	
	//Executes at 12 AM every day
	@Scheduled(cron="0 0 0 * * ?")
	void manageNotifications() {
		logger.debug("====== Scheduler started at 12 AM today =====");
		
		List<String> triggerEvents = new ArrayList<String>();
		
		List<ManageNotification> listOfMnf = manageNotificationRepository.findAllByIsActiveTrue();
		
		if(listOfMnf.size() > 0)
		{
			for (ManageNotification manageNotification : listOfMnf) {

				// fetch informations from the notifications database table
				String notName = manageNotification.getName();
				String notDesc = manageNotification.getDescription();
				String notSubject = manageNotification.getSubject();
				String notEmailBody = manageNotification.getBody();
				String userGroup = manageNotification.getUserGroups();

				triggerEvents.add(manageNotification.getTriggerEvent());

				int delayTime = manageNotification.getDelayTime().intValue();
				int reminderTime = manageNotification.getReminderTime().intValue();
				int reminderCount = manageNotification.getReminderCount().intValue();
				String notType = manageNotification.getNotificationType();

				logger.debug("manageNotification data: " + manageNotification);

				// Common parameters
				String storeName = "";
				String storeNo = "";
				long taskId = 0;
				String taskName = "";
				String taskDeadLine = "";
				String stockTakeDate = "";
				String smNameSurname = "";
				String dsmNameSurname = "";
				String ramNameSurname = "";
				List<String> listOfEmailIds = new ArrayList<String>();
				String[] userRole = userGroup.split(",");
				List userRoleList = Arrays.asList(userRole);

				if (triggerEvents.size() > 0) {
					List<AckStockTakeCycle> listOfCycles = ackStockTakeCycleRepository
							.findByStockTakeDateValidateWithCurDate();
					logger.debug("listOfCycles having size: " + listOfCycles.size());

					if (listOfCycles.size() > 0) {
						if (triggerEvents.contains("Cycle")) {
							logger.debug("====== Scheduler for Cycle =====");
							// Generate dates list for each cycle
							for (int i = 0; i < listOfCycles.size(); i++) {
								List<String> emailDates = generateEmailDatesForCycle(listOfCycles.get(i), delayTime,
										reminderCount, reminderTime);
								logger.debug("CycleDateList: " + emailDates);

								String todayDate = simpleDateFormat.format(new Date());
								if (emailDates.contains(todayDate)) {
									if (notType.equalsIgnoreCase("Escalation")) {
										notSubject = "Escalation: " + manageNotification.getSubject();
									}

									String notEmailBodyTemp = notEmailBody;

									replaceEmailBodyContents(notEmailBodyTemp, listOfEmailIds, userRoleList,
											listOfCycles, i, null, null, notSubject, "C");

								} else {
									logger.debug("CycleDateList not contain todayDate.");
								}

							}
						} else if (triggerEvents.contains("Task")) {
							logger.debug("====== Scheduler for Task =====");
							for (int i = 0; i < listOfCycles.size(); i++) {
								try {

									List<ArrayList> listOfInCompleteTasks = getTaskEndDate(listOfCycles.get(i));
									logger.debug("List Of InComplete Task : " + listOfInCompleteTasks);
									if (listOfInCompleteTasks.size() > 0) {
										for (int j = 0; j < listOfInCompleteTasks.size(); j++) {
											String dt = listOfInCompleteTasks.get(j).get(1).toString();
											Date dateFormat = simpleDateFormat.parse(dt);

											List<String> emailDatesOfTask = generateEmailDatesForTasks(
													Long.parseLong(listOfInCompleteTasks.get(j).get(0).toString()),
													dateFormat, delayTime, reminderCount, reminderTime);
											logger.debug("TaskDateList: " + emailDatesOfTask);
											String todayDate = simpleDateFormat.format(new Date());
											if (emailDatesOfTask.contains(todayDate)) {
												if (notType.equalsIgnoreCase("Escalation")) {
													notSubject = "Escalation: " + manageNotification.getSubject();
												}

												String notEmailBodyTemp = notEmailBody;
												System.out.println("String Task notEmailBodyTemp Check1");

												replaceEmailBodyContents(notEmailBodyTemp, listOfEmailIds, userRoleList,
														listOfCycles, i, dt,
														Long.parseLong(listOfInCompleteTasks.get(j).get(0).toString()),
														notSubject, "T");

											} else {
												logger.debug("TaskDateList not contain todayDate.");
											}
										}
									} else {
										logger.debug("List Of InComplete Task is empty.");
									}

								} catch (ParseException e) {
									logger.debug("error while getTaskEndDate method execution: " + e.getMessage());
								}
							}
						}
					}

				} else
					logger.debug("Trigger events are empty");
			}
		} else
			logger.debug("Manage notifications are empty");

		logger.debug("====== Scheduler Completed =====");
	}

	private String replaceEmailBodyContents(String notEmailBodyTemp, List<String> listOfEmailIds, List userRoleList,
			List<AckStockTakeCycle> listOfCycles, int i, String dt, Long taskId, String notSubject, String event) {

		String storeName = listOfCycles.get(i).getStore().getName();
		if (!storeName.isEmpty()) {
			notEmailBodyTemp = notEmailBodyTemp.replaceAll("\\{\\{store-name\\}\\}", storeName);
		}

		String storeNo = listOfCycles.get(i).getStore().getCode();
		if (!storeNo.isEmpty()) {
			notEmailBodyTemp = notEmailBodyTemp.replaceAll("\\{\\{store-no\\}\\}", storeNo);
		}

		String taskDeadLine = "";
		String stockTakeDate;
		String smNameSurname;
		String dsmNameSurname;
		String ramNameSurname;
		if (taskId != null) {
			Optional<AckCriticalPath> ackCp = ackCriticalPathRepository.findById(taskId);
			String taskName = ackCp.get().getTitle();
			if (!taskName.isEmpty()) {
				notEmailBodyTemp = notEmailBodyTemp.replaceAll("\\{\\{task-name\\}\\}", taskName);
			}
		}

		if (dt != null) {
			taskDeadLine = dt;
		}

		if (!taskDeadLine.isEmpty()) {
			notEmailBodyTemp = notEmailBodyTemp.replaceAll("\\{\\{task-deadline\\}\\}", taskDeadLine);
		}

		Date stockTakeDateFormate = listOfCycles.get(i).getStokeTakeDate();
		stockTakeDate = simpleDateFormat.format(stockTakeDateFormate);
		if (!stockTakeDate.isEmpty()) {
			notEmailBodyTemp = notEmailBodyTemp.replaceAll("\\{\\{stock-take-date\\}\\}", stockTakeDate);
		}
		String remainingDays = "";
		String deadLineDate = "";

		if (event.equals("T")) {
			deadLineDate = taskDeadLine;
		} else if (event.equals("C")) {
			deadLineDate = stockTakeDate;
		}

		try {
			String todayDt = simpleDateFormat.format(new Date());
			Date todayDate = simpleDateFormat.parse(todayDt);
			Date taskEnd = simpleDateFormat.parse(deadLineDate);
			long duration = taskEnd.getTime() - todayDate.getTime();
			long diffInDays = TimeUnit.MILLISECONDS.toDays(duration);
			remainingDays = Long.toString(diffInDays);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (!remainingDays.isEmpty()) {
			notEmailBodyTemp = notEmailBodyTemp.replaceAll("\\{\\{remaining-days\\}\\}", remainingDays);
		}

		listOfEmailIds.clear();
		String dsmEmailId = "";
		try {
			dsmEmailId = userLoginRepository.getDsmEmail(listOfCycles.get(i).getStore().getId());
			User dsmUser = userRepository.findOneByUserLogin_Email(dsmEmailId);
			dsmNameSurname = dsmUser.getName();
			notEmailBodyTemp = notEmailBodyTemp.replaceAll("\\{\\{dsm-name-surname\\}\\}", dsmNameSurname);
		} catch (Exception e) {
		}

		Optional<UserLogin> ul = null;
		try {
			User smUser = userRepository.findByStore_Id(listOfCycles.get(i).getStore().getId());
			ul = userLoginRepository.findById(smUser.getUserLogin().getId());
			smNameSurname = smUser.getName();
			notEmailBodyTemp = notEmailBodyTemp.replaceAll("\\{\\{sm-name-surname\\}\\}", smNameSurname);
		} catch (Exception e) {
		}

		String ramEmailId = "";
		try {
			ramEmailId = userLoginRepository.getRamEmail(listOfCycles.get(i).getStore().getId());
			User ramUser = userRepository.findOneByUserLogin_Email(ramEmailId);
			ramNameSurname = ramUser.getName();
			notEmailBodyTemp = notEmailBodyTemp.replaceAll("\\{\\{ram-name-surname\\}\\}", ramNameSurname);
		} catch (Exception e) {
		}

		if (userRoleList.contains("DSM"))
			listOfEmailIds.add(dsmEmailId);

		if (userRoleList.contains("SM"))
			listOfEmailIds.add(ul.get().getEmail());

		if (userRoleList.contains("RAM"))
			listOfEmailIds.add(ramEmailId);

		try {
			sendEmailUser(notEmailBodyTemp, listOfEmailIds, notSubject);
		} catch (CPException e) {
			e.printStackTrace();
		}

		logger.debug("Replace variable in emailTemplateBody." + notEmailBodyTemp);
		return notEmailBodyTemp;
	}

	@Async
	public void sendEmailUser(String notEmailBody, List<String> listOfEmailIds, String notSubject) throws CPException {
		logger.debug("sendEmailUser  listOfEmailIds : " + listOfEmailIds);

		for (int i = 0; i < listOfEmailIds.size(); i++) {

			if (listOfEmailIds.get(i).toString() != null) {
				try {
					String htmlContent = notEmailBody;
					logger.debug("Email send to : " + listOfEmailIds.get(i).toString());
					emailsenderservice.send(listOfEmailIds.get(i).toString(), notSubject, htmlContent);

				} catch (IOException | MessagingException e) {
					throw new CPException(ErrorStatus.EMAIL_SEND_FAILED, listOfEmailIds.get(i).toString());
				}
			} else {
				throw new CPException(ErrorStatus.EMAIL_NOT_FOUND, listOfEmailIds.get(i).toString());
			}
		}

		try {
			auditService.generateAuditMessageForSendEmail(listOfEmailIds, "Notification scheduler");
		} catch (Exception e) {
			logger.debug("Error in sendEmailUser while call audit service " + e.getMessage());
		}
	}

	private List<String> generateEmailDatesForCycle(AckStockTakeCycle ackStockTakeCycle, int delayTime,
			int reminderCount, int reminderTime) {

		List<String> listOfEmailDatesForCycle = new ArrayList<String>();

		if (delayTime != 0) {
			String date = calculateDateWithGivenFormat(ackStockTakeCycle.getStokeTakeDate(), delayTime);
			listOfEmailDatesForCycle.add(date);
		}

		if (reminderCount > 0 && reminderTime != 0) {
			String dateToCheck = "";
			for (int i = 0; i < reminderCount; i++) {
				if (i == 0) {
					dateToCheck = calculateDateWithGivenFormat(ackStockTakeCycle.getStokeTakeDate(), reminderTime);

					if (!listOfEmailDatesForCycle.contains(dateToCheck))
						listOfEmailDatesForCycle.add(dateToCheck);
				} else {
					Date startDate = null;
					try {
						startDate = simpleDateFormat.parse(dateToCheck);

						dateToCheck = calculateDateWithGivenFormat(startDate, reminderTime);
						if (!listOfEmailDatesForCycle.contains(dateToCheck))
							listOfEmailDatesForCycle.add(dateToCheck);
					} catch (ParseException e) {
						e.printStackTrace();
						logger.debug("error while calculate date with given format for cycle_id: "
								+ ackStockTakeCycle.getId());
					}
				}

			}
		}

		logger.debug("listOfEmailDatesForCycle: " + listOfEmailDatesForCycle);
		return listOfEmailDatesForCycle;
	}

	private List<String> generateEmailDatesForTasks(Long taskId, Date taskEndDate, int delayTime, int reminderCount,
			int reminderTime) {

		List<String> listOfEmailDatesForTask = new ArrayList<String>();

		if (delayTime != 0) {
			String date = calculateDateWithGivenFormat(taskEndDate, delayTime);
			listOfEmailDatesForTask.add(date);
		}

		if (reminderCount > 0 && reminderTime != 0) {
			String dateToCheck = "";
			for (int i = 0; i < reminderCount; i++) {
				if (i == 0) {
					dateToCheck = calculateDateWithGivenFormat(taskEndDate, reminderTime);

					if (!listOfEmailDatesForTask.contains(dateToCheck))
						listOfEmailDatesForTask.add(dateToCheck);
				} else {
					Date startDate = null;
					try {
						startDate = simpleDateFormat.parse(dateToCheck);

						dateToCheck = calculateDateWithGivenFormat(startDate, reminderTime);
						if (!listOfEmailDatesForTask.contains(dateToCheck))
							listOfEmailDatesForTask.add(dateToCheck);
					} catch (ParseException e) {
						e.printStackTrace();
						logger.debug("error while calculate date with given format for task_id: " + taskId);
					}
				}

			}
		}

		logger.debug("listOfEmailDatesForTask: " + listOfEmailDatesForTask);
		return listOfEmailDatesForTask;
	}

	private String calculateDateWithGivenFormat(Date date, int delayTime) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, delayTime);

		String formattedDate = simpleDateFormat.format(cal.getTime());
		return formattedDate;
	};

	public List<ArrayList> getTaskEndDate(AckStockTakeCycle ackStockTakeCycle) throws ParseException {

		Date stockTakeDate = ackStockTakeCycle.getStokeTakeDate();
		DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
		String strStockTakeDate = dateFormat.format(stockTakeDate);
		List<String> allDt = new ArrayList<String>();
		List<ArrayList> listOfTasks = new ArrayList<ArrayList>();

		allDt = ackStockTakeCycleService.getAllDateList(ackStockTakeCycle.getId(), strStockTakeDate);
		List<AckCriticalPath> cp = ackCriticalPathRepository
				.findByCycleIdWithStatusisNotDoneAndComplete(ackStockTakeCycle.getId());
		if (cp.size() > 0) {
			for (AckCriticalPath ackCriticalPath : cp) {
				List<String> cpTaskDt = new ArrayList<String>();
				ArrayList<Object> cpTaskEndDt = new ArrayList<Object>();

				for (int i = allDt.size() - ackCriticalPath.getStartDay(); i < allDt.size()
						- ackCriticalPath.getStartDay() + ackCriticalPath.getLength(); i++) {
					cpTaskDt.add(allDt.get(i));
				}

				Date TaskEndDateTemp = new SimpleDateFormat("yyyy-MM-dd").parse(cpTaskDt.get(cpTaskDt.size() - 1));
				DateFormat dateFormatt = new SimpleDateFormat("dd-MM-yyyy");
				String TaskDate = dateFormatt.format(TaskEndDateTemp);
				cpTaskEndDt.add(ackCriticalPath.getId());
				cpTaskEndDt.add(TaskDate);

				listOfTasks.add(cpTaskEndDt);
			}
		}

		logger.debug("List of Task with id and task End Date: " + listOfTasks);
		return listOfTasks;
	}
}
