import { ModuleWithProviders } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { AddStockTakeCycleComponent } from '../../modules/stockTakeCycle/addStockTakeCycle.component';

const routes: Routes = [
     { path: '', component: AddStockTakeCycleComponent }
];

export const ADD_STOCK_TAKE_CYCLE_COMPONENT_ROUTES: ModuleWithProviders = RouterModule.forChild(routes);