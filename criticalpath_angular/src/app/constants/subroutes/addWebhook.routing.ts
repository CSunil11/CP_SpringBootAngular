import { ModuleWithProviders } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { AddWebhookComponent } from '../../modules/webhook/addWebhook.component';

const routes: Routes = [
     { path: '', component: AddWebhookComponent }
];

export const ADD_WEBHOOK_COMPONENT_ROUTES: ModuleWithProviders = RouterModule.forChild(routes);