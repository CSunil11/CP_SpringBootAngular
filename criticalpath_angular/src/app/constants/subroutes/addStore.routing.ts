import { ModuleWithProviders } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { AddStoreComponent } from '../../modules/store/addStore.component';

const routes: Routes = [
     { path: '', component: AddStoreComponent }
];

export const ADD_STORE_COMPONENT_ROUTES: ModuleWithProviders = RouterModule.forChild(routes);