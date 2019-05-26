package com.ackermans.criticalpath.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ackermans.criticalpath.common.dto.CPException;
import com.ackermans.criticalpath.entity.Country;
import com.ackermans.criticalpath.entity.ManageNotification;
import com.ackermans.criticalpath.entity.Province;
import com.ackermans.criticalpath.entity.Store;
import com.ackermans.criticalpath.enums.ErrorStatus;
import com.ackermans.criticalpath.repository.ManageNotificationRepository;

@Service
public class ManageNotificationService {
	
	private static final Logger logger = LogManager.getFormatterLogger(ManageNotificationService.class);

	@Autowired
	private ManageNotificationRepository manageNotificationRepository;

	/**
	 * Get All ManageNotification
	 * 
	 * @return
	 */
	public List<ManageNotification> getAll() {
		return manageNotificationRepository.findAll();
	}
	
	/**
	 * Get All Active ManageNotification
	 * 
	 * @return
	 */
	public List<ManageNotification> getAllActive() {
		return manageNotificationRepository.findAllByIsActiveTrue();
	}
	
	/**
	 * Get All  Delete ManageNotification
	 * 
	 * @return
	 */
	public List<ManageNotification> getAllDeletedManageNotification() {
		return manageNotificationRepository.findAllByIsActiveFalseAndIsDeleteTrue();
	}
	
	/**
	 * Deletes the record
	 * @param ManageNotificationId
	 * @return
	 */
	public void deleteManageNotification(Long ManageNotificationId) {
		ManageNotification manageNotification = manageNotificationRepository.getOne(ManageNotificationId);
		manageNotification.setIsActive(false);
		manageNotification.setIsDelete(true);
		manageNotificationRepository.save(manageNotification);
		return ;
	}

	/**
	 * Restores the record
	 * @param ManageNotificationId
	 */
	public void restoreManageNotification(Long ManageNotificationId) {
		ManageNotification manageNotification = manageNotificationRepository.getOne(ManageNotificationId);
		manageNotification.setIsActive(true);
		manageNotification.setIsDelete(false);
		manageNotificationRepository.save(manageNotification);
		return ;
	}
	
	/**
	 * Active ManageNotification By Id
	 * 
	 * @param ManageNotification
	 * @return
	 */
	public ManageNotification activate(Long ManageNotificationId) {
		ManageNotification manageNotification = manageNotificationRepository.getOne(ManageNotificationId);
		manageNotification.setIsActive(true);
		manageNotification.setIsDelete(false);
		return manageNotificationRepository.save(manageNotification);
	}
	
	/**
	 * DeActive ManageNotification By Id
	 * 
	 * @param ManageNotification
	 * @return
	 */
	public ManageNotification deactivate(Long ManageNotificationId) {
		ManageNotification manageNotification = manageNotificationRepository.getOne(ManageNotificationId);
		manageNotification.setIsActive(false);
		manageNotification.setIsDelete(true);
		return manageNotificationRepository.save(manageNotification);
	}
	
	public ManageNotification verifyAndSave(ManageNotification manageNotification) throws CPException {
		boolean isExist = false;

		// If there is id that means record needs to be updated
		// hence we should ignore that record while checking for existence
		if (manageNotification.getId() != null && manageNotification.getId() > 0) {
			isExist = this.isExists(manageNotification.getName(), manageNotification.getId());
		} else {
			isExist = this.isExists(manageNotification.getName());
		}

		// If record is unique then save it else throw exception
		if (!isExist)
			this.save(manageNotification);
		else
			throw new CPException(ErrorStatus.DUPLICATE_RECORD);
		
		return manageNotification;
	}
	
	/**
	 * Create new ManageNotification
	 * 
	 * @param manageNotification
	 * @return
	 */
	private ManageNotification save(ManageNotification manageNotification) {
		return manageNotificationRepository.save(manageNotification);
	}
	
	/**
	 * Check if ManageNotification exists for given name
	 * 
	 * @param name
	 * @return
	 */
	public boolean isExists(String name) {
		return manageNotificationRepository.countByNameIgnoreCase(name) > 0;
	}
	
	/**
	 * Check if Province exists for given name but ignore given id
	 * 
	 * @param name
	 * @return
	 */
	public boolean isExists(String name,Long ignoreId) {
		return manageNotificationRepository.countByNameAndIgnoreId(name, ignoreId) > 0;
	}
	
	/**
	 * Get Country By countryId
	 * 
	 * @param countryId
	 * @return
	 */
	public Object getManageNotificationData(Long ManageNotificationId) {
		return manageNotificationRepository.findById(ManageNotificationId);
	}

}
