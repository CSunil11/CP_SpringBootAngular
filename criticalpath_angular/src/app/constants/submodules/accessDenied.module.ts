import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AccessDeniedComponent } from '../../common/accessDenied/accessDenied.component';
import { ACCESS_DENIED_COMPONENT_ROUTES } from '../subroutes/accessDenied.routing';

import { SharedModule } from './shared.module';

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        ACCESS_DENIED_COMPONENT_ROUTES,
        SharedModule,
    ],
    declarations: [AccessDeniedComponent]
})

export class AccessDeniedModule { }