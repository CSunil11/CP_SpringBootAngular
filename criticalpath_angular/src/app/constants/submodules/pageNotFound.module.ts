import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { PageNotFoundComponent } from '../../common/pageNotFound/pageNotFound.component';
import { PAGE_NOT_FOUND_COMPONENT_ROUTES } from '../subroutes/pageNotFound.routing';

import { SharedModule } from './shared.module';

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        PAGE_NOT_FOUND_COMPONENT_ROUTES,
        SharedModule,
    ],
    declarations: [PageNotFoundComponent]
})

export class PageNotFoundModule { }