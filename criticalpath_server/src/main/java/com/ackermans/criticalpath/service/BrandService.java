package com.ackermans.criticalpath.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.ackermans.criticalpath.common.dto.CPException;
import com.ackermans.criticalpath.entity.Brand;
import com.ackermans.criticalpath.entity.Store;
import com.ackermans.criticalpath.enums.ErrorStatus;
import com.ackermans.criticalpath.repository.BrandRepository;
import com.ackermans.criticalpath.repository.StoreRepository;
import com.ackermans.criticalpath.utils.Constants;
import com.api.criticalpath.dto.RequestApiDataDto;
import com.api.criticalpath.dto.RequestApiDto;

@Service
public class BrandService {

	@Autowired
	private BrandRepository brandRepo;
	
	@Autowired
	private StoreRepository storeRepo;
	
	@Autowired
	ApiLoggerService apiLoggerService;
	
	/**
	 * Sort by name in ascending order
	 */
	private final Sort DEFAULT_SORT = Sort.by("name");
	
	/**
	 * Save brand
	 * 
	 * @param brand
	 * @return
	 */
	private Brand save(Brand brand) {
		return brandRepo.save(brand);
	}
	
	/**
	 * Verify brand object for different rules, if not valid throw proper error message.
	 * 
	 * @param brand
	 * @throws CPException
	 */
	/*private void verify(Brand brand) throws CPException {
		
		//If there is id that means record needs to be updated
		//hence we should ignore that record while checking for existence
		if(brand.getId() != null && brand.getId() > 0)
			if(this.isExists(brand.getName(), brand.getId()))
				throw new CPException(ErrorStatus.DUPLICATE_RECORD);
		else if(this.isExists(brand.getName()))
			throw new CPException(ErrorStatus.DUPLICATE_RECORD);
		
		//Check length, it shouldn't be greater than 30
		if(brand.getName().length() > Constants.EVENT_NAME_LENGTH)
			throw new CPException(ErrorStatus.MAX_LENGTH, "name", Constants.EVENT_NAME_LENGTH);
	}*/
	
	/**
	 * Check if brand exists for given name 
	 * 
	 * @param name
	 * @return
	 */
	public boolean isExists(String name) {
		return brandRepo.countByNameIgnoreCase(name) > 0;
	}
	
	/**
	 * Check if brand exists for given name and ignore given id  
	 * 
	 * @param name
	 * @return
	 */
	public boolean isExists(String name, Long id) {
		return brandRepo.countByNameAndIgnoreId(name, id) > 0;
	}
	
	/**
	 * Validate name if invalid then throw exception otherwise save.
	 * 
	 * @param name
	 * @return
	 * @throws CPException
	 *//*
	public Brand verifyAndSave(String name) throws CPException {
		return verifyAndSave(new Brand(name));
	}*/

	/**
	 * Validate name if invalid then throw exception otherwise save.
	 * 
	 * @param brand
	 * @return
	 * @throws CPException
	 */
	public Brand verifyAndSave(Brand brand) throws CPException {
		
		boolean isExist = false;
		
		//If there is id that means record needs to be updated
		//hence we should ignore that record while checking for existence
		if(brand.getId() != null && brand.getId() > 0) 
			isExist = this.isExists(brand.getName(), brand.getId()); 
		else 
			isExist = this.isExists(brand.getName());
		
		if(brand.getName().length() > Constants.EVENT_NAME_LENGTH)
			throw new CPException(ErrorStatus.MAX_LENGTH, "name", Constants.EVENT_NAME_LENGTH);
			
		if (!isExist) 
			this.save(brand);
		else
			throw new CPException(ErrorStatus.DUPLICATE_RECORD);
			
		return brand;
		//Check length, it shouldn't be greater than 30
	}
	
	/**
	 * Get all brands sorted by name in ascending order
	 * 
	 * @return
	 */
	public List<Brand> getAll() {
		return brandRepo.findAll(DEFAULT_SORT);
	}
	
	/**
	 * Get all brands sorted by name in ascending order
	 * 
	 * @return
	 */
	public Page<Brand> getAll(int pageNumber) {
		return (Page<Brand>) brandRepo
				.findAll(PageRequest.of(pageNumber, Constants.PAGE_SIZE, DEFAULT_SORT));
	}

	/**
	 * Get all active brands sorted by name in ascending order
	 * 
	 * @return
	 */
	public List<Brand> getAllActive() {
		return brandRepo.findAllByIsActiveTrue(DEFAULT_SORT);
	}
	
