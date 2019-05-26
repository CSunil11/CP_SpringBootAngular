package com.ackermans.criticalpath.admin.dto;

import org.springframework.beans.BeanUtils;

import com.ackermans.criticalpath.entity.Brand;
import com.ackermans.criticalpath.entity.Store;
import com.ackermans.criticalpath.entity.User;
import com.ackermans.criticalpath.entity.UserLogin;

import lombok.Data;

@Data
public class UserRequest {

	private Long id;
	
	private String name;
	
	private Brand brand;
	
	private Store store;
	
	private String phone;
	
	private UserLogin userLogin;
	
	public static User convertToEntityUser(UserRequest userRequest)  {
		
		if(userRequest == null)
			return null;
		
		User user = new User();
		BeanUtils.copyProperties(userRequest, user);
		
		return user;
	}
	
	public static UserLogin convertToEntityUserLogin(UserRequest userRequest)  {
		
		if(userRequest == null)
			return null;
		
		UserLogin userLogin = new UserLogin();
		BeanUtils.copyProperties(userRequest.getUserLogin(), userLogin);
		
		return userLogin;
	}
	
}
