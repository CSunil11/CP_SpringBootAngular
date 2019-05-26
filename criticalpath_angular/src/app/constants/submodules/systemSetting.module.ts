import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule,FormGroup } from '@angular/forms';

import { SystemSettingComponent } from '../../systemSetting/systemSetting.component';
import { SYSTEM_SETTING_COMPONENT_ROUTES } from '../subroutes/systemSetting.routing';

import { SharedModule } from './shared.module';

import { UiSwitchModule } from 'ngx-toggle-switch';

@NgModule({
    imports: [
        CommonModule,
        SYSTEM_SETTING_COMPONENT_ROUTES,
        FormsModule,
        ReactiveFormsModule,
        SharedModule,
        UiSwitchModule
    ],
    declarations: [SystemSettingComponent]
})

export class SystemSettingModule { }
