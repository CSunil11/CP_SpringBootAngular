import {Component, OnInit } from '@angular/core';
import { RouterModule, Routes, Router } from '@angular/router';

import { DaysOfWeekService } from '../../service/daysOfWeek.service';

import * as ROUTS from '../../constants/routs';
import { CONSTANTS } from '../../constants/constant';
import { LocalStorageService } from '../../service/localStorage.service';

declare var $: any;

@Component({
	selector :'',
	templateUrl : './daysOfWeek.component.html'
})
export class DaysOfWeekComponent implements OnInit {
	
	constructor(public router: Router, public daysOfWeekService : DaysOfWeekService, public localStorageService: LocalStorageService){}
	
	public daysOfWeekList : any;
	deleted : boolean = false;
	restored : boolean = false;
	public daysOfWeekId : any;
	public showDeleteRecord : boolean = false;
	public showLoader : boolean = false;
	public curUserRole : any;
		
	ngOnInit() {

		this.curUserRole = this.localStorageService.getItem(CONSTANTS.USER_ROLE);
		
		this.loadAllActivedata();
		
		setTimeout(() => {
			this.initializeDatatable();
        }, 500);
		
	}
	
	
	initializeDatatable() {
		$(function () {
		    $('#daysOfWeek').DataTable({
		    	"columnDefs": [{
		            "orderable": false,
		            "targets": [1]
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
		      'destroy'     : true
		    })
		  });
		
		this.showLoader = false;
	}

	loadAllActivedata() {
		
		this.showLoader = true;
		
		var table = $('#daysOfWeek').DataTable();
		
		this.daysOfWeekService.getAllActiveDaysOfWeek().subscribe((results)  => {
			table.destroy();
			this.initializeDatatable();
			this.daysOfWeekList = results.data;
			
		},(error) => {
			
		});
	}
	
	ShowDeleteRecord() {
		
		this.showLoader = true;
		
		var table = $('#daysOfWeek').DataTable();
		
		this.showDeleteRecord = !this.showDeleteRecord;
		
		if(this.showDeleteRecord){
			this.daysOfWeekService.getAllDeleteDaysOfWeek().subscribe((results)  => {
				table.destroy();
				this.initializeDatatable();

				this.daysOfWeekList = results.data;
			},(error) => {
				
			});
		}else{
			this.loadAllActivedata();
		}
	}
	
	deleteDaysOfWeeek() {
		this.daysOfWeekService.deleteDaysOfWeek(this.daysOfWeekId).subscribe((results) => {
			this.deleted = true;
			setTimeout(() => {
				window.location.reload();	
			},800);
		}, (error) => {
			
		});
	}
	
	getId(id) {
		this.daysOfWeekId = id;
	}
	
	restoredaysOfWeek(daysOfWeekId) {
		this.daysOfWeekService.restoreDaysOfWeek(daysOfWeekId).subscribe((results) => {
			this.restored = true;
			setTimeout(() => {
				window.location.reload();	
			},800);
		}, (error) => {
			
		});
	}

}
