import {Component, OnInit } from '@angular/core';
import { RouterModule, Routes, Router } from '@angular/router';
import { ReactiveFormsModule, FormsModule, FormGroup, FormControl, Validators, FormBuilder } from '@angular/forms';
import { NoWhitespaceValidator } from '../../common/customValidator/custom.validator';
import * as ROUTS from '../../constants/routs';
import { CONSTANTS } from '../../constants/constant';

import { LocationDivisionService } from '../../service/locationDivision.service'
import { BrandUserService } from '../../service/brandUser.service'
import { CountryService } from '../../service/country.service';
import { ProvinceService } from '../../service/province.service';
import { CommonService } from '../../service/common.service';
import { LocalStorageService } from '../../service/localStorage.service';
import { UiSwitchModule } from 'ngx-toggle-switch';


@Component({
	selector :'',
	templateUrl : './addLocationDivision.component.html'
})
export class AddLocationDivisionComponent implements OnInit {
	
	constructor(public router: Router, public locationDivisionService : LocationDivisionService, 
				public brandUserService : BrandUserService,
				public countryService : CountryService,
				public provinceService : ProvinceService,
				public commonService : CommonService,
				public localStorageService : LocalStorageService){}
	
	CONSTANTS : any = CONSTANTS;
    buttonText : string;
				
	addLocationDivisionForm: FormGroup;
	public isActivated : boolean = false;
	public DSMUser : any[] = [];
	public RAMUser : any[] = [];			
	error : string;
	locationDivisionAdded : boolean = false;
	requiredFlag : boolean = false;
	public DSMUserList : any[] = [];
	public RAMUserList : any[] = [];
	countryList : any[] = [];
	provinceList : any;			
				
				
	ngOnInit() {
		
		this.commonService.validateUserByRole();
		
		this.buttonText = CONSTANTS.SUBMIT;
		
		this.brandUserService.getAllDSmUser().subscribe((results) => {
			console.log(results);
			if(results.data.length > 0) {
				for(let i = 0; i < results.data.length ; i++) {
					this.DSMUserList.push({
					        id: results.data[i].id,
					        text: results.data[i].name
					      });
				}
			}
		}, (error) => {
			
		});
		
//		this.brandUserService.getAllRamUser().subscribe((results) => {
//			if(results.data.length > 0) {
//				for(let i = 0; i < results.data.length ; i++) {
//					this.RAMUserList.push({
//					        id: results.data[i].id,
//					        text: results.data[i].name
//					      });
//				}
//			}
//		}, (error) => {
//			
//		});
		
		if(!this.commonService.checkCookie(CONSTANTS.COOKIE_ACCESSTOKEN))
				this.router.navigateByUrl('/' + ROUTS.LOGIN);
					
					
		this.addLocationDivisionForm = new FormGroup({
			name: new FormControl('', [Validators.required, NoWhitespaceValidator]),
			country: new FormControl('', [Validators.required]),
			province: new FormControl('', [Validators.required]),
			DSMUser: new FormControl(this.DSMUser, [Validators.required]),
//			RAMUser: new FormControl(this.RAMUser, [Validators.required]),
			isActive: new FormControl(true, [])
		});
		
		this.countryService.getAllActiveCountry().subscribe((results) => {
			this.countryList = results.data;
		}); 
		
	}
	
	getProvince() {
		let country = this.addLocationDivisionForm.controls['country'].value;
		this.addLocationDivisionForm.controls['province'].setValue('');
		this.provinceService.getProvinceOfCountry(country.id).subscribe((results) => {
			this.provinceList = results.data;
		}, (error) => {
			
		});
	}
	
	submitLocationDivisionForm(locationDivisionForm : any) {
		//console.log(locationDivisionForm)
		this.error = '';
		if(this.addLocationDivisionForm.valid) {
			this.buttonText = CONSTANTS.SUBMITTING;
			let LocationDivisionFormJson = {
				'uid':locationDivisionForm.DSMUser[0].id,
//				'ramuid':locationDivisionForm.RAMUser[0].id,
				'cid':locationDivisionForm.country.id,
				'pid':locationDivisionForm.province.id,
				'name':this.addLocationDivisionForm.controls['name'].value,
				'status':this.addLocationDivisionForm.controls['isActive'].value
			}
			//console.log(LocationDivisionFormJson);
			this.locationDivisionService.addLocationDivision(LocationDivisionFormJson).subscribe((results) => {
				   this.locationDivisionAdded = true;
				   setTimeout(() => {
				   this.router.navigateByUrl('/' + ROUTS.LOCATIONDIVISION);
				   },2500);
			}, (error) => {
				   this.error = JSON.parse(error._body).message;
				   this.buttonText = CONSTANTS.SUBMIT;
			});
			
		} else
			 this.requiredFlag = true;
	}
				
	changeStatus(event) {
		
		this.addLocationDivisionForm.controls['isActive'].setValue (event);
	}
				
	back() {
		
		this.router.navigateByUrl('/' + ROUTS.LOCATIONDIVISION);
	}
				
}
