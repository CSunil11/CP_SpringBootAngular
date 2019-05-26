package com.ackermans.criticalpath.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ackermans.criticalpath.entity.Brand;
import com.ackermans.criticalpath.entity.Store;

@Repository
public interface StoreRepository extends JpaRepository<Store,Long> {
	
	/**
	 * Get total Count Store by code 
	 * @param code
	 * @return
	 */
	int countByCodeIgnoreCase(String code);

	/**
	 * Get total Count Store by code but ignore id
	 * @param code
	 * @param Id
	 * @return
	 */
	@Query("SELECT COUNT(*) FROM Store WHERE code = :code AND id != :id")
	int countByCodeAndId(String code, Long id);

	/**
	 * Get all Active Store
	 * @return
	 */
	List<Store> findAllByIsActiveTrue();

	/**
	 * Get All Active Store With Fix Size Row
	 * @param pageable
	 * @return
	 */
	Page<Store> findAllByIsActiveTrue(Pageable pageable);
	
	/**
	 * Get All deleted Store With Fix Size Row
	 * @param pageable
	 * @return
	 */
	Page<Store> findAllByIsActiveFalseAndIsDeleteTrue(Pageable pageable);

	/**
	 * Get List By Searching Code
	 * @param code
	 * @return
	 */
	List<Store> findByNameIgnoreCaseStartingWith(String code);

	/**
	 * Get Store By Code
	 * @param code
	 * @return
	 */
	Store findByCode(String code);

	/**
	 * Get Store List By Country ID
	 * @param countryId
	 * @return
	 */
	List<Store> findByCountry_Id(Long countryId);

	/**
	 * Get Store List By Brand Id
	 * @param brandId
	 * @return
	 */
	List<Store> findByBrand_Id(Long brandId);
	
	/**
	 * Search active records whose name or code start with given name with fix row
	 * 
	 * @param searchStr
	 * @param pageable
	 * @return
	 */
	@Query("SELECT e FROM Store e WHERE (lower(e.name) like lower(concat(:searchStr,'%')) or lower(e.code) like lower(concat(:searchStr,'%'))) AND e.isActive = 1 ")
	public Page<Store> findBySearchActiveTrueStore(String searchStr, Pageable pageable);
	
	/**
	 * Search deleted records whose name or code start with given name with fix row
	 * 
	 * @param searchStr
	 * @param pageable
	 * @return
	 */
	@Query("SELECT e FROM Store e WHERE (lower(e.name) like lower(concat(:searchStr,'%')) or lower(e.code) like lower(concat(:searchStr,'%'))) AND e.isDelete = 1 ")
	public Page<Store> findBySearchActiveFalseStore(String searchStr, Pageable pageable);
	
	/**
	 * Search active records whose name or code start with given name with fix row
	 * 
	 * @param searchStr
	 * @param pageable
	 * @return
	 */
	@Query("SELECT e FROM Store e WHERE lower(e.name) like lower(concat(:searchStr,'%')) or lower(e.code) like lower(concat(:searchStr,'%'))")
	public Page<Store> findBySearchActiveStore(String searchStr, Pageable pageable);
	
	/**
	 * Search active stores of brand
	 * 
	 * @param brandId
	 * @return
	 */
	public List<Store> findByBrand_IdAndIsActiveTrue(Long brandId);
	
	/**
	 * Get active Store list By country iD
	 * @param countryId
	 * @return
	 */
	List<Store> findByCountry_IdAndIsActiveTrue(Long countryId);
	
	Store findByThirdPartyId(Long id);
	
	Store findByNameIgnoreCase(String name);
	
	Store findByNameIgnoreCaseAndIsActiveTrue(String name);

	List<Store> findByStockTakeCycle_Id(Long id);
	
	List<Store> findByLocationDivision_IdAndIsActiveTrueOrderByNameAsc(Long id);
	
	List<Store> findByRegionalManagers_IdAndIsActiveTrueOrderByNameAsc(Long id);


}
