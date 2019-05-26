import {Component, OnInit } from '@angular/core';
import { RouterModule, Routes, Router } from '@angular/router';
import { ReactiveFormsModule, FormsModule, FormGroup, FormControl, Validators, FormBuilder } from '@angular/forms';
import { NoWhitespaceValidator } from '../../common/customValidator/custom.validator';
import * as ROUTS from '../../constants/routs';
import { CONSTANTS } from '../../constants/constant';
import { Role } from '../../constants/role.enum';

import { BrandUserService } from '../../service/brandUser.service'
import { CommonService } from '../../service/common.service';
import { StoreService } from '../../service/store.service';
import { BrandService } from '../../service/brand.service';
import { LocalStorageService } from '../../service/localStorage.service';

import { BrandUserRequest } from '../../model/brandUserRequest';


@Component({
	selector :'',
	templateUrl : './addBrandUser.component.html'
})
export class AddBrandUserComponent implements OnInit {
	
	constructor(public router: Router, public brandUserService : BrandUserService, public storeService  : StoreService,
				public commonService : CommonService, public brandService : BrandService, 
				public localStorageService : LocalStorageService){}
	
	CONSTANTS : any = CONSTANTS;
	buttonText : string;
				
	addBrandUserForm: FormGroup;
	brandUserRequest : BrandUserRequest;
				
	error : string;
	public brand : string;
	public brandData : any;
	public storeData : any;
	
	public allPermissions : any[] = [];
	public storeList : any[]= [];
	public stores : any[] = [];
				
	public brandUserAdded : boolean = false;
	public requiredFlag : boolean = false;
	public isStoreManagerRole : boolean = false;
	public isPassWordMatched: boolean = true;
	public suggestion : boolean = false;
	public  showConfirmPas : boolean;
	public  showPas : boolean;
	public brandNameList : any[] = [];
	public brandsObjList : any[] = [];			
	// Enum
	role = Role;
	
	ngOnInit() {
		
		this.commonService.validateUserByRole();
		
		this.buttonText = CONSTANTS.SUBMIT;
		
		this.brandService.getAllActiveBrand().subscribe((results) => {
			if(results.data.length > 0) {
				this.brandsObjList = results.data;
				for(var i = 0; i < results.data.length ; i++) {
					
					 this.brandNameList.push(results.data[i].name);
				}
			}
		}, (error) => {
			
		});
	
		this.brand = this.localStorageService.getItem(CONSTANTS.BRAND);

		this.addBrandUserForm = new FormGroup({
			brand: new FormControl(this.brand, []),
			name: new FormControl('', [Validators.required, NoWhitespaceValidator]),
			email: new FormControl('', [Validators.pattern("^[_\.0-9A-Za-z-]+@([0-9A-Za-z][0-9A-Za-z.-]+\.)+[A-Za-z]{2,4}$"), Validators.required]),
			phone: new FormControl('', [Validators.required, NoWhitespaceValidator, Validators.pattern("^[0-9]+")]),
			password : new FormControl('',[Validators.required, NoWhitespaceValidator]),
			confirmPassword : new FormControl('',[Validators.required, NoWhitespaceValidator]),
			role: new FormControl('',[Validators.required]),
			store: new FormControl(null,[]),
			isActive: new FormControl(true, []),
			userLogin: new FormControl([], [])
		});
		
		this.storeService.getStoreByBrandId(this.localStorageService.getItem(CONSTANTS.BRAND_ID)).subscribe((results) => {
			if(results && results.data.length > 0) {
				this.storeList = results.data;
			}
		}, (error) => {
			
		});
		
		this.brandService.getBrandData(this.localStorageService.getItem(CONSTANTS.BRAND_ID)).subscribe((results) => {
			if(results.data) {
				this.brandData = results.data;
			}
		}, (error) => {
			
		});

	}
	
