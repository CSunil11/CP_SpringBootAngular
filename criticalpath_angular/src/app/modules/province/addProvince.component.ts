import {Component, OnInit } from '@angular/core';
import { RouterModule, Routes, Router } from '@angular/router';
import { ReactiveFormsModule, FormsModule, FormGroup, FormControl, Validators, FormBuilder } from '@angular/forms';
import { NoWhitespaceValidator } from '../../common/customValidator/custom.validator';
import * as ROUTS from '../../constants/routs';
import { CONSTANTS } from '../../constants/constant';

import { ProvinceService } from '../../service/province.service'
import { CountryService } from '../../service/country.service'
import { CommonService } from '../../service/common.service';
import { LocalStorageService } from '../../service/localStorage.service';
import { UiSwitchModule } from 'ngx-toggle-switch';


@Component({
	selector :'',
	templateUrl : './addProvince.component.html'
})
export class AddProvinceComponent implements OnInit {
	
	constructor(public router: Router, public provinceService : ProvinceService, public countryService : CountryService,
				public commonService : CommonService,
				public localStorageService : LocalStorageService){}
	
	CONSTANTS : any = CONSTANTS;
	buttonText : string;
				
	addProvinceForm: FormGroup;
	public isActivated : boolean = false;
	public country : any[] = [];
	error : string;
	provinceAdded : boolean = false;
	requiredFlag : boolean = false;
	public countryNameList : any[] = [];
	public countryList : any[] = [];
				
	ngOnInit() {
		
		this.commonService.validateUserByRole();
		
		this.buttonText = CONSTANTS.SUBMIT;
		
		this.countryService.getAllActiveCountry().subscribe((results) => {
			if(results.data.length > 0) {
				for(let i = 0; i < results.data.length ; i++) {
					 this.countryNameList.push({
					        id: results.data[i].id,
					        text: results.data[i].name
					      });
				}
			}
		}, (error) => {
			
		});
		
		if(!this.commonService.checkCookie(CONSTANTS.COOKIE_ACCESSTOKEN))
				this.router.navigateByUrl('/' + ROUTS.LOGIN);
					
					
		this.addProvinceForm = new FormGroup({
			name: new FormControl('', [Validators.required, NoWhitespaceValidator]),
			country: new FormControl(this.country, [Validators.required]),
			isActive: new FormControl(true, [])
		});
		
	}
	
	submitProvinceForm(provinceForm : any) {
		this.error = '';

		if(this.addProvinceForm.valid) {
			this.buttonText = CONSTANTS.SUBMITTING;
			let ProvinceFormJson = {
				'cid':provinceForm.country[0].id,
				'name':this.addProvinceForm.controls['name'].value,
				'isActive':this.addProvinceForm.controls['isActive'].value
			}
			
			this.provinceService.addProvince(ProvinceFormJson).subscribe((results) => {
				   this.provinceAdded = true;
				   setTimeout(() => {
				   this.router.navigateByUrl('/' + ROUTS.PROVINCE);
				   },2500);
			}, (error) => {
				   this.error = JSON.parse(error._body).message;
				   this.buttonText = CONSTANTS.SUBMIT;
			});
			
		} else
			 this.requiredFlag = true;
	}
				
	changeStatus(event) {
		
		this.addProvinceForm.controls['isActive'].setValue (event);
	}
				
	back() {
		
		this.router.navigateByUrl('/' + ROUTS.PROVINCE);
	}
				
}
