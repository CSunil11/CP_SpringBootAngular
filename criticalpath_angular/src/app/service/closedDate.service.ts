import { Injectable } from '@angular/core';
import { Observable, Subject} from 'rxjs';
import { map, take } from 'rxjs/operators';
import { catchError } from 'rxjs/operators';

import {CONSTANTS } from '../constants/constant';
import  { APIClientService } from '../service/APIClientService';
import * as ROUTS from '../constants/routs';
import * as URL from '../constants/url';

declare var $: any;

@Injectable()
export class ClosedDateService {
	
	constructor(public apiClientService : APIClientService) {}
	
	 /**
	  * Adds the new closedDate
	  */
	 addClosedDate(storeCloseDateRequest : any) : Observable<any> {
	        
		 	return this.apiClientService.doPost(URL.ADD_CLOSED_DATE, storeCloseDateRequest);
	 }
	 
	 /**
	  * Gets the list of all closedDate
	  */
	 getAllClosedDate() : Observable<any> {
	        
	        return this.apiClientService.doGet(URL.GET_CLOSED_DATE_LIST);
	 }
	 
	 /**
	  * Gets the list of all active ClosedDate
	  */
	 getAllActiveClosedDate() {
		 return this.apiClientService.doGet(URL.GET_ACTIVE_CLOSED_DATE);
	 }
	 
	 /**
	  * Gets the list of all deleted ClosedDate
	  */
	 getAllDeletedClosedDate() {
		 return this.apiClientService.doGet(URL.GET_DELETE_CLOSED_DATE);
	 }
	 
	 /**
	  * Gets the data of single closedDate by id
	  */
	 getClosedDateData(closedDateId) : Observable<any> {
	        
	        return this.apiClientService.doGet(URL.GET_CLOSED_DATE_DATA + '/' + closedDateId);
	 }
	 
	 /**
	  * Gets the data of  closedDate by storeId
	  */
	 getClosedDateByStore(storeId) : Observable<any> {
	        
	        return this.apiClientService.doGet(URL.GET_CLOSED_DATE_BY_STORE + '/' + storeId);
	 }
	 
	 /**
	  * Gets the data of  closedDate by storeId with date
	  */
	 getClosedDateByStoreDate(storeId , newdate ,stokedate) : Observable<any> {
	        
	        return this.apiClientService.doGet(URL.GET_CLOSED_DATE_BY_STORE + '/' + storeId + '/' + newdate + '/' + stokedate);
	 }
	 
	 /**
	  * Gets the data of  closedDate by storeId with date
	  */
	 getClosedDateListByStoreDate(storeId , newdate ,stokedate) : Observable<any> {
	        
	        return this.apiClientService.doGet(URL.GET_CLOSED_DATE_LIST_BY_STORE + '/' + storeId + '/' + newdate + '/' + stokedate);
	 }
	 
	 
	 /**
	  * Deletes the closedDate by closedDateId
	  */
	 deleteClosedDate(closeDateId) : Observable<any> {
	        
		 	return this.apiClientService.doDelete(URL.DELETE_CLOSED_DATE + '/' + closeDateId);
	 }
	 
	 /**
	  * Restores the closedDate by closedDateId
	  */
	 restoreClosedDate(closeDateId) : Observable<any> {
	        
		 	return this.apiClientService.doGet(URL.RESTORE_CLOSED_DATE + '/' + closeDateId);
	 }
}