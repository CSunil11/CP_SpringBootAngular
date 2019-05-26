import { ModuleWithProviders } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { EditClosedDateComponent } from '../../modules/closedDate/editClosedDate.component';

const routes: Routes = [
     { path: '', component: EditClosedDateComponent }
];

export const EDIT_CLOSED_DATE_COMPONENT_ROUTES: ModuleWithProviders = RouterModule.forChild(routes);