package com.ackermans.criticalpath.service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ackermans.criticalpath.admin.dto.UserRequest;
import com.ackermans.criticalpath.aop.service.AuditService;
import com.ackermans.criticalpath.common.dto.CPException;
import com.ackermans.criticalpath.entity.Brand;
import com.ackermans.criticalpath.entity.Store;
import com.ackermans.criticalpath.entity.User;
import com.ackermans.criticalpath.entity.UserLogin;
import com.ackermans.criticalpath.enums.ErrorStatus;
import com.ackermans.criticalpath.enums.UserRole;
import com.ackermans.criticalpath.repository.BrandRepository;
import com.ackermans.criticalpath.repository.StoreRepository;
import com.ackermans.criticalpath.repository.UserLoginRepository;
import com.ackermans.criticalpath.repository.UserRepository;
import com.ackermans.criticalpath.utils.Constants;
import com.api.criticalpath.dto.RequestApiDataDto;
import com.api.criticalpath.dto.RequestApiDto;

@Service
public class UserService {

	private static final Logger logger = LogManager.getFormatterLogger(UserService.class);
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	UserLoginService userLoginService;
	
	@Autowired
	UserLoginRepository userLoginRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	BrandRepository brandRepository;
	
	@Autowired
	StoreRepository storeRepository;
	
	@Autowired
	ApiLoggerService apiLoggerService;
	
	@Autowired
	AuditService auditService;
	
	/**
	 * Sort by name in ascending order
	 */
	private final Sort DEFAULT_SORT = Sort.by("name");
	
	
	/**
	 * Get all brands of a user
	 * 
	 * @return
	 */
	public Page<User> getAllByBrand(Long brandId, int pageNumber) {
		Pageable pageable = PageRequest.of(pageNumber, Constants.PAGE_SIZE, DEFAULT_SORT);
		return (Page<User>) userRepository.findAllBrandBybrand_Id(brandId, pageable);
	}
	
	/**
	 * Get all active user of brands
	 * 
	 * @return
	 */
	public Page<User> getAllActiveByBrand(Long brandId, int pageNumber, String searchRole) {
		Pageable pageable = PageRequest.of(pageNumber, Constants.PAGE_SIZE, DEFAULT_SORT);
		List<User> uList = new ArrayList<User>();
		
		UserRole SearchUserRole = null;
		if(!(searchRole.toString()).equalsIgnoreCase("All")) {
			for (UserRole userRole : UserRole.values()) { 
				if (userRole.name().equals(searchRole)) { 
					SearchUserRole=  userRole;
				} 
			}
		}
	
		
		if((searchRole.toString()).equalsIgnoreCase("All")){
			 uList = userRepository.findAllActiveBrandBybrand_IdAndIsActiveTrue(brandId,pageable).getContent();
		}else {
			 uList = userRepository.findAllActiveBrandBybrand_IdAndIsActiveTrueAndUserLogin_Role(brandId,pageable, SearchUserRole).getContent();
		}
		
		for(User user : uList) {
			if(user!=null)	{
				Hibernate.initialize(user.getUserLogin());
			}
		}
		if((searchRole.toString()).equalsIgnoreCase("All")){
			return (Page<User>) userRepository.findAllActiveBrandBybrand_IdAndIsActiveTrue(brandId, pageable);
		}else {
			return (Page<User>) userRepository.findAllActiveBrandBybrand_IdAndIsActiveTrueAndUserLogin_Role(brandId, pageable, SearchUserRole);
		}
//		return (Page<User>) userRepository.findAllActiveBrandBybrand_IdAndIsActiveTrue(brandId, pageable);
	}
	
