import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { EditClosedDateComponent } from '../../modules/closedDate/editClosedDate.component';
import { EDIT_CLOSED_DATE_COMPONENT_ROUTES } from '../subroutes/editClosedDate.routing';

import { SharedModule } from './shared.module';

import { UiSwitchModule } from 'ngx-toggle-switch';

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        EDIT_CLOSED_DATE_COMPONENT_ROUTES,
        SharedModule,
        UiSwitchModule
    ],
    declarations: [EditClosedDateComponent]
})

export class EditClosedDateModule { }