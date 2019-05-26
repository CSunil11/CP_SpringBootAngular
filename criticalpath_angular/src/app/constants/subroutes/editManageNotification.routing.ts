import { ModuleWithProviders } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { EditManageNotificationComponent } from '../../modules/manageNotification/editManageNotification.component';

const routes: Routes = [
     { path: '', component: EditManageNotificationComponent }
];

export const EDIT_MANAGENOTIFICATION_COMPONENT_ROUTES: ModuleWithProviders = RouterModule.forChild(routes);