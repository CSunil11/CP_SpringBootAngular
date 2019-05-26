import {Component, OnInit } from '@angular/core';
import { RouterModule, Routes, Router } from '@angular/router';
import { ReactiveFormsModule, FormsModule, FormGroup, FormControl, Validators, FormBuilder } from '@angular/forms';
import { NoWhitespaceValidator } from '../../common/customValidator/custom.validator';
import * as ROUTS from '../../constants/routs';
import { CONSTANTS } from '../../constants/constant';

import { ManageNotificationService } from '../../service/manageNotification.service';
import { CommonService } from '../../service/common.service';
import { NotificationRequest } from '../../model/notificationRequest';

import * as ClassicEditor from '@ckeditor/ckeditor5-build-classic';
declare var $: any;

@Component({
	selector :'',
	templateUrl : './addManageNotification.component.html', 
	styleUrls: [ '../../app.component.css' ]
})
export class AddManageNotificationComponent implements OnInit {
	
	constructor(public router: Router, public manageNotificationService : ManageNotificationService, public commonService : CommonService,){}
	
	CONSTANTS : any = CONSTANTS;
	descriptionLength : number;
	notificationAdded : boolean = false;
	addNotificationForm : FormGroup;
	buttonText : any;
	userGroupList : any[] = [];
	error : string;
	requiredFlag : boolean = false;
	
	public Editor = ClassicEditor;
	notificationRequest : NotificationRequest;
	
	//Initialized the array with static values
	roles : any[] = ['RAM', 'DSM', 'SM'];
	eventList : any[] = ['Cycle', 'Task'];
	typeList : any[] = ['Escalation', 'Reminder'];
	
	ngOnInit() {
		
		this.commonService.validateUserByRole();
		
		this.buttonText = CONSTANTS.SUBMIT;
		
		this.addNotificationForm = new FormGroup({
			name: new FormControl('', [Validators.required, NoWhitespaceValidator]),
			description: new FormControl('', []),
			subject: new FormControl('', [Validators.required, NoWhitespaceValidator]),
			body: new FormControl('', [Validators.required, NoWhitespaceValidator]),
			userGroups: new FormControl([], [Validators.required]),
			triggerEvent: new FormControl('', [Validators.required]),
			delayTime : new FormControl('', [Validators.required]),
			reminderTime : new FormControl('', [Validators.required]),
			reminderCount : new FormControl('', [Validators.required, Validators.min(1)]),
			notificationType : new FormControl('', [Validators.required])
		});
	}
	
	userGroupAdded(event : any) {
		
		this.userGroupList.push(event.name);
		this.addNotificationForm.controls['userGroups'].setValue(this.userGroupList);
	}
	
	userGroupRemoved(event : any) {
		
		for(var i = 0 ; i< this.userGroupList.length; i++ ) {
			if(this.userGroupList[i] === event)
				this.userGroupList.splice(i,1);
		}
		this.addNotificationForm.controls['userGroups'].setValue(this.userGroupList);
	}
	
	countLength() {
		
		let description = this.addNotificationForm.controls['description'].value;
		this.descriptionLength = description.length;
	}
	
	submitNotificationForm(addNotificationForm : any) {
    	
		this.error = '';

		if(this.addNotificationForm.valid) {
			this.buttonText = CONSTANTS.SUBMITTING;
			this.notificationRequest = new NotificationRequest(addNotificationForm);
			
			let role = this.addNotificationForm.controls['userGroups'].value;
			this.notificationRequest['userGroups'] = role.toString();
			this.notificationRequest['triggerEvent'] = this.addNotificationForm.controls['triggerEvent'].value[0].name;
			this.notificationRequest['notificationType'] = this.addNotificationForm.controls['notificationType'].value[0].name;
			
	        this.manageNotificationService.addNotification(this.notificationRequest).subscribe((results) => {
	        		this.notificationAdded = true;
	        		this.error = "";
	        		setTimeout(() => {
	        			this.router.navigateByUrl('/' + ROUTS.MANAGENOTIFICATION);
	            	}, 2500);
	        		
	        }, (error) => {
	        		this.notificationAdded = false;
	        		this.error = JSON.parse(error._body).message;
	        		this.buttonText = CONSTANTS.SUBMIT;
	        });
		} else
			this.requiredFlag = true;
    }
	
// 	showTooltip(e){
// 		$(document).ready(function(){
// 			  $('[data-toggle="tooltip"]').tooltip();   
// 			});
// //		$("#"+e.target.id).tooltip('show');
// 	}

