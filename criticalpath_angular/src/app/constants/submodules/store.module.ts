import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { StoreComponent } from '../../modules/store/store.component';
import { BRAND_COMPONENT_ROUTES } from '../subroutes/store.routing';

import { SharedModule } from './shared.module';

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        BRAND_COMPONENT_ROUTES,
        SharedModule
    ],
    declarations: [StoreComponent]
})

export class StoreModule { }