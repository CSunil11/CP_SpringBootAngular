import {Component,OnInit,ElementRef, ViewChild} from '@angular/core';
import { RouterModule,ParamMap, Routes, Router, ActivatedRoute } from '@angular/router';
import { ReactiveFormsModule, FormsModule, FormGroup, FormControl, Validators, FormBuilder } from '@angular/forms';

import * as ROUTS from '../constants/routs';
import { CONSTANTS } from '../constants/constant';

import { CommonService } from '../service/common.service';
import { StockTakeCycleService } from '../service/stockTakeCycle.service';
import { BrandUserService } from '../service/brandUser.service';
import { CriticalPathService } from '../service/criticalPath.service';
import { TaskStatusService } from '../service/taskStatus.service'
import { ClosedDateService } from '../service/closedDate.service';
import { StoreService } from '../service/store.service';
import { LocalStorageService } from '../service/localStorage.service';
import {DatePipe} from '@angular/common';
declare var $: any;

@Component({
    templateUrl :'./viewAllStockCycles.component.html'
})
export class ViewAllStockCyclesComponent implements OnInit {
    
    constructor(private route:ActivatedRoute,private datePipe: DatePipe, public router: Router,
    		 public stockTakeCycleService : StockTakeCycleService,
    		 public criticalPathService : CriticalPathService,
    		 public taskStatusService : TaskStatusService,
    		 public storeService : StoreService,
    		 public brandUserService : BrandUserService,
    		 public closedDateService : ClosedDateService,
    		 public localStorageService : LocalStorageService,
    		public commonService : CommonService) {
    }
    
    addTaskCompleteForm: FormGroup;
    stockTakeResultForm : FormGroup;
    stockTakeCycleId : any ;
    storeId : any ;
    stockTakeDate : any;
    startDate : any;
    cycleLength : any;
    criticalPathList : any[] = [];
    cycleDateList : any[] = [];
    closedDateList  : any[] = [];
    daysIndex : any[] = [];
    todayDate : any;
    stockTakeCycleLength(n: number): any[] {
	    return Array(n);
	  }
    daysOfList :any[] = ["Sunday", "Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"];
	taskStatusList :any[] = [];
	name : any;
	taskCompleteDate : any;  
	showModal : boolean;
	className :any;
	criticalPathId : any;
	statusId : any;
	completedby :any;
	completedDate : any;
	dateRangeLength : any;
	nameOld : any;
	checknameUser : any;
	taskId : any;
	startDay : any;
	userRole : any;
	storeName :any;
	criticalPathName :any;
    statusIdForClickedPath :any;
	dateValidateFlag : boolean = false;
    doNotCountList : any[] = [];
	displayStatus : any;  
	reasonForDecline :any;  
	resultId : any;
	resultMessageTitle : any;
	 error : string;
	  
	 //set 23 value pop up box variable//
	  
