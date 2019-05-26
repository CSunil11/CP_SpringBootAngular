import {Component, OnInit } from '@angular/core';
import { RouterModule, Routes, Router } from '@angular/router';
import { ReactiveFormsModule, FormsModule, FormGroup, FormControl, Validators, FormBuilder } from '@angular/forms';
import { NoWhitespaceValidator } from '../../common/customValidator/custom.validator';
import * as ROUTS from '../../constants/routs';
import { CONSTANTS } from '../../constants/constant';

import { StockTakeCycleService } from '../../service/stockTakeCycle.service';
import { DaysOfWeekService } from '../../service/daysOfWeek.service';
import { BrandService } from'../../service/brand.service';
import{ StoreService }from'../../service/store.service';
import { CommonService } from '../../service/common.service';
import { LocalStorageService } from '../../service/localStorage.service';
import { UiSwitchModule } from 'ngx-toggle-switch';


@Component({
	selector :'',
	templateUrl : './addStockTakeCycle.component.html'
})
export class AddStockTakeCycleComponent implements OnInit {
	
	constructor(public router: Router, public stockTakeCycleService : StockTakeCycleService,
				public storeService : StoreService,
				public brandService : BrandService,
				public commonService : CommonService,
				public localStorageService : LocalStorageService, public daysOfWeekService : DaysOfWeekService){}
	
	CONSTANTS : any = CONSTANTS;
	buttonText : string;
				
	addStockTakeCycleForm: FormGroup;
	error : string;
	stockTakeCycleAdded : boolean = false;
	requiredFlag : boolean = false;
	storeList : any[] = [];
	daysList : any[] = [];
	brandList : any[] = [];
	selectedBrandList : any = [];
				
	ngOnInit() {
		
		this.commonService.validateUserByRole();
		
		this.buttonText = CONSTANTS.SUBMIT;
		
		this.addStockTakeCycleForm = new FormGroup({
			name: new FormControl('', [Validators.required, NoWhitespaceValidator]),
			length: new FormControl('', [Validators.required]),
			doNotCount: new FormControl('', []),
			brand: new FormControl('', [Validators.required]),
			stores: new FormControl('', [Validators.required]),
			isActive: new FormControl(true, [])
		});
		
//		this.storeService.getAllActiveStore().subscribe((results) => {
//			this.storeList = results.data;
//		}, (error) => {
//			
//		});
		
		this.daysOfWeekService.getAllDaysOfWeek().subscribe((results) => {
			if(results != undefined)
				for(let i =0 ; i<results.data.length;i++ ) {
					this.daysList.push(results.data[i].name);
				}
//				this.daysList = results.data;
		}, (error) => {
			
		});
		
		this.brandService.getAllActiveBrand().subscribe((results) => {
			this.brandList = results.data;
		}, (error) => {
			
		});
		
		/*setTimeout(() => {
			this.removeAuthorities();
			},700);*/
	}
	
	removeAuthorities() {
		
		let cycleObj = this.addStockTakeCycleForm.controls['stores'].value;
		let storereset =[];
		for(let i=0;i<cycleObj.length;i++){
			if(cycleObj[i].locationDivision !== null) {
				cycleObj[i].locationDivision.divisionalSalesManagers = null;
				
			}
			if(cycleObj[i].regionalManagers !== null) {
				if(cycleObj[i].regionalManagers.userLogin !== null) {
					cycleObj[i].regionalManagers.userLogin = null
				}
			}
				
			storereset.push(cycleObj[i]);			
			
		}

		this.addStockTakeCycleForm.controls['stores'].setValue(storereset);
	}
	
	addStore(){
		this.removeAuthorities();
	}
	
	getStoreByBrand(event : any) {
		this.selectedBrandList.push(event);
		this.storeList = JSON.parse(JSON.stringify(this.storeList));
		this.storeService.getStoreByBrandId(event.id).subscribe((results) => {
			if(results && results.data.length > 0) {
				results.data.forEach((store) => {
					this.storeList.push(store);
				});
			}
		}, (error) => {
			
		});
	}
	
