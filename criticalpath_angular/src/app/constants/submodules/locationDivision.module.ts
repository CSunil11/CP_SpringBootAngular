import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { LocationDivisionComponent } from '../../modules/locationDivision/locationDivision.component';
import { LOCATIONDIVISION_COMPONENT_ROUTES } from '../subroutes/locationDivision.routing';

import { SharedModule } from './shared.module';

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        LOCATIONDIVISION_COMPONENT_ROUTES,
        SharedModule
    ],
    declarations: [LocationDivisionComponent]
})

export class LocationDivisionModule { }