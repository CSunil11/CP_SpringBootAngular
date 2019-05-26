import { ModuleWithProviders } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { EditTaskStatusComponent } from '../../modules/taskStatus/editTaskStatus.component';

const routes: Routes = [
     { path: '', component: EditTaskStatusComponent }
];

export const EDIT_TASK_STATUS_COMPONENT_ROUTES: ModuleWithProviders = RouterModule.forChild(routes);