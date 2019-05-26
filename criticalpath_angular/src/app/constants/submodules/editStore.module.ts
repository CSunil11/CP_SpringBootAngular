import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { EditStoreComponent } from '../../modules/store/editStore.component';
import { EDIT_STORE_COMPONENT_ROUTES } from '../subroutes/editStore.routing';

import { SharedModule } from './shared.module';

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        EDIT_STORE_COMPONENT_ROUTES,
        SharedModule,
    ],
    declarations: [EditStoreComponent]
})

export class EditStoreModule { }