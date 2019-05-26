import {Component, OnInit } from '@angular/core';
import { RouterModule, Routes, Router, ActivatedRoute } from '@angular/router';
import { ReactiveFormsModule, FormsModule, FormGroup, FormControl, Validators, FormBuilder } from '@angular/forms';
import { NoWhitespaceValidator } from '../../common/customValidator/custom.validator';
import * as ROUTS from '../../constants/routs';
import { CONSTANTS } from '../../constants/constant';
import { TaskStatusService } from '../../service/taskStatus.service';
import { CommonService } from '../../service/common.service';

@Component({
	selector :'',
	templateUrl : './editTaskStatus.component.html'
})
export class EditTaskStatusComponent implements OnInit {
	
	constructor(public router : Router,
				public route: ActivatedRoute,
				public commonService : CommonService,
				public taskStatusService : TaskStatusService){}
	
	editTaskStatusForm: FormGroup;
	public taskStatusId : number ;
	private sub : any;
				
	public taskStatusName : string;
	taskEdited : boolean = false;
	error : string;		
	requiredFlag : boolean = false;
	descriptionLength : number;
				
	CONSTANTS : any = CONSTANTS;
	buttonText : string;
				
	ngOnInit() {
		
		this.commonService.validateUserByRole();
		
		 this.buttonText = CONSTANTS.SUBMIT;
		
		 this.sub = this.route.params.subscribe(params => {
             
             this.taskStatusId  = params['id'];
         });
		 
		this.editTaskStatusForm = new FormGroup({
			name: new FormControl('', [Validators.required, NoWhitespaceValidator]),
			description: new FormControl('', []),
			
		});
		
		this.taskStatusService.getTaskStatusData(this.taskStatusId).subscribe((results) => {
			if(results !== undefined)
				this.setFormData(results);
		});
		
	}
	
	setFormData(results) {
		
		this.editTaskStatusForm = new FormGroup({
			name: new FormControl(results.data.name, [Validators.required, NoWhitespaceValidator]),
			description: new FormControl(results.data.description, []),
			id: new FormControl(results.data.id, [])
		});
		this.descriptionLength = results.data.description.length;
	}
	
	countLength(){
		let addTaskStatusForm = this.editTaskStatusForm.controls['description'].value;
		
		this.descriptionLength = addTaskStatusForm.length;
	}
	
	back() {
		this.router.navigateByUrl('/' + ROUTS.TASK_STATUS);
	}
	
	submitEditedTaskStatusForm(taskStatusForm : any) {
    	
		this.error = '';
		
		 if(this.editTaskStatusForm.valid) {
			this.buttonText = CONSTANTS.SUBMITTING;
	        this.taskStatusService.addTaskStatus(taskStatusForm).subscribe((results) => {
	        	this.taskEdited = true;
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