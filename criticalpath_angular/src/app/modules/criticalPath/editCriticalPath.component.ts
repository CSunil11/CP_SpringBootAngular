import {Component, OnInit } from '@angular/core';
import { RouterModule, Routes, Router, ActivatedRoute } from '@angular/router';
import { ReactiveFormsModule, FormsModule, FormGroup, FormControl, Validators, FormBuilder } from '@angular/forms';
import { NoWhitespaceValidator } from '../../common/customValidator/custom.validator';
import * as ROUTS from '../../constants/routs';
import { CONSTANTS } from '../../constants/constant';

import { CriticalPathService } from '../../service/criticalPath.service';
import { BrandService } from'../../service/brand.service';
import { StockTakeCycleService } from '../../service/stockTakeCycle.service';
import { StoreService } from '../../service/store.service';
import { TaskStatusService } from '../../service/taskStatus.service';
import { CommonService } from '../../service/common.service';
import { LocalStorageService } from '../../service/localStorage.service';
import { UiSwitchModule } from 'ngx-toggle-switch';


@Component({
	selector :'',
	templateUrl : './editCriticalPath.component.html'
})
export class EditCriticalPathComponent implements OnInit {
	
	constructor(public router: Router, public criticalPathService : CriticalPathService, public route: ActivatedRoute,
				public storeService : StoreService, public taskStatusService : TaskStatusService,
				public commonService : CommonService, public stockTakeCycleService : StockTakeCycleService,
				public localStorageService : LocalStorageService, public brandService : BrandService){}
	
	CONSTANTS : any = CONSTANTS;
	buttonText : string;
				
	editCriticalPathForm: FormGroup;
	error : string;
	criticalPathEdited : boolean = false;
	requiredFlag : boolean = false;
	stockTakeCycleList : any[] = [];			
	storeList : any[] = [];
	brandList : any[] = [];
	statusList : any[] = [];
	selectedBrandList : any = [];
	criticalPathList : any[] = [];
	stockTakeStoreList : any[] =[];
	listOfCycle : any[] =[];
	private sub : any;
	criticalPathId : any;
	cycleId : number;
	statusId : number;
	cycleLength : number;
	descriptionLength : number;		
	maxLength : number;		
	maxStartBy : number;
	startByValidFlag : boolean = false;
	taskLengthValidFlag : boolean = false;
	public showLoader : boolean = false;
	public showMainLoader : boolean = false;								
				
	ngOnInit() {
		
		this.showLoader =true;
		this.showMainLoader = true;
		this.commonService.validateUserByRole();
		
		this.buttonText = CONSTANTS.SUBMIT;
		
		this.sub = this.route.params.subscribe(params => {
            
            this.criticalPathId  = params['id'];
        });
		
		this.editCriticalPathForm = new FormGroup({
			cycle: new FormControl('', [Validators.required]),
			title: new FormControl('', [Validators.required]),
			description: new FormControl('', []),
			length: new FormControl('', [Validators.min(1),Validators.required]),
			startDay: new FormControl('', [Validators.min(1),Validators.required]),
			status: new FormControl('', [Validators.required]),
			stores: new FormControl('', [Validators.required]),
			id: new FormControl('', [Validators.required]),
			isActive: new FormControl(true, [])
		});
		
		this.taskStatusService.getAllActiveTaskStatus().subscribe((results) => {
			this.statusList = results.data;
		}, (error) => {
			
		});
		
		let stockTakeCycleList =[];
		let stockTakeStoreList =[];
		this.stockTakeCycleService.getAllActiveStockTakeCycle().subscribe((results) => {
			
			stockTakeCycleList = results.data.cycle;
			stockTakeStoreList = results.data.store;
			this.listOfCycle = [];
			let j=0;
			
			for(let i=0;i<stockTakeStoreList.length;i++) {
				if(stockTakeStoreList[i].length > 0) {
					this.listOfCycle.push(stockTakeCycleList[i]);
					this.listOfCycle[j].storesObjects = stockTakeStoreList[i];
					j++;
				}
			}
			
		}, (error) => {
		});
		
		this.criticalPathService.getCriticalPathData(this.criticalPathId).subscribe((results) => {
			console.log(results);
			this.cycleId = results.data.criticalPath.cycle.id;
			this.statusId = results.data.criticalPath.status.id;
			this.storeList = [];
			
			
			let store = results.data.storeOfCycle;
			let storereset =[];
			
			for(let i=0;i<store.length;i++){
				if(store[i].locationDivision !== null) {
					store[i].locationDivision = null;
				}
				this.storeList.push(store[i]);
			}
			
			//this.storeList = results.data.cycle.stores;
			this.cycleLength = parseInt(results.data.criticalPath.cycle.length);
			
			console.log("1: "+this.storeList);

			this.editCriticalPathForm.controls['title'].setValue(results.data.criticalPath.title);
			this.editCriticalPathForm.controls['description'].setValue(results.data.criticalPath.description);
			this.editCriticalPathForm.controls['length'].setValue(results.data.criticalPath.length);
			this.editCriticalPathForm.controls['startDay'].setValue(results.data.criticalPath.startDay);
			this.editCriticalPathForm.controls['length'].setValue(results.data.criticalPath.length);
			this.editCriticalPathForm.controls['isActive'].setValue(results.data.criticalPath.isActive);
			this.editCriticalPathForm.controls['id'].setValue(results.data.criticalPath.id);
			
//			this.editCriticalPathForm.controls["startDay"].setValidators([]);
//			this.editCriticalPathForm.controls['startDay'].updateValueAndValidity();
//			this.editCriticalPathForm.controls["length"].setValidators([]);
//			this.editCriticalPathForm.controls['length'].updateValueAndValidity();
			
			this.descriptionLength = results.data.criticalPath.description.length;
			
			setTimeout(() => {
				for( let cycleIndex=0; cycleIndex<this.listOfCycle.length; cycleIndex++) {
					if( this.listOfCycle[cycleIndex].id == results.data.criticalPath.cycle.id ) {
						this.editCriticalPathForm.controls['cycle'].setValue(this.listOfCycle[cycleIndex]);
						break;
					}
				}
				
				for( let statusIndex=0; statusIndex<this.statusList.length; statusIndex++) {
					if( this.statusList[statusIndex].id == results.data.criticalPath.status.id ) {
						this.editCriticalPathForm.controls['status'].setValue(this.statusList[statusIndex]);
						break;
					}
				}
				this.showLoader =false;
			},500);
			
			let storeLists: any = results.data.criticalPath.stores;
			this.editCriticalPathForm.controls['stores'].setValue(storeLists);
			this.showMainLoader = false;	
		}, (error) => {
			
		});
		
		setTimeout(() => {
		this.removeAuthorities();
		},1000);
		
	}
	
