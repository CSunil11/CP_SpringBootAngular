import { Injectable } from '@angular/core';
import { Observable, Subject} from 'rxjs';
import { map, take } from 'rxjs/operators';
import { catchError } from 'rxjs/operators';

import  { APIClientService } from '../service/APIClientService';
import * as URL from '../constants/url';
import * as ROUTS from '../constants/routs';
import { CONSTANTS } from '../constants/constant';

declare var $: any;

@Injectable()
export class CriticalPathService {
	
	constructor(public apiClientService : APIClientService) {}
	
	  /**
	  * Adds the new criticalPath
	  */
	 addCriticalPath(criticalPathForm : any) : Observable<any> {
	        
		 	return this.apiClientService.doPost(URL.ADD_CRITICAL_PATH, criticalPathForm);
	 }
	 
	 /**
	  * Gets the list of all criticalPath
	  */
	 getAllCriticalPath() : Observable<any> {
	        
	        return this.apiClientService.doGet(URL.GET_CRITICAL_PATH_LIST);
	 }
	 
	 /**
	  * Gets the list of all active CriticalPath
	  */
	 getAllActiveCriticalPath() {
		 return this.apiClientService.doGet(URL.GET_ACTIVE_CRITICAL_PATH);
	 }
	 
	 /**
	  * Gets the list of all deleted CriticalPath
	  */
	 getAllDeletedCriticalPath() {
		 return this.apiClientService.doGet(URL.GET_DELETE_CRITICAL_PATH);
	 }
	 
	 /**
	  * Gets the data of single criticalPath by id
	  */
	 getCriticalPathData(criticalPathId) : Observable<any> {
	        
	        return this.apiClientService.doGet(URL.GET_CRITICAL_PATH_DATA + '/' + criticalPathId);
	 }
	 
	 /**
	  * Deletes the criticalPath by criticalPathId
	  */
	 deleteCriticalPath(criticalPathId) : Observable<any> {
	        
		 	return this.apiClientService.doDelete(URL.DELETE_CRITICAL_PATH + '/' + criticalPathId);
	 }
	 
	 /**
	  * Restores the criticalPath by criticalPathId
	  */
	 restoreCriticalPath(criticalPathId) : Observable<any> {
	        
		 	return this.apiClientService.doGet(URL.RESTORE_CRITICAL_PATH + '/' + criticalPathId);
	 }
	 
	 /**
	  * Gets the data of criticalPath by stockTakeCycleId
	  */
	 getCriticalPathOfStockTakeCycle(stockTakeCycleId) : Observable<any> {
	        
	        return this.apiClientService.doGet(URL.GET_CRITICAL_PATH_DATA_OF_CYCLE + '/' + stockTakeCycleId);
	 }
	 
	 /**
	  * Gets the data of AckcriticalPath by stockTakeCycleId
	  */
	 getCriticalPathOfAckStockTakeCycle(stockTakeCycleId) : Observable<any> {
	        
	        return this.apiClientService.doGet(URL.GET_CRITICAL_PATH_DATA_OF_ACKCYCLE + '/' + stockTakeCycleId);
	 }
	 
	 /**
	  * update task status information ackermanpage
	  */
	 updateTaskStatus(criticalPathJson : any) : Observable<any> {
	        
			 return this.apiClientService.doPost(URL.UPDATE_CRITICAL_PATH_STATUS , criticalPathJson);
	 }
	 
	 /**
	  * update Acktask status information ackermanpage
	  */
	 updateAckTaskStatus(criticalPathJson : any) : Observable<any> {
	        
			 return this.apiClientService.doPost(URL.UPDATE_ACK_CRITICAL_PATH_STATUS , criticalPathJson);
	 }
	 
	 /** 
	  * Gets the data of StockTakeResult by StoreId
	  */
	 getStockTakeResult(storeId) : Observable<any> {
	        
	        return this.apiClientService.doGet(URL.GET_STOCK_TAKE_RESULT + '/' + storeId);
	 }
	 
	 /** 
	  * set the status of approve or decline
	  */
	 saveApproveStatus(resultId ,reasonForDecline,status ,userRole) : Observable<any> {
	        
	        return this.apiClientService.doGet(URL.UPDATE_SM_STATUS_OF_STOCK_TAKE_RESULT + '/' + resultId + '/' + reasonForDecline + '/' + status + '/' + userRole);
	 }
	 
	 /**
	  * update ram decline status information stockTakeResult
	  */
	 saveRamDeclineStatus(ramDeclineJson : any) : Observable<any> {
	        
			 return this.apiClientService.doPost(URL.UPDATE_STATUS_DECLINE_BY_RAM , ramDeclineJson);
	 }
}