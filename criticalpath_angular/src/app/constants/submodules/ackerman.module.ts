import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule,FormGroup } from '@angular/forms';

import { AckermanComponent } from '../../dashboard/ackerman.component';
import { AckermanCpComponent } from '../../dashboard/ackermanCp.component';
import { AckermanViewAllBranchesComponent } from '../../dashboard/ackermanViewAllBranches.component';
import { ACKERMAN_COMPONENT_ROUTES } from '../subroutes/ackerman.routing';

import { SharedModule } from './shared.module';

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        ACKERMAN_COMPONENT_ROUTES,
        ReactiveFormsModule,
        SharedModule
    ],
    declarations: [AckermanComponent, AckermanViewAllBranchesComponent]
//    declarations: [AckermanComponent]
})

export class AckermanModule { }
