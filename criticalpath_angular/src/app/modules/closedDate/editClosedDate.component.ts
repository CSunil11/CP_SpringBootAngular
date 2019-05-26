import {Component, OnInit } from '@angular/core';
import { RouterModule, Routes, Router, ActivatedRoute } from '@angular/router';
import { DatePipe } from '@angular/common';
import { ReactiveFormsModule, FormsModule, FormGroup, FormControl, Validators, FormBuilder } from '@angular/forms';
import { NoWhitespaceValidator } from '../../common/customValidator/custom.validator';
import * as ROUTS from '../../constants/routs';
import { CONSTANTS } from '../../constants/constant';
import { ClosedDateService } from '../../service/closedDate.service';
import{ BrandService }from'../../service/brand.service';
import{ StoreService }from'../../service/store.service';
import { UiSwitchModule } from 'ngx-toggle-switch';
import {SelectModule} from 'ng2-select';
import{ StoreCloseDateRequest }from'../../model/storeCloseDateRequest';
import { CommonService } from '../../service/common.service';


declare var $:any;
@Component({
	selector :'',
	templateUrl : './editClosedDate.component.html'
})
export class EditClosedDateComponent implements OnInit {
	
	constructor(public router : Router,
				public route: ActivatedRoute,
				private datePipe: DatePipe,
				public closedDateService : ClosedDateService,
				public brandService : BrandService,
				public commonService : CommonService,
				public storeService : StoreService){}
	
	CONSTANTS : any = CONSTANTS;
    buttonText : string;
				
	editClosedDateForm: FormGroup;
	public closedDateId : number ;
	private sub : any;
	error : string;
	editedClosedDate : boolean = false;
	requiredFlag : boolean = false;
				
	selectedBrandList : any = [];
	brandList : any[] = [];
	storeList : any[] = [];
	descriptionLength : number;
				
	public storeCloseDateRequest : any;
				
	ngOnInit() {
		
		this.commonService.validateUserByRole();
		
		this.buttonText = CONSTANTS.SUBMIT;
		
		let closedDateObj = this;
		
		 this.sub = this.route.params.subscribe(params => {
             
             this.closedDateId  = params['id'];
         });
		 
		 this.initializeComponent();
		 
		 this.editClosedDateForm = new FormGroup({
			name: new FormControl('', [Validators.required, NoWhitespaceValidator]),
			description: new FormControl('', []),
			closeDate: new FormControl('', [Validators.required]),
			brand: new FormControl([], [Validators.required]),
			stores: new FormControl([], [Validators.required]),
			isActive: new FormControl('', [])
		 });
		
		 $('#datepicker').on('change', function (e) {
			    var data = e.target.value;
			    closedDateObj.editClosedDateForm.controls['closeDate'].setValue(data);
		 });
		 
		this.brandService.getAllActiveBrand().subscribe((results) => {
			
				this.brandList = results.data;
			
		}, (error) => {
			
		});
		
		this.closedDateService.getClosedDateData(this.closedDateId).subscribe((results) => {
			if(results !== undefined)
				this.setFormData(results);
		});
		
		setTimeout(() => {
			this.removeAuthorities();
			},700);
		
	}
	
	countLength(){
		let descriptionvalue = this.editClosedDateForm.controls['description'].value;
		
		this.descriptionLength = descriptionvalue.length;
	}
	
	selectCheckBox(e){
		if(e.target.checked === true) {
			this.editClosedDateForm.controls['stores'].setValue(this.storeList);

		}else {
			this.editClosedDateForm.controls['stores'].setValue("");
		}
	}
	
