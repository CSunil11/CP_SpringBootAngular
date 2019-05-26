import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { BrandUserComponent } from '../../modules/brandUser/brandUser.component';
import { BRAND_USER_COMPONENT_ROUTES } from '../subroutes/brandUser.routing';

import { SharedModule } from './shared.module';

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        BRAND_USER_COMPONENT_ROUTES,
        SharedModule
    ],
    declarations: [BrandUserComponent]
})

export class BrandUserModule { }