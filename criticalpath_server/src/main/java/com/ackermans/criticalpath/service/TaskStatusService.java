package com.ackermans.criticalpath.service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ackermans.criticalpath.common.dto.CPException;
import com.ackermans.criticalpath.entity.Country;
import com.ackermans.criticalpath.entity.CriticalPath;
import com.ackermans.criticalpath.entity.Store;
import com.ackermans.criticalpath.entity.TaskStatus;
import com.ackermans.criticalpath.enums.ErrorStatus;
import com.ackermans.criticalpath.repository.CriticalPathRepository;
import com.ackermans.criticalpath.repository.TaskStatusRepository;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

@Service
public class TaskStatusService {

	private static final Logger logger = LogManager.getFormatterLogger(TaskStatusService.class);
	
	@Autowired
	private TaskStatusRepository taskStatusRepo;
	
	@Autowired
	private CriticalPathRepository criticalPathRepository;
	
	/**
	 * Get all task status 
	 * 
	 * @return
	 */
	public List<TaskStatus> getAll() {
		return taskStatusRepo.findAll();
	}
	
	/**
	 * Get All Active Task Status List
	 * 
	 * @return
	 */
	public List<TaskStatus> getAllActive() {
		return taskStatusRepo.findAllByIsActiveTrue();
	}
	
	/**
	 * Get All deleted Task Status List
	 * 
	 * @return
	 */
	public List<TaskStatus> getAllDeletedTaskStatus() {
		return taskStatusRepo.findAllByIsActiveFalseAndIsDeleteTrue();
	}
	
	/**
	 * Check if TaskStatus exists for given name
	 * 
	 * @param name
	 * @return
	 */
	public boolean isExists(String name) {
		return taskStatusRepo.countByNameIgnoreCase(name) > 0;
	}
	
	/**
	 * Check if TaskStatus exists for given name but ignore given id
	 * 
	 * @param name
	 * @return
	 */
	public boolean isExists(String name,Long ignoreId) {
		return taskStatusRepo.countByNameAndIgnoreId(name, ignoreId) > 0;
	}
	
	/**
	 * Check if a task exists with same name then throw exception otherwise save.
	 * 
	 * @param name
	 * @param description
	 * @return
	 * @throws CPException 
	 */
	public TaskStatus verifyAndSave(String name, String description) throws CPException {
		return this.verifyAndSave(new TaskStatus(name, description));
	}
	
	/**
	 * Check if a task exists with same name then throw exception otherwise save.
	 * 
	 * @param taskStatus
	 * @return
	 * @throws CPException 
	 */
	public TaskStatus verifyAndSave(TaskStatus taskStatus) throws CPException {
		
		boolean isExist = false;
		
		//If there is id that means record needs to be updated
		//hence we should ignore that record while checking for existence
		if(taskStatus.getId() != null && taskStatus.getId() > 0) {
			isExist = this.isExists(taskStatus.getName(), taskStatus.getId());
		} else {
			isExist = this.isExists(taskStatus.getName());
		}
		
		//If record is unique then save it else throw exception
		if(!isExist)
			this.save(taskStatus);
		else
			throw new CPException(ErrorStatus.DUPLICATE_RECORD);
		
		return taskStatus;
	}
	
	/**
	 * Activate taskstatus by id
	 * 
	 * @param id
	 * @return
	 */
	public TaskStatus activate(Long id) {
		TaskStatus taskStatus = taskStatusRepo.getOne(id);
		taskStatus.setIsActive(true);
		return taskStatusRepo.save(taskStatus);
	}
	
	/**
	 * Deactivate taskstatus by id
	 * 
	 * @param id
	 * @return
	 */
	public TaskStatus deactivate(Long id) {
		TaskStatus taskStatus = taskStatusRepo.getOne(id);
		taskStatus.setIsActive(false);
		return taskStatusRepo.save(taskStatus);
	}

	/**
	 * Create new task status
	 * 
	 * @param taskStatus
	 * @return
	 */
	private TaskStatus save(TaskStatus taskStatus) {
		return taskStatusRepo.save(taskStatus);
	}

	/**
	 * 
	 * @param Get Task Status data
	 * @return
	 */
	public Object getTaskStatusData(Long taskStatusId) {
		return taskStatusRepo.findById(taskStatusId);
	}
	
