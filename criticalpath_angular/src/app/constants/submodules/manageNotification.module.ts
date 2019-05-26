import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ManageNotificationComponent } from '../../modules/manageNotification/manageNotification.component';
import { MANAGENOTIFICATION_COMPONENT_ROUTES } from '../subroutes/manageNotification.routing';

import { SharedModule } from './shared.module';

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        MANAGENOTIFICATION_COMPONENT_ROUTES,
        SharedModule
    ],
    declarations: [ManageNotificationComponent]
})

export class ManageNotificationModule { }