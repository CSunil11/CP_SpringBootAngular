import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { EditManageNotificationComponent } from '../../modules/manageNotification/editManageNotification.component';
import { EDIT_MANAGENOTIFICATION_COMPONENT_ROUTES } from '../subroutes/editManageNotification.routing';

import { SharedModule } from './shared.module';

import { UiSwitchModule } from 'ngx-toggle-switch';

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        EDIT_MANAGENOTIFICATION_COMPONENT_ROUTES,
        SharedModule,
        UiSwitchModule
    ],
    declarations: [EditManageNotificationComponent]
})

export class EditManageNotificationModule { }