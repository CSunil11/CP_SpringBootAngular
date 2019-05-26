import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Http, Response,RequestOptions,Headers,URLSearchParams } from '@angular/http';
import { Observable, Subject} from 'rxjs';
import { map, take } from 'rxjs/operators';
import { catchError } from 'rxjs/operators';

import * as URL from '../constants/url';

import { CommonRepository } from '../repository/common.repository';


@Injectable()
export class LinksRepository {
	
	constructor(private httpClient : HttpClient,private http : Http,
			 public commonRepository : CommonRepository){}
	
	/**
     * Get user  details by accesstoken
     */
    getUserDetails(accessToken : string) : Observable<any> {
        
    	 return this.http.post(URL.GET_USER_DETAILS, '', this.commonRepository.getCommonRequestOptionsForJSON(URL.BEARER)).pipe(
                 map(response => response.json())
    	);              
    }
    
    /**
     * Get admin details by accesstoken
     */
    getAdminDetails(accessToken : string) : Observable<any> {
        
    	 return this.http.post(URL.GET_ADMIN_DETAILS, '', this.commonRepository.getCommonRequestOptionsForJSON(URL.BEARER)).pipe(
                 map(response => response.json())
    	);              
    }
	
}