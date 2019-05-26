import { ModuleWithProviders } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { DashboardComponent } from '../../dashboard/dashboard.component';

const routes: Routes = [
     { path: '', component: DashboardComponent }
];

export const DASHBOARD_COMPONENT_ROUTES: ModuleWithProviders = RouterModule.forChild(routes);