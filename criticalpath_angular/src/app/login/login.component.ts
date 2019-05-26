import {Component, OnInit, HostListener} from '@angular/core';
import { RouterModule, Routes,Router,ActivatedRoute } from '@angular/router';
import { ReactiveFormsModule, FormsModule, FormGroup, FormControl, Validators, FormBuilder } from '@angular/forms';

import { LoginRepository } from '../repository/login.repository';
import { CommonService } from '../service/common.service';
import { StockTakeCycleService } from '../service/stockTakeCycle.service';
import { ClosedDateService } from '../service/closedDate.service';
import { LocalStorageService } from '../service/localStorage.service';

import * as ROUTS from '../constants/routs';
import { CONSTANTS } from '../constants/constant';

import { Role } from '../constants/role.enum';
import{ DatePipe }from'@angular/common';

@Component({
	selector :'',
	templateUrl : './login1.component.html'
})
export class LoginComponent implements OnInit {
	
	constructor(public routerModule : RouterModule,public router: Router,
			 	public route: ActivatedRoute,
			 	public closedDateService : ClosedDateService,
				public loginRepository : LoginRepository,
				public commonService : CommonService,
				public stockTakeCycleService : StockTakeCycleService,
				public localStorageService : LocalStorageService,
				public datePipe : DatePipe) {}
	
	loginForm: FormGroup;
	public access_token : string;
	error : string;
	requiredFlag : boolean = false;
	storeId : any; 			
	daysOfList :any[] = ["Sunday", "Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"];
				
	ngOnInit() {
		
//		if (this.commonService.checkCookie(CONSTANTS.COOKIE_ACCESSTOKEN)) 
//				this.router.navigateByUrl('/' + ROUTS.DASHBOARD);
		
		 this.loginForm = new FormGroup({
	            agentId: new FormControl('', [Validators.pattern("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$"), Validators.required]),
	            password: new FormControl('', [Validators.required]),
	            rememberMe: new FormControl(null)
	      });

	}
	
