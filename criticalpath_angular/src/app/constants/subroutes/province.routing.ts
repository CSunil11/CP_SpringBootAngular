import { ModuleWithProviders } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { ProvinceComponent } from '../../modules/province/province.component';

import * as ROUTES from '../routs';

const routes: Routes = [
     { 	 path: '', 
    	 component: ProvinceComponent,
    	 children: [
    	            { path: ROUTES.ADD_PROVINCE, loadChildren: '../submodules/addProvince.module#AddProvinceModule'},
    	            { path: ROUTES.EDIT_PROVINCE, loadChildren: '../submodules/editProvince.module#EditProvinceModule'}
    	           ]
     }
];

export const PROVINCE_COMPONENT_ROUTES: ModuleWithProviders = RouterModule.forChild(routes);