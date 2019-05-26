import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Http, Response } from '@angular/http';
import { Headers, RequestOptions, URLSearchParams } from '@angular/http';
import { Observable, Subject} from 'rxjs';
import { map, take } from 'rxjs/operators';
import { catchError } from 'rxjs/operators';
import { Cookie } from 'ng2-cookies';
import { CommonService } from '../service/common.service';
import { CONSTANTS } from '../constants/constant';

import * as URL from '../constants/url';

import { CommonRepository } from '../repository/common.repository';

@Injectable()
export class SystemSettingRepository {
	
	 constructor(private httpClient : HttpClient,private http : Http,
			 	 public commonRepository : CommonRepository){}
	 
	 turnEmailOnOrOff(isEmailTurnedOnOrOff) : Observable<any> {
	        
	        return this.http.post(URL.EMAIL_SETTING + "/" + isEmailTurnedOnOrOff,'', this.commonRepository.getCommonRequestOptionsForJSON(URL.BEARER)).pipe(
	                map(response => response.json())
	        );
	  }
	
}