	removeAuthorities() {

		let cycleObj = this.editCriticalPathForm.controls['cycle'].value;
		let cycleStoreObj = this.storeList;
		this.storeList = [];
		
			let store = cycleStoreObj;
			let storereset =[];
			for(let i=0;i<store.length;i++){
			if(store[i].locationDivision != null) {
				store[i].locationDivision = null;
			}
			
			if(store[i].stockTakeCycle.createdBy != null) {
				if(store[i].stockTakeCycle.createdBy.userLogin != null) {
					if(store[i].stockTakeCycle.createdBy.userLogin.authorities != null) {
							store[i].stockTakeCycle.createdBy.userLogin.authorities = null;
					}
				}
			}
			this.storeList.push(store[i]);
		}
		
		if(cycleObj !== '') {
			let store = cycleObj.storesObjects;
			let storereset =[];
			for(let i=0;i<store.length;i++){
			if(store[i].locationDivision != null) {
				store[i].locationDivision = null;
				storereset.push(store[i]);		
			}
		}
			this.editCriticalPathForm.controls['cycle'].setValue(storereset);		

		if(cycleObj.createdBy != null)
		{
			if(cycleObj.createdBy.userLogin != null)
			{
				if(cycleObj.createdBy.userLogin.authorities != null)
				{
					cycleObj.createdBy.userLogin.authorities = null;
				}
			}
		}

			this.editCriticalPathForm.controls['cycle'].setValue(cycleObj);
		}
		
		let storeobj = this.editCriticalPathForm.controls['stores'].value;
		let storeresetobj =[];

		if(storeobj !== null) {
			for(let ii=0;ii<storeobj.length;ii++){
				if(storeobj[ii].locationDivision !== null) {
					storeobj[ii].locationDivision = null;
					
				}
				if(storeobj[ii].regionalManagers !== null) {
					if(storeobj[ii].regionalManagers.userLogin !== null) {
						storeobj[ii].regionalManagers.userLogin = null ;
					
					}
				}
				storeresetobj.push(storeobj[ii]);
			}

			this.editCriticalPathForm.controls['stores'].setValue(storeresetobj);
		}

	}
	
	countLength(){
		let descriptionvalue = this.editCriticalPathForm.controls['description'].value;
		
		this.descriptionLength = descriptionvalue.length;
		
	}
	
	checkLength(){
		
		let lengthVal = this.editCriticalPathForm.controls['length'].value;
		let startVal = this.editCriticalPathForm.controls['startDay'].value;
		
		if(startVal > 0) {
			if(lengthVal <= startVal && lengthVal <= this.cycleLength)
			{
				this.startByValidFlag = false;
				this.taskLengthValidFlag = false;
			}
			else
			{
				this.taskLengthValidFlag = true;
				this.startByValidFlag = false;
			}
		}
		else {
			if(lengthVal <= this.cycleLength)
			{
				this.startByValidFlag = false;
				this.taskLengthValidFlag = false;
			}
			else
			{
				this.taskLengthValidFlag = true;
				this.startByValidFlag = false;
			}
		}
				
	}
	
