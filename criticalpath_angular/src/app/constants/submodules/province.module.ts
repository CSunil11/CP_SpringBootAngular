import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ProvinceComponent } from '../../modules/province/province.component';
import { PROVINCE_COMPONENT_ROUTES } from '../subroutes/province.routing';

import { SharedModule } from './shared.module';

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        PROVINCE_COMPONENT_ROUTES,
        SharedModule
    ],
    declarations: [ProvinceComponent]
})

export class ProvinceModule { }