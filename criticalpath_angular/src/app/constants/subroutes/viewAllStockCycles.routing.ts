import { ModuleWithProviders } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { ViewAllStockCyclesComponent } from '../../dashboard/viewAllStockCycles.component';

const routes: Routes = [
     { path: '', component: ViewAllStockCyclesComponent }
];

export const VIEW_ALL_STOCK_CYCLES_COMPONENT_ROUTES: ModuleWithProviders = RouterModule.forChild(routes);