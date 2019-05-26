package com.ackermans.criticalpath.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
import com.ackermans.criticalpath.entity.Brand;
import com.ackermans.criticalpath.entity.CloseDate;
import com.ackermans.criticalpath.entity.Country;
import com.ackermans.criticalpath.entity.LocationDivision;
import com.ackermans.criticalpath.entity.Province;
import com.ackermans.criticalpath.entity.StockTakeCycle;
import com.ackermans.criticalpath.entity.Store;
import com.ackermans.criticalpath.entity.User;
import com.ackermans.criticalpath.entity.UserLogin;
import com.ackermans.criticalpath.enums.ErrorStatus;
import com.ackermans.criticalpath.enums.UserRole;
import com.ackermans.criticalpath.repository.BrandRepository;
import com.ackermans.criticalpath.repository.CountryRepository;
import com.ackermans.criticalpath.repository.LocationDivisionRepository;
import com.ackermans.criticalpath.repository.ProvinceRepository;
import com.ackermans.criticalpath.repository.StockTakeCycleRepository;
import com.ackermans.criticalpath.repository.StoreCloseDateRepository;
import com.ackermans.criticalpath.repository.StoreRepository;
import com.ackermans.criticalpath.repository.UserLoginRepository;
import com.ackermans.criticalpath.repository.UserRepository;
import com.ackermans.criticalpath.utils.Constants;
import com.api.criticalpath.dto.RequestApiDataDto;
import com.api.criticalpath.dto.RequestApiDto;

@Service
public class StoreService {

	private static final Logger logger = LogManager.getFormatterLogger(StoreService.class);

	@Autowired
	private StoreRepository storeRepository;

	@Autowired
	private StoreCloseDateRepository storeCloseDateRepository;

	@Autowired
	private StockTakeCycleRepository stockTakeCycleRepository;

	@Autowired
	private CountryRepository countryRepository;

	@Autowired
	private ApiLoggerService apiLoggerService;

	@Autowired
	private BrandRepository brandRepository;

	@Autowired
	private LocationDivisionRepository locationDivisionRepository;

	@Autowired
	private ProvinceRepository provinceRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserLoginRepository userLoginRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;

	private final Sort DEFAULT_SORT = Sort.by("code");

	/**
	 * Verify and Save Store
	 * 
	 * @param store
	 * @return
	 * @throws CPException
	 */
	public Store verifyAndSave(Store store) throws CPException {

		boolean isExist = false;

		// If there is id that means record needs to be updated
		// hence we should ignore that record while checking for existence
		if (store.getId() != null && store.getId() > 0) {
			isExist = this.isExists(store.getCode(), store.getId());
		} else {
			isExist = this.isExists(store.getCode());
		}

		// If record is unique then save it else throw exception
		if (!isExist)
			this.save(store);
		else
			throw new CPException(ErrorStatus.DUPLICATE_RECORD);

		return store;
	}

	/**
	 * Save Or Update Store
	 * 
	 * @param store
	 * @return
	 */
	private Store save(Store store) {
		return storeRepository.save(store);
	}

	/**
	 * Check if Store exists for given code
	 * 
	 * @param code
	 * @return
	 */
	private boolean isExists(String code) {
		return storeRepository.countByCodeIgnoreCase(code) > 0;
	}

	/**
	 * Check if Store exists for given code but ignore given id
	 * 
	 * @param code
	 * @return
	 */
	private boolean isExists(String code, Long Id) {
		return storeRepository.countByCodeAndId(code, Id) > 0;
	}

	/**
	 * Get all Store
	 * 
	 * @return
	 */
	public List<Store> getAll() {
		return storeRepository.findAll();
	}

	/**
	 * Get List By PageNumber and PageSize
	 * 
	 * @param pageNumber
	 * @return
	 */
	public Page<Store> getAll(int pageNumber) {
		Pageable pageable = PageRequest.of(pageNumber, Constants.PAGE_SIZE, DEFAULT_SORT);
		return storeRepository.findAll(pageable);
	}

