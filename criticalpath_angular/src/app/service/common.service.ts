import { Injectable } from '@angular/core';
import { Cookie } from 'ng2-cookies';
import { RouterModule, Routes, Router } from '@angular/router';

import {CONSTANTS } from '../constants/constant';

import { LocalStorageService } from './localStorage.service';

import * as ROUTS from '../constants/routs';


declare var $: any;
declare var CryptoJS: any;

@Injectable()
export class CommonService {

    constructor(private router: Router,public localStorageService : LocalStorageService) { }

    /**
     * Set cookie
     */
    setCookie(key: string, value: any, cookieExpirationDays: number, path: string) {

        if (cookieExpirationDays)
            Cookie.set(key, value, cookieExpirationDays, path);
        else
            Cookie.set(key, value, null, path);
    }

    /**
     * Method to get cookie
     * @param {string} key [description]
     */
    getCookie(key: string): any {

        return Cookie.get(key);
    }

    checkCookie(key: string): boolean {

        return Cookie.check(key);
    }

    /**
     * Method to check user login by cookie
     */
    checkLogin(): boolean {
    	
    	if(this.checkIsNullOrUndefined(this.getCookie(CONSTANTS.COOKIE_ACCESSTOKEN))) {
    		
    		return false;
    	} 
    	return true;
    }
    
    /**
     * Method to redirect
     */
    checkLoginAndRedirect(): boolean {
    	
    	if(!this.checkLogin()) 
    		return false;
    	
    }
     
    /**
     * Encrypt
     * @param  {string} normalData [description]
     * @return {string}            [description]
     */
    encrypt(normalData: string): string {

        if (!normalData)
            return null;

        return CryptoJS.AES.encrypt(normalData, CONSTANTS.ENCRYPT_DECRYPT_SECRET_KEY);
    }

    /**
     * Decrypt
     * @param  {string} encryptedData [description]
     * @return {string}               [description]
     */
    decrypt(encryptedData: string): string {

        if (!encryptedData)
            return null;

        var decrypted = CryptoJS.AES.decrypt(encryptedData, CONSTANTS.ENCRYPT_DECRYPT_SECRET_KEY);
        return decrypted ? decrypted.toString(CryptoJS.enc.Utf8) : null;
    }
    
     /**
     * Method to delete cookie
     * @param {string} key [description]
     */
    deleteCookie() {

        Cookie.deleteAll('/');
    }
    
    /**
     * Check is null 
     */
    checkIsNullOrUndefined(data: any) {
    	if(data == null || data == undefined || data == '')
    		return true;
    	return false;
    }
    
    /**
     * LogOut 
     */
    logOut() {
    	 this.deleteCookie();
    	 this.localStorageService.removeItem(CONSTANTS.PERMISSIONS);
         this.router.navigateByUrl(ROUTS.LOGIN);
    }

    validateUserByRole(){
        if(this.localStorageService.getItem(CONSTANTS.USER_ROLE) === 'ROLE_ADMIN_READONLY') {
        	this.router.navigateByUrl(ROUTS.NOT_AUTHORIZED);
//        	this.router.navigateByUrl(ROUTS.NOT_AUTHORIZED);
//        	this.router.navigateByUrl("/notAuthorized");
//        	   this.logOut();
        }
    }

}