import { ModuleWithProviders } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { WebhookComponent } from '../../modules/webhook/webhook.component';

import * as ROUTES from '../routs';

const routes: Routes = [
     { 	 path: '', 
    	 component: WebhookComponent,
    	 children: [
    	            { path: ROUTES.ADD_WEBHOOK, loadChildren: '../submodules/addWebhook.module#AddWebhookModule'},
//    	            { path: ROUTES.EDIT_WEBHOOK, loadChildren: '../submodules/editWebhook.module#EditWebhookModule'}
    	           ]
     }
];

export const WEBHOOK_COMPONENT_ROUTES: ModuleWithProviders = RouterModule.forChild(routes);