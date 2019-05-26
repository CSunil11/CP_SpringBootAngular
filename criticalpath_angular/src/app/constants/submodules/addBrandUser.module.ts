import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AddBrandUserComponent } from '../../modules/brandUser/addBrandUser.component';
import { ADD_BRAND_USER_COMPONENT_ROUTES } from '../subroutes/addBrandUser.routing';

import { SharedModule } from './shared.module';

import { UiSwitchModule } from 'ngx-toggle-switch';

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        ADD_BRAND_USER_COMPONENT_ROUTES,
        SharedModule,
        UiSwitchModule
    ],
    declarations: [AddBrandUserComponent]
})

export class AddBrandUserModule { }