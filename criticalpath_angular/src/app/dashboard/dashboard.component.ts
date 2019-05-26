import {Component,OnInit,ElementRef, ViewChild} from '@angular/core';
import { RouterModule, Routes, Router } from '@angular/router';

import * as ROUTS from '../constants/routs';
import { CONSTANTS } from '../constants/constant';

import { CommonService } from '../service/common.service';

@Component({
    templateUrl :'./dashboard.component.html'
})
export class DashboardComponent implements OnInit {
    
    constructor(public router: Router, public commonService : CommonService) {  
    
    }
    
    ngOnInit() {
    	
    	if(!this.commonService.checkCookie(CONSTANTS.COOKIE_ACCESSTOKEN))
			this.router.navigateByUrl('/' + ROUTS.LOGIN);
    	
    }
    
}