	/**
	 * Get All Active Store List
	 * 
	 * @return
	 */
	public List<Store> getAllActive() {
		return storeRepository.findAllByIsActiveTrue();
	}

	/**
	 * Get All Active With Fix Size Row
	 * 
	 * @param pageNumber
	 * @return
	 */
	public Page<Store> getAllActive(int pageNumber) {
		Pageable pageable = PageRequest.of(pageNumber, Constants.PAGE_SIZE, DEFAULT_SORT);
		return storeRepository.findAllByIsActiveTrue(pageable);
	}

	/**
	 * Get All deleted store list With Fix Size Row
	 * 
	 * @param pageNumber
	 * @return
	 */
	public Page<Store> getAllDeletedStore(int pageNumber) {
		Pageable pageable = PageRequest.of(pageNumber, Constants.PAGE_SIZE, DEFAULT_SORT);
		return storeRepository.findAllByIsActiveFalseAndIsDeleteTrue(pageable);
	}

	/**
	 * Activate Store by id
	 * 
	 * @param id
	 * @return
	 */
	public Store activate(Long id) {
		Store store = storeRepository.getOne(id);
		store.setIsActive(true);
		return storeRepository.save(store);
	}

	/**
	 * Deactivate Store by id
	 * 
	 * @param id
	 * @return
	 */
	public Store deactivate(Long id) {
		Store store = storeRepository.getOne(id);
		store.setIsActive(false);
		return storeRepository.save(store);
	}

	/**
	 * Search store whose name starts with given search keyword
	 * 
	 * @return
	 */
	public List<Store> search(String searchKeyword) {
		return storeRepository.findByNameIgnoreCaseStartingWith(searchKeyword);
	}

	/**
	 * Get Store List By Searching Value With Fix row
	 * 
	 * @param searchStr
	 * @param pageNumber
	 * @return
	 */
	public Page<Store> searchActive(String searchStr, int pageNumber) {
		Pageable pageable = PageRequest.of(pageNumber, Constants.PAGE_SIZE, DEFAULT_SORT);
		return storeRepository.findBySearchActiveStore(searchStr, pageable);

	}

	/**
	 * Get Active Store List By Searching Value With Fix row
	 * 
	 * @param searchStr
	 * @param pageNumber
	 * @return
	 */
	public Page<Store> searchActiveTrue(String searchStr, int pageNumber) {
		Pageable pageable = PageRequest.of(pageNumber, Constants.PAGE_SIZE, DEFAULT_SORT);
		return storeRepository.findBySearchActiveTrueStore(searchStr, pageable);

	}

	/**
	 * Get deleted Store List By Searching Value With Fix row
	 * 
	 * @param searchStr
	 * @param pageNumber
	 * @return
	 */
	public Page<Store> searchActiveFalse(String searchStr, int pageNumber) {
		Pageable pageable = PageRequest.of(pageNumber, Constants.PAGE_SIZE, DEFAULT_SORT);
		return storeRepository.findBySearchActiveFalseStore(searchStr, pageable);

	}

	/**
	 * Get Store By Code
	 * 
	 * @param code
	 * @return
	 */
	public Store getByCode(String code) {
		return storeRepository.findByCode(code);
	}

	/**
	 * Get Store List By Country ID
	 * 
	 * @param countryId
	 * @return
	 */
	public List<Store> getByCountry(Long countryId) {
		return storeRepository.findByCountry_Id(countryId);
	}

	/**
	 * Get Store List By Brand ID
	 * 
	 * @param brandId
	 * @return
	 */
	public List<Store> getByBrand(Long brandId) {
		return storeRepository.findByBrand_IdAndIsActiveTrue(brandId);
	}

	/**
	 * Get Store by Store ID
	 * 
	 * @param storeId
	 * @return
	 */
	public Optional<Store> get(Long storeId) {
		return storeRepository.findById(storeId);
	}

