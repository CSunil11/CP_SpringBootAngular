import {Component, OnInit } from '@angular/core';
import { RouterModule, Routes, Router, ActivatedRoute } from '@angular/router';
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
	templateUrl : './editBrandUser.component.html'
})
export class EditBrandUserComponent implements OnInit {
	
	constructor(public router: Router, public route: ActivatedRoute, public brandUserService : BrandUserService, public storeService  : StoreService,
				public commonService : CommonService, public brandService : BrandService, 
				public localStorageService : LocalStorageService){}
	
	CONSTANTS : any = CONSTANTS;
	buttonText : string;
	editBrandUserForm: FormGroup;
	brandUserRequest : BrandUserRequest;
				
	error : string;
	public brand : string;
	private sub : any;
	public brandData : any;
	public storeData : any;
	public brandUserId : number ;
	
	public storeList : any[] = [];
	public stores : any[] = [];
	public brandUserData : any;
				
	public brandUserEdited : boolean = false;
	public requiredFlag : boolean = false;
	public flag : boolean = false;
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
		
		this.sub = this.route.params.subscribe(params => {
            
            this.brandUserId  = params['id'];
        });
		
		
		this.storeService.getStoreByBrandId(this.localStorageService.getItem(CONSTANTS.BRAND_ID)).subscribe((results) => {
			if(results && results.data.length > 0) {
				this.storeList = results.data;
			}
		}, (error) => {
			
		});
		
		this.editBrandUserForm = new FormGroup({
			brand: new FormControl(this.brand, []),
			name: new FormControl('', [Validators.required, NoWhitespaceValidator]),
			email: new FormControl('', [Validators.pattern("^[_\.0-9A-Za-z-]+@([0-9A-Za-z][0-9A-Za-z.-]+\.)+[A-Za-z]{2,4}$"), Validators.required]),
			phone: new FormControl('', [Validators.required, NoWhitespaceValidator, Validators.pattern("^[0-9]+")]),
			password : new FormControl('',[Validators.required, NoWhitespaceValidator]),
			confirmPassword : new FormControl('',[Validators.required, NoWhitespaceValidator]),
			role: new FormControl('',[Validators.required]),
			store: new FormControl(null,[]),
			isActive: new FormControl(true, []),
			id: new FormControl('', []),
			userLogin: new FormControl('', [])
		});
		
		this.brand = this.localStorageService.getItem(CONSTANTS.BRAND);
	
		this.brandUserService.getBrandUserData(this.brandUserId).subscribe((results) => {
			
			this.editBrandUserForm.controls['brand'].setValue(this.brand);
			this.editBrandUserForm.controls['name'].setValue(results.data.name);
			this.editBrandUserForm.controls['phone'].setValue(results.data.phone);
			this.editBrandUserForm.controls['email'].setValue(results.data.userLogin.email);
			this.editBrandUserForm.controls['password'].setValue(results.data.userLogin.password);
			this.editBrandUserForm.controls['confirmPassword'].setValue(results.data.userLogin.password);
			this.editBrandUserForm.controls['isActive'].setValue(results.data.userLogin.isActive);
			this.editBrandUserForm.controls['id'].setValue(results.data.id);
			this.editBrandUserForm.controls['userLogin'].setValue(results.data.userLogin);
			
			let userLogin = this.editBrandUserForm.controls['userLogin'].value;
			userLogin.authorities = null;
			
			let roleKey = this.keys(true);
			for(var i = 0 ; i< roleKey.length ;i++) {
				if(roleKey[i] == results.data.userLogin.role) { 
					this.editBrandUserForm.controls['role'].setValue(Object.values(this.role)[i]);
				}
			}
			
			this.editBrandUserForm.controls['userLogin'].setValue(userLogin);
			this.checkRole();
			
			if(results.data.store != null)
				this.editBrandUserForm.controls['store'].setValue([results.data.store]);
		}, (error) => {
			
		});
		
		
		
