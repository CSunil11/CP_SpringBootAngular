import { ModuleWithProviders } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { StockTakeCycleComponent } from '../../modules/stockTakeCycle/stockTakeCycle.component';

import * as ROUTES from '../routs';

const routes: Routes = [
     { 	 path: '', 
    	 component: StockTakeCycleComponent,
    	 children: [
    	            { path: ROUTES.ADD_STOCK_TAKE_CYCLE, loadChildren: '../submodules/addStockTakeCycle.module#AddStockTakeCycleModule'},
    	            { path: ROUTES.EDIT_STOCK_TAKE_CYCLE, loadChildren: '../submodules/editStockTakeCycle.module#EditStockTakeCycleModule'}
    	           ]
     }
];

export const STOCK_TAKE_CYCLE_COMPONENT_ROUTES: ModuleWithProviders = RouterModule.forChild(routes);