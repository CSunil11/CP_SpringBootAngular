package com.ackermans.criticalpath.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ackermans.criticalpath.entity.Store;
import com.ackermans.criticalpath.entity.User;
import com.ackermans.criticalpath.enums.UserRole;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	/**
	 * Get all brands of a user
	 * 
	 * @return
	 */
	Page<User> findAllBrandBybrand_Id(Long brandId, Pageable pageable);

	/**
	 * Get all active user of brands
	 * 
	 * @return
	 */
	Page<User> findAllActiveBrandBybrand_IdAndIsActiveTrue(Long brandId, Pageable pageable);
	
	/**
	 * Get all active user of brands by userRole
	 * 
	 * @return
	 */
	Page<User> findAllActiveBrandBybrand_IdAndIsActiveTrueAndUserLogin_Role(Long brandId, Pageable pageable, UserRole searchRole);
	
	/**
	 * Get all deleted user of brands
	 * 
	 * @return
	 */
	Page<User> findAllActiveBrandBybrand_IdAndIsActiveFalseAndIsDeleteTrue(Long brandId, Pageable pageable);
	
	/**
	 * Get all deleted user of brands
	 * 
	 * @return
	 */
	Page<User> findAllActiveBrandBybrand_IdAndIsActiveFalseAndIsDeleteTrueAndUserLogin_Role(Long brandId, Pageable pageable, UserRole searchRole);

	/**
	 * Searches for the record starting with name or email
	 * 
	 * @param searchstr
	 * @param brandId
	 * @return
	 */
	@Query(value = "Select * from  user u " + " INNER JOIN user_login ul ON u.user_login_id = ul.id"
			+ " where u.brand_id = :brandId and ((lower(ul.email) like lower(concat(:searchstr,'%'))) or (lower(u.name) like lower(concat(:searchstr,'%'))))", nativeQuery = true)
	Page<User> search(String searchstr, Long brandId, Pageable pageable);
	
	/**
	 * Searches for the active user record starting with name or email
	 * 
	 * @param searchstr
	 * @param brandId
	 * @return
	 */
	@Query(value = "Select * from  user u " + " INNER JOIN user_login ul ON u.user_login_id = ul.id"
			+ " where u.is_active = 1 AND u.brand_id = :brandId and ((lower(ul.email) like lower(concat(:searchstr,'%'))) or (lower(u.name) like lower(concat(:searchstr,'%'))))", nativeQuery = true)
	Page<User> searchActiveUser(String searchstr, Long brandId, Pageable pageable);
	
	@Query(value = "Select * from  user u " + " INNER JOIN user_login ul ON u.user_login_id = ul.id"
			+ " where u.is_active = 1 AND u.brand_id = :brandId AND ul.role = :searchRole and ((lower(ul.email) like lower(concat(:searchstr,'%'))) or (lower(u.name) like lower(concat(:searchstr,'%'))))", nativeQuery = true)
	Page<User> searchActiveUser(String searchstr, Long brandId, Pageable pageable, String searchRole);
	
	/**
	 * Searches for the deleted user record starting with name or email
	 * 
	 * @param searchstr
	 * @param brandId
	 * @return
	 */
	@Query(value = "Select * from  user u " + " INNER JOIN user_login ul ON u.user_login_id = ul.id"
			+ " where u.is_delete = 1 AND u.brand_id = :brandId and ((lower(ul.email) like lower(concat(:searchstr,'%'))) or (lower(u.name) like lower(concat(:searchstr,'%'))))", nativeQuery = true)
	Page<User> searchDeletedUser(String searchstr, Long brandId, Pageable pageable);
	
	@Query(value = "Select * from  user u " + " INNER JOIN user_login ul ON u.user_login_id = ul.id"
			+ " where u.is_delete = 1 AND u.brand_id = :brandId AND ul.role = :searchRole  and ((lower(ul.email) like lower(concat(:searchstr,'%'))) or (lower(u.name) like lower(concat(:searchstr,'%'))))", nativeQuery = true)
	Page<User> searchDeletedUser(String searchstr, Long brandId, Pageable pageable, String searchRole);

	/**
	 * Find User by User Login Id
	 * 
	 * @param userId
	 * @return
	 */
	User findOneByUserLogin_Id(Long userId);

	/**
	 * Find User by email
	 * 
	 * @param email
	 * @return
	 */
	User findOneByUserLogin_Email(String email);

	/**
	 * Find all users who has given role
	 * 
	 * @param roleDsmUser
	 * @return
	 */
	List<User> findAllByUserLogin_RoleAndIsActiveTrue(UserRole roleDsmUser);

	/**
	 * Find all DSM users who are not assigned to any location division 
	 * 
	 * @return
	 */
	@Query(value="SELECT u.* FROM user u "
			+ "INNER JOIN user_login ul ON ul.id = u.user_login_id "
			+ "WHERE ul.role = :role "
			+ "AND u.id NOT IN (SELECT user_id FROM location_division where user_id is not null)", nativeQuery=true)
	List<User> findAllUnassignedUsers(String role);
	
	List<User> findByThirdPartyId(Long id);
	
	User findByNameIgnoreCase(String userName);
	
	@Query(value="select user.name from user inner join user_login on " + 
			" user.user_login_id = user_login.id " + 
			" where role = \"ROLE_SM_USER\" and store_id =:storeId", nativeQuery=true)
	String findSmUserNameByStoreId(Long storeId);

	User findByStore_Id(Long storeId);
	
	@Query(value="select * from user inner join user_login on " + 
			" user.user_login_id = user_login.id " + 
			" where user_login.role = :role and user.third_party_id = :thirdPartyId", nativeQuery=true)
	User getUserByThirdPartyId(Long thirdPartyId ,String role);

	@Query(value="delete from oauth_refresh_token where token_id in (SELECT refresh_token FROM oauth_access_token " + 
			"where user_name =:user)", nativeQuery=true)
	void removeOauthRefreshToken(String user);

	@Query(value="delete oauth_access_token" + 
			"WHERE user_name =:user", nativeQuery=true)
	void setBlankOauthAccessToken(String user);
	

}
