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
export class BrandService {
	
	constructor(public apiClientService : APIClientService) {}
	
	/**
	  * Adds the new brand
	  */
	 addBrand(brandForm : any) : Observable<any> {
	        
		 	return this.apiClientService.doPost(URL.ADD_BRAND, brandForm);
	 }
	 
	 /**
	  * Gets the list of all brand
	  */
	 getAllBrand() : Observable<any> {
	        
	        return this.apiClientService.doGet(URL.GET_BRAND_LIST);
	 }
	 
	 /**
	  * Gets the brand by pagenumber
	  */
	 getBrandByPageNumber(pageNumber) : Observable<any> {
	        
	        return this.apiClientService.doGet(URL.GET_BRAND_LIST + '/' + pageNumber);
	 }
	 
	 /**
	  * Gets the list of all active Brand
	  */
	 getAllActiveBrand() {
		 return this.apiClientService.doGet(URL.GET_ACTIVE_BRAND);
	 }
	 
	 /**
	  * Gets the list of all active Brand by pagenumber
	  */
	 getAllActiveBrandByPageNumber(pageNumber) {
		 return this.apiClientService.doGet(URL.GET_ACTIVE_BRAND + '/' + pageNumber);
	 }
	 
	 /**
	  * Gets the list of all deleted Brand by pagenumber
	  */
	 getAllDeletedBrandByPageNumber(pageNumber) {
		 return this.apiClientService.doGet(URL.GET_DELETE_BRAND + '/' + pageNumber);
	 }
	 
	 /**
	  * Gets the data of single brand by id
	  */
	 getBrandData(brandId) : Observable<any> {
	        
	        return this.apiClientService.doGet(URL.GET_BRAND_DATA + '/' + brandId);
	 }
	 
	 /**
	  * Deletes the brand by brandId
	  */
	 deleteBrand(brandId) : Observable<any> {
	        
		 	return this.apiClientService.doDelete(URL.DELETE_BRAND + '/' + brandId);
	 }
	 
	 /**
	  * Restores the brand by brandId
	  */
	 restoreBrand(brandId) : Observable<any> {
	        
		 	return this.apiClientService.doGet(URL.RESTORE_BRAND + '/' + brandId);
	 }
	 
	 /**
	  * Search brand on particular page
	  */
	 searchBrandOnPage(searchStr : string, pageNumber : number) {
		 return this.apiClientService.doGet(URL.SEARCH_BRAND + '/' + searchStr + '/' + pageNumber);
	 }
	 
	 /**
	  * Search active brand on particular page
	  */
	 searchActiveBrandOnPage(searchStr : string, pageNumber : number) {
		 return this.apiClientService.doGet(URL.SEARCH_ACTIVE_BRAND + '/' + searchStr + '/' + pageNumber);
	 }
	 
	 /**
	  * Search deleted brand on particular page
	  */
	 searchDeletedBrandOnPage(searchStr : string, pageNumber : number) {
		 return this.apiClientService.doGet(URL.SEARCH_DELETE_BRAND + '/' + searchStr + '/' + pageNumber);
	 }
}