import { ModuleWithProviders } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { EditDaysOfWeekComponent } from '../../modules/daysOfWeek/editDaysOfWeek.component';

const routes: Routes = [
     { path: '', component: EditDaysOfWeekComponent }
];

export const EDIT_DAYS_OF_WEEK_COMPONENT_ROUTES: ModuleWithProviders = RouterModule.forChild(routes);