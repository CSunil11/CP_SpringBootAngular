import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { LoginComponent } from '../../login/login.component';
import { LOGIN_COMPONENT_ROUTES } from '../subroutes/login.routing';

import { SharedModule } from './shared.module';
import { AckermanCpComponent } from '../../dashboard/ackermanCp.component';
import { AckermanViewAllBranchesComponent } from '../../dashboard/ackermanViewAllBranches.component';

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        LOGIN_COMPONENT_ROUTES,
        SharedModule
    ],
    declarations: [LoginComponent, AckermanCpComponent]
})

export class LoginModule { }