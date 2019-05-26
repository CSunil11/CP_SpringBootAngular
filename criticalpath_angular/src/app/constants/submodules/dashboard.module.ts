import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule,FormGroup } from '@angular/forms';

import { DashboardComponent } from '../../dashboard/dashboard.component';
import { DASHBOARD_COMPONENT_ROUTES } from '../subroutes/dashboard.routing';

import { SharedModule } from './shared.module';

@NgModule({
    imports: [
        CommonModule,
        DASHBOARD_COMPONENT_ROUTES,
        FormsModule,
        ReactiveFormsModule,
        SharedModule
    ],
    declarations: [DashboardComponent]
})

export class DashboardModule { }
