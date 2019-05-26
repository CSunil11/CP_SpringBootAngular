import { environment } from '../../environments/environment';

'use strict';

export const BASE_URL : string = environment.apiUrl;

//Outh
export const LOGIN : string = BASE_URL + "oauth/token";
export const AUTHORIZATION : string = "Basic Y2xpZW50OnNlY3JldA==";
export const CLIENTID : string = "client";
export const SECRET : string = "secret";
export const BEARER : string = "bearer ";

export const GET_USER : string = BASE_URL + "common/getUserLogin";
export const GET_USER_DETAILS : string = BASE_URL + "common/getUserDetails";
export const GET_ADMIN_DETAILS : string = BASE_URL + "common/getAdminDetails";
export const EMAIL_SETTING : string = BASE_URL + "systemSetting/emailSetting";
export const GET_ALL_PERMISSIONS : string = BASE_URL + "common/getAllPermissions";

export const ADD_COUNTRY : string = BASE_URL + "admin/country/save";
export const GET_COUNTRY_LIST : string = BASE_URL + "admin/country/all";
export const GET_COUNTRY_DATA : string = BASE_URL + "admin/country/getCountryData";
export const GET_ACTIVE_COUNTRY : string = BASE_URL + "admin/country/all-active";
export const GET_DELETE_COUNTRY : string = BASE_URL + "admin/country/all-deleteCountry";
export const DELETE_COUNTRY : string = BASE_URL + "admin/country/deleteCountry";
export const RESTORE_COUNTRY : string = BASE_URL + "admin/country/restoreCountry";
export const SEARCH_ACTIVE_COUNTRY : string = BASE_URL + "admin/country/search-active";
export const SEARCH_DELETE_COUNTRY : string = BASE_URL + "admin/country/search-delete";

export const ADD_PROVINCE : string = BASE_URL + "admin/province/save";
export const GET_PROVINCE_LIST : string = BASE_URL + "admin/province/all";
export const GET_PROVINCE_DATA : string = BASE_URL + "admin/province/getProvinceData";
export const GET_ACTIVE_PROVINCE : string = BASE_URL + "admin/province/all-active";
export const GET_DELETE_PROVINCE : string = BASE_URL + "admin/province/all-deleteProvince";
export const DELETE_PROVINCE : string = BASE_URL + "admin/province/deleteProvince";
export const RESTORE_PROVINCE : string = BASE_URL + "admin/province/restoreProvince";
export const GET_PROVINCE_OF_COUNTRY : string  = BASE_URL + "admin/province/getProvinceOfCountry";
export const SEARCH_ACTIVE_PROVINCE : string = BASE_URL + "admin/province/search-active";
export const SEARCH_DELETE_PROVINCE : string = BASE_URL + "admin/province/search-delete";

export const ADD_LOCATIONDIVISION : string = BASE_URL + "admin/locationdivision/save";
export const GET_LOCATIONDIVISION_LIST : string = BASE_URL + "admin/locationdivision/all";
export const GET_LOCATIONDIVISION_DATA : string = BASE_URL + "admin/locationdivision/getLocationDivisionData";
export const GET_ACTIVE_LOCATIONDIVISION : string = BASE_URL + "admin/locationdivision/all-active";
export const GET_DELETE_LOCATIONDIVISION : string = BASE_URL + "admin/locationdivision/all-deleteLocationDivision";
export const DELETE_LOCATIONDIVISION : string = BASE_URL + "admin/locationdivision/delete";
export const RESTORE_LOCATIONDIVISION : string = BASE_URL + "admin/locationdivision/restore";
export const GET_LOCATIONDIVISION_OF_PROVINCE : string = BASE_URL + "admin/locationdivision/getLocationDivisionOfProvince";
export const SEARCH_ACTIVE_LOC_DIV : string = BASE_URL + "admin/locationdivision/search-active";
export const SEARCH_DELETE_LOC_DIV : string = BASE_URL + "admin/locationdivision/search-delete";

export const ADD_TASK_STATUS : string = BASE_URL + "admin/taskstatus/save";
export const GET_TASK_STATUS_LIST : string = BASE_URL + "admin/taskstatus/all";
export const GET_ACTIVE_TASK_STATUS_LIST : string = BASE_URL + "admin/taskstatus/all-active";
export const GET_DELETE_TASK_STATUS_LIST : string = BASE_URL + "admin/taskstatus/all-deleteTaskStatus";
export const GET_TASK_STATUS_DATA : string = BASE_URL + "admin/taskstatus/getTaskStatusData";
export const EDIT_TASK_STATUS : string = BASE_URL + "admin/taskstatus/editTaskStatus";
export const DELETE_TASK_STATUS : string = BASE_URL + "admin/taskstatus/deleteTaskStatus";
export const RESTORE_TASK_STATUS : string = BASE_URL + "admin/taskstatus/restoreTaskStatus";

