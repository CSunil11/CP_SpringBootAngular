import { ModuleWithProviders } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { DaysOfWeekComponent } from '../../modules/daysOfWeek/daysOfWeek.component';

import * as ROUTES from '../routs';

const routes: Routes = [
     { 	 path: '', 
    	 component: DaysOfWeekComponent
     }
];

export const DAYS_OF_WEEK_COMPONENT_ROUTES: ModuleWithProviders = RouterModule.forChild(routes);