	/**
	 * Get all deleted user of brands
	 * 
	 * @return
	 */
	public Page<User> getAllDeletedUserByBrand(Long brandId, int pageNumber, String searchRole) {
		Pageable pageable = PageRequest.of(pageNumber, Constants.PAGE_SIZE, DEFAULT_SORT);
		List<User> uList = new ArrayList<User>();
		 
		
		UserRole SearchUserRole = null;
		if(!(searchRole.toString()).equalsIgnoreCase("All")) {
			for (UserRole userRole : UserRole.values()) { 
				if (userRole.name().equals(searchRole)) { 
					SearchUserRole=  userRole;
				} 
			}
		}
		
		if((searchRole.toString()).equalsIgnoreCase("All")){
			 uList = userRepository.findAllActiveBrandBybrand_IdAndIsActiveFalseAndIsDeleteTrue(brandId,pageable).getContent();
		}else {
			 uList = userRepository.findAllActiveBrandBybrand_IdAndIsActiveFalseAndIsDeleteTrueAndUserLogin_Role(brandId,pageable, SearchUserRole).getContent();
		}
		
		for(User user : uList) {
			if(user!=null)	{
				Hibernate.initialize(user.getUserLogin());
			}
		}
		
		if((searchRole.toString()).equalsIgnoreCase("All")){
			return (Page<User>) userRepository.findAllActiveBrandBybrand_IdAndIsActiveFalseAndIsDeleteTrue(brandId, pageable);
		}else {
			return (Page<User>) userRepository.findAllActiveBrandBybrand_IdAndIsActiveFalseAndIsDeleteTrueAndUserLogin_Role(brandId, pageable, SearchUserRole);
		}
		
	}
	
	/**
	 * Save or Update
	 * 
	 * @param userRequest
	 * @return
	 * @throws CPException
	 */
	public User verifyAndSave(UserRequest userRequest) throws CPException {
		UserLogin storedUserLoginObj;
		boolean isExist = false;
		User user;
		//If there is id that means record needs to be updated
		//hence we should ignore that record while checking for existence
		if(userRequest.getUserLogin().getId() != null && userRequest.getUserLogin().getId() > 0) 
			isExist = this.isExists(userRequest.getUserLogin().getEmail(),userRequest.getUserLogin().getId()); 
		else 
			isExist = this.isExists(userRequest.getUserLogin().getEmail());
		
		if (!isExist)  {
			UserLogin userLogin = UserRequest.convertToEntityUserLogin(userRequest);
			user = UserRequest.convertToEntityUser(userRequest);
			
			if(userRequest.getUserLogin().getId() != null && userRequest.getUserLogin().getId() > 0) {
				storedUserLoginObj = userLoginRepository.findById(userRequest.getUserLogin().getId()).get();
			
			//If password saved in databased and received in request are not same then need to update, else skip it
			if(!storedUserLoginObj.getPassword().equals(userRequest.getUserLogin().getPassword()))
				userLogin.setPassword(passwordEncoder.encode(userRequest.getUserLogin().getPassword()));
			} else
				userLogin.setPassword(passwordEncoder.encode(userRequest.getUserLogin().getPassword()));
			
			userLoginRepository.save(userLogin);
			
			if(!userLogin.getIsActive())
				user.setIsActive(false);
			user.setUserLogin(userLogin);
			this.save(user);
		}
		else
			throw new CPException(ErrorStatus.DUPLICATE_RECORD);
			
		return user;
		//Check length, it shouldn't be greater than 30
	}
	
	/**
	 * Save Or Update User
	 * 
	 * @param country
	 * @return
	 */
	private User save(User user) {
		return userRepository.save(user);
	}
	
	/**
	 * Check if user exists for given email 
	 * 
	 * @param name
	 * @return
	 */
	public boolean isExists(String email) {
		return userLoginRepository.countByEmailIgnoreCase(email) > 0;
	}
	
	/**
	 * Check if user exists for given email and ignore given id  
	 * 
	 * @param name
	 * @return
	 */
	public boolean isExists(String email, Long id) {
		return userLoginRepository.countByEmailAndIgnoreId(email, id) > 0;
	}
	
	/**
	 * Find all active brands whose name or email starts with given string sorted by name in ascending order
	 * 
	 * @param name
	 * @return
	 */
	public Page<User> search(String searchstr, Long brandId, int pageNumber) {
		Pageable pageable = PageRequest.of(pageNumber, Constants.PAGE_SIZE, DEFAULT_SORT);
		return userRepository.search(searchstr, brandId, pageable);
	}
	
