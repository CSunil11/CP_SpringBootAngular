import { ModuleWithProviders } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { AddManageNotificationComponent } from '../../modules/manageNotification/addManageNotification.component';

const routes: Routes = [
     { path: '', component: AddManageNotificationComponent }
];

export const ADD_MANAGENOTIFICATION_COMPONENT_ROUTES: ModuleWithProviders = RouterModule.forChild(routes);