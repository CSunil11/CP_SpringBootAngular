import { ModuleWithProviders } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { EditStoreComponent } from '../../modules/store/editStore.component';

const routes: Routes = [
     { path: '', component: EditStoreComponent }
];

export const EDIT_STORE_COMPONENT_ROUTES: ModuleWithProviders = RouterModule.forChild(routes);