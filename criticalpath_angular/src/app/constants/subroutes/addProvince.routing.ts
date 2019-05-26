import { ModuleWithProviders } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { AddProvinceComponent } from '../../modules/province/addProvince.component';

const routes: Routes = [
     { path: '', component: AddProvinceComponent }
];

export const ADD_PROVINCE_COMPONENT_ROUTES: ModuleWithProviders = RouterModule.forChild(routes);