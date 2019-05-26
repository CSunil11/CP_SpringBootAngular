import { ModuleWithProviders } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { EditCriticalPathComponent } from '../../modules/criticalPath/editCriticalPath.component';

const routes: Routes = [
     { path: '', component: EditCriticalPathComponent }
];

export const EDIT_CRITICAL_PATH_COMPONENT_ROUTES: ModuleWithProviders = RouterModule.forChild(routes);