	/**
	 * Find all active brand user whose name or email starts with given string sorted by name in ascending order
	 * 
	 * @param name
	 * @return
	 */
	public Page<User> searchActiveBrandUser(String searchstr, Long brandId, int pageNumber, String searchRole) {
		Pageable pageable = PageRequest.of(pageNumber, Constants.PAGE_SIZE, DEFAULT_SORT);

//		if(searchRole.equalsIgnoreCase("All")) {
//			return userRepository.searchActiveUser(searchstr, brandId, pageable);
//		}else {
//			return userRepository.searchActiveUser(searchstr, brandId, pageable, searchRole);
//		}
		
		Page<User> uList = null;
		if(searchRole.equalsIgnoreCase("All")) {
			uList= userRepository.searchActiveUser(searchstr, brandId, pageable);
		}else {
			uList =userRepository.searchActiveUser(searchstr, brandId, pageable, searchRole);
		}
		for(User user : uList) {
			if(user!=null)	{
				Hibernate.initialize(user.getUserLogin());
			}
		}
		if(searchRole.equalsIgnoreCase("All")) {
			return userRepository.searchActiveUser(searchstr, brandId, pageable);
		}else {
			return userRepository.searchActiveUser(searchstr, brandId, pageable, searchRole);
		}
//		return userRepository.searchActiveUser(searchstr, brandId, pageable);

	}
	
	/**
	 * Find all deleted brand user whose name or email starts with given string sorted by name in ascending order
	 * 
	 * @param name
	 * @return
	 */
	public Page<User> searchDeletedBrandUser(String searchstr, Long brandId, int pageNumber, String searchRole) {
		Pageable pageable = PageRequest.of(pageNumber, Constants.PAGE_SIZE, DEFAULT_SORT);
		Page<User> uList = null;
		
		if(searchRole.equalsIgnoreCase("All")) {
			uList=  userRepository.searchDeletedUser(searchstr, brandId, pageable);
		}else {
			uList =userRepository.searchDeletedUser(searchstr, brandId, pageable, searchRole);
		}
	
		for(User user : uList) {
			if(user!=null)	{
				Hibernate.initialize(user.getUserLogin());
			}
		}
		if(searchRole.equalsIgnoreCase("All")) {
			return userRepository.searchDeletedUser(searchstr, brandId, pageable);
		}else {
			return userRepository.searchDeletedUser(searchstr, brandId, pageable, searchRole);
		}
//		return userRepository.searchDeletedUser(searchstr, brandId, pageable);

	}
	
	/**
	 * Get brand user data
	 * @param brandUserId
	 * @return
	 */
	public Object getBrandUserData(Long brandUserId) {
		User user =  userRepository.findById(brandUserId).get();
		user.getUserLogin().setPassword(user.getUserLogin().getPassword());
		return user;
	}
	
	/**
	 * Deletes the record
	 * @param userId
	 * @return
	 * @throws CPException 
	 */
	public void deleteUser(Long userId) throws CPException {
		
		User user = userRepository.getOne(userId);
		
		user.setIsActive(false);
		user.setIsDelete(true);
		userRepository.save(user);
		
		userLoginService.deleteUser(user.getUserLogin().getId());
	}

	/**
	 * Restores the record
	 * @param userId
	 */
	public void restoreUser(Long userId) {
		User user = userRepository.getOne(userId);
		user.setIsActive(true);
		user.setIsDelete(false);
		userRepository.save(user);
		
		userLoginService.restoreUser(user.getUserLogin().getId());
	}
	/**
	 * get user name for send email
	 * 
	 * @param userId
	 * @return
	 */
	public User getUserByUserLoginId(Long userId) {
		
		return userRepository.findOneByUserLogin_Id(userId);
	}
	
	/**
	 * Get user by email
	 * 
	 * @param email
	 * @return
	 */
	public User getUserByEmail(String email) {
		
		return userRepository.findOneByUserLogin_Email(email);
	}

	/**
	 * Get all active user who has given role
	 * 
	 * @param roleDsmUser
	 * @return 
	 */
	public List<User> getAllActiveByRole(UserRole userRole) {
		return userRepository.findAllByUserLogin_RoleAndIsActiveTrue(userRole);
	}

	/**
	 * Get all active DSM users who are not assigned to any location division
	 * 
	 * @return
	 */
	public List<User> getUnassignedDSM() {
		
		return userRepository.findAllUnassignedUsers(UserRole.ROLE_DSM_USER.toString());
	}
	
