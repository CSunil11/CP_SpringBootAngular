import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AddClosedDateComponent } from '../../modules/closedDate/addClosedDate.component';
import { ADD_CLOSED_DATE_COMPONENT_ROUTES } from '../subroutes/addClosedDate.routing';

import { SharedModule } from './shared.module';

import { UiSwitchModule } from 'ngx-toggle-switch';

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        ADD_CLOSED_DATE_COMPONENT_ROUTES,
        SharedModule,
        UiSwitchModule
    ],
    declarations: [AddClosedDateComponent]
})

export class AddClosedDateModule { }