	  storeNo: any;
	  storeNamePopupBox: any;
	  analysedBy: any;
	  prevoiusCountDate: any;
	  currentCountDate: any;
	  calculatedDate: any;
	  approxMonth: any;
	  stockCountResultCost: any;
	  cellRegStockAdj: any;
	  totalShrinkageAdj: any;
	  minusSwellAllowance: any;
	  totalShrinkage: any;
	  salesIncludeVat: any;
	  salesExcludeVat: any;
	  shrinkageper: any;
	  minusSwellNotes: any;
	  stockCountResultCostNotes: any;
	  salesExcludeVatNotes: any;
	  salesIncNote:any;
	  cellRegAdjNote :any;
	  prevCountDateTime :any;
	  currentCountDateTime :any;
      stockTakeDateDisplay : any;
	///////////////////////////////
	  public showLoader : boolean = false;	
	  
	  
    ngOnInit() {
    	
    	this.userRole = this.localStorageService.getItem(CONSTANTS.USER_ROLE);
    	console.log(this.localStorageService.getItem(CONSTANTS.USER_ROLE));	
    	let todayDt =  new Date();
        this.todayDate = this.datePipe.transform(todayDt, 'yyyy-MM-dd'); 
        console.log("  this.todayDate......"+ this.todayDate);
        this.statusId = 6;
        this.className = 'bg_green';
    	let tempObj = this;
        
    	this.addTaskCompleteForm = new FormGroup({
  			name: new FormControl({value: '', disabled: true},[Validators.required]),
  			taskCompleteDate: new FormControl({value: '', disabled: true}, [Validators.required])			
        });

        
     /*   this.stockTakeResultForm = new FormGroup({
        	storeNo: new FormControl({value: ''}),
        	storeName: new FormControl({value: ''}),
        	analysedBy: new FormControl({value: ''}),
        	prevoiusCountDate: new FormControl({value: ''}),
        	currentCountDate: new FormControl({value: ''}),
        	calculatedDate: new FormControl({value: ''}),
        	approxMonth: new FormControl({value: ''}),
        	stockCountResultCost: new FormControl({value: ''}),
        	cellRegStockAdj: new FormControl({value: ''}),
        	totalShrinkageAdj: new FormControl({value: ''}),
        	minusSwellAllowance: new FormControl({value: ''}),
        	totalShrinkage: new FormControl({value: ''}),
        	salesIncludeVat: new FormControl({value: ''}),
        	salesExcludeVat: new FormControl({value: ''}),
        	shrinkageper: new FormControl({value: ''}),
        	minusSwellNotes :  new FormControl({value: ''}),
        	stockCountResultCostNotes : new  FormControl({value: ''}),
        	salesExcludeVatNotes: new  FormControl({value: ''})
        });*/

    	this.stockTakeDate = "";
    	
    	this.route.paramMap.subscribe((params: ParamMap) => {   //This apploach is better than the upper one
    		 this.stockTakeCycleId = parseInt(params.get('cycleStartBy'));
             this.startDate = params.get('startDate');
             this.storeId = parseInt(params.get('storeId'));
             this.stockTakeDate = params.get('stokeDate');           
        });  	
    	console.log( this.stockTakeCycleId );
    	
    	this.storeService.getStoreData(this.storeId).subscribe((results) => {
    		this.storeName = results.data.name;
    		console.log(this.storeName);
    	})
    	
    	this.getStockTakeResult();
    	
    	
    	
    	this.criticalPathService.getCriticalPathOfAckStockTakeCycle(this.stockTakeCycleId).subscribe((results) => {
    		
    		this.criticalPathList = results.data; 		
    		console.log(this.criticalPathList);

    	}); 
    	
    	
//    	this.criticalPathService.getCriticalPathOfStockTakeCycle(this.stockTakeCycleId).subscribe((results) => {
//    		
//    		this.criticalPathList = results.data; 		
//    		console.log(this.criticalPathList);
//
//    	}); 
    	
//    	this.stockTakeCycleService.getStockTakeCycleData(this.stockTakeCycleId).subscribe((results) => {
    	this.stockTakeCycleService.getAckStockTakeCycleData(this.stockTakeCycleId).subscribe((results) => {
    		this.cycleLength = parseInt(results.data.length);
    		console.log(results.data);
//    		
//    		let doNotCountDay : any;
//			doNotCountDay = results.data.doNotCount.split(", ");
//			for(let i =0; i< doNotCountDay.length;i++) {
//				this.doNotCountList.push(doNotCountDay[i]);
//			}
//			
//			for(let j =0; j< this.doNotCountList.length; j++){
//				for(let i =0; i< this.daysOfList.length; i++){
//					if(this.daysOfList[i] == this.doNotCountList[j]){
//    				this.daysIndex.push(i);
//					}
//				}
//			}
//
//    		
//    		 let startDate1 = new Date(this.startDate);
//    		 let stockTakeDate1 = new Date(this.stockTakeDate);
//    		 let totalSundays=0;
//    		 let start  = startDate1;
// 	         for(let j = start; j <= stockTakeDate1;){
//
// 	        	for(let i = 0; i< tempObj.daysIndex.length;i++) {
//	        		 if (j.getDay() == tempObj.daysIndex[i]){
//	        			 totalSundays++
//		        	 }
//	        	 }
// 	        	 j.setTime(j.getTime() + 1000*60*60*24);
// 	         }
// 	         	         
// 	        startDate1.setDate(startDate1.getDate() - (tempObj.cycleLength -1) -1 - (totalSundays));
//        	
//        	 let dd = startDate1.getDate();
//             let day =  startDate1.getDay();
//             let mm = startDate1.getMonth()+ 1;
//             let y = startDate1.getFullYear();          
//
//             let startdt =  y + '-' + mm + '-' + dd ;
//             
//             let dd1 = stockTakeDate1.getDate();
//             let day1 =  stockTakeDate1.getDay();
//             let mm1 = stockTakeDate1.getMonth()+ 1;
//             let y1 = stockTakeDate1.getFullYear();          
//
//             let stockdt =  y1 + '-' + mm1 + '-' + dd1 ;
//             
//             this.closedDateService.getClosedDateByStoreDate(tempObj.storeId , startdt, stockdt).subscribe((results) => {
//	        	 let closeddatelength = results.data;
//	        	 startDate1.setDate(startDate1.getDate() - closeddatelength); 
//		         
//		         let startDate  = startDate1;
//		         let totalDonotCount = 0;
//		         for(let j = startDate; j <= stockTakeDate1;){
//		        	 for(let i = 0; i< tempObj.daysIndex.length;i++) {
//		        		 if (j.getDay() == tempObj.daysIndex[i]){
//		        			 totalDonotCount++
//			        	 }
//		        	 }
//		        	 j.setTime(j.getTime() + 1000*60*60*24);
//		         }
//		         
//		         startDate1.setDate(startDate1.getDate() - (tempObj.cycleLength -1) -1 - (totalDonotCount) -closeddatelength);
//		         
//		      this.startDate = startDate1;
//		      
//		      this.closedDateService.getClosedDateListByStoreDate(this.storeId , startdt, stockdt).subscribe((results) => {
//
//	            	 for(let i =0; i<results.data.length;i++) {
//	            		 this.closedDateList.push(new Date(results.data[i]));
//	            	 }
//	            	 console.log( this.closedDateList);
//	            	 this.getCycleDate();
//	             });
//	        	 
//	 		 });


          
        	 this.stockTakeCycleService.getAllDateList(this.stockTakeCycleId , this.stockTakeDate).subscribe((results) => {
        		 console.log(results.data);
        		 this.cycleDateList = results.data;
        	 })
    	});

        // this.stockTakeDate = new Date(this.stockTakeDate);
        try{
        this.stockTakeDateDisplay = this.datePipe.transform(this.stockTakeDate, 'dd/MM/yyyy');
        }
        catch(error){
         let dateArray=this.stockTakeDate.split("-");
         this.stockTakeDateDisplay = dateArray[1] + "/" + dateArray[0] + "/" + dateArray[2];
        }

        console.log("ViewAll this.stockTakeDateDisplay: "+this.stockTakeDateDisplay);
 
    }
    
