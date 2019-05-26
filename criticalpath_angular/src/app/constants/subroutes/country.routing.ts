import { ModuleWithProviders } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { CountryComponent } from '../../modules/country/country.component';

import * as ROUTES from '../routs';

const routes: Routes = [
     { 	 path: '', 
    	 component: CountryComponent,
    	 children: [
    	            { path: ROUTES.ADD_COUNTRY, loadChildren: '../submodules/addCountry.module#AddCountryModule'},
    	            { path: ROUTES.EDIT_COUNTRY, loadChildren: '../submodules/editCountry.module#EditCountryModule'}
    	           ]
     }
];

export const COUNTRY_COMPONENT_ROUTES: ModuleWithProviders = RouterModule.forChild(routes);