	@Async
	public void saveImportedUser(RequestApiDto apiDtoObj, Long transactionId) {
		if (apiDtoObj.getData().size() > 0) {
			for (int i = 0; i < apiDtoObj.getData().size(); i++) {

				if(validateUser(transactionId, apiDtoObj.getData().get(i), apiDtoObj)) {
					UserRole role = null ;
					String userRole = null;
					
					switch (apiDtoObj.getType().trim()) {
					case "user_sm": {
						role = UserRole.ROLE_SM_USER;
						userRole = "ROLE_SM_USER";				
						break;
					}
					case "user_dsm": {
						role = UserRole.ROLE_DSM_USER;
						userRole = "ROLE_DSM_USER";			
						break;
						}
					case "user_ram": {
						role = UserRole.ROLE_RAM_USER; 
						userRole = "ROLE_RAM_USER";			
						break;
						}
					}
					
					User userOb = null;

					try {
						userOb = userRepository.getUserByThirdPartyId(Long.parseLong(apiDtoObj.getData().get(i).getId().trim()),userRole);
					} catch (Exception e) {
						userOb = null;
					}
					
					if (userOb != null) {

						if(setNmPhBrStUl(apiDtoObj, i, userOb))
						{
							try {
								UserLogin ulTemp = userOb.getUserLogin();
								if(ulTemp != null)
									ulTemp = userLoginRepository.save(ulTemp);
								
								userOb.setUserLogin(ulTemp);
								userRepository.save(userOb);
								apiLoggerService.saveApiLog(transactionId, Constants.USER, Long.parseLong(apiDtoObj.getData().get(i).getId().trim()), Constants.UPDATED, Constants.BYTHIRDPARTYID, Constants.SUCCESSUPD, apiDtoObj.getMsgId());	
							} catch (Exception e) {
								logger.debug("Error while savinf user for User_id: "+apiDtoObj.getData().get(i).getId().trim()+" : "+e.getMessage());
								apiLoggerService.saveApiLog(transactionId, Constants.USER, Long.parseLong(apiDtoObj.getData().get(i).getId().trim()), Constants.FAILED, Constants.BYTHIRDPARTYID, e.getMessage(), apiDtoObj.getMsgId());
							}
						}
						else
							apiLoggerService.saveApiLog(transactionId, Constants.USER, Long.parseLong(apiDtoObj.getData().get(i).getId().trim()), Constants.NOTFOUND, Constants.BYTHIRDPARTYID, Constants.SKIPPED, apiDtoObj.getMsgId());
					}
					else {
						//valid
//						User userObWithName = null;
//						
//						if(validateName(apiDtoObj, i)) {
//							 userObWithName = userRepository.findByNameIgnoreCase(apiDtoObj.getData().get(i).getName().trim());
//						}
//						
//						if(userObWithName != null)
//						{
//							userObWithName.setThirdPartyId(Long.parseLong(apiDtoObj.getData().get(i).getId().trim()));
//							
//							if(setNmPhBrStUl(apiDtoObj, i, userObWithName))
//							{
//								try {
//									UserLogin ulTemp = userObWithName.getUserLogin();
//									userLoginRepository.save(ulTemp);
//									userRepository.save(userObWithName);
//									apiLoggerService.saveApiLog(transactionId, Constants.USER, Long.parseLong(apiDtoObj.getData().get(i).getId().trim()), Constants.UPDATED, Constants.BYNAME, Constants.SUCCESSUPD, apiDtoObj.getMsgId());
//								} catch (Exception e) {
//									apiLoggerService.saveApiLog(transactionId, Constants.USER, Long.parseLong(apiDtoObj.getData().get(i).getId().trim()), Constants.FAILED, Constants.BYNAME, e.getMessage(), apiDtoObj.getMsgId());
//								}
//							}
//							else
//							{
//								apiLoggerService.saveApiLog(transactionId, Constants.USER, Long.parseLong(apiDtoObj.getData().get(i).getId().trim()), Constants.NOTFOUND, Constants.BYNAME, Constants.SKIPPED, apiDtoObj.getMsgId());
//							}
//						}
//						else
//						{
							User userObjNew = new User();
							userObjNew.setThirdPartyId(Long.parseLong(apiDtoObj.getData().get(i).getId().trim()));
							
							if(setNmPhBrStUl(apiDtoObj, i, userObjNew))
							{
								try {
									UserLogin ulTemp = userObjNew.getUserLogin();
									if(ulTemp != null)
										ulTemp = userLoginRepository.save(ulTemp);
									
									userObjNew.setUserLogin(ulTemp);
									userLoginRepository.save(ulTemp);
									userRepository.save(userObjNew);
									
									apiLoggerService.saveApiLog(transactionId, Constants.USER, Long.parseLong(apiDtoObj.getData().get(i).getId().trim()), Constants.INSERTED, Constants.BYTHIRDPARTYID, Constants.SUCCESSINS, apiDtoObj.getMsgId());
								} catch (Exception e) {
									logger.debug("Error while creating new user for User_id: "+apiDtoObj.getData().get(i).getId().trim()+" : "+e.getMessage());
									apiLoggerService.saveApiLog(transactionId, Constants.USER, Long.parseLong(apiDtoObj.getData().get(i).getId().trim()), Constants.FAILED, Constants.BYTHIRDPARTYID, e.getMessage(), apiDtoObj.getMsgId());
								}
							}
							else
							{
								apiLoggerService.saveApiLog(transactionId, Constants.USER, Long.parseLong(apiDtoObj.getData().get(i).getId().trim()), Constants.NOTFOUND, Constants.BYTHIRDPARTYID, Constants.SKIPPED, apiDtoObj.getMsgId());
							}
						}
					}
			}
		} else
			apiLoggerService.saveApiLog(transactionId, Constants.USER, Long.valueOf(0), Constants.NOTFOUND, Constants.SKIPPED, "Data parameter is empty in request", apiDtoObj.getMsgId());
	}
	
