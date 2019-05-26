import {Component, OnInit } from '@angular/core';
import { RouterModule, Routes, Router } from '@angular/router';

import { CommonService } from '../service/common.service';
import { CONSTANTS } from '../constants/constant';

import { LocalStorageService } from '../service/localStorage.service';
import { SystemSettingService } from '../service/systemSetting.service';

import { UiSwitchModule } from 'ngx-toggle-switch';

@Component({
	selector :'',
	templateUrl : './systemSetting.component.html'
})
export class SystemSettingComponent implements OnInit {
	
	constructor(public commonService : CommonService,
				public localStorageService : LocalStorageService,
				public systemSettingService : SystemSettingService) {}
	
	public viewRight : boolean = false;
	public createRight : boolean = false;
	public upadteRight : boolean = false;
	public deleteRight : boolean = false;
				
				
	ngOnInit() {
		if(!this.commonService.checkLogin() || this.checkRole()) {
    		this.commonService.logOut();
    	}
	}
	
	checkRole() {
		let roleWithPrivileges = this.localStorageService.getItem(CONSTANTS.USER_ROLE);
		roleWithPrivileges.forEach((role) => {
    		if(role.name == 'ROLE_USER' ||  role.name == 'ROLE_SUPERADMIN') {
    			
    			role.permission.forEach((permissions) => {
    	    		if(permissions.name !== '') {
    	        		if(permissions.name == 'view')
    	        			this.viewRight = true;
    	        		if(permissions.name == 'create')
    	        			this.createRight = true;
    	        		if(permissions.name == 'update')
    	        			this.upadteRight = true;
    	        		if(permissions.name == 'delete')
    	        			this.deleteRight = true;
    	        	}
    	    	});
    			return false;
    		}
    		return true;
    	});
    }
	
	logout() {
		this.commonService.logOut();
	}
	
	turnEmailOnOrOff(event) {
	
		this.systemSettingService.turnEmailOnOrOff(event).subscribe((result) => {
		},(error) => {
			
		});	
	}
 }