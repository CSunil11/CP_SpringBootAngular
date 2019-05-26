package com.ackermans.criticalpath.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ackermans.criticalpath.common.dto.CPException;
import com.ackermans.criticalpath.entity.Country;
import com.ackermans.criticalpath.entity.LocationDivision;
import com.ackermans.criticalpath.entity.Province;
import com.ackermans.criticalpath.entity.Store;
import com.ackermans.criticalpath.entity.User;
import com.ackermans.criticalpath.entity.UserLogin;
import com.ackermans.criticalpath.enums.ErrorStatus;
import com.ackermans.criticalpath.enums.UserRole;
import com.ackermans.criticalpath.repository.CountryRepository;
import com.ackermans.criticalpath.repository.LocationDivisionRepository;
import com.ackermans.criticalpath.repository.ProvinceRepository;
import com.ackermans.criticalpath.repository.UserLoginRepository;
import com.ackermans.criticalpath.repository.UserRepository;
import com.ackermans.criticalpath.utils.Constants;
import com.api.criticalpath.dto.RequestApiDataDto;
import com.api.criticalpath.dto.RequestApiDto;

@Service
public class LocationDivisionService {
	
	private static final Logger logger = LogManager.getFormatterLogger(LocationDivisionService.class);
	
	@Autowired
	private LocationDivisionRepository locationDivisionRepo;
	
	@Autowired
	private ApiLoggerService apiLoggerService;
	
	@Autowired
	private CountryRepository countryRepository;
	
	@Autowired
	private ProvinceRepository provinceRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserLoginRepository userLoginRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	private final Sort DEFAULT_SORT = Sort.by("name");

	/**
	 * Build object from given value and verify and save
	 * 
	 * @param id
	 * @param name
	 * @param uid
	 * @param isActive
	 * @return
	 * @throws CPException 
	 */
	public LocationDivision verifyAndSave(Long id, String name, Long uid, Long cid, Long pid, boolean isActive) throws CPException {
		LocationDivision locDiv = new LocationDivision();
		locDiv.setId(id);
		locDiv.setName(name);
		locDiv.setIsActive(isActive);
		User divisionalSalesManagers = new User();
		divisionalSalesManagers.setId(uid);
		locDiv.setDivisionalSalesManagers(divisionalSalesManagers );
		
		/*
		 * User regionalManagers = new User(); regionalManagers.setId(ramuid);
		 * locDiv.setRegionalManagers(regionalManagers);
		 */
		
		Country country = new Country();
		country.setId(cid);
		locDiv.setCountry(country);
		
		Province province = new Province();
		province.setId(pid);
		locDiv.setProvince(province);
		return this.verifyAndSave(locDiv);
	}
	
	/**
	 * Verify and Save location division
	 * 
	 * @param locationDivision
	 * @return
	 * @throws CPException
	 */
	public LocationDivision verifyAndSave(LocationDivision locationDivision) throws CPException {
		
		this.verify(locationDivision);
		
		return this.save(locationDivision);
	}
	
	/**
	 * Save location division
	 * 
	 * @param locationDivision
	 * @return
	 */
	private LocationDivision save(LocationDivision locationDivision) {
		return locationDivisionRepo.save(locationDivision);
	}

	/**
	 * Validate Entity
	 * 
	 * @param locationDivision
	 * @return
	 * @throws CPException
	 */
	public void verify(LocationDivision locationDivision) throws CPException {
		
		// If there is id that means record needs to be updated
		// hence we should ignore that record while checking for existence
//		if (locationDivision.getId() != null 
//				&& locationDivision.getId() > 0 
//				&& this.isExists(locationDivision.getName(), locationDivision.getId())) {
//			
//			throw new CPException(ErrorStatus.DUPLICATE_RECORD);
//			
//		} else if (this.isExists(locationDivision.getName())) {
//			
//			throw new CPException(ErrorStatus.DUPLICATE_RECORD);
//		}	
		
		boolean isExist = false;
		
		if (locationDivision.getId() != null && locationDivision.getId() > 0) {
			isExist = this.isExists(locationDivision.getName(), locationDivision.getId());
		} else {
			isExist = this.isExists(locationDivision.getName());
		}

		// If record is unique then save it else throw exception
		if (isExist)
			throw new CPException(ErrorStatus.DUPLICATE_RECORD);
	}

