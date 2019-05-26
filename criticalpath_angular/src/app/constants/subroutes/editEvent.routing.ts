import { ModuleWithProviders } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { EditEventComponent } from '../../modules/event/editEvent.component';

const routes: Routes = [
     { path: '', component: EditEventComponent }
];

export const EDIT_EVENT_COMPONENT_ROUTES: ModuleWithProviders = RouterModule.forChild(routes);