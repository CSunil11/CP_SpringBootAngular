import { ModuleWithProviders } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { StoreComponent } from '../../modules/store/store.component';

import * as ROUTES from '../routs';

const routes: Routes = [
     { 	 path: '', 
    	 component: StoreComponent,
    	 /*children: [
    	            { path: ROUTES.ADD_BRAND, loadChildren: '../submodules/addStore.module#AddStoreModule'},
    	            { path: ROUTES.EDIT_BRAND, loadChildren: '../submodules/editStore.module#EditStoreModule'}
    	           ]*/
     }
];

export const BRAND_COMPONENT_ROUTES: ModuleWithProviders = RouterModule.forChild(routes);