	removeBrand(event : any) {
		
		//Splice the store from array
		for(var i = 0 ; i < this.storeList.length ; i++) {
			if(this.storeList[i].brand.id == event.id) {
				this.storeList.splice(i, 1);
				i = i-1;
			}
		}
		
		let selectedStores: any = this.addStockTakeCycleForm.controls['stores'].value;
		for(var i = 0 ; i < selectedStores.length ; i++) {
			if(selectedStores[i].brand.id == event.id) {
				selectedStores.splice(i, 1);
				i = i-1;
			}
		}
		
//		this.setFormControl(selectedStores,'stores');
		this.addStockTakeCycleForm.controls['stores'].setValue(selectedStores);
		//Splice the brand from array
		var index = this.selectedBrandList.findIndex((brand) => brand.id == event.id);
		this.selectedBrandList.splice(index, 1);
	}
	
	removeStore(event : any) {
		
		let selectedStores: any = this.addStockTakeCycleForm.controls['stores'].value;
		let isNeedToRemoveBrand = false;
		
		for(var i = 0 ; i < selectedStores.length ; i++) {
			
			if(selectedStores[i].brand.id == event.brand.id) {
				isNeedToRemoveBrand = true;
				break;
			}	
		}

		let selectedBrands: any = this.addStockTakeCycleForm.controls['brand'].value;	
		if(!isNeedToRemoveBrand) {
			var brandIndex = selectedBrands.findIndex((brand) => brand.id == event.brand.id);
			selectedBrands.splice(brandIndex, 1);
			this.addStockTakeCycleForm.controls['brand'].setValue(selectedBrands);
		}
	}
	
	selectCheckBox(e){
		if(e.target.checked === true) {
			this.addStockTakeCycleForm.controls['stores'].setValue(this.storeList);

		}else {
			this.addStockTakeCycleForm.controls['stores'].setValue("");
		}
	}
	
	submitStockTakeCycleForm(stockTakeCycleForm : any) {
    	
		this.error = '';
		
		let cycleObj = this.addStockTakeCycleForm.controls['stores'].value;
		let storereset =[];
		for(let i=0;i<cycleObj.length;i++){
			if(cycleObj[i].locationDivision !== null) {
				cycleObj[i].locationDivision.divisionalSalesManagers = null;
				
			}
			if(cycleObj[i].regionalManagers !== null) {
				if(cycleObj[i].regionalManagers.userLogin !== null) {
					cycleObj[i].regionalManagers.userLogin = null
				}
			}
			storereset.push(cycleObj[i]);			
			
		}

		this.addStockTakeCycleForm.controls['stores'].setValue(storereset);
		
		console.log(stockTakeCycleForm);
		if(this.addStockTakeCycleForm.valid) {
			this.buttonText = CONSTANTS.SUBMITTING;
			let storelist = [];
			storelist = this.addStockTakeCycleForm.controls['stores'].value;
			let storeIdList = [];
			let strTemp : any ;
			for(let i =0; i< storelist.length;i++) {
				if(i==0)
					strTemp = storelist[i].id;
				else
					strTemp = strTemp + '/' + storelist[i].id;
			}
			
			let doNotCountList = [];
			doNotCountList = this.addStockTakeCycleForm.controls['doNotCount'].value;
			let doNotCountNameList = [];
			let strDoNotTemp : any ;
			for(let i =0; i< doNotCountList.length;i++) {
				if(i==0)
					strDoNotTemp = doNotCountList[i].name;
				else
					strDoNotTemp = strDoNotTemp + ', ' + doNotCountList[i].name;
			}
			console.log(storeIdList);
			let stockTakeCycleJson = {
					'name':this.addStockTakeCycleForm.controls['name'].value,
					'length':this.addStockTakeCycleForm.controls['length'].value,
					'donotcount':strDoNotTemp,
					'store':strTemp,
					'status':this.addStockTakeCycleForm.controls['isActive'].value
				}
			console.log(stockTakeCycleJson);
	        this.stockTakeCycleService.addStockTakeCycle(stockTakeCycleJson).subscribe((results) => {
	        		this.stockTakeCycleAdded = true;
	        		setTimeout(() => {
	        			this.router.navigateByUrl('/' + ROUTS.STOCK_TAKE_CYCLE);
	            	}, 2500);
	        		
	        }, (error) => {
	        		this.error = JSON.parse(error._body).message;
	        		this.buttonText = CONSTANTS.SUBMIT;
	        });
		} else
			this.requiredFlag = true;
    }
	
	back() {
		this.router.navigateByUrl('/' + ROUTS.STOCK_TAKE_CYCLE);
	}
	
	changeStatus(stockTakeCycle) {
		this.addStockTakeCycleForm.controls['isActive'].setValue (stockTakeCycle);
	}
}