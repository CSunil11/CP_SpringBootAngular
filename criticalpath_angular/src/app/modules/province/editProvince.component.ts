import {Component, OnInit } from '@angular/core';
import { RouterModule, Routes, Router, ActivatedRoute } from '@angular/router';
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
	templateUrl : './editProvince.component.html'
})
export class EditProvinceComponent implements OnInit {
	
	constructor(public router: Router,
				public route: ActivatedRoute,
				public provinceService : ProvinceService, 
				public countryService : CountryService,
				public commonService : CommonService,
				public localStorageService : LocalStorageService){}
	
	CONSTANTS : any = CONSTANTS;
	buttonText : string;
	editProvinceForm: FormGroup;
	public provinceId : number ;
	private sub : any;
	public isActivated : boolean = false;
	error : string;
	public country : any = {};			
	provinceEdited : boolean = false;
	requiredFlag : boolean = false;
	public countryNameList : any[] = [];
	public countryList : any[] = [];
							
	ngOnInit() {
		
		this.commonService.validateUserByRole();
		
		this.buttonText = CONSTANTS.SUBMIT;
		
		this.sub = this.route.params.subscribe(params => {
          
             this.provinceId  =  params['id'];
         });
					
		this.countryService.getAllActiveCountry().subscribe((results) => {
			if(results.data.length > 0) {
				this.countryList = results.data;
					for(var i = 0; i < results.data.length ; i++) {			
						this.countryNameList.push({
					        id: results.data[i].id,
					        text: results.data[i].name
					      });
					}
			}
		}, (error) => {
						
			});
					 
		this.editProvinceForm = new FormGroup({
			name: new FormControl('', [Validators.required, NoWhitespaceValidator]),
			country: new FormControl(this.country,[Validators.required]),
			isActive: new FormControl('', []),
			id:new FormControl('', [Validators.required])
		});
			
		
		
		this.provinceService.getProvinceData(this.provinceId).subscribe((results) => {
			if(results !== undefined) {
				console.log(results.data);
				if(results.data.country !== null) {
					this.country = {id: results.data.country.id , text: results.data.country.name};
					this.editProvinceForm.controls['country'].setValue(results.data.country.name);
					this.editProvinceForm.controls['name'].setValue(results.data.name);
					this.editProvinceForm.controls['id'].setValue(results.data.id);
					this.editProvinceForm.controls['isActive'].setValue(results.data.isActive);
				}else {
					this.country = {id: null, text: null};
					this.editProvinceForm.controls['name'].setValue(results.data.name);
					this.editProvinceForm.controls['id'].setValue(results.data.id);
					this.editProvinceForm.controls['isActive'].setValue(results.data.isActive);
				}
				
			}
//				this.country = {id: results.data.country.id , text: results.data.country.name};
//				this.setFormData(results);
				
		});
					
	}
				
	setFormData(results) {
					
		this.editProvinceForm = new FormGroup({
			name: new FormControl(results.data.name, [Validators.required, NoWhitespaceValidator]),
			country: new FormControl(results.data.country.name,),
			isActive: new FormControl(results.data.isActive, []),
			id: new FormControl(results.data.id, [Validators.required])
		});
					
	}
		
	countryOnSelect(event : any) {
		this.country = event;
	}
	
	submitEditedProvinceForm(provinceForm : any) {	    	
		this.error = '';
		if(this.editProvinceForm.valid) {
			this.buttonText = CONSTANTS.SUBMITTING;
			let ProvinceFormJson = {
					'id':provinceForm.id,
					'cid':this.country.id,
					'name':this.editProvinceForm.controls['name'].value,
					'isActive':this.editProvinceForm.controls['isActive'].value
			} 
			
			this.provinceService.addProvince(ProvinceFormJson).subscribe((results) => {
			this.provinceEdited = true;
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
		
		this.editProvinceForm.controls['isActive'].setValue (event);
	}
				
	back() {
		
		this.router.navigateByUrl('/' + ROUTS.PROVINCE);
	}
				
}
