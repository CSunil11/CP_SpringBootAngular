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
export class BrandUserService {
	
	constructor(public apiClientService : APIClientService) {}
	
	/**
	  * Adds the new BrandUser
	  */
	 addBrandUser(brandUserRequest : any) : Observable<any> {
	        
		 	return this.apiClientService.doPost(URL.ADD_BRAND_USER, brandUserRequest);
	 }
	 
	 /**
	  * Gets the list of all BrandUser
	  */
	 getAllByBrand(brandId : number, pageNumber : number) : Observable<any> {
	        
	        return this.apiClientService.doGet(URL.GET_ALL_BY_BRAND + '/' + brandId + '/' + pageNumber);
	 }
	 
	 /**
	  * Gets the BrandUser by pagenumber
	  */
	 getBrandUserByPageNumber(pageNumber) : Observable<any> {
	        
	        return this.apiClientService.doGet(URL.GET_BRAND_USER_LIST + '/' + pageNumber);
	 }
	 
	 /**
	  * Gets the list of all active BrandUser
	  */
	 getAllActiveBrandUser(brandId : number, pageNumber : number ,searchRole : any) {
		 return this.apiClientService.doGet(URL.GET_ALL_ACTIVE_BRAND_USER + '/' + brandId + '/' + pageNumber + '/' + searchRole);
	 }
	 
	 /**
	  * Gets the list of all deleted BrandUser
	  */
	 getAllDeletedBrandUser(brandId : number, pageNumber : number, searchRole : any) {
		 return this.apiClientService.doGet(URL.GET_ALL_DELETE_BRAND_USER + '/' + brandId + '/' + pageNumber + '/' + searchRole);
	 }
	 
	 /**
	  * Gets the data of single BrandUser by id
	  */
	 getBrandUserData(brandUserId) : Observable<any> {
	        
	        return this.apiClientService.doGet(URL.GET_BRAND_USER_DATA + '/' + brandUserId);
	 }
	 
	 /**
	  * Deletes the BrandUser bybrandUserId
	  */
	 deleteBrandUser(brandUserId) : Observable<any> {
	        
		 	return this.apiClientService.doDelete(URL.DELETE_BRAND_USER + '/' + brandUserId);
	 }
	 
	 /**
	  * Restores the BrandUser bybrandUserId
	  */
	 restoreBrandUser(brandUserId) : Observable<any> {
	        
		 	return this.apiClientService.doGet(URL.RESTORE_BRAND_USER + '/' + brandUserId);
	 }
	 
	 /**
	  * Search BrandUser by email or name
	  */
	 searchBrandUserOnPage(searchStr : string, brandUserId : any, pageNumber : any) {
		 return this.apiClientService.doGet(URL.SEARCH_BRAND_USER + '/' + searchStr + '/' + brandUserId + '/' + pageNumber);
	 }
	 
	 /**
	  * Search all active BrandUser by email or name
	  */
	 searchActiveBrandUserOnPage(searchStr : string, brandUserId : any, pageNumber : any, searchRole : any) {
		 return this.apiClientService.doGet(URL.SEARCH_ACTIVE_BRAND_USER + '/' + searchStr + '/' + brandUserId + '/' + pageNumber + '/' + searchRole);
	 }
	 
	 /**
	  * Search all deleted BrandUser by email or name
	  */
	 searchDeletedBrandUserOnPage(searchStr : string, brandUserId : any, pageNumber : any, searchRole : any) {
		 return this.apiClientService.doGet(URL.SEARCH_DELETE_BRAND_USER + '/' + searchStr + '/' + brandUserId + '/' + pageNumber + '/' + searchRole);
	 }
	 
	 /**
	  * Gets the list of all DSM User
	  */
	 getAllDSmUser() {
		 return this.apiClientService.doGet(URL.GET_DSM_USER_LIST);
	 }
	 
	 
	 /**
	  * Gets the list of all RAM User
	  */
	 getAllRamUser() {
		 return this.apiClientService.doGet(URL.GET_RAM_USER_LIST);
	 }
	 
	 /**
	  * user Logout
	  */
	 logOut() { 
		 	return this.apiClientService.doGet(URL.USER_LOGOUT);
	 }
}