import { ModuleWithProviders } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { AddClosedDateComponent } from '../../modules/closedDate/addClosedDate.component';

const routes: Routes = [
     { path: '', component: AddClosedDateComponent }
];

export const ADD_CLOSED_DATE_COMPONENT_ROUTES: ModuleWithProviders = RouterModule.forChild(routes);