	/**
	 * Get all active brands sorted by name in ascending order
	 * 
	 * @return
	 */
	public Page<Brand> getAllActive(int pageNumber) {
		return  brandRepo
				.findAllByIsActiveTrue(PageRequest.of(pageNumber, Constants.PAGE_SIZE, DEFAULT_SORT));
	}
	
	/**
	 * Get all deleted brands sorted by name in ascending order
	 * 
	 * @return
	 */
	public Page<Brand> getAllDeletedBrand(int pageNumber) {
		return brandRepo
				.findAllByIsActiveFalseAndIsDeleteTrue(PageRequest.of(pageNumber, Constants.PAGE_SIZE, DEFAULT_SORT));
	}
	
	/**
	 * Finall all active brands whose name starts with given string sorted by name in ascending order
	 * 
	 * @param name
	 * @return
	 */
	public List<Brand> search(String name) {
		return brandRepo.findByNameStartingWithIgnoreCaseAndIsActiveTrue(name, DEFAULT_SORT);
	}
	
	/**
	 * Get Brand List By Searching Value With Fix row
	 * @param searchStr
	 * @param pageNumber
	 * @return
	 */
	public Page<Brand> searchActive(String searchStr, int pageNumber) {
		Pageable pageable = PageRequest.of(pageNumber, Constants.PAGE_SIZE, DEFAULT_SORT);
		return brandRepo.findBySearchActiveBrand(searchStr,pageable);
		
	}

	/**
	 * Get all active Brand List By Searching Value With Fix row
	 * @param searchStr
	 * @param pageNumber
	 * @return
	 */
	public Page<Brand> searchActiveTrue(String searchStr, int pageNumber) {
		Pageable pageable = PageRequest.of(pageNumber, Constants.PAGE_SIZE, DEFAULT_SORT);
		return brandRepo.findBySearchActiveTrueBrand(searchStr,pageable);
		
	}
	
	/**
	 * Get all delete  Brand List By Searching Value With Fix row
	 * @param searchStr
	 * @param pageNumber
	 * @return
	 */
	public Page<Brand> searchActiveFalse(String searchStr, int pageNumber) {
		Pageable pageable = PageRequest.of(pageNumber, Constants.PAGE_SIZE, DEFAULT_SORT);
		return brandRepo.findBySearchActiveFalseBrand(searchStr,pageable);
		
	}
	
	
	/**
	 * Get Brand by BrandId
	 * @param brandId
	 * @return
	 */
	public Object get(Long brandId) {
		return brandRepo.findById(brandId);
	}
	
	/**
	 * Deletes the record
	 * @param eventId
	 * @return
	 * @throws CPException 
	 */
	public void deleteBrand(Long brandId) throws CPException {
		
		//Check if there any active store for that brand
		List<Store> store = (List<Store>) storeRepo.findByBrand_IdAndIsActiveTrue(brandId);
		
		if(!store.isEmpty())
			throw new CPException(ErrorStatus.STORES_EXISTS);
		else  {
			Brand brand = brandRepo.getOne(brandId);
			brand.setIsActive(false);
			brand.setIsDelete(true);
			brandRepo.save(brand);
		}
	}

	/**
	 * Restores the event
	 * @param eventId
	 */
	public void restoreBrand(Long eventId) {
		Brand brand = brandRepo.getOne(eventId);
		brand.setIsActive(true);
		brand.setIsDelete(false);
		brandRepo.save(brand);
		return ;
	}
	
