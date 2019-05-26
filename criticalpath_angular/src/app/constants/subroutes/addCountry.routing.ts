import { ModuleWithProviders } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { AddCountryComponent } from '../../modules/country/addCountry.component';

const routes: Routes = [
     { path: '', component: AddCountryComponent }
];

export const ADD_COUNTRY_COMPONENT_ROUTES: ModuleWithProviders = RouterModule.forChild(routes);