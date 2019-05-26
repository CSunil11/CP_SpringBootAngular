import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AddProvinceComponent } from '../../modules/province/addProvince.component';
import { ADD_PROVINCE_COMPONENT_ROUTES } from '../subroutes/addProvince.routing';

import { SharedModule } from './shared.module';

import { UiSwitchModule } from 'ngx-toggle-switch';

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        ADD_PROVINCE_COMPONENT_ROUTES,
        SharedModule,
        UiSwitchModule
    ],
    declarations: [AddProvinceComponent]
})

export class AddProvinceModule { }
