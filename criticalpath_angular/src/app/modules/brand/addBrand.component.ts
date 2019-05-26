import {Component, OnInit } from '@angular/core';
import { RouterModule, Routes, Router } from '@angular/router';
import { ReactiveFormsModule, FormsModule, FormGroup, FormControl, Validators, FormBuilder } from '@angular/forms';
import { NoWhitespaceValidator } from '../../common/customValidator/custom.validator';

import * as ROUTS from '../../constants/routs';
import { CONSTANTS } from '../../constants/constant';

import { BrandService } from '../../service/brand.service'
import { CommonService } from '../../service/common.service';
import { LocalStorageService } from '../../service/localStorage.service';


@Component({
	selector :'',
	templateUrl : './addBrand.component.html'
})
export class AddBrandComponent implements OnInit {
	
	constructor(public router: Router, public brandService : BrandService,
				public commonService : CommonService,
				public localStorageService : LocalStorageService){}
	
	CONSTANTS : any = CONSTANTS;
    buttonText : string;
				
	addBrandForm: FormGroup;
	error : string;
	brandAdded : boolean = false;
	requiredFlag : boolean = false;
				
	ngOnInit() {
		
		this.commonService.validateUserByRole();
		
		this.buttonText = CONSTANTS.SUBMIT;
		
		this.addBrandForm = new FormGroup({
			name: new FormControl('', [Validators.required, NoWhitespaceValidator]),
			isActive: new FormControl(true, [])
      });
	}
	
	submitBrandForm(brandForm : any) {
    	
		this.error = '';
		if(this.addBrandForm.valid) {
			this.buttonText = CONSTANTS.SUBMITTING;
	        this.brandService.addBrand(brandForm).subscribe((results) => {
	        		this.brandAdded = true;
	        		setTimeout(() => {
	        			this.router.navigateByUrl('/' + ROUTS.BRAND);
	            	}, 2500);
	        		
	        }, (error) => {
	        		this.error = JSON.parse(error._body).message;
	        		this.buttonText = CONSTANTS.SUBMIT;
	        });
		} else 
			this.requiredFlag = true;
    }
	
	back() {
		this.router.navigateByUrl('/' + ROUTS.BRAND);
	}
	
	changeStatus(event) {
		this.addBrandForm.controls['isActive'].setValue (event);
	}
}