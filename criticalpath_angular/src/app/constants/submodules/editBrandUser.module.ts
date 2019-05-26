import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { EditBrandUserComponent } from '../../modules/brandUser/editBrandUser.component';
import { EDIT_BRAND_USER_COMPONENT_ROUTES } from '../subroutes/editBrandUser.routing';

import { SharedModule } from './shared.module';

import { UiSwitchModule } from 'ngx-toggle-switch';

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        EDIT_BRAND_USER_COMPONENT_ROUTES,
        SharedModule,
        UiSwitchModule
    ],
    declarations: [EditBrandUserComponent]
})

export class EditBrandUserModule { }