import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AddCriticalPathComponent } from '../../modules/criticalPath/addCriticalPath.component';
import { ADD_CRITICAL_PATH_COMPONENT_ROUTES } from '../subroutes/addCriticalPath.routing';

import { SharedModule } from './shared.module';

import { UiSwitchModule } from 'ngx-toggle-switch';

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        ADD_CRITICAL_PATH_COMPONENT_ROUTES,
        SharedModule,
        UiSwitchModule
    ],
    declarations: [AddCriticalPathComponent]
})

export class AddCriticalPathModule { }

