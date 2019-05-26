import { Injectable } from '@angular/core';
import { Cookie } from 'ng2-cookies';
import { Observable, Subject} from 'rxjs';
import { map, take } from 'rxjs/operators';
import { catchError } from 'rxjs/operators';

import  { APIClientService } from '../service/APIClientService';
import * as URL from '../constants/url';
import {CONSTANTS } from '../constants/constant';
import * as ROUTS from '../constants/routs';


declare var $: any;

@Injectable()
export class StoreService {
	
	constructor(public apiClientService : APIClientService) {}
	
	/**
	  * Adds the new store
	  */
	 addStore(storeForm : any) : Observable<any> {
	        
		 	return this.apiClientService.doPost(URL.ADD_STORE, storeForm);
	 }
	 
	 /**
	  * Gets the list of all store
	  */
	 getAllStore() : Observable<any> {
	        
	        return this.apiClientService.doGet(URL.GET_STORE_LIST);
	 }
	 
	 /**
	  * Gets the store by pagenumber
	  */
	 getStoreByPageNumber(pageNumber) : Observable<any> {
	        
	        return this.apiClientService.doGet(URL.GET_STORE_LIST + '/' + pageNumber);
	 }
	 
	 /**
	  * Gets the list of all active Store
	  */
	 getAllActiveStore() {
		 return this.apiClientService.doGet(URL.GET_ACTIVE_STORE);
	 }
	 
	 /**
	  * Gets the list of all active Store by pagenumber
	  */
	 getAllActiveStoreByPageNumber(pageNumber) {
		 return this.apiClientService.doGet(URL.GET_ACTIVE_STORE + '/' + pageNumber);
	 }
	 
	 /**
	  * Gets the list of all deleted Store by pagenumber
	  */
	 getAllDeletedStoreByPageNumber(pageNumber) {
		 return this.apiClientService.doGet(URL.GET_DELETE_STORE + '/' + pageNumber);
	 }
	 
	 
	 /**
	  * Gets the data of single store by id
	  */
	 getStoreData(storeId) : Observable<any> {
	        
	        return this.apiClientService.doGet(URL.GET_STORE_DATA + '/' + storeId);
	 }
	 
	 /**
	  * Deletes the store by storeId
	  */
	 deleteStore(storeId) : Observable<any> {
	        
		 	return this.apiClientService.doDelete(URL.DELETE_STORE + '/' + storeId);
	 }
	 
	 /**
	  * Restores the store by storeId
	  */
	 restoreStore(storeId) : Observable<any> {
	        
		 	return this.apiClientService.doGet(URL.RESTORE_STORE + '/' + storeId);
	 }
	 
	 /**
	  * Search store on particular page
	  */
	 searchStoreOnPage(searchStr : string, pageNumber : number) {
		 return this.apiClientService.doGet(URL.SEARCH_STORE + '/' + searchStr + '/' + pageNumber);
	 }
	 
	 /**
	  * Search active Store on particular page
	  */
	 searchActiveStoreOnPage(searchStr : string, pageNumber : number) {
		 return this.apiClientService.doGet(URL.SEARCH_ACTIVE_STORE + '/' + searchStr + '/' + pageNumber);
	 }
	 
	 /**
	  * Search deleted Store on particular page
	  */
	 searchDeletedStoreOnPage(searchStr : string, pageNumber : number) {
		 return this.apiClientService.doGet(URL.SEARCH_DELETE_STORE + '/' + searchStr + '/' + pageNumber);
	 }
	 
	 /**
	  * Get Store by brandId
	  */
	 getStoreByBrandId(brandId) {
		 return this.apiClientService.doGet(URL.GET_BY_BRAND + '/' + brandId);
	 }
}