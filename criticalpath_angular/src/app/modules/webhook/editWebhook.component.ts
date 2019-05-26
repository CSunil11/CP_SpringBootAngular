import {Component, OnInit } from '@angular/core';
import { RouterModule, Routes, Router, ActivatedRoute } from '@angular/router';
import { ReactiveFormsModule, FormsModule, FormGroup, FormControl, Validators, FormBuilder } from '@angular/forms';
import { MatAutocompleteModule, MatButtonModule,MatFormFieldModule,MatInputModule, MatRippleModule } from '@angular/material';
import * as ROUTS from '../../constants/routs';
import { CONSTANTS } from '../../constants/constant';
import { NoWhitespaceValidator } from '../../common/customValidator/custom.validator';

import { WebhookService } from '../../service/webhook.service';
import { EventService } from '../../service/event.service';
import { CommonService } from '../../service/common.service';

import { UiSwitchModule } from 'ngx-toggle-switch';

import {Observable} from 'rxjs';
import {map, startWith} from 'rxjs/operators';


@Component({
	selector :'',
	templateUrl : './editWebhook.component.html'
})
export class EditWebhookComponent implements OnInit {
	
	constructor(public router : Router,
				public route: ActivatedRoute,
				public webhookService : WebhookService,
				public commonService : CommonService,
				public eventService : EventService){}
	
	editWebhookForm: FormGroup;
	public webhookId : number ;
	private sub : any;
	error : string;
	editedWebhook : boolean = false;
	eventList: string[] = [];
	requiredFlag : boolean = false;
	noResult :boolean = false;
    eventFlag : boolean = false;
	suggestion : boolean = false;
	
	CONSTANTS : any = CONSTANTS;
	buttonText : string;
				
	ngOnInit() {
		
		 this.commonService.validateUserByRole();
		
		 this.buttonText = CONSTANTS.SUBMIT;
		
		 this.sub = this.route.params.subscribe(params => {
             
             this.webhookId  = params['id'];
         });
		 
		 this.editWebhookForm = new FormGroup({
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
		
		this.webhookService.getWebhookData(this.webhookId).subscribe((results) => {
			if(results !== undefined)
				this.setFormData(results);
		});
	}
	
	setFormData(results) {
		
		this.editWebhookForm = new FormGroup({
			url: new FormControl(results.data.url, [Validators.required, NoWhitespaceValidator]),
			method: new FormControl(results.data.method, []),
			event: new FormControl(results.data.event.name, [Validators.required, NoWhitespaceValidator]),
			isActive: new FormControl(results.data.isActive, []),
			id: new FormControl(results.data.id, [])
		});
		
	}
	
	submitEditedWebhookForm(editWebhookForm : any) {
    	
		this.error = '';
		this.noResult = false;
		
		if(this.editWebhookForm.valid && !this.eventFlag) {
			this.buttonText = CONSTANTS.SUBMITTING;
	        this.webhookService.addWebhook(editWebhookForm).subscribe((results) => {
	        	this.requiredFlag = false;
	        	this.editedWebhook = true;
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
	
	typeaheadNoResults(event : any) {
		this.eventFlag = true;
	}
	
	typeaheadOnSelect(event : Event) {
		this.eventFlag = false;
		this.suggestion = false;
	}
	
	back() {
		this.router.navigateByUrl('/' + ROUTS.WEBHOOK);
	}
	
	searchEvent() {
		this.eventFlag = true;
	}
	
	changeStatus(event) {
		this.editWebhookForm.controls['isActive'].setValue (event);
	}
}