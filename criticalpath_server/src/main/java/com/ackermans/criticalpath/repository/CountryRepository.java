package com.ackermans.criticalpath.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ackermans.criticalpath.entity.Country;
import com.ackermans.criticalpath.entity.Province;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {

	/**
	 * Get All Active Country
	 * @return
	 */
	List<Country> findAllByIsActiveTrue();

	/**
	 * Get All delete Country
	 * @return
	 */
	List<Country> findAllByIsActiveFalseAndIsDeleteTrue();
	
	/**
	 * Get List By Searching parameter
	 * @param string
	 * @return
	 */
	List<Country> findByNameIgnoreCaseStartingWith(String search);

	/**
	 * Get Active Country List By Searching Value
	 * 
	 * @param searchStr
	 * @return
	 */
	List<Country> findByNameIgnoreCaseStartingWithAndIsActiveTrue(String searchStr);

	/**
	 * Get Count of Country by Code and Name
	 * @param code
	 * @param name
	 * @return
	 */
	int countByCodeIgnoreCaseOrNameIgnoreCase(String code, String name);

	/**
	 * Get Count of Country by Code, Name and  ignore id
	 * @param code
	 * @param name
	 * @param id
	 * @return
	 */
	@Query("SELECT COUNT(*) FROM Country WHERE (code = :code OR name = :name) AND id != :id")
	int countByCodeIgnoreCaseAndNameIgnoreCaseAndId(String code, String name, Long id);
	
	Country findByCodeIgnoreCase(String code);

	Page<Country> findAllByIsActiveFalseAndIsDeleteTrue(Pageable pageable);

	Page<Country> findAllByIsActiveTrue(Pageable pageable);

	/**
	 * Search active records whose name or code start with given name with fix row
	 * 
	 * @param searchStr
	 * @param pageable
	 * @return
	 */
	@Query("SELECT e FROM Country e WHERE (lower(e.name) like lower(concat(:searchStr,'%'))) AND e.isActive = 1 ")
	public Page<Country> findBySearchActiveTrueCountry(String searchStr, Pageable pageable);
	
	/**
	 * Search deleted records whose name or code start with given name with fix row
	 * 
	 * @param searchStr
	 * @param pageable
	 * @return
	 */
	@Query("SELECT e FROM Country e WHERE (lower(e.name) like lower(concat(:searchStr,'%'))) AND e.isDelete = 1 ")
	public Page<Country> findBySearchActiveFalseCountry(String searchStr, Pageable pageable);

}
