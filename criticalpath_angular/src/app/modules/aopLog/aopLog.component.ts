import {Component, OnInit,Input,Output, EventEmitter } from '@angular/core';
import { RouterModule, Routes, Router } from '@angular/router';

import * as ROUTS from '../../constants/routs';

import {CONSTANTS} from '../../constants/constant';

import { AopLogService } from '../../service/aopLog.service'; 
import {DatePipe} from '@angular/common';
declare var $: any;



declare var $: any;

@Component({
	selector :'',
	templateUrl : './aopLog.component.html'
})
export class AopLogComponent implements OnInit {
	
	constructor(public aopLogService : AopLogService ,private datePipe: DatePipe) {}

	public loggerList : any;
	public searchDate :any;
	public searchDateFormatted : any;
	public showLoader : boolean = false;

	ngOnInit() {
		let tempObj = this;
		this.showLoader = true;
		
		let todayDate =  this.datePipe.transform(new Date(), 'dd/MM/yyyy');
		this.searchDateFormatted = todayDate;
		let fromatedDate = todayDate.replace(/[^a-zA-Z0-9]/g, '-');
		this.searchDate = fromatedDate;
	 	$('#selectedDt').datepicker({
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
	 	
	 	this.searchBySelectedDate();
	 	this.showLoader = false;
	}
	
	initializeDatatable() {
		$(function () {
		    $('#aopLogTable').DataTable({
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
	
	searchBySelectedDate() {
		
		this.showLoader = true;
		if(this.searchDate !== 'undefined') {
			console.log(this.searchDate);
			this.aopLogService.searchByDate(this.searchDate).subscribe((results) => {
				this.loggerList = results.data;
				this.showLoader = false;
				console.log(this.loggerList);
			});
		}
		else
		{
			this.loggerList = [];
		}
	}
	
	searchByDate(e, tempObj){  
	
			let date = $(e.target).val();
//			let selectedDate = new Date(date);
//			let newdate = this.datePipe.transform(selectedDate, 'yyyy-MM-dd');
//			let newdate = selectedDate;
			let fromatedDate = date.replace(/[^a-zA-Z0-9]/g, '-');
			this.searchDate = fromatedDate;
			this.searchBySelectedDate();
	 }	
			
			
}