	private boolean setNmPhBrStUl(RequestApiDto apiDtoObj, int i, User userOb) {
		
		boolean checkFlag = true;
		
		try {
			
			userOb.setPhone("0000000000");
			
			setNameForUser(apiDtoObj,i,userOb);
			
			setBrandForUser(apiDtoObj,i,userOb);
			
			try {
				if(apiDtoObj.getType().toString().trim().equalsIgnoreCase("user_sm"))
					setStoreForSmUser(apiDtoObj,i,userOb);
				else
					userOb.setStore(null);
				
			} catch (Exception e) {
				userOb.setStore(null);
			}
			
			try {
				UserLogin ulOb = userLoginRepository.findByEmailIgnoreCase(apiDtoObj.getData().get(i).getEmail().trim());
				if(ulOb != null)
				{
					ulOb.setEmail(apiDtoObj.getData().get(i).getEmail().trim());
					ulOb.setPassword(passwordEncoder.encode(Constants.DEFAULTPASSWORD));
					UserRole role = null ;
					
					switch (apiDtoObj.getType()) {
					case "user_sm": role = UserRole.ROLE_SM_USER; break;
					case "user_dsm": role = UserRole.ROLE_DSM_USER; break;
					case "user_ram": role = UserRole.ROLE_RAM_USER; break; 
					}
					
					if(role != null)
						ulOb.setRole(role);
					else
						ulOb.setRole(null);
					
					ulOb.setPermissions(null);
					ulOb.setIsActive(true);
					ulOb.setIsDelete(false);
				
					userOb.setUserLogin(ulOb);
				}
				else
				{
					UserLogin ulNew = new UserLogin();
					ulNew.setEmail(apiDtoObj.getData().get(i).getEmail().trim());
					ulNew.setPassword(passwordEncoder.encode(Constants.DEFAULTPASSWORD));
					UserRole role = null ;
					
					switch (apiDtoObj.getType()) {
					case "user_sm": role = UserRole.ROLE_SM_USER; break;
					case "user_dsm": role = UserRole.ROLE_DSM_USER; break;
					case "user_ram": role = UserRole.ROLE_RAM_USER; break; 
					}
					
					if(role != null)
						ulNew.setRole(role);
					else
						ulNew.setRole(null);
				
					ulNew.setPermissions(null);
					ulNew.setIsActive(true);
					ulNew.setIsDelete(false);
					userOb.setUserLogin(ulNew);
				}
			
			} catch (Exception e) {
				userOb.setUserLogin(null);
				logger.debug("Error while creating new userLogin object for User_id: "+apiDtoObj.getData().get(i).getId().trim()+" : "+e.getMessage());
			}
				
			
		} catch (Exception e) {
			
			userOb.setBrand(null);
			userOb.setStore(null);
			userOb.setUserLogin(null);
		}
		
		userOb.setIsActive(true);
		userOb.setIsDelete(false);
		
		return checkFlag;
	}
	
