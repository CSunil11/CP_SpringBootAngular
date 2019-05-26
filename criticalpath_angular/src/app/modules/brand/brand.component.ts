import {Component, OnInit,Input,Output, EventEmitter } from '@angular/core';
import { RouterModule, Routes, Router } from '@angular/router';

import * as ROUTS from '../../constants/routs';

import {CONSTANTS} from '../../constants/constant';

import { BrandService } from '../../service/brand.service';

import { LocalStorageService } from '../../service/localStorage.service';

declare var $: any;

@Component({
	selector :'',
	templateUrl : './brand.component.html'
})
export class BrandComponent implements OnInit {
	
	constructor(public router: Router, public brandService : BrandService, public localStorageService : LocalStorageService){}
	
	public brandList : any;
	public brandId : any;
	deleted : boolean = false;
	restored : boolean = false;
	error : string;
	public showLoader : boolean = false;
	
	@Input() id: string;
	@Input() maxSize: number;
	@Output() pageChange: EventEmitter<number> = new EventEmitter() ;
	
	currentPage: number = 0;
//	public pageLimitOptions = CONSTANTS.PAGE_NUMBER_LIMIT_OPTIONS;
	public originalTotalElement = 0;
	
	public isSearchingBrand : boolean = true;
	public brandSearchResultsMessage : any;
	public brandSearchResults : any[] = [];
    public isFirstTimeSearch: boolean = true;
	
	public startElementNum : any;
	public endElementNum : any;
	public showDeleteRecord : boolean = false;
	public curUserRole : any;
	
	public paginationConfig = { 
			totalPages : 1,
			totalElements : 0,
//			boundaryLinks : true, 
			maxSize : 2,
			rotate : false, 
			align : true,
			firstText : "&laquo;",
			previousText : "Previous",
			nextText : "Next",
			lastText : "&raquo;",
			itemsPerPage : 0
		};
	
	public brandSearchParam = { 
//			limit : this.pageLimitOptions[2], 
			pageNo : 1,
			searchString : "",
			startDate : undefined,
			endDate : undefined,
		};
	ngOnInit() {
		this.showLoader = true;
		
		this.getBrandByPageNumber(false, this.currentPage);
		
		this.curUserRole = this.localStorageService.getItem(CONSTANTS.USER_ROLE);
		
		setTimeout(() => {
			this.initializeDatatable();
        }, 500);
		
	}
	
