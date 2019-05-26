import { ModuleWithProviders } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { LayoutComponent } from '../../common/layout/layout.component';

import * as ROUTES from '../routs';

const routes: Routes = [
     {
	       path: '', 
	       component: LayoutComponent,
	       children: [
	            { path: ROUTES.VIEWALLSTOCKCYCLE, loadChildren: '../submodules/viewAllStockCycles.module#ViewAllStockCyclesModule'},
	            { path: ROUTES.COUNTRY, loadChildren: '../submodules/country.module#CountryModule'},
	            { path: ROUTES.ADD_COUNTRY, loadChildren: '../submodules/addCountry.module#AddCountryModule'},
	            { path: ROUTES.EDIT_COUNTRY, loadChildren: '../submodules/editCountry.module#EditCountryModule'},
	            { path: ROUTES.PROVINCE, loadChildren: '../submodules/province.module#ProvinceModule'},
	            { path: ROUTES.ADD_PROVINCE, loadChildren: '../submodules/addProvince.module#AddProvinceModule'},
	            { path: ROUTES.EDIT_PROVINCE, loadChildren: '../submodules/editProvince.module#EditProvinceModule'},
	            { path: ROUTES.LOCATIONDIVISION, loadChildren: '../submodules/locationDivision.module#LocationDivisionModule'},
	            { path: ROUTES.ADD_LOCATIONDIVISION, loadChildren: '../submodules/addLocationDivision.module#AddLocationDivisionModule'},
	            { path: ROUTES.EDIT_LOCATIONDIVISION, loadChildren: '../submodules/editLocationDivision.module#EditLocationDivisionModule'},
	            { path: ROUTES.TASK_STATUS, loadChildren: '../submodules/taskStatus.module#TaskStatusModule'},
	            { path: ROUTES.ADD_TASK_STATUS, loadChildren: '../submodules/addTaskStatus.module#AddTaskStatusModule'},
	            { path: ROUTES.EDIT_TASK_STATUS, loadChildren: '../submodules/editTaskStatus.module#EditTaskStatusModule'},
	            { path: ROUTES.SYSTEM_SETTING, loadChildren: '../submodules/systemSetting.module#SystemSettingModule'},
	            { path: ROUTES.DASHBOARD, loadChildren: '../submodules/dashboard.module#DashboardModule' },
	            { path: ROUTES.EVENT, loadChildren: '../submodules/event.module#EventModule'},
	            { path: ROUTES.ADD_EVENT, loadChildren: '../submodules/addEvent.module#AddEventModule'},
	            { path: ROUTES.EDIT_EVENT, loadChildren: '../submodules/editEvent.module#EditEventModule'},
	            { path: ROUTES.WEBHOOK, loadChildren: '../submodules/webhook.module#WebhookModule'},
	            { path: ROUTES.ADD_WEBHOOK, loadChildren: '../submodules/addWebhook.module#AddWebhookModule'},
	            { path: ROUTES.EDIT_WEBHOOK, loadChildren: '../submodules/editWebhook.module#EditWebhookModule'},
	            { path: ROUTES.DAYS_OF_WEEK, loadChildren: '../submodules/daysOfWeek.module#DaysOfWeekModule'},
	            { path: ROUTES.EDIT_DAYS_OF_WEEK, loadChildren: '../submodules/editDaysOfWeek.module#EditDaysOfWeekModule'},
	            { path: ROUTES.BRAND, loadChildren: '../submodules/brand.module#BrandModule'},
	            { path: ROUTES.ADD_BRAND, loadChildren: '../submodules/addBrand.module#AddBrandModule'},
	            { path: ROUTES.EDIT_BRAND, loadChildren: '../submodules/editBrand.module#EditBrandModule'},
	            { path: ROUTES.STORE, loadChildren: '../submodules/store.module#StoreModule'},
	            { path: ROUTES.ADD_STORE, loadChildren: '../submodules/addStore.module#AddStoreModule'},
	            { path: ROUTES.EDIT_STORE, loadChildren: '../submodules/editStore.module#EditStoreModule'},
	            { path: ROUTES.CLOSED_DATE, loadChildren: '../submodules/closedDate.module#ClosedDateModule'},
	            { path: ROUTES.ADD_CLOSED_DATE, loadChildren: '../submodules/addClosedDate.module#AddClosedDateModule'},
	            { path: ROUTES.EDIT_CLOSED_DATE, loadChildren: '../submodules/editClosedDate.module#EditClosedDateModule'},
	            { path: ROUTES.STOCK_TAKE_CYCLE, loadChildren: '../submodules/stockTakeCycle.module#StockTakeCycleModule'},
	            { path: ROUTES.ADD_STOCK_TAKE_CYCLE, loadChildren: '../submodules/addStockTakeCycle.module#AddStockTakeCycleModule'},
	            { path: ROUTES.EDIT_STOCK_TAKE_CYCLE, loadChildren: '../submodules/editStockTakeCycle.module#EditStockTakeCycleModule'},
	            { path: ROUTES.CRITICAL_PATH, loadChildren: '../submodules/criticalPath.module#CriticalPathModule'},
	            { path: ROUTES.ADD_CRITICAL_PATH, loadChildren: '../submodules/addCriticalPath.module#AddCriticalPathModule'},
	            { path: ROUTES.EDIT_CRITICAL_PATH, loadChildren: '../submodules/editCriticalPath.module#EditCriticalPathModule'},
	            { path: ROUTES.BRAND_USER, loadChildren: '../submodules/brandUser.module#BrandUserModule'},
	            { path: ROUTES.ADD_BRAND_USER, loadChildren: '../submodules/addBrandUser.module#AddBrandUserModule'},
	            { path: ROUTES.EDIT_BRAND_USER, loadChildren: '../submodules/editBrandUser.module#EditBrandUserModule'},
	            { path: ROUTES.API_LOG, loadChildren: '../submodules/apiLog.module#ApiLogModule'},
	            { path: ROUTES.AOP_LOG, loadChildren: '../submodules/aopLog.module#AopLogModule'},
	            { path: ROUTES.MANAGENOTIFICATION, loadChildren: '../submodules/manageNotification.module#ManageNotificationModule'},
	            { path: ROUTES.ADD_MANAGENOTIFICATION, loadChildren: '../submodules/addManageNotification.module#AddManageNotificationModule'},
	            { path: ROUTES.EDIT_MANAGENOTIFICATION, loadChildren: '../submodules/editManageNotification.module#EditManageNotificationModule'},
//	            { path: ROUTES.ACKERMANPAGE, loadChildren: '../submodules/ackerman.module#AckermanModule' },
	            { path: ROUTES.ACCESS_DENIED, loadChildren: '../submodules/accessDenied.module#AccessDeniedModule'},
	            { path: ROUTES.NOT_AUTHORIZED, loadChildren: '../submodules/notAuthorized.module#NotAuthorizedModule'},
	            { path: ROUTES.PAGE_NOT_FOUND, loadChildren: '../submodules/pageNotFound.module#PageNotFoundModule'}
	       ]
     }
];

export const LAYOUT_COMPONENT_ROUTES: ModuleWithProviders = RouterModule.forChild(routes);
