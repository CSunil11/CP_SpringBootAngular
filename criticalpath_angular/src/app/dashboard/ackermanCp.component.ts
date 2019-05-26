import {Component,OnInit,ElementRef, ViewChild} from '@angular/core';
import { RouterModule, Routes, Router, ActivatedRoute, ParamMap } from '@angular/router';
import { ReactiveFormsModule, FormsModule, FormGroup, FormControl, Validators, FormBuilder } from '@angular/forms';

import * as ROUTS from '../constants/routs';
import { CONSTANTS } from '../constants/constant';

import { CommonService } from '../service/common.service';
import { BrandUserService } from '../service/brandUser.service';
import { StockTakeCycleService } from '../service/stockTakeCycle.service';
import { ClosedDateService } from '../service/closedDate.service';
import {DatePipe} from '@angular/common';
declare var $: any;

@Component({
    templateUrl :'./ackermanCp.component.html'
})
export class AckermanCpComponent implements OnInit {
    
    constructor(public router: Router, public commonService : CommonService,
    		private datePipe: DatePipe, 
    		public brandUserService : BrandUserService,
    		public closedDateService : ClosedDateService,
    	    public route: ActivatedRoute, public stockTakeCycleService : StockTakeCycleService) {}
  
    addStockTakeDateForm: FormGroup;
    stockTakeCycleId : any ;
    storeId : any ;
    closedDate : any;
    cycleLength : number ;	
    daysIndex : any[] = [];
    daysOfList :any[] = ["Sunday", "Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"];
    stokeTakeDate : any;
    stokeTakeDateFormate : any;
    storeName : any;
    startdate : any;
    startdatecheck : any;
    cycleStartDate : any;
    stokeDate : any;
    stokeDt : any;
    startDateFormate : any;		 
    someFormattedDateVar : any = 0;
    adminCycleData : any;
    time : any;
   	doNotCountList : any[] = [];
    remainingDateList : any[] = [];
    closeDateList : any[] = [];
    tempList : any[] = [];
    dbCloseList  : any[] = [];
    buttonText : string;
    dateStart : any = null;
    		 
    ngOnInit() {
    	
    	this.buttonText = "SUBMIT STOCK TAKE DATE";
    	
    	  this.addStockTakeDateForm = new FormGroup({
    		  stokeDate: new FormControl('', [Validators.required]),
    		  startdate: new FormControl('', [Validators.required]),
    		  time : new FormControl('', [Validators.required])
        });
    	
    	this.time = 'AM';
    	
    	this.route.paramMap.subscribe((params: ParamMap) => {   //This apploach is better than the upper one

          console.log("storeId checking..."+params.get('storeId'));  
            if(params.get('storeId') !== null && params.get('storeId') !== 'undefined' && params.get('cycleId') !== null && params.get('cycleId') !== 'undefined')
            {
    		      this.storeId = parseInt(params.get('storeId'));
              this.stockTakeCycleId = parseInt(params.get('cycleId'));
              this.storeName = params.get('storeName');

              this.stockTakeCycleService.getUpcomingCycleStartDate(this.storeId).subscribe((results) => {
                console.log(results.data);
                this.dateStart = results.data;
              });
            }
            
        });
    	
    	
    	
    	
    	
    	
    	
    	
//    	this.stockTakeCycleId  = cycleId;
//    	this.storeId  = storeId;

//    	if(this.stockTakeCycleId > 0){
    		
//	    	this.stockTakeCycleService.getStockTakeCycleData(this.stockTakeCycleId).subscribe((results) => {
//	    		console.log(results);
//	    		console.log( this.stockTakeCycleId );
////	    		
//	    		let doNotCountDay : any;
//				doNotCountDay = results.data.cycle.doNotCount.split(", ");
//				for(let i =0; i< doNotCountDay.length;i++) {
//					this.doNotCountList.push(doNotCountDay[i]);
//				}
//				
//				for(let j =0; j< this.doNotCountList.length; j++){
//					for(let i =0; i< this.daysOfList.length; i++){
//						if(this.daysOfList[i] == this.doNotCountList[j]){
//	    				this.daysIndex.push(i);
//						}
//					}
//				}
//	    		this.cycleLength = results.data.cycle.length;
//	    	});
//	    	console.log(this.daysIndex);
//	    	let tempObj = this;
//	    	$('#dt').datepicker({
//	    		         format:'mm/dd/yyyy',
//	    		         timepicker:false,
//	    		         inline:true,
//	    		         keyboardNavigation: false,
//	    		         autoclose: true,
//	    		         closeOnDateSelect : true,
//	    		         clearBtn : true,
//	    		         orientation: "bottom"
//	    		     }).on("change", function(e){
////	    		    	 $('.datepicker').hide();
//	    		    	 tempObj.getDate(e, tempObj);
//	    		     });
//	    }
    }

    
    
