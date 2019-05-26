import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { NotAuthorizedComponent } from '../../common/notAuthorized/notAuthorized.component';
import { NOT_AUTHORIZED_COMPONENT_ROUTES } from '../subroutes/notAuthorized.routing';

import { SharedModule } from './shared.module';

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        NOT_AUTHORIZED_COMPONENT_ROUTES,
        SharedModule,
    ],
    declarations: [NotAuthorizedComponent]
})

export class NotAuthorizedModule { }