export const ADD_EVENT : string = BASE_URL + "admin/event/save";
export const GET_EVENT_LIST : string = BASE_URL + "admin/event/all";
export const GET_EVENT_DATA : string = BASE_URL + "admin/event/getEventData";
export const DELETE_EVENT : string = BASE_URL + "admin/event/deleteEvent";
export const RESTORE_EVENT : string = BASE_URL + "admin/event/restoreEvent";
export const GET_ACTIVE_EVENT : string = BASE_URL + "admin/event/all-active";
export const GET_DELETE_EVENT : string = BASE_URL + "admin/event/all-delete";

export const ADD_WEBHOOK : string = BASE_URL + "admin/webhook/save";
export const GET_WEBHOOK_LIST : string = BASE_URL + "admin/webhook/all";
export const GET_WEBHOOK_DATA : string = BASE_URL + "admin/webhook/getWebhookData";
export const DELETE_WEBHOOK : string = BASE_URL + "admin/webhook/deleteWebhook";
export const RESTORE_WEBHOOK : string = BASE_URL + "admin/webhook/restoreWebhook";
export const GET_ACTIVE_WEBHOOK : string = BASE_URL + "admin/webhook/all-active";
export const GET_DELETE_WEBHOOK : string = BASE_URL + "admin/webhook/all-delete";

export const GET_DAYS_OF_WEEK_LIST : string = BASE_URL + "admin/daysofweek/all";
export const ADD_DAYS_OF_WEEK : string = BASE_URL + "admin/daysofweek/save";
export const GET_DAYS_OF_WEEK_DATA : string = BASE_URL + "admin/daysofweek/getDaysOfWeekData";
export const DELETE_DAYS_OF_WEEK : string = BASE_URL + "admin/daysofweek/deleteDaysOfWeek";
export const RESTORE_DAYS_OF_WEEK : string = BASE_URL + "admin/daysofweek/restoreDaysOfWeek";
export const GET_ACTIVE_DAYS_OF_WEEK : string = BASE_URL + "admin/daysofweek/all-active";
export const GET_DELETE_DAYS_OF_WEEK : string = BASE_URL + "admin/daysofweek/all-delete";

export const ADD_BRAND : string = BASE_URL + "admin/brand/save";
export const GET_BRAND_LIST : string = BASE_URL + "admin/brand/all";
export const GET_BRAND_DATA : string = BASE_URL + "admin/brand/getBrandData";
export const DELETE_BRAND : string = BASE_URL + "admin/brand/deleteBrand";
export const RESTORE_BRAND : string = BASE_URL + "admin/brand/restoreBrand";
export const GET_ACTIVE_BRAND : string = BASE_URL + "admin/brand/all-active";
export const GET_DELETE_BRAND : string = BASE_URL + "admin/brand/all-delete";
export const SEARCH_BRAND : string = BASE_URL + "admin/brand/search";
export const SEARCH_ACTIVE_BRAND : string = BASE_URL + "admin/brand/search-active";
export const SEARCH_DELETE_BRAND : string = BASE_URL + "admin/brand/search-delete";
export const GET_ACTIVE_STATUS_BRAND : string = BASE_URL + "admin/brand/getActiveStatusBrands";

export const ADD_STORE : string = BASE_URL + "admin/store/save";
export const GET_STORE_LIST : string = BASE_URL + "admin/store/all";
export const GET_STORE_DATA : string = BASE_URL + "admin/store/get";
export const DELETE_STORE : string = BASE_URL + "admin/store/deleteStore";
export const RESTORE_STORE : string = BASE_URL + "admin/store/restoreStore";
export const GET_ACTIVE_STORE : string = BASE_URL + "admin/store/all-active";
export const GET_DELETE_STORE : string = BASE_URL + "admin/store/all-delete";
export const SEARCH_STORE : string = BASE_URL + "admin/store/search";
export const SEARCH_ACTIVE_STORE : string = BASE_URL + "admin/store/search-active";
export const SEARCH_DELETE_STORE : string = BASE_URL + "admin/store/search-delete";

export const ADD_CLOSED_DATE : string = BASE_URL + "admin/storeclosedate/save";
export const GET_CLOSED_DATE_LIST : string = BASE_URL + "admin/storeclosedate/all";
export const GET_CLOSED_DATE_DATA : string = BASE_URL + "admin/storeclosedate/getStoreClosedData";
export const DELETE_CLOSED_DATE : string = BASE_URL + "admin/storeclosedate/deleteCloseDate";
export const RESTORE_CLOSED_DATE : string = BASE_URL + "admin/storeclosedate/restoreCloseDate";
export const GET_ACTIVE_CLOSED_DATE : string = BASE_URL + "admin/storeclosedate/all-active";
export const GET_DELETE_CLOSED_DATE : string = BASE_URL + "admin/storeclosedate/all-delete";
export const GET_CLOSED_DATE_BY_STORE : string = BASE_URL + "admin/storeclosedate/getbystore";
export const GET_CLOSED_DATE_LIST_BY_STORE : string = BASE_URL + "admin/storeclosedate/getcloseddatebystore";
export const GET_BY_BRAND : string = BASE_URL + "admin/store/getbybrand";

