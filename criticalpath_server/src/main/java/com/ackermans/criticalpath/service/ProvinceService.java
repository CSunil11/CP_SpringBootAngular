package com.ackermans.criticalpath.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.ackermans.criticalpath.common.dto.CPException;
import com.ackermans.criticalpath.entity.Country;
import com.ackermans.criticalpath.entity.LocationDivision;
import com.ackermans.criticalpath.entity.Province;
import com.ackermans.criticalpath.enums.ErrorStatus;
import com.ackermans.criticalpath.repository.ApiLoggerRepository;
import com.ackermans.criticalpath.repository.CountryRepository;
import com.ackermans.criticalpath.repository.ProvinceRepository;
import com.ackermans.criticalpath.utils.Constants;
import com.api.criticalpath.dto.RequestApiDataDto;
import com.api.criticalpath.dto.RequestApiDto;

@Service
public class ProvinceService {

	private static final Logger logger = LogManager.getFormatterLogger(ProvinceService.class);

	@Autowired
	private ProvinceRepository provinceRepository;

	@Autowired
	private CountryRepository countryRepository;

	@Autowired
	private ApiLoggerRepository apiLoggerRepository;

	@Autowired
	private ApiLoggerService apiLoggerService;

	private final Sort DEFAULT_SORT = Sort.by("name");

	/**
	 * Get all Province
	 * 
	 * @return
	 */
	public List<Province> getAll() {
		return provinceRepository.findAll();
	}

	/**
	 * Get List By PageNumber and PageSize
	 * 
	 * @param pageNumber
	 * @return
	 */
	public Page<Province> getAll(int pageNumber) {
		Pageable pageable = PageRequest.of(pageNumber, Constants.PAGE_SIZE, DEFAULT_SORT);
		return provinceRepository.findAll(pageable);
	}

	/**
	 * Get All Active Province List
	 * 
	 * @return
	 */
	public List<Province> getAllActive() {
		return provinceRepository.findAllByIsActiveTrue();
	}

	/**
	 * Get All Active With Fix Size Row
	 * 
	 * @param pageNumber
	 * @return
	 */
	public Page<Province> getAllActive(int pageNumber) {
		Pageable pageable = PageRequest.of(pageNumber, Constants.PAGE_SIZE, DEFAULT_SORT);
		return provinceRepository.findAllByIsActiveTrue(pageable);
	}

	/**
	 * Get All Delete Province List
	 * 
	 * @return
	 */
	public List<Province> getAllDeleteProvince() {
		return provinceRepository.findAllByIsDeleteTrue();
	}
	
	/**
	 * Get All deleted Province list With Fix Size Row
	 * 
	 * @param pageNumber
	 * @return
	 */
	public Page<Province> getAllDeletedProvince(int pageNumber) {
		Pageable pageable = PageRequest.of(pageNumber, Constants.PAGE_SIZE, DEFAULT_SORT);
		return provinceRepository.findAllByIsActiveFalseAndIsDeleteTrue(pageable);
	}

	/**
	 * Get List By Searching parameter
	 * 
	 * @return
	 */
	public List<Province> search(String search) {
		return provinceRepository.findByNameIgnoreCaseStartingWith(search);
	}

	/**
	 * Get Active Province List By Searching Value
	 * 
	 * @param searchStr
	 * @return
	 */
	public List<Province> searchActive(String searchStr) {
		return provinceRepository.findByNameIgnoreCaseStartingWithAndIsActiveTrue(searchStr);
	}
	
	/**
	 * Get Active Province List By Searching Value With Fix row
	 * 
	 * @param searchStr
	 * @param pageNumber
	 * @return
	 */
	public Page<Province> searchActiveTrue(String searchStr, int pageNumber) {
		Pageable pageable = PageRequest.of(pageNumber, Constants.PAGE_SIZE, DEFAULT_SORT);
		return provinceRepository.findBySearchActiveTrueProvince(searchStr, pageable);

	}
	
	/**
	 * Get deleted Province List By Searching Value With Fix row
	 * 
	 * @param searchStr
	 * @param pageNumber
	 * @return
	 */
	public Page<Province> searchActiveFalse(String searchStr, int pageNumber) {
		Pageable pageable = PageRequest.of(pageNumber, Constants.PAGE_SIZE, DEFAULT_SORT);
		return provinceRepository.findBySearchActiveFalseProvince(searchStr, pageable);

	}

