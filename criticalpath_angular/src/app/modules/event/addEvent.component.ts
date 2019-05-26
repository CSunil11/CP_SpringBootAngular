import {Component, OnInit } from '@angular/core';
import { RouterModule, Routes, Router } from '@angular/router';
import { ReactiveFormsModule, FormsModule, FormGroup, FormControl, Validators, FormBuilder } from '@angular/forms';
import { NoWhitespaceValidator } from '../../common/customValidator/custom.validator';
import * as ROUTS from '../../constants/routs';
import { CONSTANTS } from '../../constants/constant';

import { EventService } from '../../service/event.service'
import { CommonService } from '../../service/common.service';
import { LocalStorageService } from '../../service/localStorage.service';
import { UiSwitchModule } from 'ngx-toggle-switch';


@Component({
	selector :'',
	templateUrl : './addEvent.component.html'
})
export class AddEventComponent implements OnInit {
	
	constructor(public router: Router, public eventService : EventService,
				public commonService : CommonService,
				public localStorageService : LocalStorageService){}
	
	CONSTANTS : any = CONSTANTS;
				
	addEventForm: FormGroup;
	error : string;
	eventAdded : boolean = false;
	requiredFlag : boolean = false;
	descriptionLength : number;			
	buttonText : string;
				
	ngOnInit() {
		
		this.commonService.validateUserByRole();
		
		this.buttonText = CONSTANTS.SUBMIT;
		
		if(!this.commonService.checkCookie(CONSTANTS.COOKIE_ACCESSTOKEN))
			this.router.navigateByUrl('/' + ROUTS.LOGIN);
		
		this.addEventForm = new FormGroup({
			name: new FormControl('', [Validators.required, NoWhitespaceValidator, Validators.minLength(3)]),
			description: new FormControl('', []),
			isActive: new FormControl(true, [])
      });
	}
	
	countLength(){
		let descriptionvalue = this.addEventForm.controls['description'].value;
		
		this.descriptionLength = descriptionvalue.length;
	
	}
	
	submitEventForm(eventForm : any) {
    	
		this.error = '';
		
		if(this.addEventForm.valid) {
			this.buttonText = CONSTANTS.SUBMITTING;
	        this.eventService.addEvent(eventForm).subscribe((results) => {
	        		this.eventAdded = true;
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
		this.addEventForm.controls['isActive'].setValue (event);
	}
}