	brandOnSelect(event : any) {
		let brandName = event;
		this.storeList = [];
		this.addBrandUserForm.controls['store'].setValue([]);
		
		for(var i =0; i<this.brandsObjList.length ; i++){
			if(brandName == this.brandsObjList[i].name){
				this.localStorageService.setItem(CONSTANTS.BRAND, this.brandsObjList[i].name);
				this.localStorageService.setItem(CONSTANTS.BRAND_ID, JSON.stringify(this.brandsObjList[i].id));

				this.storeService.getStoreByBrandId(this.localStorageService.getItem(CONSTANTS.BRAND_ID)).subscribe((results) => {
					if(results && results.data.length > 0) {
						let storeObj;
						storeObj =results.data
						this.storeList = storeObj;
//						this.storeList = results.data;
					}
				}, (error) => {
					
				});
				
				this.brandService.getBrandData(this.localStorageService.getItem(CONSTANTS.BRAND_ID)).subscribe((results) => {
					if(results.data) {
						let brandObj;
						brandObj = results.data;
						this.brandData = brandObj;
					}
				}, (error) => {
					
				});
				
			}
		}	
	}
	
	keys() : Array<string> {
		
	    const keys = Object.keys(this.role);
	    return keys.map(el => Object(this.role)[el]);
    }
	
	onSelect(event : Event) {
		this.suggestion = false;
	}
	
	checkRole() {
		
		if(this.addBrandUserForm.controls['role'].value == this.role.ROLE_SM_USER) {
			this.isStoreManagerRole = true;
			this.addBrandUserForm.get('store').setValidators([Validators.required]);
			this.addBrandUserForm.updateValueAndValidity();
		} else {
			this.isStoreManagerRole = false;
			this.addBrandUserForm.get('store').clearValidators();
			this.addBrandUserForm.updateValueAndValidity();
			this.addBrandUserForm.controls['store'].setValue(null);
		}
	}
	
	submitForm(brandUserForm : any) {
    	
		this.error = '';
		
		if(this.addBrandUserForm.valid && this.isPassWordMatched) {
			this.buttonText = CONSTANTS.SUBMITTING;
			
			this.brandUserRequest = new BrandUserRequest(brandUserForm);
			this.brandUserRequest['brand'] = this.brandData;
			
			if(this.addBrandUserForm.controls['store'].value != null)
				this.brandUserRequest['store'] = this.addBrandUserForm.controls['store'].value[0];
			
			if(this.addBrandUserForm.controls['role'].value == this.role.ROLE_SM_USER)
				this.brandUserRequest['role'] = Object.keys(this.role)[2];
			else if(this.addBrandUserForm.controls['role'].value == this.role.ROLE_DSM_USER)
				this.brandUserRequest['role'] = Object.keys(this.role)[1];
			else
				this.brandUserRequest['role'] = Object.keys(this.role)[0];
			
			
			let userLogin = { 
				role : this.brandUserRequest['role'],
				email : this.addBrandUserForm.controls['email'].value,
				password : this.addBrandUserForm.controls['password'].value,
				isActive : this.addBrandUserForm.controls['isActive'].value
			};

			this.brandUserRequest['userLogin'] = userLogin;
	        this.brandUserService.addBrandUser(this.brandUserRequest).subscribe((results) => {
	        		this.brandUserAdded = true;
	        		setTimeout(() => {
	        			this.router.navigateByUrl('/' + ROUTS.BRAND_USER);
	            	}, 2500);
	        		
	        }, (error) => {
	        		this.error = JSON.parse(error._body).message;
	        		this.buttonText = CONSTANTS.SUBMIT;
	        });
		} else {
			this.requiredFlag = true;
		}
    }
	
	back() {
		this.router.navigateByUrl('/' + ROUTS.BRAND_USER);
	}
	
	changeStatus(brandUser) {
		this.addBrandUserForm.controls['isActive'].setValue(brandUser);
	}
	
	matchPassword() {
    	
		if (this.addBrandUserForm.controls.password.value != this.addBrandUserForm.controls.confirmPassword.value)  {
			this.isPassWordMatched = false;
		} else {
			this.isPassWordMatched = true;
		}
    }
	
	password() {
	    this.showPas = !this.showPas;
	}
	
	confirmPassword() {
	    this.showConfirmPas = !this.showConfirmPas;
	}
}