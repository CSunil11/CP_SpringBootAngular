import {Component, OnInit } from '@angular/core';
import { RouterModule, Routes, Router, ActivatedRoute } from '@angular/router';
import { ReactiveFormsModule, FormsModule, FormGroup, FormControl, Validators, FormBuilder } from '@angular/forms';
import { NoWhitespaceValidator } from '../../common/customValidator/custom.validator';
import * as ROUTS from '../../constants/routs';
import { CONSTANTS } from '../../constants/constant';
import { StoreService } from '../../service/store.service';
import { CountryService } from '../../service/country.service';
import { BrandService } from '../../service/brand.service';
import { ProvinceService } from '../../service/province.service';
import { LocationDivisionService } from '../../service/locationDivision.service';
import { BrandUserService } from '../../service/brandUser.service';
import { CommonService } from '../../service/common.service';

@Component({
	selector :'',
	templateUrl : './editStore.component.html'
})
export class EditStoreComponent implements OnInit {
	
	constructor(public route: ActivatedRoute,public router: Router,
				public countryService : CountryService, public locationDivisionService : LocationDivisionService,
				public brandService : BrandService, public provinceService : ProvinceService, public brandUserService : BrandUserService,
				public commonService : CommonService,
				public storeService : StoreService){}
				
	
	CONSTANTS : any = CONSTANTS;
	buttonText : string;
	editStoreForm: FormGroup;
	public storeId : number ;
	private sub : any;
	error : string;
	editedStore : boolean = false;
    countryList : any[] = [];
	brandList : any[] = [];
    requiredFlag : boolean = false;
	provinceList : any;
	locDivList : any;
	public RAMUserList : any[] = [];
				
	ngOnInit() {
		
		 this.commonService.validateUserByRole();
		
		 this.buttonText = CONSTANTS.SUBMIT;
		
		 this.sub = this.route.params.subscribe(params => {
             
             this.storeId  = params['id'];
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
			}, (error) => {
				
			});
		 
//		this.locationDivisionService.getAllActiveLocationDivision().subscribe((results) => {
//				this.locDivList = results.data;
//		 }); 
		
		this.editStoreForm = new FormGroup({
			code: new FormControl('', [Validators.required, NoWhitespaceValidator]),//, Validators.pattern("^[a-zA-Z]*$")
			name: new FormControl('', [Validators.required, NoWhitespaceValidator]),
			country: new FormControl('', [Validators.required]),
			brand: new FormControl('', [Validators.required]),
			province: new FormControl('', [Validators.required]),
			locationDivision: new FormControl('', [Validators.required]),
			regionalManagers: new FormControl('', [Validators.required]),
			isActive: new FormControl('', []),
			id: new FormControl('', [])
		});
		
		this.storeService.getStoreData(this.storeId).subscribe((results) => {
			console.log(results.data);
			if(results.data.country !== null) {
				this.provinceService.getProvinceOfCountry(results.data.country.id).subscribe((results) => {
					this.provinceList = results.data;
				});
			} else {
				this.provinceList =[];
			}
			
			
			if(results.data.province !== null) {
				this.locationDivisionService.getLocationDivisionOfProvince(results.data.province.id).subscribe((results) => {
					this.locDivList = results.data;
				}, (error) => {
					
				});
			} else {
				this.locDivList =[];
			}
			
		
			console.log(results.data);
			if(results !== undefined) {
				this.editStoreForm.controls['code'].setValue(results.data.code);
				this.editStoreForm.controls['name'].setValue(results.data.name);
				this.editStoreForm.controls['isActive'].setValue(results.data.isActive);
				this.editStoreForm.controls['id'].setValue(results.data.id);
				
				setTimeout(() => {
					if(results.data.country !== null) {
						for( let countryIndex=0; countryIndex<this.countryList.length; countryIndex++) {
							if( this.countryList[countryIndex].id == results.data.country.id ) {
								this.editStoreForm.controls['country'].setValue(this.countryList[countryIndex]);
								break;
							}
						}
					}
					
					if(results.data.province !== null) {
						for( let provinceIndex=0; provinceIndex<this.provinceList.length; provinceIndex++) {
							if( this.provinceList[provinceIndex].id == results.data.province.id) {
								this.editStoreForm.controls['province'].setValue(this.provinceList[provinceIndex]);
								break;
							}
						}
					}
					
					if(results.data.brand !== null) {
						for( let brandIndex=0; brandIndex<this.brandList.length; brandIndex++) {
							if( this.brandList[brandIndex].id == results.data.brand.id) {
								this.editStoreForm.controls['brand'].setValue(this.brandList[brandIndex]);
								break;
							}
						}
					}
					
					if(results.data.locationDivision !== null) {
						for( let locDivIndex=0; locDivIndex<this.locDivList.length; locDivIndex++) {
							if( this.locDivList[locDivIndex].id == results.data.locationDivision.id ) {
								let locDiv = this.locDivList[locDivIndex];
								locDiv.divisionalSalesManagers = null;
								this.editStoreForm.controls['locationDivision'].setValue(locDiv);
								break;
							}
						}
					}
					
					if(results.data.regionalManagers !== null) {
						for( let regManIndex=0; regManIndex<this.RAMUserList.length; regManIndex++) {
							if( this.RAMUserList[regManIndex].id == results.data.regionalManagers.id) {
								this.editStoreForm.controls['regionalManagers'].setValue(this.RAMUserList[regManIndex]);
								break;
							}
						}
					}
					
				},500);
			}
		});
	}
	
	removeUser() {
		let locDiv = this.editStoreForm.controls['locationDivision'].value;
		locDiv.divisionalSalesManagers = null;
//		locDiv.regionalManagers = null;
		this.editStoreForm.controls['locationDivision'].setValue(locDiv);
	}
	
	getProvince() {
		let country = this.editStoreForm.controls['country'].value;
		this.editStoreForm.controls['province'].setValue('');
		this.editStoreForm.controls['locationDivision'].setValue('');
		this.provinceList = null;
		this.provinceService.getProvinceOfCountry(country.id).subscribe((results) => {
			this.provinceList = results.data;
		}, (error) => {
			
		});
		this.locDivList = null;
	}
	
	getLocationDivision(){

		let province = this.editStoreForm.controls['province'].value;
		this.locDivList = null;
		this.locationDivisionService.getLocationDivisionOfProvince(province.id).subscribe((results) => {
			this.locDivList = results.data;
			this.editStoreForm.controls['locationDivision'].setValue('');
		}, (error) => {
			
		});
	}
	
	
	back() {
		this.router.navigateByUrl('/' + ROUTS.STORE);
	}
	
	submitEditedStoreForm(storeForm : any) {
    	this.error = '';
    	
    	if(this.editStoreForm.valid) {
    		this.buttonText = CONSTANTS.SUBMITTING;
	        this.storeService.addStore(storeForm).subscribe((results) => {
	        	this.editedStore = true;
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
		this.editStoreForm.controls['isActive'].setValue (event);
	}
}