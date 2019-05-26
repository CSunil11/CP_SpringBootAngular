package com.ackermans.criticalpath.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ackermans.criticalpath.entity.UserLogin;
import com.ackermans.criticalpath.enums.UserRole;

@Repository
public interface UserLoginRepository extends JpaRepository<UserLogin, Long> {

	UserLogin findByEmailIgnoreCase(String email);
	
	/**
	 * Get number of records which has given email but ignore case
	 * 
	 * @param name
	 * @return
	 */
	long countByEmailIgnoreCase(String email);
	
	/**
	 * Get number of records which has given email but ignore case 
	 * and id should not be equal to ignoreId
	 * 
	 * @param email
	 * @param ignoreId
	 * @return
	 */
	@Query("SELECT COUNT(*) FROM UserLogin WHERE email = :email AND id != :id")
	long countByEmailAndIgnoreId(String email, Long id);

	@Query(value = "select role from user_login where id = (select user_login_id from user where id = :id)", nativeQuery = true)
	String findRoleByUserId(Long id);
	/**
	 * get email of ram user and dsm user by store id
	 * @param storeId
	 * @return
	 */
	@Query(value="select user_login.email as email from user_login inner join user on user.user_login_id = user_login.id"+
			" where user.id = (select ram_user_id from store where id =:storeId) or"+
			" user.id = (select user_id from location_division where id =(SELECT location_division_id FROM store where id =:storeId ))", nativeQuery=true)
	List getDsmAndRamEmail(Long storeId);
	
	@Query(value="select user_login.email as email from user_login inner join user on user.user_login_id = user_login.id"+
			" where"+
			" user.id = (select user_id from location_division where id =(SELECT location_division_id FROM store where id =:storeId ))", nativeQuery=true)
	String getDsmEmail(Long storeId);
	
	@Query(value="select user_login.email as email from user_login inner join user on user.user_login_id = user_login.id"+
			" where"+
			" user.id = (select ram_user_id from store where id =:storeId)", nativeQuery=true)
	String getRamEmail(Long storeId);
	
}
