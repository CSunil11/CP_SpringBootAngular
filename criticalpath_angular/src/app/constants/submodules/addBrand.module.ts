import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AddBrandComponent } from '../../modules/brand/addBrand.component';
import { ADD_BRAND_COMPONENT_ROUTES } from '../subroutes/addBrand.routing';

import { SharedModule } from './shared.module';

//import { UiSwitchModule } from 'ngx-toggle-switch';

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        ADD_BRAND_COMPONENT_ROUTES,
        SharedModule,
//        UiSwitchModule
    ],
    declarations: [AddBrandComponent]
})

export class AddBrandModule { }