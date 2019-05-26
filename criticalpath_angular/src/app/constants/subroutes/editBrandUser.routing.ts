import { ModuleWithProviders } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { EditBrandUserComponent } from '../../modules/brandUser/editBrandUser.component';

const routes: Routes = [
     { path: '', component: EditBrandUserComponent }
];

export const EDIT_BRAND_USER_COMPONENT_ROUTES: ModuleWithProviders = RouterModule.forChild(routes);