package com.ackermans.criticalpath.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
import com.ackermans.criticalpath.entity.Country;
import com.ackermans.criticalpath.entity.Event;
import com.ackermans.criticalpath.entity.Province;
import com.ackermans.criticalpath.entity.Store;
import com.ackermans.criticalpath.enums.ErrorStatus;
import com.ackermans.criticalpath.repository.CountryRepository;
import com.ackermans.criticalpath.utils.Constants;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

@Service
public class CountryService {

	private static final Logger logger = LogManager.getFormatterLogger(CountryService.class);

	@Autowired
	private CountryRepository countryRepository;
	
	@Autowired
	private StoreService storeService;
	
	private final Sort DEFAULT_SORT = Sort.by("name");

	/**
	 * Get All Country
	 * 
	 * @return
	 */
	public List<Country> getAll() {
		return countryRepository.findAll();
	}

	/**
	 * Get All Active Country
	 * 
	 * @return
	 */
	public List<Country> getAllActive() {
		return countryRepository.findAllByIsActiveTrue();
	}
	
	/**
	 * Get All  Delete Country
	 * 
	 * @return
	 */
	public List<Country> getAllDeletedCountry() {
		return countryRepository.findAllByIsActiveFalseAndIsDeleteTrue();
	}

	/**
	 * Get List By Searching parameter
	 * 
	 * @return
	 */
	public List<Country> search(String search) {
		return countryRepository.findByNameIgnoreCaseStartingWith(search);
	}

	/**
	 * Get Active Country List By Searching parameter
	 * 
	 * @param searchStr
	 * @return
	 */
	public List<Country> searchActive(String searchStr) {
		return countryRepository.findByNameIgnoreCaseStartingWithAndIsActiveTrue(searchStr);
	}

	/**
	 * Active Country By Id
	 * 
	 * @param countryId
	 * @return
	 */
	public Country activate(Long countryId) {
		Country country = countryRepository.getOne(countryId);
		country.setIsActive(true);
		country.setIsDelete(false);
		return countryRepository.save(country);
	}
	
	/**
	 * Get All deleted Province list With Fix Size Row
	 * 
	 * @param pageNumber
	 * @return
	 */
	public Page<Country> getAllDeletedCountry(int pageNumber) {
		Pageable pageable = PageRequest.of(pageNumber, Constants.PAGE_SIZE, DEFAULT_SORT);
		return countryRepository.findAllByIsActiveFalseAndIsDeleteTrue(pageable);
	}
	
	/**
	 * Get All Active With Fix Size Row
	 * 
	 * @param pageNumber
	 * @return
	 */
	public Page<Country> getAllActive(int pageNumber) {
		Pageable pageable = PageRequest.of(pageNumber, Constants.PAGE_SIZE, DEFAULT_SORT);
		return countryRepository.findAllByIsActiveTrue(pageable);
	}
	
	/**
	 * Get Active Country List By Searching Value With Fix row
	 * 
	 * @param searchStr
	 * @param pageNumber
	 * @return
	 */
	public Page<Country> searchActiveTrue(String searchStr, int pageNumber) {
		Pageable pageable = PageRequest.of(pageNumber, Constants.PAGE_SIZE, DEFAULT_SORT);
		return countryRepository.findBySearchActiveTrueCountry(searchStr, pageable);

	}
	
	/**
	 * Get deleted Country List By Searching Value With Fix row
	 * 
	 * @param searchStr
	 * @param pageNumber
	 * @return
	 */
	public Page<Country> searchActiveFalse(String searchStr, int pageNumber) {
		Pageable pageable = PageRequest.of(pageNumber, Constants.PAGE_SIZE, DEFAULT_SORT);
		return countryRepository.findBySearchActiveFalseCountry(searchStr, pageable);

	}


	/**
	 * DeActive Country By Id
	 * 
	 * @param countryId
	 * @return
	 */
	public Country deactivate(Long countryId) {
		Country country = countryRepository.getOne(countryId);
		country.setIsActive(false);
		country.setIsDelete(true);
		return countryRepository.save(country);
	}

	/**
	 * Verify and Save Country
	 * 
	 * @param country
	 * @return
	 * @throws CPException
	 */
	public Country verifyAndSave(Country country) throws CPException {

		boolean isExist = false;

		// If there is id that means record needs to be updated
		// hence we should ignore that record while checking for existence
		if (country.getId() != null && country.getId() > 0) {
			isExist = this.isExists(country.getCode(), country.getName(), country.getId());
		} else {
			isExist = this.isExists(country.getCode(), country.getName());
		}

		// If record is unique then save it else throw exception
		if (!isExist)
			this.save(country);
		else
			throw new CPException(ErrorStatus.DUPLICATE_RECORD);

		return country;
	}

