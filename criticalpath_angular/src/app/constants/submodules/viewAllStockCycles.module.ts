import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ViewAllStockCyclesComponent } from '../../dashboard/viewAllStockCycles.component';
import { VIEW_ALL_STOCK_CYCLES_COMPONENT_ROUTES } from '../subroutes/viewAllStockCycles.routing';

import { SharedModule } from './shared.module';

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        VIEW_ALL_STOCK_CYCLES_COMPONENT_ROUTES,
        SharedModule
    ],
    declarations: [ViewAllStockCyclesComponent]
})

export class ViewAllStockCyclesModule { }