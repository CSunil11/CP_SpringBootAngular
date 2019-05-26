import{Component,OnInit}from'@angular/core';import{RouterModule,Routes,Router}from'@angular/router';

import*as ROUTS from'../../constants/routs';import{CONSTANTS}from'../../constants/constant';

import{ProvinceService}from'../../service/province.service';
import{Input,Output,EventEmitter}from'@angular/core';
import { LocalStorageService } from '../../service/localStorage.service';
declare var $:any;

@Component
({selector:'',templateUrl:'./province.component.html'
})export class ProvinceComponent implements OnInit {

	constructor(public router: Router, public provinceService : ProvinceService, public localStorageService : LocalStorageService){}
	
	public provinceList : any;
	public uploadLogs : any = [];
	public uploadedSuccess : boolean ;
	formData: any;
	typeError : boolean = false;
	invalid : boolean = false;
	public provinceId : any;
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
	
	public isSearchingProvince : boolean = true;
	public provinceSearchResultsMessage : any;
	public provinceSearchResults : any[] = [];
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
	
	public provinceSearchParam = {
			pageNo : 1,
			searchString : "",
			startDate : undefined,
			endDate : undefined,
	};
	
	ngOnInit() {
		
		this.formData = new FormData();
		
//		this.loadAllActivedata();
		this.getProvinceByPageNumber(false, this.currentPage);
		
		this.curUserRole = this.localStorageService.getItem(CONSTANTS.USER_ROLE);
		
		setTimeout(() => {
			this.initializeDatatable();
        }, 500);
	}

	initializeDatatable() {
		$(function () {
		    $('#provinceTable').DataTable({
		    	"columnDefs": [{
		            "orderable": false,
		            "targets": [6]
		        }],
		      'paging'      : false,
		      'lengthChange': true,
		      'searching'   : false,
		      'info'        : false,
		      'autoWidth'   : false,
		      'destroy'     : true
		    })
		  });
		
		this.showLoader = false;
	}
	
	loadAllActivedata() {
		
		this.showLoader = true;
		var table = $('#provinceTable').DataTable();
		this.provinceService.getAllActiveProvince().subscribe((results)  => {
			table.destroy();
			this.initializeDatatable();
			this.provinceList = results.data;
			
		},(error) => {
			
		});
	}
	
	ShowDeleteRecord() {
		
		this.showLoader = true;
		
		this.showDeleteRecord = !this.showDeleteRecord;

		this.provinceSearchParam.searchString = "";
		
		if(this.showDeleteRecord){
			this.getAllDeletedProvince(false,this.currentPage);
		} else{
			
			this.getProvinceByPageNumber(false, this.currentPage);
		}
	}
	
	pageChanged(event: any): void {
		this.provinceSearchParam.pageNo = event.page;
		
		if(this.showDeleteRecord == false){
			this.getProvinceByPageNumber(true, this.provinceSearchParam.pageNo - 1);
		}else{
			this.getAllDeletedProvince(true,this.provinceSearchParam.pageNo - 1);
		}
	}
	
	getProvinceByPageNumber(isPageChanged, pageNumber) {
		if( !isPageChanged ) 
			this.provinceSearchParam.pageNo = 1;
		 if(this.provinceSearchParam.searchString == '') {
			this.provinceService.getAllActiveProvinceByPageNumber(pageNumber).subscribe((results)  => {
			 try { 
				var table = $('#provinceTable').DataTable();
				table.destroy();
				this.initializeDatatable();
			} catch (error) {
				console.error('Log error', error);
			}
			 
			this.provinceList = results.data.content;
			this.paginationConfig.maxSize =  results.data.totalPages ;
			this.paginationConfig.totalElements = results.data.totalElements;
			this.paginationConfig.totalPages = results.data.totalPages;
			this.paginationConfig.itemsPerPage = results.data.numberOfElements;
			this.showLoader=false;
		},(error) => {
			
		});
	  } else {
		this.searchProvinceByName(false);
	  }
	}
	
	getAllDeletedProvince(isPageChanged ,pageNumber){
		if( !isPageChanged ) 
			this.provinceSearchParam.pageNo = 1;
	 if(this.provinceSearchParam.searchString == '') {
		this.provinceService.getAllDeletedProvinceByPageNumber(pageNumber).subscribe((results)  => {
			 try { 
				var table = $('#provinceTable').DataTable();
				table.destroy();
				this.initializeDatatable();
				}
				catch (error) {
				console.error('Log error', error);
				}
			
			this.provinceList = results.data.content;
			this.provinceSearchResults = results.data.content;
            this.originalTotalElement = results.data.totalElements;
            this.paginationConfig.totalElements = results.data.totalElements;
            this.paginationConfig.totalPages = results.data.totalPages;
            this.paginationConfig.maxSize = results.data.totalPages;
            this.paginationConfig.itemsPerPage = results.data.numberOfElements;
            this.showLoader=false;
		},(error) => {
			
		});
		} else {
			this.searchProvinceByName(false);
		}
	}
	
	searchProvinceByName(isPageChanged) {
		
		this.isSearchingProvince = true;
		this.provinceSearchResults = [];	
		
		if(this.showDeleteRecord == true){
	
			if(this.provinceSearchParam.searchString.trim() == '') {
				this.isSearchingProvince = false;
				this.getAllDeletedProvince(isPageChanged, this.currentPage);
				return;
			}
			
		if(/^[a-zA-Z0-9 -]*$/.test(this.provinceSearchParam.searchString.trim()) == true){			
			this.provinceService.searchDeletedProvinceOnPage(this.provinceSearchParam.searchString.trim(), this.provinceSearchParam.pageNo - 1).subscribe((results)  => {
				this.isSearchingProvince = false;
				if( results && results.data && results.data.content && results.data.content.length > 0 ) {
					 try { 
							var table = $('#provinceTable').DataTable();
							table.destroy();
							this.initializeDatatable();
						} catch (error) {
							console.error('Log error', error);
						}
					this.isSearchingProvince = true;
					this.provinceList = results.data.content;
					this.provinceSearchResults = results.data.content;
	                this.originalTotalElement = results.data.totalElements;
	                this.paginationConfig.totalElements = results.data.totalElements;
	                this.paginationConfig.totalPages = results.data.totalPages;
	                this.paginationConfig.maxSize = results.data.totalPages;
	            }else {
	                this.provinceSearchResultsMessage = results.message;
	                this.paginationConfig.totalElements = 0;
	                this.paginationConfig.totalPages = 0;
	                this.provinceList = [];
	            }
				 this.showLoader=false;
			},(error) => {

				 this.paginationConfig.totalElements = 0;
		         this.paginationConfig.totalPages = 0;
			});
		} else {
			 try { 
					var table = $('#provinceTable').DataTable();
					table.destroy();
					this.initializeDatatable();
				} catch (error) {
					console.error('Log error', error);
				}
			this.provinceList =[];
		}	
		}
		else{
			if(this.provinceSearchParam.searchString.trim() == '') {
				this.isSearchingProvince = true;
				this.getProvinceByPageNumber(isPageChanged, this.provinceSearchParam.pageNo - 1);
				return;
			}
			
			if(/^[a-zA-Z0-9 -]*$/.test(this.provinceSearchParam.searchString.trim()) == true){
			this.provinceService.searchActiveProvinceOnPage(this.provinceSearchParam.searchString.trim(), this.provinceSearchParam.pageNo - 1).subscribe((results)  => {
				 try { 
						var table = $('#provinceTable').DataTable();
						table.destroy();
						this.initializeDatatable();
					} catch (error) {
						console.error('Log error', error);
					}
				this.initializeDatatable();
				if( results && results.data && results.data.content && results.data.content.length > 0 ) {
					this.isSearchingProvince = true;				
					this.provinceList = results.data.content;
					this.provinceSearchResults = results.data.content;
	                this.originalTotalElement = results.data.totalElements;
	                this.paginationConfig.totalElements = results.data.totalElements;
	                this.paginationConfig.totalPages = results.data.totalPages;
	                this.paginationConfig.maxSize = results.data.totalPages;
	            } else {
	                this.provinceSearchResultsMessage = results.message;
	                this.paginationConfig.totalElements = 0;
	                this.paginationConfig.totalPages = 0;
	                this.provinceList = [];
	            }
				 this.showLoader=false;
			},(error) => {
			
				 this.paginationConfig.totalElements = 0;
		         this.paginationConfig.totalPages = 0;
			});
		
			} else {
				 try { 
						var table = $('#provinceTable').DataTable();
						table.destroy();
						this.initializeDatatable();
				   } catch (error) {
						console.error('Log error', error);
				   }
				this.provinceList =[];
			}
		}
	}

	addNewProvince() {
		
		this.router.navigateByUrl('/' + ROUTS.ADD_PROVINCE);
	}
	
	storeId(provinceId) {
		this.provinceId = provinceId;
	}
	
	deleteProvince() {
		this.error = '';
		this.provinceService.deleteProvince(this.provinceId).subscribe((results) => {
			this.deleted = true;
			setTimeout(() => {
				window.location.reload();	
			},800);
		}, (error) => {
			this.error = JSON.parse(error._body).message;
		});
	}
	
	restoreProvince(provinceId) {
		this.error = '';
		this.provinceService.restoreProvince(provinceId).subscribe((results) => {
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