	/**
	 * Activate province by id
	 * 
	 * @param id
	 * @return
	 */
	public Province activate(Long id) {
		Province province = provinceRepository.getOne(id);
		return this.activate(province);
	}

	/**
	 * Activate Province by province
	 * 
	 * @param event
	 * @return
	 */
	public Province activate(Province province) {
		province.setIsActive(true);
		return provinceRepository.save(province);
	}

	/**
	 * Deactivate Province by id
	 * 
	 * @param id
	 * @return
	 */
	public Province deactivate(Long id) {
		Province province = provinceRepository.getOne(id);
		return this.deactivate(province);
	}

	/**
	 * Deactivate Event by Event
	 * 
	 * @param event
	 * @return
	 */
	public Province deactivate(Province province) {
		province.setIsActive(false);
		return provinceRepository.save(province);
	}

	/**
	 * Restores the province
	 * 
	 * @param provinceId
	 */
	public void restoreProvince(Long provinceId) {
		Province province = provinceRepository.getOne(provinceId);
		province.setIsActive(true);
		province.setIsDelete(false);
		provinceRepository.save(province);
		return;
	}

	/**
	 * Deletes the record
	 * 
	 * @param provinceId
	 * @return
	 */
	public void deleteProvince(Long provinceId) {
		Province province = provinceRepository.getOne(provinceId);
		province.setIsActive(false);
		province.setIsDelete(true);
		provinceRepository.save(province);
		return;
	}

	/**
	 * Get Province By provinceId
	 * 
	 * @param countryId
	 * @return
	 */
	public Object getProvinceData(Long provinceId) {
		return provinceRepository.findById(provinceId);
	}

	public Province verifyAndSave(Province province) throws CPException {
		boolean isExist = false;

		// If there is id that means record needs to be updated
		// hence we should ignore that record while checking for existence
		if (province.getId() != null && province.getId() > 0) {
			isExist = this.isExists(province.getName(), province.getId());
		} else {
			isExist = this.isExists(province.getName());
		}

		// If record is unique then save it else throw exception
		if (!isExist)
			this.save(province);
		else
			throw new CPException(ErrorStatus.DUPLICATE_RECORD);
		
		return province;
	}

	/**
	 * Create new Province
	 * 
	 * @param province
	 * @return
	 */
	private Province save(Province province) {
		return provinceRepository.save(province);
	}

	/**
	 * Check if Province exists for given name
	 * 
	 * @param name
	 * @return
	 */
	public boolean isExists(String name) {
		return provinceRepository.countByNameIgnoreCase(name) > 0;
	}
	
	/**
	 * Check if Province exists for given name but ignore given id
	 * 
	 * @param name
	 * @return
	 */
	public boolean isExists(String name,Long ignoreId) {
		return provinceRepository.countByNameAndIgnoreId(name, ignoreId) > 0;
	}

	/**
	 * @param countryId
	 * @param provinceName
	 * @param isActive
	 * @return
	 * @throws CPException 
	 */
	public Province verifyAndSave(Long countryId, Long provinceId, String provinceName, Boolean isActive) throws CPException {
		Province province = new Province();
		province.setId(provinceId);
		province.setName(provinceName);
		province.setIsActive(isActive);
		Country country = new Country();
		country.setId(countryId);
		province.setCountry(country);
		
		return this.verifyAndSave(province);
	}

	/**
	 * @param countryId
	 * @return
	 * @throws CPException 
	 */
	public List<Province> getProvinceOfCountry(Long countryId) {
		
		return provinceRepository.findByCountryIdAndIsActiveTrue(countryId);
	}
	
