import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AddLocationDivisionComponent } from '../../modules/locationDivision/addLocationDivision.component';
import { ADD_LOCATIONDIVISION_COMPONENT_ROUTES } from '../subroutes/addLocationDivision.routing';

import { SharedModule } from './shared.module';

import { UiSwitchModule } from 'ngx-toggle-switch';

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        ADD_LOCATIONDIVISION_COMPONENT_ROUTES,
        SharedModule,
        UiSwitchModule
    ],
    declarations: [AddLocationDivisionComponent]
})

export class AddLocationDivisionModule { }
