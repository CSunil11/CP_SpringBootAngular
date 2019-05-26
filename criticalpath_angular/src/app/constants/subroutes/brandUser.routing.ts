import { ModuleWithProviders } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { BrandUserComponent } from '../../modules/brandUser/brandUser.component';

import * as ROUTES from '../routs';

const routes: Routes = [
     { 	 path: '', 
    	 component: BrandUserComponent
    	/* children: [
    	            { path: ROUTES.ADD_BRAND_USER, loadChildren: '../submodules/addBrandUser.module#AddBrandUserModule'},
    	            { path: ROUTES.EDIT_BRAND_USER, loadChildren: '../submodules/editBrandUser.module#EditBrandUserModule'}
    	           ]*/
     }
];

export const BRAND_USER_COMPONENT_ROUTES: ModuleWithProviders = RouterModule.forChild(routes);