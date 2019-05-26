import { ModuleWithProviders } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { PageNotFoundComponent } from '../../common/pageNotFound/pageNotFound.component';

const routes: Routes = [
     { path: '', component: PageNotFoundComponent }
];

export const PAGE_NOT_FOUND_COMPONENT_ROUTES: ModuleWithProviders = RouterModule.forChild(routes);