import { ModuleWithProviders } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { ManageNotificationComponent } from '../../modules/manageNotification/manageNotification.component';

import * as ROUTES from '../routs';

const routes: Routes = [
     { 	 path: '', 
    	 component: ManageNotificationComponent
    	 /*children: [
    	            { path: ROUTES.ADD_MANAGENOTIFICATION, loadChildren: '../submodules/addManageNotification.module#AddManageNotificationModule'},
    	            { path: ROUTES.EDIT_MANAGENOTIFICATION, loadChildren: '../submodules/editManageNotification.module#EditManageNotificationModule'}
    	           ]*/
     }
];

export const MANAGENOTIFICATION_COMPONENT_ROUTES: ModuleWithProviders = RouterModule.forChild(routes);