export const ADD_STOCK_TAKE_CYCLE : string = BASE_URL + "admin/stockTakeCycle/save";
export const GET_STOCK_TAKE_CYCLE_LIST : string = BASE_URL + "admin/stockTakeCycle/all";
export const GET_STOCK_TAKE_CYCLE_DATA : string = BASE_URL + "admin/stockTakeCycle/getStockTakeCycleData";
export const DELETE_STOCK_TAKE_CYCLE : string = BASE_URL + "admin/stockTakeCycle/deleteStockTakeCycle";
export const HIDE_STOCK_TAKE_CYCLE : string = BASE_URL + "admin/stockTakeCycle/hideStockTakeCycle";
export const RESTORE_STOCK_TAKE_CYCLE : string = BASE_URL + "admin/stockTakeCycle/restoreStockTakeCycle";
export const GET_ACTIVE_STOCK_TAKE_CYCLE : string = BASE_URL + "admin/stockTakeCycle/all-active";
export const GET_ACTIVE_STOCK_TAKE_CYCLE_PER_STORE : string = BASE_URL + "admin/stockTakeCycle/all-active-per-store";
export const GET_STOCK_TAKE_CYCLE_PER_STORE : string = BASE_URL + "admin/stockTakeCycle/cycles-per-store";
export const GET_STOCK_TAKE_CYCLE_STATUS_PER_STORE : string = BASE_URL + "admin/stockTakeCycle/cycles-per-store/status";
export const GET_STORE_PER_DSM_USER : string = BASE_URL + "admin/stockTakeCycle/store-per-dsm-user";
export const GET_STOCK_TAKE_CYCLE_DSM_USER : string = BASE_URL + "admin/stockTakeCycle/stock-cycle-dsm-user";
export const GET_STOCK_TAKE_CYCLE_STATUS_DSM_USER : string = BASE_URL + "admin/stockTakeCycle/stock-cycle-dsm-user/status";
export const GET_DELETE_STOCK_TAKE_CYCLE : string = BASE_URL + "admin/stockTakeCycle/all-delete";
export const ADD_STOCK_TAKE_DATE : string = BASE_URL + "admin/stockTakeCycle/add-stockTakeDate";
export const GET_STOCK_TAKE_CYCLE_BY_USER : string = BASE_URL + "admin/stockTakeCycle/StockTakeCycleByUser";

export const ADD_CRITICAL_PATH : string = BASE_URL + "admin/criticalPath/save";
export const GET_CRITICAL_PATH_LIST : string = BASE_URL + "admin/criticalPath/all";
export const GET_CRITICAL_PATH_DATA : string = BASE_URL + "admin/criticalPath/getCriticalPathData";
export const DELETE_CRITICAL_PATH : string = BASE_URL + "admin/criticalPath/deleteCriticalPath";
export const RESTORE_CRITICAL_PATH : string = BASE_URL + "admin/criticalPath/restoreCriticalPath";
export const GET_ACTIVE_CRITICAL_PATH : string = BASE_URL + "admin/criticalPath/all-active";
export const GET_DELETE_CRITICAL_PATH : string = BASE_URL + "admin/criticalPath/all-delete";
export const GET_CRITICAL_PATH_DATA_OF_CYCLE : string = BASE_URL + "admin/criticalPath/getCriticalPathOfStockTakeCycle";
export const UPDATE_CRITICAL_PATH_STATUS : string = BASE_URL + "admin/criticalPath/updateCriticalPathStatus";

export const ADD_BRAND_USER : string = BASE_URL + "admin/user/save";
export const GET_ALL_BY_BRAND : string = BASE_URL + "admin/user/getAllByBrand";
export const GET_ALL_ACTIVE_BRAND_USER : string = BASE_URL + "admin/user/getAllActiveByBrand";
export const GET_ALL_DELETE_BRAND_USER : string = BASE_URL + "admin/user/getAllDeleteByBrand";
export const GET_BRAND_USER_DATA : string = BASE_URL + "admin/user/getBrandUserData";
export const DELETE_BRAND_USER : string = BASE_URL + "admin/user/deleteUser";
export const RESTORE_BRAND_USER : string = BASE_URL + "admin/user/restoreUser";
export const SEARCH_BRAND_USER : string = BASE_URL + "admin/user/search";
export const SEARCH_ACTIVE_BRAND_USER : string = BASE_URL + "admin/user/search-active";
export const SEARCH_DELETE_BRAND_USER : string = BASE_URL + "admin/user/search-delete";
export const GET_BRAND_USER_LIST : string = BASE_URL + "admin/user/getBrandUserByPageNumber";
export const GET_DSM_USER_LIST : string = BASE_URL + "admin/user/unassigned/dsm";
export const GET_RAM_USER_LIST : string = BASE_URL + "admin/user/all-active/ram";
export const USER_LOGOUT : string = BASE_URL + "admin/user/logout";

