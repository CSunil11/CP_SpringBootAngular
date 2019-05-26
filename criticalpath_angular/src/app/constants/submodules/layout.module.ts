import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule,FormGroup } from '@angular/forms';

import { LayoutComponent } from '../../common/layout/layout.component';
import { LAYOUT_COMPONENT_ROUTES } from '../subroutes/layout.routing';

import { SharedModule } from './shared.module';

@NgModule({
    imports: [
        CommonModule,
        LAYOUT_COMPONENT_ROUTES,
        FormsModule,
        ReactiveFormsModule,
        SharedModule
    ],
    declarations: [LayoutComponent]
})

export class LayoutModule { }
