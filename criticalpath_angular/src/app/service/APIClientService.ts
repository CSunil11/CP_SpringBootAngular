import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { RouterModule, Router } from '@angular/router';
import { Http, Response } from '@angular/http';
import { Headers, RequestOptions, URLSearchParams } from '@angular/http';
import { Observable, Subject} from 'rxjs';
import { map, take } from 'rxjs/operators';
import { catchError } from 'rxjs/operators';
import { Cookie } from 'ng2-cookies';
import { CONSTANTS } from '../constants/constant';
import { CommonRepository } from '../repository/common.repository';
import { CommonService } from '../service/common.service';
import * as URL from '../constants/url';
import * as ROUTS from '../constants/routs';

@Injectable()
export class APIClientService {

	constructor(private httpClient : HttpClient,private http : Http, public commonRepository : CommonRepository, public router: Router,
				public commonService : CommonService) { }

	/**
	 * Get
	 */
	doGet(url : string) : Observable<any> {
		if(this.commonService.checkCookie(CONSTANTS.COOKIE_ACCESSTOKEN)) {
			return this.http.get(url,  this.commonRepository.getCommonRequestOptionsForJSON(URL.BEARER)).pipe(
					map(response => response.json()),
					catchError(e => this.handleError(e)));
		} else
			this.router.navigateByUrl('/' + ROUTS.LOGIN);
	}
	
	/**
	 * Post
	 */
	doPost(url : string, request : any) : Observable<any> {
			return this.http.post(url, request, this.commonRepository.getCommonRequestOptionsForJSON(URL.BEARER)).pipe(
					map(response => response.json()));
	}
	
	/**
	 * Delete
	 */
	doDelete(url : string) : Observable<any> {
			return this.http.delete(url,  this.commonRepository.getCommonRequestOptionsForJSON(URL.BEARER)).pipe(
					map(response => response.json()));
	}
	
	/**
	 * Post
	 */
	doPostForUplaoadData(url : string, request : any) : Observable<any> {
			return this.http.post(url, request, this.commonRepository.getCommonRequestOptionsWithoutHeader(URL.BEARER)).pipe(
					map(response => response.json()));
	}
	
	/**
	 * Handle error
	 */
	public handleError(e : any) : any {
		
		this.router.navigateByUrl('/' + ROUTS.ACCESS_DENIED);
	}
}