	/**
	 * Get all store
	 * 
	 * @param storeIds
	 * @return
	 */
	public Set<Store> get(Set<Store> storeIds) {
		return storeRepository.findAll().stream().collect(Collectors.toSet());
	}

	/**
	 * Deletes the record
	 * 
	 * @param storeId
	 * @return
	 * @throws CPException
	 */
	public void deleteStore(Long storeId) throws CPException {

		// Check if there any active store for that brand
		List<CloseDate> closeDateStore = (List<CloseDate>) storeCloseDateRepository
				.findByStores_IdAndIsActiveTrue(storeId);
		Store storeCheck = storeRepository.getOne(storeId);
		if (!closeDateStore.isEmpty())
			throw new CPException(ErrorStatus.CLOSE_DATE_EXISTS);
		if (storeCheck.getStockTakeCycle() != null)
			throw new CPException(ErrorStatus.STOCK_TAKE_CYCLE_EXISTS);

		Store store = storeRepository.getOne(storeId);
		store.setIsActive(false);
		store.setIsDelete(true);
		storeRepository.save(store);
		return;
	}

	/**
	 * Restores the record
	 * 
	 * @param storeId
	 */
	public void restoreStore(Long storeId) {
		Store store = storeRepository.getOne(storeId);
		store.setIsActive(true);
		store.setIsDelete(false);
		storeRepository.save(store);
		return;
	}

	/**
	 * Get active Store list By country iD
	 * 
	 * @param countryId
	 * @return
	 */
	public List<Store> findActiveStoreByCountryId(Long countryId) {
		return storeRepository.findByCountry_IdAndIsActiveTrue(countryId);
	}

	@Async
	public void saveImportedStore(RequestApiDto apiDtoObj, Long transactionId) {

		if (apiDtoObj.getData().size() > 0) {
			for (int i = 0; i < apiDtoObj.getData().size(); i++) {
				if (validateStore(transactionId, apiDtoObj.getData().get(i), apiDtoObj)) {
					Store storeOb = storeRepository.findByThirdPartyId(Long.parseLong(apiDtoObj.getData().get(i).getId()));

					if (storeOb != null) {

						if (!storeOb.getName().trim().equalsIgnoreCase(apiDtoObj.getData().get(i).getName())) {
							storeOb.setName(apiDtoObj.getData().get(i).getName());
						}

						if (setCodeBrandLocDivCouProUs(apiDtoObj, i, storeOb, transactionId)) {
							try {
								storeRepository.save(storeOb);
								apiLoggerService.saveApiLog(transactionId, Constants.STORE,
										Long.parseLong(apiDtoObj.getData().get(i).getId()), Constants.UPDATED,
										Constants.BYTHIRDPARTYID, Constants.SUCCESSUPD, apiDtoObj.getMsgId());
							} catch (Exception e) {
								apiLoggerService.saveApiLog(transactionId, Constants.STORE,
										Long.parseLong(apiDtoObj.getData().get(i).getId()), Constants.FAILED,
										Constants.BYTHIRDPARTYID, e.getMessage(), apiDtoObj.getMsgId());
							}

						}
					} else {

						Store storeObWithName = storeRepository
								.findByNameIgnoreCaseAndIsActiveTrue(apiDtoObj.getData().get(i).getName());

						if (storeObWithName != null) {
							storeObWithName.setThirdPartyId(Long.parseLong(apiDtoObj.getData().get(i).getId()));
							if (setCodeBrandLocDivCouProUs(apiDtoObj, i, storeObWithName, transactionId)) {
								try {
									storeRepository.save(storeObWithName);
									apiLoggerService.saveApiLog(transactionId, Constants.STORE,
											Long.parseLong(apiDtoObj.getData().get(i).getId()), Constants.UPDATED,
											Constants.BYNAME, Constants.SUCCESSUPD, apiDtoObj.getMsgId());
								} catch (Exception e) {
									apiLoggerService.saveApiLog(transactionId, Constants.STORE,
											Long.parseLong(apiDtoObj.getData().get(i).getId()), Constants.FAILED,
											Constants.BYNAME, e.getMessage(), apiDtoObj.getMsgId());
								}

							} else {
								apiLoggerService.saveApiLog(transactionId, Constants.STORE,
										Long.parseLong(apiDtoObj.getData().get(i).getId()), Constants.NOTFOUND,
										Constants.BYNAME, Constants.SKIPPED, apiDtoObj.getMsgId());
							}
						} else {
							Store stOb = new Store();
							stOb.setThirdPartyId(Long.parseLong(apiDtoObj.getData().get(i).getId()));
							stOb.setName(apiDtoObj.getData().get(i).getName());
							if (setCodeBrandLocDivCouProUs(apiDtoObj, i, stOb, transactionId)) {
								try {
									storeRepository.save(stOb);
									apiLoggerService.saveApiLog(transactionId, Constants.STORE,
											Long.parseLong(apiDtoObj.getData().get(i).getId()), Constants.INSERTED,
											Constants.BYTHIRDPARTYID, Constants.SUCCESSINS, apiDtoObj.getMsgId());
								} catch (Exception e) {
									apiLoggerService.saveApiLog(transactionId, Constants.STORE,
											Long.parseLong(apiDtoObj.getData().get(i).getId()), Constants.FAILED,
											Constants.BYTHIRDPARTYID, e.getMessage(), apiDtoObj.getMsgId());
								}

							} else {
								apiLoggerService.saveApiLog(transactionId, Constants.STORE,
										Long.parseLong(apiDtoObj.getData().get(i).getId()), Constants.NOTFOUND,
										Constants.BYTHIRDPARTYID, Constants.SKIPPED, apiDtoObj.getMsgId());
							}
						}
					}
				}
			}
		} else
			apiLoggerService.saveApiLog(transactionId, Constants.STORE, Long.valueOf(0), Constants.NOTFOUND,
					Constants.SKIPPED, "Data parameter is empty in request", apiDtoObj.getMsgId());

	}

