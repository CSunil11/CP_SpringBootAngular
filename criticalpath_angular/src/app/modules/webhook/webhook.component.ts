import {Component, OnInit } from '@angular/core';
import { RouterModule, Routes, Router } from '@angular/router';

import * as ROUTS from '../../constants/routs';
import { CONSTANTS } from '../../constants/constant';

import { WebhookService } from '../../service/webhook.service';
import { LocalStorageService } from '../../service/localStorage.service';

declare var $: any;

@Component({
	selector :'',
	templateUrl : './webhook.component.html'
})
export class WebhookComponent implements OnInit {
	
	constructor(public router: Router, public webhookService : WebhookService, public localStorageService : LocalStorageService){}
	
	public webhookList : any;
	deleted : boolean = false;
	restored : boolean = false;
	webhookId : any;
	public showDeleteRecord : boolean = false;
	public showLoader : boolean = false;
    public curUserRole : any;
	
	ngOnInit() {
		
		this.loadAllActivedata();
		
		this.curUserRole = this.localStorageService.getItem(CONSTANTS.USER_ROLE);
		
		setTimeout(() => {
			this.initializeDatatable();
        }, 500);
	}
	
	initializeDatatable() {
		 $(function () {
		    $('#webhookTable').DataTable({
		    	"columnDefs": [{
		            "orderable": false,
		            "targets": [2]
		        }],
		    	language: {
			    	'searchPlaceholder'	:'Search'
			    },
		      'paging'      : true,
		      'pageLength'  : 25,
		      'lengthChange': false,
		      'searching'   : true,
		      'info'        : false,
		      'autoWidth'   : false,
		      'destroy' 	: true
		    })
		  });
		 
		 this.showLoader = false;
	}
	
	loadAllActivedata() {
		
		this.showLoader = true;
		
		var table = $('#webhookTable').DataTable();
		
		this.webhookService.getAllActiveWebhook().subscribe((results)  => {
			table.destroy();
			this.initializeDatatable();
			this.webhookList = results.data;
			
		},(error) => {
			
		});
	}
	
	showDeletedRecord() {
		
		this.showLoader = true;
		
		var table = $('#webhookTable').DataTable();
		
		this.showDeleteRecord = !this.showDeleteRecord;
		
		if(this.showDeleteRecord){
			this.webhookService.getAllDeletedWebhook().subscribe((results)  => {
				table.destroy();
				this.initializeDatatable();

				this.webhookList = results.data;
			},(error) => {
				
			});
		}else{
			this.loadAllActivedata();
		}
	}

	addNewWebhook() {
		
		this.router.navigateByUrl('/' + ROUTS.ADD_WEBHOOK);
	}
	
	
	deleteWebhook() {
		this.webhookService.deleteWebhook(this.webhookId).subscribe((results) => {
			this.deleted = true;
			setTimeout(() => {
				window.location.reload();	
			},800);
			
		}, (error) => {
			
		});
	}
	
	getId(id) {
		this.webhookId = id;
	}
	
	restoreWebhook(webhookId) {
		this.webhookService.restoreWebhook(webhookId).subscribe((results) => {
			this.restored = true;
			setTimeout(() => {
				window.location.reload();	
			},800);
		}, (error) => {
			
		});
	}
}