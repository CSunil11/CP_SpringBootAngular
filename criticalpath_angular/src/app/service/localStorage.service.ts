import { Injectable } from '@angular/core';
import * as crypto from 'crypto-js';
import { CONSTANTS } from '../constants/constant';
import { CommonService } from './common.service';
declare var require: any

@Injectable()
export class LocalStorageService {		
	
		public CryptoJS;
	
		constructor() { 	
		
			this.initializeSecureWebStorage();	
		}	
	
		initializeSecureWebStorage( ) {		
			
			this.CryptoJS = require("crypto-js");	
			
		}		
		
		setItem( key, value) {		
			
			localStorage.setItem( key, this.CryptoJS.AES.encrypt( value, CONSTANTS.CRYPTO_SECRET_KEY));	
			
		}		
		
		getItem( key ) {	
			
			if( key && key.length > 0 ) {			
				
				let bytes = this.CryptoJS.AES.decrypt(localStorage.getItem(key),CONSTANTS.CRYPTO_SECRET_KEY);
				if( bytes != undefined ) {				
					
					let plaintext = bytes.toString(this.CryptoJS.enc.Utf8);				
					
					return plaintext;			
				}		
			}		
			return undefined;	
		}	
		
		removeItem( key ) {		
			localStorage.removeItem(key);	
		}	
		
		removeAll() {		
			
			localStorage.clear();	
		}
}