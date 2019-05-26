import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { BrandComponent } from '../../modules/brand/brand.component';
import { BRAND_COMPONENT_ROUTES } from '../subroutes/brand.routing';

import { SharedModule } from './shared.module';

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        BRAND_COMPONENT_ROUTES,
        SharedModule
    ],
    declarations: [BrandComponent]
})

export class BrandModule { }