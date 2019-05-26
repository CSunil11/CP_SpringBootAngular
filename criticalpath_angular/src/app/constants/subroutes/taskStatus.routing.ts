import { ModuleWithProviders } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { TaskStatusComponent } from '../../modules/taskStatus/taskStatus.component';

import * as ROUTES from '../routs';

const routes: Routes = [
     { path: '',
       component: TaskStatusComponent,
       children: [
	  	            { path: ROUTES.ADD_TASK_STATUS, loadChildren: '../submodules/addTaskStatus.module#AddTaskStatusModule'},
	  	            { path: ROUTES.EDIT_TASK_STATUS, loadChildren: '../submodules/editTaskStatus.module#EditTaskStatusModule'}
  	           ]
     }
];

export const TASK_STATUS_COMPONENT_ROUTES: ModuleWithProviders = RouterModule.forChild(routes);