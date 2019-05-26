import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { EditTaskStatusComponent } from '../../modules/taskStatus/editTaskStatus.component';
import { EDIT_TASK_STATUS_COMPONENT_ROUTES } from '../subroutes/editTaskStatus.routing';

import { SharedModule } from './shared.module';

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        EDIT_TASK_STATUS_COMPONENT_ROUTES,
        SharedModule
    ],
    declarations: [EditTaskStatusComponent]
})

export class EditTaskStatusModule {}