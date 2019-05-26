import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatAutocompleteModule, MatButtonModule,MatFormFieldModule,MatInputModule, MatRippleModule } from '@angular/material';
import { AddWebhookComponent } from '../../modules/webhook/addWebhook.component';
import { ADD_WEBHOOK_COMPONENT_ROUTES } from '../subroutes/addWebhook.routing';

import { SharedModule } from './shared.module';

import { UiSwitchModule } from 'ngx-toggle-switch';

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        ADD_WEBHOOK_COMPONENT_ROUTES,
        SharedModule,
        UiSwitchModule,
        MatAutocompleteModule,
        MatButtonModule,
        MatFormFieldModule,
        MatInputModule,
        MatRippleModule,
    ],
    exports: [
              MatAutocompleteModule,
              MatButtonModule,
              MatFormFieldModule,
              MatInputModule,
              MatRippleModule,
              ],
    declarations: [AddWebhookComponent]
})

export class AddWebhookModule { }