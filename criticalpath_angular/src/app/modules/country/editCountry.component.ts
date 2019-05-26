import {Component, OnInit } from '@angular/core';
import { RouterModule, Routes, Router, ActivatedRoute } from '@angular/router';
import { ReactiveFormsModule, FormsModule, FormGroup, FormControl, Validators, FormBuilder } from '@angular/forms';
import { NoWhitespaceValidator } from '../../common/customValidator/custom.validator';
import * as ROUTS from '../../constants/routs';
import  { CONSTANTS } from '../../constants/constant';

import { CountryService } from '../../service/country.service';

import { UiSwitchModule } from 'ngx-toggle-switch';
import { CommonService } from '../../service/common.service';

@Component({
	selector :'',
	templateUrl : './editCountry.component.html'
})
export class EditCountryComponent implements OnInit {
	
	constructor(public router : Router,
				public route: ActivatedRoute,
				public countryService : CountryService,
				public commonService: CommonService){}
	
	CONSTANTS : any = CONSTANTS;
				
	editCountryForm: FormGroup;
	public countryId : number ;
	private sub : any;
	public isActivated : boolean = false;
	error : string;
    countryEdited : boolean = false;
	requiredFlag : boolean = false;
	buttonText : string;
				
	ngOnInit() {

	 	this.commonService.validateUserByRole();
		
		this.buttonText = CONSTANTS.SUBMIT;
		
		 this.sub = this.route.params.subscribe(params => {
             
             this.countryId  = params['id'];
         });
		 
		this.editCountryForm = new FormGroup({
			name: new FormControl('', [Validators.required, NoWhitespaceValidator]),
			code: new FormControl('', [Validators.required, NoWhitespaceValidator]),
			isActive: new FormControl('', [])
		});
		
		this.countryService.getCountryData(this.countryId).subscribe((results) => {
			if(results !== undefined)
				this.setFormData(results);
		});
		
	}
	
	setFormData(results) {
		
		this.editCountryForm = new FormGroup({
			name: new FormControl(results.data.name, [Validators.required, NoWhitespaceValidator]),
			code: new FormControl(results.data.code, [Validators.required, NoWhitespaceValidator]),
			isActive: new FormControl(results.data.isActive, []),
			id: new FormControl(results.data.id, [Validators.required])
		});
		
	}
	
	submitEditedCountryForm(countryForm : any) {
    	
		this.error = '';
		if(this.editCountryForm.valid) {
			this.buttonText = CONSTANTS.SUBMITTING;
	        this.countryService.addCountry(countryForm).subscribe((results) => {
	        	this.countryEdited = true;
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
		this.editCountryForm.controls['isActive'].setValue (event);
	}
	
	back() {
		this.router.navigateByUrl('/' + ROUTS.COUNTRY);
	}
}