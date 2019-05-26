import { ModuleWithProviders } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { EditCountryComponent } from '../../modules/country/editCountry.component';

const routes: Routes = [
     { path: '', component: EditCountryComponent }
];

export const EDIT_COUNTRY_COMPONENT_ROUTES: ModuleWithProviders = RouterModule.forChild(routes);