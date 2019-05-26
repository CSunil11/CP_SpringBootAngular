package com.ackermans.criticalpath.config;


import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import com.ackermans.criticalpath.enums.UserRole;




@Configuration
public class OAuth2Configuration {

	@Configuration
	@EnableResourceServer
	protected static class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {
		 
		@Autowired
        private CustomLogoutSuccessHandler customLogoutSuccessHandler;
	
		@Override
	    public void configure(HttpSecurity http) throws Exception {
	    	
	    	http.authorizeRequests().and()
	    	
	    	.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and().logout().logoutUrl("/oauth/logout").logoutSuccessHandler(customLogoutSuccessHandler)
			.and().authorizeRequests()
			//Allow below pattern without authorization
			.antMatchers("/noAuth/**").permitAll()
			//Allow for user who has role either User or Admin or Super Admin
			.antMatchers("/common/**").access("hasAnyRole('" + UserRole.ROLE_RAM_USER.name() + "', '"+ UserRole.ROLE_ADMIN.name() +"', '"+ UserRole.ROLE_SUPER_ADMIN.name() +"', '"+ UserRole.ROLE_ADMIN_READONLY.name() +"', '"+ UserRole.ROLE_SM_USER.name() +"', '"+ UserRole.ROLE_DSM_USER.name()+"')")
			//Allow specific url's 
			.antMatchers( "/admin/brand/all-active",
						  "/admin/country/all-active",
						  "/admin/province/all-active",
						  "/admin/daysofweek/all").access("hasAnyRole('" + UserRole.ROLE_RAM_USER.name() + "', '"+ UserRole.ROLE_ADMIN.name() +"', '"+ UserRole.ROLE_SUPER_ADMIN.name() +"', '"+ UserRole.ROLE_ADMIN_READONLY.name() +"', '"+ UserRole.ROLE_SM_USER.name() +"', '"+ UserRole.ROLE_DSM_USER.name()+"')")

			//Allow only for users who has role of Regional Manager
			.antMatchers( "/admin/store/**",
						  "/admin/storeclosedate/**",
						  "/admin/stockTakeCycle/**",
						  "/admin/criticalPath/**").access("hasAnyRole('" + UserRole.ROLE_RAM_USER.name() + "', '"+ UserRole.ROLE_ADMIN.name() +"', '"+ UserRole.ROLE_SUPER_ADMIN.name() +"','"+ UserRole.ROLE_ADMIN_READONLY.name() +"', '"+ UserRole.ROLE_SM_USER.name() +"', '"+ UserRole.ROLE_DSM_USER.name()+"')")
			//Allow only for users who has role either Admin or Super Admin
			.antMatchers("/admin/brand/**", 
						  "/admin/webhook/**",
					  	  "/admin/country/**",
					  	  "/admin/province/**",
					  	  "/admin/event/**",
					  	  "/admin/taskstatus/**",
					  	  "/admin/daysofweek/**",
					  	  "/admin/user/**",
					  	  "/admin/apiLogger/**",
					  	  "/admin/aopLogger/**",
					  	  "/admin/managenotification/**",
					  	  "/admin/locationdivision/**").access("hasAnyRole('" + UserRole.ROLE_ADMIN.name() + "','" + UserRole.ROLE_SUPER_ADMIN.name() +"', '"+ UserRole.ROLE_ADMIN_READONLY.name() +"', '"+ UserRole.ROLE_SM_USER.name() +"', '"+ UserRole.ROLE_RAM_USER.name() +"', '"+ UserRole.ROLE_DSM_USER.name()+"')")
			//Allow access to either RAM/DSM or SM user
			.antMatchers("/user/**").access("hasAnyRole('" + UserRole.ROLE_RAM_USER.name() + "','" + UserRole.ROLE_SM_USER.name() +"', '"+ UserRole.ROLE_DSM_USER.name()+"')")
			//Allow access to Super Admin only for Import data API
			.antMatchers("/data/**").access("hasRole('"+UserRole.ROLE_API_USER+"')")
			//Deny any other request
	    	.anyRequest().denyAll()
	    	.and().csrf().disable()
	    	.exceptionHandling();
	    }
	}
	
	
	@Configuration
    @EnableAuthorizationServer
    protected static class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter implements EnvironmentAware {

        private static final String PROP_CLIENTID = "client";
        private static final String PROP_SECRET = "secret";
        private static final int PROP_TOKEN_VALIDITY_SECONDS = 60 * 60 * 12;

        @Autowired
        private DataSource dataSource;
        
        @Autowired
        private PasswordEncoder passwordEncoder;
        
        @Bean
        public TokenStore tokenStore() {
            return new JdbcTokenStore(dataSource);
        }

        @Autowired
        @Qualifier("myAuthenticationManager")
        private AuthenticationManager authenticationManager;

        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
            endpoints
	        	.tokenStore(tokenStore())
	        	.authenticationManager(authenticationManager);
        }
        
        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        	
        	String secretEncoded = passwordEncoder.encode(PROP_SECRET);
        	
        	clients
	            .inMemory()
	            .withClient(PROP_CLIENTID)
	            .scopes("read", "write", "trust")
	            .authorizedGrantTypes("password", "authorization_code", "refresh_token", "implicit")                    
	            .secret(secretEncoded)
	            .accessTokenValiditySeconds(PROP_TOKEN_VALIDITY_SECONDS)
	            .refreshTokenValiditySeconds(PROP_TOKEN_VALIDITY_SECONDS);
        }

		@Override
		public void setEnvironment(Environment arg0) {
		}
    }
}
