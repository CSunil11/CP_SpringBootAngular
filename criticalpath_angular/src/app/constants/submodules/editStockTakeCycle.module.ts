import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { EditStockTakeCycleComponent } from '../../modules/stockTakeCycle/editStockTakeCycle.component';
import { EDIT_STOCK_TAKE_CYCLE_COMPONENT_ROUTES } from '../subroutes/editStockTakeCycle.routing';

import { SharedModule } from './shared.module';

import { UiSwitchModule } from 'ngx-toggle-switch';

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        EDIT_STOCK_TAKE_CYCLE_COMPONENT_ROUTES,
        SharedModule,
        UiSwitchModule
    ],
    declarations: [EditStockTakeCycleComponent]
})

export class EditStockTakeCycleModule { }