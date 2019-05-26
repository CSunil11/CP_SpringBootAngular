import { Injectable } from '@angular/core';
import { Observable, Subject} from 'rxjs';
import { map, take } from 'rxjs/operators';
import { catchError } from 'rxjs/operators';

import { CONSTANTS } from '../constants/constant';
import  { APIClientService } from '../service/APIClientService';
import * as ROUTS from '../constants/routs';
import * as URL from '../constants/url';

declare var $: any;

@Injectable()
export class DaysOfWeekService {
	
	constructor(public apiClientService : APIClientService) {}
	
	/**
	 * Gets the list of all days of week
	 */
	getAllDaysOfWeek() : Observable<any> {
        
        return this.apiClientService.doGet(URL.GET_DAYS_OF_WEEK_LIST);
	}
	
	/**
	 * Gets the data of single days of week by id
	 */
	getDaysOfWeekData(daysOfWeekId) : Observable<any> {
	        
	        return this.apiClientService.doGet(URL.GET_DAYS_OF_WEEK_DATA + '/' + daysOfWeekId);
	 }
	
	/**
	 * Gets the list of all active DaysOfWeek
	 */
	getAllActiveDaysOfWeek() : Observable<any> {
        
		 return this.apiClientService.doGet(URL.GET_ACTIVE_DAYS_OF_WEEK);
	 }
	
	/**
	 * Gets the list of all delete DaysOfWeek
	 */
	getAllDeleteDaysOfWeek() : Observable<any> {
        
		 return this.apiClientService.doGet(URL.GET_DELETE_DAYS_OF_WEEK);
	 }
	
	 /**
	  * Adds the edit days of week
	  */
	 addDaysOfWeek(editDaysOfWeekForm : any) : Observable<any> {
		 	return this.apiClientService.doPost(URL.ADD_DAYS_OF_WEEK, editDaysOfWeekForm);
	 }
	 
 	 /**
	  * Deletes the DaysOfWeek by DaysOfWeekId
	  */
	 deleteDaysOfWeek(daysOfWeekId) : Observable<any> {
	        
		 	return this.apiClientService.doDelete(URL.DELETE_DAYS_OF_WEEK  + '/' + daysOfWeekId);
	 }
	 
	 /**
	  * Restores the DaysOfWeek by DaysOfWeekId
	  */
	 restoreDaysOfWeek(daysOfWeekId) : Observable<any> {
	        
		 	return this.apiClientService.doGet(URL.RESTORE_DAYS_OF_WEEK + '/' + daysOfWeekId);
	 }
}
