import { ModuleWithProviders } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { BrandComponent } from '../../modules/brand/brand.component';

import * as ROUTES from '../routs';

const routes: Routes = [
     { 	 path: '', 
    	 component: BrandComponent,
    	 children: [
    	            { path: ROUTES.ADD_BRAND, loadChildren: '../submodules/addBrand.module#AddBrandModule'},
    	            { path: ROUTES.EDIT_BRAND, loadChildren: '../submodules/editBrand.module#EditBrandModule'}
    	           ]
     }
];

export const BRAND_COMPONENT_ROUTES: ModuleWithProviders = RouterModule.forChild(routes);