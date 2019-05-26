import { ModuleWithProviders } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { LocationDivisionComponent } from '../../modules/locationDivision/locationDivision.component';

import * as ROUTES from '../routs';

const routes: Routes = [
     { 	 path: '', 
    	 component: LocationDivisionComponent,
    	 children: [
    	            { path: ROUTES.ADD_LOCATIONDIVISION, loadChildren: '../submodules/addLocationDivision.module#AddLocationDivisionModule'},
    	            { path: ROUTES.EDIT_LOCATIONDIVISION, loadChildren: '../submodules/editLocationDivision.module#EditLocationDivisionModule'}
    	           ]
     }
];

export const LOCATIONDIVISION_COMPONENT_ROUTES: ModuleWithProviders = RouterModule.forChild(routes);