	private void setNameForUser(RequestApiDto apiDtoObj, int i, User userOb) {
	
		try {
			if (validateName(apiDtoObj, i)) {
				userOb.setName(apiDtoObj.getData().get(i).getName().trim());
			}else {
				userOb.setName("NA-"+apiDtoObj.getData().get(i).getId().trim());
				logger.debug("Error in setNameForUser while get name null for user with user_id: "+userOb.getThirdPartyId());
			}
		} catch (Exception e) {
			userOb.setName("NA-"+apiDtoObj.getData().get(i).getId().trim());
			logger.debug("Error in setNameForUser while get data for user with user_id: "+userOb.getThirdPartyId()+" : "+e.getMessage());
		}
		
	}

	private boolean validateName(RequestApiDto apiDtoObj, int i) {
		boolean validateFlag = false;

		validateFlag = apiDtoObj.getData().get(i).getName() != null
				&& (!apiDtoObj.getData().get(i).getName().trim().equalsIgnoreCase("null"))
				&& (!apiDtoObj.getData().get(i).getName().trim().equalsIgnoreCase("0"))
				&& (!apiDtoObj.getData().get(i).getName().trim().equalsIgnoreCase(""));

		return validateFlag;
	}

	private void setStoreForSmUser(RequestApiDto apiDtoObj, int i, User userOb) {
		
		try {
			
			if (validateStoreId(apiDtoObj, i)) {
				Store storeObj = storeRepository.findByThirdPartyId(Long.parseLong(apiDtoObj.getData().get(i).getStore_id().trim()));
				if (storeObj != null) {
					userOb.setStore(storeObj);
				} else {
					try {
						Store newDummyStoreOb = new Store();
						newDummyStoreOb.setThirdPartyId(Long.parseLong(apiDtoObj.getData().get(i).getStore_id().trim()));
						newDummyStoreOb.setName("NA-" + Long.parseLong(apiDtoObj.getData().get(i).getStore_id().trim()));
						newDummyStoreOb.setIsActive(false);
						newDummyStoreOb.setIsDelete(true);
						newDummyStoreOb = storeRepository.save(newDummyStoreOb);
						userOb.setStore(newDummyStoreOb);
						
					} catch (Exception e) {
						userOb.setStore(null);
						logger.debug("Error  in setStoreForSmUser while create Dummy obj for store with user_id: "+apiDtoObj.getData().get(i).getId().trim()+" : " +e.getMessage());
					}
				}
			} else {
				userOb.setStore(null);
				logger.debug("Error in setStoreForSmUser while get brand_id get null or get  not field store_id with user_id: "+apiDtoObj.getData().get(i).getId().trim());
			}
		} catch (Exception e) {
			userOb.setStore(null);
			logger.debug("Error in setStoreForSmUser while not get data of Brand_id for Brand with user_id: "+apiDtoObj.getData().get(i).getId().trim()+" : " +e.getMessage());
		}
	}

	private boolean validateStoreId(RequestApiDto apiDtoObj, int i) {
		boolean validateFlag = false;

		validateFlag = apiDtoObj.getData().get(i).getStore_id() != null
				&& (!apiDtoObj.getData().get(i).getStore_id().trim().equalsIgnoreCase("null"))
				&& (!apiDtoObj.getData().get(i).getStore_id().trim().equalsIgnoreCase("0"))
				&& (!apiDtoObj.getData().get(i).getStore_id().trim().equalsIgnoreCase(""));

		return validateFlag;
	}

