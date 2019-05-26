import{Component,OnInit,Input,Output,EventEmitter}from'@angular/core';import{RouterModule,Routes,Router}from'@angular/router';import{ReactiveFormsModule,FormsModule,FormGroup,FormControl,Validators,FormBuilder}from'@angular/forms';import*as ROUTS from'../../constants/routs';

import{CONSTANTS}from'../../constants/constant';

import{BrandUserService}from'../../service/brandUser.service';import{BrandService}from'../../service/brand.service';import{CommonService}from'../../service/common.service';import{LocalStorageService}from'../../service/localStorage.service';import{TypeaheadModule}from'ngx-bootstrap/typeahead';

declare var $:any;

@Component
({selector:'',templateUrl:'./brandUser.component.html'
})export class BrandUserComponent implements OnInit {

	constructor(public router: Router, public brandUserService : BrandUserService, public brandService : BrandService, 
				public localStorageService : LocalStorageService, 
				public commonService : CommonService){}
	
	public brandUserList : any;
	public brandUserId : any;
	deleted : boolean = false;
	restored : boolean = false;
	error : string;
	
	brandUserForm: FormGroup;
	
	@Input() id: string;
	@Input() maxSize: number;
	@Output() pageChange: EventEmitter<number> = new EventEmitter() ;
	
	currentPage: number = 0;
	public pageLimitOptions = CONSTANTS.PAGE_NUMBER_LIMIT_OPTIONS;
	public originalTotalElement = 0;
	
	public isSearchingBrandUser : boolean = true;
	public brandUserSearchResultsMessage : any;
	public brandUserSearchResults : any[] = [];
    public isFirstTimeSearch: boolean = true;
	
	public startElementNum : any;
	public endElementNum : any;
	
	public brandId : number;
	public brandList : any[] = [];
	public brandObj : any[] = [];
	private value:any = {};
	private roleValue:any = {};
	public showDeleteRecord : boolean = false;
	public showLoader : boolean = false;
	
	public userRoleList:any[] = [];
	searchRole :any;
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
	
	public brandUserSearchParam = { 
			limit : this.pageLimitOptions[1], 
			pageNo : 1,
			searchString : "",
			startDate : undefined,
			endDate : undefined,
	};

	ngOnInit() {
		
		this.showLoader = true;
		
		this.curUserRole = this.localStorageService.getItem(CONSTANTS.USER_ROLE);
		
		this.brandUserForm = new FormGroup({
			brand: new FormControl('', [Validators.required]),
		});
		let roleList =[];
		 roleList = [
	                 {id: 0, text: "All"},   
	                 {id: 1, text: "Risk Admin Manager"},
	                 {id: 2, text: "Divisional Sales Manager"},
	                 {id: 3, text: "Store Manager"}
	                 ];
		
		for(let i = 0; i < roleList.length ; i++) {
			this.userRoleList.push(roleList[i].text);
		}
		this.searchRole = "All";
		console.log(this.userRoleList);
		console.log(this.searchRole);
		this.brandService.getAllActiveBrand().subscribe((results) => {
			
			if(results.data.length > 0) {
				this.brandObj = results.data;
				let brandName = results.data[0].name;
				this.brandOnSelect(brandName);
				this.localStorageService.setItem(CONSTANTS.BRAND, brandName);
				for(var i = 0; i < results.data.length ; i++) {
					 this.brandList.push(results.data[i].name);
				}
			}
		}, (error) => {
			
		});
		
		setTimeout(() => {
			this.initializeDatatable();
        }, 500);
	}

	brandOnSelect(event : any) {
		
		let brandName = event;
		
		this.localStorageService.setItem(CONSTANTS.BRAND, brandName);
	
		for(var i = 0; i < this.brandObj.length ; i++) {	
			 if(event == this.brandObj[i].name) {
				 this.brandId = this.brandObj[i].id;
				 this.localStorageService.setItem(CONSTANTS.BRAND_ID, this.brandId.toString());
			 }
		}
		
		if(this.showDeleteRecord){
			this.getAllDeletedBrandUser(false,this.currentPage);
		} else{
			this.getBrandUserByPageNumber(false, this.currentPage);
		}
	}

	public refreshValue(value:any):void {
	    this.value = value;
	}
	
	public refreshRoleValue(value:any):void {
	    this.roleValue = value;
	}

	roleOnSelect(event : any) {
		
		let roleName = event;
		console.log("roleName....."+roleName)
		console.log("brandId....."+this.brandId)
		this.searchRole = "";
		if(roleName == "All") {
			this.searchRole ="All";
		}
		if(roleName == "Risk Admin Manager") {
			this.searchRole ="ROLE_RAM_USER";
		}
		if(roleName == "Divisional Sales Manager") {
			this.searchRole ="ROLE_DSM_USER";
		}
		if(roleName == "Store Manager") {
			this.searchRole ="ROLE_SM_USER";
		}
		
//		console.log(searchRole);
	
		if(this.showDeleteRecord){
			this.getAllDeletedBrandUser(false,this.currentPage);
		} else{
			this.getBrandUserByPageNumber(false, this.currentPage);
		}
	}
	
	initializeDatatable() {
		$(function () {
		    $('#brandUserTable').DataTable({
		    	'columnDefs': [{
		            'orderable': false,
		            'targets': [3]
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

	showDeletedRecord() {	
		this.showLoader = true;
		this.brandUserSearchParam.searchString = "";
		var table = $('#brandUserTable').DataTable();
		table.destroy();
		this.initializeDatatable();
		this.showDeleteRecord = !this.showDeleteRecord;
		
		if(this.showDeleteRecord){
			this.getAllDeletedBrandUser(false,this.currentPage);
		} else{
			this.getBrandUserByPageNumber(false, this.currentPage);
		}
	}

	pageChanged(event: any): void {
		this.brandUserSearchParam.pageNo = event.page;
		if(this.showDeleteRecord == false){
			this.getBrandUserByPageNumber(true, this.brandUserSearchParam.pageNo - 1);
		}else{
			this.getAllDeletedBrandUser(true,this.brandUserSearchParam.pageNo - 1);
		}		
	}
	
	getAllDeletedBrandUser(isPageChanged ,pageNumber){
		if( !isPageChanged ) 
			this.brandUserSearchParam.pageNo = 1;
		if(this.brandUserSearchParam.searchString == '') {
		this.brandUserService.getAllDeletedBrandUser(this.brandId,pageNumber,this.searchRole).subscribe((results)  => {
			var table = $('#brandUserTable').DataTable();
			table.destroy();
			this.initializeDatatable();
			this.brandUserList = results.data.content;
			this.paginationConfig.maxSize =  results.data.totalPages ;
			this.paginationConfig.totalElements = results.data.totalElements;
			this.paginationConfig.totalPages = results.data.totalPages;
			this.paginationConfig.itemsPerPage = results.data.numberOfElements;
			this.showLoader=false;
		},(error) => {
			
		});
		} else {
			this.brandUserService.searchDeletedBrandUserOnPage(this.brandUserSearchParam.searchString.trim(),  this.localStorageService.getItem(CONSTANTS.BRAND_ID), pageNumber,this.searchRole).subscribe((results)  => {
				var table = $('#brandUserTable').DataTable();
				table.destroy();
				this.initializeDatatable();
					this.brandUserList = results.data.content;
					this.paginationConfig.maxSize =  results.data.totalPages ;
					this.paginationConfig.totalElements = results.data.totalElements;
					this.paginationConfig.totalPages = results.data.totalPages;
					this.paginationConfig.itemsPerPage = results.data.numberOfElements;
					this.showLoader=false;
					if(this.paginationConfig.totalElements > 0)
						this.startElementNum = 1;
				},(error) => {
					
				});
		}
	}
	
	getBrandUserByPageNumber(isPageChanged, pageNumber) {
		if( !isPageChanged ) 
			this.brandUserSearchParam.pageNo = 1;
		
		if(this.brandUserSearchParam.searchString == '') {
			this.brandUserService.getAllActiveBrandUser(this.brandId, pageNumber ,this.searchRole).subscribe((results)  => {
				var table = $('#brandUserTable').DataTable();
				table.destroy();
				this.initializeDatatable();
			
				if( results && results.data && results.data.content && results.data.content.length > 0 ) {
					this.isSearchingBrandUser = true;
					this.brandUserList = results.data.content;
					this.brandUserSearchResults = results.data.content;
	                this.originalTotalElement = results.data.totalElements;
	                this.paginationConfig.totalElements = results.data.totalElements;
	                this.paginationConfig.totalPages = results.data.totalPages;
	                this.paginationConfig.maxSize = results.data.totalPages;
	                this.paginationConfig.itemsPerPage = results.data.numberOfElements;
	            }else {
	                this.brandUserSearchResultsMessage = results.message;
	                this.paginationConfig.totalElements = 0;
	                this.paginationConfig.totalPages = 0;
	                this.brandUserList = [];
	            }
				
				
//				this.brandUserList = results.data.content;
//				this.paginationConfig.maxSize =  results.data.totalPages ;
//				this.paginationConfig.totalElements = results.data.totalElements;
//				this.paginationConfig.totalPages = results.data.totalPages;
//				this.paginationConfig.itemsPerPage = results.data.numberOfElements;
				this.showLoader=false;
				if(this.paginationConfig.totalElements > 0)
					this.startElementNum = 1;
			},(error) => {
				
			});
		} else {
//			this.brandUserService.searchBrandUserOnPage(this.brandUserSearchParam.searchString.trim(), this.localStorageService.getItem(CONSTANTS.BRAND_ID), pageNumber).subscribe((results)  => {
			this.brandUserService.searchActiveBrandUserOnPage(this.brandUserSearchParam.searchString.trim(),  this.localStorageService.getItem(CONSTANTS.BRAND_ID), pageNumber,this.searchRole).subscribe((results)  => {
				var table = $('#brandUserTable').DataTable();
				table.destroy();
				this.initializeDatatable();
				if( results && results.data && results.data.content && results.data.content.length > 0 ) {
					this.isSearchingBrandUser = true;
					this.brandUserList = results.data.content;
					this.brandUserSearchResults = results.data.content;
	                this.originalTotalElement = results.data.totalElements;
	                this.paginationConfig.totalElements = results.data.totalElements;
	                this.paginationConfig.totalPages = results.data.totalPages;
	                this.paginationConfig.maxSize = results.data.totalPages;
	                this.paginationConfig.itemsPerPage = results.data.numberOfElements;
	            }else {
	                this.brandUserSearchResultsMessage = results.message;
	                this.paginationConfig.totalElements = 0;
	                this.paginationConfig.totalPages = 0;
	                this.brandUserList = [];
	            }
				
//				this.brandUserList = results.data.content;
//				this.paginationConfig.maxSize =  results.data.totalPages ;
//				this.paginationConfig.totalElements = results.data.totalElements;
//				this.paginationConfig.totalPages = results.data.totalPages;
//				this.paginationConfig.itemsPerPage = results.data.numberOfElements;
				this.showLoader=false;
				if(this.paginationConfig.totalElements > 0)
					this.startElementNum = 1;
			},(error) => {
				
			});
		}
	}
	
	searchBrandUser(isPageChanged) {
		
		this.isSearchingBrandUser = true;
		this.brandUserSearchResults = [];
		
		if(this.showDeleteRecord == true){
			
			if(this.brandUserSearchParam.searchString.trim() == '') {
				this.isSearchingBrandUser = true;
				this.getAllDeletedBrandUser(isPageChanged, this.brandUserSearchParam.pageNo - 1);
				return;
			} else {
				this.brandUserService.searchDeletedBrandUserOnPage(this.brandUserSearchParam.searchString.trim(),  this.localStorageService.getItem(CONSTANTS.BRAND_ID), this.brandUserSearchParam.pageNo - 1, this.searchRole).subscribe((results)  => {
					this.isSearchingBrandUser = false;
					var table = $('#brandUserTable').DataTable();
					table.destroy();
					this.initializeDatatable();
					if( results && results.data && results.data.content && results.data.content.length > 0 ) {
						this.isSearchingBrandUser = true;
						this.brandUserList = results.data.content;
						this.brandUserSearchResults = results.data.content;
		                this.originalTotalElement = results.data.totalElements;
		                this.paginationConfig.totalElements = results.data.totalElements;
		                this.paginationConfig.totalPages = results.data.totalPages;
		                this.paginationConfig.maxSize = results.data.totalPages;
		                this.paginationConfig.itemsPerPage = results.data.numberOfElements;
		            }else {
		                this.brandUserSearchResultsMessage = results.message;
		                this.paginationConfig.totalElements = 0;
		                this.paginationConfig.totalPages = 0;
		                this.brandUserList = [];
		            }
				},(error) => {
					 this.paginationConfig.totalElements = 0;
			         this.paginationConfig.totalPages = 0;
				});
			}
			if(/^[a-zA-Z0-9- _@`.]*$/.test(this.brandUserSearchParam.searchString.trim()) == true){
			this.brandUserService.searchDeletedBrandUserOnPage(this.brandUserSearchParam.searchString.trim(),  this.localStorageService.getItem(CONSTANTS.BRAND_ID), this.brandUserSearchParam.pageNo - 1, this.searchRole).subscribe((results)  => {
				this.isSearchingBrandUser = false;
				var table = $('#brandUserTable').DataTable();
				table.destroy();
				this.initializeDatatable();
				if( results && results.data && results.data.content && results.data.content.length > 0 ) {
					this.isSearchingBrandUser = true;
					this.brandUserList = results.data.content;
					this.brandUserSearchResults = results.data.content;
	                this.originalTotalElement = results.data.totalElements;
	                this.paginationConfig.totalElements = results.data.totalElements;
	                this.paginationConfig.totalPages = results.data.totalPages;
	                this.paginationConfig.maxSize = results.data.totalPages;
	                this.paginationConfig.itemsPerPage = results.data.numberOfElements;
	            }else {
	                this.brandUserSearchResultsMessage = results.message;
	                this.paginationConfig.totalElements = 0;
	                this.paginationConfig.totalPages = 0;
	                this.brandUserList = [];
	            }
			},(error) => {
				 this.paginationConfig.totalElements = 0;
		         this.paginationConfig.totalPages = 0;
			});
			} else {
				var table = $('#brandUserTable').DataTable();
				table.destroy();
				this.initializeDatatable();
				this.brandUserList =[];
			}
		}
		else{
			
			if(this.brandUserSearchParam.searchString.trim() == '') {
			
				this.isSearchingBrandUser = true;
				this.getBrandUserByPageNumber(isPageChanged, this.brandUserSearchParam.pageNo - 1);
				return;
			} else {
				this.brandUserService.searchActiveBrandUserOnPage(this.brandUserSearchParam.searchString.trim(),  this.localStorageService.getItem(CONSTANTS.BRAND_ID), this.brandUserSearchParam.pageNo - 1,this.searchRole).subscribe((results)  => {
					this.isSearchingBrandUser = false;
					var table = $('#brandUserTable').DataTable();
					table.destroy();
					this.initializeDatatable();
					if( results && results.data && results.data.content && results.data.content.length > 0 ) {
						this.isSearchingBrandUser = true;
						this.brandUserList = results.data.content;
						this.brandUserSearchResults = results.data.content;
		                this.originalTotalElement = results.data.totalElements;
		                this.paginationConfig.totalElements = results.data.totalElements;
		                this.paginationConfig.totalPages = results.data.totalPages;
		                this.paginationConfig.maxSize = results.data.totalPages;
		                this.paginationConfig.itemsPerPage = results.data.numberOfElements;
		            }else {
		                this.brandUserSearchResultsMessage = results.message;
		                this.paginationConfig.totalElements = 0;
		                this.paginationConfig.totalPages = 0;
		                this.brandUserList = [];
		            }
				},(error) => {
					 this.paginationConfig.totalElements = 0;
			         this.paginationConfig.totalPages = 0;
				});
			}
			if(/^[a-zA-Z0-9- _@`.]*$/.test(this.brandUserSearchParam.searchString.trim()) == true){
		
			this.brandUserService.searchActiveBrandUserOnPage(this.brandUserSearchParam.searchString.trim(),  this.localStorageService.getItem(CONSTANTS.BRAND_ID), this.brandUserSearchParam.pageNo - 1, this.searchRole).subscribe((results)  => {
				this.isSearchingBrandUser = false;
				var table = $('#brandUserTable').DataTable();
				table.destroy();
				this.initializeDatatable();
				if( results && results.data && results.data.content && results.data.content.length > 0 ) {
					this.isSearchingBrandUser = true;
					this.brandUserList = results.data.content;
					this.brandUserSearchResults = results.data.content;
	                this.originalTotalElement = results.data.totalElements;
	                this.paginationConfig.totalElements = results.data.totalElements;
	                this.paginationConfig.totalPages = results.data.totalPages;
	                this.paginationConfig.maxSize = results.data.totalPages;
	                this.paginationConfig.itemsPerPage = results.data.numberOfElements;
	            }else {
	                this.brandUserSearchResultsMessage = results.message;
	                this.paginationConfig.totalElements = 0;
	                this.paginationConfig.totalPages = 0;
	                this.brandUserList = [];
	            }
			},(error) => {
				 this.paginationConfig.totalElements = 0;
		         this.paginationConfig.totalPages = 0;
			});
			} else {
				var table = $('#brandUserTable').DataTable();
				table.destroy();
				this.initializeDatatable();
				this.brandUserList =[];
			}
		}
	
	}
	
	storeId(brandUserId) {
		this.brandUserId = brandUserId;
	}

	delete() {
		this.brandUserService.deleteBrandUser(this.brandUserId).subscribe((results) => {
			this.deleted = true;
			setTimeout(() => {
				window.location.reload();	
			},800);
		}, (error) => {
			this.error = JSON.parse(error._body).message;
		});
	}

	restoreBrand(brandUserId) {
		this.brandUserService.restoreBrandUser(brandUserId).subscribe((results) => {
			this.restored = true;
			setTimeout(() => {
				window.location.reload();	
			},800);
		}, (error) => {
			
		});
	}
	
	setBrand(brand : string) {
		this.localStorageService.setItem(CONSTANTS.BRAND, brand);
	}

	addNew() {
		this.router.navigateByUrl('/' + ROUTS.ADD_BRAND_USER);
	}

	addNewBrand() {
		this.router.navigateByUrl('/' + ROUTS.ADD_BRAND);
	}
}