	/**
	 * Check if location division exists for given name
	 * 
	 * @param name
	 * @return
	 */
	public boolean isExists(String name) {
		return locationDivisionRepo.countByNameIgnoreCase(name) > 0;
	}

	/**
	 * Check if location division exists for given name but ignore given id
	 * 
	 * @param name
	 * @param id
	 * @return
	 */
	public boolean isExists(String name, Long id) {
		return locationDivisionRepo.countByNameAndIgnoreId(name, id) > 0;
	}

	/**
	 * Get all location division
	 * 
	 * @return
	 */
	public List<LocationDivision> getAll() {
		return locationDivisionRepo.findAll();
	}
	
	/**
	 * Get all active location division
	 * 
	 * @return
	 */
	public List<LocationDivision> getAllActive() {
		return locationDivisionRepo.findAllByIsActiveTrueAndIsDeleteFalse();
	}
	
	/**
	 * Get All Active With Fix Size Row
	 * 
	 * @param pageNumber
	 * @return
	 */
	public Page<LocationDivision> getAllActive(int pageNumber) {
		Pageable pageable = PageRequest.of(pageNumber, Constants.PAGE_SIZE, DEFAULT_SORT);
		return locationDivisionRepo.findAllByIsActiveTrueAndIsDeleteFalse(pageable);
	}

	/**
	 * Get all active location division
	 * 
	 * @return
	 */
	public List<LocationDivision> getAllDeletedLocationDivi() {
		return locationDivisionRepo.findAllByIsActiveFalseAndIsDeleteTrue();
	}
	
	/**
	 * Soft Delete record for given id
	 * It will update active flag to false and delete flag to true
	 * 
	 * @param locationDivisionId
	 */
	public void delete(Long locationDivisionId) {
		locationDivisionRepo.delete(locationDivisionId); 
	}
	
	/**
	 * Restore record for given id
	 * It will update active flag to true and delete flag to false
	 * 
	 * @param locationDivisionId
	 */
	public void restore(Long locationDivisionId) {
		locationDivisionRepo.restore(locationDivisionId);
	}

	/**
	 * Get LocationDivision By locationDivisionId
	 * @param locationDivisionId
	 * @return
	 */
	public Object getLocationDivisionData(Long locationDivisionId) {
		return locationDivisionRepo.findById(locationDivisionId);
	}
	
	/**
	 * Get LocationDivision of Province
	 * @param provinceId
	 * @return
	 * @throws CPException 
	 */
	public List<LocationDivision> getLocationDivisionOfProvince(Long provinceId) {

		return locationDivisionRepo.findByProvinceIdAndIsActiveTrue(provinceId);
	}
	