    getStockTakeResult(){
    	this.criticalPathService.getStockTakeResult(this.storeId).subscribe((results) => {
			console.log("=========1==========");
			console.log(results);
			console.log(results.data);
			console.log(results.data.length);
//			this.displayStatus = results.data.status_ram;
			
			
			if(results.data.id !== null)
			{
				if(results.data.status_ram !== null && results.data.status_ram !== "") {
					this.displayStatus = results.data.status_ram + " by RAM";
				}
				else if(results.data.status_dsm !== null) {
					this.displayStatus = results.data.status_dsm +" by DSM";
				}
				else if(results.data.status_sm !== null) {
					this.displayStatus = results.data.status_sm +" by SM";
				}
				else
				{
					this.displayStatus = null;
				}
				this.resultMessageTitle = "New Stock take Result";
				console.log("this.resultMessageTitle");
			}
			else
			{
				console.log("this.resultMessageTitle_1");
				this.resultMessageTitle = "";
			}
			
			console.log("_____"+this.resultMessageTitle);
			
			
			console.log("----------"+this.displayStatus);
			this.resultId = results.data.id;
			if(results.data.BranchCode != null) {
				this.storeNo = results.data.BranchCode;
//				this.stockTakeResultForm.controls['storeNo'].setValue(results.data.BranchCode);
			}else {
				this.storeNo = "";
//				this.stockTakeResultForm.controls['storeNo'].setValue("");
			}
			
			if(results.data.BranchName != null) {
				this.storeNamePopupBox = results.data.BranchName;
//				this.stockTakeResultForm.controls['storeName'].setValue(results.data.BranchName);
			}else {
				this.storeNamePopupBox = "";
//				this.stockTakeResultForm.controls['storeName'].setValue("");
			}
			
			if(results.data.StockCountDate != null) {
				this.currentCountDate = results.data.StockCountDate;
//				this.stockTakeResultForm.controls['currentCountDate'].setValue(results.data.StockCountDate);
			}else {
				this.currentCountDate = "";
//				this.stockTakeResultForm.controls['currentCountDate'].setValue("");
			}
			
			if(results.data.PrevStockCountDate != null) {
				let splitDt = results.data.PrevStockCountDate.split(" ");
				let newDate = new Date(splitDt[0]);
				console.log(newDate);
				this.prevoiusCountDate = results.data.PrevStockCountDate;
//				this.stockTakeResultForm.controls['prevoiusCountDate'].setValue(results.data.PrevStockCountDate);
			}else {
				this.prevoiusCountDate = "";
//				this.stockTakeResultForm.controls['prevoiusCountDate'].setValue("");
			}
			console.log(results.data.ResultDate);
			if(results.data.ResultDate != null) {
				let newDate = new Date(results.data.ResultDate);
				console.log(newDate);
				this.calculatedDate = results.data.ResultDate;
//				this.stockTakeResultForm.controls['calculatedDate'].setValue(this.datePipe.transform(newDate, 'dd MMM yyyy'));
			}else {
				this.calculatedDate = "";
//				this.stockTakeResultForm.controls['calculatedDate'].setValue("");
			}
			
			if(results.data.ApproxMonths != null) {
				this.approxMonth = results.data.ApproxMonths;
//				this.stockTakeResultForm.controls['approxMonth'].setValue(results.data.ApproxMonths);
			}else {
				this.approxMonth = "";
//				this.stockTakeResultForm.controls['approxMonth'].setValue("");
			}
			
			if(results.data.StockCountResultAmt != null) {
				this.stockCountResultCost = results.data.StockCountResultAmt;
//				this.stockTakeResultForm.controls['stockCountResultCost'].setValue(results.data.StockCountResultAmt);
			}else {
				this.stockCountResultCost = "";
//				this.stockTakeResultForm.controls['stockCountResultCost'].setValue("");
			}
			
			if(results.data.CellRegStockAdjAmt != null) {
				this.cellRegStockAdj = results.data.CellRegStockAdjAmt;
//				this.stockTakeResultForm.controls['cellRegStockAdj'].setValue(results.data.CellRegStockAdjAmt);
			}else {
				this.cellRegStockAdj = "";
//				this.stockTakeResultForm.controls['cellRegStockAdj'].setValue("");
			}
			
			if(results.data.SwellAllowAmt != null) {
				this.minusSwellAllowance = results.data.SwellAllowAmt;
//				this.stockTakeResultForm.controls['minusSwellAllowance'].setValue(results.data.SwellAllowAmt);
			}else {
				this.minusSwellAllowance = "";
//				this.stockTakeResultForm.controls['minusSwellAllowance'].setValue("");
			}
			
			if(results.data.SalesIncAmt != null) {
				this.salesIncludeVat = results.data.SalesIncAmt;
//				this.stockTakeResultForm.controls['salesIncludeVat'].setValue(results.data.SalesIncAmt);
			}else {
				this.salesIncludeVat = "";
//				this.stockTakeResultForm.controls['salesIncludeVat'].setValue("");
			}
			
			if(results.data.SalesExcAmt != null) {
				this.salesExcludeVat = results.data.SalesExcAmt;
//				this.stockTakeResultForm.controls['salesExcludeVat'].setValue(results.data.SalesExcAmt);
			}else {
				this.salesExcludeVat = "";
//				this.stockTakeResultForm.controls['salesExcludeVat'].setValue("");
			}
			
			if(results.data.ShrinkPerc != null) {
				this.shrinkageper = results.data.ShrinkPerc;
//				this.stockTakeResultForm.controls['shrinkageper'].setValue(results.data.ShrinkPerc);
			}else {
				this.shrinkageper ="";
//				this.stockTakeResultForm.controls['shrinkageper'].setValue("");
			}
			
			if(results.data.SalesExcAmtNote != null) {
				this.salesExcludeVatNotes = results.data.SalesExcAmtNote;
//				this.stockTakeResultForm.controls['salesExcludeVatNotes'].setValue(results.data.ShrinkPercNote);
			}else {
				this.salesExcludeVatNotes = "";
//				this.stockTakeResultForm.controls['salesExcludeVatNotes'].setValue("");
			}
//			console.log(parseFloat(results.data.CellRegStockAdjAmt) + parseFloat(results.data.StockCountResultAmt));
			this.analysedBy ="";
			this.totalShrinkage =parseFloat(this.cellRegStockAdj) + parseFloat(this.stockCountResultCost) +parseFloat(this.minusSwellAllowance);
			this.totalShrinkageAdj =parseFloat(this.cellRegStockAdj) + parseFloat(this.stockCountResultCost);
			
			if(results.data.SwellAllowNote != null) {
				this.minusSwellNotes =results.data.SwellAllowNote;
			}else {
				this.minusSwellNotes ="";
			}
			
			if(results.data.StockCountResultNote != null) {
				this.stockCountResultCostNotes =results.data.StockCountResultNote;
			}else {
				this.stockCountResultCostNotes ="";
			}
			
			if(results.data.SalesIncAmtNote != null) {
				this.salesIncNote =results.data.SalesIncAmtNote;
			}else {
				this.salesIncNote ="";
			}
			
			if(results.data.CellRegStockAdjNote != null) {
				this.cellRegAdjNote =results.data.CellRegStockAdjNote;
			}else {
				this.cellRegAdjNote ="";
			}
			
			this.prevCountDateTime = results.data.PrevStockCountDateTime;
			this.currentCountDateTime = results.data.StockCountDateTime;
		
    	});
    }
    
