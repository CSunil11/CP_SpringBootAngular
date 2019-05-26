import { Injectable } from '@angular/core';
import { Cookie } from 'ng2-cookies';
import { RouterModule, Routes, Router } from '@angular/router';

import {CONSTANTS } from '../constants/constant';

import { SystemSettingRepository } from '../repository/systemSetting.repository';

import * as ROUTS from '../constants/routs';


declare var $: any;

@Injectable()
export class SystemSettingService {
	
	constructor(public systemSettingRepository : SystemSettingRepository) {}
	
	turnEmailOnOrOff(isEmailTurnedOnOrOff) {
		return this.systemSettingRepository.turnEmailOnOrOff(isEmailTurnedOnOrOff) 
			
		
	}
	
}