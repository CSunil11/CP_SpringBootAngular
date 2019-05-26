import { ModuleWithProviders } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { AopLogComponent } from '../../modules/aopLog/aopLog.component';

import * as ROUTES from '../routs';

const routes: Routes = [
     { 	 path: '', 
    	 component: AopLogComponent
     }
];

export const AOP_LOG_COMPONENT_ROUTES: ModuleWithProviders = RouterModule.forChild(routes);