import {Component, OnInit,Input,Output, EventEmitter } from '@angular/core';
import { RouterModule, Routes, Router } from '@angular/router';

import * as ROUTS from '../../constants/routs';

import {CONSTANTS} from '../../constants/constant';

import { StoreService } from '../../service/store.service';

import { LocalStorageService } from '../../service/localStorage.service';

declare var $: any;

@Component({
	selector :'',
	templateUrl : './store.component.html'
})
export class StoreComponent implements OnInit {
	
	constructor(public router: Router, public storeService : StoreService, public localStorageService : LocalStorageService){}
	
	public storeList : any;
	public storeId : any;
	deleted : boolean = false;
	restored : boolean = false;
	error : string;
	public showLoader : boolean = false;
	
	@Input() id: string;
	@Input() maxSize: number;
	@Output() pageChange: EventEmitter<number> = new EventEmitter() ;
	
	currentPage: number = 0;
	public pageLimitOptions = CONSTANTS.PAGE_NUMBER_LIMIT_OPTIONS;
	public originalTotalElement = 0;
	
	public isSearchingStore : boolean = true;
	public storeSearchResultsMessage : any;
	public storeSearchResults : any[] = [];
    public isFirstTimeSearch: boolean = true;
	public showDeleteRecord : boolean = false;
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
	
	public storeSearchParam = {
			pageNo : 1,
			searchString : "",
			startDate : undefined,
			endDate : undefined,
	};
	
	ngOnInit() {
		
		this.showLoader = true;
		
		this.getStoreByPageNumber(false, this.currentPage);
		
		this.curUserRole = this.localStorageService.getItem(CONSTANTS.USER_ROLE);
		
		setTimeout(() => {
			this.initializeDatatable();
        }, 500);
	}
	
