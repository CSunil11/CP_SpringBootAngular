import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { EditEventComponent } from '../../modules/event/editEvent.component';
import { EDIT_EVENT_COMPONENT_ROUTES } from '../subroutes/editEvent.routing';

import { SharedModule } from './shared.module';

import { UiSwitchModule } from 'ngx-toggle-switch';

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        EDIT_EVENT_COMPONENT_ROUTES,
        SharedModule,
        UiSwitchModule
    ],
    declarations: [EditEventComponent]
})

export class EditEventModule { }