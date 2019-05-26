package com.ackermans.criticalpath.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ackermans.criticalpath.entity.Brand;
import com.ackermans.criticalpath.entity.LocationDivision;
import com.ackermans.criticalpath.entity.Store;

@Repository
public interface LocationDivisionRepository extends JpaRepository<LocationDivision, Long> {

	/**
	 * Get count of records which name matches with given name 
	 * 
	 * @param name
	 * @return
	 */
	long countByNameIgnoreCase(String name);
	
	/**
	 * Get count of records which name matches with given name but ignore given id
	 * 
	 * @param name
	 * @param ignoreId
	 * @return
	 */
	@Query("SELECT COUNT(*) FROM LocationDivision WHERE UPPER(name) = UPPER(:name) AND id != :ignoreId")
	long countByNameAndIgnoreId(String name, Long ignoreId);

	/**
	 * Get all active and non-deleted location division
	 * @return
	 */
	List<LocationDivision> findAllByIsActiveTrueAndIsDeleteFalse();
	
	/**
	 * Get All Active Store With Fix Size Row
	 * @param pageable
	 * @return
	 */
	Page<LocationDivision> findAllByIsActiveTrueAndIsDeleteFalse(Pageable pageable);
	
	/**
	 * Get all delete location division
	 * @return
	 */
	List<LocationDivision> findAllByIsActiveFalseAndIsDeleteTrue();

	/**
	 * Update active flag to false and delete flag to true for given id 
	 * 
	 * @param locationDivisionId
	 */
	@Transactional
	@Modifying
	@Query("UPDATE LocationDivision SET isActive = false, isDelete = true WHERE id = :locationDivisionId")
	void delete(Long locationDivisionId);
	
	/**
	 * Update active flag to true and delete flag to false for given id
	 * 
	 * @param locationDivisionId
	 */
	@Transactional
	@Modifying
	@Query("UPDATE LocationDivision SET isActive = true, isDelete = false WHERE id = :locationDivisionId")
	void restore(Long locationDivisionId);

	/**
	 * Get Active locationDivision By province id
	 * 
	 * @param provinceId
	 * @return
	 */
	List<LocationDivision> findByProvinceIdAndIsActiveTrue(Long provinceId);
	
	LocationDivision findByThirdPartyId(Long id);
	
	LocationDivision findByNameIgnoreCase(String name);

	List<LocationDivision> findByDivisionalSalesManagers_Id(Long userId);
	
	/**
	 * Search active records whose name or code start with given name with fix row
	 * 
	 * @param searchStr
	 * @param pageable
	 * @return
	 */
	@Query("SELECT e FROM LocationDivision e WHERE (lower(e.name) like lower(concat(:searchStr,'%'))) AND e.isActive = 1 ")
	public Page<LocationDivision> findBySearchActiveTrueLocDiv(String searchStr, Pageable pageable);
	
	/**
	 * Search deleted records whose name or code start with given name with fix row
	 * 
	 * @param searchStr
	 * @param pageable
	 * @return
	 */
	@Query("SELECT e FROM LocationDivision e WHERE (lower(e.name) like lower(concat(:searchStr,'%'))) AND e.isDelete = 1 ")
	public Page<LocationDivision> findBySearchActiveFalseLocDiv(String searchStr, Pageable pageable);
	
	/**
	 * Get All deleted locDiv With Fix Size Row
	 * @param pageable
	 * @return
	 */
	Page<LocationDivision> findAllByIsActiveFalseAndIsDeleteTrue(Pageable pageable);

}
