import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ClosedDateComponent } from '../../modules/closedDate/closedDate.component';
import { CLOSED_DATE_COMPONENT_ROUTES } from '../subroutes/closedDate.routing';

import { SharedModule } from './shared.module';

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        CLOSED_DATE_COMPONENT_ROUTES,
        SharedModule
    ],
    declarations: [ClosedDateComponent]
})

export class ClosedDateModule { }