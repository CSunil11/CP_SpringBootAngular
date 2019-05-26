import { ModuleWithProviders } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { AddBrandComponent } from '../../modules/brand/addBrand.component';

const routes: Routes = [
     { path: '', component: AddBrandComponent }
];

export const ADD_BRAND_COMPONENT_ROUTES: ModuleWithProviders = RouterModule.forChild(routes);