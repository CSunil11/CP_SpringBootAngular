import { ModuleWithProviders } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { EditStockTakeCycleComponent } from '../../modules/stockTakeCycle/editStockTakeCycle.component';

const routes: Routes = [
     { path: '', component: EditStockTakeCycleComponent }
];

export const EDIT_STOCK_TAKE_CYCLE_COMPONENT_ROUTES: ModuleWithProviders = RouterModule.forChild(routes);