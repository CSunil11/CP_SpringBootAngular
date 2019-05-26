import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AddStockTakeCycleComponent } from '../../modules/stockTakeCycle/addStockTakeCycle.component';
import { ADD_STOCK_TAKE_CYCLE_COMPONENT_ROUTES } from '../subroutes/addStockTakeCycle.routing';

import { SharedModule } from './shared.module';

import { UiSwitchModule } from 'ngx-toggle-switch';

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        ADD_STOCK_TAKE_CYCLE_COMPONENT_ROUTES,
        SharedModule,
        UiSwitchModule
    ],
    declarations: [AddStockTakeCycleComponent]
})

export class AddStockTakeCycleModule { }