import { ModuleWithProviders } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { EditWebhookComponent } from '../../modules/webhook/editWebhook.component';

const routes: Routes = [
     { path: '', component: EditWebhookComponent }
];

export const EDIT_WEBHOOK_COMPONENT_ROUTES: ModuleWithProviders = RouterModule.forChild(routes);