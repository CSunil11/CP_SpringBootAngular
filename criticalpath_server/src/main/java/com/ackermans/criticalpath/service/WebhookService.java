package com.ackermans.criticalpath.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ackermans.criticalpath.common.dto.CPException;
import com.ackermans.criticalpath.entity.Event;
import com.ackermans.criticalpath.entity.Webhook;
import com.ackermans.criticalpath.enums.ErrorStatus;
import com.ackermans.criticalpath.repository.EventRepository;
import com.ackermans.criticalpath.repository.WebhookRepositoy;
import com.ackermans.criticalpath.utils.Constants;

@Service
public class WebhookService {

	@Autowired
	private WebhookRepositoy webhookRepositoy;
	
	@Autowired
	private EventRepository eventRepository;
	
	private final Sort DEFAULT_SORT =  Sort.by("url");
	/**
	 * Verify and Save Webhook
	 * @param webhook
	 * @return
	 * @throws CPException
	 */
	public Webhook verifyAndSave(Webhook webhook) throws CPException {
		
		boolean isExist = false;
		
		// If there is id that means record needs to be updated
		// hence we should ignore that record while checking for existence
		if (webhook.getId() != null && webhook.getId() > 0) {
			isExist = this.isExists(webhook.getUrl(), webhook.getId());
		} else {
			isExist = this.isExists(webhook.getUrl());
		}

		// If record is unique then save it else throw exception
		if (!isExist) {
			Event event = eventRepository.findByNameIgnoreCase(webhook.getEvent().getName().toString());
			webhook.setEvent(event);
			this.save(webhook);
		}
		else
			throw new CPException(ErrorStatus.DUPLICATE_RECORD);

		return webhook;
	}
	
	/**
	 * Save Or Update Webhook
	 * 
	 * @param webhook
	 * @return
	 */
	private Webhook save(Webhook webhook) {
		return webhookRepositoy.save(webhook);
	}


	/**
	 * Check if Webhook exists for given url
	 * 
	 * @param url
	 * @return
	 */
	private boolean isExists(String url) {
		return webhookRepositoy.countByUrlIgnoreCase(url) > 0;
	}

	/**
	 * Check if Webhook exists for given url but ignore given id
	 * 
	 * @param url
	 * @return
	 */
	private boolean isExists(String url, Long Id) {
		return webhookRepositoy.countByUrlAndIgnoreId(url, Id) > 0;
	}
	
	/**
	 * Get all Webhook
	 * @return
	 */
	public List<Webhook> getAll(){
		return webhookRepositoy.findAll();
	}
	
	/**
	 * Get List By PageNumber and PageSize
	 * 
	 * @param pageNumber
	 * @return
	 */
	public Page<Webhook> getAll(int pageNumber) {
		Pageable pageable = PageRequest.of(pageNumber, Constants.PAGE_SIZE, DEFAULT_SORT);
		return webhookRepositoy.findAll(pageable);
	}
	
	/**
	 * Get All Active Webhook List
	 * 
	 * @return
	 */
	public List<Webhook> getAllActive() {
		return webhookRepositoy.findAllByIsActiveTrue();
	}
	
	/**
	 * Get All delete Webhook List
	 * 
	 * @return
	 */
	public List<Webhook> getAllDeletedWebhook() {
		return webhookRepositoy.findAllByIsActiveFalseAndIsDeleteTrue();
	}
	
	/**
	 * Get All Active With Fix Size Row
	 * 
	 * @param pageNumber
	 * @return
	 */
	public Page<Webhook> getAllActive(int pageNumber) {
		Pageable pageable = PageRequest.of(pageNumber, Constants.PAGE_SIZE, DEFAULT_SORT);
		return webhookRepositoy.findAllByIsActiveTrue(pageable);
	}
	
	/**
	 * Activate Webhook by id
	 * 
	 * @param id
	 * @return
	 */
	public Webhook activate(Long id) {
		Webhook webhook = webhookRepositoy.getOne(id);
		webhook.setIsActive(true);
		return webhookRepositoy.save(webhook);
	}
	
	/**
	 * Deactivate Webhook by id
	 * 
	 * @param id
	 * @return
	 */
	public Webhook deactivate(Long id) {
		Webhook webhook = webhookRepositoy.getOne(id);
		webhook.setIsActive(false);
		return webhookRepositoy.save(webhook);
	}
	
	/**
	 * Search by URL starting with given search keyword
	 *
	 * @return
	 */
	public List<Webhook> search(String searchKeyword) {
		return webhookRepositoy.findByUrlIgnoreCaseStartingWith(searchKeyword);
	}

	/**
	 * Get Webhook By Event ID
	 * @param eventId
	 * @return
	 */
	public List<Webhook> getByEvent(Long eventId) {
		// TODO Auto-generated method stub
		return webhookRepositoy.findByEvent_Id(eventId);
	}
	
	/**
	 * Get Webhook by Webhook ID
	 * @param storeId
	 * @return
	 */
	public Optional<Webhook> get(Long webhookId) {
		return webhookRepositoy.findById(webhookId);
	}

	/**
	 * Deletes the record
	 * @param eventId
	 * @return
	 */
	public void deleteWebhook(Long webhookId) {
		Webhook webhook = webhookRepositoy.getOne(webhookId);
		webhook.setIsActive(false);
		webhook.setIsDelete(true);
		webhookRepositoy.save(webhook);
		return ;
	}
	
	/**
	 * Restores the record
	 * @param eventId
	 */
	public void restoreWebhook(Long webhookId) {
		Webhook webhook = webhookRepositoy.getOne(webhookId);
		webhook.setIsActive(true);
		webhook.setIsDelete(false);
		webhookRepositoy.save(webhook);
		return ;
	}
}
