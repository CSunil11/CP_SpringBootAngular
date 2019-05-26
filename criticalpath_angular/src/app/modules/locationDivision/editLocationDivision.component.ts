import {Component, OnInit } from '@angular/core';
import { RouterModule, Routes, Router, ActivatedRoute } from '@angular/router';
import { ReactiveFormsModule, FormsModule, FormGroup, FormControl, Validators, FormBuilder } from '@angular/forms';
import { NoWhitespaceValidator } from '../../common/customValidator/custom.validator';
import * as ROUTS from '../../constants/routs';
import { CONSTANTS } from '../../constants/constant';
import { LocationDivisionService } from '../../service/locationDivision.service';
import { BrandUserService } from '../../service/brandUser.service';
import { CountryService } from '../../service/country.service';
import { ProvinceService } from '../../service/province.service';
import { CommonService } from '../../service/common.service';
import { LocalStorageService } from '../../service/localStorage.service';

import { UiSwitchModule } from 'ngx-toggle-switch';


@Component({
	selector :'',
	templateUrl : './editLocationDivision.component.html'
})
export class EditLocationDivisionComponent implements OnInit {
	
	constructor(public router: Router,
				public route: ActivatedRoute,
				public brandUserService : BrandUserService,
				public countryService : CountryService,  public provinceService : ProvinceService, 
				public locationDivisionService : LocationDivisionService, 
				public commonService : CommonService,
				public localStorageService : LocalStorageService){}
	
	CONSTANTS : any = CONSTANTS;
	buttonText : string;
	editLocationDivisionForm: FormGroup;
	public locationDivisionId : number ;
	private sub : any;
	public isActivated : boolean = false;
	error : string;
	public DSMUser : any = {};	
	public RAMUser : any = {};
	locationDivisionEdited : boolean = false;
	requiredFlag : boolean = false;
	public DSMUserList : any[] = [];
	public RAMUserList : any[] = [];
	countryList : any[] = [];
	provinceList : any;
							
	ngOnInit() {
		
		this.commonService.validateUserByRole();
		
		this.buttonText = CONSTANTS.SUBMIT;
		
		this.editLocationDivisionForm = new FormGroup({
			name: new FormControl('', [Validators.required, NoWhitespaceValidator]),
			country: new FormControl('', [Validators.required]),
			province: new FormControl('', [Validators.required]),
			DSMUser: new FormControl(this.DSMUser),
//			RAMUser: new FormControl(this.RAMUser, [Validators.required]),
			isActive: new FormControl('', []),
			id: new FormControl('', [])
		});
		
		this.sub = this.route.params.subscribe(params => {
          
             this.locationDivisionId  =  params['id'];
         });
			
		this.countryService.getAllActiveCountry().subscribe((results) => {
				this.countryList = results.data;
		 }); 
		 
		this.locationDivisionService.getLocationDivisionData(this.locationDivisionId).subscribe((results) => {
			console.log(results.data);
			if(results.data.country !== null) {
				this.provinceService.getProvinceOfCountry(results.data.country.id).subscribe((results) => {
					this.provinceList = results.data;
				});
			} else {
				this.provinceList = [];
			}
		
			
			if(results !== undefined)
			{
				if(results.data.divisionalSalesManagers != null) {
					console.log("ddddd");
					this.DSMUser = {id: results.data.divisionalSalesManagers.id , text: results.data.divisionalSalesManagers.name};
					console.log(this.DSMUser);
				} else {
					this.DSMUser = {id: null , text: null};
				}
					

//				if(results.data.regionalManagers != null) {
//					this.RAMUser = {id: results.data.regionalManagers.id , text: results.data.regionalManagers.name};
//				} else {
//					this.RAMUser = {id: null , text: null};
//				}
					

				this.editLocationDivisionForm.controls['name'].setValue(results.data.name);
				this.editLocationDivisionForm.controls['isActive'].setValue(results.data.isActive);
				this.editLocationDivisionForm.controls['id'].setValue(results.data.id);
			}
				
				this.brandUserService.getAllDSmUser().subscribe((results) => {
					console.log(this.DSMUser);
					this.DSMUserList.push(this.DSMUser);
					console.log("111111");
					console.log(results.data);
					if(results.data.length > 0) {
						console.log("111111====");
						for(let i = 0; i < results.data.length ; i++) {
							this.DSMUserList.push({
							        id: results.data[i].id,
							        text: results.data[i].name
							      });
							
						}
						
					} 
				}, (error) => {
					
				});
				
//				this.brandUserService.getAllRamUser().subscribe((results) => {
//					console.log("222222");
//					console.log(results.data);
//					if(results.data.length > 0) {
//						for(let i = 0; i < results.data.length ; i++) {
//							this.RAMUserList.push({
//							        id: results.data[i].id,
//							        text: results.data[i].name
//							      });
//						}
//					}
//				}, (error) => {
//					
//				});
				
				setTimeout(() => {
					if(results.data.country !== null) {
						for( let countryIndex=0; countryIndex<this.countryList.length; countryIndex++) {
							if( this.countryList[countryIndex].id == results.data.country.id ) {
								this.editLocationDivisionForm.controls['country'].setValue(this.countryList[countryIndex]);
								break;
							}
						}
					}
					
					if(results.data.province !== null) {
						for( let provinceIndex=0; provinceIndex<this.provinceList.length; provinceIndex++) {
							if( this.provinceList[provinceIndex].id == results.data.province.id) {
								this.editLocationDivisionForm.controls['province'].setValue(this.provinceList[provinceIndex]);
								break;
							}
						}	
					}
				
				},500);
		});
					 
	}
	
		
	DSMUserOnSelect(event : any) {
		this.DSMUser = event;
	}
	
	
//	RAMUserOnSelect(event : any) {
//		this.RAMUser = event;
//	}
	
	getProvince() {
		let country = this.editLocationDivisionForm.controls['country'].value;
		this.provinceService.getProvinceOfCountry(country.id).subscribe((results) => {
			this.provinceList = results.data;
		}, (error) => {
			
		});
	}
	
	submitEditedLocationDivisionForm(locationDivisionForm : any) {    	
		this.error = '';

		if(this.editLocationDivisionForm.valid) {
			
			this.buttonText = CONSTANTS.SUBMITTING;
			
			let LocationDivisionFormJson = {
					'id':locationDivisionForm.id,
					'uid':this.DSMUser.id,
//					'ramuid':this.RAMUser.id,
					'cid':locationDivisionForm.country.id,
					'pid':locationDivisionForm.province.id,
					'name':this.editLocationDivisionForm.controls['name'].value,
					'status':this.editLocationDivisionForm.controls['isActive'].value
			} 

			this.locationDivisionService.addLocationDivision(LocationDivisionFormJson).subscribe((results) => {
			this.locationDivisionEdited = true;
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
		
		this.editLocationDivisionForm.controls['isActive'].setValue (event);
	}
				
	back() {
		
		this.router.navigateByUrl('/' + ROUTS.LOCATIONDIVISION);
	}
				
}