	private void setBrandForUser(RequestApiDto apiDtoObj, int i, User userOb) {
	
		try {
			
			if (validateBrandId(apiDtoObj, i)) {
				Brand brandObj = brandRepository.findByThirdPartyId(Long.parseLong(apiDtoObj.getData().get(i).getBrand_id().trim()));
				if (brandObj != null) {
					userOb.setBrand(brandObj);
				} else {
					try {
						
						Brand newDummyBrandOb = new Brand();
						newDummyBrandOb.setThirdPartyId(Long.parseLong(apiDtoObj.getData().get(i).getBrand_id().trim()));
						newDummyBrandOb.setName("NA-" + Long.parseLong(apiDtoObj.getData().get(i).getBrand_id().trim()));
						newDummyBrandOb.setIsActive(false);
						newDummyBrandOb.setIsDelete(true);
						newDummyBrandOb = brandRepository.save(newDummyBrandOb);
						userOb.setBrand(newDummyBrandOb);
						
					} catch (Exception e) {
						userOb.setBrand(null);
						logger.debug("Error  in setBrandForUser while create Dummy obj for Brand with user_id: "+apiDtoObj.getData().get(i).getId().trim()+" : " +e.getMessage());
					}
				}
			} else {
				userOb.setBrand(null);
				logger.debug("Error in setBrandForUser while get brand_id get null or get  not field Brand_id with user_id: "+apiDtoObj.getData().get(i).getId().trim());
			}
		} catch (Exception e) {
			userOb.setBrand(null);
			logger.debug("Error in setBrandForUser while not get data of Brand_id for Brand with user_id: "+apiDtoObj.getData().get(i).getId().trim()+" : " +e.getMessage());
		}
	}

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
	 * @param transactionId
	 * @param requestApiDataDto
	 * @return
	 */
	private Boolean validateUser(Long transactionId, RequestApiDataDto requestApiDataDto,RequestApiDto apiDtoObj) {
		
		try {
			 if(requestApiDataDto.getId() == null) {
					apiLoggerService.saveApiLog(transactionId, Constants.USER, Long.valueOf(0), Constants.VALIDATION_FAILED, Constants.SKIPPED, "Id parameter not found in request", apiDtoObj.getMsgId());
					logger.debug("Error due to Insufficient data: Id parameter not found in request for User_id: "+requestApiDataDto.getId().trim());
					return false;
					
				} else if(requestApiDataDto.getId().trim().equals("") || requestApiDataDto.getId().trim().equalsIgnoreCase("null")) {
					apiLoggerService.saveApiLog(transactionId, Constants.USER, Long.valueOf(0), Constants.VALIDATION_FAILED, Constants.SKIPPED, "Id value not found in request", apiDtoObj.getMsgId());
					logger.debug("Error due to Insufficient data: Id Value not found in request for User_id: "+requestApiDataDto.getId().trim());
					return false;
				}
				else if(requestApiDataDto.getEmail() == null) {
					apiLoggerService.saveApiLog(transactionId, Constants.USER, Long.valueOf(requestApiDataDto.getId().trim()), Constants.VALIDATION_FAILED, Constants.SKIPPED, "Email parameter not found in request", apiDtoObj.getMsgId());
					logger.debug("Error due to Insufficient data: Email parameter not found in request for User_id: "+requestApiDataDto.getId().trim());
					return false;
				}
				else if(requestApiDataDto.getEmail().trim().equals("") || requestApiDataDto.getEmail().trim().equalsIgnoreCase("null")) {
					apiLoggerService.saveApiLog(transactionId, Constants.USER, Long.valueOf(requestApiDataDto.getId().trim()), Constants.VALIDATION_FAILED, Constants.SKIPPED, "Email value not found in request", apiDtoObj.getMsgId());
					logger.debug("Error due to Insufficient data: Email value not found in request for User_id: "+requestApiDataDto.getId().trim());
					return false;
				}
				else if(requestApiDataDto.getEmail()!= null) {
					 String email = requestApiDataDto.getEmail().trim();
					 String regex ="^[a-zA-Z0-9_+&*-]+(?:\\."+ 
		                      "[a-zA-Z0-9_+&*-]+)*@" + 
		                      "(?:[a-zA-Z0-9-]+\\.)+[a-z" + 
		                      "A-Z]{2,7}$";
			         Pattern pattern = Pattern.compile(regex);
			         java.util.regex.Matcher mat = pattern.matcher(email);

			         if(!mat.matches())
			         {
			         	apiLoggerService.saveApiLog(transactionId, Constants.USER, Long.valueOf(requestApiDataDto.getId().trim()), Constants.VALIDATION_FAILED, Constants.SKIPPED, "Email not valid found in request", apiDtoObj.getMsgId());
			         	logger.debug("Email is invalid for User_id: "+requestApiDataDto.getId().trim());
			         	return false;
			         }
				}
				return true;
		} catch (Exception e) {
			return false;
		}
		
	}

	public void logOutUser() {
		String user = SecurityContextHolder.getContext().getAuthentication().getName();
		if(user != null) {
			try {
				auditService.generateAuditMessageForUserLogOut();
//				userRepository.removeOauthRefreshToken(user);
//				userRepository.setBlankOauthAccessToken(user); 
			} catch (Exception e) {
			 logger.debug("Error while remove refresh token. "+e.getMessage());
			}
		
		}
	}
}
