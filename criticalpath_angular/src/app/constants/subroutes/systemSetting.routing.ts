import { ModuleWithProviders } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { SystemSettingComponent } from '../../systemSetting/systemSetting.component';

const routes: Routes = [
     { path: '', component: SystemSettingComponent }
];

export const SYSTEM_SETTING_COMPONENT_ROUTES: ModuleWithProviders = RouterModule.forChild(routes);