	removeAuthorities() {
		
		let store = this.editClosedDateForm.controls['stores'].value;
		let storereset =[];
		for(let i=0;i<store.length;i++){
			if(store[i].locationDivision !== null) {
				if(store[i].locationDivision.divisionalSalesManagers !== null){
						store[i].locationDivision.divisionalSalesManagers = null;
				}				
				if(store[i].regionalManagers !== null){
					if(store[i].regionalManagers.userLogin !== null){
						store[i].regionalManagers.userLogin = null;
					}
				}
				
			}
			
			storereset.push(store[i]);					
		}

		this.editClosedDateForm.controls['stores'].setValue(storereset);
	}
	
	
	setFormData(results) {
		
		this.editClosedDateForm = new FormGroup({
			name: new FormControl(results.data.name, [Validators.required, NoWhitespaceValidator]),
			description: new FormControl(results.data.description, []),
			closeDate: new FormControl(this.datePipe.transform(results.data.closeDate,"dd/MM/yyyy"), [Validators.required]),
			brand: new FormControl([], []),
			stores: new FormControl([], [Validators.required]),
			isActive: new FormControl(results.data.isActive, []),
			id: new FormControl(results.data.id, [])
		});
		
		this.descriptionLength = results.data.description.length;
		
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
		
		this.editClosedDateForm.controls['brand'].setValue(this.selectedBrandList);
		let storeLists: any = results.data.stores;
		
		this.selectedBrandList.forEach((brand) => {
			this.getStoreByBrand(brand);
		});
		
		this.editClosedDateForm.controls['stores'].setValue(storeLists);
	}
	
	removeBrand(event : any) {
		
		//Splice the store from array
		for(var i = 0 ; i < this.storeList.length ; i++) {
			if(this.storeList[i].brand.id == event.id) {
				this.storeList.splice(i, 1);
				i = i-1;
			}
		}
		
		let selectedStores: any = this.editClosedDateForm.controls['stores'].value;
		for(var i = 0 ; i < selectedStores.length ; i++) {
			if(selectedStores[i].brand.id == event.id) {
				selectedStores.splice(i, 1);
				i = i-1;
			}
		}
		this.editClosedDateForm.controls['stores'].setValue(selectedStores);
		
		//Splice the brand from array
		var index = this.selectedBrandList.findIndex((brand) => brand.id == event.id);
		this.selectedBrandList.splice(index, 1);
//		this.removeAuthorities();
	}
	
	removeStore(event : any) {	
		
		let selectedStores: any = this.editClosedDateForm.controls['stores'].value;
		let isNeedToRemoveBrand = false;
		
		for(var i = 0 ; i < selectedStores.length ; i++) {
			
			if(selectedStores[i].brand.id == event.brand.id) {
				isNeedToRemoveBrand = true;
				break;
			}	
		}
	
		let selectedBrands: any = this.editClosedDateForm.controls['brand'].value;	
		if(!isNeedToRemoveBrand) {
			var brandIndex = selectedBrands.findIndex((brand) => brand.id == event.brand.id);
			selectedBrands.splice(brandIndex, 1);
			this.editClosedDateForm.controls['brand'].setValue(selectedBrands);
			
			for(var i = 0 ; i < this.storeList.length ; i++) {
				if(this.storeList[i].brand.id == event.brand.id) {
					this.storeList.splice(i, 1);
					i = i-1;
				}
			}
		}
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
	
	submitEditedClosedDateForm(closedDateForm : any) {
		this.removeAuthorities();
		this.error = '';
		if(this.editClosedDateForm.valid) {
			this.buttonText = CONSTANTS.SUBMITTING;
			this.storeCloseDateRequest = new StoreCloseDateRequest(closedDateForm);
	        this.closedDateService.addClosedDate(closedDateForm).subscribe((results) => {
	        	this.editedClosedDate = true;
	    		setTimeout(() => {
	    			this.router.navigateByUrl('/' + ROUTS.CLOSED_DATE);
	        	}, 2500);
	        }, (error) => {
	        	this.error = JSON.parse(error._body).message;
	        	this.buttonText = CONSTANTS.SUBMIT;
	        });
		}else 
			this.requiredFlag = true;
    }

	changeStatus(closedDate) {
		this.editClosedDateForm.controls['isActive'].setValue (closedDate);
	}
	
	back() {
		this.router.navigateByUrl('/' + ROUTS.CLOSED_DATE);
	}
	
	initializeComponent() {
		 $('#datepicker').datepicker({
			    autoclose: true,
			    format: 'dd/mm/yyyy',
			    forceParse : false,
		 });
	}
		  
}