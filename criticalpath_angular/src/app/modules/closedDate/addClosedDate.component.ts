import{Component,OnInit}from'@angular/core';import{RouterModule,Routes,Router}from'@angular/router';import{ReactiveFormsModule,FormsModule,FormGroup,FormControl,Validators,FormBuilder}from'@angular/forms';

import * as ROUTS from'../../constants/routs';
import{CONSTANTS}from'../../constants/constant';
import { NoWhitespaceValidator } from '../../common/customValidator/custom.validator';
import{ClosedDateService}from'../../service/closedDate.service';
import{BrandService}from'../../service/brand.service';
import{StoreService}from'../../service/store.service';
import{CommonService}from'../../service/common.service';
import{LocalStorageService}from'../../service/localStorage.service';
import{ UiSwitchModule }from'ngx-toggle-switch';
import{ StoreCloseDateRequest }from'../../model/storeCloseDateRequest';

declare var $:any;

@Component
({selector:'',templateUrl:'./addClosedDate.component.html'
})export class AddClosedDateComponent implements OnInit {

	constructor(public router: Router, public closedDateService : ClosedDateService,
				public commonService : CommonService,
				public brandService : BrandService,
				public storeService : StoreService,
				public localStorageService : LocalStorageService){}
	
	CONSTANTS : any = CONSTANTS;
	buttonText : string;
				
	addClosedDateForm: FormGroup;
	error : string;
	closedDateAdded : boolean = false;
	brandList : any[] = [];
	storeList : any[] = [];
	public brandId : any;
    public storeId : any;
	storeObj : any[] = [];
	selectedBrandList : any = [];
	public storeCloseDateRequest : any;
	requiredFlag : boolean = false;
	descriptionLength : number;
				
	ngOnInit() {
		
		this.commonService.validateUserByRole();
		
		this.buttonText = CONSTANTS.SUBMIT;
		
		let closedDateObj = this;
		
		this.initializeComponent();
		
		this.addClosedDateForm = new FormGroup({
			name: new FormControl('', [Validators.required, NoWhitespaceValidator]),
			description: new FormControl('', []),
			brand: new FormControl([], [Validators.required]),
			closeDate: new FormControl('', [Validators.required]),
			stores: new FormControl([], [Validators.required]),
			isActive: new FormControl(true, [])
		});
		
		$('#datepicker').on('change', function (e) {
		    var data = e.target.value;
		    closedDateObj.setFormControl(data, 'closeDate');
		});
		
		this.brandService.getAllActiveBrand().subscribe((results) => {
			this.brandList = results.data;
		}, (error) => {
			
		}); 
		
	}
	
	countLength(){
		let descriptionvalue = this.addClosedDateForm.controls['description'].value;
		
		this.descriptionLength = descriptionvalue.length;
		
	}

	setFormControl(value : any, controlName : string) {
		this.addClosedDateForm.controls[controlName].setValue(value);
	}
	
	getBrand(brandId) {
		 this.storeService.getStoreByBrandId(brandId).subscribe((results) => {
				this.storeList.push(results.data);
			}, (error) => {
				
			});
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
	
	selectCheckBox(e){
		if(e.target.checked === true) {
			this.addClosedDateForm.controls['stores'].setValue(this.storeList);

		}else {
			this.addClosedDateForm.controls['stores'].setValue("");
		}
	}
	
	removeAuthorities() {

		let store = this.addClosedDateForm.controls['stores'].value;
		let storereset =[];
		for(let i=0;i<store.length;i++){
			if(store[i].locationDivision !== null) {
				if(store[i].locationDivision.divisionalSalesManagers !== null){
//					store[i].locationDivision.divisionalSalesManagers.userLogin.authorities = null;
				}				
//				if(store[i].locationDivision.regionalManagers !== null){
////					store[i].locationDivision.regionalManagers.userLogin.authorities = null;
//				}
				if(store[i].regionalManagers !== null){
					if(store[i].regionalManagers.userLogin !== null){
						store[i].regionalManagers.userLogin = null;
					}
				}
			}
				
			storereset.push(store[i]);					
		}

		this.addClosedDateForm.controls['stores'].setValue(storereset);
	}
	
	removeBrand(event : any) {
		
		//Splice the store from array
		for(var i = 0 ; i < this.storeList.length ; i++) {
			if(this.storeList[i].brand.id == event.id) {
				this.storeList.splice(i, 1);
				i = i-1;
			}
		}
		
		let selectedStores: any = this.addClosedDateForm.controls['stores'].value;
		for(var i = 0 ; i < selectedStores.length ; i++) {
			if(selectedStores[i].brand.id == event.id) {
				selectedStores.splice(i, 1);
				i = i-1;
			}
		}
		
		this.setFormControl(selectedStores,'stores');
		//Splice the brand from array
		var index = this.selectedBrandList.findIndex((brand) => brand.id == event.id);
		this.selectedBrandList.splice(index, 1);
	}
	
	removeStore(event : any) {
		
		let selectedStores: any = this.addClosedDateForm.controls['stores'].value;
		let isNeedToRemoveBrand = false;
		
		for(var i = 0 ; i < selectedStores.length ; i++) {
			
			if(selectedStores[i].brand.id == event.brand.id) {
				isNeedToRemoveBrand = true;
				break;
			}	
		}

		let selectedBrands: any = this.addClosedDateForm.controls['brand'].value;	
		if(!isNeedToRemoveBrand) {
			var brandIndex = selectedBrands.findIndex((brand) => brand.id == event.brand.id);
			selectedBrands.splice(brandIndex, 1);
			this.addClosedDateForm.controls['brand'].setValue(selectedBrands);
		}
	}
	
	submitClosedDateForm(closedDateForm : any) {
		this.removeAuthorities();
		
		this.error = '';
		if(this.addClosedDateForm.valid) {
			this.buttonText = CONSTANTS.SUBMITTING;
			this.storeCloseDateRequest = new StoreCloseDateRequest(closedDateForm);
	        this.closedDateService.addClosedDate(this.storeCloseDateRequest).subscribe((results) => {
	        		this.closedDateAdded = true;
	        		setTimeout(() => {
	        			this.router.navigateByUrl('/' + ROUTS.CLOSED_DATE);
	            	}, 2500);
	        		
	        }, (error) => {
	        		this.error = JSON.parse(error._body).message;
	        		this.buttonText = CONSTANTS.SUBMIT;
	        });
		} else
			this.requiredFlag = true;
    }

	initializeComponent() {
		 $('#datepicker').datepicker({
			    autoclose: true,
			    format: 'dd/mm/yyyy',
			  });
		  
	}
	
	back() {
		this.router.navigateByUrl('/' + ROUTS.CLOSED_DATE);
	}

	changeStatus(closedDate) {
		this.addClosedDateForm.controls['isActive'].setValue (closedDate);
	}
}