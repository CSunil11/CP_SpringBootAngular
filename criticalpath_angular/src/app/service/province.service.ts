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
export class ProvinceService {
	
	constructor(public apiClientService : APIClientService) {}
	
	/**
	 * Adds the new country
	 */
	addProvince(provinceForm : any) : Observable<any> {
        
		 return this.apiClientService.doPost(URL.ADD_PROVINCE, provinceForm);
	 }
	
	/**
	 * Gets the list of all country
	 */
	getAllProvince() : Observable<any> {
		 return this.apiClientService.doGet(URL.GET_PROVINCE_LIST);
	 }
	
	/**
	 * Gets the list of all active province
	 */
	getAllActiveProvince() : Observable<any> {
        
		 return this.apiClientService.doGet(URL.GET_ACTIVE_PROVINCE);
	 }
	
	/**
	 * Gets the list of all delete country
	 */
	getAllDeleteProvince() : Observable<any> {
        
		 return this.apiClientService.doGet(URL.GET_DELETE_PROVINCE);
	 }
	
	/**
	 * Gets the data of single country by id
	 */
	getProvinceData(provinceId) : Observable<any> {
        
		 return this.apiClientService.doGet(URL.GET_PROVINCE_DATA + '/' + provinceId);
	 }
	
//	/**
//	 * Upload country data in bulk
//	 */
//	upload(formData) : Observable<any> {
//        
//		 return this.apiClientService.doPostForUplaoadData(URL.UPLOAD_COUNTRY_DATA, formData);
//	 }
	
	/**
	  * Deletes the country by countryId
	  */
	 deleteProvince(provinceId) : Observable<any> {
	        
		 	return this.apiClientService.doDelete(URL.DELETE_PROVINCE + '/' + provinceId);
	 }
	 
	 /**
	  * Restores the country by countryId
	  */
	 restoreProvince(provinceId) : Observable<any> {
	        
		 	return this.apiClientService.doGet(URL.RESTORE_PROVINCE + '/' + provinceId);
	 }
	 
	 /**
	  * Get province by country id
	  */
	 getProvinceOfCountry(countryId) : Observable<any> {
		 return this.apiClientService.doGet(URL.GET_PROVINCE_OF_COUNTRY + '/' + countryId);
	 }
	 
	 /**
	  * Gets the list of all active province by pagenumber
	  */
	 getAllActiveProvinceByPageNumber(pageNumber) {
		 return this.apiClientService.doGet(URL.GET_ACTIVE_PROVINCE + '/' + pageNumber);
	 }
	 
	 /**
	  * Gets the list of all deleted province by pagenumber
	  */
	 getAllDeletedProvinceByPageNumber(pageNumber) {
		 return this.apiClientService.doGet(URL.GET_DELETE_PROVINCE + '/' + pageNumber);
	 }
	 
	 /**
	  * Search active province on particular page
	  */
	 searchActiveProvinceOnPage(searchStr : string, pageNumber : number) {
		 return this.apiClientService.doGet(URL.SEARCH_ACTIVE_PROVINCE + '/' + searchStr + '/' + pageNumber);
	 }
	 
	 /**
	  * Search deleted province on particular page
	  */
	 searchDeletedProvinceOnPage(searchStr : string, pageNumber : number) {
		 return this.apiClientService.doGet(URL.SEARCH_DELETE_PROVINCE + '/' + searchStr + '/' + pageNumber);
	 }
}
