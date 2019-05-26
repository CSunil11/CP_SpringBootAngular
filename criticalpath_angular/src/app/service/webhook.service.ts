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
export class WebhookService {
	
	constructor(public apiClientService : APIClientService) {}
	
	 /**
	  * Adds the new Webhhook
	  */
	 addWebhook(webhhookForm : any) : Observable<any> {
	        
		 	return this.apiClientService.doPost(URL.ADD_WEBHOOK, webhhookForm);
	 }
	 
	 /**
	  * Gets the list of all Webhhook
	  */
	 getAllWebhook() : Observable<any> {
	        
	        return this.apiClientService.doGet(URL.GET_WEBHOOK_LIST);
	 }
	 
	 /**
	  * Gets the list of all active Webhhook
	  */
	 getAllActiveWebhook() : Observable<any> {
	        
	        return this.apiClientService.doGet(URL.GET_ACTIVE_WEBHOOK);
	 }
	 
	 /**
	  * Gets the list of all deleted Webhhook
	  */
	 getAllDeletedWebhook() : Observable<any> {
	        
	        return this.apiClientService.doGet(URL.GET_DELETE_WEBHOOK);
	 }
	 
	 /**
	  * Gets the data of single Webhhook by id
	  */
	 getWebhookData(webhookId) : Observable<any> {
	        return this.apiClientService.doGet(URL.GET_WEBHOOK_DATA + '/' + webhookId);
	 }
	 
	 /**
	  * Deletes the Webhhook by webhookId
	  */
	 deleteWebhook(webhookId) : Observable<any> {
	        
		 	return this.apiClientService.doDelete(URL.DELETE_WEBHOOK + '/' + webhookId);
	 }
	 
	 /**
	  * Restores the Webhhook by webhookId
	  */
	 restoreWebhook(webhookId) : Observable<any> {
	        
		 	return this.apiClientService.doGet(URL.RESTORE_WEBHOOK + '/' + webhookId);
	 }
}