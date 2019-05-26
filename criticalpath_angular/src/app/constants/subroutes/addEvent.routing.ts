import { ModuleWithProviders } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { AddEventComponent } from '../../modules/event/addEvent.component';

const routes: Routes = [
     { path: '', component: AddEventComponent }
];

export const ADD_EVENT_COMPONENT_ROUTES: ModuleWithProviders = RouterModule.forChild(routes);