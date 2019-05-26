import { Injectable } from '@angular/core';

import { Http, Response } from '@angular/http';
import { Headers, RequestOptions, URLSearchParams } from '@angular/http';
import { Observable, Subject} from 'rxjs';
import { map, take } from 'rxjs/operators';
import { catchError } from 'rxjs/operators';
import { Cookie } from 'ng2-cookies';
import { CommonService } from '../service/common.service';
import { CONSTANTS } from '../constants/constant';

import * as URL from '../constants/url';

@Injectable()
export class CommonRepository {
    
    constructor(private http: Http,
        public commonService: CommonService) { }
    
    /**
     * Get access token from cookie
     */
    getToken() : string {
        
        return this.commonService.decrypt(Cookie.get(CONSTANTS.COOKIE_ACCESSTOKEN));
    }
    
     /**
     * Get access token from cookie
     */
    getEncryptedId() : string {
        
        return this.commonService.decrypt(Cookie.get(CONSTANTS.COOKIE_ENCRYPTED_ID));
    }

    /**
     * Get common request header with authorization token for content type 'json'
     */
    getCommonRequestOptionsForJSON(authorization : string) : RequestOptions{
        
        let headers = new Headers();
        headers.append('Authorization',(authorization == URL.AUTHORIZATION ? authorization : authorization + this.getToken()));
        headers.append('Content-Type', 'application/json');
        return new RequestOptions({ headers: headers });
    }
    
    /**
     * Get common request header with authorization token for content type 'URLEncoded'
     */
    getCommonRequestOptionsForURLEncoded(authorization : string) : RequestOptions{
        
        let headers = new Headers();
        headers.append('Authorization',(authorization == URL.AUTHORIZATION ? authorization : authorization + this.getToken()));
        headers.append('Content-Type', 'application/x-www-form-urlencoded');
        return new RequestOptions({ headers: headers });
    }
    
     /**
     * Get common request header with authorization token for content type 'URLEncoded'
     */
    getCommonRequestOptionsWithoutContentType(authorization : string) : RequestOptions{
        
        let headers = new Headers();
        headers.append('Authorization',(authorization == URL.AUTHORIZATION ? authorization : authorization + this.getToken()));
        return new RequestOptions({ headers: headers });
    }
    
    /**
     * Get common request header with authorization token for content type 'json'
     */
    getCommonRequestOptionsWithoutHeader(authorization : string) : RequestOptions{
        
        let headers = new Headers();
        headers.append('Authorization',(authorization == URL.AUTHORIZATION ? authorization : authorization + this.getToken()));
//        headers.append('Content-Type', 'application/json');
        return new RequestOptions({ headers: headers });
    }
    
    /**
     * Gets the list of all permissions 
     */
    getAllPermissions() : Observable<any> {
    	return this.http.post(URL.GET_ALL_PERMISSIONS, '', this.getCommonRequestOptionsForJSON(URL.BEARER)).pipe(
                map(response => response.json())
    			); 
    }

}