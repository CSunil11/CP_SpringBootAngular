import {Component, OnInit,Input,Output, EventEmitter } from '@angular/core';
import { RouterModule, Routes, Router } from '@angular/router';

import * as ROUTS from '../../constants/routs';

import {CONSTANTS} from '../../constants/constant';

import { ApiLogService } from '../../service/apiLog.service'; 
import {DatePipe} from '@angular/common';
declare var $: any;



declare var $: any;

@Component({
	selector :'',
	templateUrl : './apiLog.html'
})
export class ApiLogComponent implements OnInit {
	
	constructor(public apiLogService : ApiLogService ,private datePipe: DatePipe) {}

	public searchString : any;
	public loggerList : any;
	public searchDate :any;
	public showLoader : boolean = false;
	
	ngOnInit() {
		let tempObj = this;
	 	$('#completedt').datepicker({
	         format:'dd/mm/yyyy',
	         timepicker:false,
	         autoclose: true,
	         closeOnDateSelect : true,
	         orientation: "bottom",
	         todayHighlight: 1
//	         datesDisabled:['02/02/2019', '02/03/2019']
	     }).on("change", function(e){
	    	 $('.datepicker').hide();
	    	 tempObj.searchByDate(e, tempObj);
	     });
	}
	
	initializeDatatable() {
		$(function () {
		    $('#apiLogTable').DataTable({
		    	"columnDefs": [{
		            "orderable": false,
		            "targets": [0,1,2]
		        }],
		      'paging'      : true,
		      'pageLength'  : 10,
		      'lengthChange': true,
		      'autoWidth'   : true,
		      'searching'   : false,
		      'destroy'     : true
		    })
		  });
	}
	
	searchByTransOrMsgId() {
		this.showLoader = true;
//		if(this.searchString !== '' && this.searchString !== '.' && this.searchDate !== 'undefined') {
		if(this.searchString === '' && this.searchDate === undefined) {
			console.log("Not Validating....");
		}
		else
		{
			if(this.searchString === null || this.searchString === '')
				this.searchString = null;
				
			console.log("Validating....");
			this.apiLogService.searchByTransOrMsgId(this.searchString , this.searchDate).subscribe((results) => {
				this.loggerList = results.data;
				this.showLoader = false;
				console.log(this.loggerList);
			});
		}
		
		if(this.searchString == '') {
			this.loggerList = [];
		}
	}
	
	searchByDate(e, tempObj){  
			let date = $(e.target).val();
			let selectedDate = new Date(date);
//			let newdate = this.datePipe.transform(selectedDate, 'yyyy-MM-dd');
			let fromatedDate = date.replace(/[^a-zA-Z0-9]/g, '-');
			this.searchDate = fromatedDate;
			this.searchByTransOrMsgId();
	 }	
			
			
}