	initializeDatatable() {
		$(function () {
		    $('#brandTable').DataTable({
		    	'columnDefs': [{
		            'orderable': false,
//		            'targets': 2
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
		this.brandSearchParam.pageNo = event.page;
		
		if(this.showDeleteRecord == false){
				this.getBrandByPageNumber(true, this.brandSearchParam.pageNo - 1);
		}else{
			this.getAllDeletedBrand(true,this.brandSearchParam.pageNo - 1);
		}
	}
	
	getBrandByPageNumber(isPageChanged, pageNumber) {
		if( !isPageChanged ) 
			this.brandSearchParam.pageNo = 1;
		
			this.brandService.getAllActiveBrandByPageNumber(pageNumber).subscribe((results)  => {
			var table = $('#brandTable').DataTable();
			table.destroy();
			this.initializeDatatable();
			this.brandList = results.data.content;
			this.paginationConfig.maxSize =  results.data.totalPages ;
			this.paginationConfig.totalElements = results.data.totalElements;
			this.paginationConfig.totalPages = results.data.totalPages;
			this.paginationConfig.itemsPerPage = results.data.numberOfElements;
			
			if(this.paginationConfig.totalElements > 0)
				this.startElementNum = 1;
		},(error) => {
			
		});
	}
	
	searchBrandByName(isPageChanged) {
		
		this.isSearchingBrand = true;
		this.brandSearchResults = [];	
		
		if(this.showDeleteRecord == true){
			if(this.brandSearchParam.searchString.trim() == '') {
				this.isSearchingBrand = false;
				this.getAllDeletedBrand(isPageChanged, this.brandSearchParam.pageNo - 1);
				return;
			}
			
			if(/^[a-zA-Z0-9 -]*$/.test(this.brandSearchParam.searchString.trim()) == true){
			this.brandService.searchDeletedBrandOnPage(this.brandSearchParam.searchString.trim(), this.brandSearchParam.pageNo - 1).subscribe((results)  => {
				this.isSearchingBrand = false;
				var table = $('#brandTable').DataTable();
				table.destroy();
				this.initializeDatatable();
				if( results && results.data && results.data.content && results.data.content.length > 0 ) {
					this.isSearchingBrand = true;
					this.brandList = results.data.content;
					this.brandSearchResults = results.data.content;
	                this.originalTotalElement = results.data.totalElements;
	                this.paginationConfig.totalElements = results.data.totalElements;
	                this.paginationConfig.totalPages = results.data.totalPages;
	                this.paginationConfig.maxSize = results.data.totalPages;
	                this.paginationConfig.itemsPerPage = results.data.numberOfElements;
	            }else {
	                this.brandSearchResultsMessage = results.message;
	                this.paginationConfig.totalElements = 0;
	                this.paginationConfig.totalPages = 0;
	                this.brandList = [];
	            }
				
			},(error) => {

				 this.paginationConfig.totalElements = 0;
		         this.paginationConfig.totalPages = 0;
				});
			} else {
				var table = $('#brandTable').DataTable();
				table.destroy();
				this.initializeDatatable();
				this.brandList =[];
			}
		}
		else{
			
			if(this.brandSearchParam.searchString.trim() == '') {
				this.isSearchingBrand = true;
				this.getBrandByPageNumber(isPageChanged, this.brandSearchParam.pageNo - 1);
				return;
			}
			
			
			if(/^[a-zA-Z0-9 -]*$/.test(this.brandSearchParam.searchString.trim()) == true){	
			this.brandService.searchActiveBrandOnPage(this.brandSearchParam.searchString.trim(), this.brandSearchParam.pageNo - 1).subscribe((results)  => {
				this.isSearchingBrand = false;
				var table = $('#brandTable').DataTable();
				table.destroy();
				this.initializeDatatable();
				
				if( results && results.data && results.data.content && results.data.content.length > 0 ) {
					this.isSearchingBrand = true;				
					this.brandList = results.data.content;
					this.brandSearchResults = results.data.content;
	                this.originalTotalElement = results.data.totalElements;
	                this.paginationConfig.totalElements = results.data.totalElements;
	                this.paginationConfig.totalPages = results.data.totalPages;
	                this.paginationConfig.maxSize = results.data.totalPages;
	                this.paginationConfig.itemsPerPage = results.data.numberOfElements;
	            } else {
	                this.brandSearchResultsMessage = results.message;
	                this.paginationConfig.totalElements = 0;
	                this.paginationConfig.totalPages = 0;
	                this.brandList = [];
	            }
			},(error) => {
			
				 this.paginationConfig.totalElements = 0;
		         this.paginationConfig.totalPages = 0;
			});
		} else {
			var table = $('#brandTable').DataTable();
			table.destroy();
			this.initializeDatatable();
			this.brandList =[];
		}
			
		}
	}
	
	getAllDeletedBrand(isPageChanged ,pageNumber){
		if( !isPageChanged ) 
			this.brandSearchParam.pageNo = 1;
		
		this.brandService.getAllDeletedBrandByPageNumber(pageNumber).subscribe((results)  => {
			var table = $('#brandTable').DataTable();
			table.destroy();
			this.initializeDatatable();
			this.brandList = results.data.content;
			this.brandSearchResults = results.data.content;
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
			this.getAllDeletedBrand(false,this.currentPage);
		} else{
			this.getBrandByPageNumber(false, this.currentPage);
		}
	}
	
	storeId(brandId) {
		this.brandId = brandId;
	}
	
	addNewBrand() {
		this.router.navigateByUrl('/' + ROUTS.ADD_BRAND);
	}
	
	deleteBrand() {
		this.brandService.deleteBrand(this.brandId).subscribe((results) => {
			this.deleted = true;
			setTimeout(() => {
				window.location.reload();	
			},800);
		}, (error) => {
			this.error = JSON.parse(error._body).message;
		});
	}
	
	restoreBrand(brandId) {
		this.brandService.restoreBrand(brandId).subscribe((results) => {
			this.restored = true;
			setTimeout(() => {
				window.location.reload();	
			},800);
		}, (error) => {
			
		});
	}
}