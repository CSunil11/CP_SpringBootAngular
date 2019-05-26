import {Component, OnInit } from '@angular/core';
import { RouterModule, Routes, Router, ActivatedRoute } from '@angular/router';
import { ReactiveFormsModule, FormsModule, FormGroup, FormControl, Validators, FormBuilder } from '@angular/forms';
import { NoWhitespaceValidator } from '../../common/customValidator/custom.validator';
import * as ROUTS from '../../constants/routs';
import { CONSTANTS } from '../../constants/constant';
import { BrandService } from '../../service/brand.service';
import { CommonService } from '../../service/common.service';

//import { UiSwitchModule } from 'ngx-toggle-switch';

@Component({
	selector :'',
	templateUrl : './editBrand.component.html'
})
export class EditBrandComponent implements OnInit {
	
	constructor(public router : Router,
				public route: ActivatedRoute,
				public commonService : CommonService,
				public brandService : BrandService){}
	
	CONSTANTS : any = CONSTANTS;
	buttonText : string;
				
	editBrandForm: FormGroup;
	public brandId : number ;
	private sub : any;
	error : string;
	editedBrand : boolean = false;
    requiredFlag : boolean = false;
				
	ngOnInit() {
		
		this.commonService.validateUserByRole();
		
		 this.buttonText = CONSTANTS.SUBMIT;
		
		 this.sub = this.route.params.subscribe(params => {
             
             this.brandId  = params['id'];
         });
		 
		this.editBrandForm = new FormGroup({
			name: new FormControl('', [Validators.required, NoWhitespaceValidator]),
			isActive: new FormControl('', [])
		});
		
		this.brandService.getBrandData(this.brandId).subscribe((results) => {
			if(results !== undefined)
				this.setFormData(results);
		});
		
	}
	
	setFormData(results) {
		
		this.editBrandForm = new FormGroup({
			name: new FormControl(results.data.name, [Validators.required, NoWhitespaceValidator]),
			isActive: new FormControl(results.data.isActive, []),
			id: new FormControl(results.data.id, [])
		});
		
	}
	
	submitEditedBrandForm(brandForm : any) {
    	this.error = '';
    	
    	 if(this.editBrandForm.valid) {
    		this.buttonText = CONSTANTS.SUBMITTING;
	        this.brandService.addBrand(brandForm).subscribe((results) => {
	        	this.editedBrand = true;
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
		this.editBrandForm.controls['isActive'].setValue (event);
	}
}