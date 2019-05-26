import {Component, OnInit } from '@angular/core';
import { RouterModule, Routes, Router } from '@angular/router';
import{ BlockUI,NgBlockUI,BlockUIModule }from'ng-block-ui';
import * as ROUTS from '../../constants/routs';
import { CONSTANTS } from '../../constants/constant';
import { EventService } from '../../service/event.service';
import { LocalStorageService } from '../../service/localStorage.service';

declare var $: any;

@Component({
	selector :'',
	templateUrl : './event.component.html'
})
export class EventComponent implements OnInit {
	
	constructor(public router: Router, public eventService : EventService, public localStorageService : LocalStorageService){}
	
	public eventList : any;
	deleted : boolean = false;
	restored : boolean = false;
	public eventId : any;
	public uploadLogs : any = [];
	public uploadedSuccess : boolean ;
	formData: any;
	typeError : boolean = false;
	invalid : boolean = false;
	public showDeleteRecord : boolean = false;
	public showLoader : boolean = false;
	public curUserRole : any;
	
	//Wires up BlockUI instance
	@BlockUI() blockUI: NgBlockUI;
	
	ngOnInit() {
		
		this.formData = new FormData();
		
		this.curUserRole = this.localStorageService.getItem(CONSTANTS.USER_ROLE);
		
		this.loadAllActivedata();
		
		setTimeout(() => {
			this.initializeDatatable();
        }, 500);
	}
	
	initializeDatatable() {
		$(function () {
		    $('#eventTable').DataTable({
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
			      'destroy'     : true
		    })
		  });
		
		this.showLoader = false;
	}
	
	loadAllActivedata() {
		
		this.showLoader = true;
		
		var table = $('#eventTable').DataTable();
		this.eventService.getAllActiveEvent().subscribe((results)  => {
			table.destroy();
			this.initializeDatatable();
			this.eventList = results.data;
			
		},(error) => {
			
		});
	}
	
	ShowDeleteRecord() {
		
		this.showLoader = true;
		
		var table = $('#eventTable').DataTable();
		this.showDeleteRecord = !this.showDeleteRecord;
		
		if(this.showDeleteRecord){
			this.eventService.getAllDeleteEvent().subscribe((results)  => {
				
				table.destroy();
				this.initializeDatatable();
				this.eventList = results.data;
				
			},(error) => {
				
			});
		}else{
			this.loadAllActivedata();
		}
	}
	
	addNewEvent() {
		
		this.router.navigateByUrl('/' + ROUTS.ADD_EVENT);
	}
	
	deleteEvent() {
		this.eventService.deleteEvent(this.eventId).subscribe((results) => {
			this.deleted = true;
			setTimeout(() => {
				window.location.reload();	
			},800);
		}, (error) => {
			
		});
	}
	
	getId(id) {
		this.eventId = id;
	}
	
	restoreEvent(eventId) {
		this.eventService.restoreEvent(eventId).subscribe((results) => {
			this.restored = true;
			setTimeout(() => {
				window.location.reload();	
			},800);
		}, (error) => {
			
		});
	}
	
	upload(event : any) {
		
		var table = $('#eventTable').DataTable();
		
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
            			this.eventService.upload(this.formData).subscribe((results)  => {
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
            				
            				this.eventService.getAllEvent().subscribe((results)  => {
            					this.eventList = results.data;
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
	
	redirectToListPage() {
		this.uploadLogs = [];
		this.initializeDatatable();
	}
}