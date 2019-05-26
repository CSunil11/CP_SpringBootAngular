import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AddManageNotificationComponent } from '../../modules/manageNotification/addManageNotification.component';
import { ADD_MANAGENOTIFICATION_COMPONENT_ROUTES } from '../subroutes/addManageNotification.routing';

import { SharedModule } from './shared.module';

import { UiSwitchModule } from 'ngx-toggle-switch';

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        ADD_MANAGENOTIFICATION_COMPONENT_ROUTES,
        SharedModule,
        UiSwitchModule
    ],
    declarations: [AddManageNotificationComponent]
})

export class AddManageNotificationModule { }
