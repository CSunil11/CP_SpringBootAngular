package com.ackermans.criticalpath.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ackermans.criticalpath.admin.dto.StoreCloseDateRequest;
import com.ackermans.criticalpath.common.dto.ApiResponse;
import com.ackermans.criticalpath.common.dto.CPException;
import com.ackermans.criticalpath.entity.Brand;
import com.ackermans.criticalpath.entity.CloseDate;
import com.ackermans.criticalpath.entity.StockTakeCycle;
import com.ackermans.criticalpath.entity.Store;
import com.ackermans.criticalpath.enums.ErrorStatus;
import com.ackermans.criticalpath.repository.StoreCloseDateRepository;
import com.ackermans.criticalpath.utils.DateUtils;

@Service
@Transactional
public class StoreCloseDateService {
	
	private final Logger logger = LogManager.getFormatterLogger();

	@Autowired
	private StoreCloseDateRepository storeCloseDateRepository;
	
	@Autowired
	private StoreService storeService;
	
	/**
	 * Verify and Save StoreCloseDate
	 * @param storeCloseDateReq
	 * @return
	 * @throws CPException
	 * @throws ParseException 
	 */
	public CloseDate verifyAndSave(StoreCloseDateRequest storeCloseDateReq) throws CPException, ParseException {
		
		this.validate(storeCloseDateReq);
		
		CloseDate storeCloseDate;
		if(storeCloseDateReq.getId() != null && storeCloseDateReq.getId() > 0) {
			storeCloseDate = this.get(storeCloseDateReq.getId());
			storeCloseDateRepository.deleteStoreCloseDate(storeCloseDateReq.getId());
		}
		else
			storeCloseDate = new CloseDate();
		
		storeCloseDate.setName(storeCloseDateReq.getName());
		storeCloseDate.setDescription(storeCloseDateReq.getDescription());
		Date closeDateFormate = new SimpleDateFormat("dd/MM/yyyy").parse(storeCloseDateReq.getCloseDate());
//		storeCloseDate.setCloseDate(DateUtils.getDate(storeCloseDateReq.getCloseDate()));
		storeCloseDate.setCloseDate(closeDateFormate);
		
		
		storeCloseDate.setStores(storeCloseDateReq.getStores());
		storeCloseDate.setIsActive(storeCloseDateReq.getIsActive());

		return storeCloseDateRepository.save(storeCloseDate);
	}
	
	/**
	 * Validate request
	 * 
	 * @param storeCloseDateReq
	 * @throws CPException
	 */
	public void validate(StoreCloseDateRequest storeCloseDateReq) throws CPException {
		
		if(storeCloseDateReq.getCloseDate() == null)
			throw new CPException(ErrorStatus.CLOSE_DATE_NOT_FOUND);
		
		if(storeCloseDateReq.getStores() == null || storeCloseDateReq.getStores().size() == 0)
			throw new CPException(ErrorStatus.STORES_NOT_FOUND);
		
		//If there is id that means record needs to be updated
		//hence we should ignore that record while checking for existence
		if(storeCloseDateReq.getId() != null && storeCloseDateReq.getId() > 0) {
			try {
				if(isExist(storeCloseDateReq.getName(), DateUtils.getDate(storeCloseDateReq.getCloseDate()), storeCloseDateReq.getId()))
					throw new CPException(ErrorStatus.DUPLICATE_RECORD);
			} catch (ParseException e) {
				logger.error("Error while parsing close date :: "+e);
				throw new CPException(ErrorStatus.INVALID_DATE, storeCloseDateReq.getCloseDate(), DateUtils.DATE_FORMAT);
			}
		} else {
			try {
				if(isExist(storeCloseDateReq.getName(), DateUtils.getDate(storeCloseDateReq.getCloseDate())))
						throw new CPException(ErrorStatus.DUPLICATE_RECORD);
			} catch (ParseException e) {
				logger.error("Error while parsing close date :: "+e);
				throw new CPException(ErrorStatus.INVALID_DATE, storeCloseDateReq.getCloseDate(), DateUtils.DATE_FORMAT);
			}
		}
	}
	
	/**
	 * Check if record already exists for given close date
	 * 
	 * @param closeDate
	 * @return
	 */
	public boolean isExist(String name, Date closeDate) {
		return storeCloseDateRepository.countByNameOrCloseDate(name, closeDate) > 0;
	}
	
	/**
	 * Check if record already exists for given close date but ignore given id
	 * 
	 * @param closeDate
	 * @return
	 */
	public boolean isExist(String name, Date closeDate, Long ignoreId) {
		return storeCloseDateRepository.countByCloseDateAndIgnoreId(name, closeDate, ignoreId) > 0;
	}