	checkStartBy(){
		
		let startVal = this.editCriticalPathForm.controls['startDay'].value;
		let lengthVal = this.editCriticalPathForm.controls['length'].value;

		if(lengthVal > 0 && startVal !== null) {
			if(startVal >= lengthVal && startVal <= this.cycleLength){
				this.startByValidFlag = false;
				this.taskLengthValidFlag = false;
			}
			else{
				this.startByValidFlag = true;
				this.taskLengthValidFlag = false;
			}
		}else {
			if(startVal <= this.cycleLength)
			{
				this.startByValidFlag = false;
				this.taskLengthValidFlag = false;
			}
			else
			{
				this.startByValidFlag = true;
				this.taskLengthValidFlag = false;
			}
		}
		
	}
	
	clearCreatedBy() {
		this.editCriticalPathForm.controls['cycle'].value.createdBy = null;
	}
	
	onSelectCycle(){
		this.showLoader =true;
		this.showMainLoader = true;
		this.removeAuthorities();
		let cycle = this.editCriticalPathForm.controls['cycle'].value;
		this.editCriticalPathForm.controls['stores'].setValue([]);
		this.storeList = cycle.storesObjects;
		this.editCriticalPathForm.controls['stores'].setValue(this.storeList);
		this.showLoader =false;
		this.showMainLoader = false;
		this.cycleLength = cycle.length;
		
		this.editCriticalPathForm.controls["startDay"].setValidators([Validators.required, Validators.max(this.cycleLength)]);
		this.editCriticalPathForm.controls['startDay'].updateValueAndValidity();
		this.editCriticalPathForm.controls["length"].setValidators([Validators.required, Validators.max(this.cycleLength)]);
		this.editCriticalPathForm.controls['length'].updateValueAndValidity();
		
		this.taskLengthValidFlag = false;
		this.startByValidFlag = false;
	}
	
	selectCheckBox(e){
		this.showLoader =true;
		this.editCriticalPathForm.controls['stores'].setValue([]);
		let cycle = this.editCriticalPathForm.controls['cycle'].value;
		this.storeList =[];
		this.storeList = cycle.storesObjects;
		if(e.target.checked === true) {
			
			this.editCriticalPathForm.controls['stores'].setValue(this.storeList);
			this.showLoader =false;
		}else {
			this.editCriticalPathForm.controls['stores'].setValue("");
			this.showLoader =false;
		}
	}
	
	submitCriticalPathForm(criticalPathForm : any) {

		try{
	    	for(let ii=0;ii<criticalPathForm.cycle.storesObjects.length;ii++){
				if(criticalPathForm.cycle.storesObjects[ii].locationDivision !== null) {
					criticalPathForm.cycle.storesObjects[ii].locationDivision = null;
				}
				if(criticalPathForm.cycle.storesObjects[ii].regionalManagers !== null) {
					criticalPathForm.cycle.storesObjects[ii].regionalManagers = null;
				}
			}
		} catch (error) {

		}
    	
    	try {
	    	for(let ii=0;ii<criticalPathForm.stores.length;ii++){
				if(criticalPathForm.stores[ii].stockTakeCycle !== null) {
					if(criticalPathForm.stores[ii].stockTakeCycle.createdBy !== null) {
						if(criticalPathForm.stores[ii].stockTakeCycle.createdBy.userLogin !== null) {
								criticalPathForm.stores[ii].stockTakeCycle.createdBy.userLogin = null;
						}
					}
				}
				
				if(criticalPathForm.stores[ii].locationDivision !== null) {
					criticalPathForm.stores[ii].locationDivision = null;
				}
				if(criticalPathForm.stores[ii].regionalManagers !== null) {
					criticalPathForm.stores[ii].regionalManagers = null;
				}
			}
		} catch (error) {
			
		}
    	
//    	this.removeAuthorities();
    	
		this.error = '';

		if(this.editCriticalPathForm.valid) {
			this.buttonText = CONSTANTS.SUBMITTING;
	        this.criticalPathService.addCriticalPath(criticalPathForm).subscribe((results) => {
	        		this.criticalPathEdited = true;
	        		setTimeout(() => {
	        			this.router.navigateByUrl('/' + ROUTS.CRITICAL_PATH);
	            	}, 2500);
	        		
	        }, (error) => {
	        		this.error = JSON.parse(error._body).message;
	        		this.buttonText = CONSTANTS.SUBMIT;
	        });
		} else
			this.requiredFlag = true;

    }
	
	back() {
		this.router.navigateByUrl('/' + ROUTS.CRITICAL_PATH);
	}
	
	changeStatus(criticalPath) {
		this.editCriticalPathForm.controls['isActive'].setValue (criticalPath);
	}
}