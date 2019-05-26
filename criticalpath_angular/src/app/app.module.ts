import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { FormsModule, ReactiveFormsModule,FormGroup } from '@angular/forms';
import { TypeaheadModule } from 'ngx-bootstrap/typeahead';
import {platformBrowserDynamic} from '@angular/platform-browser-dynamic';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import { HttpClientModule } from '@angular/common/http';
import { HttpModule } from '@angular/http';
import { DatePipe } from '@angular/common';

import { AppComponent } from './app.component';


import { LoginRepository } from './repository/login.repository';
import { CommonRepository } from './repository/common.repository';
import { LinksRepository } from './repository/links.repository';
import { SystemSettingRepository } from './repository/systemSetting.repository';

import { CommonService } from './service/common.service';
import { LocalStorageService } from './service/localStorage.service';
import { SystemSettingService } from './service/systemSetting.service';
import { CountryService } from './service/country.service';
import { ProvinceService } from './service/province.service';
import { LocationDivisionService } from './service/locationDivision.service';
import { TaskStatusService } from './service/taskStatus.service';
import { EventService } from './service/event.service';
import { APIClientService } from './service/APIClientService';
import { WebhookService } from './service/webhook.service';
import { DaysOfWeekService } from './service/daysOfWeek.service';
import { BrandService } from './service/brand.service';
import { StoreService } from './service/store.service';
import { ClosedDateService } from './service/closedDate.service';
import { StockTakeCycleService } from './service/stockTakeCycle.service';
import { CriticalPathService } from './service/criticalPath.service';
import { BrandUserService } from './service/brandUser.service';
import { ApiLogService } from './service/apiLog.service';
import { AopLogService } from './service/aopLog.service';
import { ManageNotificationService } from './service/manageNotification.service';

import * as ROUTS from './constants/routs';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    FormsModule, 
    ReactiveFormsModule,
    HttpClientModule,
    HttpModule,
    TypeaheadModule,
    RouterModule.forRoot(ROUTS.routes, { useHash: true })
  ],
  providers: [LoginRepository, CommonRepository, CommonService, LocalStorageService, SystemSettingService, SystemSettingRepository,
              CountryService, ProvinceService, TaskStatusService, EventService, APIClientService, BrandUserService, 
              WebhookService, DaysOfWeekService, BrandService, StoreService, ApiLogService, AopLogService,
              ClosedDateService, DatePipe, StockTakeCycleService,CriticalPathService, LocationDivisionService,ManageNotificationService],
  bootstrap: [AppComponent]
})
export class AppModule { }
