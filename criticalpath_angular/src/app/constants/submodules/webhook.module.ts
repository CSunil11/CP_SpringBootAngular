import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { WebhookComponent } from '../../modules/webhook/webhook.component';
import { WEBHOOK_COMPONENT_ROUTES } from '../subroutes/webhook.routing';

import { SharedModule } from './shared.module';

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        WEBHOOK_COMPONENT_ROUTES,
        SharedModule
    ],
    declarations: [WebhookComponent]
})

export class WebhookModule { }