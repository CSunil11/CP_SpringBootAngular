import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { StockTakeCycleComponent } from '../../modules/stockTakeCycle/stockTakeCycle.component';
import { STOCK_TAKE_CYCLE_COMPONENT_ROUTES } from '../subroutes/stockTakeCycle.routing';

import { SharedModule } from './shared.module';

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        STOCK_TAKE_CYCLE_COMPONENT_ROUTES,
        SharedModule
    ],
    declarations: [StockTakeCycleComponent]
})

export class StockTakeCycleModule { }