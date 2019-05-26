import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AddTaskStatusComponent } from '../../modules/taskStatus/addTaskStatus.component';
import { ADD_TASK_STATUS_COMPONENT_ROUTES } from '../subroutes/addTaskStatus.routing';

import { SharedModule } from './shared.module';

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        ADD_TASK_STATUS_COMPONENT_ROUTES,
        SharedModule,
    ],
    declarations: [AddTaskStatusComponent]
})

export class AddTaskStatusModule { }