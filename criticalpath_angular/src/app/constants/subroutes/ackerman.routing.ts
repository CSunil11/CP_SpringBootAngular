import { ModuleWithProviders } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { AckermanComponent } from '../../dashboard/ackerman.component';
import { AckermanCpComponent } from '../../dashboard/ackermanCp.component';
import { AckermanViewAllBranchesComponent } from '../../dashboard/ackermanViewAllBranches.component';

const routes: Routes = [
     { path: '', component: AckermanComponent },
//     { path: 'ackermanCpComponent', component: AckermanCpComponent },
//     {
//         path: 'ackermanCpComponent/:cycleStartBy/:startDate/:storeId/:stokeDate', 
//         component: ViewAllStockCyclesComponent,
//     },
//      {
//      	path: 'ackermanPageview/ackermanCpComponent/:cycleStartBy/:startDate/:storeId/:stokeDate',
//          component: ViewAllStockCyclesComponent,
//      },
     
     { path: 'ackermanViewAllBranchesComponent', component: AckermanViewAllBranchesComponent },
     
  
];

export const ACKERMAN_COMPONENT_ROUTES: ModuleWithProviders = RouterModule.forChild(routes);