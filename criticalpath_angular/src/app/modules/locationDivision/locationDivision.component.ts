import{Component,OnInit}from'@angular/core';import{RouterModule,Routes,Router}from'@angular/router';

import*as ROUTS from'../../constants/routs';import{CONSTANTS}from'../../constants/constant';

import{LocationDivisionService}from'../../service/locationDivision.service';import{Input,Output,EventEmitter}from'@angular/core';declare var $:any;
import { LocalStorageService } from '../../service/localStorage.service';

@Component
({selector:'',templateUrl:'./locationDivision.component.html'
})export class LocationDivisionComponent implements OnInit {

	constructor(public router: Router, public locationDivisionService : LocationDivisionService, public localStorageService : LocalStorageService){}
	
	public locationDivisionList : any;
	formData: any;
	typeError : boolean = false;
	invalid : boolean = false;
	public locationDivisionId : any;
	deleted : boolean = false;
	restored : boolean = false;
	error : string;
	public showDeleteRecord : boolean = false;
	public showLoader : boolean = false;
	
	@Input() id: string;
	@Input() maxSize: number;
	@Output() pageChange: EventEmitter<number> = new EventEmitter() ;
	
	currentPage: number = 0;
	public pageLimitOptions = CONSTANTS.PAGE_NUMBER_LIMIT_OPTIONS;
	public originalTotalElement = 0;
	
	public isSearchingLocDiv : boolean = true;
	public locDivSearchResultsMessage : any;
	public locDivSearchResults : any[] = [];
    public isFirstTimeSearch: boolean = true;
	public curUserRole : any;
	
	public paginationConfig = { 
			totalPages : 1,
			totalElements : 0,
			maxSize : 3,
			rotate : false, 
			align : true,
			firstText : "&laquo;",
			previousText : "Previous",
			nextText : "Next",
			lastText : "&raquo;",
			itemsPerPage : 0
		};
	
	public locDivSearchParam = {
			pageNo : 1,
			searchString : "",
			startDate : undefined,
			endDate : undefined,
	};

	ngOnInit() {
		
		this.formData = new FormData();
		
//		this.loadAllActivedata();
		
		this.getLocDivByPageNumber(false, this.currentPage);
		
		this.curUserRole = this.localStorageService.getItem(CONSTANTS.USER_ROLE);
		
		setTimeout(() => {
			this.initializeDatatable();
        }, 500);
	}

	initializeDatatable() {
		$(function () {
		    $('#locationDivisionTable').DataTable({
		    	'columnDefs': [{
		            'orderable': false,
		            'targets': [6]
		        }],
		      'paging'      : false,
		      'lengthChange': true,
		      'searching'   : false,
		      'info'        : false,
		      'autoWidth'   : false,
		      'destroy'     : true
		    })
		  });

	this.showLoader=false;

	}

	loadAllActivedata() {
		
		this.showLoader = true;
		
		var table = $('#locationDivisionTable').DataTable();
		this.locationDivisionService.getAllActiveLocationDivision().subscribe((results)  => {
			table.destroy();
			this.initializeDatatable();
			this.locationDivisionList = results.data;
			
		},(error) => {
			
		});
	}
	
	ShowDeleteRecord() {
		
		this.showLoader = true;
		
		this.showDeleteRecord = !this.showDeleteRecord;

		this.locDivSearchParam.searchString = "";
		
		if(this.showDeleteRecord){
			this.getAllDeletedLocDiv(false,this.currentPage);
		} else{
			
			this.getLocDivByPageNumber(false, this.currentPage);
		}
	}

	pageChanged(event: any): void {
		this.locDivSearchParam.pageNo = event.page;
		
		if(this.showDeleteRecord == false){
			this.getLocDivByPageNumber(true, this.locDivSearchParam.pageNo - 1);
		}else{
			this.getAllDeletedLocDiv(true,this.locDivSearchParam.pageNo - 1);
		}
	}
	
	getLocDivByPageNumber(isPageChanged, pageNumber) {
		if( !isPageChanged ) 
			this.locDivSearchParam.pageNo = 1;
		if(this.locDivSearchParam.searchString == '') {
			this.locationDivisionService.getAllActiveLocDivByPageNumber(pageNumber).subscribe((results)  => {
			 try { 
				var table = $('#locationDivisionTable').DataTable();
				table.destroy();
				this.initializeDatatable();
			} catch (error) {
				console.error('Log error', error);
			}
			 
			this.locationDivisionList = results.data.content;
			this.paginationConfig.maxSize =  results.data.totalPages ;
			this.paginationConfig.totalElements = results.data.totalElements;
			this.paginationConfig.totalPages = results.data.totalPages;
			this.paginationConfig.itemsPerPage = results.data.numberOfElements;
			 this.showLoader=false;
		},(error) => {
			
		});
	  } else {
		  this.searchLocDivByName(false);
	  }
	}
	
	getAllDeletedLocDiv(isPageChanged ,pageNumber){
		if( !isPageChanged ) 
			this.locDivSearchParam.pageNo = 1;
		if(this.locDivSearchParam.searchString == '') {	
		this.locationDivisionService.getAllDeletedLocDivByPageNumber(pageNumber).subscribe((results)  => {
			 try { 
				var table = $('#locationDivisionTable').DataTable();
				table.destroy();
				this.initializeDatatable();
				}
				catch (error) {
				console.error('Log error', error);
				}
			
			this.locationDivisionList = results.data.content;
			this.locDivSearchResults = results.data.content;
            this.originalTotalElement = results.data.totalElements;
            this.paginationConfig.totalElements = results.data.totalElements;
            this.paginationConfig.totalPages = results.data.totalPages;
            this.paginationConfig.maxSize = results.data.totalPages;
            this.paginationConfig.itemsPerPage = results.data.numberOfElements;
            this.showLoader=false;
		},(error) => {
			
		});
	 } else {
		  this.searchLocDivByName(false);
	  }
	}

	addNewLocationDivision() {
		
		this.router.navigateByUrl('/' + ROUTS.ADD_LOCATIONDIVISION);
	}
	
	storeId(locationDivisionId) {
		this.locationDivisionId = locationDivisionId;
	}
	
	deleteLocationDivision() {
		this.error = '';
		this.locationDivisionService.deleteLocationDivision(this.locationDivisionId).subscribe((results) => {
			this.deleted = true;
			setTimeout(() => {
				window.location.reload();	
			},800);
		}, (error) => {
			this.error = JSON.parse(error._body).message;
		});
	}
	
	restoreLocationDivision(locationDivisionId) {
		this.error = '';
		this.locationDivisionService.restoreLocationDivision(locationDivisionId).subscribe((results) => {
			this.restored = true;
			setTimeout(() => {
				window.location.reload();	
			},800);
		}, (error) => {
			
		});
	}

	redirectToListPage() {
//		this.uploadLogs = [];
		this.initializeDatatable();
	}

	searchLocDivByName(isPageChanged) {
		
		this.isSearchingLocDiv = true;
		this.locDivSearchResults = [];	
		
		if(this.showDeleteRecord == true){
	
			if(this.locDivSearchParam.searchString.trim() == '') {
				this.isSearchingLocDiv = false;
				this.getAllDeletedLocDiv(isPageChanged, this.currentPage);
				return;
			}
			
		if(/^[a-zA-Z0-9 -]*$/.test(this.locDivSearchParam.searchString.trim()) == true){			
			this.locationDivisionService.searchDeletedLocDivOnPage(this.locDivSearchParam.searchString.trim(), this.locDivSearchParam.pageNo - 1).subscribe((results)  => {
				this.isSearchingLocDiv = false;
				if( results && results.data && results.data.content && results.data.content.length > 0 ) {
					 try { 
							var table = $('#locationDivisionTable').DataTable();
							table.destroy();
							this.initializeDatatable();
						} catch (error) {
							console.error('Log error', error);
						}
					this.isSearchingLocDiv = true;
					this.locationDivisionList = results.data.content;
					this.locDivSearchResults = results.data.content;
	                this.originalTotalElement = results.data.totalElements;
	                this.paginationConfig.totalElements = results.data.totalElements;
	                this.paginationConfig.totalPages = results.data.totalPages;
	                this.paginationConfig.maxSize = results.data.totalPages;
	            }else {
	                this.locDivSearchResultsMessage = results.message;
	                this.paginationConfig.totalElements = 0;
	                this.paginationConfig.totalPages = 0;
	                this.locationDivisionList = [];
	            }
				 this.showLoader=false;
			},(error) => {

				 this.paginationConfig.totalElements = 0;
		         this.paginationConfig.totalPages = 0;
			});
		} else {
			 try { 
					var table = $('#locationDivisionTable').DataTable();
					table.destroy();
					this.initializeDatatable();
				} catch (error) {
					console.error('Log error', error);
				}
			this.locationDivisionList =[];
		}	
		}
		else{
			if(this.locDivSearchParam.searchString.trim() == '') {
				this.isSearchingLocDiv = true;
				this.getLocDivByPageNumber(isPageChanged, this.locDivSearchParam.pageNo - 1);
				return;
			}
			
			if(/^[a-zA-Z0-9 -]*$/.test(this.locDivSearchParam.searchString.trim()) == true){
			this.locationDivisionService.searchActiveLocDivOnPage(this.locDivSearchParam.searchString.trim(), this.locDivSearchParam.pageNo - 1).subscribe((results)  => {
				 try { 
						var table = $('#locationDivisionTable').DataTable();
						table.destroy();
						this.initializeDatatable();
					} catch (error) {
						console.error('Log error', error);
					}
				this.initializeDatatable();
				if( results && results.data && results.data.content && results.data.content.length > 0 ) {
					this.isSearchingLocDiv = true;				
					this.locationDivisionList = results.data.content;
					this.locDivSearchResults = results.data.content;
	                this.originalTotalElement = results.data.totalElements;
	                this.paginationConfig.totalElements = results.data.totalElements;
	                this.paginationConfig.totalPages = results.data.totalPages;
	                this.paginationConfig.maxSize = results.data.totalPages;
	            } else {
	                this.locDivSearchResultsMessage = results.message;
	                this.paginationConfig.totalElements = 0;
	                this.paginationConfig.totalPages = 0;
	                this.locationDivisionList = [];
	            }
				 this.showLoader=false;
			},(error) => {
			
				 this.paginationConfig.totalElements = 0;
		         this.paginationConfig.totalPages = 0;
			});
		
			} else {
				 try { 
						var table = $('#locationDivisionTable').DataTable();
						table.destroy();
						this.initializeDatatable();
				   } catch (error) {
						console.error('Log error', error);
				   }
				this.locationDivisionList =[];
			}
		}
	}
}