	@Async
	public void saveImportedLocDiv(RequestApiDto apiDtoObj, Long transactionId) {
		
		if (apiDtoObj.getData().size() > 0) {
			for (int i = 0; i < apiDtoObj.getData().size(); i++) {

				if (validateLocationDivision(transactionId, apiDtoObj.getData().get(i), apiDtoObj)) {
					LocationDivision locDivOb = locationDivisionRepo
							.findByThirdPartyId(Long.parseLong(apiDtoObj.getData().get(i).getId().trim()));

					if (locDivOb != null) {

						if (!locDivOb.getName().trim().equalsIgnoreCase(apiDtoObj.getData().get(i).getName().trim())) {
							locDivOb.setName(apiDtoObj.getData().get(i).getName().trim());
						}

						if (setUsersCouPro(apiDtoObj, i, locDivOb, transactionId)) {
							try {
								locationDivisionRepo.save(locDivOb);
								apiLoggerService.saveApiLog(transactionId, Constants.LOCDIV,
										Long.parseLong(apiDtoObj.getData().get(i).getId().trim()), Constants.UPDATED,
										Constants.BYTHIRDPARTYID, Constants.SUCCESSUPD, apiDtoObj.getMsgId());
							} catch (Exception e) {
								apiLoggerService.saveApiLog(transactionId, Constants.LOCDIV,
										Long.parseLong(apiDtoObj.getData().get(i).getId().trim()), Constants.FAILED,
										Constants.BYTHIRDPARTYID, e.getMessage(), apiDtoObj.getMsgId());
								logger.debug("Error while saving Location Division byThirdPartyId for LocationDivision_id: "+apiDtoObj.getData().get(i).getId().trim()+" : "+e.getMessage());
							}

						}
					} else {

						LocationDivision locDivObWithName = locationDivisionRepo
								.findByNameIgnoreCase(apiDtoObj.getData().get(i).getName().toString().trim());

						if (locDivObWithName != null) {
							locDivObWithName.setThirdPartyId(Long.parseLong(apiDtoObj.getData().get(i).getId().trim()));
							if (setUsersCouPro(apiDtoObj, i, locDivObWithName, transactionId)) {
								try {
									locationDivisionRepo.save(locDivObWithName);
									apiLoggerService.saveApiLog(transactionId, Constants.LOCDIV,
											Long.parseLong(apiDtoObj.getData().get(i).getId().trim()), Constants.UPDATED,
											Constants.BYNAME, Constants.SUCCESSUPD, apiDtoObj.getMsgId());
								} catch (Exception e) {
									apiLoggerService.saveApiLog(transactionId, Constants.LOCDIV,
											Long.parseLong(apiDtoObj.getData().get(i).getId().trim()), Constants.FAILED,
											Constants.BYNAME, e.getMessage(), apiDtoObj.getMsgId());
									logger.debug("Error while saving Location Division byName for LocationDivision_id: "+apiDtoObj.getData().get(i).getId().trim()+" : "+e.getMessage());
								}
							} else {
								apiLoggerService.saveApiLog(transactionId, Constants.LOCDIV,
										Long.parseLong(apiDtoObj.getData().get(i).getId().trim()), Constants.NOTFOUND,
										Constants.BYNAME, Constants.SKIPPED, apiDtoObj.getMsgId());
							}
						} else {
							LocationDivision ld = new LocationDivision();
							ld.setThirdPartyId(Long.parseLong(apiDtoObj.getData().get(i).getId().trim()));
							ld.setName(apiDtoObj.getData().get(i).getName().trim());
							if (setUsersCouPro(apiDtoObj, i, ld, transactionId)) {
								try {
									locationDivisionRepo.save(ld);
									apiLoggerService.saveApiLog(transactionId, Constants.LOCDIV,
											Long.parseLong(apiDtoObj.getData().get(i).getId().trim()), Constants.INSERTED,
											Constants.BYTHIRDPARTYID, Constants.SUCCESSINS, apiDtoObj.getMsgId());
								} catch (Exception e) {
									apiLoggerService.saveApiLog(transactionId, Constants.LOCDIV,
											Long.parseLong(apiDtoObj.getData().get(i).getId().trim()), Constants.FAILED,
											Constants.BYTHIRDPARTYID, e.getMessage(), apiDtoObj.getMsgId());
									logger.debug("Error while saving Location Division with LocationDivision_id: "+apiDtoObj.getData().get(i).getId().trim()+" : "+e.getMessage());
								}

							} else {
								apiLoggerService.saveApiLog(transactionId, Constants.LOCDIV,
										Long.parseLong(apiDtoObj.getData().get(i).getId().trim()), Constants.NOTFOUND,
										Constants.BYTHIRDPARTYID, Constants.SKIPPED, apiDtoObj.getMsgId());
							}
						}
					}
				}
				else
					logger.debug("Error while validating Location Division with LocationDivision_id: "+apiDtoObj.getData().get(i).getId().trim());
			}
		}
		else
		{
			apiLoggerService.saveApiLog(transactionId, Constants.LOCDIV, Long.valueOf(0), Constants.NOTFOUND,
					Constants.SKIPPED, "Data parameter is empty in request", apiDtoObj.getMsgId());
			logger.debug("Error as Location Division request doesn't have data");
		}
	}

