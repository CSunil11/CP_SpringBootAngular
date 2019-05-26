import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AddCountryComponent } from '../../modules/country/addCountry.component';
import { ADD_COUNTRY_COMPONENT_ROUTES } from '../subroutes/addCountry.routing';

import { SharedModule } from './shared.module';

import { UiSwitchModule } from 'ngx-toggle-switch';

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        ADD_COUNTRY_COMPONENT_ROUTES,
        SharedModule,
        UiSwitchModule
    ],
    declarations: [AddCountryComponent]
})

export class AddCountryModule { }