	private boolean setCodeBrandLocDivCouProUs(RequestApiDto apiDtoObj, int i, Store storeOb, Long transactionId) {

		boolean checkFlag = true;

		try {
			storeOb.setCode(apiDtoObj.getData().get(i).getCode().trim());

			setBrandObForStore(apiDtoObj, i, storeOb, transactionId);

			setLocDivCouProForStore(apiDtoObj, i, storeOb, transactionId);
			
			settingRamUserForLocDiv(apiDtoObj, i, storeOb, transactionId);

			storeOb.setIsActive(true);
			storeOb.setIsDelete(false);

		} catch (Exception e) {
			checkFlag = false;
		}

		return checkFlag;
	}
	
	private void settingRamUserForLocDiv(RequestApiDto apiDtoObj, int i, Store storeOb, Long transactionId) {

		try {
			if (validatingRamUserId(apiDtoObj, i)) {

				List<User> ramUserObList = userRepository.findByThirdPartyId(Long.parseLong(apiDtoObj.getData().get(i).getRam_id().trim()));
				UserRole role = null;
				for (UserRole userRole : UserRole.values()) {
					if (userRole.name().equals("ROLE_RAM_USER")) {
						role = userRole;
					}
				}
				User ramUserOb = userRepository.getUserByThirdPartyId(Long.parseLong(apiDtoObj.getData().get(i).getRam_id().trim()), "ROLE_RAM_USER");

				if (ramUserOb != null) {

					if (ramUserOb.getUserLogin() != null) {
						if (userLoginRepository.findRoleByUserId(ramUserOb.getId()).equals("ROLE_RAM_USER")) {
							storeOb.setRegionalManagers(ramUserOb);
						} else {
							User userDummyObj = setDummyRamUserForLocDiv(apiDtoObj.getData().get(i).getRam_id().trim());
							storeOb.setRegionalManagers(userDummyObj);
							apiLoggerService.saveApiLog(transactionId, Constants.STORE,
									Long.parseLong(apiDtoObj.getData().get(i).getId().trim()), Constants.NOTFOUND,
									Constants.BYTHIRDPARTYID,
									"Given User is not RAM User, So new user created with Store_id: "
											+ apiDtoObj.getData().get(i).getId().trim(),
									apiDtoObj.getMsgId());
							logger.debug("Given User is not RAM User, So new user created with Store_id: "
									+ apiDtoObj.getData().get(i).getId().trim());
						}
					} else {

						try {

							UserLogin userLoginDummyObj = new UserLogin();
							UserRole roleNewForRam = null;
							for (UserRole userRole : UserRole.values()) {
								if (userRole.name().equals("ROLE_RAM_USER")) {
									roleNewForRam = userRole;
								}
							}
							userLoginDummyObj.setRole(roleNewForRam);
							userLoginDummyObj
									.setEmail("ram" + apiDtoObj.getData().get(i).getRam_id().trim() + "@cp.com");
							userLoginDummyObj.setIsActive(false);
							userLoginDummyObj.setIsDelete(true);
							userLoginDummyObj.setPassword(passwordEncoder.encode(Constants.DEFAULTPASSWORD));
							userLoginDummyObj = userLoginRepository.save(userLoginDummyObj);

							ramUserOb.setUserLogin(userLoginDummyObj);

							storeOb.setRegionalManagers(ramUserOb);

						} catch (Exception e) {
							storeOb.setRegionalManagers(ramUserOb);
							logger.debug("Error while creating new Ram UserLogin for Store_id: "
									+ apiDtoObj.getData().get(i).getId().trim() + " : " + e.getMessage());
						}

					}

				} else {
					User userDummyObjNew = setDummyRamUserForLocDiv(apiDtoObj.getData().get(i).getRam_id().trim());
					storeOb.setRegionalManagers(userDummyObjNew);
					logger.debug("User not available for Store_id: " + apiDtoObj.getData().get(i).getId().trim());
				}

			} else {
				storeOb.setRegionalManagers(null);
				logger.debug("Error while validation of RAM value for Store_id: "+ apiDtoObj.getData().get(i).getId().trim());
			}
		} catch (Exception e) {
			storeOb.setRegionalManagers(null);
			logger.debug("Error while fetching Ram data for Store_id: "
					+ apiDtoObj.getData().get(i).getId().trim() + " :  " + e.getMessage());
		}
	}
	