	private boolean setUsersCouPro(RequestApiDto apiDtoObj, int i, LocationDivision locDivOb, Long transactionId) {

		boolean checkFlag = true;

		settingProCtrObj(apiDtoObj, i, locDivOb, transactionId);

		settingDsmUserForLocDiv(apiDtoObj, i, locDivOb, transactionId);

//		settingRamUserForLocDiv(apiDtoObj, i, locDivOb, transactionId);

		locDivOb.setIsActive(true);
		locDivOb.setIsDelete(false);

		return checkFlag;
	}

//	private void settingRamUserForLocDiv(RequestApiDto apiDtoObj, int i, LocationDivision locDivOb,
//			Long transactionId) {
//
//		try {
//			if (validatingRamUserId(apiDtoObj, i)) {
//
//				List<User> ramUserObList = userRepository.findByThirdPartyId(Long.parseLong(apiDtoObj.getData().get(i).getRam_id().trim()));
//				UserRole role = null;
//				for (UserRole userRole : UserRole.values()) { 
//					if (userRole.name().equals("ROLE_RAM_USER")) 
//						{ 
//						 role = userRole;
//						} 
//				}
//				User ramUserOb = userRepository.getUserByThirdPartyId(Long.parseLong(apiDtoObj.getData().get(i).getRam_id().trim()) , "ROLE_RAM_USER");; 
//				
//				if (ramUserOb != null) {
//
//					if (ramUserOb.getUserLogin() != null) {
//						if (userLoginRepository.findRoleByUserId(ramUserOb.getId()).equals("ROLE_RAM_USER")) {
//							locDivOb.setRegionalManagers(ramUserOb);
//						} else {
//							User userDummyObj = setDummyRamUserForLocDiv(apiDtoObj.getData().get(i).getRam_id().trim());
//							locDivOb.setRegionalManagers(userDummyObj);
//							apiLoggerService.saveApiLog(transactionId, Constants.LOCDIV,
//									Long.parseLong(apiDtoObj.getData().get(i).getId().trim()), Constants.NOTFOUND,
//									Constants.BYTHIRDPARTYID,
//									"Given User is not RAM User, So new user created with LocationDivision_id: "
//											+ apiDtoObj.getData().get(i).getId().trim(),
//									apiDtoObj.getMsgId());
//							logger.debug("Given User is not RAM User, So new user created with LocationDivision_id: "
//											+ apiDtoObj.getData().get(i).getId().trim());
//						}
//					} else {
//						
//						try {
//							
//							UserLogin userLoginDummyObj = new UserLogin();
//							UserRole roleNewForRam = null;
//							for (UserRole userRole : UserRole.values()) { 
//								if (userRole.name().equals("ROLE_RAM_USER")) 
//									{ 
//									 roleNewForRam = userRole;
//									} 
//								}
//							userLoginDummyObj.setRole(roleNewForRam);
//							userLoginDummyObj.setEmail("ram"+apiDtoObj.getData().get(i).getRam_id().trim()+"@cp.com");
//							userLoginDummyObj.setIsActive(false);
//							userLoginDummyObj.setIsDelete(true);
//							userLoginDummyObj.setPassword(passwordEncoder.encode(Constants.DEFAULTPASSWORD));
//							userLoginDummyObj=userLoginRepository.save(userLoginDummyObj);
//							
//							ramUserOb.setUserLogin(userLoginDummyObj);
//							
//							locDivOb.setRegionalManagers(ramUserOb);
//							
//						} catch (Exception e) {
//							locDivOb.setRegionalManagers(ramUserOb);
//							logger.debug("Error while creating new Ram UserLogin for LocationDivision_id: "+ apiDtoObj.getData().get(i).getId().trim()+" : "+e.getMessage());
//						}
//						
//					}
//
//				} else {
//					User userDummyObjNew = setDummyRamUserForLocDiv(apiDtoObj.getData().get(i).getRam_id().trim());
//					locDivOb.setRegionalManagers(userDummyObjNew);
//					logger.debug("User not available for LocationDivision_id: "+ apiDtoObj.getData().get(i).getId().trim());
//				}
//
//			} else {
//				locDivOb.setRegionalManagers(null);
//				logger.debug("Error while validation of RAM value for LocationDivision_id: "+apiDtoObj.getData().get(i).getId().trim());
//			}
//		} catch (Exception e) {
//			locDivOb.setRegionalManagers(null);
//			logger.debug("Error while fetching Ram data for LocationDivision_id: "+apiDtoObj.getData().get(i).getId().trim()+" :  " + e.getMessage());
//		}
//	}

