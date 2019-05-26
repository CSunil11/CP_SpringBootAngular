import { ModuleWithProviders } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { AddLocationDivisionComponent } from '../../modules/locationDivision/addLocationDivision.component';

const routes: Routes = [
     { path: '', component: AddLocationDivisionComponent }
];

export const ADD_LOCATIONDIVISION_COMPONENT_ROUTES: ModuleWithProviders = RouterModule.forChild(routes);