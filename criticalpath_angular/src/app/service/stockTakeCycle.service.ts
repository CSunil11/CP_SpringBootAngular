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
export class StockTakeCycleService {
	
	constructor(public apiClientService : APIClientService) {}
	
	/**
	  * Adds the new stockTakeCycle
	  */
	 addStockTakeCycle(stockTakeCycleForm : any) : Observable<any> {
	        
		 	return this.apiClientService.doPost(URL.ADD_STOCK_TAKE_CYCLE, stockTakeCycleForm);
	 }
	 
	 /**
	  * Gets the list of all stockTakeCycle
	  */
	 getAllStockTakeCycle() : Observable<any> {
	        
	        return this.apiClientService.doGet(URL.GET_STOCK_TAKE_CYCLE_LIST);
	 }
	 
	 /**
	  * Gets the list of all active StockTakeCycle
	  */
	 getAllActiveStockTakeCycle() {
		 return this.apiClientService.doGet(URL.GET_ACTIVE_STOCK_TAKE_CYCLE);
	 }
	 
	 /**
	  * Gets the list of all Deleted StockTakeCycle
	  */
	 getAllDeletedStockTakeCycle() {
		 return this.apiClientService.doGet(URL.GET_DELETE_STOCK_TAKE_CYCLE);
	 }
	 
	 /**
	  * Gets the data of single stockTakeCycle by id
	  */
	 getStockTakeCycleData(stockTakeCycleId) : Observable<any> {
	        
	        return this.apiClientService.doGet(URL.GET_STOCK_TAKE_CYCLE_DATA + '/' + stockTakeCycleId);
	 }
	 
	 /**
	  * Gets the data of single AckstockTakeCycle by id
	  */
	 getAckStockTakeCycleData(stockTakeCycleId) : Observable<any> {
	        
	        return this.apiClientService.doGet(URL.GET_ACK_STOCK_TAKE_CYCLE_DATA + '/' + stockTakeCycleId);
	 }
	 
	 /**
	  * Gets the list of all active StockTakeCycle along with Store name
	  */
	 getAllActiveStockTakeCyclePerStore() {
		 return this.apiClientService.doGet(URL.GET_ACTIVE_STOCK_TAKE_CYCLE_PER_STORE);
	 }
	 
	 /**
	  * Gets the list of active StockTakeCycles per store name
	  */
	 getStockTakeCyclePerStore(storeId){
		 return this.apiClientService.doGet(URL.GET_STOCK_TAKE_CYCLE_PER_STORE + '/' + storeId);
	 }
	 
	 /**
	  * Gets the list of active StockTakeCycles status as per store name
	  */
	 getStockTakeCycleStatusPerStore(storeId, ls){
		 return this.apiClientService.doPost(URL.GET_STOCK_TAKE_CYCLE_STATUS_PER_STORE, storeId + '/' + ls);
	 }
	 
	 /**
	  * Gets the list of active StockTakeCycles per store name
	  */
	 getAllActiveStockTakeCycleDsmUser(userId, role, sortBy) : Observable<any> {
		 return this.apiClientService.doGet(URL.GET_STOCK_TAKE_CYCLE_DSM_USER + '/' + userId + '/' + role + '/' + sortBy );
	 }
	 
	 /**
	  * Gets the list of active AckStockTakeCycles per store name
	  */
	 getAllActiveAckStockTakeCycleDsmUser(userId, role, sortBy, storeId) : Observable<any> {
		 return this.apiClientService.doGet(URL.GET_ACK_STOCK_TAKE_CYCLE_DSM_USER + '/' + userId + '/' + role + '/' + sortBy + '/' + storeId);
	 }
	 
	 /**
	  * Gets the list of active StockTakeCycles status per store name
	  */
	 getAllActiveStockTakeCycleStatusDsmUser(userId, role, sortBy, ls) : Observable<any> {
		 return this.apiClientService.doPost(URL.GET_STOCK_TAKE_CYCLE_STATUS_DSM_USER ,userId + '/' + role + '/' + sortBy + '/' + ls);
	 }
	 
	 /**
	  * Gets the list of active AckStockTakeCycles status per store name
	  */
	 getAllActiveAckStockTakeCycleStatusDsmUser(userId, role, sortBy, ls) : Observable<any> {
		 return this.apiClientService.doPost(URL.GET_ACK_STOCK_TAKE_CYCLE_STATUS_DSM_USER ,userId + '/' + role + '/' + sortBy + '/' + ls);
	 }
	 