	private void settingDsmUserForLocDiv(RequestApiDto apiDtoObj, int i, LocationDivision locDivOb,
			Long transactionId) {
		try {
			if (validatingDsmUserId(apiDtoObj, i)) {

				UserRole role = null;
				for (UserRole userRole : UserRole.values()) { 
					if (userRole.name().equals("ROLE_DSM_USER")) 
						{ 
						 role = userRole;
						}
				}
				User dsmUserOb = userRepository.getUserByThirdPartyId(Long.parseLong(apiDtoObj.getData().get(i).getDsm_id().trim()) , "ROLE_DSM_USER"); 
				
				if (dsmUserOb != null) {

					if (dsmUserOb.getUserLogin() != null) {
						if (userLoginRepository.findRoleByUserId(dsmUserOb.getId()).equals("ROLE_DSM_USER")) {
							locDivOb.setDivisionalSalesManagers(dsmUserOb);
						} else {
							User userDummyObj = setDummyDsmUserForLocDiv(apiDtoObj.getData().get(i).getDsm_id().trim());
							locDivOb.setDivisionalSalesManagers(userDummyObj);
							apiLoggerService.saveApiLog(transactionId, Constants.LOCDIV,
									Long.parseLong(apiDtoObj.getData().get(i).getId().trim()), Constants.NOTFOUND,
									Constants.BYTHIRDPARTYID, "User is not DSM User", apiDtoObj.getMsgId());
							logger.debug("DSM User not available for LocationDivision_id: "+apiDtoObj.getData().get(i).getId().trim());
						}
					} else {
						
						try {
							UserLogin userLoginDummyObj = new UserLogin();
							UserRole roleNew = null;
							for (UserRole userRole : UserRole.values()) { 
								if (userRole.name().equals("ROLE_DSM_USER")) 
									{ 
									 roleNew = userRole;
									} 
								}
							userLoginDummyObj.setRole(roleNew);
							userLoginDummyObj.setEmail("dsm"+apiDtoObj.getData().get(i).getDsm_id().trim()+"@cp.com");
							userLoginDummyObj.setIsActive(false);
							userLoginDummyObj.setIsDelete(true);
							userLoginDummyObj.setPassword(passwordEncoder.encode(Constants.DEFAULTPASSWORD));
							userLoginDummyObj=userLoginRepository.save(userLoginDummyObj);
							dsmUserOb.setUserLogin(userLoginDummyObj);
							
							locDivOb.setDivisionalSalesManagers(dsmUserOb);
						
						} catch (Exception e) {
							locDivOb.setDivisionalSalesManagers(dsmUserOb);
							logger.debug("Error while creating new DSM UserLogin for LocationDivision_id: "+apiDtoObj.getData().get(i).getId().trim()+ " : "+e.getMessage());
						}
					}

				} else {
					User userDummyObjNew = setDummyDsmUserForLocDiv(apiDtoObj.getData().get(i).getDsm_id().trim());
					locDivOb.setDivisionalSalesManagers(userDummyObjNew);
					logger.debug("DSM User not available for LocationDivision_id: "+apiDtoObj.getData().get(i).getId().trim());
				}

			} else {
				locDivOb.setDivisionalSalesManagers(null);
				logger.debug("Error while validation DSM value is 0 or blank for LocationDivision_id: "+apiDtoObj.getData().get(i).getId().trim());
			}
		} catch (Exception e) {
			locDivOb.setDivisionalSalesManagers(null);
			logger.debug("Error while get Data of DSM value is 0 or blank for LocationDivision_id: "+apiDtoObj.getData().get(i).getId().trim()+" : "+e.getMessage());
		}
	}