    getCycleDate(){
    	
    	let totalDonotCount =0;
    	let doNotCountList  = [];
    	let startDate;
    	let stockTakeDate;
    	
    	startDate = new Date(this.startDate);
   	 	stockTakeDate = new Date(this.stockTakeDate);

         startDate = new Date(this.startDate);
         
    	for(let j = startDate; j <= stockTakeDate;){
//       	 if (j.getDay() == this.daysIndex){     		
//       		doNotCountList.push(new Date(j));
//       	 }
    	for(let i = 0; i< this.daysIndex.length;i++) {
       		 if (j.getDay() == this.daysIndex[i]){
       			doNotCountList.push(new Date(j));
	        	 }
       	 }
     
       	 j.setTime(j.getTime() + 1000*60*60*24);
        }
   
    	 let totalNotCountList =[];
    	 let totalNotCountListAsc =[];
    	 totalNotCountList = doNotCountList.concat(this.closedDateList);
    	
    	 totalNotCountList.sort(function(x,y){ return x.getDate() < y.getDate() ? -1 : x.getDate() == y.getDate() ? 0 : 1; });
    	
    	 totalNotCountListAsc = totalNotCountList;
    	  	 
    	 startDate = new Date(this.startDate);
    	 stockTakeDate = new Date(this.stockTakeDate);
    	 
    	for(let jj = startDate; jj <= stockTakeDate;){
    			this.cycleDateList.push(new Date(jj));
    			 jj.setTime(jj.getTime() + 1000*60*60*24);
    	}
    		
      	for(let jj = 0; jj < this.cycleDateList.length; jj ++){
      		for(let i =0; i < totalNotCountListAsc.length;i++) {       			
      			 if (this.cycleDateList[jj].getDate() === totalNotCountListAsc[i].getDate()){
      				this.cycleDateList.splice(jj,1);      				
               	 }
      		 }
        }  
    }
    
