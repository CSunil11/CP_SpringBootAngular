import {Component, OnInit } from '@angular/core';
import { RouterModule, Routes, Router } from '@angular/router';
import { ReactiveFormsModule, FormsModule, FormGroup, FormControl, Validators, FormBuilder } from '@angular/forms';
import { NoWhitespaceValidator } from '../../common/customValidator/custom.validator';

import * as ROUTS from '../../constants/routs';
import { CONSTANTS } from '../../constants/constant';

import { WebhookService } from '../../service/webhook.service'
import { EventService } from '../../service/event.service'
import { CommonService } from '../../service/common.service';
import { LocalStorageService } from '../../service/localStorage.service';

import { UiSwitchModule } from 'ngx-toggle-switch';
import { MatAutocompleteModule, MatButtonModule,MatFormFieldModule,MatInputModule, MatRippleModule } from '@angular/material';

import {Observable} from 'rxjs';
import {map, startWith} from 'rxjs/operators';

import { TypeaheadModule } from 'ngx-bootstrap/typeahead';

declare var $ : any;
@Component({
	selector :'',
	templateUrl : './addWebhook.component.html'
})
export class AddWebhookComponent implements OnInit {
	
	constructor(public router: Router, public webhookService : WebhookService, public eventService : EventService,
				public commonService : CommonService,
				public localStorageService : LocalStorageService){}
	
	addWebhookForm: FormGroup;
	error : string;
	webhookAdded : boolean = false;
	eventList: string[] = [];
	noResult :boolean = false;
	requiredFlag : boolean = false;
    eventFlag : boolean = false;
	suggestion : boolean = false;
				
	CONSTANTS : any = CONSTANTS;
	buttonText : string;
				
	ngOnInit() {
		
		this.commonService.validateUserByRole();
		
		this.buttonText = CONSTANTS.SUBMIT;
		
		this.addWebhookForm = new FormGroup({
			url: new FormControl('', [Validators.required, NoWhitespaceValidator]),
			method: new FormControl('GET', []),
			event: new FormControl('', [Validators.required, NoWhitespaceValidator]),
			isActive: new FormControl(true, [])
      });
		
		this.eventService.getAllActiveEvent().subscribe((results) => {
			 for(var i = 0; i < results.data.length ; i++) {
				 this.eventList.push(results.data[i].name.toLowerCase());
			 }
		});
	}
	
	submitWebhookForm(addWebhookForm : any) {
    	
		this.error = '';
		
		 if(this.addWebhookForm.valid && !this.eventFlag) {
			 this.buttonText = CONSTANTS.SUBMITTING;
			 this.webhookService.addWebhook(addWebhookForm).subscribe((results) => {
				 this.requiredFlag = false;
	        		this.webhookAdded = true;
	        		setTimeout(() => {
	        			this.router.navigateByUrl('/' + ROUTS.WEBHOOK);
	            	}, 2500);
	        		
	        }, (error) => {
	        		this.error = JSON.parse(error._body).message;
	        		this.buttonText = CONSTANTS.SUBMIT;
	        });
		 } else {
			 this.requiredFlag = true;
			 if(this.eventFlag)
				 this.suggestion = true;
			 else
				 this.suggestion = false;
		 }
    }
	
	typeaheadOnSelect(event : Event) {
		this.eventFlag = false;
		this.suggestion = false;
		
	}
	
	typeaheadNoResults(event : any) {
		this.noResult = event;
	}
	
	back() {
		this.router.navigateByUrl('/' + ROUTS.WEBHOOK);
	}
	
	searchEvent() {
		this.eventFlag = true;
	}
	
	changeStatus(webhook) {
		this.addWebhookForm.controls['isActive'].setValue (webhook);
	}
}