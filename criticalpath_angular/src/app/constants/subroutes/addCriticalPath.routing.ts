import { ModuleWithProviders } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { AddCriticalPathComponent } from '../../modules/criticalPath/addCriticalPath.component';

const routes: Routes = [
     { path: '', component: AddCriticalPathComponent }
];

export const ADD_CRITICAL_PATH_COMPONENT_ROUTES: ModuleWithProviders = RouterModule.forChild(routes);