import {Component, OnInit } from '@angular/core';
import { RouterModule, Routes, Router } from '@angular/router';

import * as ROUTS from '../../constants/routs';
import { CONSTANTS } from '../../constants/constant';

import { TaskStatusService } from '../../service/taskStatus.service'
import { LocalStorageService } from '../../service/localStorage.service';

import{ BlockUI,NgBlockUI,BlockUIModule }from'ng-block-ui';

declare var $: any;

@Component({
	selector :'',
	templateUrl : './taskStatus.component.html',
})
export class TaskStatusComponent implements OnInit {
	
	constructor(public router: Router, public taskStatusService : TaskStatusService, public localStorageService : LocalStorageService){}
	
	public taskStatusList : any;
	public uploadLogs : any = [];
	public uploadedSuccess : boolean ;
	formData: any;
	typeError : boolean = false;
	invalid : boolean = false;
	deleted : boolean = false;
	restored : boolean = false;
	public taskStatusId : any;
	error : string;
	public showDeleteRecord : boolean = false;
	public showLoader : boolean = false;
	public curUserRole : any;
	
	//Wires up BlockUI instance
	@BlockUI() blockUI: NgBlockUI;
	
	ngOnInit() {
		
		this.formData = new FormData();
		
		this.loadAllActivedata();
		
		this.curUserRole = this.localStorageService.getItem(CONSTANTS.USER_ROLE);
		
		setTimeout(() => {
			this.initializeDatatable();
        }, 500);
	}
	
	initializeDatatable() {
		$(function () {
		    $('#taskStatusTable').DataTable({
		    	"columnDefs": [{
		            "orderable": false,
		            "targets": 1
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
	
	loadAllActivedata(){
		
		this.showLoader = true;
		
		var table = $('#taskStatusTable').DataTable();
		this.taskStatusService.getAllActiveTaskStatus().subscribe((results)  => {
			table.destroy();
			this.initializeDatatable();
			this.taskStatusList = results.data;
			
		},(error) => {
			
		});
	}
	
	ShowDeleteRecord() {
		
		this.showLoader = true;
		
		var table = $('#taskStatusTable').DataTable();
		this.showDeleteRecord = !this.showDeleteRecord;
		
		if(this.showDeleteRecord == true){
			this.taskStatusService.getAllDeleteTaskStatus().subscribe((results)  => {
				table.destroy();
				this.initializeDatatable();
				this.taskStatusList = results.data;
				
			},(error) => {
				
			});
		}else{
			this.loadAllActivedata();
		}
	}
	
	addNewTaskStatus() {
		
		this.router.navigateByUrl('/' + ROUTS.ADD_TASK_STATUS);
	}
	
	upload(event : any) {
		
		var table = $('#taskStatusTable').DataTable();
		
		this.invalid = false;
		this.typeError = false;
        let fileList: FileList = event.target.files;

        if (fileList.length > 0) {

            let file: File = fileList[0];

            if (file.type == "text/csv") {

//                if (file.size < CONSTANTS.MAX_FILE_UPLOAD_SIZE_LIMIT) {

                    this.formData.append('data', event.target.files[0], file.name);
                    this.blockUI.start(CONSTANTS.blockUIMessage);
            		this.uploadLogs = [];
            		setTimeout(() => {
            			this.taskStatusService.upload(this.formData).subscribe((results)  => {
            				table.destroy();
            				this.blockUI.stop();
            				this.formData = new FormData();
            				
            				if(results.data.length > 0) {
            					if(results.data.includes(CONSTANTS.invalidCsvMessage)) { 
            						this.invalid = true;
            						setTimeout(() => {
    	            					this.invalid = false;
    	            				},4000);
            					}else
            						this.uploadLogs = results.data;
            				}
            				else  {
            					this.uploadedSuccess = true;
            					setTimeout(() => {
	            					this.uploadedSuccess = false;
	            				},2000);
            				}
            				
            				this.taskStatusService.getAllActiveTaskStatus().subscribe((results)  => {
            					this.taskStatusList = results.data;
	            			});
            				
            				setTimeout(() => {
            					this.initializeDatatable();
            				},3000);
            				
            			},(error) => {
            				console.log('Error while uploading');
            			});
                    }, 1000);
//                }
            } else {
            	this.typeError = true;
            	setTimeout(() => {
					this.typeError = false;
				},4000);
            	return;
            }
            	
        }
	}
	
	storeId(taskStatusId) {
		this.taskStatusId = taskStatusId;
	}
	
	deleteTaskStatus() {
		this.error = '';
		this.taskStatusService.deleteTaskStatus(this.taskStatusId).subscribe((results) => {
			this.deleted = true;
			setTimeout(() => {
				window.location.reload();	
			},800);
		}, (error) => {
			this.error = JSON.parse(error._body).message;
		});
	}
	
	restoreTaskStatus(taskStatusId) {
		this.error = '';
		this.taskStatusService.restoreTaskStatus(taskStatusId).subscribe((results) => {
			this.restored = true;
			setTimeout(() => {
				window.location.reload();	
			},800);
		}, (error) => {
			
		});
	}

	redirectToListPage() {
		this.uploadLogs = [];
		this.initializeDatatable();
	}
}