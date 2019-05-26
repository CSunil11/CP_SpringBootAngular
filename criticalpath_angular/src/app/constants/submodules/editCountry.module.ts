import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { EditCountryComponent } from '../../modules/country/editCountry.component';
import { EDIT_COUNTRY_COMPONENT_ROUTES } from '../subroutes/editCountry.routing';

import { SharedModule } from './shared.module';

import { UiSwitchModule } from 'ngx-toggle-switch';

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        EDIT_COUNTRY_COMPONENT_ROUTES,
        SharedModule,
        UiSwitchModule
    ],
    declarations: [EditCountryComponent]
})

export class EditCountryModule { }