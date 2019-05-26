import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Http, Response,RequestOptions,Headers,URLSearchParams } from '@angular/http';
import { Observable, Subject} from 'rxjs';
import { map, take } from 'rxjs/operators';
import { catchError } from 'rxjs/operators';

import * as URL from '../constants/url';

import { CommonRepository } from '../repository/common.repository';

@Injectable()
export class LoginRepository {
	
	 constructor(private httpClient : HttpClient,private http : Http,
			 	 public commonRepository : CommonRepository){}
	 
	 login(userName : string, password : string) : Observable<any> {
	        
       let urlSearchParams = new URLSearchParams();
       
       urlSearchParams.append('username', userName);
       urlSearchParams.append('password', password);
       urlSearchParams.append('grant_type', "password");
   
       let body = urlSearchParams.toString();
       
       return this.http.post(URL.LOGIN, body, this.commonRepository.getCommonRequestOptionsForURLEncoded(URL.AUTHORIZATION)).pipe(
                            map(response => response.json())
                            );
   }
	 	/**
	     * Get user by accesstoken
	     */
	    getUser() : Observable<any> {
	        
	    	 return this.http.post(URL.GET_USER, '', this.commonRepository.getCommonRequestOptionsForJSON(URL.BEARER)).pipe(
	                 map(response => response.json())
	    	);              
	    }
}