import { ModuleWithProviders } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { EditLocationDivisionComponent } from '../../modules/locationDivision/editLocationDivision.component';

const routes: Routes = [
     { path: '', component: EditLocationDivisionComponent }
];

export const EDIT_LOCATIONDIVISION_COMPONENT_ROUTES: ModuleWithProviders = RouterModule.forChild(routes);