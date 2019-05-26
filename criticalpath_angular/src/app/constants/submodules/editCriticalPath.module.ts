import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { EditCriticalPathComponent } from '../../modules/criticalPath/editCriticalPath.component';
import { EDIT_CRITICAL_PATH_COMPONENT_ROUTES } from '../subroutes/editCriticalPath.routing';

import { SharedModule } from './shared.module';

import { UiSwitchModule } from 'ngx-toggle-switch';

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        EDIT_CRITICAL_PATH_COMPONENT_ROUTES,
        SharedModule,
        UiSwitchModule
    ],
    declarations: [EditCriticalPathComponent]
})

export class EditCriticalPathModule { }