import { ModuleWithProviders } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { AccessDeniedComponent } from '../../common/accessDenied/accessDenied.component';

const routes: Routes = [
     { path: '', component: AccessDeniedComponent }
];

export const ACCESS_DENIED_COMPONENT_ROUTES: ModuleWithProviders = RouterModule.forChild(routes);