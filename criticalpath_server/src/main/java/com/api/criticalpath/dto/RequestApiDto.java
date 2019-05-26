package com.api.criticalpath.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class RequestApiDto {
	
	private String type;
	
	@JsonProperty("MsgID")
	private String msgId;
	
	private List<RequestApiDataDto> data;

}
