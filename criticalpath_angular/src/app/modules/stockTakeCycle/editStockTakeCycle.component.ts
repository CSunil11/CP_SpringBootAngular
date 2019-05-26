import {Component, OnInit } from '@angular/core';
import { RouterModule, Routes, Router, ActivatedRoute } from '@angular/router';
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
	templateUrl : './editStockTakeCycle.component.html'
})
export class EditStockTakeCycleComponent implements OnInit {
	
	constructor(public router: Router, public route: ActivatedRoute, public stockTakeCycleService : StockTakeCycleService,
				public storeService : StoreService,
				public brandService : BrandService,
				public commonService : CommonService, public daysOfWeekService : DaysOfWeekService, 
				public localStorageService : LocalStorageService){}
	
	CONSTANTS : any = CONSTANTS;
	buttonText : string;
	editStockTakeCycleForm: FormGroup;
	error : string;
	stockTakeCycleEdited : boolean = false;
	requiredFlag : boolean = false;
	storeList : any[] = [];
	stockTakeCycleId : any ;
	private sub : any;
	daysList : any[]= [];
	brandList : any[] = [];
	selectedBrandList : any = [];
	doNotCountList : any[] = [];
			
	ngOnInit() {
		
		this.commonService.validateUserByRole();
		
		this.buttonText = CONSTANTS.SUBMIT;
		
		this.sub = this.route.params.subscribe(params => {
            
            this.stockTakeCycleId  = params['id'];
        });
		
		this.editStockTakeCycleForm = new FormGroup({
			name: new FormControl('', [Validators.required, NoWhitespaceValidator]),
			length: new FormControl('', [Validators.required]),
			doNotCount: new FormControl('', []),
			brand: new FormControl('', [Validators.required]),
			stores: new FormControl('', [Validators.required]),
			isActive: new FormControl(true, []),
			id: new FormControl('', [Validators.required]),
		});
		
		this.daysOfWeekService.getAllDaysOfWeek().subscribe((results) => {
//			if(results != undefined)
//				this.daysList = results.data;
			if(results != undefined)
				/*for(let i =0 ; i<results.data.length;i++ ) {
					this.daysList.push(results.data[i]);
				}*/
				for(let i =0; i< results.data.length;i++) {
					this.daysList.push({
				        id: results.data[i].name,
				        name: results.data[i].name
				      });
				}
		}, (error) => {
			
		});
		
		this.brandService.getAllActiveBrand().subscribe((results) => {
			this.brandList = results.data;
		}, (error) => {
			
		});
		
		
		this.stockTakeCycleService.getStockTakeCycleData(this.stockTakeCycleId).subscribe((results) => {
			console.log('donotcount')
			console.log(results)
			if(results !== undefined) {
			
				let doNotCountDay : any;
			if(results.data.cycle.doNotCount !== null) {
				doNotCountDay = results.data.cycle.doNotCount.split(", ");
				for(let i =0; i< doNotCountDay.length;i++) {
					this.doNotCountList.push({
				        id: doNotCountDay[i],
				        name: doNotCountDay[i]
				      });
				}
			}
					
			}
			console.log(this.doNotCountList);
			this.editStockTakeCycleForm.controls['name'].setValue(results.data.cycle.name);
			this.editStockTakeCycleForm.controls['length'].setValue(results.data.cycle.length);
			this.editStockTakeCycleForm.controls['doNotCount'].setValue(this.doNotCountList);
			this.editStockTakeCycleForm.controls['isActive'].setValue(results.data.cycle.isActive);
			this.editStockTakeCycleForm.controls['id'].setValue(results.data.cycle.id);
			
			let storesLists: any = results.data.stores;
			storesLists.forEach((store) => {
				
				let isNeedToAdd: boolean = true;
				let selectedBrand: any = this.brandList.filter((brand)=> brand.id == store.brand.id);		
				
				if(selectedBrand.length > 0) {
					
					for(let i = 0; i < this.selectedBrandList.length; i++) {
						
						if(selectedBrand[0].id == this.selectedBrandList[i].id) {
							isNeedToAdd = false;
						}
					}
					
					if(isNeedToAdd)
						this.selectedBrandList.push(selectedBrand[0]);
				}
			});
			
			this.editStockTakeCycleForm.controls['brand'].setValue(this.selectedBrandList);
			let storeLists: any = results.data.stores;
			
			this.selectedBrandList.forEach((brand) => {
				this.getStoreByBrand(brand);
			});
			
			this.editStockTakeCycleForm.controls['stores'].setValue(storeLists);
		}, (error) => {
			
		});
		
		console.log(this.editStockTakeCycleForm);
					
		setTimeout(() => {
			this.removeAuthorities();
			},700);
	}
	
