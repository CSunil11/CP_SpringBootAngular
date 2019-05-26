import { ModuleWithProviders } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { ClosedDateComponent } from '../../modules/closedDate/closedDate.component';

import * as ROUTES from '../routs';

const routes: Routes = [
     { 	 path: '', 
    	 component: ClosedDateComponent,
    	 children: [
    	            { path: ROUTES.ADD_CLOSED_DATE, loadChildren: '../submodules/addClosedDate.module#AddClosedDateModule'},
    	            { path: ROUTES.EDIT_CLOSED_DATE, loadChildren: '../submodules/editClosedDate.module#EditClosedDateModule'}
    	           ]
     }
];

export const CLOSED_DATE_COMPONENT_ROUTES: ModuleWithProviders = RouterModule.forChild(routes);