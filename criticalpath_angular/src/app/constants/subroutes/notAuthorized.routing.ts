import { ModuleWithProviders } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { NotAuthorizedComponent } from '../../common/notAuthorized/notAuthorized.component';

const routes: Routes = [
     { path: '', component: NotAuthorizedComponent }
];

export const NOT_AUTHORIZED_COMPONENT_ROUTES: ModuleWithProviders = RouterModule.forChild(routes);