	@Async
	public void saveImportedProvince(RequestApiDto apiDtoObj, Long transactionId) {

		if (apiDtoObj.getData().size() > 0) {
			for (int i = 0; i < apiDtoObj.getData().size(); i++) {

				if (validateProvince(transactionId, apiDtoObj.getData().get(i), apiDtoObj)) {
					Province proOb = provinceRepository
							.findByThirdPartyId(Long.parseLong(apiDtoObj.getData().get(i).getId().trim()));

					if (proOb != null) {
						proOb.setName(apiDtoObj.getData().get(i).getName().trim());

						if (setCountryObject(apiDtoObj, i, proOb, transactionId)) {
							try {
								provinceRepository.save(proOb);
								apiLoggerService.saveApiLog(transactionId, Constants.PROVINCE,
										Long.parseLong(apiDtoObj.getData().get(i).getId().trim()), Constants.UPDATED,
										Constants.BYTHIRDPARTYID, Constants.SUCCESSUPD, apiDtoObj.getMsgId());
								
							} catch (Exception e) {
								apiLoggerService.saveApiLog(transactionId, Constants.PROVINCE,
										Long.parseLong(apiDtoObj.getData().get(i).getId().trim()), Constants.FAILED,
										Constants.BYTHIRDPARTYID, e.getMessage(), apiDtoObj.getMsgId());
							}

						} else {
							apiLoggerService.saveApiLog(transactionId, Constants.PROVINCE,
									Long.parseLong(apiDtoObj.getData().get(i).getId().trim()), Constants.NOTFOUND,
									Constants.BYNAME, "Given country not available", apiDtoObj.getMsgId());
						}
					} else {

						Province proObWithName = provinceRepository
								.findByNameIgnoreCase(apiDtoObj.getData().get(i).getName().trim());

						if (proObWithName != null) {
							proObWithName.setThirdPartyId(Long.parseLong(apiDtoObj.getData().get(i).getId().trim()));

							if (setCountryObject(apiDtoObj, i, proObWithName, transactionId)) {
								try {
									provinceRepository.save(proObWithName);
									apiLoggerService.saveApiLog(transactionId, Constants.PROVINCE,
											Long.parseLong(apiDtoObj.getData().get(i).getId().trim()), Constants.UPDATED,
											Constants.BYNAME, Constants.SUCCESSUPD, apiDtoObj.getMsgId());
								} catch (Exception e) {
									apiLoggerService.saveApiLog(transactionId, Constants.PROVINCE,
											Long.parseLong(apiDtoObj.getData().get(i).getId().trim()), Constants.FAILED,
											Constants.BYNAME, e.getMessage(), apiDtoObj.getMsgId());
								}

							} else {
								apiLoggerService.saveApiLog(transactionId, Constants.PROVINCE,
										Long.parseLong(apiDtoObj.getData().get(i).getId().trim()), Constants.NOTFOUND,
										Constants.BYNAME, "Given country not available", apiDtoObj.getMsgId());
							}
						} else {
							Province prObjNew = new Province();
							prObjNew.setName(apiDtoObj.getData().get(i).getName().trim());
							prObjNew.setThirdPartyId(Long.parseLong(apiDtoObj.getData().get(i).getId().trim()));

							if (setCountryObject(apiDtoObj, i, prObjNew, transactionId)) {
								try {
									provinceRepository.save(prObjNew);
									apiLoggerService.saveApiLog(transactionId, Constants.PROVINCE,
											Long.parseLong(apiDtoObj.getData().get(i).getId().trim()), Constants.INSERTED,
											Constants.BYTHIRDPARTYID, Constants.SUCCESSINS, apiDtoObj.getMsgId());
								} catch (Exception e) {
									apiLoggerService.saveApiLog(transactionId, Constants.PROVINCE,
											Long.parseLong(apiDtoObj.getData().get(i).getId().trim()), Constants.FAILED,
											Constants.BYTHIRDPARTYID, e.getMessage(), apiDtoObj.getMsgId());
								}

							} else {
								apiLoggerService.saveApiLog(transactionId, Constants.PROVINCE,
										Long.parseLong(apiDtoObj.getData().get(i).getId().trim()), Constants.NOTFOUND,
										Constants.BYTHIRDPARTYID, "Given country not available", apiDtoObj.getMsgId());
							}
						}
					}
				}
			}
		} else
			apiLoggerService.saveApiLog(transactionId, Constants.PROVINCE, Long.valueOf(0), Constants.NOTFOUND,
					Constants.SKIPPED, "Data parameter is empty in request", apiDtoObj.getMsgId());
	}

