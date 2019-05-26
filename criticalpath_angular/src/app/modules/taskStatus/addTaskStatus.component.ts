import {Component, OnInit } from '@angular/core';
import { RouterModule, Routes, Router } from '@angular/router';
import { ReactiveFormsModule, FormsModule, FormGroup, FormControl, Validators, FormBuilder } from '@angular/forms';

import { NoWhitespaceValidator } from '../../common/customValidator/custom.validator';

import * as ROUTS from '../../constants/routs';
import { CONSTANTS } from '../../constants/constant';

import { TaskStatusService } from '../../service/taskStatus.service';
import { CommonService } from '../../service/common.service';

@Component({
	selector :'',
	templateUrl : './addTaskStatus.component.html'
})
export class AddTaskStatusComponent implements OnInit {
	
	constructor(public router: Router, public taskStatusService : TaskStatusService,
				public commonService : CommonService){}
	
	addTaskStatusForm: FormGroup;
	error: string;
	taskAdded : boolean = false;
	requiredFlag : boolean = false;
	descriptionLength : number;
	
	CONSTANTS : any = CONSTANTS;
	buttonText : string;
	
	ngOnInit() {
		
		this.commonService.validateUserByRole();
		
		this.buttonText = CONSTANTS.SUBMIT;
		
		if(!this.commonService.checkCookie(CONSTANTS.COOKIE_ACCESSTOKEN))
			this.router.navigateByUrl('/' + ROUTS.LOGIN);
		
		this.addTaskStatusForm = new FormGroup({
			name: new FormControl('', [Validators.required, NoWhitespaceValidator]),
			description: new FormControl('', [])
      });
	}
	
	back() {
		this.router.navigateByUrl('/' + ROUTS.TASK_STATUS);
	}
	
	countLength(){
		let descriptionvalue = this.addTaskStatusForm.controls['description'].value;
		
		this.descriptionLength = descriptionvalue.length;
	}
	
	submitTaskStatusForm(taskStatusForm : any) {
    	
		this.error = '';
		 if(this.addTaskStatusForm.valid) {
			this.buttonText = CONSTANTS.SUBMITTING;
	        this.taskStatusService.addTaskStatus(taskStatusForm).subscribe((results) => {
	        	this.taskAdded = true;
	        	setTimeout(() => {
	        		this.router.navigateByUrl('/' + ROUTS.TASK_STATUS);
	        	},2500);
	        }, (error) => {
	        		this.error = JSON.parse(error._body).message;
	        		this.buttonText = CONSTANTS.SUBMIT;
	        });
		 } else
			 this.requiredFlag = true;
    }
	
}