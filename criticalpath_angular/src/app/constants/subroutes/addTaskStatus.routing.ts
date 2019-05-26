import { ModuleWithProviders } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { AddTaskStatusComponent } from '../../modules/taskStatus/addTaskStatus.component';

const routes: Routes = [
     { path: '', component: AddTaskStatusComponent }
];

export const ADD_TASK_STATUS_COMPONENT_ROUTES: ModuleWithProviders = RouterModule.forChild(routes);