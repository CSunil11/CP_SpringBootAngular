import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { EditProvinceComponent } from '../../modules/province/editProvince.component';
import { EDIT_PROVINCE_COMPONENT_ROUTES } from '../subroutes/editProvince.routing';

import { SharedModule } from './shared.module';

import { UiSwitchModule } from 'ngx-toggle-switch';

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        EDIT_PROVINCE_COMPONENT_ROUTES,
        SharedModule,
        UiSwitchModule
    ],
    declarations: [EditProvinceComponent]
})

export class EditProvinceModule { }