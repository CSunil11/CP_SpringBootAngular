import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AopLogComponent } from '../../modules/aopLog/aopLog.component';
import { AOP_LOG_COMPONENT_ROUTES } from '../subroutes/aopLog.routing';

import { SharedModule } from './shared.module';

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        AOP_LOG_COMPONENT_ROUTES,
        SharedModule
    ],
    declarations: [AopLogComponent]
})

export class AopLogModule { }
