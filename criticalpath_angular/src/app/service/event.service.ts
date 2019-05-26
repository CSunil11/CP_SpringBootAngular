import { Injectable } from '@angular/core';
import { Observable, Subject} from 'rxjs';
import { map, take } from 'rxjs/operators';
import { catchError } from 'rxjs/operators';

import  { APIClientService } from '../service/APIClientService';
import * as URL from '../constants/url';
import { CONSTANTS } from '../constants/constant';
import * as ROUTS from '../constants/routs';


declare var $: any;

@Injectable()
export class EventService {
	
	constructor(public apiClientService : APIClientService) {}
	
	/**
	  * Adds the new event
	  */
	 addEvent(eventForm : any) : Observable<any> {
	        
		 	return this.apiClientService.doPost(URL.ADD_EVENT, eventForm);
	 }
	 
	 /**
	  * Gets the list of all event
	  */
	 getAllEvent() : Observable<any> {
	        
	        return this.apiClientService.doGet(URL.GET_EVENT_LIST);
	 }
	 
	 /**
	  * Gets the list of all active Event
	  */
	 getAllActiveEvent() {
		 return this.apiClientService.doGet(URL.GET_ACTIVE_EVENT);
	 }
	 
	 /**
	  * Gets the list of all delete Event
	  */
	 getAllDeleteEvent() {
		 return this.apiClientService.doGet(URL.GET_DELETE_EVENT);
	 }
	 
	 /**
	  * Gets the data of single event by id
	  */
	 getEventData(eventId) : Observable<any> {
	        
	        return this.apiClientService.doGet(URL.GET_EVENT_DATA + '/' + eventId);
	 }
	 
	 /**
	  * Deletes the event by eventId
	  */
	 deleteEvent(eventId) : Observable<any> {
	        
		 	return this.apiClientService.doDelete(URL.DELETE_EVENT + '/' + eventId);
	 }
	 
	 /**
	  * Restores the event by eventId
	  */
	 restoreEvent(eventId) : Observable<any> {
	        
		 	return this.apiClientService.doGet(URL.RESTORE_EVENT + '/' + eventId);
	 }
	 
	 	/**
		 * Upload event data in bulk
		 */
		upload(formData) : Observable<any> {
	        
			 return this.apiClientService.doPostForUplaoadData(URL.UPLOAD_EVENT_DATA, formData);
		 }
}