	private boolean validatingDsmUserId(RequestApiDto apiDtoObj, int i) {

		boolean checkFlag = true;

		checkFlag = apiDtoObj.getData().get(i).getDsm_id() != null
				&& !apiDtoObj.getData().get(i).getDsm_id().trim().isEmpty()
				&& !apiDtoObj.getData().get(i).getDsm_id().trim().equalsIgnoreCase("0")
				&& !apiDtoObj.getData().get(i).getDsm_id().trim().equalsIgnoreCase("null");

		return checkFlag;
	}

	private boolean validatingRamUserId(RequestApiDto apiDtoObj, int i) {

		boolean checkFlag = apiDtoObj.getData().get(i).getRam_id() != null
				&& !apiDtoObj.getData().get(i).getRam_id().trim().isEmpty()
				&& !apiDtoObj.getData().get(i).getRam_id().trim().equalsIgnoreCase("0")
				&& !apiDtoObj.getData().get(i).getRam_id().trim().equalsIgnoreCase("null");

		return checkFlag;
	}

	
	private User setDummyDsmUserForLocDiv(String thirdPartyId) {
		User userLdObj = new User();
		try {
			UserLogin userLoginDummyObj = new UserLogin();
			UserRole role = null;
			for (UserRole userRole : UserRole.values()) { 
				if (userRole.name().equals("ROLE_DSM_USER")) 
					{ 
					 role = userRole;
					} 
				}
			userLoginDummyObj.setRole(role);
			userLoginDummyObj.setEmail("dsm"+thirdPartyId+"@cp.com");
			userLoginDummyObj.setIsActive(false);
			userLoginDummyObj.setIsDelete(true);
			userLoginDummyObj.setPassword(passwordEncoder.encode(Constants.DEFAULTPASSWORD));
			userLoginDummyObj=userLoginRepository.save(userLoginDummyObj);
			
			userLdObj.setThirdPartyId(Long.parseLong(thirdPartyId));
			userLdObj.setIsActive(false);
			userLdObj.setIsDelete(true);
			userLdObj.setPhone("0000000000");
			userLdObj.setUserLogin(userLoginDummyObj);
			userLdObj.setName("NA-" + thirdPartyId);
			userLdObj = userRepository.save(userLdObj);

		} catch (Exception e) {
			userLdObj = null;
			logger.debug("Error in setDummyDsmUserForLocDiv for location Division  set dsm  user with locationDivivsion_id: " +userLdObj.getThirdPartyId()+" "+ e.getMessage());
		}
		return userLdObj;
	}

	private User setDummyRamUserForLocDiv(String thirdPartyId) {
		User userLdObj = new User();
		try {
			UserLogin userLoginDummyObj = new UserLogin();
			UserRole role = null;
			for (UserRole userRole : UserRole.values()) { 
				if (userRole.name().equals("ROLE_RAM_USER")) 
					{ 
					 role = userRole;
					} 
				}
			userLoginDummyObj.setRole(role);
			userLoginDummyObj.setEmail("ram"+thirdPartyId+"@cp.com");
			userLoginDummyObj.setIsActive(false);
			userLoginDummyObj.setIsDelete(true);
			userLoginDummyObj.setPassword(passwordEncoder.encode(Constants.DEFAULTPASSWORD));
			userLoginDummyObj=userLoginRepository.save(userLoginDummyObj);
			
			userLdObj.setThirdPartyId(Long.parseLong(thirdPartyId));
			userLdObj.setIsActive(false);
			userLdObj.setIsDelete(true);
			userLdObj.setPhone("0000000000");
			userLdObj.setUserLogin(userLoginDummyObj);
			userLdObj.setName("NA-" + thirdPartyId);
			userLdObj = userRepository.save(userLdObj);

		} catch (Exception e) {
			userLdObj = null;
			logger.debug("Error in setDummyRamUserForLocDiv for location Division  set ram  user with locationDivivsion_id: " +userLdObj.getThirdPartyId()+" "+ e.getMessage());
		}
		return userLdObj;
	}
	
