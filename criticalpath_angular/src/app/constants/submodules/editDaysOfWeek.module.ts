import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { EditDaysOfWeekComponent } from '../../modules/daysOfWeek/editDaysOfWeek.component';
import { EDIT_DAYS_OF_WEEK_COMPONENT_ROUTES } from '../subroutes/editDaysOfWeek.routing';

import { SharedModule } from './shared.module';

import { UiSwitchModule } from 'ngx-toggle-switch';

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        EDIT_DAYS_OF_WEEK_COMPONENT_ROUTES,
        SharedModule,
        UiSwitchModule
    ],
    declarations: [EditDaysOfWeekComponent]
})

export class EditDaysOfWeekModule { }