export const UPLOAD_COUNTRY_DATA : string = BASE_URL + "admin/country/upload";
export const UPLOAD_TASK_DATA : string = BASE_URL + "admin/taskstatus/upload";
export const UPLOAD_EVENT_DATA : string = BASE_URL + "admin/event/upload";

export const GET_ACTIVE_LOCDIV : string = BASE_URL + "admin/locationdivision/all-active";

export const IS_ACTIVE_CYCLE_AVAIL_ACK : string = BASE_URL + "user/ackStockTakeCycle/isActiveCycleAvail";
export const GET_ACK_STOCK_TAKE_CYCLE_DSM_USER : string = BASE_URL + "user/ackStockTakeCycle/stock-cycle-dsm-user";
export const GET_ACK_STOCK_TAKE_CYCLE_STATUS_DSM_USER : string = BASE_URL + "user/ackStockTakeCycle/stock-cycle-dsm-user/status";
export const ADD_CYCLE_ACK : string = BASE_URL + "user/ackStockTakeCycle/addCycle";
export const GET_ACK_STOCK_TAKE_CYCLE_DATA : string = BASE_URL + "user/ackStockTakeCycle/getAckCycleById";
export const CALCULATE_STARTDATE : string = BASE_URL + "user/ackStockTakeCycle/calculate-startdate";
export const GET_ALL_DATELIST : string = BASE_URL + "user/ackStockTakeCycle/getalldatelist";
export const CALCULATE_MULTIPLE_STARTDATE : string = BASE_URL + "user/ackStockTakeCycle/calculate-multiple-startdate";
export const ADD_MULTIPLE_STARTDATE : string = BASE_URL + "user/ackStockTakeCycle/add-multiple-startdate";
export const DELETE_AND_CREATE_NEW_STOCK_TAKE : string = BASE_URL + "user/ackStockTakeCycle/deleteAndCreateNewStockTake";
export const EDIT_NEW_STOCK_TAKE : string = BASE_URL + "user/ackStockTakeCycle/editNewStockTake";
export const GET_UPCOMING_ACK_CYCLE_STARTDATE : string = BASE_URL + "user/ackStockTakeCycle/getUpcomingCycleStartDate";
export const GET_STORE_STOCK_TAKE_DATE : string = BASE_URL + "user/ackStockTakeCycle/getStoreStockTakeDate";


export const GET_CRITICAL_PATH_DATA_OF_ACKCYCLE : string = BASE_URL + "user/ackCriticalPath/getAllByAckCycleId";
export const UPDATE_ACK_CRITICAL_PATH_STATUS : string = BASE_URL + "user/ackCriticalPath/updateAckTaskStatus";
export const GET_STOCK_TAKE_RESULT : string = BASE_URL + "user/ackCriticalPath/getStockTakeResult";
export const UPDATE_SM_STATUS_OF_STOCK_TAKE_RESULT : string = BASE_URL + "user/ackCriticalPath/updateStatus";
export const UPDATE_STATUS_DECLINE_BY_RAM : string = BASE_URL + "user/ackCriticalPath/updateDeclineStatusByRAM";

export const SEARCH : string = BASE_URL + "admin/apiLogger/searchByTransOrMsgId";

export const SEARCH_AOP_LOG : string = BASE_URL + "admin/aopLogger/searchByDate";

export const ADD_MANAGE_NOTIFICATION : string = BASE_URL + "admin/managenotification/save";
export const GET_MANAGE_NOTIFICATION_LIST : string = BASE_URL + "admin/managenotification/all";
export const GET_MANAGE_NOTIFICATION_DATA : string = BASE_URL + "admin/managenotification/getManageNotificationData";
export const GET_ACTIVE_MANAGE_NOTIFICATION : string = BASE_URL + "admin/managenotification/all-active";
export const GET_DELETE_MANAGE_NOTIFICATION : string = BASE_URL + "admin/managenotification/all-deleteManageNotification";
export const DELETE_MANAGE_NOTIFICATION : string = BASE_URL + "admin/managenotification/deleteManageNotification";
export const RESTORE_MANAGE_NOTIFICATION : string = BASE_URL + "admin/managenotification/restoreManageNotification";

