import { Injectable } from '@angular/core';
import { Observable, Subject} from 'rxjs';
import { map, take } from 'rxjs/operators';
import { catchError } from 'rxjs/operators';

import  { APIClientService } from '../service/APIClientService';
import * as URL from '../constants/url';
import {CONSTANTS } from '../constants/constant';
import * as ROUTS from '../constants/routs';


declare var $: any;

@Injectable()
export class ManageNotificationService {
	
	constructor(public apiClientService : APIClientService) {}
	
	/**
	 * Adds the new Notification
	 */
	addNotification(notificationForm : any) : Observable<any> {
        
		 return this.apiClientService.doPost(URL.ADD_MANAGE_NOTIFICATION, notificationForm);
	 }
	
	/**
	 * Gets the list of all Notification
	 */
	getAllNotification() : Observable<any> {
		 return this.apiClientService.doGet(URL.GET_MANAGE_NOTIFICATION_LIST);
	 }
	
	/**
	 * Gets the list of all active Notification
	 */
	getAllActiveNotification() : Observable<any> {
        
		 return this.apiClientService.doGet(URL.GET_ACTIVE_MANAGE_NOTIFICATION);
	 }
	
	/**
	 * Gets the list of all delete Notification
	 */
	getAllDeleteNotification() : Observable<any> {
        
		 return this.apiClientService.doGet(URL.GET_DELETE_MANAGE_NOTIFICATION);
	 }
	
	/**
	 * Gets the data of single Notification by id
	 */
	getNotificationData(notificationId) : Observable<any> {
        
		 return this.apiClientService.doGet(URL.GET_MANAGE_NOTIFICATION_DATA + '/' + notificationId);
	 }
	
	/**
	  * Deletes the Notification by notificationId
	  */
	 deleteNotification(notificationId) : Observable<any> {
	        
		 	return this.apiClientService.doDelete(URL.DELETE_MANAGE_NOTIFICATION + '/' + notificationId);
	 }
	 
	 /**
	  * Restores the Notification by notificationId
	  */
	 restoreNotification(notificationId) : Observable<any> {
	        
		 	return this.apiClientService.doGet(URL.RESTORE_MANAGE_NOTIFICATION + '/' + notificationId);
	 }
	 
	
}