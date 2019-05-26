import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { EventComponent } from '../../modules/event/event.component';
import { EVENT_COMPONENT_ROUTES } from '../subroutes/event.routing';

import { SharedModule } from './shared.module';

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        EVENT_COMPONENT_ROUTES,
        SharedModule
    ],
    declarations: [EventComponent]
})

export class EventModule { }