	private boolean setCountryObject(RequestApiDto apiDtoObj, int i, Province proOb, Long transactionId) {
		boolean checkFlag = true;
		try {
				if (validatingCountryCode(apiDtoObj, i)) {
					Country countryObj = countryRepository.findByCodeIgnoreCase(apiDtoObj.getData().get(i).getCountry_code().trim());
					if (countryObj != null) {
						proOb.setCountry(countryObj);
					} else {
						try {
							Country newCountryOb = new Country();
							newCountryOb.setName(apiDtoObj.getData().get(i).getCountry_code().trim());
							newCountryOb.setCode(apiDtoObj.getData().get(i).getCountry_code().trim());
							newCountryOb.setIsActive(false);
							newCountryOb.setIsDelete(true);
							newCountryOb = countryRepository.save(newCountryOb);
							
							proOb.setCountry(newCountryOb);
							
						} catch (Exception e) {
							proOb.setCountry(null);
							apiLoggerService.saveApiLog(transactionId, Constants.PROVINCE,
									Long.parseLong(apiDtoObj.getData().get(i).getId().trim()), Constants.FAILED,
									Constants.BYTHIRDPARTYID, "Error while creating new contry:"+e.getMessage(), apiDtoObj.getMsgId());
							logger.debug("Error while creating new contry: "+e.getMessage());
						}
					}
				} else {
					proOb.setCountry(null);
					apiLoggerService.saveApiLog(transactionId, Constants.PROVINCE,
							Long.parseLong(apiDtoObj.getData().get(i).getId().trim()), Constants.FAILED,
							Constants.VALIDATION_FAILED, "Error while validation: contry_code not valid", apiDtoObj.getMsgId());
					logger.debug("Error while validation: contry_code not valid");
				}

		} catch (Exception e) {
			proOb.setCountry(null);
			logger.debug("Error while get data. " +e.getMessage());
			apiLoggerService.saveApiLog(transactionId, Constants.PROVINCE,
					Long.parseLong(apiDtoObj.getData().get(i).getId().trim()), Constants.FAILED,
					Constants.VALIDATION_FAILED, "Error while get data. " +e.getMessage(), apiDtoObj.getMsgId());
		}
		
		proOb.setIsActive(true);
		proOb.setIsDelete(false);

		return checkFlag;
	}

	private boolean validatingCountryCode(RequestApiDto apiDtoObj, int i) {
		
		boolean checkFlag = false;
		
		checkFlag = apiDtoObj.getData().get(i).getCountry_code() != null
				&& !apiDtoObj.getData().get(i).getCountry_code().trim().isEmpty()
				&& !apiDtoObj.getData().get(i).getCountry_code().trim().equalsIgnoreCase("0");
		
		return checkFlag;
	}

	/**
	 * Validates the request
	 * 
	 * @param transactionId
	 * @param requestApiDataDto
	 * @return
	 */
	private Boolean validateProvince(Long transactionId, RequestApiDataDto requestApiDataDto, RequestApiDto apiDtoObj) {

		try {
			if (requestApiDataDto.getId() == null) {
				apiLoggerService.saveApiLog(transactionId, Constants.PROVINCE, Long.valueOf(0), Constants.VALIDATION_FAILED,
						Constants.SKIPPED, "Error due to Insufficient data: Id parameter not found in request for Province_id: "+requestApiDataDto.getId(), apiDtoObj.getMsgId());
				logger.debug("Error due to Insufficient data: Id parameter not found in request for Province_id: "+requestApiDataDto.getId());
				return false;
				
			} else if (requestApiDataDto.getId().equals("") || requestApiDataDto.getId().equals("0") || requestApiDataDto.getId().equalsIgnoreCase("null")) {
				apiLoggerService.saveApiLog(transactionId, Constants.PROVINCE, Long.valueOf(0), Constants.VALIDATION_FAILED,
						Constants.SKIPPED, "Error due to Insufficient data: Id value not valid in request for Province_id: "+requestApiDataDto.getId(), apiDtoObj.getMsgId());
				logger.debug("Error due to Insufficient data: Id value not valid in request for Province_id: "+requestApiDataDto.getId());
				return false;
				
			} else if (requestApiDataDto.getName() == null) {
				apiLoggerService.saveApiLog(transactionId, Constants.PROVINCE, Long.valueOf(requestApiDataDto.getId()),
						Constants.VALIDATION_FAILED, Constants.SKIPPED, "Error due to Insufficient data: Name parameter not found in request for Province_id: "+requestApiDataDto.getId(),
						apiDtoObj.getMsgId());
				logger.debug("Error due to Insufficient data: Name parameter not found in request for Province_id: "+requestApiDataDto.getId());
				return false;
				
			}
			else if (requestApiDataDto.getName().equals("") || requestApiDataDto.getName().equalsIgnoreCase("null")) {
				apiLoggerService.saveApiLog(transactionId, Constants.PROVINCE, Long.valueOf(requestApiDataDto.getId()),
						Constants.VALIDATION_FAILED, Constants.SKIPPED, "Error due to Insufficient data: Name value not found in request for Province_id: "+requestApiDataDto.getId(),
						apiDtoObj.getMsgId());
				logger.debug("Error due to Insufficient data: Name value not found in request for Province_id: "+requestApiDataDto.getId());
				return false;
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

}
