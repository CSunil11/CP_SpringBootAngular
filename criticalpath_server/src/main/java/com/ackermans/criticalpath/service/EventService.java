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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ackermans.criticalpath.common.dto.CPException;
import com.ackermans.criticalpath.entity.Event;
import com.ackermans.criticalpath.enums.ErrorStatus;
import com.ackermans.criticalpath.repository.EventRepository;
import com.ackermans.criticalpath.utils.Constants;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

@Service
public class EventService {
	
	private static final Logger logger = LogManager.getFormatterLogger(EventService.class);
	
	@Autowired
	private EventRepository eventRepository;
	
	private final Sort DEFAULT_SORT =  Sort.by("name"); 
	
	/**
	 * Verify and Save events
	 * @param event
	 * @return
	 * @throws CPException
	 */
	public Event verifyAndSave(Event event) throws CPException {
		
		boolean isExist = false;

		// If there is id that means record needs to be updated
		// hence we should ignore that record while checking for existence
		if (event.getId() != null && event.getId() > 0) {
			isExist = this.isExists(event.getName(), event.getId());
		} else {
			isExist = this.isExists(event.getName());
		}

		// If record is unique then save it else throw exception
		if (!isExist)
			this.save(event);
		else
			throw new CPException(ErrorStatus.DUPLICATE_RECORD);

		return event;
	}

	/**
	 * Check if Event exists for given name
	 * 
	 * @param name
	 * @return
	 */
	public boolean isExists(String name) {
		return eventRepository.countByNameIgnoreCase(name) > 0;
	}
	
	/**
	 * Check if Event exists for given name but ignore given id
	 * 
	 * @param name
	 * @return
	 */
	public boolean isExists(String name,Long ignoreId) {
		return eventRepository.countByNameAndIgnoreId(name, ignoreId) > 0;
	}
	
	/**
	 * Create new Event
	 * 
	 * @param event
	 * @return
	 */
	private Event save(Event event) {
		return eventRepository.save(event);
	}
	
	/**
	 * Get all Events
	 * @return
	 */
	public List<Event> getAll(){
		return eventRepository.findAll();
	}
	
	/**
	 * Activate Event by id
	 * 
	 * @param id
	 * @return
	 */
	public Event activate(Long id) {
		Event events = eventRepository.getOne(id);
		return this.activate(events);
	}
	
	/**
	 * Deactivate Event by id
	 * 
	 * @param id
	 * @return
	 */
	public Event deactivate(Long id) {
		Event event = eventRepository.getOne(id);
		return this.deactivate(event);
	}
	
	/**
	 * Activate Event by Event
	 * @param event
	 * @return
	 */
	public Event activate(Event event) {
		event.setIsActive(true);
		return eventRepository.save(event);
	}
	
	/**
	 * Deactivate Event by Event
	 * @param event
	 * @return
	 */
	public Event deactivate(Event event) {
		event.setIsActive(false);
		return eventRepository.save(event);
	}
	
	/**
	 * Get List By Searching parameter
	 * 
	 * @return
	 */
	public List<Event> search(String search) {
		return eventRepository.findByNameIgnoreCaseStartingWith(search);
	}
	
	/**
	 * Get List By PageNumber and PageSize
	 * @param pagenumber
	 * @param pagesize
	 * @return
	 */
	public Page<Event> Get(int pagenumber,int pagesize){
		Pageable pageable = PageRequest.of(pagenumber, pagesize, DEFAULT_SORT); 
		return eventRepository.findAll(pageable);
	}

	/**
	 * Get List By PageNumber and PageSize
	 * 
	 * @param pageNumber
	 * @return
	 */
	public Page<Event> getAll(int pageNumber) {
		Pageable pageable = PageRequest.of(pageNumber, Constants.PAGE_SIZE, DEFAULT_SORT);
		return eventRepository.findAll(pageable);
	}

	/**
	 * Get All Active Event List
	 * 
	 * @return
	 */
	public List<Event> getAllActive() {
		return eventRepository.findAllByIsActiveTrue();
	}
	
	/**
	 * Get All deleted Event List
	 * 
	 * @return
	 */
	public List<Event> getAllDeletedEvent() {
		return eventRepository.findAllByIsActiveFalseAndIsDeleteTrue();
	}

	/**
	 * Get All Active With Fix Size Row
	 * 
	 * @param pageNumber
	 * @return
	 */
	public Page<Event> getAllActive(int pageNumber) {
		Pageable pageable = PageRequest.of(pageNumber, Constants.PAGE_SIZE, DEFAULT_SORT);
		return eventRepository.findAllByIsActiveTrue(pageable);
	}

	/**
	 * Get Active Event List By Searching Value
	 * 
	 * @param searchStr
	 * @return
	 */
	public List<Event> searchActive(String searchStr) {
		return eventRepository.findByNameIgnoreCaseStartingWithAndIsActiveTrue(searchStr);
	}
	
	/**
	 * Get Event By eventId
	 * @param countryId
	 * @return
	 */
	public Object getEventData(Long eventId) {
		return eventRepository.findById(eventId);
	}

	/**
	 * Deletes the record
	 * @param eventId
	 * @return
	 */
	public void deleteEvent(Long eventId) {
		Event event = eventRepository.getOne(eventId);
		event.setIsActive(false);
		event.setIsDelete(true);
		eventRepository.save(event);
		return ;
	}

	/**
	 * Restores the event
	 * @param eventId
	 */
	public void restoreEvent(Long eventId) {
		Event event = eventRepository.getOne(eventId);
		event.setIsActive(true);
		event.setIsDelete(false);
		eventRepository.save(event);
		return ;
	}
	
	/**
	 * Upload event data in bulk
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
					Event event = new Event();

					if (checkIfNotEmpty(row, count, logs) && checkIfNameIsValid(row, count, logs)) {
						event.setName(row[0]);
						event.setDescription(row[1]);
						try {
							logger.info("Saving the entity in database");
							eventRepository.save(event);
						} catch (Exception e) {
							logger.error("Error while saving entity in database");
							for (Throwable t = e.getCause(); t != null; t = t.getCause()) {
								if (t.getClass().toString().contains("MySQLIntegrityConstraintViolationException")) {
									SQLException sqlException = (SQLException) t;
									if (sqlException.getMessage().toString().contains("Duplicate")) {

										String sqlMessage = sqlException.getMessage();
										String[] columnValue = sqlMessage.split("\'");
										String message = "";
										if (columnValue[1].equals(event.getName()))
											message = "Row  " + count + " : Duplicate name " + "'" + event.getName()
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
