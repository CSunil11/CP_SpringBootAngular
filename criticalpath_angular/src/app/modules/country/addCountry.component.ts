import {Component, OnInit } from '@angular/core';
import { RouterModule, Routes, Router } from '@angular/router';
import { ReactiveFormsModule, FormsModule, FormGroup, FormControl, Validators, FormBuilder } from '@angular/forms';
import { NoWhitespaceValidator } from '../../common/customValidator/custom.validator';
import * as ROUTS from '../../constants/routs';
import { CONSTANTS } from '../../constants/constant';

import { CountryService } from '../../service/country.service';
import { CommonService } from '../../service/common.service';
import { LocalStorageService } from '../../service/localStorage.service';
import { UiSwitchModule } from 'ngx-toggle-switch';


@Component({
	selector :'',
	templateUrl : './addCountry.component.html'
})
export class AddCountryComponent implements OnInit {
	
	constructor(public router: Router, public countryService : CountryService,
				public commonService : CommonService,
				public localStorageService : LocalStorageService){}
	
	CONSTANTS : any = CONSTANTS;
				
	addCountryForm: FormGroup;
	public isActivated : boolean = false;
	error : string;
	countryAdded : boolean = false;
    requiredFlag : boolean = false;
	buttonText : string;
				
	ngOnInit() {

		this.commonService.validateUserByRole();
		
		this.buttonText = CONSTANTS.SUBMIT;
		
		if(!this.commonService.checkCookie(CONSTANTS.COOKIE_ACCESSTOKEN))
			this.router.navigateByUrl('/' + ROUTS.LOGIN);
		
		
		this.addCountryForm = new FormGroup({
			name: new FormControl('', [Validators.required, NoWhitespaceValidator]),
			code: new FormControl('', [Validators.required, NoWhitespaceValidator]),
			isActive: new FormControl(true, [])
      });
	}
	
	submitCountryForm(countryForm : any) {
		
		this.error = '';
		 if(this.addCountryForm.valid) {
			
			this.buttonText = CONSTANTS.SUBMITTING;
	        this.countryService.addCountry(countryForm).subscribe((results) => {
	        	this.countryAdded = true;
	        	setTimeout(() => {
	        		this.router.navigateByUrl('/' + ROUTS.COUNTRY);
	        	},2500);
	        }, (error) => {
	        		this.error = JSON.parse(error._body).message;
	        		this.buttonText = CONSTANTS.SUBMIT;
	        });
		 } else
			 this.requiredFlag = true;
    }
	
	changeStatus(event) {
		this.addCountryForm.controls['isActive'].setValue (event);
	}
	
	back() {
		this.router.navigateByUrl('/' + ROUTS.COUNTRY);
	}
	
	/*checkRole() {
		
		let role = this.localStorageService.getItem(CONSTANTS.USER_ROLE);
		role.forEach((role) => {
    		if(role.name == 'ROLE_SUPER_ADMIN') {
    			return false;
    		}
    		return true;
    	});
	}*/
}