	submitForm() {

		if(this.loginForm.valid) {
	        this.loginRepository.login(this.loginForm.controls.agentId.value, this.loginForm.controls.password.value).subscribe((results) => {
	        	let encryptedToken: string = this.commonService.encrypt(results.access_token);
	        	this.access_token =results.access_token;
	        	//Save the access token and path in Cookie
	            this.commonService.setCookie(CONSTANTS.COOKIE_ACCESSTOKEN, encryptedToken, null, '/');
	            this.loginRepository.getUser().subscribe((results) => {
	                if (results != undefined && results.role.length != 0) {
	                		this.localStorageService.setItem(CONSTANTS.USER_ROLE, results.role);
	                		if(results.role == 'ROLE_RAM_USER' || results.role == 'ROLE_DSM_USER')
	                		{
	            				console.log(this.localStorageService.getItem(CONSTANTS.USER_ROLE));
	                			this.router.navigateByUrl('/' + ROUTS.ACKERMANPAGE);
	                		}else if(results.role == 'ROLE_SM_USER') {
	                			this.localStorageService.setItem(CONSTANTS.USER_ROLE, results.role);
	            				
	                			let userId = results.id;
	                			
	                			this.stockTakeCycleService.isActiveCycleAvail(userId).subscribe((results) => {
	                				
	                				console.log(results.data);
	                				
	                				this.storeId = results.data.storeId;
	                				let storeName = results.data.storeName;
	                				let cycleId = results.data.cycleId;
	                				let stockTakeDate = results.data.takeDate;

	                				if(stockTakeDate == null) {
	                					this.router.navigateByUrl("/ackermanViewAllBranchesComponent/"+this.storeId+"/"+cycleId+"/"+storeName);
	                				}else {
	                					let startDate = results.data.startDate;
	                					console.log("startdate"+startDate);
	                					console.log("takedate"+stockTakeDate);
	                					
	                					let stockDate = new Date(stockTakeDate);
	                					let startdate = new Date(startDate);
	                					
	                					let dd = stockDate.getDate();
		                		 	    let day =  stockDate.getDay();
		                		 	    let mm = stockDate.getMonth()+ 1;
		                		 	    let y = stockDate.getFullYear();          
		                		 	         
		                		 	    // let stockdt =  mm + '-' + dd + '-' + y ;
		                		 	    let stockdt = this.datePipe.transform(stockTakeDate, 'MM-dd-yyyy');
		                		 	    
		                		 	     dd = startdate.getDate();
		                		 	     day =  startdate.getDay();
		                		 	     mm = startdate.getMonth()+ 1;
		                		 	     y = startdate.getFullYear();   
		                		 	     
			                		 	 let startdt =  mm + '-' + dd + '-' + y ;
			                		 	
	                				  this.router.navigateByUrl("/viewAllStockCycles/"+cycleId+"/"+startdt+"/"+this.storeId+"/"+stockdt);
	                					
//
//	                		    		startDate.setDate(startDate.getDate() - (cycleLength -1)); 
//	                		    		let start  = startDate;
//	                		    		  for(let i = start; i <= stockDate;){
//	                		 	        	 if (i.getDay() == daysIndex){
//	                		 	        		 totalSundays++
//	                		 	        	 }
//	                		 	        	 i.setTime(i.getTime() + 1000*60*60*24);
//	                		 	         }
//	                		 	         	         
//	                		    		  startDate.setDate(startDate.getDate() - (cycleLength -1) -1 - (totalSundays));
//	                		    		 
//	                		    		  //set date format for database
//	                		 	       let dd = startDate.getDate();
//	                		 	       let day =  startDate.getDay();
//	                		 	       let mm = startDate.getMonth()+ 1;
//	                		 	       let y = startDate.getFullYear();          
//	                		 	         
//	                		 	        let newdate1 =  y + '-' + mm + '-' + dd ;
//	                		 	         
//	                		 	         dd = stockDate.getDate();
//	                		 	         day =  stockDate.getDay();
//	                		 	         mm = stockDate.getMonth()+ 1;
//	                		 	         y = stockDate.getFullYear();
//	                		 	         
//	                		 	         let stockDate1 =  y + '-' + mm + '-' + dd ;
//	                		 	         let stockDt =  mm + '-' + dd + '-' + y ;
//	                		 	         //set date format for database
//	                		 	         console.log(this.storeId);
//	                		 	        console.log(stockDate1);
//	                		 	       console.log(newdate1);
//	                		 	         this.closedDateService.getClosedDateByStoreDate(this.storeId , newdate1, stockDate1).subscribe((results) => {
//	                		 	            console.log(results);
//	                		 	        	 let closeddatelength = results.data;
//	                		 	        	startDate.setDate(startDate.getDate() - closeddatelength); 
//	                		 		         
//	                		 		         let startDate1  = startDate;
//	                		 		         let totalDonotCount = 0;
//	                		 		         for(let j = startDate1; j <= stockDate;){
//	                		 		        	 if (j.getDay() == daysIndex){
//	                		 		        		 totalDonotCount++
//	                		 		        	 }
//	                		 		        	 j.setTime(j.getTime() + 1000*60*60*24);
//	                		 		         }
//	                		 		         
//	                		 		        startDate.setDate(startDate.getDate() - (cycleLength -1) -1 - (totalDonotCount) -closeddatelength);
//	                		 		         
//	                		 		         dd = startDate.getDate();
//	                		 		         day =  startDate.getDay();
//	                		 		         mm = startDate.getMonth()+ 1;
//	                		 		         y = startDate.getFullYear();          
//	                		 		         
//	                		 		         let someFormattedDate =  mm + '-' + dd + '-' + y;
//	                		 		         console.log(someFormattedDate);
////	                		 		        this.router.navigateByUrl("/ackermanPage/ackermanCpComponent/"+cycleId+"/"+someFormattedDate+"/"+this.storeId+"/"+stockDt);
//	                		 		       this.router.navigateByUrl("/viewAllStockCycles/"+cycleId+"/"+someFormattedDate+"/"+this.storeId+"/"+stockDt);
//	                		 	 		 });

	                				}
	                			});
	                		}
	                		else
	                		{
	                			this.router.navigateByUrl('/' + ROUTS.DASHBOARD);
	                		}
	                	}
	            }, (error) => {
	            	console.log("error: "+error);
	            });
	        	
	        }, (error) => {
	        	this.error = JSON.parse(error._body).error_description;
	        	console.log("Error: "+this.error);
	        }); 
		} else
			this.requiredFlag = true;
    }
	
	setRoleWithPrivileges(roleWithPrivileges) {
		this.localStorageService.setItem(CONSTANTS.USER_ROLE, JSON.stringify(roleWithPrivileges));
	}
}