	private void settingProCtrObj(RequestApiDto apiDtoObj, int i, LocationDivision locDivOb, Long transactionId) {
		try {
			if (validatingProvinceId(apiDtoObj, i)) {
				Province proOb = provinceRepository.findByThirdPartyId(Long.parseLong(apiDtoObj.getData().get(i).getProvince_id().trim()));
	
				if (proOb != null) {
	
					String ctrCode = null;
					locDivOb.setProvince(proOb);
	
					try {
						if (proOb.getCountry() != null)
							ctrCode = proOb.getCountry().getCode();
						else
							locDivOb.setCountry(null);
	
						if (ctrCode != null) {
							Country ctrOb = countryRepository.findByCodeIgnoreCase(ctrCode);
							if (ctrOb != null)
								locDivOb.setCountry(ctrOb);
							else
								locDivOb.setCountry(null);
								logger.debug("Error Country not found for LocationDivision_id: "+apiDtoObj.getData().get(i).getId().trim());
						}
						else
						{
							locDivOb.setCountry(null);
							logger.debug("Error Country not found for LocationDivision_id: "+apiDtoObj.getData().get(i).getId().trim());
						}
					} catch (Exception e) {
						locDivOb.setCountry(null);
					}
	
				} else {
					try {
							Province newPrObj = new Province();
							newPrObj.setThirdPartyId(Long.parseLong(apiDtoObj.getData().get(i).getProvince_id().trim()));
							newPrObj.setName("NA-" + apiDtoObj.getData().get(i).getProvince_id().trim());
							newPrObj.setCountry(null);
							newPrObj.setIsActive(false);
							newPrObj.setIsDelete(true);
							newPrObj = provinceRepository.save(newPrObj);
							
							locDivOb.setProvince(newPrObj);
							locDivOb.setCountry(null);
						
						} catch (Exception e) {
							locDivOb.setProvince(null);
							locDivOb.setCountry(null);
							apiLoggerService.saveApiLog(transactionId, Constants.PROVINCE,
									Long.parseLong(apiDtoObj.getData().get(i).getId()), Constants.FAILED,
									Constants.BYTHIRDPARTYID, e.getMessage(), apiDtoObj.getMsgId());
							logger.debug("Error while creating new Province object for LocationDivision_id: "+apiDtoObj.getData().get(i).getId().trim() +" Error:  " + e.getMessage());
						}
				}
			} else {
				locDivOb.setProvince(null);
				locDivOb.setCountry(null);
				logger.debug("Error while validating province value may be 0 or blank for LocationDivision_id: "+apiDtoObj.getData().get(i).getId().trim());
			}
		} catch (Exception e) {
			locDivOb.setProvince(null);
			locDivOb.setCountry(null);
			logger.debug("Error while get Data of Province for LocationDivision_id: "+apiDtoObj.getData().get(i).getId().trim()+" : " + e.getMessage());
		}
	}

	private boolean validatingProvinceId(RequestApiDto apiDtoObj, int i) {
		return apiDtoObj.getData().get(i).getProvince_id() != null
				&& !apiDtoObj.getData().get(i).getProvince_id().trim().isEmpty()
				&& !apiDtoObj.getData().get(i).getProvince_id().trim().equalsIgnoreCase("0")
				&& !apiDtoObj.getData().get(i).getProvince_id().trim().equalsIgnoreCase("null");
	}

