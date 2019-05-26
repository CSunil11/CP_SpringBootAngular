package com.ackermans.criticalpath.utils;

import java.text.SimpleDateFormat;

public class Constants {

	public static final int EVENT_NAME_LENGTH = 100;
	public static final int PAGE_SIZE = 25;
	
	//Modules
	public static final String PROVINCE = "Province";
	public static final String BRAND = "Brand";
	public static final String STORE = "Store";
	public static final String LOCDIV = "Location Division";
	public static final String USER = "User";
	public static final String COUNTRY = "Country";
	public static final String STOCKTAKERESULT = "Stock take result";
	
	//Action by
	public static final String BYNAME = "By name";
	public static final String BYTHIRDPARTYID = "By ThirdPartyId";
	
	//status
	public static final String RECEIVED = "Received";
	public static final String UPDATED = "Updated";
	public static final String INSERTED = "Inserted";
	public static final String SKIP = "Skipped";
	public static final String FAILED = "Failed";
	public static final String NOTFOUND = "RequiredDataNotFound";
	public static final String SENDING = "Sending";
	public static final String SENT = "Sent";
	
	//Remarks
	public static final String SUCCESSUPD = "Successfully Updated";
	public static final String SUCCESSINS = "Successfully Inserted";
	public static final String ALREADYAVAIL = "Data already available";
	public static final String FAILDIFFIDNAMEAVAIL = "Failed due to name available with different ID";
	public static final String SKIPPED = "Skipped due to insufficient data";
	public static final String VALIDATION_FAILED = "Validation failed due to invalid request";
	public static final String ERRORWHILESAVE = "Error generated while saving the data";
	
	public static final String NA = "NA";
	public static final String DEFAULTPASSWORD = "pa$$w0rd";
	
	public static final String RESULT_STATUS_ATTR[] = { "CompanyCode", "CompanyName", "BranchCode", "BranchName",
			"BranchManagerName", "RamCode", "RamName", "DsmCode", "DsmName", "ApproxMonths", "StockCountResultAmt",
			"CellRegStockAdjAmt", "SwellAllowAmt", "StockCountMiscAmt", "SalesIncAmt", "SalesExcAmt", "ShrinkPerc",
			"ShrinkPercNote", "StockCountResultNote", "CellRegStockAdjNote", "SwellAllowNote", "StockCountMiscNote",
			"SalesIncAmtNote", "SalesExcAmtNote" };
	
	public static final SimpleDateFormat dateFormatYmd = new SimpleDateFormat("yyyy-MM-dd");
}
