import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ApiLogComponent } from '../../modules/apiLog/apiLog';
import { API_LOG_COMPONENT_ROUTES } from '../subroutes/apiLog.routing';

import { SharedModule } from './shared.module';

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        API_LOG_COMPONENT_ROUTES,
        SharedModule
    ],
    declarations: [ApiLogComponent]
})

export class ApiLogModule { }