    getDate(e, tempObj){  
    	 this.closeDateList = [];
    	 this.remainingDateList = [];
    		var date = $(e.target).val();
    		let stockDate = new Date(date);
    		
    		let olddd = stockDate.getDate();
	         let day =  stockDate.getDay();
	         let mm = stockDate.getMonth()+ 1;
	         let y = stockDate.getFullYear();          
	         
	         let dd = olddd;
	         let someFormattedDate =  mm + '/' + dd + '/' + y;
	         let stockdt = mm + '-' + dd + '-' + y;
	     	
    		 let stockDate1 =  y + '-' + mm + '-' + dd ;
	         this.stokeTakeDate = stockDate1;
    		  this.stokeTakeDateFormate = stockdt;
	         tempObj.stokeDate = this.datePipe.transform(someFormattedDate, 'dd/MM/yyyy');
	         console.log(someFormattedDate);
    		
    		let newdate = new Date(date);
    		let dayMilliseconds = 1000 * 60 * 60 * 24;
    		newdate.setDate(newdate.getDate() - (tempObj.cycleLength -1));   
	        
	    	 let start  = newdate;
//	    	 console.log("start 1.."+newdate)
//	    	 console.log("stockDate 1.."+stockDate)
//	    	 this.stockTakeCycleService.calculateStartDt(this.stockTakeCycleId , stockdt , this.storeId).subscribe((results) => {
//	    		 console.log(results);
//	    		 let startdt = new Date(results.data);
//	    		 console.log(startdt);
//	    		 let olddd = startdt.getDate();
//		         let day =  startdt.getDay();
//		         let mm = startdt.getMonth()+ 1;
//		         let y = startdt.getFullYear();          
//		         
//		         let dd = olddd;
//		         let someFormattedDate =  dd + '/' + mm + '/' + y;
//		         this.cycleStartDate = y + '-' + mm + '-' + dd ;
//		         this.startDateFormate = dd + '-' + mm + '-' + y ;
//		 		 (<HTMLInputElement>document.getElementById("start")).value = someFormattedDate;
//		 		 this.someFormattedDateVar = 1;
//	    	 })
//	    	  tempObj.check(newdate, stockDate);
	    	 
	    	 console.log(this.remainingDateList);
	    	 
	    	
    }
    
