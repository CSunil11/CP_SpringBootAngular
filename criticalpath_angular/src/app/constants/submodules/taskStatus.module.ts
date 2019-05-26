import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { TaskStatusComponent } from '../../modules/taskStatus/taskStatus.component';
import { TASK_STATUS_COMPONENT_ROUTES } from '../subroutes/taskStatus.routing';

import { SharedModule } from './shared.module';

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        TASK_STATUS_COMPONENT_ROUTES,
        SharedModule
    ],
    declarations: [TaskStatusComponent]
})

export class TaskStatusModule { }