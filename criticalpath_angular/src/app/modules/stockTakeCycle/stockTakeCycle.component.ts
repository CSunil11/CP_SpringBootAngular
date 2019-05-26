import {Component, OnInit } from '@angular/core';
import { RouterModule, Routes, Router } from '@angular/router';

import * as ROUTS from '../../constants/routs';
import {CONSTANTS} from '../../constants/constant';

import { StockTakeCycleService } from '../../service/stockTakeCycle.service'
import { LocalStorageService } from '../../service/localStorage.service';

declare var $: any;

@Component({
	selector :'',
	templateUrl : './stockTakeCycle.component.html'
})
export class StockTakeCycleComponent implements OnInit {
	
	constructor(public router: Router, public stockTakeCycleService : StockTakeCycleService, public localStorageService : LocalStorageService){}
	
	public stockTakeCycleList : any;
	public stockTakeStoreList : any;
	listOfCycle :any= [];
	deleted : boolean = false;
	restored : boolean = false;
	public stockTakeCycleId : any;
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
		    $('#stockTakeCycleTable').DataTable({
		    	"columnDefs": [{
		            "orderable": false,
		            "targets": [3]
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
		
		this.stockTakeCycleService.getAllActiveStockTakeCycle().subscribe((results)  => {
			
//			this.stockTakeCycleList = results.data;
//			
//			var table = $('#stockTakeCycleTable').DataTable();
//			table.destroy();
//			this.initializeDatatable();
			console.log(results);
			this.stockTakeCycleList = results.data.cycle;
			this.stockTakeStoreList = results.data.store;
			console.log(this.stockTakeStoreList);
			
			this.listOfCycle = [];
			for( let indexOfCycle = 0; indexOfCycle < results.data.cycle.length; indexOfCycle++ ) {
				this.listOfCycle.push(results.data.cycle[indexOfCycle]);
				this.listOfCycle[indexOfCycle].storesObjects = results.data.store[indexOfCycle];
			}
			
			console.log(this.listOfCycle);
			
			var table = $('#stockTakeCycleTable').DataTable();
			table.destroy();
			this.initializeDatatable();
			
			
		},(error) => {
			
		});
	}
	
	showDeletedRecord() {
		
		this.showLoader = true;
		
		this.showDeleteRecord = !this.showDeleteRecord;
		
		if(this.showDeleteRecord){
			this.stockTakeCycleService.getAllDeletedStockTakeCycle().subscribe((results)  => {
				var table = $('#stockTakeCycleTable').DataTable();
				table.destroy();
				this.initializeDatatable();
//				console.log(results);
//				this.stockTakeCycleList = results.data;
//				console.log(this.stockTakeCycleList);
				console.log(results);
				if(results.data !== null) {
					this.stockTakeCycleList = results.data.cycle;
					this.stockTakeStoreList = results.data.store;
					console.log(this.stockTakeStoreList);
					
					this.listOfCycle = [];
					for( let indexOfCycle = 0; indexOfCycle < results.data.cycle.length; indexOfCycle++ ) {
						this.listOfCycle.push(results.data.cycle[indexOfCycle]);
						this.listOfCycle[indexOfCycle].storesObjects = results.data.store[indexOfCycle];
					}
				}else {
					this.listOfCycle =[];
				}
				
				
				console.log(this.listOfCycle);
			},(error) => {
				
			});
		}else{
			this.loadAllActivedata();
		}
	}
	
	addNewStockTakeCycle() {
		
		this.router.navigateByUrl('/' + ROUTS.ADD_STOCK_TAKE_CYCLE);
	}
	
	deleteStockTakeCycle() {
		this.stockTakeCycleService.deleteStockTakeCycle(this.stockTakeCycleId).subscribe((results) => {
			this.deleted = true;
			setTimeout(() => {
				window.location.reload();	
			},800);
		}, (error) => {
			
		});
	}
	
	getId(id) {
		this.stockTakeCycleId = id;
	}
	
	restoreStockTakeCycle(stockTakeCycleId) {
		this.stockTakeCycleService.restoreStockTakeCycle(stockTakeCycleId).subscribe((results) => {
			this.restored = true;
			setTimeout(() => {
				window.location.reload();	
			},800);
		}, (error) => {
			
		});
	}
}