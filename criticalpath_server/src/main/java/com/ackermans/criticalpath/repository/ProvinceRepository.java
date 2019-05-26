package com.ackermans.criticalpath.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ackermans.criticalpath.entity.LocationDivision;
import com.ackermans.criticalpath.entity.Province;

@Repository
public interface ProvinceRepository extends JpaRepository<Province,Long>{
	
	/**
	 * Get total Count Province by name 
	 * @param name
	 * @return
	 */
	int countByNameIgnoreCase(String name);

	/**
	 * Get total Count Province by name but ignore id
	 * @param name
	 * @param ignoreId
	 * @return
	 */
	@Query("SELECT COUNT(*) FROM Province WHERE name = :name AND id != :ignoreId")
	long countByNameAndIgnoreId(String name, Long ignoreId);

	/**
	 * Get List By Searching parameter
	 * @param string
	 * @return
	 */
	List<Province> findByNameIgnoreCaseStartingWith(String string);

	/**
	 * Get All Active Province List
	 * 
	 * @return
	 */
	List<Province> findAllByIsActiveTrue();

	/**
	 * Get All Delete Province List
	 * @return
	 */
	List<Province> findAllByIsDeleteTrue();
	
	/**
	 * Get All Active Province List By Fix size Row 
	 * 
	 * @param pageable
	 * @return
	 */
	Page<Province> findAllByIsActiveTrue(Pageable pageable);

	/**
	 * Get Active Province List By Searching Value
	 * 
	 * @param searchStr
	 * @return
	 */
	List<Province> findByNameIgnoreCaseStartingWithAndIsActiveTrue(String searchStr);
	
	/**
	 * Get Active Province By Name
	 * 
	 * @param provinceName
	 * @return
	 */
	Province findByNameIgnoreCase(String provinceName);

	/**
	 * Get Active Province By country id
	 * 
	 * @param countryId
	 * @return
	 */
	List<Province> findByCountryIdAndIsActiveTrue(Long countryId);
	
	Province findByThirdPartyId(Long id);

	Page<Province> findAllByIsActiveFalseAndIsDeleteTrue(Pageable pageable);
	
	/**
	 * Search active records whose name or code start with given name with fix row
	 * 
	 * @param searchStr
	 * @param pageable
	 * @return
	 */
	@Query("SELECT e FROM Province e WHERE (lower(e.name) like lower(concat(:searchStr,'%'))) AND e.isActive = 1 ")
	public Page<Province> findBySearchActiveTrueProvince(String searchStr, Pageable pageable);
	
	/**
	 * Search deleted records whose name or code start with given name with fix row
	 * 
	 * @param searchStr
	 * @param pageable
	 * @return
	 */
	@Query("SELECT e FROM Province e WHERE (lower(e.name) like lower(concat(:searchStr,'%'))) AND e.isDelete = 1 ")
	public Page<Province> findBySearchActiveFalseProvince(String searchStr, Pageable pageable);

	
}