	/**
	 * Save Or Update Country
	 * 
	 * @param country
	 * @return
	 */
	private Country save(Country country) {
		return countryRepository.save(country);
	}

	/**
	 * Check if Country exists for given Name And Code
	 * 
	 * @param code
	 * @param name
	 * @return
	 */
	private boolean isExists(String code, String name) {
		return countryRepository.countByCodeIgnoreCaseOrNameIgnoreCase(code, name) > 0;
	}

	/**
	 * Check if Country exists for given code ,name and Id
	 * 
	 * @param code
	 * @param name
	 * @param Id
	 * @return
	 */
	private boolean isExists(String code, String name, Long Id) {
		return countryRepository.countByCodeIgnoreCaseAndNameIgnoreCaseAndId(code, name, Id) > 0;
	}

	/**
	 * Get Country By countryId
	 * 
	 * @param countryId
	 * @return
	 */
	public Object getCountryData(Long countryId) {
		return countryRepository.findById(countryId);
	}
	
	/**
	 * Deletes the record
	 * @param countryId
	 * @return
	 * @throws CPException 
	 */
	public void deleteCountry(Long countryId) throws CPException {
		
		List<Store> store = storeService.findActiveStoreByCountryId(countryId);
		
		if(store.isEmpty()) {
			Country country = countryRepository.getOne(countryId);
			country.setIsActive(false);
			country.setIsDelete(true);
			countryRepository.save(country);
		} else
			throw new CPException(ErrorStatus.COUNTRY_EXISTS, "'" + store.get(0).getName() +"'");
			
		return;
	}

	/**
	 * Restores the record
	 * @param countryId
	 */
	public void restoreCountry(Long countryId) {
		Country country = countryRepository.getOne(countryId);
		country.setIsActive(true);
		country.setIsDelete(false);
		countryRepository.save(country);
		return ;
	}

	/**
	 * Upload country data in bulk
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
					if (row[0].equals("Name") && row[1].equals("Code") && allData.size() > 1) {
						isValidCsv = true;
//						count = 0;

					} else {
						logger.error("Invalid csv file");
						logs.add("Invalid csv file");
						return logs;
					}
				}

				if (count > 1) {
					Country country = new Country();

					if (checkIfNotEmpty(row, count, logs) && checkIfNameIsValid(row, count, logs)
							&& checkIfCodeIsValid(row, count, logs)) {
						country.setName(row[0]);
						country.setCode(row[1]);
						try {
							logger.info("Saving the entity in database");
							countryRepository.save(country);
						} catch (Exception e) {
							logger.error("Error while saving entity in database");
							for (Throwable t = e.getCause(); t != null; t = t.getCause()) {
								if (t.getClass().toString().contains("MySQLIntegrityConstraintViolationException")) {
									SQLException sqlException = (SQLException) t;
									if (sqlException.getMessage().toString().contains("Duplicate")) {

										String sqlMessage = sqlException.getMessage();
										String[] columnValue = sqlMessage.split("\'");
										String message = "";
										if (columnValue[1].equals(country.getName()))
											message = "Row  " + count + " : Duplicate name " + "'" + country.getName()
													+ "'";
										else
											message = "Row  " + count + " : Duplicate code " + "'" + country.getCode()
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
		if (!row[0].matches("^[a-zA-Z\\s]*$")) {
			logger.error("Row " + count + " : Invalid name " + "'" + row[0] + "'");
			logs.add("Row " + count + " : Invalid name " + "'" + row[0] + "'");
			return false;
		}
		return true;
	}

	private Boolean checkIfCodeIsValid(String[] row, int count, List<String> logs) {
		if (!row[1].matches("^[a-zA-Z]*$")) {
			logger.error("Row " + count + " : Invalid code " + "'" + row[1] + "'");
			logs.add("Row " + count + " : Invalid code " + "'" + row[1] + "'");
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

		if (row[1].trim().length() == 0 || row[1].trim().equals("")) {
			logger.error("Row " + count + " : 'Code' cannot be empty");
			logs.add("Row " + count + " : 'Code' cannot be empty");
			return false;
		}
		return true;
	}

}
