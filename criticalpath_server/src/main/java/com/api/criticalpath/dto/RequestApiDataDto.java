package com.api.criticalpath.dto;

import javax.persistence.Entity;

import com.ackermans.criticalpath.entity.BaseEntity;
import com.ackermans.criticalpath.entity.Brand;
import com.ackermans.criticalpath.entity.Country;
import com.ackermans.criticalpath.entity.LocationDivision;
import com.ackermans.criticalpath.entity.Province;
import com.ackermans.criticalpath.entity.Store;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
public class RequestApiDataDto {
	
	private String id;
	
	private String name;
	
	private String code;
	
	private String email;
	
	private String brand_id;
	
	private String store_id;
	
	private String country_code;
	
	private String province_id;
	
	private String location_division_id;
	
	private String sm_id;
	
	private String dsm_id;
	
	private String ram_id;
	
	//For Stock_take_result API specially
	@JsonProperty("CompanyCode")
	private String company_code;
	
	@JsonProperty("CompanyName")
	private String company_name;
	
	@JsonProperty("BranchCode")
	private String branch_code;

	@JsonProperty("BranchName")
	private String branch_name;

	@JsonProperty("BranchManagerName")
	private String branch_manager_name;

	@JsonProperty("StockCountDate")
	private String stock_count_date;

	@JsonProperty("RamCode")
	private String ram_code;

	@JsonProperty("RamName")
	private String ram_name;

	@JsonProperty("DsmCode")
	private String dsm_code;

	@JsonProperty("DsmName")
	private String dsm_name;

	@JsonProperty("PrevStockCountDate")
	private String prev_stock_count_date;

	@JsonProperty("ResultDate")
	private String result_date;

	@JsonProperty("ApproxMonths")
	private String approx_months;

	@JsonProperty("StockCountResultAmt")
	private String stock_count_result_amt;
	
	@JsonProperty("StockCountResultNote")
	private String stock_count_result_note;

	@JsonProperty("CellRegStockAdjAmt")
	private String cell_reg_stock_adj_amt;
	
	@JsonProperty("CellRegStockAdjNote")
	private String cell_reg_stock_adj_note;

	@JsonProperty("SwellAllowAmt")
	private String swell_allow_amt;
	
	@JsonProperty("SwellAllowNote")
	private String swell_allow_note;

	@JsonProperty("StockCountMiscAmt")
	private String stock_count_misc_amt;
	
	@JsonProperty("StockCountMiscNote")
	private String stock_count_misc_note;

	@JsonProperty("SalesIncAmt")
	private String sales_inc_amt;
	
	@JsonProperty("SalesIncAmtNote")
	private String sales_inc_amt_note;

	@JsonProperty("SalesExcAmt")
	private String sales_exc_amt;
	
	@JsonProperty("SalesExcAmtNote")
	private String sales_exc_amt_note;

	@JsonProperty("ShrinkPerc")
	private String shrink_perc;
	
	@JsonProperty("ShrinkPercNote")
	private String shrink_perc_note;
	
	
	
}
