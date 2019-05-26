import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { EditBrandComponent } from '../../modules/brand/editBrand.component';
import { EDIT_BRAND_COMPONENT_ROUTES } from '../subroutes/editBrand.routing';

import { SharedModule } from './shared.module';

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        EDIT_BRAND_COMPONENT_ROUTES,
        SharedModule,
    ],
    declarations: [EditBrandComponent]
})

export class EditBrandModule { }