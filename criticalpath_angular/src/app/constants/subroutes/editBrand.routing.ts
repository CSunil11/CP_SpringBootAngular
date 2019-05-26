import { ModuleWithProviders } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { EditBrandComponent } from '../../modules/brand/editBrand.component';

const routes: Routes = [
     { path: '', component: EditBrandComponent }
];

export const EDIT_BRAND_COMPONENT_ROUTES: ModuleWithProviders = RouterModule.forChild(routes);