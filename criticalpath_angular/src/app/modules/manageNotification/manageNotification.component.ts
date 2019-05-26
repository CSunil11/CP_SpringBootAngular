import{Component,OnInit}from'@angular/core';import{RouterModule,Routes,Router}from'@angular/router';

import*as ROUTS from'../../constants/routs';import{CONSTANTS}from'../../constants/constant';

import{ManageNotificationService}from'../../service/manageNotification.service'
import { LocalStorageService } from '../../service/localStorage.service';
declare var $:any;

@Component
({selector:'',templateUrl:'./manageNotification.component.html'
})export class ManageNotificationComponent implements OnInit {

	constructor(public router: Router, public manageNotificationService : ManageNotificationService, public localStorageService: LocalStorageService){
		
	}
	
	public notificationList : any;
	public uploadLogs : any = [];
	public uploadedSuccess : boolean ;
	formData: any;
	typeError : boolean = false;
	invalid : boolean = false;
	public notificationId : any;
	deleted : boolean = false;
	restored : boolean = false;
	error : string;
	public showDeleteRecord : boolean = false;
	public showLoader : boolean = false;
	public curUserRole : any;
	
	ngOnInit() {

		this.curUserRole = this.localStorageService.getItem(CONSTANTS.USER_ROLE);
		
		this.formData = new FormData();
		
		this.loadAllActivedata();
		
		setTimeout(() => {
			this.initializeDatatable();
        }, 500);
	
	}
	
	initializeDatatable() {
		$(function () {
		    $('#notificationTable').DataTable({
		    	"columnDefs": [{
		            "orderable": false,
		            "targets": 8
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
		var table = $('#notificationTable').DataTable();
		this.manageNotificationService.getAllActiveNotification().subscribe((results)  => {
			table.destroy();
			this.initializeDatatable();
			this.notificationList = results.data;
			
		},(error) => {
			
		});
	}
	
	ShowDeleteRecord() {
		
		this.showLoader = true;
		
		var table = $('#notificationTable').DataTable();
		this.showDeleteRecord = !this.showDeleteRecord;
		
		if(this.showDeleteRecord == true){
			this.manageNotificationService.getAllDeleteNotification().subscribe((results)  => {			
				table.destroy();
				this.initializeDatatable();
				
				this.notificationList = results.data;
			},(error) => {
				
			});
		}else{
			this.loadAllActivedata();
		}
	}
	
	addNewNotification() {
		
		this.router.navigateByUrl('/' + ROUTS.ADD_MANAGENOTIFICATION);
	}
	
	storeId(notificationId) {
		this.notificationId = notificationId;
	}
	
	deleteNotification() {
		this.error = '';
		this.manageNotificationService.deleteNotification(this.notificationId).subscribe((results) => {
			this.deleted = true;
			setTimeout(() => {
				window.location.reload();	
			},800);
		}, (error) => {
			this.error = JSON.parse(error._body).message;
		});
	}
	
	restoreNotification(notificationId) {
		this.error = '';
		this.manageNotificationService.restoreNotification(notificationId).subscribe((results) => {
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