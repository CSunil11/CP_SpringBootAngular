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
export class LocationDivisionService {
	
	constructor(public apiClientService : APIClientService) {}
	
	/**
	 * Adds the new country
	 */
	addLocationDivision(locationDivisionForm : any) : Observable<any> {
        
		 return this.apiClientService.doPost(URL.ADD_LOCATIONDIVISION, locationDivisionForm);
	 }
	
	/**
	 * Gets the list of all country
	 */
	getAllLocationDivision() : Observable<any> {
		 return this.apiClientService.doGet(URL.GET_LOCATIONDIVISION_LIST);
	 }
	
	/**
	 * Gets the list of all active LocationDivision
	 */
	getAllActiveLocationDivision() : Observable<any> {
        
		 return this.apiClientService.doGet(URL.GET_ACTIVE_LOCATIONDIVISION);
	 }
	
	/**
	 * Gets the list of all delete LocationDivision
	 */
	getAllDeleteLocationDivision() : Observable<any> {
        
		 return this.apiClientService.doGet(URL.GET_DELETE_LOCATIONDIVISION);
	 }
	
	/**
	 * Gets the data of single country by id
	 */
	getLocationDivisionData(locationDivisionId) : Observable<any> {
        
		 return this.apiClientService.doGet(URL.GET_LOCATIONDIVISION_DATA + '/' + locationDivisionId);
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
	 deleteLocationDivision(locationDivisionId) : Observable<any> {
	        
		 	return this.apiClientService.doDelete(URL.DELETE_LOCATIONDIVISION + '/' + locationDivisionId);
	 }
	 
	 /**
	  * Restores the country by countryId
	  */
	 restoreLocationDivision(locationDivisionId) : Observable<any> {
	        
		 	return this.apiClientService.doGet(URL.RESTORE_LOCATIONDIVISION + '/' + locationDivisionId);
	}
	 
	 
	 /**
	  * Get locationDivision by province id
	  */
	 getLocationDivisionOfProvince(provinceId) : Observable<any> {
		 return this.apiClientService.doGet(URL.GET_LOCATIONDIVISION_OF_PROVINCE + '/' + provinceId);
	 }
	 
	 /**
	  * Gets the list of all active LocDiv by pagenumber
	  */
	 getAllActiveLocDivByPageNumber(pageNumber) {
		 return this.apiClientService.doGet(URL.GET_ACTIVE_LOCATIONDIVISION + '/' + pageNumber);
	 }
	 
	 /**
	  * Search active LocDiv on particular page
	  */
	 searchActiveLocDivOnPage(searchStr : string, pageNumber : number) {
		 return this.apiClientService.doGet(URL.SEARCH_ACTIVE_LOC_DIV + '/' + searchStr + '/' + pageNumber);
	 }
	 
	 /**
	  * Search deleted LocDiv on particular page
	  */
	 searchDeletedLocDivOnPage(searchStr : string, pageNumber : number) {
		 return this.apiClientService.doGet(URL.SEARCH_DELETE_LOC_DIV + '/' + searchStr + '/' + pageNumber);
	 }
	 
	 /**
	  * Gets the list of all deleted LocDiv by pagenumber
	  */
	 getAllDeletedLocDivByPageNumber(pageNumber) {
		 return this.apiClientService.doGet(URL.GET_DELETE_LOCATIONDIVISION + '/' + pageNumber);
	 }
}