	/**
	 * Deletes the record
	 * @param taskStatusId
	 * @return
	 * @throws CPException 
	 */
	public void deleteTaskStatus(Long taskStatusId) throws CPException {
		
		List<CriticalPath> critcalPath = criticalPathRepository.findByStatusIdAndIsActiveTrue(taskStatusId);
		
		if(critcalPath.isEmpty()) {
			TaskStatus taskStatus = taskStatusRepo.getOne(taskStatusId);
			taskStatus.setIsActive(false);
			taskStatus.setIsDelete(true);
			taskStatusRepo.save(taskStatus);
		} else
			throw new CPException(ErrorStatus.TASK_STATUS_EXISTS, "'" + critcalPath.get(0).getTitle() +"'");
			
		return;
	}

	/**
	 * Restores the record
	 * @param taskStatusId
	 */
	public void restoreTaskStatus(Long taskStatusId) {
		TaskStatus taskStatus = taskStatusRepo.getOne(taskStatusId);
		taskStatus.setIsActive(true);
		taskStatus.setIsDelete(false);
		taskStatusRepo.save(taskStatus);
		return ;
	}
	
	/**
	 * Upload task data in bulk
	 * 
	 * @return
	 * @throws IOException
	 */
	public List<String> upload(MultipartFile file) throws SQLException, IOException {

		List<String> logs = new ArrayList<>();

		try {

			// Create an object of filereader
			Reader reader = new InputStreamReader(file.getInputStream());

			// create csvParser object with custom seperator semi-colon
			CSVParser parser = new CSVParserBuilder().withSeparator(',').build();

			// create csvReader object passing file reader as a parameter
			CSVReader csvReader = new CSVReaderBuilder(reader).withCSVParser(parser).build();

			// Read all data at once
			List<String[]> allData = csvReader.readAll();
			int count = 0;
			boolean isValidCsv = false;
			for (String[] row : allData) {
				count++;
				if (!isValidCsv) {
					if (row[0].equals("Name") && row[1].equals("Description") && allData.size() > 1) {
						isValidCsv = true;
//						count = 0;

					} else {
						logger.error("Invalid csv file");
						logs.add("Invalid csv file");
						return logs;
					}
				}

				if (count > 1) {
					TaskStatus taskStatus = new TaskStatus();

					if (checkIfNotEmpty(row, count, logs) && checkIfNameIsValid(row, count, logs)) {
						taskStatus.setName(row[0]);
						taskStatus.setDescription(row[1]);
						try {
							logger.info("Saving the entity in database");
							taskStatusRepo.save(taskStatus);
						} catch (Exception e) {
							logger.error("Error while saving entity in database");
							for (Throwable t = e.getCause(); t != null; t = t.getCause()) {
								if (t.getClass().toString().contains("MySQLIntegrityConstraintViolationException")) {
									SQLException sqlException = (SQLException) t;
									if (sqlException.getMessage().toString().contains("Duplicate")) {

										String sqlMessage = sqlException.getMessage();
										String[] columnValue = sqlMessage.split("\'");
										String message = "";
										if (columnValue[1].equals(taskStatus.getName()))
											message = "Row  " + count + " : Duplicate name " + "'" + taskStatus.getName()
													+ "'";
										logger.error(message);
										logs.add(message);
									}
								}
							}
						}
					}
				}
			}

		}

		catch (Exception e) {
			logger.error("exception" + e);
			e.printStackTrace();
		}

		return logs;
	}
	
	private Boolean checkIfNameIsValid(String[] row, int count, List<String> logs) {
		if (!row[0].matches("^[a-zA-Z0-9\\s]*$")) {
			logger.error("Row " + count + " : Invalid name " + "'" + row[0] + "'");
			logs.add("Row " + count + " : Invalid name " + "'" + row[0] + "'");
			return false;
		}
		return true;
	}

	private Boolean checkIfNotEmpty(String[] row, int count, List<String> logs) {

		if (row[0].trim().length() == 0 || row[0].trim().equals("")) {
			logger.error("Row " + count + " : 'Name' cannot be empty");
			logs.add("Row " + count + " : 'Name' cannot be empty");
			return false;
		}
		return true;
	}
}