	removeAuthorities() {

		let cycleObj = this.editStockTakeCycleForm.controls['stores'].value;
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
		this.editStockTakeCycleForm.controls['stores'].setValue(storereset);
	}
	
	addStore(){
		this.removeAuthorities();
	}
	
	getStoreByBrand(event : any) {
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
		
		let selectedStores: any = this.editStockTakeCycleForm.controls['stores'].value;
		for(var i = 0 ; i < selectedStores.length ; i++) {
			if(selectedStores[i].brand.id == event.id) {
				selectedStores.splice(i, 1);
				i = i-1;
			}
		}
		this.editStockTakeCycleForm.controls['stores'].setValue(selectedStores);
		
		//Splice the brand from array
		var index = this.selectedBrandList.findIndex((brand) => brand.id == event.id);
		this.selectedBrandList.splice(index, 1);
	}
	
	removeStore(event : any) {	
		
		let selectedStores: any = this.editStockTakeCycleForm.controls['stores'].value;
		let isNeedToRemoveBrand = false;
		
		for(var i = 0 ; i < selectedStores.length ; i++) {
			
			if(selectedStores[i].brand.id == event.brand.id) {
				isNeedToRemoveBrand = true;
				break;
			}	
		}
	
		let selectedBrands: any = this.editStockTakeCycleForm.controls['brand'].value;	
		if(!isNeedToRemoveBrand) {
			var brandIndex = selectedBrands.findIndex((brand) => brand.id == event.brand.id);
			selectedBrands.splice(brandIndex, 1);
			this.editStockTakeCycleForm.controls['brand'].setValue(selectedBrands);
			
			for(var i = 0 ; i < this.storeList.length ; i++) {
				if(this.storeList[i].brand.id == event.brand.id) {
					this.storeList.splice(i, 1);
					i = i-1;
				}
			}
		}
	}
	
	selectCheckBox(e){
		if(e.target.checked === true) {
			this.editStockTakeCycleForm.controls['stores'].setValue(this.storeList);

		}else {
			this.editStockTakeCycleForm.controls['stores'].setValue("");
		}
	}
	
	submitStockTakeCycleForm(stockTakeCycleForm : any) {
    	
		this.error = '';
		
		let cycleObj = this.editStockTakeCycleForm.controls['stores'].value;
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
		this.editStockTakeCycleForm.controls['stores'].setValue(storereset);
		
		if(this.editStockTakeCycleForm.valid) {
			this.buttonText = CONSTANTS.SUBMITTING;
			let storelist = [];
			storelist = this.editStockTakeCycleForm.controls['stores'].value;
			console.log(storelist);
			let storeIdList = [];
			let strTemp : any ;
			for(let i =0; i< storelist.length;i++) {
				if(i==0)
					strTemp = storelist[i].id;
				else
					strTemp = strTemp + '/' + storelist[i].id;
			}
			
			let doNotCount = [];
			doNotCount = this.editStockTakeCycleForm.controls['doNotCount'].value;
			console.log(doNotCount);
//			let doNotCountNameList = [];
			let strDoNotTemp : any ;
			for(let i =0; i< doNotCount.length;i++) {
				if(i==0)
					strDoNotTemp = doNotCount[i].name;
				else
					strDoNotTemp = strDoNotTemp + ', ' + doNotCount[i].name;
			}

			let stockTakeCycleJson = {
					'id':stockTakeCycleForm.id,
					'name':this.editStockTakeCycleForm.controls['name'].value,
					'length':this.editStockTakeCycleForm.controls['length'].value,
					'donotcount':strDoNotTemp,
					'store':strTemp,
					'status':this.editStockTakeCycleForm.controls['isActive'].value
				}

	        this.stockTakeCycleService.addStockTakeCycle(stockTakeCycleJson).subscribe((results) => {
	        		this.stockTakeCycleEdited = true;
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
		this.editStockTakeCycleForm.controls['isActive'].setValue (stockTakeCycle);
	}
}