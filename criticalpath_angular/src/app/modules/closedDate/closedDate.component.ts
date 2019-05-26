import {Component, OnInit } from '@angular/core';
import { RouterModule, Routes, Router } from '@angular/router';

import * as ROUTS from '../../constants/routs';
import { CONSTANTS } from '../../constants/constant';

import { DatePipe } from '@angular/common';
import { ClosedDateService } from '../../service/closedDate.service';
import { LocalStorageService } from '../../service/localStorage.service';

declare var $: any;

@Component({
	selector :'',
	templateUrl : './closedDate.component.html'
})
export class ClosedDateComponent implements OnInit {
	
	constructor(public router: Router, public closedDateService : ClosedDateService,  public datePipe : DatePipe, public localStorageService : LocalStorageService){}
	
	public closedDateList : any;
	deleted : boolean = false;
	restored : boolean = false;
	closeDateId : any;
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
		    $('#closedDateTable').DataTable({
		    	"columnDefs": [{
		            "orderable": false,
		            "targets": 3
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
		
		this.closedDateService.getAllActiveClosedDate().subscribe((results)  => {
			var table = $('#closedDateTable').DataTable();
			table.destroy();
			this.initializeDatatable();
			this.closedDateList = results.data;
			
		},(error) => {
			
		});
	}
	
	showDeletedRecord() {
		
		this.showLoader = true;
		
		this.showDeleteRecord = !this.showDeleteRecord;
		
		if(this.showDeleteRecord){
			this.closedDateService.getAllDeletedClosedDate().subscribe((results)  => {
				var table = $('#closedDateTable').DataTable();
				table.destroy();
				this.initializeDatatable();

				this.closedDateList = results.data;
			},(error) => {
				
			});
		}else{
			this.loadAllActivedata();
		}
	}
	
	addNewClosedDate() {
		
		this.router.navigateByUrl('/' + ROUTS.ADD_CLOSED_DATE);
	}
	
	storeId(closeDateId) {
		this.closeDateId = closeDateId;
	}
	
	deleteClosedDate() {
		this.closedDateService.deleteClosedDate(this.closeDateId).subscribe((results) => {
			this.deleted = true;
			setTimeout(() => {
				window.location.reload();	
			},800);
		}, (error) => {
			
		});
	}
	
	restoreClosedDate(closeDateId) {
		this.closedDateService.restoreClosedDate(closeDateId).subscribe((results) => {
			this.restored = true;
			setTimeout(() => {
				window.location.reload();	
			},800);
		}, (error) => {
			
		});
	}
}