    getDate(e, tempObj){  
     
        this.taskCompleteDate = null;
		var date = $(e.target).val();
		let selectedDate = new Date(date);
		let fromatedDate = date.replace(/[^a-zA-Z0-9]/g, '-');
		let newdate = fromatedDate.split("-")[2]+"-"+fromatedDate.split("-")[1]+"-"+fromatedDate.split("-")[0];
		this.taskCompleteDate = date;
		this.addTaskCompleteForm.controls['taskCompleteDate'].setValue(this.taskCompleteDate)
		let flag = 0;

		for(let i = this.cycleLength-this.startDay; i< this.cycleLength-this.startDay + this.dateRangeLength;i++) {
			if(newdate === this.cycleDateList[i]) 
				flag++;
		}
		
		if(flag > 0) 
			this.dateValidateFlag =false;
		else
			this.dateValidateFlag = true;

    }
    
    checkColor(criticalPathId,taskid,event,index){
    	let tempObj = this;
    	this.taskCompleteDate = null;
        this.addTaskCompleteForm.controls['taskCompleteDate'].setValue("");
         
//    	$('#completedt').datepicker({
//	         format:'dd/mm/yyyy',
//	         timepicker:false,
//	         autoclose: true,
//	         closeOnDateSelect : true,
//	         orientation: "bottom",
//	         todayHighlight: 1
//	     }).on("change", function(e){
//	    	 tempObj.getDate(e, tempObj);
//             $('.datepicker').hide();
//	     });
    	this.dateValidateFlag = null;
    	this.taskId = taskid;
    	this.criticalPathId = criticalPathId;
        this.statusIdForClickedPath = null;

    	this.criticalPathService.getCriticalPathOfAckStockTakeCycle(this.stockTakeCycleId).subscribe((results) => {
    		
    		this.criticalPathList = results.data; 		

        	for(let i =0 ; i< this.criticalPathList.length; i++) {
        		if(this.criticalPathList[i].id == criticalPathId) {
        			let completeddate = this.criticalPathList[i].completeDate;
        			this.name = this.criticalPathList[i].completedBy;
        			this.nameOld = this.criticalPathList[i].completedBy;
        			this.checknameUser = this.criticalPathList[i].completedBy;
        		    this.taskCompleteDate = this.datePipe.transform(completeddate, 'dd/MM/yyyy'); 			
        		    this.startDay = this.criticalPathList[i].startDay;
        		    this.dateRangeLength  =  this.criticalPathList[i].length ;
        		    this.criticalPathName = this.criticalPathList[i].title;
                    this.statusIdForClickedPath = this.criticalPathList[i].status.id;

                    let min = this.cycleDateList[this.criticalPathList[i].startDay-1];
        			let max= this.cycleDateList[this.dateRangeLength-1];
        			
        			if(this.nameOld !== null) {
        	    		  this.addTaskCompleteForm.controls['name'].disable();
        	    		  this.addTaskCompleteForm.controls['taskCompleteDate'].disable();
        	    	 } else {	
        	    		  this.addTaskCompleteForm.controls['name'].enable();
        	    		  this.addTaskCompleteForm.controls['taskCompleteDate'].enable();
        	    	}
        		}
        	}
        	let selectedDate ;
        	
    		if(index >= this.cycleLength-this.startDay && index < this.cycleLength-this.startDay + this.dateRangeLength) {
    			this.addTaskCompleteForm.controls['taskCompleteDate'].setValue(this.datePipe.transform(this.cycleDateList[index], 'dd/MM/yyyy'));
    			selectedDate = this.datePipe.transform(this.cycleDateList[index], 'dd/MM/yyyy');
    		} else {
    			this.addTaskCompleteForm.controls['taskCompleteDate'].setValue("");
    		}
    		$('#completedt').datepicker({
    		    format:'dd/mm/yyyy',
    		    timepicker:false,
    		    autoclose: true,
    		    closeOnDateSelect : true,
    		    orientation: "bottom",
    		    todayHighlight: 1
    		}).on('hide', function() {
//    			$('.datepicker').datepicker.currentDate = $('input#completedt').val(); 
    			tempObj.setSelectedDate(selectedDate, tempObj);
    		}).on("change", function(e){
    		    tempObj.getDate(e, tempObj);
    	        $('.datepicker').hide();
    		});
    		index = null;
    		this.cycleDateList[index] = null; 
    	}); 
    }
    
