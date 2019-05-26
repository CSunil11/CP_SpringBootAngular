import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { DaysOfWeekComponent } from '../../modules/daysOfWeek/daysOfWeek.component';
import { DAYS_OF_WEEK_COMPONENT_ROUTES } from '../subroutes/daysOfWeek.routing';

import { SharedModule } from './shared.module';

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        DAYS_OF_WEEK_COMPONENT_ROUTES,
        SharedModule
    ],
    declarations: [DaysOfWeekComponent]
})

export class DaysOfWeekModule { }