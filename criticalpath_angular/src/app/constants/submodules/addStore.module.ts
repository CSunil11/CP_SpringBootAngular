import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AddStoreComponent } from '../../modules/store/addStore.component';
import { ADD_STORE_COMPONENT_ROUTES } from '../subroutes/addStore.routing';

import { SharedModule } from './shared.module';

//import { UiSwitchModule } from 'ngx-toggle-switch';

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        ADD_STORE_COMPONENT_ROUTES,
        SharedModule,
//        UiSwitchModule
    ],
    declarations: [AddStoreComponent]
})

export class AddStoreModule { }