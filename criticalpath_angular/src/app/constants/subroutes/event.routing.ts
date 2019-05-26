import { ModuleWithProviders } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { EventComponent } from '../../modules/event/event.component';

import * as ROUTES from '../routs';

const routes: Routes = [
     { 	 path: '', 
    	 component: EventComponent,
    	 children: [
    	            { path: ROUTES.ADD_EVENT, loadChildren: '../submodules/addEvent.module#AddEventModule'},
    	            { path: ROUTES.EDIT_EVENT, loadChildren: '../submodules/editEvent.module#EditEventModule'}
    	           ]
     }
];

export const EVENT_COMPONENT_ROUTES: ModuleWithProviders = RouterModule.forChild(routes);