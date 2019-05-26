import {Component, OnInit } from '@angular/core';
import { RouterModule, Routes, Router } from '@angular/router';
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
	templateUrl : './addCriticalPath.component.html'
})
export class AddCriticalPathComponent implements OnInit {
	
	constructor(public router: Router, public criticalPathService : CriticalPathService,
			    public storeService : StoreService, public taskStatusService : TaskStatusService,
				public commonService : CommonService, public stockTakeCycleService : StockTakeCycleService,
				public localStorageService : LocalStorageService, public brandService : BrandService){}
	
	CONSTANTS : any = CONSTANTS;
	buttonText : string;
				
	addCriticalPathForm: FormGroup;
	error : string;
	criticalPathAdded : boolean = false;
	requiredFlag : boolean = false;
	stockTakeCycleList : any[] = [];			
	storeList : any[] = [];
	statusList : any[] = [];
	stockTakeStoreList : any[] =[];
	listOfCycle : any[] =[];
	brandList : any[] = [];
	selectedBrandList : any = [];
	cycleLength : number;	
	descriptionLength : number;		
	maxLength : number;	
	maxStartBy : number;
	startByValidFlag : boolean = false;
	taskLengthValidFlag : boolean = false;
	public showLoader : boolean = false;
	public showMainLoader : boolean = false;
				
	ngOnInit() {

			this.showMainLoader =true;
		this.commonService.validateUserByRole();

		this.buttonText = CONSTANTS.SUBMIT;
		
		let temp = this.cycleLength
		this.addCriticalPathForm = new FormGroup({
			cycle: new FormControl('', [Validators.required]),
			title: new FormControl('', [Validators.required]),
			description: new FormControl('', []),
			length: new FormControl('', [Validators.min(1),Validators.required]),
			startDay: new FormControl('', [Validators.min(1),Validators.required]),
			status: new FormControl('', [Validators.required]),
//			brand: new FormControl('', [Validators.required]),
			stores: new FormControl('', [Validators.required]),
			isActive: new FormControl(true, [])
		});
		
		this.stockTakeCycleService.getAllActiveStockTakeCycle().subscribe((results) => {
				this.stockTakeCycleList = results.data.cycle;
				this.stockTakeStoreList = results.data.store;
				this.listOfCycle = [];
				
				let j=0;
				for(let i=0;i<this.stockTakeStoreList.length;i++) {
					if(this.stockTakeStoreList[i].length > 0) {
						this.listOfCycle.push(this.stockTakeCycleList[i]);
						this.listOfCycle[j].storesObjects = this.stockTakeStoreList[i];
						j++;
					}
				}
				this.showMainLoader = false;
			}, (error) => {
				
		});
		
		this.taskStatusService.getAllActiveTaskStatus().subscribe((results) => {
			this.statusList = results.data;
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
		
	clearCreatedBy() {
		this.addCriticalPathForm.controls['cycle'].value.createdBy = null;
	}
	
	onSelectCycle(){
		this.showLoader = true;
		this.showMainLoader = true;
		this.removeAuthorities();	
		let cycle = this.addCriticalPathForm.controls['cycle'].value;
		this.addCriticalPathForm.controls['stores'].setValue([]);
		this.storeList = cycle.storesObjects;		
		this.addCriticalPathForm.controls['stores'].setValue(this.storeList);
		this.showMainLoader = false;
		this.showLoader = false;
		this.cycleLength = parseInt(cycle.length);
		
		this.addCriticalPathForm.controls["startDay"].setValidators([Validators.required, Validators.max(this.cycleLength)]);
		this.addCriticalPathForm.controls['startDay'].updateValueAndValidity();
		this.addCriticalPathForm.controls["length"].setValidators([Validators.required, Validators.max(this.cycleLength)]);
		this.addCriticalPathForm.controls['length'].updateValueAndValidity();
		
		this.taskLengthValidFlag = false;
		this.startByValidFlag = false;

	}
	
	selectCheckBox(e){
		this.showLoader = true;
		this.addCriticalPathForm.controls['stores'].setValue("");
		let cycle = this.addCriticalPathForm.controls['cycle'].value;
		this.storeList =[];
		this.storeList = cycle.storesObjects;	
		if(e.target.checked === true) {
			
			this.addCriticalPathForm.controls['stores'].setValue(this.storeList);
			this.showLoader = false;

		}else {
			this.addCriticalPathForm.controls['stores'].setValue("");
			this.showLoader = false;
		}
	}
	
	countLength(){
		let descriptionvalue = this.addCriticalPathForm.controls['description'].value;
		
		this.descriptionLength = descriptionvalue.length;
		
	}
	
	checkLength(){
		
		let lengthVal = this.addCriticalPathForm.controls['length'].value;
		let startVal = this.addCriticalPathForm.controls['startDay'].value;

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
		
		
//		if(this.maxLength <= (this.cycleLength+1))
//		{
//			this.startByValidFlag = false;
//			this.taskLengthValidFlag = false;
//		}
//		else
//		{
//			this.taskLengthValidFlag = true;
//			this.startByValidFlag = false;
//		}
		
		
	}
	
	checkStartBy(){
		
		let startVal = this.addCriticalPathForm.controls['startDay'].value;
		let lengthVal = this.addCriticalPathForm.controls['length'].value;

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
//		if(this.maxLength <= (this.cycleLength + 1))
//		{
//			this.startByValidFlag = false;
//			this.taskLengthValidFlag = false;
//		}
//		else
//		{
//			this.startByValidFlag = true;
//			this.taskLengthValidFlag = false;
//		}
		
	}
	
	removeAuthorities() {
		
		let cycleObj = this.addCriticalPathForm.controls['cycle'].value;
		let store = cycleObj.storesObjects;
		let storereset =[];
		for(let i=0;i<store.length;i++){
			if(store[i].locationDivision != null) {
				store[i].locationDivision = null;
				storereset.push(store[i]);		
			}
		}
		this.addCriticalPathForm.controls['cycle'].setValue(storereset);
		
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

		this.addCriticalPathForm.controls['cycle'].setValue(cycleObj);
		
		let storeobj = this.addCriticalPathForm.controls['stores'].value;
		let storeresetobj =[];

		if(storeobj !== null) {
			for(let ii=0;ii<storeobj.length;ii++){
				if(storeobj[ii].locationDivision !== null) {
					storeobj[ii].locationDivision = null;
					storeresetobj.push(storeobj[ii]);	
				}
				if(storeobj[ii].regionalManagers !== null) {
					if(storeobj[ii].regionalManagers.userLogin !== null) {
						storeobj[ii].regionalManagers.userLogin = null ;
						storeresetobj.push(storeobj[ii]);	
					}
				}
			}
			this.addCriticalPathForm.controls['stores'].setValue(storeresetobj);
		}
	}
	
	submitCriticalPathForm(criticalPathForm : any) {
    	
		this.error = '';

		if(this.addCriticalPathForm.valid) {
			this.buttonText = CONSTANTS.SUBMITTING;
	        this.criticalPathService.addCriticalPath(criticalPathForm).subscribe((results) => {
	        		this.criticalPathAdded = true;
	        		this.error = "";
	        		setTimeout(() => {
	        			this.router.navigateByUrl('/' + ROUTS.CRITICAL_PATH);
	            	}, 2500);
	        		
	        }, (error) => {
	        		this.criticalPathAdded = false;
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
		this.addCriticalPathForm.controls['isActive'].setValue (criticalPath);
	}
}