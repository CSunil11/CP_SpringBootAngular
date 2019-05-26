import {Component, OnInit, HostListener} from '@angular/core';
import { RouterModule, Routes, Router } from '@angular/router';

import { CommonService } from '../../service/common.service';
import { BrandUserService } from '../../service/brandUser.service';
import { LocalStorageService } from '../../service/localStorage.service';
import { LoginRepository } from '../../repository/login.repository';
import { CommonRepository } from '../../repository/common.repository';

import * as ROUTS from '../../constants/routs';
import { CONSTANTS } from '../../constants/constant';

@Component({
	selector :'',
	templateUrl : './layout.component.html',
	styleUrls: ['../../../assets/css/skins/_all-skins.min.css']
})
export class LayoutComponent implements OnInit {

	constructor(public router : Router,
				public commonService : CommonService, public commonRepository : CommonRepository,
				public localStorageService : LocalStorageService,
				 public brandUserService : BrandUserService,
				public loginRepository : LoginRepository) {}
	
	public modulePermissionRight : any[]= [];
	public allPermissions : any[] = [];
	public accessToken : any;
				
	ngOnInit() {
		
		this.accessToken = this.commonService.decrypt(this.commonService.getCookie(CONSTANTS.COOKIE_ACCESSTOKEN));
		this.loginRepository.getUser().subscribe((results) => {
        	
            if (results != undefined) {
            	
            	if(results.role.length != 0) {
            		this.modulePermissionRight = results.permissions;
            	}
            }
        },(error) => {
        	if(JSON.parse(error._body).error == "access_denied")
        		this.router.navigateByUrl('/' + ROUTS.ACCESS_DENIED);
        });
		
		this.commonRepository.getAllPermissions().subscribe((results) => {
			this.allPermissions = results;
		}, (error) => {
			
		});
	}
	
	logout() {
		
		this.brandUserService.logOut().subscribe((results)  => {
			console.log("logout 1");
		},(error) => {
			
		});
		this.commonService.deleteCookie();
	
		this.router.navigateByUrl('/' + ROUTS.LOGIN);
	}
	
	/*@HostListener('window:popstate', ['$event'])
    onPopState(event: Event) {
        window.history.forward();
        event.preventDefault();
        event.stopPropagation();
    }*/
}