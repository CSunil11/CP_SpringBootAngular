import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { CriticalPathComponent } from '../../modules/criticalPath/criticalPath.component';
import { CRITICAL_PATH_COMPONENT_ROUTES } from '../subroutes/criticalPath.routing';

import { SharedModule } from './shared.module';

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        CRITICAL_PATH_COMPONENT_ROUTES,
        SharedModule
    ],
    declarations: [CriticalPathComponent]
})

export class CriticalPathModule { }