	/**
	 * Get all StoreCloseDate
	 * @return
	 */
	public List<CloseDate> getAll(){
		return storeCloseDateRepository.findAll();
	}
	
	/**
	 * Find by close date
	 * 
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public List<CloseDate> getByCloseDate(String date) throws ParseException {
		return storeCloseDateRepository.findByCloseDate(DateUtils.getDate(date));
	}
	
	/**
	 * Get all active StoreCloseDate List
	 * 
	 * @return
	 */
	public List<CloseDate> getAllActive() {
		
 		List<CloseDate> lcd= storeCloseDateRepository.findAllByIsActiveTrue();
 		
 		for (CloseDate cd : lcd) {
			for (Store store : cd.getStores()) {
				System.out.println(store);
			}
		}
		
		if(lcd.size()>0)
			return lcd;
		else
			return null;
	}
	
	/**
	 * Get all deleted StoreCloseDate List
	 * 
	 * @return
	 */
	public List<CloseDate> getAllDeletedStoreClosed() {
		
		List<CloseDate> lcd= storeCloseDateRepository.findAllByIsActiveFalseAndIsDeleteTrue();
 		
 		for (CloseDate cd : lcd) {
			for (Store store : cd.getStores()) {
				System.out.println(store);
			}
		}
		
		if(lcd.size()>0)
			return lcd;
		else
			return null;
	}
	
	/**
	 * Activate StoreCloseDate by id
	 * 
	 * @param id
	 * @return
	 */
	public CloseDate activate(Long id) {
		CloseDate storeCloseDate = storeCloseDateRepository.getOne(id);
		storeCloseDate.setIsActive(true);
		return storeCloseDateRepository.save(storeCloseDate);
	}
	
	/**
	 * Deactivate StoreCloseDate by id
	 * 
	 * @param id
	 * @return
	 */
	public CloseDate deactivate(Long id) {
		CloseDate storeCloseDate = storeCloseDateRepository.getOne(id);
		storeCloseDate.setIsActive(false);
		return storeCloseDateRepository.save(storeCloseDate);
	}

	/**
	 * Get StoreCloseDate By Store Id
	 * 
	 * @param storeId
	 * @return
	 */
	public List<CloseDate> getByStore(Long storeId) {
		return storeCloseDateRepository.findByStores_Id(storeId);
	}
	
	/**
	 * Find by id
	 * 
	 * @param id
	 * @return
	 */
	public CloseDate get(Long id) {
		return storeCloseDateRepository.getOne(id);
	}
	
	/**
	 * Get Store close date By closeDateId
	 * @param closeDateId
	 * @return
	 */
	public Object getStoreClosedData(Long closeDateId) {
		
		Optional<CloseDate> closedDate = storeCloseDateRepository.findById(closeDateId);
		
		Set<Store> stores = closedDate.get().getStores();
		
		int n = stores.size();
	    List<Store> aList = new ArrayList<Store>(n);
	    aList.addAll(stores);
	    
		for (Store store : aList) {
				System.out.println(store);
		}
		
		if(aList.size()>0)
			return closedDate;
		else
			return null;
	}
	
	/**
	 * Deletes the record
	 * @param eventId
	 * @return
	 * @throws CPException 
	 */
	public void deleteCloseDate(Long closeDateId) throws CPException {
		
		CloseDate closeDate = storeCloseDateRepository.getOne(closeDateId);
		closeDate.setIsActive(false);
		closeDate.setIsDelete(true);
		storeCloseDateRepository.save(closeDate);
	}

	/**
	 * Restores the record
	 * @param eventId
	 */
	public void restoreCloseDate(Long closeDateId) {
		CloseDate closeDate = storeCloseDateRepository.getOne(closeDateId);
		closeDate.setIsActive(true);
		closeDate.setIsDelete(false);
		storeCloseDateRepository.save(closeDate);
		return ;
	}

	/**
	 * Get closed date count by date
	 * @param storeId
	 * @param newdate
	 * @param stokedate
	 * @return
	 */
	public int getByDate(Long storeId, String newdate, String stokedate) {
		
		return storeCloseDateRepository.findByStoresDate(storeId , newdate ,stokedate);
	}

	/**
	 * Get closed date list by date
	 * @param storeId
	 * @param newdate
	 * @param stokedate
	 * @return
	 */
	public Object getClosedDateListByDate(Long storeId, String newdate, String stokedate) {
		List list = new ArrayList<>();
		list = storeCloseDateRepository.closedDateByStoresDate(storeId , newdate ,stokedate);
	
		return list;
	}
}
