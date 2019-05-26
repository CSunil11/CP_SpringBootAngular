import { ModuleWithProviders } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { CriticalPathComponent } from '../../modules/criticalPath/criticalPath.component';

import * as ROUTES from '../routs';

const routes: Routes = [
     { 	 path: '', 
    	 component: CriticalPathComponent,
    	 children: [
    	            { path: ROUTES.ADD_CRITICAL_PATH, loadChildren: '../submodules/addCriticalPath.module#AddCriticalPathModule'}
//    	            { path: ROUTES.EDIT_CRITICAL_PATH, loadChildren: '../submodules/editCriticalPath.module#EditCriticalPathModule'}
    	           ]
     }
];

export const CRITICAL_PATH_COMPONENT_ROUTES: ModuleWithProviders = RouterModule.forChild(routes);