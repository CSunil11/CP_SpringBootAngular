package com.ackermans.criticalpath.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.ackermans.criticalpath.enums.UserRole;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
public class UserLogin extends BaseEntity implements UserDetails {
	
	private static final long serialVersionUID = 1L;
	
	@Column(unique=true)
	private String email;

	private String password;

	@Enumerated(EnumType.STRING)
	private UserRole role;
	
	@ManyToMany
	@LazyCollection( LazyCollectionOption.FALSE)
	@OrderBy("displayOrder")
	private List<Permission> permissions;
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		List<GrantedAuthority> grantedAuths = new ArrayList<GrantedAuthority>();
		grantedAuths.add(new SimpleGrantedAuthority(role.name()));
		
		//If custom permissions are there then add those along with role.
		if(permissions != null)
			permissions.stream().forEach(permission -> grantedAuths.add(new SimpleGrantedAuthority(permission.getPermKey())));
		
		return grantedAuths;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return this.isActive;
	}

	@Override
	public String getEntityName() {
		return this.getClass().getSimpleName();
	}
}