	@Async
	public void saveImportedBrand(RequestApiDto apiDtoObj, Long transactionId) {
		
		if (apiDtoObj.getData().size() > 0) {
			for (int i = 0; i < apiDtoObj.getData().size(); i++) {

				if(validateBrand(transactionId, apiDtoObj.getData().get(i), apiDtoObj)) {
					Brand brOb = brandRepo.findByThirdPartyId(Long.parseLong(apiDtoObj.getData().get(i).getId()));
	
					if (brOb != null) {
	
						if (!brOb.getName().equalsIgnoreCase(apiDtoObj.getData().get(i).getName())) {
							try {
								brOb.setName(apiDtoObj.getData().get(i).getName());
								brOb.setIsActive(true);
								brOb.setIsDelete(false);
								brandRepo.save(brOb);
								apiLoggerService.saveApiLog(transactionId, Constants.BRAND, Long.parseLong(apiDtoObj.getData().get(i).getId()), Constants.UPDATED, Constants.BYTHIRDPARTYID, Constants.SUCCESSUPD, apiDtoObj.getMsgId());
								
							} catch (Exception e) {
								apiLoggerService.saveApiLog(transactionId, Constants.BRAND, Long.parseLong(apiDtoObj.getData().get(i).getId()), Constants.FAILED, Constants.BYTHIRDPARTYID, Constants.FAILDIFFIDNAMEAVAIL, apiDtoObj.getMsgId());
							}
						}
						else
						{
							try {
								brOb.setIsActive(true);
								brOb.setIsDelete(false);
								brandRepo.save(brOb);
								apiLoggerService.saveApiLog(transactionId, Constants.BRAND, Long.parseLong(apiDtoObj.getData().get(i).getId()), Constants.SKIP, Constants.BYTHIRDPARTYID, Constants.ALREADYAVAIL, apiDtoObj.getMsgId());	
							} catch (Exception e) {
								apiLoggerService.saveApiLog(transactionId, Constants.BRAND, Long.parseLong(apiDtoObj.getData().get(i).getId()), Constants.FAILED, Constants.BYTHIRDPARTYID, e.getMessage(), apiDtoObj.getMsgId());
							}
							
						}
					
				} else {
					
					Brand brObWithName = brandRepo
							.findByNameIgnoreCase(apiDtoObj.getData().get(i).getName());
					
					if(brObWithName != null)
					{
						try {
							brObWithName.setThirdPartyId(Long.parseLong(apiDtoObj.getData().get(i).getId()));
							brObWithName.setIsActive(true);
							brObWithName.setIsDelete(false);
							brandRepo.save(brObWithName);
							
							apiLoggerService.saveApiLog(transactionId, Constants.BRAND, Long.parseLong(apiDtoObj.getData().get(i).getId()), Constants.UPDATED, Constants.BYNAME, Constants.SUCCESSUPD, apiDtoObj.getMsgId());	
						} catch (Exception e) {
							apiLoggerService.saveApiLog(transactionId, Constants.BRAND, Long.parseLong(apiDtoObj.getData().get(i).getId()), Constants.FAILED, Constants.BYNAME, e.getMessage(), apiDtoObj.getMsgId());
						}
						
					}
					else
					{
						try {
							Brand brObjNew = new Brand();
							brObjNew.setName(apiDtoObj.getData().get(i).getName());
							brObjNew.setThirdPartyId(Long.parseLong(apiDtoObj.getData().get(i).getId()));
							brandRepo.save(brObjNew);
							
							apiLoggerService.saveApiLog(transactionId, Constants.BRAND, Long.parseLong(apiDtoObj.getData().get(i).getId()), Constants.INSERTED, Constants.BYTHIRDPARTYID, Constants.SUCCESSINS, apiDtoObj.getMsgId());	
						} catch (Exception e) {
							apiLoggerService.saveApiLog(transactionId, Constants.BRAND, Long.parseLong(apiDtoObj.getData().get(i).getId()), Constants.FAILED, Constants.BYTHIRDPARTYID, e.getMessage(), apiDtoObj.getMsgId());
						}
						
					}
					
					
				}
			}
		  }
		}
		else
			apiLoggerService.saveApiLog(transactionId, Constants.BRAND, Long.valueOf(0), Constants.NOTFOUND, Constants.SKIPPED, "Data parameter is empty in request", apiDtoObj.getMsgId());
	}

	/**
	 * Validates the request
	 * @param transactionId
	 * @param requestApiDataDto
	 * @return
	 */
	private Boolean validateBrand(Long transactionId, RequestApiDataDto requestApiDataDto, RequestApiDto rpd) {
		
		if(requestApiDataDto.getId() == null) {
			apiLoggerService.saveApiLog(transactionId, Constants.BRAND, Long.valueOf(0), Constants.VALIDATION_FAILED, Constants.SKIPPED, "Id parameter not found in request", rpd.getMsgId());
			return false;
		}  else if(requestApiDataDto.getId().equals("")) {
			apiLoggerService.saveApiLog(transactionId, Constants.BRAND, Long.valueOf(0), Constants.VALIDATION_FAILED, Constants.SKIPPED, "Id value not found in request",rpd.getMsgId());
			return false;
		}  else if(requestApiDataDto.getName() == null) {
			apiLoggerService.saveApiLog(transactionId, Constants.BRAND, Long.valueOf(requestApiDataDto.getId()), Constants.VALIDATION_FAILED, Constants.SKIPPED, "Name parameter not found in request",rpd.getMsgId());
			return false;
		} else if(requestApiDataDto.getName().equals("")) {
			apiLoggerService.saveApiLog(transactionId, Constants.BRAND, Long.valueOf(requestApiDataDto.getId()), Constants.VALIDATION_FAILED, Constants.SKIPPED, "Name value not found in request",rpd.getMsgId());
			return false;
		}
		return true;
	}
}