	private boolean validatingRamUserId(RequestApiDto apiDtoObj, int i) {

		boolean checkFlag = apiDtoObj.getData().get(i).getRam_id() != null
				&& !apiDtoObj.getData().get(i).getRam_id().trim().isEmpty()
				&& !apiDtoObj.getData().get(i).getRam_id().trim().equalsIgnoreCase("0")
				&& !apiDtoObj.getData().get(i).getRam_id().trim().equalsIgnoreCase("null");

		return checkFlag;
	}
	
	private User setDummyRamUserForLocDiv(String thirdPartyId) {
		User userStObj = new User();
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
			
			userStObj.setThirdPartyId(Long.parseLong(thirdPartyId));
			userStObj.setIsActive(false);
			userStObj.setIsDelete(true);
			userStObj.setPhone("0000000000");
			userStObj.setUserLogin(userLoginDummyObj);
			userStObj.setName("NA-" + thirdPartyId);
			userStObj = userRepository.save(userStObj);

		} catch (Exception e) {
			userStObj = null;
			logger.debug("Error in setDummyRamUserForLocDiv for Store with Store_id: " +userStObj.getThirdPartyId()+" : "+ e.getMessage());
		}
		return userStObj;
	}




	/**
	 * set location_division_id for store request API
	 * @param apiDtoObj
	 * @param i
	 * @param storeOb
	 * @param transactionId
	 */
	private void setLocDivCouProForStore(RequestApiDto apiDtoObj, int i, Store storeOb, Long transactionId) {

		try {

			if (validateLocDivId(apiDtoObj, i)) {

				LocationDivision locDivObj = locationDivisionRepository.findByThirdPartyId(
						Long.parseLong(apiDtoObj.getData().get(i).getLocation_division_id().trim()));

				if (locDivObj != null) {
					
					storeOb.setLocationDivision(locDivObj);

					setCouPrForLocDiv(storeOb, locDivObj);
					
				} else {

					try {
						LocationDivision newDummyLocDivOb = new LocationDivision();

						newDummyLocDivOb.setThirdPartyId(Long.parseLong(apiDtoObj.getData().get(i).getLocation_division_id().trim()));
						newDummyLocDivOb.setName("NA-" + Long.parseLong(apiDtoObj.getData().get(i).getLocation_division_id().trim()));
						newDummyLocDivOb.setIsActive(false);
						newDummyLocDivOb.setIsDelete(true);

						newDummyLocDivOb = locationDivisionRepository.save(newDummyLocDivOb);

						storeOb.setLocationDivision(newDummyLocDivOb);
						storeOb.setCountry(null);
						storeOb.setProvince(null);
						logger.debug("New LocDiv object created in setLocDivCouProForStore for Store_id: "+apiDtoObj.getData().get(i).getId().trim());
						
					} catch (Exception e) {

						storeOb.setLocationDivision(null);
						storeOb.setCountry(null);
						storeOb.setProvince(null);
						logger.debug("Error in setLocDivCouProForStore while create dummyLocDivOb with Store_id: "+apiDtoObj.getData().get(i).getId().trim()+" : "+ e.getMessage());
					}

				}
			} else {

				storeOb.setLocationDivision(null);
				storeOb.setCountry(null);
				storeOb.setProvince(null);
				logger.debug("Error in setLocDivCouProForStore while get locDivision_id get null or not get field locDivision_id with Store_id: "+apiDtoObj.getData().get(i).getId().trim());

			}
		} catch (Exception e) {
			
			storeOb.setLocationDivision(null);
			storeOb.setCountry(null);
			storeOb.setProvince(null);
			logger.debug("Error in setLocDivCouProForStore while get Data of locationDivision_id for store_id: "+apiDtoObj.getData().get(i).getId().trim()+" : " + e.getMessage());
		}
	}

	private void setCouPrForLocDiv(Store storeOb, LocationDivision locDivObj) {

		try {
			if (locDivObj.getCountry() != null) {
				
				if (locDivObj.getCountry().getCode().trim() != null) {
					
					String ctrCode = locDivObj.getCountry().getCode();
					
					if (ctrCode != null) {
						Country ctrOb = countryRepository.findByCodeIgnoreCase(ctrCode.trim());
						
						if (ctrOb != null) {
							storeOb.setCountry(ctrOb);
						} else {
							storeOb.setCountry(null);
							logger.debug("Error in setCouPrForLocDiv while get country code for locDiv of store Request with Store_id: "+storeOb.getThirdPartyId());
						}
							
					} else {
						storeOb.setCountry(null);
						logger.debug("Error in setCouPrForLocDiv while get country code is null for locDiv of store Request with Store_id: "+storeOb.getThirdPartyId());
					}
						
				} else {
					storeOb.setCountry(null);
					logger.debug("Error in setCouPrForLocDiv while get country code for locDiv of store Request with Store_id: "+storeOb.getThirdPartyId());
				}
			} else { 
				storeOb.setCountry(null);
				logger.debug("Error in setCouPrForLocDiv while get country is null for locDiv of store Request with Store_id: " +storeOb.getThirdPartyId());
			}
		} catch (Exception e) {
			storeOb.setCountry(null);
			logger.debug("Error in setCouPrForLocDiv while get data of country for locDiv of store request with store_id: "+storeOb.getThirdPartyId()+" "+ e.getMessage());
		}
		
		try {
			if (locDivObj.getProvince() != null) {
				if (locDivObj.getProvince().getName() != null) {
					String prName = locDivObj.getProvince().getName().toString().trim();
					if (prName != null) {
						Province proOb = provinceRepository.findByNameIgnoreCase(prName);
						
						if (proOb != null) {
							storeOb.setProvince(proOb);
						} else {
							storeOb.setProvince(null);
							logger.debug("Error in setCouPrForLocDiv while get province obj with province name for locDiv of store Request with Store_id: "+storeOb.getThirdPartyId());
						}
					} else {
						storeOb.setProvince(null);
						logger.debug("Error in setCouPrForLocDiv while get province name is null for locDiv of store Request with Store_id: "+storeOb.getThirdPartyId());
					}
				} else {
					storeOb.setProvince(null);
					logger.debug("Error in setCouPrForLocDiv while get province name for locDiv of store Request with Store_id: "+storeOb.getThirdPartyId());
				}
			} else {
				storeOb.setProvince(null);
				logger.debug("Error in setCouPrForLocDiv while get province for locDiv of store Request with Store_id: "+storeOb.getThirdPartyId());
			}
		} catch (Exception e) {
			storeOb.setProvince(null);
			logger.debug("Error in setCouPrForLocDiv while get data of province for locDiv of store request with Store_id: "+storeOb.getThirdPartyId()+" : "+e.getMessage());
		}
	}

	/**
	 * validate locationDivision_id for store request API
	 * 
	 * @param apiDtoObj
	 * @param i
	 * @return
	 */
	private boolean validateLocDivId(RequestApiDto apiDtoObj, int i) {
		boolean validateFlag = false;

		validateFlag = apiDtoObj.getData().get(i).getLocation_division_id() != null
				&& (!apiDtoObj.getData().get(i).getLocation_division_id().trim().equalsIgnoreCase("null"))
				&& (!apiDtoObj.getData().get(i).getLocation_division_id().trim().equalsIgnoreCase("0"))
				&& (!apiDtoObj.getData().get(i).getLocation_division_id().trim().equalsIgnoreCase(""));

		return validateFlag;
	}

	/**
	 * set  brand_id for store request
	 * 
	 * @param apiDtoObj
	 * @param i
	 * @param storeOb
	 * @param transactionId
	 */
	private void setBrandObForStore(RequestApiDto apiDtoObj, int i, Store storeOb, Long transactionId) {
	
		try {
			
			if (validateBrandId(apiDtoObj, i)) {
				Brand brandObj = brandRepository.findByThirdPartyId(Long.parseLong(apiDtoObj.getData().get(i).getBrand_id().trim()));
				if (brandObj != null) {
					storeOb.setBrand(brandObj);
				} else {
					try {
						
						Brand newDummyBrandOb = new Brand();
						newDummyBrandOb.setThirdPartyId(Long.parseLong(apiDtoObj.getData().get(i).getBrand_id().trim()));
						newDummyBrandOb.setName("NA-" + Long.parseLong(apiDtoObj.getData().get(i).getBrand_id().trim()));
						newDummyBrandOb.setIsActive(false);
						newDummyBrandOb.setIsDelete(true);
						newDummyBrandOb = brandRepository.save(newDummyBrandOb);
						storeOb.setBrand(newDummyBrandOb);
						
					} catch (Exception e) {
						storeOb.setBrand(null);
						logger.debug("Error in setBrandObForStore while create Dummy obj for Brand with store_id: "+apiDtoObj.getData().get(i).getId().trim()+" : " +e.getMessage());
					}
				}
			} else {
				storeOb.setBrand(null);
				logger.debug("Error in setBrandObForStore while validation for Brand_id with store_id: "+apiDtoObj.getData().get(i).getId().trim());
			}
		} catch (Exception e) {
			storeOb.setBrand(null);
			logger.debug("Error in setBrandObForStore while not get data of Brand_id for Brand with store_id: "+apiDtoObj.getData().get(i).getId().trim()+" : " +e.getMessage());
		}
	}

	/**
	 * validate request for brand_id
	 * 
	 * @param apiDtoObj
	 * @param i
	 * @return
	 */
	private boolean validateBrandId(RequestApiDto apiDtoObj, int i) {
		boolean validateFlag = false;

		validateFlag = apiDtoObj.getData().get(i).getBrand_id() != null
				&& (!apiDtoObj.getData().get(i).getBrand_id().trim().equalsIgnoreCase("null"))
				&& (!apiDtoObj.getData().get(i).getBrand_id().trim().equalsIgnoreCase("0"))
				&& (!apiDtoObj.getData().get(i).getBrand_id().trim().equalsIgnoreCase(""));

		return validateFlag;
	}

	/**
	 * Validates the request
	 * 
	 * @param transactionId
	 * @param requestApiDataDto
	 * @return
	 */
	private Boolean validateStore(Long transactionId, RequestApiDataDto requestApiDataDto, RequestApiDto apiDtoObj) {
		
		try {
			if (requestApiDataDto.getId() == null) {
				apiLoggerService.saveApiLog(transactionId, Constants.STORE, Long.valueOf(0), Constants.VALIDATION_FAILED,
						Constants.SKIPPED, "Id parameter not found in request", apiDtoObj.getMsgId());
				logger.debug("Error due to Insufficient data: Id parameter not found in request for Store_id: "+requestApiDataDto.getId().trim());
				return false;
				
			} else if (requestApiDataDto.getId().trim().equals("") || requestApiDataDto.getId().trim().equals("0") || requestApiDataDto.getId().trim().equalsIgnoreCase("null")) {
				apiLoggerService.saveApiLog(transactionId, Constants.STORE, Long.valueOf(0), Constants.VALIDATION_FAILED,
						Constants.SKIPPED, "Id value not found in request", apiDtoObj.getMsgId());
				logger.debug("Error due to Insufficient data: Id value not found in request for Store_id: "+requestApiDataDto.getId().trim());
				return false;
				
			} else if (requestApiDataDto.getName() == null) {
				apiLoggerService.saveApiLog(transactionId, Constants.STORE, Long.valueOf(requestApiDataDto.getId().trim()),
						Constants.VALIDATION_FAILED, Constants.SKIPPED, "Name parameter not found in request",
						apiDtoObj.getMsgId());
				logger.debug("Error due to Insufficient data: Name Parameter not found in request for Store_id: "+requestApiDataDto.getId().trim());
				return false;
				
			} else if (requestApiDataDto.getName().trim().equals("") || requestApiDataDto.getName().trim().equalsIgnoreCase("null")) {
				apiLoggerService.saveApiLog(transactionId, Constants.STORE, Long.valueOf(requestApiDataDto.getId().trim()),
						Constants.VALIDATION_FAILED, Constants.SKIPPED, "Name value not found in request",
						apiDtoObj.getMsgId());
				logger.debug("Error due to Insufficient data: Name value not found in request for Store_id: "+requestApiDataDto.getId().trim());
				return false;
			
				
			} else if (requestApiDataDto.getCode() == null) {
				apiLoggerService.saveApiLog(transactionId, Constants.STORE, Long.valueOf(requestApiDataDto.getId().trim()),
						Constants.VALIDATION_FAILED, Constants.SKIPPED, "Code parameter not found in request",
						apiDtoObj.getMsgId());
				logger.debug("Error due to Insufficient data: Code parameter not found in request for Store_id: "+requestApiDataDto.getId().trim());
				return false;
				
				
			} else if (requestApiDataDto.getCode().trim().equals("") || requestApiDataDto.getCode().trim().equalsIgnoreCase("null")) {
				apiLoggerService.saveApiLog(transactionId, Constants.STORE, Long.valueOf(requestApiDataDto.getId().trim()),
						Constants.VALIDATION_FAILED, Constants.SKIPPED, "Code value not found in request",
						apiDtoObj.getMsgId());
				logger.debug("Error due to Insufficient data: Code value not found in request for Store_id: "+requestApiDataDto.getId().trim());
				return false;
				
			}
		} catch (Exception e) {
			return false;
		}
		
		return true;
	}
}