    setSelectedDate(date,tempObj){
    	this.addTaskCompleteForm.controls['taskCompleteDate'].setValue(date);
    }
    
    saveApproveStatus(status){
    	this.showLoader = true;
    	this.criticalPathService.saveApproveStatus(this.resultId ,this.reasonForDecline ,status ,this.userRole).subscribe((results) => {
    		this.reasonForDecline = null;
    		this.getStockTakeResult();
    		this.showLoader = false;
		});
    }
    
    saveData(){
    	
    	let criticalPathJson = {
    			'criticalPathid': this.criticalPathId,
				'statusid': this.statusId,
				'completedby':this.name,
				'taskCompleteDate': this.addTaskCompleteForm.controls['taskCompleteDate'].value,
				'storeName': this.storeName,
				'storeId' : this.storeId,
				'cycleId': this.stockTakeCycleId,
				'criticalPathName': this.criticalPathName,
		}
    	console.log("criticalPathJson");
    	console.log(criticalPathJson);
    	if(this.taskCompleteDate !== undefined && this.name!== undefined) {
    		this.showLoader = true;
    		this.criticalPathService.updateAckTaskStatus(criticalPathJson).subscribe((results) => {
    			setTimeout(() => {
    				window.location.reload();	
    				this.showLoader = false;
    			},2500);
//    			this.showLoader = false;
    		}, (error) => {
	        	this.error = JSON.parse(error._body).message;
//	        	this.buttonText = CONSTANTS.SUBMIT;
	        	
	        });
    	}
    	
    	this.name = null;
    }
    
    logout() {
    	this.brandUserService.logOut().subscribe((results)  => {
		},(error) => {
			
		});
		this.commonService.deleteCookie();
		this.router.navigateByUrl('/' + ROUTS.LOGIN);
	}
    
}