import {Component,OnInit,ElementRef, ViewChild} from '@angular/core';
import { RouterModule, Routes, Router } from '@angular/router';

import * as ROUTS from '../constants/routs';
import { CONSTANTS } from '../constants/constant';

import { CommonService } from '../service/common.service';
import { BrandUserService } from '../service/brandUser.service';

@Component({
    templateUrl :'./ackermanIndex.component.html'
})
export class AckermanComponent implements OnInit {
    
    constructor(public router: Router, public commonService : CommonService, public brandUserService : BrandUserService) {
    }
    
    ngOnInit() {
    }
    
    logout() {
    	this.brandUserService.logOut().subscribe((results)  => {
		},(error) => {
			
		});
		this.commonService.deleteCookie();
		this.router.navigateByUrl('/' + ROUTS.LOGIN);
	}
    
    
}