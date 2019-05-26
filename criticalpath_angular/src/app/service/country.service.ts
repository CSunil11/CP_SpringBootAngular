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
export class CountryService {
	
	constructor(public apiClientService : APIClientService) {}
	
	/**
	 * Adds the new country
	 */
	addCountry(countryForm : any) : Observable<any> {
        
		 return this.apiClientService.doPost(URL.ADD_COUNTRY, countryForm);
	 }
	
	/**
	 * Gets the list of all country
	 */
	getAllCountry() : Observable<any> {
        
		 return this.apiClientService.doGet(URL.GET_COUNTRY_LIST);
	 }
	
	/**
	 * Gets the list of all active country
	 */
	getAllActiveCountry() : Observable<any> {
        
		 return this.apiClientService.doGet(URL.GET_ACTIVE_COUNTRY);
	 }
	
	/**
	 * Gets the list of all delete country
	 */
	getAllDeleteCountry() : Observable<any> {
        
		 return this.apiClientService.doGet(URL.GET_DELETE_COUNTRY);
	 }
	
	/**
	 * Gets the data of single country by id
	 */
	getCountryData(countryId) : Observable<any> {
        
		 return this.apiClientService.doGet(URL.GET_COUNTRY_DATA + '/' + countryId);
	 }
	
	/**
	 * Upload country data in bulk
	 */
	upload(formData) : Observable<any> {
        
		 return this.apiClientService.doPostForUplaoadData(URL.UPLOAD_COUNTRY_DATA, formData);
	 }
	
	/**
	  * Deletes the country by countryId
	  */
	 deleteCountry(countryId) : Observable<any> {
	        
		 	return this.apiClientService.doDelete(URL.DELETE_COUNTRY + '/' + countryId);
	 }
	 
	 /**
	  * Restores the country by countryId
	  */
	 restoreCountry(countryId) : Observable<any> {
	        
		 	return this.apiClientService.doGet(URL.RESTORE_COUNTRY + '/' + countryId);
	 }
	 
	 /**
	  * Gets the list of all active country by pagenumber
	  */
	 getAllActiveCountryByPageNumber(pageNumber) {
		 return this.apiClientService.doGet(URL.GET_ACTIVE_COUNTRY + '/' + pageNumber);
	 }
	 
	 /**
	  * Gets the list of all deleted country by pagenumber
	  */
	 getAllDeletedCountryByPageNumber(pageNumber) {
		 return this.apiClientService.doGet(URL.GET_DELETE_COUNTRY + '/' + pageNumber);
	 }
	 
	 /**
	  * Search active province on particular page
	  */
	 searchActiveCountryOnPage(searchStr : string, pageNumber : number) {
		 return this.apiClientService.doGet(URL.SEARCH_ACTIVE_COUNTRY + '/' + searchStr + '/' + pageNumber);
	 }
	 
	 /**
	  * Search deleted province on particular page
	  */
	 searchDeletedCountryOnPage(searchStr : string, pageNumber : number) {
		 return this.apiClientService.doGet(URL.SEARCH_DELETE_COUNTRY + '/' + searchStr + '/' + pageNumber);
	 }
}