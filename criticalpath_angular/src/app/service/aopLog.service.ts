import { Injectable } from '@angular/core';
import { Cookie } from 'ng2-cookies';
import { RouterModule, Routes, Router } from '@angular/router';

import {CONSTANTS } from '../constants/constant';

import { Observable, Subject} from 'rxjs';
import { map, take } from 'rxjs/operators';
import { catchError } from 'rxjs/operators';
import * as URL from '../constants/url';
import * as ROUTS from '../constants/routs';
import  { APIClientService } from '../service/APIClientService';


declare var $: any;
declare var CryptoJS: any;

@Injectable()
export class AopLogService {
	
	constructor(public apiClientService : APIClientService) {}
	/**
	 * search by Date
	 */
	searchByDate(searchDate :any) : Observable<any> {
	        
		 	return this.apiClientService.doGet(URL.SEARCH_AOP_LOG + '/' + searchDate);
	}
}