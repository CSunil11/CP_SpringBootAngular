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
export class TaskStatusService {
	
	constructor(public apiClientService : APIClientService) {}
	
	/**
	 * Adds the task status
	 */
	addTaskStatus(taskStatusForm) : Observable<any> {
        
        return this.apiClientService.doPost(URL.ADD_TASK_STATUS, taskStatusForm);
	}
	
	/**
	 * Get all the task status
	 */
	getAllTaskStatus() : Observable<any> {
        
        return this.apiClientService.doGet(URL.GET_TASK_STATUS_LIST);
	}
 
	/**
	 * Get all the active task status
	 */
	getAllActiveTaskStatus() : Observable<any> {
        
        return this.apiClientService.doGet(URL.GET_ACTIVE_TASK_STATUS_LIST);
	}
	
	/**
	 * Get all the Delete task status
	 */
	getAllDeleteTaskStatus() : Observable<any> {
        
        return this.apiClientService.doGet(URL.GET_DELETE_TASK_STATUS_LIST);
	}
	
	/**
	 * Get the task status data
	 */
 	getTaskStatusData(taskStatusId) : Observable<any> {
        
        return this.apiClientService.doGet(URL.GET_TASK_STATUS_DATA + '/' + taskStatusId);
 	}
 	
 	/**
	 * Upload task data in bulk
	 */
	upload(formData) : Observable<any> {
        
		 return this.apiClientService.doPostForUplaoadData(URL.UPLOAD_TASK_DATA, formData);
	 }
	
	/**
	  * Deletes the TaskStatus by taskStatusId
	  */
	 deleteTaskStatus(taskStatusId) : Observable<any> {
	        
		 	return this.apiClientService.doDelete(URL.DELETE_TASK_STATUS + '/' + taskStatusId);
	 }
	 
	 /**
	  * Restores the TaskStatus by taskStatusId
	  */
	 restoreTaskStatus(taskStatusId) : Observable<any> {
	        
		 	return this.apiClientService.doGet(URL.RESTORE_TASK_STATUS + '/' + taskStatusId);
	 }
}