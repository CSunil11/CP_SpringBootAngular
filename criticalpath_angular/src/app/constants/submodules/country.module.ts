import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { CountryComponent } from '../../modules/country/country.component';
import { COUNTRY_COMPONENT_ROUTES } from '../subroutes/country.routing';

import { SharedModule } from './shared.module';

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        COUNTRY_COMPONENT_ROUTES,
        SharedModule
    ],
    declarations: [CountryComponent]
})

export class CountryModule { }