	 /**
	  * Gets the  active StockTakeCycles by userId
	  */
	 getStockTakeCycleByUserId(userId) : Observable<any> {
		 return this.apiClientService.doGet(URL.GET_STOCK_TAKE_CYCLE_BY_USER + '/' + userId );
	 }

	 /**
	  * Gets the list of active Stores per DSM User
	  */
	 getAllActiveStoresPerDsmUser(id, role) : Observable<any> {
		 return this.apiClientService.doGet(URL.GET_STORE_PER_DSM_USER + '/' + id + '/ ' + role );
	 }
	 
	 /**
	  * Deletes the stockTakeCycle by stockTakeCycleId
	  */
	 deleteStockTakeCycle(stockTakeCycleId) : Observable<any> {
	        
		 	return this.apiClientService.doDelete(URL.DELETE_STOCK_TAKE_CYCLE + '/' + stockTakeCycleId);
	 }

	 /**
	  * hides the stockTakeCycle by stockTakeCycleId
	  */
	 hideStockTakeCycleinAckerman(stockTakeCycleId) : Observable<any> {
	        
		 	return this.apiClientService.doDelete(URL.HIDE_STOCK_TAKE_CYCLE + '/' + stockTakeCycleId);
	 }
	 
	 /**
	  * Restores the stockTakeCycle by stockTakeCycleId
	  */
	 restoreStockTakeCycle(stockTakeCycleId) : Observable<any> {
	        
		 	return this.apiClientService.doGet(URL.RESTORE_STOCK_TAKE_CYCLE + '/' + stockTakeCycleId);
	 }
	 
	 /**
	  * save stokeTakeDate in stokeTakeCycle
	  */
	 addStockTakeDate(stockTakeCycleId , stockTakeDate) : Observable<any> {
	        
		 	return this.apiClientService.doGet(URL.ADD_STOCK_TAKE_DATE + '/' + stockTakeCycleId + '/' + stockTakeDate);
	 }

	 /**
	  * Gets the  active StockTakeCycles by userId
	  */
	 isActiveCycleAvail(userId) : Observable<any> {
		 return this.apiClientService.doGet(URL.IS_ACTIVE_CYCLE_AVAIL_ACK + '/' + userId );
	 }
	 
	 /**
	  * add the data of  stockTakeCycle in ack cycle
	  */
	 addAckCycle(stockTakeCycleId , startDate , stockTakeDate , storeId, time) : Observable<any> {
	        return this.apiClientService.doGet(URL.ADD_CYCLE_ACK + '/' + stockTakeCycleId +'/'+ startDate + '/' + stockTakeDate +'/'+ storeId +'/'+ time);
	 }
	 
	 calculateStartDt(dataList:any) : Observable<any> {
	        return this.apiClientService.doPost(URL.CALCULATE_STARTDATE ,dataList);
	 }
	 
	 getUpcomingCycleStartDate(storeId) : Observable<any> {
	        return this.apiClientService.doGet(URL.GET_UPCOMING_ACK_CYCLE_STARTDATE + '/'+ storeId);
	 }
	 
	 getAllDateList(stockTakeCycleId , stockTakeDate) : Observable<any> {
		 console.log(stockTakeDate);
	        return this.apiClientService.doGet(URL.GET_ALL_DATELIST + '/' + stockTakeCycleId + '/' + stockTakeDate );
	 }
	 
	 
	 calculateMultipleStartDate(selectedStoreList:any) : Observable<any> {
	        return this.apiClientService.doPost(URL.CALCULATE_MULTIPLE_STARTDATE ,selectedStoreList);
	 }
	 
	 addMultipleStartDate(selectedStoreList:any) : Observable<any> {
	        return this.apiClientService.doPost(URL.ADD_MULTIPLE_STARTDATE ,selectedStoreList);
	 }
	 
	 deleteAndCreateNewStockTake(stockTakeJson : any) : Observable<any> {
		 return this.apiClientService.doPost(URL.DELETE_AND_CREATE_NEW_STOCK_TAKE, stockTakeJson);
	 }
	 
	 editNewStockTake(editStockTakeJson : any) : Observable<any> {
		 return this.apiClientService.doPost(URL.EDIT_NEW_STOCK_TAKE, editStockTakeJson);
	 }
	 
	 getStoreStockTakeDate(selectedStoreList:any) : Observable<any> {
	        return this.apiClientService.doPost(URL.GET_STORE_STOCK_TAKE_DATE ,selectedStoreList);
	 }
}