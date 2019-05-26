import { ModuleWithProviders } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { EditProvinceComponent } from '../../modules/province/editProvince.component';

const routes: Routes = [
     { path: '', component: EditProvinceComponent }
];

export const EDIT_PROVINCE_COMPONENT_ROUTES: ModuleWithProviders = RouterModule.forChild(routes);