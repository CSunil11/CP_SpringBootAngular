import {Component, OnInit } from '@angular/core';
import { RouterModule, Routes, Router, ActivatedRoute } from '@angular/router';
import { ReactiveFormsModule, FormsModule, FormGroup, FormControl, Validators, FormBuilder } from '@angular/forms';
import { NoWhitespaceValidator } from '../../common/customValidator/custom.validator';

import * as ROUTS from '../../constants/routs';
import { CONSTANTS } from '../../constants/constant';
import { EventService } from '../../service/event.service';
import { CommonService } from '../../service/common.service';

import { UiSwitchModule } from 'ngx-toggle-switch';

@Component({
	selector :'',
	templateUrl : './editEvent.component.html'
})
export class EditEventComponent implements OnInit {
	
	constructor(public router : Router,
				public route: ActivatedRoute,
				public commonService : CommonService,
				public eventService : EventService){}
	
	CONSTANTS : any = CONSTANTS;
	buttonText : string;
				
	editEventForm: FormGroup;
	public eventId : number ;
	private sub : any;
	error : string;
	editedEvent : boolean = false;
	requiredFlag : boolean = false;
	descriptionLength : number;		
				
	ngOnInit() {
		
		this.commonService.validateUserByRole();
		
		 this.buttonText = CONSTANTS.SUBMIT;
		 this.sub = this.route.params.subscribe(params => {
             
             this.eventId  = params['id'];
         });
		 
		this.editEventForm = new FormGroup({
			name: new FormControl('', [Validators.required, NoWhitespaceValidator, Validators.minLength(3)]),
			description: new FormControl('', []),
			isActive: new FormControl('', [])
		});
		
		this.eventService.getEventData(this.eventId).subscribe((results) => {
			if(results !== undefined)
				this.setFormData(results);
		});
		
	}
	
	countLength(){
		let descriptionvalue = this.editEventForm.controls['description'].value;
		
		this.descriptionLength = descriptionvalue.length;
	
	}
	
	setFormData(results) {
		
		this.editEventForm = new FormGroup({
			name: new FormControl(results.data.name, [Validators.required, NoWhitespaceValidator, Validators.minLength(3)]),
			description: new FormControl(results.data.description, []),
			isActive: new FormControl(results.data.isActive, []),
			id: new FormControl(results.data.id, [Validators.required])
		});
		
		this.descriptionLength = results.data.description.length;
		
	}
	
	submitEditedEventForm(eventForm : any) {
    	
		this.error = '';
		
		if(this.editEventForm.valid) {
			this.buttonText = CONSTANTS.SUBMITTING;
	        this.eventService.addEvent(eventForm).subscribe((results) => {
	        	this.editedEvent = true;
	    		setTimeout(() => {
	    			this.router.navigateByUrl('/' + ROUTS.EVENT);
	        	}, 2500);
	        }, (error) => {
	        	this.error = JSON.parse(error._body).message;
	        	this.buttonText = CONSTANTS.SUBMIT;
	        });
		} else
			this.requiredFlag = true;
    }
	
	back() {
		this.router.navigateByUrl('/' + ROUTS.EVENT);
	}

	changeStatus(event) {
		this.editEventForm.controls['isActive'].setValue (event);
	}
}