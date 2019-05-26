import {Component, OnInit } from '@angular/core';
import { RouterModule, Routes, Router } from '@angular/router';
import { ReactiveFormsModule, FormsModule, FormGroup, FormControl, Validators, FormBuilder } from '@angular/forms';
import { NoWhitespaceValidator } from '../../common/customValidator/custom.validator';
import * as ROUTS from '../../constants/routs';
import { CONSTANTS } from '../../constants/constant';

import { StoreService } from '../../service/store.service';
import { CountryService } from '../../service/country.service';
import { BrandUserService } from '../../service/brandUser.service'
import { BrandService } from '../../service/brand.service';
import { CommonService } from '../../service/common.service';
import { LocalStorageService } from '../../service/localStorage.service';
import { ProvinceService } from '../../service/province.service';
import { LocationDivisionService } from '../../service/locationDivision.service';


@Component({
	selector :'',
	templateUrl : './addStore.component.html'
})
export class AddStoreComponent implements OnInit {
	
	constructor(public router: Router, 
				public storeService : StoreService,
				public countryService : CountryService,
				public brandService : BrandService,
				public brandUserService : BrandUserService,
				public commonService : CommonService,
				public provinceService : ProvinceService,
				public locationDivisionService : LocationDivisionService,
				public localStorageService : LocalStorageService){}
	
	CONSTANTS : any = CONSTANTS;
    buttonText : string;
				
	addStoreForm: FormGroup;
	error : string;
	storeAdded : boolean = false;
	countryList : any[] = [];
	brandList : any[] = [];
	requiredFlag : boolean = false;
	provinceList : any;
	locDivList : any;
	public RAMUser : any[] = [];	
	public RAMUserList : any[] = [];
				
	ngOnInit() {
		
		this.commonService.validateUserByRole();
		
		this.buttonText = CONSTANTS.SUBMIT;
		
		this.addStoreForm = new FormGroup({
			code: new FormControl('', [Validators.required, NoWhitespaceValidator]),//, Validators.pattern("^[a-zA-Z]*$")
			name: new FormControl('', [Validators.required, NoWhitespaceValidator]),
			country: new FormControl('', [Validators.required]),
			brand: new FormControl('', [Validators.required]),
			province: new FormControl('', [Validators.required]),
			locationDivision: new FormControl('', [Validators.required]),
			regionalManagers: new FormControl('', [Validators.required]),
			isActive: new FormControl(true, [])
		});
		
		this.countryService.getAllActiveCountry().subscribe((results) => {
			this.countryList = results.data;
		}); 
		
		this.brandService.getAllActiveBrand().subscribe((results) => {
			this.brandList = results.data;
		}); 
		
		this.brandUserService.getAllRamUser().subscribe((results) => {
			console.log(results);
			this.RAMUserList = results.data
//			if(results.data.length > 0) {
//				for(let i = 0; i < results.data.length ; i++) {
//					this.RAMUserList.push({
//					        id: results.data[i].id,
//					        text: results.data[i].name
//					      });
//				}
//			}
		}, (error) => {
			
		});
		 
	}
	
	back() {
		this.router.navigateByUrl('/' + ROUTS.STORE);
	}
	
	removeUser() {
		let locDiv = this.addStoreForm.controls['locationDivision'].value;
		locDiv.divisionalSalesManagers = null;
		this.addStoreForm.controls['locationDivision'].setValue(locDiv);
	}
	
	getProvince() {
		let country = this.addStoreForm.controls['country'].value;
		this.addStoreForm.controls['province'].setValue('');
		this.addStoreForm.controls['locationDivision'].setValue('');
		this.provinceList = null;
		this.provinceService.getProvinceOfCountry(country.id).subscribe((results) => {
			this.provinceList = results.data;
		}, (error) => {
			
		});
		this.locDivList = null;
	}
	
	getLocationDivision(){
		let province = this.addStoreForm.controls['province'].value;
		this.locDivList = null;
		this.locationDivisionService.getLocationDivisionOfProvince(province.id).subscribe((results) => {
			this.locDivList = results.data;
		}, (error) => {
			
		});

	}
	
	submitStoreForm(storeForm : any) {
    	
		this.error = '';
		
		if(this.addStoreForm.valid) {
//			this.addStoreForm.controls['RAMUser'].setValue(this.RAMUser.id);
			this.buttonText = CONSTANTS.SUBMITTING;
			console.log(storeForm);
	        this.storeService.addStore(storeForm).subscribe((results) => {
	        		this.storeAdded = true;
	        		setTimeout(() => {
	        			this.router.navigateByUrl('/' + ROUTS.STORE);
	            	}, 2500);
	        		
	        }, (error) => {
	        		this.error = JSON.parse(error._body).message;
	        		this.buttonText = CONSTANTS.SUBMIT;
	        });
		} else
			this.requiredFlag = true;
    }
	
	changeStatus(event) {
		this.addStoreForm.controls['isActive'].setValue (event);
	}
}