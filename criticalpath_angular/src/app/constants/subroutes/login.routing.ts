import { ModuleWithProviders } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { LoginComponent } from '../../login/login.component';
import { AckermanCpComponent } from '../../dashboard/ackermanCp.component';
import { AckermanViewAllBranchesComponent } from '../../dashboard/ackermanViewAllBranches.component';

const routes: Routes = [
    { path: '', component: LoginComponent },
    
//    { path: 'ackermanCpComponent', component: AckermanCpComponent },
//    { path: 'ackermanViewAllBranchesComponent', component: AckermanViewAllBranchesComponent },
    
    {
        path: 'ackermanViewAllBranchesComponent/:storeId/:cycleId/:storeName', 
        component: AckermanCpComponent,
    },
//    {
//    	path: 'ackermanPage/ackermanCpComponent/:cycleStartBy/:startDate/:storeId/:stokeDate',
////    	path: 'ackermanPage/ackermanCpComponent/:cycleStartBy,
//        component: ViewAllStockCyclesComponent,
//    },
    
];

export const LOGIN_COMPONENT_ROUTES: ModuleWithProviders = RouterModule.forChild(routes);