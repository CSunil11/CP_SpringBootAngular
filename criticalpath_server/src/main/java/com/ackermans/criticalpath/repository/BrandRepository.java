package com.ackermans.criticalpath.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ackermans.criticalpath.entity.Brand;
import com.ackermans.criticalpath.entity.Province;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {

	/**
	 * Get number of records which has given name but ignore case
	 * 
	 * @param name
	 * @return
	 */
	long countByNameIgnoreCase(String name);
	
	/**
	 * Get number of records which has given name but ignore case 
	 * and id should not be equal to ignoreId
	 * 
	 * @param name
	 * @param ignoreId
	 * @return
	 */
	@Query("SELECT COUNT(*) FROM Brand WHERE name = :name AND id != :ignoreId")
	long countByNameAndIgnoreId(String name, Long ignoreId);
	
	/**
	 * Find by name but ignore case
	 * 
	 * @param name
	 * @return
	 */
	Brand findByNameIgnoreCase(String name);
	
	/**
	 * Find brand whose name starting with given name but ignore case
	 * 
	 * @param name
	 * @return
	 */
	Brand findByNameStartingWithIgnoreCase(String name);

	/**
	 * Find all brands which are active
	 * 
	 * @param pageRequest
	 * @return
	 */
	Page<Brand> findAllByIsActiveTrue(Pageable pageRequest);
	
	/**
	 * Find all brands which are delete
	 * 
	 * @param pageRequest
	 * @return
	 */
	Page<Brand> findAllByIsActiveFalseAndIsDeleteTrue(Pageable pageRequest);

	/**Find all brands which are active
	 * 
	 * @param by
	 * @return
	 */
	List<Brand> findAllByIsActiveTrue(Sort by);

	/**
	 * Search active records whose name start with given name
	 * 
	 * @param name
	 * @param dEFAULT_SORT
	 * @return
	 */
	List<Brand> findByNameStartingWithIgnoreCaseAndIsActiveTrue(String name, Sort sort);

	/**
	 * Search active records whose name start with given name with fix row
	 * 
	 * @param searchStr
	 * @param pageable
	 * @return
	 */
	@Query("SELECT e FROM Brand e WHERE lower(e.name) like lower(concat(:searchStr,'%'))")
	public Page<Brand> findBySearchActiveBrand(String searchStr, Pageable pageable);
	
	/**
	 * search active record whose name start with given name with fix row
	 * @param searchStr
	 * @param pageable
	 * @return
	 */
	@Query("SELECT e FROM Brand e WHERE  e.isActive = 1 AND lower(e.name) like lower(concat(:searchStr,'%'))")
	public Page<Brand> findBySearchActiveTrueBrand(String searchStr, Pageable pageable);
	
	/**
	 * search delete record whose name start with given name with fix row
	 * @param searchStr
	 * @param pageable
	 * @return
	 */
	@Query("SELECT e FROM Brand e WHERE  e.isDelete = 1 AND lower(e.name) like lower(concat(:searchStr,'%'))")
	public Page<Brand> findBySearchActiveFalseBrand(String searchStr, Pageable pageable);
	
	Brand findByThirdPartyId(Long id);
}