	showToolTipForBodyContext(){
		var options = {
		 content: 'Store No: {{store-no}} <br/> Store Name: {{store-name}} <br/>  Store Manager: {{sm-name-surname}} <br/>  Deadline: {{task-deadline}}  <br/> Task: {{task-name}} <br/> Stock Take Date: {{stock-take-date}}  <br/> RAM: {{ram-name-surname}} <br/> DSM: {{dsm-name-surname}} <br/> SM: {{sm-name-surname}} <br/> Remaining days: {{remaining-days}}',
		 html: true,
		 placement: 'right'
		};

		$('#bodyContInfo').popover(options);

		/***** Dismiss all popovers by clicking outside, don't dismiss if clicking inside the popover content  **************/

		$('html').on('click', function(e) {
		  if (typeof $(e.target).data('original-title') == 'undefined' &&
		     !$(e.target).parents().is('.popover.in')) {
		    $('[data-original-title]').popover('hide');
		  }
		});
	}

	showToolTipForDTime(){
		var options = {
		 content: 'How many days before or after the deadline date would you like to send a reminder / escalation? <br/><br/> A negative value would send the message before the deadline is reached, positive value would send it after the deadline date. <br/><br/> Example: If the deadline date is 13 April, and you set the Delay time to -2 a message would be sent 2 days  before the deadline, so on the 11th of April.',
		 html: true,
		 placement: 'right'
		};

		$('#dTimeInfo').popover(options);

		/***** Dismiss all popovers by clicking outside, don't dismiss if clicking inside the popover content  **************/

		$('html').on('click', function(e) {
		  if (typeof $(e.target).data('original-title') == 'undefined' &&
		     !$(e.target).parents().is('.popover.in')) {
		    $('[data-original-title]').popover('hide');
		  }
		});
	}

	showToolTipForRTime(){
		var options = {
		 content: 'Similar to Delay time, if you have a reminder count of 3, it means you would like to repeat this  message 3 times, and if the Reminder Time is set to 1, it will send 3 messages, 1 day apart. Starting 3 days before the deadline. <br/><br/> ie: Reminder Count: 3, <br/> Reminder Time: 2 <br/> Deadline: 13 April <br/><br/>This configuration would send 3 messages 2 days apart, starting 6 days before the Deadline. So 07 April the first message is sent, then 09 April and finally 11 April.',
		 html: true,
		 placement: 'right'
		};

		$('#remTimeInfo').popover(options);

		/***** Dismiss all popovers by clicking outside, don't dismiss if clicking inside the popover content  **************/

		$('html').on('click', function(e) {
		  if (typeof $(e.target).data('original-title') == 'undefined' &&
		     !$(e.target).parents().is('.popover.in')) {
		    $('[data-original-title]').popover('hide');
		  }
		});
	}

	showToolTipForRCount(){
		var options = {
		 content: 'How many times would you like to repeat this message.',
		 html: true,
		 placement: 'right'
		};

		$('#remCountInfo').popover(options);

		/***** Dismiss all popovers by clicking outside, don't dismiss if clicking inside the popover content  **************/

		$('html').on('click', function(e) {
		  if (typeof $(e.target).data('original-title') == 'undefined' &&
		     !$(e.target).parents().is('.popover.in')) {
		    $('[data-original-title]').popover('hide');
		  }
		});
	}
	
	back() {
		this.router.navigateByUrl('/' + ROUTS.MANAGENOTIFICATION);
	}
}