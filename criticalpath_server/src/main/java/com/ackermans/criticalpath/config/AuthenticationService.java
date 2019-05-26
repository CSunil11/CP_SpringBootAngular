package com.ackermans.criticalpath.config;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ackermans.criticalpath.aop.service.AuditService;
import com.ackermans.criticalpath.entity.UserLogin;
import com.ackermans.criticalpath.service.AckCriticalPathService;
import com.ackermans.criticalpath.service.UserLoginService;

@Service("authenticationService")
public class AuthenticationService implements UserDetailsService {
    
    @Autowired
    private UserLoginService userLoginService;
    
    @Autowired
	private AuditService auditService;

	private final Logger logger = LogManager.getFormatterLogger(AuthenticationService.class);
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
    	UserLogin userLogin = userLoginService.findByEmail(username);
    	
    	if(userLogin == null || !userLogin.getIsActive() || userLogin.getIsDelete())
			throw new UsernameNotFoundException("User doesn't exists.");
    	else {
    		try {
    			auditService.generateAuditMessageForLogin(username);
    		} catch (Exception e) {
    			logger.debug("Error in sendEmailUser while call audit service " + e.getMessage());
    		}
    	}
    	
		return userLogin;
    }
}