		this.brandService.getBrandData(this.localStorageService.getItem(CONSTANTS.BRAND_ID)).subscribe((results) => {
			if(results.data) {
				
				this.brandData = results.data;
			}
		}, (error) => {
			
		});
		
		
	}
	
	//for set validation message//
	countLength(){
			let count =  this.editBrandUserForm.controls['phone'].value;
			if(count.length > 0) {
				this.flag = false;
			}else {
				this.flag = true;
				this.requiredFlag = true;
			}
	}
	
	brandOnSelect(event : any) {
		let brandName = event;
		this.storeList = [];
		this.editBrandUserForm.controls['store'].setValue([]);
		
		for(var i =0; i<this.brandsObjList.length ; i++){
			if(brandName == this.brandsObjList[i].name){
				this.localStorageService.setItem(CONSTANTS.BRAND, this.brandsObjList[i].name);
				this.localStorageService.setItem(CONSTANTS.BRAND_ID, JSON.stringify(this.brandsObjList[i].id));

				this.storeService.getStoreByBrandId(this.localStorageService.getItem(CONSTANTS.BRAND_ID)).subscribe((results) => {
					if(results && results.data.length > 0) {
						let storeObj;
						storeObj =results.data
						this.storeList = storeObj;
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
	
	keys(returnKey) : Array<string> {
		
		if(returnKey)
			return  Object.keys(this.role);
		else {
		    const keys = Object.keys(this.role);
		    return keys.map(el => Object(this.role)[el]);
		}
    }
	
	onSelect(event : Event) {
		this.suggestion = false;
	}
	
	checkRole() {
		
		if(this.editBrandUserForm.controls['role'].value == this.role.ROLE_SM_USER) {
			this.isStoreManagerRole = true;
			this.editBrandUserForm.get('store').setValidators([Validators.required]);
			this.editBrandUserForm.updateValueAndValidity();
		} else {
			this.isStoreManagerRole = false;
			this.editBrandUserForm.get('store').clearValidators();
			this.editBrandUserForm.updateValueAndValidity();
			this.editBrandUserForm.controls['store'].setValue(null);
		}
	}
	
	submitForm(brandUserForm : any) {
    	
		let roleValues = this.keys(false);
		let roleKeys = this.keys(true);
		
		this.error = '';
		
		if(this.editBrandUserForm.valid) {
			
			this.buttonText = CONSTANTS.SUBMITTING;
			
			this.brandUserRequest = new BrandUserRequest(brandUserForm);
			
			this.brandUserRequest['brand'] = this.brandData;
			
			if(this.editBrandUserForm.controls['store'].value != null)
				this.brandUserRequest['store'] = this.editBrandUserForm.controls['store'].value[0];
			
			this.brandUserRequest['id'] = this.editBrandUserForm.controls['id'].value;
			
			this.brandUserRequest['userLogin'] = this.editBrandUserForm.controls['userLogin'].value;
			
			let userLogin = this.editBrandUserForm.controls['userLogin'].value;
			
			userLogin.email = this.editBrandUserForm.controls['email'].value;
			userLogin.password = this.editBrandUserForm.controls['password'].value;
			userLogin.isActive = this.editBrandUserForm.controls['isActive'].value;
			
			for(var i = 0 ; i< roleValues.length ;i++) {
				if(roleValues[i] == this.editBrandUserForm.controls['role'].value) 
					this.brandUserRequest['role'] = Object.keys(this.role)[i];
			}
			
			userLogin.role = this.brandUserRequest['role'];
			
			this.brandUserRequest['userLogin'] = userLogin;
			
	        this.brandUserService.addBrandUser(this.brandUserRequest).subscribe((results) => {
	        		this.brandUserEdited = true;
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
		this.editBrandUserForm.controls['isActive'].setValue(brandUser);
	}
	
	matchPassword() {
    	
		if (this.editBrandUserForm.controls.password.value != this.editBrandUserForm.controls.confirmPassword.value)  {
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