	initializeDatatable() {
		$(function () {
		    $('#storeTable').DataTable({
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
		
		this.showLoader = false;
	}
	
	pageChanged(event: any): void {
		this.storeSearchParam.pageNo = event.page;
		
		if(this.showDeleteRecord == false){
			this.getStoreByPageNumber(true, this.storeSearchParam.pageNo - 1);
		}else{
			this.getAllDeletedStore(true,this.storeSearchParam.pageNo - 1);
		}
//			this.getStoreByPageNumber(true, this.storeSearchParam.pageNo - 1);
	}
	
	getStoreByPageNumber(isPageChanged, pageNumber) {
		if( !isPageChanged ) 
			this.storeSearchParam.pageNo = 1;
			this.storeService.getAllActiveStoreByPageNumber(pageNumber).subscribe((results)  => {
			var table = $('#storeTable').DataTable();
			table.destroy();
			this.initializeDatatable();
			this.storeList = results.data.content;
			this.paginationConfig.maxSize =  results.data.totalPages ;
			this.paginationConfig.totalElements = results.data.totalElements;
			this.paginationConfig.totalPages = results.data.totalPages;
			this.paginationConfig.itemsPerPage = results.data.numberOfElements;
		},(error) => {
			
		});
	}
	
	searchStoreByName(isPageChanged) {
		
		this.isSearchingStore = true;
		this.storeSearchResults = [];	
		
		if(this.showDeleteRecord == true){
	
			if(this.storeSearchParam.searchString.trim() == '') {
				this.isSearchingStore = false;
				this.getAllDeletedStore(isPageChanged, this.currentPage);
				return;
			}
			
		if(/^[a-zA-Z0-9 -]*$/.test(this.storeSearchParam.searchString.trim()) == true){			
			this.storeService.searchDeletedStoreOnPage(this.storeSearchParam.searchString.trim(), this.storeSearchParam.pageNo - 1).subscribe((results)  => {
				this.isSearchingStore = false;
				var table = $('#storeTable').DataTable();
				table.destroy();
				this.initializeDatatable();
				if( results && results.data && results.data.content && results.data.content.length > 0 ) {
					this.isSearchingStore = true;
					this.storeList = results.data.content;
					this.storeSearchResults = results.data.content;
	                this.originalTotalElement = results.data.totalElements;
	                this.paginationConfig.totalElements = results.data.totalElements;
	                this.paginationConfig.totalPages = results.data.totalPages;
	                this.paginationConfig.maxSize = results.data.totalPages;
	            }else {
	                this.storeSearchResultsMessage = results.message;
	                this.paginationConfig.totalElements = 0;
	                this.paginationConfig.totalPages = 0;
	                this.storeList = [];
	            }
				
			},(error) => {

				 this.paginationConfig.totalElements = 0;
		         this.paginationConfig.totalPages = 0;
			});
		} else {
			var table = $('#storeTable').DataTable();
			table.destroy();
			this.initializeDatatable();
			this.storeList =[];
		}	
			
		
		}
		else{
			if(this.storeSearchParam.searchString.trim() == '') {
				this.isSearchingStore = true;
				this.getStoreByPageNumber(isPageChanged, this.storeSearchParam.pageNo - 1);
				return;
			}
			
			if(/^[a-zA-Z0-9 -]*$/.test(this.storeSearchParam.searchString.trim()) == true){
			this.storeService.searchActiveStoreOnPage(this.storeSearchParam.searchString.trim(), this.storeSearchParam.pageNo - 1).subscribe((results)  => {
				this.isSearchingStore = false;
				var table = $('#storeTable').DataTable();
				table.destroy();
				this.initializeDatatable();
				if( results && results.data && results.data.content && results.data.content.length > 0 ) {
					this.isSearchingStore = true;				
					this.storeList = results.data.content;
					this.storeSearchResults = results.data.content;
	                this.originalTotalElement = results.data.totalElements;
	                this.paginationConfig.totalElements = results.data.totalElements;
	                this.paginationConfig.totalPages = results.data.totalPages;
	                this.paginationConfig.maxSize = results.data.totalPages;
	            } else {
	                this.storeSearchResultsMessage = results.message;
	                this.paginationConfig.totalElements = 0;
	                this.paginationConfig.totalPages = 0;
	                this.storeList = [];
	            }
			},(error) => {
			
				 this.paginationConfig.totalElements = 0;
		         this.paginationConfig.totalPages = 0;
			});
		
			} else {
				var table = $('#storeTable').DataTable();
				table.destroy();
				this.initializeDatatable();
				this.storeList =[];
			}
		}
	}
	
	getAllDeletedStore(isPageChanged ,pageNumber){
		if( !isPageChanged ) 
			this.storeSearchParam.pageNo = 1;
		
		this.storeService.getAllDeletedStoreByPageNumber(pageNumber).subscribe((results)  => {
			var table = $('#storeTable').DataTable();
			table.destroy();
			this.initializeDatatable();
			this.storeList = results.data.content;
			this.storeSearchResults = results.data.content;
            this.originalTotalElement = results.data.totalElements;
            this.paginationConfig.totalElements = results.data.totalElements;
            this.paginationConfig.totalPages = results.data.totalPages;
            this.paginationConfig.maxSize = results.data.totalPages;
            this.paginationConfig.itemsPerPage = results.data.numberOfElements;
		},(error) => {
			
		});
	}
	
	showDeletedRecord() {		
		
		this.showLoader = true;
		
		this.showDeleteRecord = !this.showDeleteRecord;
		
		if(this.showDeleteRecord){
			this.getAllDeletedStore(false,this.currentPage);
		} else{
			
			this.getStoreByPageNumber(false, this.currentPage);
		}
	}
	
	getId(id) {
		this.storeId = id;
	}
	
	addNewStore() {
		this.router.navigateByUrl('/' + ROUTS.ADD_STORE);
	}
	
	deleteStore() {
		this.storeService.deleteStore(this.storeId).subscribe((results) => {
			this.deleted = true;
			setTimeout(() => {
				window.location.reload();	
			},800);
		}, (error) => {
			this.error = JSON.parse(error._body).message;
		});
	}
	
	restoreStore(storeId) {
		this.storeService.restoreStore(storeId).subscribe((results) => {
			this.restored = true;
			setTimeout(() => {
				window.location.reload();	
			},800);
		}, (error) => {
			
		});
	}
}