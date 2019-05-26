import {Component, OnInit } from '@angular/core';
import { RouterModule, Routes, Router, ActivatedRoute } from '@angular/router';
import { ReactiveFormsModule, FormsModule, FormGroup, FormControl, Validators, FormBuilder } from '@angular/forms';
import { NoWhitespaceValidator } from '../../common/customValidator/custom.validator';

import * as ROUTS from '../../constants/routs';
import { CONSTANTS } from '../../constants/constant';
import { DaysOfWeekService } from '../../service/daysOfWeek.service';
import { CommonService } from '../../service/common.service';

import { UiSwitchModule } from 'ngx-toggle-switch';

@Component({
	selector :'',
	templateUrl : './editDaysOfWeek.component.html'
})
export class EditDaysOfWeekComponent implements OnInit {
	
	constructor(public router : Router,
				public route: ActivatedRoute,
				public commonService : CommonService,
				public daysOfWeekService : DaysOfWeekService){}
	
	CONSTANTS : any = CONSTANTS;
	buttonText : string;
				
	editDaysOfWeekForm: FormGroup;
	public daysOfWeekId : number ;
	private sub : any;
	error : string;
	editedDaysOfWeek : boolean = false;
	requiredFlag : boolean = false;
				
	ngOnInit() {
		
		 this.commonService.validateUserByRole();
		
		 this.buttonText = CONSTANTS.SUBMIT;
		
		 this.sub = this.route.params.subscribe(params => {
             
             this.daysOfWeekId  = params['id'];
         });
		 
		this.editDaysOfWeekForm = new FormGroup({
			name: new FormControl('', [Validators.pattern("^[a-zA-Z ]*$") , Validators.required, NoWhitespaceValidator, Validators.minLength(3)])
		});
		
		this.daysOfWeekService.getDaysOfWeekData(this.daysOfWeekId).subscribe((results) => {
			if(results !== undefined)
				this.setFormData(results);
		});
		
	}
	
	setFormData(results) {
		
		this.editDaysOfWeekForm = new FormGroup({
			name: new FormControl(results.data.name, [Validators.required, NoWhitespaceValidator, Validators.minLength(3)]),
			id: new FormControl(results.data.id, [Validators.required])
		});
		
	}
	
	submitEditDaysOfWeekForm(editDaysOfWeekForm : any) {
    	
		this.error = '';
		
		if(this.editDaysOfWeekForm.valid) {
			this.buttonText = CONSTANTS.SUBMITTING;
	        this.daysOfWeekService.addDaysOfWeek(editDaysOfWeekForm).subscribe((results) => {
	        	this.editedDaysOfWeek = true;
	    		setTimeout(() => {
	    			this.router.navigateByUrl('/' + ROUTS.DAYS_OF_WEEK);
	        	}, 2500);
	        }, (error) => {
	        	this.error = JSON.parse(error._body).message;
	        	this.buttonText = CONSTANTS.SUBMIT;
	        });
		} else {
			if(this.editDaysOfWeekForm.controls['name'].value == '')
				this.requiredFlag = true;
		}
    }
	
	back() {
		this.router.navigateByUrl('/' + ROUTS.DAYS_OF_WEEK);
	}
}