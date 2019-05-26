import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { EditLocationDivisionComponent } from '../../modules/locationDivision/editLocationDivision.component';
import { EDIT_LOCATIONDIVISION_COMPONENT_ROUTES } from '../subroutes/editLocationDivision.routing';

import { SharedModule } from './shared.module';

import { UiSwitchModule } from 'ngx-toggle-switch';

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        EDIT_LOCATIONDIVISION_COMPONENT_ROUTES,
        SharedModule,
        UiSwitchModule
    ],
    declarations: [EditLocationDivisionComponent]
})

export class EditLocationDivisionModule { }