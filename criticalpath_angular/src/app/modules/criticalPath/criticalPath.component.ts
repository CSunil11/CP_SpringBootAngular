import {Component, OnInit } from '@angular/core';
import { RouterModule, Routes, Router } from '@angular/router';

import * as ROUTS from '../../constants/routs';
import {CONSTANTS} from '../../constants/constant';

import { CriticalPathService } from '../../service/criticalPath.service';
import { LocalStorageService } from '../../service/localStorage.service';

declare var $: any;

@Component({
	selector :'',
	templateUrl : './criticalPath.component.html'
})
export class CriticalPathComponent implements OnInit {
	
	constructor(public router: Router, public criticalPathService : CriticalPathService, public localStorageService : LocalStorageService){}
	
	public criticalPathList : any;
	deleted : boolean = false;
	restored : boolean = false;
	public criticalPathId : any;
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
		    $('#criticalPathTable').DataTable({
		    	"columnDefs": [{
		            "orderable": false,
		            "targets": [5]
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
	 	
		this.criticalPathService.getAllActiveCriticalPath().subscribe((results)  => {
			var table = $('#criticalPathTable').DataTable();
			table.destroy();
			this.initializeDatatable();
			
			this.criticalPathList = results.data;
			
		},(error) => {
			
		});
	}
	
	showDeletedRecord() {
		
		this.showLoader = true;
		
		this.showDeleteRecord = !this.showDeleteRecord;
		
		if(this.showDeleteRecord){
			this.criticalPathService.getAllDeletedCriticalPath().subscribe((results)  => {
				var table = $('#criticalPathTable').DataTable();
				table.destroy();
				this.initializeDatatable();

				this.criticalPathList = results.data;
			},(error) => {
				
			});
		}else{
			this.loadAllActivedata();
		}
	}
	
	addNewCriticalPath() {
		
		this.router.navigateByUrl('/' + ROUTS.ADD_CRITICAL_PATH);
	}
	
	deleteCriticalPath() {
		this.criticalPathService.deleteCriticalPath(this.criticalPathId).subscribe((results) => {
			this.deleted = true;
			setTimeout(() => {
				window.location.reload();	
			},800);
		}, (error) => {
			
		});
	}
	
	getId(id) {
		this.criticalPathId = id;
	}
	
	restoreCriticalPath(criticalPathId) {
		this.criticalPathService.restoreCriticalPath(criticalPathId).subscribe((results) => {
			this.restored = true;
			setTimeout(() => {
				window.location.reload();	
			},800);
		}, (error) => {
			
		});
	}
}