    check(start , stockDate){
    	this.remainingDateList = [];
    	this.closeDateList = [];
    	 this.stokeDt = stockDate;
    	let totalSundays = 0;
    	let olddd ;
    	let day ;
    	let mm;
        let y;  
       console.log("start.....  "+start) ;
        olddd = start.getDate();
        day =  start.getDay();
        mm = start.getMonth()+ 1;
        y = start.getFullYear();          
        
        let srartdt =  y + '-' + mm + '-' + olddd ;
        
        olddd = stockDate.getDate();
        day =  stockDate.getDay();
        mm = stockDate.getMonth()+ 1;
        y = stockDate.getFullYear();
        
        let stockdt =  y + '-' + mm + '-' + olddd ;
        
//        let dbCloseList =[];
   	 	this.closedDateService.getClosedDateListByStoreDate(this.storeId , srartdt, stockdt).subscribe((results) => {
//   	 		dbCloseList = results.data;
   	 		for(let i =0; i<results.data.length;i++) {
//		 		if(this.closeDateList.indexOf(dbCloseList[i]) == -1) {
		 			this.dbCloseList.push(this.datePipe.transform(results.data[i], 'yyyy-MM-dd'));
//		 		}
		 	}
   	    });
   	 	console.log("this.dbCloseList=========");
		console.log(this.dbCloseList);
		
   	 	for(let j = start; j <= stockDate;){
   	 			for(let i = 0; i< this.daysIndex.length;i++) {
   	 				if (j.getDay() == this.daysIndex[i]){
  	       			 totalSundays++;
  	       			 this.closeDateList.push(this.datePipe.transform(j, 'yyyy-MM-dd'));
   	 				}
   	 			}
   	 			this.remainingDateList.push(this.datePipe.transform(j, 'yyyy-MM-dd'));
   	 			j.setTime(j.getTime() + 1000*60*60*24);
  	      }
   	 	console.log("closeDateList=========");
		console.log(this.closeDateList);
		console.log("dbCloseList length====------------=====");
		console.log(this.dbCloseList);
   	 	
   		for(let i =0; i< this.dbCloseList.length;i++) {
   		 		console.log("gfgdgg-----------------");
   		 		if(this.closeDateList.indexOf(this.dbCloseList[i]) == -1) {
   		 			console.log("sssssssss...."+this.dbCloseList[i]);
   		 			this.closeDateList.push(this.datePipe.transform(this.dbCloseList[i], 'yyyy-MM-dd'));
   		 		}
   		}
   		    console.log("closeDateList=========");
   			console.log(this.closeDateList);
   			console.log("remainingDateList=========");
   			console.log(this.remainingDateList);
   			
//   		 	start.setDate(start.getDate()- this.cycleLength - this.closeDateList.length );
   		 	
//   			for(let jj = 0; jj < this.remainingDateList.length; jj ++){
//   				if(this.closeDateList.indexOf(this.remainingDateList[jj]) !== -1) {
//   		 			this.remainingDateList.splice(jj,1);
//   		 		}
//   	        }  
   			for(let i =0; i < this.closeDateList.length;i++) {
   				for(let jj = 0; jj < this.remainingDateList.length; jj ++){
   	      			 if (this.remainingDateList[jj] === this.closeDateList[i]){
   	      				 console.log("dddddd");
   	      				this.remainingDateList.splice(jj,1);      				
   	               	 }
   	      		 }
   	        }  
   			console.log("remainingDateList=========");
   			console.log(this.remainingDateList);
   		 	this.startdatecheck = start;
   			console.log("start 3...."+this.startdatecheck);
   			this.checkListLength();
   			return this.remainingDateList.length;
//        });
   	 	
    }
    
    checkListLength(){
    	
    	 if(this.remainingDateList.length < this.cycleLength) {
    		 console.log("gggg" +this.remainingDateList.length);
    		 console.log("stock dt : "+ this.stokeDt);
    		 console.log("startdatecheck dt : " +this.startdatecheck);
    		 let newdate = new Date(this.startdatecheck);
    		 newdate.setDate(newdate.getDate() - this.cycleLength - (this.cycleLength - this.remainingDateList.length) -1);
    		 console.log("newdate dt : " +newdate);
//    		 this.check(newdate, this.stokeDt);
    	 }else {
    		 console.log("else...........");
    		 console.log(new Date(this.remainingDateList[0]));
    	 }
    	
    }
    
    saveStokeTakeDate(){

    	this.buttonText = 'SUBMITTING...';
    	
    	if(this.someFormattedDateVar === 1)
    	{
    		console.log("Date is entered"+this.someFormattedDateVar);
    		console.log(this.adminCycleData);
    		console.log(this.stokeTakeDateFormate);
    		console.log(this.cycleStartDate);
    		this.stockTakeCycleService.addAckCycle(this.stockTakeCycleId ,this.cycleStartDate, this.stokeTakeDate ,this.storeId, this.time).subscribe((results) => {
    			console.log(results);
    			this.router.navigateByUrl("/viewAllStockCycles/"+results.data.id+"/"+this.startDateFormate+"/"+this.storeId+"/"+this.stokeTakeDateFormate);
    		}, (error) => {
    			
    		});
    	}
    	else
    	{
    		console.log("Date not enetered "+this.someFormattedDateVar);
    	}
    	
    }
    
    logout() {
    	this.brandUserService.logOut().subscribe((results)  => {
		},(error) => {
			
		});
		this.commonService.deleteCookie();
		this.router.navigateByUrl('/' + ROUTS.LOGIN);
	}
    

}

