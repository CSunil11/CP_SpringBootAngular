import { ModuleWithProviders } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { ApiLogComponent } from '../../modules/apiLog/apiLog';

import * as ROUTES from '../routs';

const routes: Routes = [
     { 	 path: '', 
    	 component: ApiLogComponent
     }
];

export const API_LOG_COMPONENT_ROUTES: ModuleWithProviders = RouterModule.forChild(routes);