package com.ackermans.criticalpath.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ackermans.criticalpath.common.dto.CPException;
import com.ackermans.criticalpath.entity.DaysOfWeek;
import com.ackermans.criticalpath.enums.ErrorStatus;
import com.ackermans.criticalpath.repository.DaysOfWeekRepository;

@Service
public class DaysOfWeekService {

	@Autowired
	private DaysOfWeekRepository daysOfWeekRepo;
	
	/**
	 * Get all days of week
	 * 
	 * @return
	 */
	public List<DaysOfWeek> all() {
		return daysOfWeekRepo.findAll();
	}
	
	/**
	 * Get All Active DaysOfWeek
	 * 
	 * @return
	 */
	public List<DaysOfWeek> getAllActive() {
		return daysOfWeekRepo.findAllByIsActiveTrue();
	}
	
	/**
	 * Get All  Deleted DaysOfWeek
	 * 
	 * @return
	 */
	public List<DaysOfWeek> getAllDeletedDaysOfWeek() {
		return daysOfWeekRepo.findAllByIsActiveFalseAndIsDeleteTrue();
	}
	
	/**
	 * delete the record
	 * @param DaysOfWeekId
	 */
	public void deleteDaysOfWeek(Long DaysOfWeekId) {
		DaysOfWeek daysofweeek = daysOfWeekRepo.getOne(DaysOfWeekId);
		daysofweeek.setIsActive(false);
		daysofweeek.setIsDelete(true);
		daysOfWeekRepo.save(daysofweeek);
		return ;
	}

	/**
	 * restore the DaysOfWeek
	 * @param DaysOfWeekId
	 */
	public void restoreDaysOfWeek(Long DaysOfWeekId) {
		DaysOfWeek daysofweeek = daysOfWeekRepo.getOne(DaysOfWeekId);
		daysofweeek.setIsActive(true);
		daysofweeek.setIsDelete(false);
		daysOfWeekRepo.save(daysofweeek);
		return ;
	}

	/**
	 * Get daysOfWeek  by daysOfWeekId
	 * @param DaysOfWeekId
	 * @return
	 */
	public Object getDaysOfWeekData(Long DaysOfWeekId) {
		return daysOfWeekRepo.findById(DaysOfWeekId);
	}

	/**
	 *verify and save daysOfWeek 
	 * @param daysOfWeek
	 * @return
	 * @throws CPException 
	 */
	public DaysOfWeek verifyAndSave(DaysOfWeek daysOfWeek) throws CPException {
		boolean isExist = false;
		// If there is id that means record needs to be updated
		// hence we should ignore that record while checking for existence
		if (daysOfWeek.getId() != null && daysOfWeek.getId() > 0) {
			isExist = this.isExists(daysOfWeek.getName().trim(), daysOfWeek.getId());
		} 

		// If record is unique then save it else throw exception
		if (!isExist)
			this.save(daysOfWeek);
		else
			throw new CPException(ErrorStatus.DUPLICATE_RECORD);

		return daysOfWeek;
	}
	
	/**
	 * Check if DaysOfWeek exists for given name
	 * @param name
	 * @return
	 */
	public boolean isExists(String name) {
		return daysOfWeekRepo.countByNameIgnoreCase(name) > 0;
	}
	
	/**
	 * Check if DaysOfWeek exists for given name but ignore given id
	 * @param name
	 * @param ignoreId
	 * @return
	 */
	public boolean isExists(String name,Long ignoreId) {
		return daysOfWeekRepo.countByNameAndIgnoreId(name, ignoreId) > 0;
	}
	
	/**
	 * Create new daysOfWeek
	 * 
	 * @param event
	 * @return
	 */
	private DaysOfWeek save(DaysOfWeek daysOfWeek) {
		return daysOfWeekRepo.save(daysOfWeek);
	}
}
