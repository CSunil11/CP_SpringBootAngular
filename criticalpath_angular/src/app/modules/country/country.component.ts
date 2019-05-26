import{Component,OnInit}from'@angular/core';import{RouterModule,Routes,Router}from'@angular/router';

import*as ROUTS from'../../constants/routs';import{CONSTANTS}from'../../constants/constant';

import{CountryService}from'../../service/country.service'
import{BlockUI,NgBlockUI,BlockUIModule}from'ng-block-ui';
import { LocalStorageService } from '../../service/localStorage.service';
declare var $:any;

@Component
({selector:'',templateUrl:'./country.component.html'
})export class CountryComponent implements OnInit {

	constructor(public router: Router, public countryService : CountryService, public localStorageService : LocalStorageService){}
	
	public countryList : any;
	public uploadLogs : any = [];
	public uploadedSuccess : boolean ;
	formData: any;
	typeError : boolean = false;
	invalid : boolean = false;
	public countryId : any;
	deleted : boolean = false;
	restored : boolean = false;
	error : string;
	public showDeleteRecord : boolean = false;
	public showLoader : boolean = false;
	
	currentPage: number = 0;
	public pageLimitOptions = CONSTANTS.PAGE_NUMBER_LIMIT_OPTIONS;
	public originalTotalElement = 0;
	
	public isSearchingCountry : boolean = true;
	public countrySearchResultsMessage : any;
	public countrySearchResults : any[] = [];
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
	
	public countrySearchParam = {
			pageNo : 1,
			searchString : "",
			startDate : undefined,
			endDate : undefined,
	};
	//Wires up BlockUI instance
	@BlockUI() blockUI: NgBlockUI;

	ngOnInit() {		
		
		this.formData = new FormData();
		
//		this.loadAllActivedata();
		this.getCountryByPageNumber(false, this.currentPage);

		this.curUserRole = this.localStorageService.getItem(CONSTANTS.USER_ROLE);
		console.log(this.curUserRole);
		
		setTimeout(() => {
			this.initializeDatatable();
        }, 500);
	}

	initializeDatatable() {
		$(function () {
		    $('#countryTable').DataTable({
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
		
		var table = $('#countryTable').DataTable();
		
		this.countryService.getAllActiveCountry().subscribe((results)  => {
			table.destroy();
			this.initializeDatatable();
			
			this.countryList = results.data;
			
		},(error) => {
			
		});
	}
	
	pageChanged(event: any): void {
		this.countrySearchParam.pageNo = event.page;
		
		if(this.showDeleteRecord == false){
			this.getCountryByPageNumber(true, this.countrySearchParam.pageNo - 1);
		}else{
			this.getAllDeletedCountry(true,this.countrySearchParam.pageNo - 1);
		}
	}
	
	ShowDeleteRecord() {
		
		this.showLoader = true;
		
		this.showDeleteRecord = !this.showDeleteRecord;

		this.countrySearchParam.searchString = "";
		
		if(this.showDeleteRecord){
			this.getAllDeletedCountry(false,this.currentPage);
		} else{
			
			this.getCountryByPageNumber(false, this.currentPage);
		}
	}
	
	getCountryByPageNumber(isPageChanged, pageNumber) {
		if( !isPageChanged ) 
			this.countrySearchParam.pageNo = 1;
		 if(this.countrySearchParam.searchString == '') {
			this.countryService.getAllActiveCountryByPageNumber(pageNumber).subscribe((results)  => {
			 try { 
				var table = $('#countryTable').DataTable();
				table.destroy();
				this.initializeDatatable();
			} catch (error) {
				console.error('Log error', error);
			}
			 
			this.countryList = results.data.content;
			this.paginationConfig.maxSize =  results.data.totalPages ;
			this.paginationConfig.totalElements = results.data.totalElements;
			this.paginationConfig.totalPages = results.data.totalPages;
			this.paginationConfig.itemsPerPage = results.data.numberOfElements;
			this.showLoader=false;
		},(error) => {
			
		});
	  } else {
		this.searchCountryByName(false);
	  }
	}
	
	getAllDeletedCountry(isPageChanged ,pageNumber){
		if( !isPageChanged ) 
			this.countrySearchParam.pageNo = 1;
		console.log("33");
	 if(this.countrySearchParam.searchString == '') {
		this.countryService.getAllDeletedCountryByPageNumber(pageNumber).subscribe((results)  => {
			console.log(results);
			this.countryList =[];
			 try { 
				var table = $('#countryTable').DataTable();
				table.destroy();
				this.initializeDatatable();
				}
				catch (error) {
				console.error('Log error', error);
				}
			
			this.countryList = results.data.content;
			this.countrySearchResults = results.data.content;
            this.originalTotalElement = results.data.totalElements;
            this.paginationConfig.totalElements = results.data.totalElements;
            this.paginationConfig.totalPages = results.data.totalPages;
            this.paginationConfig.maxSize = results.data.totalPages;
            this.paginationConfig.itemsPerPage = results.data.numberOfElements;
            this.showLoader=false;
            console.log(this.countryList);
		},(error) => {
			
		});
		} else {
			this.searchCountryByName(false);
		}
	}
	
	searchCountryByName(isPageChanged) {
		console.log(this.countrySearchParam.searchString);
		this.isSearchingCountry = true;
		this.countrySearchResults = [];	
		
		if(this.showDeleteRecord == true){
	
			if(this.countrySearchParam.searchString.trim() == '') {
				console.log("1");
				this.isSearchingCountry = false;
				this.getAllDeletedCountry(isPageChanged, this.currentPage);
				return;
			}
			
		if(/^[a-zA-Z0-9 -]*$/.test(this.countrySearchParam.searchString.trim()) == true){	
			console.log("2");
			this.countryService.searchDeletedCountryOnPage(this.countrySearchParam.searchString.trim(), this.countrySearchParam.pageNo - 1).subscribe((results)  => {
				this.isSearchingCountry = false;
				console.log(results);
				if( results && results.data && results.data.content && results.data.content.length > 0 ) {
					 try { 
							var table = $('#countryTable').DataTable();
							table.destroy();
							this.initializeDatatable();
						} catch (error) {
							console.error('Log error', error);
						}
					this.isSearchingCountry = true;
					this.countryList = results.data.content;
					this.countrySearchResults = results.data.content;
	                this.originalTotalElement = results.data.totalElements;
	                this.paginationConfig.totalElements = results.data.totalElements;
	                this.paginationConfig.totalPages = results.data.totalPages;
	                this.paginationConfig.maxSize = results.data.totalPages;
	            }else {
	                this.countrySearchResultsMessage = results.message;
	                this.paginationConfig.totalElements = 0;
	                this.paginationConfig.totalPages = 0;
	                this.countryList = [];
	            }
				 this.showLoader=false;
			},(error) => {

				 this.paginationConfig.totalElements = 0;
		         this.paginationConfig.totalPages = 0;
			});
		} else {
			 try { 
					var table = $('#countryTable').DataTable();
					table.destroy();
					this.initializeDatatable();
				} catch (error) {
					console.error('Log error', error);
				}
			this.countryList =[];
		}	
		}
		else{
			if(this.countrySearchParam.searchString.trim() == '') {
				this.isSearchingCountry = true;
				this.getCountryByPageNumber(isPageChanged, this.countrySearchParam.pageNo - 1);
				return;
			}
			
			if(/^[a-zA-Z0-9 -]*$/.test(this.countrySearchParam.searchString.trim()) == true){
			this.countryService.searchActiveCountryOnPage(this.countrySearchParam.searchString.trim(), this.countrySearchParam.pageNo - 1).subscribe((results)  => {
				 try { 
						var table = $('#countryTable').DataTable();
						table.destroy();
						this.initializeDatatable();
					} catch (error) {
						console.error('Log error', error);
					}
				this.initializeDatatable();
				if( results && results.data && results.data.content && results.data.content.length > 0 ) {
					this.isSearchingCountry = true;				
					this.countryList = results.data.content;
					this.countrySearchResults = results.data.content;
	                this.originalTotalElement = results.data.totalElements;
	                this.paginationConfig.totalElements = results.data.totalElements;
	                this.paginationConfig.totalPages = results.data.totalPages;
	                this.paginationConfig.maxSize = results.data.totalPages;
	            } else {
	                this.countrySearchResultsMessage = results.message;
	                this.paginationConfig.totalElements = 0;
	                this.paginationConfig.totalPages = 0;
	                this.countryList = [];
	            }
				 this.showLoader=false;
			},(error) => {
			
				 this.paginationConfig.totalElements = 0;
		         this.paginationConfig.totalPages = 0;
			});
		
			} else {
				 try { 
						var table = $('#countryTable').DataTable();
						table.destroy();
						this.initializeDatatable();
				   } catch (error) {
						console.error('Log error', error);
				   }
				this.countryList =[];
			}
		}
	}
	
	addNewCountry() {
		
		this.router.navigateByUrl('/' + ROUTS.ADD_COUNTRY);
	}

	upload(event : any) {
		
			var table = $('#countryTable').DataTable();
		
			this.invalid = false;
			this.typeError = false;
	        let fileList: FileList = event.target.files;

	        if (fileList.length > 0) {

	            let file: File = fileList[0];

	            if (file.type == "text/csv") {

//	                if (file.size < CONSTANTS.MAX_FILE_UPLOAD_SIZE_LIMIT) {

	                    this.formData.append('data', event.target.files[0], file.name);
	                    this.blockUI.start(CONSTANTS.blockUIMessage);
	            		this.uploadLogs = [];
	            		setTimeout(() => {
	            			this.countryService.upload(this.formData).subscribe((results)  => {
	            				 
	            				table.destroy();
	            				this.blockUI.stop();
	            				this.formData = new FormData();
	            				
	            				if(results.data.length > 0) {
	            					if(results.data.includes(CONSTANTS.invalidCsvMessage)) {
	            						this.invalid = true;
	            						setTimeout(() => {
	            							this.invalid = false;
	    	            				},4000);
	            					} else
	            						this.uploadLogs = results.data;
	            				}
	            				else  {
	            					this.uploadedSuccess = true;
	            					setTimeout(() => {
		            					this.uploadedSuccess = false;
		            				},2000);
	            				}
	            				
	            				this.countryService.getAllCountry().subscribe((results)  => {
	            					this.countryList = results.data;
		            			});
	            				setTimeout(() => {
	            					this.initializeDatatable();
	            				},3000);
	            				
	            			},(error) => {
	            				console.log('Error while uploading');
	            			});
	                    }, 1000);
//	                }
	            } else {
	            	this.typeError = true;
	            	setTimeout(() => {
						this.typeError = false;
    				},4000);
	            	return;
	            }
	            	
	        }
	}
	
	storeId(countryId) {
		this.countryId = countryId;
	}
	
	deleteCountry() {
		this.error = '';
		this.countryService.deleteCountry(this.countryId).subscribe((results) => {
			this.deleted = true;
			setTimeout(() => {
				window.location.reload();	
			},800);
		}, (error) => {
			this.error = JSON.parse(error._body).message;
		});
	}
	
	restoreCountry(countryId) {
		this.error = '';
		this.countryService.restoreCountry(countryId).subscribe((results) => {
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