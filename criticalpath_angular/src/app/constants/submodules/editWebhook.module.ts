import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatAutocompleteModule, MatButtonModule,MatFormFieldModule,MatInputModule, MatRippleModule } from '@angular/material';
import { EditWebhookComponent } from '../../modules/webhook/editWebhook.component';
import { EDIT_WEBHOOK_COMPONENT_ROUTES } from '../subroutes/editWebhook.routing';

import { SharedModule } from './shared.module';

import { UiSwitchModule } from 'ngx-toggle-switch';

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        EDIT_WEBHOOK_COMPONENT_ROUTES,
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
    declarations: [EditWebhookComponent]
})

export class EditWebhookModule { }