	/**
	 * Validates the request
	 * 
	 * @param transactionId
	 * @param requestApiDataDto
	 * @return
	 */
	private Boolean validateLocationDivision(Long transactionId, RequestApiDataDto requestApiDataDto,
			RequestApiDto apiDtoObj) {

		try {
			if (requestApiDataDto.getId() == null) {
				apiLoggerService.saveApiLog(transactionId, Constants.LOCDIV, Long.valueOf(0), Constants.VALIDATION_FAILED,
						Constants.SKIPPED, "Id parameter not found in request", apiDtoObj.getMsgId());
				logger.debug("Error due to Insufficient data: Id parameter not found in request for LocationDivision_id: "+requestApiDataDto.getId().trim());
				return false;
				
			} else if (requestApiDataDto.getId().trim().equals("") || requestApiDataDto.getId().trim().equals("0") || requestApiDataDto.getId().trim().equalsIgnoreCase("null")) {
				apiLoggerService.saveApiLog(transactionId, Constants.LOCDIV, Long.valueOf(0), Constants.VALIDATION_FAILED,
						Constants.SKIPPED, "Error due to Insufficient data: Id value not found in request for LocationDivision_id: "+requestApiDataDto.getId().trim(), apiDtoObj.getMsgId());
				logger.debug("Error due to Insufficient data: Id value not found in request for LocationDivision_id: "+requestApiDataDto.getId().trim());
				return false;
				
			} else if (requestApiDataDto.getName() == null) {
				apiLoggerService.saveApiLog(transactionId, Constants.LOCDIV, Long.valueOf(requestApiDataDto.getId().trim()),
						Constants.VALIDATION_FAILED, Constants.SKIPPED, "Error due to Insufficient data: Name parameter not found in request for LocationDivision_id: "+requestApiDataDto.getId(),
						apiDtoObj.getMsgId());
				logger.debug("Error due to Insufficient data: Name parameter not found in request for LocationDivision_id: "+requestApiDataDto.getId().trim());
				return false;
				
			} else if (requestApiDataDto.getName().trim().equals("") || requestApiDataDto.getName().trim().equalsIgnoreCase("null")) {
				apiLoggerService.saveApiLog(transactionId, Constants.LOCDIV, Long.valueOf(requestApiDataDto.getId().trim()),
						Constants.VALIDATION_FAILED, Constants.SKIPPED, "Error due to Insufficient data: Name value not found in request for LocationDivision_id: "+requestApiDataDto.getId(),
						apiDtoObj.getMsgId());
				logger.debug("Error due to Insufficient data: Name value not found in request for LocationDivision_id: "+requestApiDataDto.getId().trim());
				return false;
				
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	/**
	 * Get Active LocationDivision List By Searching Value With Fix row
	 * 
	 * @param searchStr
	 * @param pageNumber
	 * @return
	 */
	public Page<LocationDivision> searchActiveTrue(String searchStr, int pageNumber) {
		Pageable pageable = PageRequest.of(pageNumber, Constants.PAGE_SIZE, DEFAULT_SORT);
		return locationDivisionRepo.findBySearchActiveTrueLocDiv(searchStr, pageable);

	}
	
	/**
	 * Get deleted LocationDivision List By Searching Value With Fix row
	 * 
	 * @param searchStr
	 * @param pageNumber
	 * @return
	 */
	public Page<LocationDivision> searchActiveFalse(String searchStr, int pageNumber) {
		Pageable pageable = PageRequest.of(pageNumber, Constants.PAGE_SIZE, DEFAULT_SORT);
		return locationDivisionRepo.findBySearchActiveFalseLocDiv(searchStr, pageable);

	}
	
	/**
	 * Get All deleted locDiv list With Fix Size Row
	 * 
	 * @param pageNumber
	 * @return
	 */
	public Page<LocationDivision> getAllDeletedLocDiv(int pageNumber) {
		Pageable pageable = PageRequest.of(pageNumber, Constants.PAGE_SIZE, DEFAULT_SORT);
		return locationDivisionRepo.findAllByIsActiveFalseAndIsDeleteTrue(pageable);
	}
}
