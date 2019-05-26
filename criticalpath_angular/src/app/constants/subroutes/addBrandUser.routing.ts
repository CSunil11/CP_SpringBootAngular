import { ModuleWithProviders } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { AddBrandUserComponent } from '../../modules/brandUser/addBrandUser.component';

const routes: Routes = [
     { path: '', component: AddBrandUserComponent }
];

export const ADD_BRAND_USER_COMPONENT_ROUTES: ModuleWithProviders = RouterModule.forChild(routes);