import{Component,OnInit,ElementRef,ViewChild}from'@angular/core';import{RouterModule,Routes,Router}from'@angular/router';import{FormGroup,FormControl,FormArray,FormBuilder,Validators}from'@angular/forms';

import*as ROUTS from'../constants/routs';import{CONSTANTS}from'../constants/constant';

import{CommonService}from'../service/common.service';import{StoreService}from'../service/store.service';import{CriticalPathService}from'../service/criticalPath.service';import{ClosedDateService}from'../service/closedDate.service';import{LoginRepository}from'../repository/login.repository';import{LocalStorageService}from'../service/localStorage.service';import{StockTakeCycleService}from'../service/stockTakeCycle.service';import{DatePipe}from'@angular/common';declare var $:any;

import { BrandUserService } from '../service/brandUser.service';


@Component
({templateUrl:'./ackermanViewAllBranches.component.html'
})export class AckermanViewAllBranchesComponent implements OnInit {

	storeList:any[]=[];stockTakeCycleList:any[]=[];

	stockCycleForm:FormGroup;stockCycleArray:FormArray;stockTakeResultForm:FormGroup;addDeclineformRam:FormGroup;storeId:any;userId:any;stockList=[];statusList=[];userRole:any;criticalPathList:any[]=[];cycleLength:any;daysIndex:any[]=[];stockTakeDate:any;startDate:any;cycleDateList:any[]=[];closedDateList:any[]=[];selectedStoreList:any[]=[];startDateList:any[]=[];todayDate:any;
	stockTakeCycleLength(n: number): any[] {
	    return Array(n);
	  }
    daysOfList :any[] = ["Sunday", "Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"];
    sortBy : any;
    delThisCycle : any;
    tempStoreId : any ;
    storeNameTitle : any;
	storeViewPageId : any;
	cycleViewPageId : any;
	startDateViewPage : any;
	stockDateViewPage : any; 
	userLoginRole : any;
	public showLoader : boolean = false;	
	doNotCountList : any[] = [];
    taskSequenceNumber : any[] = [];
	timeSelect : any = null;
	addStockTakeDateForm : FormGroup;
	buttonText : string;  
	stockDt : any;
	storeIdList : any[] =[];
	addNewCycleDateValidateFlag :any =0;
	  
	// Delete stock take date
	deleteStockTakeDateForm: FormGroup;
    deleteStockTakeId : any;
	isDateDirty : boolean = false;
	isTimeDirty : boolean = false;
	oldStockTake : any;
	oldTime : any;
	deleteStoreId : any;  
	  
	resultId : any;
	displayStatus : any;
	reasonForDecline :any; 
	ackCycleId : any;
	ramTime : any;
	dateValidateFlag :boolean = false;
    dateValidateFlagForNewStockDate: boolean = false;
	errorMsg : any = null;  
	  
	editStockTakeDateForm :   FormGroup;
	oldEditTime : any = null;
	isEditTimeDirty : boolean = false;
	  
	 //set 23 value pop up box variable//
	  
	  storeNo: any;
	  storeName: any;
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
	  resultDate : any;
	  
	  
	  checkBoxVal :  boolean = false;
	  
	  smReason :any;
	  dsmReason :any;
	  dsmDate:any;
	  smDate: any;
	  prevCountDateTime :any;
	  currentCountDateTime :any;
	  
	///////////////////////////////
    
    constructor(public router: Router, public commonService : CommonService,private datePipe: DatePipe, 
    			public closedDateService : ClosedDateService,
    			public localStorageService : LocalStorageService, public brandUserService : BrandUserService,
    			public loginRepository : LoginRepository,  public criticalPathService : CriticalPathService,
    			public storeService : StoreService, public stockTakeCycleService : StockTakeCycleService, private fb: FormBuilder) {
    	
    }
    
    ngOnInit() {
    	let tempObj = this;
    	this.buttonText = "SUBMIT STOCK TAKE DATE";
//    	$('#completedt').datepicker({
//	         format:'mm/dd/yyyy',
//	         timepicker:false,
//	         autoclose: true,
//	         closeOnDateSelect : true,
//	         orientation: "bottom",
//	         todayHighlight: 1
//	     }).on("change", function(e){
//	    	 $('.datepicker').hide();
//	    	 tempObj.getDate(e, tempObj);
//	     });
    	
    	this.stockTakeResultForm = new FormGroup({
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
        });
    	
    	this.addDeclineformRam = new FormGroup({
//    		smReason: new FormControl({value: ''}),
//    		dsmReason: new FormControl({value: ''}),
    		ramReason: new FormControl('', [Validators.required]),
    		newStockDateRam:new FormControl('', [Validators.required]),
    		newTimeRam: new FormControl('', [Validators.required]),
        });
    	
    	this.addStockTakeDateForm = new FormGroup({
			taskCompleteDate: new FormControl('', [Validators.required]),
			newTime : new FormControl('', [Validators.required])
        });
    	
    	this.userLoginRole = this.localStorageService.getItem(CONSTANTS.USER_ROLE);
    	
    	this.deleteStockTakeDateForm = new FormGroup({
    		newStockDate: new FormControl('', [Validators.required]),
  		  	newTimeForDel: new FormControl('', [Validators.required])
    	});
    	
    	this.editStockTakeDateForm = new FormGroup({
  		  	newTimeForEdit: new FormControl('', [Validators.required])
    	});
    	
    	this.showLoader = true;
    	
    	let todayDt =  new Date();
        this.todayDate = this.datePipe.transform(todayDt, 'yyyy-MM-dd'); 
    	this.tempStoreId = -1;
    	
    	this.sortBy = 1;
    	this.stockCycleForm = this.fb.group({
    		stockCycleArray: this.fb.array([])
    	});
    	
		this.loginRepository.getUser().subscribe((results) => {
			if (results != undefined && results.role.length != 0) {
				this.userRole = results.role;
				console.log(results);
				this.getAllActiveStoresPerDsmUser(results.id, results.role, this.sortBy);
//        		if(results.role == 'ROLE_RAM_USER')
//        		{
//        			this.getAllActiveStoresPerDsmUser(results.id, results.role);
//        		}
//        		else if(results.role == 'ROLE_DSM_USER')
//        		{
//        			this.getAllActiveStoresPerDsmUser(results.id, results.role);
//        		}
        	}
        },(error) => {
        });
		
//		 $('#deleteStockDate').datepicker({
//		        format:'dd/mm/yyyy',
//		        timepicker:false,
//		        autoclose: true,
//		        closeOnDateSelect : true,
//		        orientation: "bottom",
//		        todayHighlight: 1
//		    }).on("change", function(e){
//		   	 $('.datepicker').hide();
//		   	 tempObj.getNewDate(e);
//		 });
		 
		 $('#ramStockDate').datepicker({
		        format:'dd/mm/yyyy',
		        timepicker:false,
		        autoclose: true,
		        closeOnDateSelect : true,
		        orientation: "bottom",
		        todayHighlight: 1
		    }).on("change", function(e){
		   	 $('.datepicker').hide();
		   	 tempObj.getNewDateRam(e);
		 });
    	
    }
    
    addNewStockDate(){
    	
    	this.dateValidateFlagForNewStockDate = false;
    	this.timeSelect =null;
    	this.stockDt = null;
    	this.addStockTakeDateForm.controls['newTime'].setValue(null);
    	
    	let tempObj =this;
    	$('#completedt').datepicker({
	         format:'dd/mm/yyyy',
	         timepicker:false,
	         autoclose: true,
	         closeOnDateSelect : true,
	         orientation: "bottom",
	         todayHighlight: 1
	     }).on("change", function(e){
	    	 $('.datepicker').hide();
	    	 tempObj.getNewStockTakeDate(e, tempObj);
	     });
    }

    getNewStockTakeDate(e, tempObj){  
    		var date = $(e.target).val();
    		let stockDate = new Date(date);
    		
             let dateSplitted = date.split('/')[1]+"-"+date.split('/')[0]+"-"+date.split('/')[2];
	         this.stockDt = date;
	     	let roomListData = {};
	     	let listOfCycle = [];
			for( let indexOfCycle = 0; indexOfCycle < this.selectedStoreList.length; indexOfCycle++ ) {
				listOfCycle.push(this.selectedStoreList[indexOfCycle]);
                listOfCycle[indexOfCycle].stockTakeDateFormatted = dateSplitted;
			}
	    	 this.stockTakeCycleService.calculateMultipleStartDate(listOfCycle).subscribe((results) => {
	    		 this.startDateList = results.data;
	    	 })
	    	 let msSelectedDate = Date.UTC(new Date(dateSplitted).getFullYear(), new Date(dateSplitted).getMonth()+1, new Date(dateSplitted).getDate());
	    	 let msMinStockTakeDate = Date.UTC( new Date(this.resultDate).getFullYear(),  new Date(this.resultDate).getMonth()+1,  new Date(this.resultDate).getDate());
	    	    let flag = 0;
	    	    if (msSelectedDate < msMinStockTakeDate) {
	    	    	flag++;
	    	    }
	    	    else {	
	    	    	
	    	    }
	    		
	    		if(flag > 0) 
	    			this.dateValidateFlagForNewStockDate =true;
	    		else
	    			this.dateValidateFlagForNewStockDate = false;
	    	 
    }
    
    getAllStockCycles(role, sortBy, j){
    	
    	this.showLoader = true;
    	
        if(j === 1)
        {
            this.tempStoreId = this.storeId;
        }
        else if(j === 0)
        {
            this.storeId = this.tempStoreId;
        }

        this.sortBy = sortBy;
    	if(this.storeId == 0)
    	{
    		//Fetch all stores and cycles 
    		this.stockTakeCycleService.getAllActiveStockTakeCyclePerStore().subscribe((results) => {
    			this.stockTakeCycleList = results.data;
    			this.displayStockCyclesToForm();
    		}, (error) => {
    		});
    	}
    	else if(this.storeId == -1)
    	{
    		//Fetch all stores and cycles for dsm user
    		//Need to correct query here
    		this.stockTakeCycleList = [];
    		this.statusList = [];
    		this.stockList = [];
    		this.stockTakeCycleService.getAllActiveAckStockTakeCycleDsmUser(this.userId, role, this.sortBy, this.storeId).subscribe((resultsDsmUser) => {
    			this.stockTakeCycleList = resultsDsmUser.data;
                
                this.arrangeData(this.stockTakeCycleList);
    			this.displayStockCyclesToForm();
    			
    		}, (error) => {
    		});
    	}
    	else if(this.storeId != -1 && this.storeId != 0)
    	{
    		//Fetch selected store cycles 
    		this.stockTakeCycleList = [];
    		this.statusList = [];
    		this.stockList = [];
    		let tempStoreId = this.storeId;
            this.stockTakeCycleService.getAllActiveAckStockTakeCycleDsmUser(this.userId, role, this.sortBy, this.storeId).subscribe((results) => {
            	this.stockTakeCycleList = results.data;

    			this.arrangeData(this.stockTakeCycleList);
        		this.displayStockCyclesToForm();
    		}, (error) => {
    			
    		});
    	}
    	
    	this.storeId = -1;
    }
    
    getAllActiveStoresPerDsmUser(id, role, sortBy) {
    	this.stockTakeCycleService.getAllActiveStoresPerDsmUser(id, role).subscribe((results) => {
    		this.storeList = results.data;
			
			this.storeId = -1;
			this.userId = id;
			this.getAllStockCycles(role, sortBy, 2);
		}, (error) => {
		});
    }
    
    displayStockCyclesToForm() {
    	if(this.stockList.length > 0) {
			let stockCycleFormConst = (<FormArray>this.stockCycleForm.controls['stockCycleArray']);
			for( let i=0; i< this.stockList.length; i++ ) {
				stockCycleFormConst.push(this.createItem());
				stockCycleFormConst.controls[i].patchValue(this.stockList[i]);
			}
    	}
    	this.showLoader = false;
    }

    arrangeData(list : any[][]) {
    	let rowData = [];
        console.log("list");
        console.log(list);
    	
        // console.log(status);

    		for(let i = 0; i < list.length; i++) {
                
                let pendingCount = 0;
                let completeCount = 0;
                let taskCount = 0;
        		
          //       for(let j = 0; j < status.length; j++) {
        		// 	if(list[i].cycleId == status[j].cycleId) {

        		// 		taskCount = taskCount + 1;
                        
        		// 		if(status[j].status === "Completed" || status[j].status === "Complete") {
        		// 			completeCount++;
                            
        		// 		}

        		// 	}
        		// }
       		    // let takeDate = new Date(list[i][4]);
                rowData.push(list[i][2]);
            		console.log("storeName  "+list[i][2]);
        		rowData.push(list[i][3]);
        			console.log("days  "+list[i][3]);
        		if(list[i][4] == null) {
        			rowData.push("");
        		}	else {
        			rowData.push(list[i][4]);
        		}
        	
        		rowData.push(list[i][5]);
        			console.log("Tasks  "+list[i][5]);
        			
                let compTask = parseInt(list[i][5].toString().substr(0,3));
                let totTask = parseInt(list[i][5].toString().substr(5));
                let days = parseInt(list[i][3].toString());
                let pedTask = totTask - compTask;
                	console.log(compTask+"__"+pedTask+"__"+totTask+"__"+days);
                

           		if(days !=null && days == 0) {
           			rowData.push("Stock Take");
           		}
                else if(days !=null && days == 0 && pedTask > 0) {
                   rowData.push("Stock Take");
                }
                else if(days !=null && days > 15) {
                   rowData.push("Upcoming");
                }
           		else if(days !=null && days > 0 && compTask < totTask) {
           			rowData.push("In Progress");
           		}
           		else if(days !=null && days < 0 && pedTask > 0) {
           			rowData.push("Due");
           		}
                else if(days !=null && pedTask == 0) {
                   rowData.push("Completed");
                }
                else {
                   rowData.push("");
                }
                
        		rowData.push(list[i][1]);
        		rowData.push(list[i][0]);
        		rowData.push(list[i][6]);
        		rowData.push(list[i][7]);
                rowData.push(list[i][8]);
        		this.stockList.push(rowData);
        		rowData = [];
        		taskCount = 0;
        		pendingCount = 0;
        	}
    }
    
    createItem(): FormGroup {
		  return this.fb.group({
			storeName: '',
			cycleName: '',
			days: '',
			takeDate: '',
			taskComplete: '',
			status: '',
		    id: '',
		  });
		}
    
    getAllActiveStores(){
    	this.storeService.getAllActiveStore().subscribe((results) => {
			this.storeList = results.data;
			
			this.storeId = 0;
			this.getAllStockCycles(this.userRole, this.sortBy, 2);
		}, (error) => {
			
		});
    }

    hidePopBox(){ 
		this.cycleDateList = [];
        let popBoxCss = document.getElementById('myModal');
        /*popBoxCss.className = "modal fade critical_path_modal hide";*/
    }
    
    displayFullPage(){
    		this.router.navigateByUrl("/ackermanPageview/ackermanCpComponent/"+this.cycleViewPageId+"/"+this.startDateViewPage+"/"+this.storeViewPageId+"/"+this.stockDateViewPage);
    }
    
    setPopBox(storeId, storeName, cycleId){
    	this.errorMsg = null;
    	this.storeViewPageId = storeId;
    	this.cycleViewPageId = cycleId;
    	this.showLoader = true;
        let popBoxCss = document.getElementById('myModal');
       /* popBoxCss.className = "modal fade critical_path_modal show";*/
        this.storeNameTitle = storeName;
    	
    	this.criticalPathList =[];
    	this.criticalPathService.getCriticalPathOfAckStockTakeCycle(cycleId).subscribe((results) => {
    		let allPathList = [];
    		
    		allPathList = results.data;
    		console.log(allPathList);
    		for(let i =0; i< allPathList.length;i++) {

    			if(allPathList[i].status.id == 3) {
                    this.taskSequenceNumber.push(i);
    				this.criticalPathList.push(allPathList[i]);
    				console.log("In progress loop: "+i);
    			}
    		}
    		console.log(this.criticalPathList);
    	});
    	
    	
    	this.stockTakeCycleService.getAckStockTakeCycleData(cycleId).subscribe((results) => {
    		
    		this.cycleLength = parseInt(results.data.length);
    		console.log(results.data);
    		let strat = results.data.stokeStartDate;
    		let stock =  results.data.stokeTakeDate; 
    		this.stockTakeDate = new Date(stock);
    		console.log(this.stockTakeDate);

    	   let dd1 = this.stockTakeDate.getDate();
           let day1 =  this.stockTakeDate.getDay();
           let mm1 = this.stockTakeDate.getMonth()+ 1;
           let y1 = this.stockTakeDate.getFullYear();          

           let stockdt =  mm1 + '-' + dd1 + '-' + y1 ;  
           console.log(stockdt);
           this.stockDateViewPage = stockdt;
           let startDate1 = new Date(strat);
   	
          let dd = startDate1.getDate();
          let day =  startDate1.getDay();
          let mm = startDate1.getMonth()+ 1;
          let y = startDate1.getFullYear();          

           let startdt =  mm + '-' + dd + '-' + y ;
           this.startDateViewPage = startdt;
           
           this.stockTakeCycleService.getAllDateList(cycleId , stockdt).subscribe((results) => {
      		 console.log(results.data);
      		 this.cycleDateList = results.data;
      		this.showLoader = false;
      	 })
    	     
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
//    		let strat = results.data.stokeStartDate;
//    		let stock =  results.data.stokeTakeDate; 
//    		console.log(results.data);
//    		
//    		this.stockTakeDate = new Date(stock);
//    		this.startDate =  new Date(stock);
//    	
//    		console.log(this.stockTakeDate);
//    		
//    		this.startDate.setDate(this.startDate.getDate() - (this.cycleLength -1)); 
//    		console.log(this.startDate);
//	    	let start  = this.startDate;
//	    	let stockDate = this.stockTakeDate;
//	    	let totalSundays = 0;
//	    	for(let j = start; j <= stockDate;){
//	        	for(let i = 0; i< this.daysIndex.length;i++) {
//	        		 if (j.getDay() == this.daysIndex[i]){
//	        			 totalSundays++
//		        	 }
//	        	 }
//	        	 j.setTime(j.getTime() + 1000*60*60*24);
//	         }
//	         	         
//	         this.startDate.setDate( this.startDate.getDate() - (this.cycleLength -1) -1 - (totalSundays));
//    		
//
//	         //set date format for database
//	        let dd = this.startDate.getDate();
//            let day =  this.startDate.getDay();
//            let mm = this.startDate.getMonth()+ 1;
//            let y = this.startDate.getFullYear();          
//
//            let startdt =  y + '-' + mm + '-' + dd ;
//            
//            let dd1 = this.stockTakeDate.getDate();
//            let day1 =  this.stockTakeDate.getDay();
//            let mm1 = this.stockTakeDate.getMonth()+ 1;
//            let y1 = this.stockTakeDate.getFullYear();          
//
//            let stockdt =  y1 + '-' + mm1 + '-' + dd1 ;      
//            //set date format for database
//            
//	         this.closedDateService.getClosedDateByStoreDate(storeId, startdt, stockdt).subscribe((results) => {
//	        	 let closeddatelength = results.data;
//	        	 this.startDate.setDate(this.startDate.getDate() - closeddatelength); 
//		         
//		         let startDate  = this.startDate;
//		         let totalDonotCount = 0;
//		         for(let j = startDate; j <= this.stockTakeDate;){
//		        	 for(let i = 0; i< this.daysIndex.length;i++) {
//		        		 if (j.getDay() == this.daysIndex[i]){
//		        			 totalDonotCount++
//			        	 }
//		        	 }
//		        	 j.setTime(j.getTime() + 1000*60*60*24);
//		         }
//		         
//		         this.startDate.setDate(this.startDate.getDate() - (this.cycleLength -1) -1 - (totalDonotCount) -closeddatelength);
//		         
//		         let startDate1 = new Date(this.startDate);
//	    		 let stockTakeDate1 = new Date(this.stockTakeDate);
//	     	
//	        	 dd = startDate1.getDate();
//	             day =  startDate1.getDay();
//	             mm = startDate1.getMonth()+ 1;
//	             y = startDate1.getFullYear();          
//
//	             let startdt =  y + '-' + mm + '-' + dd ;
//	             this.startDateViewPage = startdt;
//	            
//	             let dd1 = stockTakeDate1.getDate();
//	             let day1 =  stockTakeDate1.getDay();
//	             let mm1 = stockTakeDate1.getMonth()+ 1;
//	             let y1 = stockTakeDate1.getFullYear();          
//
//	             let stockdt =  y1 + '-' + mm1 + '-' + dd1 ;
//	             this.stockDateViewPage = stockdt;
//
//	             this.closedDateService.getClosedDateListByStoreDate(storeId , startdt, stockdt).subscribe((results) => {
//
//	            	 for(let i =0; i<results.data.length;i++) {
//	            		 this.closedDateList.push(new Date(results.data[i]));
//	            	 }
//	            	 this.getCycleDate();
//	             });
//	         });
	         
    	});
    }
    
    getCycleDate(){
    	
    	let totalDonotCount =0;
    	let doNotCountList  = [];
    	
    	let startDate;
    	let stockTakeDate;
    	
    	startDate = new Date(this.startDate);
   	 	stockTakeDate = new Date(this.stockTakeDate);

//        startDate = new Date(this.startDate);
         
    	for(let j = startDate; j <= stockTakeDate;){
    		
//	       	 if (j.getDay() == this.daysIndex){
//	       		doNotCountList.push(new Date(j));
//	       	 }
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
    	 
    	 this.cycleDateList = [];
    	 
    	for(let jj = startDate; jj <= stockTakeDate;){
    			this.cycleDateList.push(new Date(jj));
    			jj.setTime(jj.getTime() + 1000*60*60*24);
    	}
    	
      	for(let jj = 0; jj < this.cycleDateList.length; jj ++){
      		for(let i =0; i < totalNotCountListAsc.length;i++) {     
      			 if (this.cycleDateList[jj].toString().substr(0,15) === totalNotCountListAsc[i].toString().substr(0,15)){
      				this.cycleDateList.splice(jj,1);      				
               	 }
      		 }
        }
      	this.showLoader = false;
    }
    
    sortStoreCycles(){
    	// console.log("this.sortBy: "+this.sortBy);
    }

    hideCycle(){
        // console.log("deleteCycle");

        // this.stockTakeCycleService.hideStockTakeCycleinAckerman(this.delThisCycle).subscribe((results) => {
        //     console.log("Success");

        //     this.loginRepository.getUser().subscribe((results) => {
        //     if (results != undefined && results.role.length != 0) {
        //         this.userRole = results.role;
        //         this.getAllActiveStoresPerDsmUser(results.id, results.role, this.sortBy);
        //     }
        // },(error) => {
        // });

        // }, (error) => {
        //     console.log("error");
        // });

    }

    getDelCycleId(id){
        // console.log("getCycleId: "+id);
        this.delThisCycle = id;
    }
    
    selectCheckBox(e,storeId, storeName, cycleId ,cycleStockTakeDate){
    	this.errorMsg = null;
    	this.showLoader = true;
    	if(e.target.checked == true) {
    		this.selectedStoreList.push({
    	        storeId: storeId,
    	        storeName: storeName,
    	        cycleId : cycleId,
    	        takeDate : this.datePipe.transform(cycleStockTakeDate, 'MM/dd/yyyy')
    	      });   	
    		this.storeIdList.push(storeId);
    	}else {
    		for(let i =0; i< this.selectedStoreList.length;i++) {
    			if(storeId == this.selectedStoreList[i].storeId && storeId == this.storeIdList[i]) {
    				this.selectedStoreList.splice(i,1);
    				this.storeIdList.splice(i,1);
    			}
    		}
    	}
    	if(this.storeIdList.length > 0) {
    		this.stockTakeCycleService.getStoreStockTakeDate(this.storeIdList).subscribe((results) => {
       		 console.log(results.data);
       		 if(results.data.length == 1 ) {
       			 this.resultDate = results.data[0];
       			 this.addNewCycleDateValidateFlag = 0;
       		 } else {
       			 this.addNewCycleDateValidateFlag = 1;
       		 }
       		 this.showLoader = false;
       	 });
    	} else {
    		 this.addNewCycleDateValidateFlag = 0;
    		 this.showLoader = false;
    	}
    	
    }
    
    selectAllCheckBox(e,stockList){
    	this.showLoader = true;
    	this.errorMsg = null;
    	this.selectedStoreList=[];
    	this.storeIdList =[];
        this.addNewCycleDateValidateFlag = 0;
    	 
    	stockList.forEach(x => this.selectedStoreList.push({
	        storeId:  x[5],
	        storeName: x[0],
	        cycleId : x[6],
	        takeDate : this.datePipe.transform(x[2], 'MM/dd/yyyy')
	      })
    	);
    	
    	for(let i=0;i<stockList.length;i++) {

    		 (<HTMLInputElement>document.getElementById("chkBx["+i+"]")).checked = true;
    		
    	}
    	
    	for(let j=0;j< this.selectedStoreList.length;j++) {
    		this.storeIdList.push(this.selectedStoreList[j].storeId);
    	}
    	if(this.storeIdList.length > 0) {
    		this.stockTakeCycleService.getStoreStockTakeDate(this.storeIdList).subscribe((results) => {
       		 if(results.data.length == 1 ) {
                 this.resultDate = results.data[0];
       			 this.addNewCycleDateValidateFlag = 0;
       		 } else {
       			 this.addNewCycleDateValidateFlag = 1;
       		 }
       		 this.showLoader = false;
       	 });
    	} else {
    		 this.addNewCycleDateValidateFlag = 0;
    		 this.showLoader = false;
    	}
    	
    }
    
    checkForm(timeSelect1){
    	this.timeSelect = timeSelect1;
    	console.log(this.timeSelect);
    }
    
    closeModel(){
    	this.timeSelect =null;
    	this.stockDt = null;
    	this.addStockTakeDateForm.controls['newTime'].setValue(null);
    	this.dateValidateFlagForNewStockDate = false;
    }
    
    closeDelModel(){
    	this.deleteStockTakeDateForm.controls['newStockDate'].setValue(null);
    	this.deleteStockTakeDateForm.controls['newTimeForDel'].setValue(null);
    	this.dateValidateFlag =false;
    }
    
    closeEditModel(){
    	this.editStockTakeDateForm.controls['newTimeForEdit'].setValue(null);
    }
    
    closeDsmDeclineModel(){
    	this.reasonForDecline = null;
    }
    closeRamDeclineModel(){
    	this.dsmReason = null;
    	this.smReason = null;
    	this.addDeclineformRam.controls['ramReason'].setValue(null);
    	this.addDeclineformRam.controls['newStockDateRam'].setValue(null);
    	this.addDeclineformRam.controls['newTimeRam'].setValue(null);
    }
    
    saveStockTakeDate(){
    	
    	this.errorMsg = null;
    	this.showLoader = true;
    	
    	this.buttonText = 'SUBMITTING...';
    	for(let i =0; i< this.startDateList.length;i++) {
    		this.startDateList[i].push(this.timeSelect);
    	}
     	 this.stockTakeCycleService.addMultipleStartDate(this.startDateList).subscribe((results) => {
     		console.log("add cycle resutls..");
     		 console.log(results);
     		 this.errorMsg = results.data;
    		 let j=0;
    		 this.getAllStockCycles(this.userLoginRole, this.sortBy, j);
    		 this.selectedStoreList = [];
    		 this.storeIdList =[];
    		 this.addStockTakeDateForm.controls['newTime'].setValue(null);
         	 this.timeSelect = null;
         	 this.stockDt = null;
    	 })
    }

    logout() {
    	this.brandUserService.logOut().subscribe((results)  => {
		},(error) => {
			
		});
		this.commonService.deleteCookie();
		this.router.navigateByUrl('/' + ROUTS.LOGIN);
	}

	// Delete and create new stock take date
	delStockTakeCycle(date : any, time : any, id : any , storeId:any) {
        
        this.deleteStoreId  =storeId;
    	this.errorMsg = null;
    	this.oldStockTake = this.datePipe.transform(date, 'dd/MM/yyyy');
    	this.oldTime = time;
    	let tempDateObj = this;
    	this.deleteStockTakeId = id;
//    	this.deleteStockTakeDateForm.controls['newStockDate'].setValue(this.oldStockTake);
    	this.dateValidateFlag =false;
    	this.deleteStockTakeDateForm.controls['newStockDate'].setValue(null);
    	this.deleteStockTakeDateForm.controls['newTimeForDel'].setValue(null);
//    	this.addDeclineformRam.controls['newTime'].setValue("");
    	 
    	 $('#deleteStockDate').datepicker({
		        format:'dd/mm/yyyy',
		        timepicker:false,
		        autoclose: true,
		        closeOnDateSelect : true,
		        orientation: "bottom",
		        todayHighlight: 1
		   }).on("change", function(e){
		   	 $('.datepicker').hide();
		   	tempDateObj.getNewDateForDel(e);
		 });

         // $('#deleteStockDate').datepicker({
         //        autoclose: true,
         //        format: 'dd/mm/yyyy',
         //      }).on("change", function(e){
         //        // $('.datepicker').hide();
         //       tempDateObj.getNewDateForDel(e);
         // });
         
    	 
    	 
//    	if(time == 'AM') 
//    		this.deleteStockTakeDateForm.controls['newTime'].setValue('1');
//    	else
//    		this.deleteStockTakeDateForm.controls['newTime'].setValue('0');
    }
    
    validTImeForDelOperation(time : any){
    	
    		if(this.oldTime !== time)
    			this.isTimeDirty = true;
    		else
    			this.isTimeDirty = false;

//            this.getNewDateForDel(this.oldStockTake);
    }
    
    editStockTakeCycle(date : any, time : any, id : any , storeId:any) {
        
        this.deleteStoreId  =storeId;
    	this.oldEditTime = time;
    	let tempDateObj = this;
    	this.deleteStockTakeId = id;
    	 
    	if(time == 'AM') 
    		this.editStockTakeDateForm.controls['newTimeForEdit'].setValue('1');
    	else
    		this.editStockTakeDateForm.controls['newTimeForEdit'].setValue('0');
    }
    
    validTimeForEditOperation(time : any){
    	
		if(this.oldEditTime !== time)
			this.isEditTimeDirty = true;
		else
			this.isEditTimeDirty = false;

    }
    
    getNewDateForDel(e) {
    	
    	if(this.oldStockTake !== e.target.value) {
    		this.isDateDirty = true;
    		this.deleteStockTakeDateForm.controls['newStockDate'].setValue(e.target.value);
    		var selectedDate = $(e.target).val();
    		
    		let dataList=[];
    		dataList.push(this.deleteStockTakeId);
    		dataList.push(selectedDate);
    		dataList.push(this.deleteStoreId);
    		
    		this.stockTakeCycleService.calculateStartDt(dataList).subscribe((results) => {
	    		console.log("results..."+results.data);
	    		let startDate = results.data;
	    		this.dateValidateFlag =false;
	    		
	    		let msDateStart = Date.UTC(new Date(startDate).getFullYear(), new Date(startDate).getMonth()+1, new Date(startDate).getDate());
	    	    let msDateToday = Date.UTC( new Date().getFullYear(),  new Date().getMonth()+1,  new Date().getDate());
	    	    
	    	    if (msDateStart <= msDateToday) {
	    	    	console.log("iff");
                    this.dateValidateFlag =true;
	    	    } else {
	    	    	console.log("else");
                    this.dateValidateFlag =false;
	    	    }
	    	});
    	}
        else
        {
    		this.isDateDirty = false;
            this.dateValidateFlag =true;
        }
    }
    
    getNewDateRam(e) {
    	
    		this.addDeclineformRam.controls['newStockDateRam'].setValue(e.target.value);
    
    }
    
    saveDeletedForm() {
    	    this.showLoader = true;
    		this.errorMsg = null;
            this.addNewCycleDateValidateFlag = 0;
	    	let time;
	    	if(this.deleteStockTakeDateForm.controls['newTimeForDel'].value == 1)
				time =  'AM'; 
			else
				time =  'PM'; 
	    	
	    	let stockTakeJson = {
	    			'newStockDate': this.deleteStockTakeDateForm.controls['newStockDate'].value,
	    			'newTime' : time,
	    			'id' : this.deleteStockTakeId,
			}
	    	
	    	this.stockTakeCycleService.deleteAndCreateNewStockTake(stockTakeJson).subscribe((results) => {
	    		this.errorMsg = results.data;
	    		let j=0;
	    		 this.getAllStockCycles(this.userLoginRole, this.sortBy, j);
	    	});
	    	this.deleteStockTakeDateForm.controls['newStockDate'].setValue("");
	    	this.deleteStockTakeDateForm.controls['newTimeForDel'].setValue("");
    }
    
    saveEditedForm(){
    	 this.showLoader = true;
 		 this.errorMsg = null;
         this.addNewCycleDateValidateFlag = 0;
	    	let time;
	    	if(this.editStockTakeDateForm.controls['newTimeForEdit'].value == 1)
				time =  'AM'; 
			else
				time =  'PM'; 
	    	
	    	let editStockTakeJson = {
	    			'newEditTime' : time,
	    			'id' : this.deleteStockTakeId,
			}
	    	this.stockTakeCycleService.editNewStockTake(editStockTakeJson).subscribe((results) => {
//	    		this.errorMsg = results.data;
	    		this.isEditTimeDirty = false;
	    		let j=0;
	    		this.getAllStockCycles(this.userLoginRole, this.sortBy, j);
	    		this.showLoader = false;
	    	});
	    	this.editStockTakeDateForm.controls['newTimeForEdit'].setValue("");
    }
    
    getStockTakeResult(storeId , displayStatuss , cycleId){
    	this.displayStatus = displayStatuss;
    	this.ackCycleId = cycleId;
    	this.criticalPathService.getStockTakeResult(storeId).subscribe((results) => {
			this.resultId = results.data.id;
			if(results.data.BranchCode != null) {
				this.storeNo = results.data.BranchCode;
//				this.stockTakeResultForm.controls['storeNo'].setValue(results.data.BranchCode);
			}else {
				this.storeNo = "";
//				this.stockTakeResultForm.controls['storeNo'].setValue("");
			}
			
			if(results.data.BranchName != null) {
				this.storeName = results.data.BranchName;
//				this.stockTakeResultForm.controls['storeName'].setValue(results.data.BranchName);
			}else {
				this.storeName = "";
//				this.stockTakeResultForm.controls['storeName'].setValue("");
			}
			
			if(results.data.StockCountDate != null) {
				this.currentCountDate = results.data.StockCountDate;
			}else {
				this.currentCountDate = "";
			}
			
			if(results.data.PrevStockCountDate != null) {
				this.prevoiusCountDate = results.data.PrevStockCountDate;
			}else {
				this.prevoiusCountDate = "";
			}
			console.log(results.data.ResultDate);
			if(results.data.ResultDate != null) {
				this.calculatedDate = results.data.ResultDate;
			}else {
				this.calculatedDate = "";
			}
			
			if(results.data.ApproxMonths != null) {
				this.approxMonth = results.data.ApproxMonths;
			}else {
				this.approxMonth = "";
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
			
			
			if(results.data.reason_dsm != null) {
//				this.addDeclineformRam.controls['dsmReason'].setValue(results.data.reason_dsm);
				this.dsmReason = results.data.reason_dsm;
			}else {
//				this.addDeclineformRam.controls['dsmReason'].setValue("");
				this.dsmReason = "";
			}
			
			if(results.data.reason_sm != null) {
				this.smReason = results.data.reason_sm;
//				this.addDeclineformRam.controls['smReason'].setValue(results.data.reason_sm);
			}else {
				this.smReason = "";
//				this.addDeclineformRam.controls['smReason'].setValue("");
			}
			
			if(results.data.date_dsm != null) {
				this.smDate = results.data.date_dsm;
			}else {
				this.smDate = "";
			}
			
			if(results.data.date_sm != null) {
				this.dsmDate = results.data.date_sm;
			}else {
				this.dsmDate = "";
			}
			
			 this.prevCountDateTime = results.data.PrevStockCountDateTime;
			 this.currentCountDateTime = results.data.StockCountDateTime;
	
    	})
    }
    
    checkTimeRam(time){
    	this.ramTime = time;
    }
    
    saveApproveStatus(status){
    	this.showLoader = true;
    	this.errorMsg = null;
    	this.criticalPathService.saveApproveStatus(this.resultId ,this.reasonForDecline ,status ,this.userLoginRole).subscribe((results) => {
    	this.reasonForDecline = null;
    	this.showLoader = false;
    	this.errorMsg = results.data;
    	let j=0;
		 this.getAllStockCycles(this.userLoginRole, this.sortBy, j);
		});
    }

	saveDeclineStatusRAm(){
    	 this.showLoader = true;
		 this.errorMsg = null;
    	let ramDeclineJson = {
    			'resultId': this.resultId,
    			'ackCycleId' : this.ackCycleId,
    			'ramTime' : this.ramTime,
    			'stockTakeDate' : this.addDeclineformRam.controls['newStockDateRam'].value,
    			'ramReason' : this.addDeclineformRam.controls['ramReason'].value
		}
    	this.criticalPathService.saveRamDeclineStatus(ramDeclineJson).subscribe((results) => {
    		this.errorMsg = results.data;
    		this.resultId = null;
    		this.ackCycleId = null;
    		this.ramTime = null;
    		this.addDeclineformRam.controls['ramReason'].setValue("");
    		this.addDeclineformRam.controls['newStockDateRam'].setValue("");
    		this.showLoader = false;
    		let j=0;
    		this.getAllStockCycles(this.userLoginRole, this.sortBy, j);
		});
    }
}