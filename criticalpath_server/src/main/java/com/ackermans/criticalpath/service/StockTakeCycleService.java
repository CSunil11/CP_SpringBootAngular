package com.ackermans.criticalpath.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ackermans.criticalpath.admin.dto.StoreInterface;
import com.ackermans.criticalpath.common.dto.CPException;
import com.ackermans.criticalpath.common.service.ResponseGenerator;
import com.ackermans.criticalpath.dto.StockAckerResponse;
import com.ackermans.criticalpath.dto.StockTakeCycleResponse;
import com.ackermans.criticalpath.entity.AckStockTakeCycle;
import com.ackermans.criticalpath.entity.StockTakeCycle;
import com.ackermans.criticalpath.entity.Store;
import com.ackermans.criticalpath.entity.User;
import com.ackermans.criticalpath.enums.ErrorStatus;
import com.ackermans.criticalpath.enums.UserRole;
import com.ackermans.criticalpath.repository.AckStockTakeCycleRepository;
import com.ackermans.criticalpath.repository.StockTakeCycleRepository;
import com.ackermans.criticalpath.repository.StoreRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@Transactional
public class StockTakeCycleService {

	private final Logger logger = LogManager.getFormatterLogger();

	@Autowired
	private StockTakeCycleRepository stockTakeCycleRepository;

	@Autowired
	private AckStockTakeCycleRepository ackStockTakeCycleRepository;

	@Autowired
	private StoreRepository storeRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private StoreService storeService;

	/**
	 * Verify and Save Stock Take Cycle
	 * 
	 * @param stockTakeCycle
	 * @return
	 * @throws CPException
	 */
	public StockTakeCycle verifyAndSave(JsonNode stockTakeCycleReq, OAuth2Authentication oAuth2Authentication)
			throws CPException {

		boolean isExist = false;

		Long cycleId = stockTakeCycleReq.get("id") != null ? stockTakeCycleReq.get("id").asLong() : 0L;
		String name = stockTakeCycleReq.get("name").asText();
		String length = stockTakeCycleReq.get("length").asText();
		String donotcount = stockTakeCycleReq.get("donotcount") != null ? stockTakeCycleReq.get("donotcount").asText()
				: null;
		Boolean status = stockTakeCycleReq.get("status").asBoolean();
		String store = stockTakeCycleReq.get("store").asText();
		List<String> storetemp = Arrays.asList(store.split("/"));
		System.out.println(store);

		StockTakeCycle stockTakeCycle = new StockTakeCycle();
		User user = userService.getUserByEmail(oAuth2Authentication.getName());
		stockTakeCycle.setCreatedBy(user);
		stockTakeCycle.setName(name);
		stockTakeCycle.setDoNotCount(donotcount);
		stockTakeCycle.setLength(length);
		stockTakeCycle.setIsActive(status);
		// If there is id that means record needs to be updated
		// hence we should ignore that record while checking for existence
//		if (stockTakeCycle.getId() != null && stockTakeCycle.getId() > 0) {
//			isExist = this.isExist(stockTakeCycle.getName(), stockTakeCycle.getId());
//		} else {
//			isExist = this.isExist(stockTakeCycle.getName());
//		}

		if (cycleId != null && cycleId > 0) {
			stockTakeCycle.setId(cycleId);
			isExist = this.isExist(name, cycleId);
		} else {
			isExist = this.isExist(name);
		}

		// If record is unique then save it else throw exception

		if (!isExist) {

			stockTakeCycle = stockTakeCycleRepository.save(stockTakeCycle);

			List<Long> storeReqIdList = new ArrayList<>();
			for (String storeReqList : storetemp) {
				Store storeObj = storeRepository.getOne(Long.parseLong(storeReqList));
				storeObj.setStockTakeCycle(stockTakeCycle);
				storeReqIdList.add(Long.parseLong(storeReqList));
				System.out.println("fffff");
				storeRepository.save(storeObj);
			}
			System.out.println("sss");
			List<Store> dbStore = storeRepository.findByStockTakeCycle_Id(stockTakeCycle.getId());
			List<Long> dbStoreIdList = new ArrayList<>();
			for (Store dbStoreList : dbStore) {
				dbStoreIdList.add(dbStoreList.getId());
			}

			for (Long dbStoreId : dbStoreIdList) {
				if (!storeReqIdList.contains(dbStoreId)) {
					Store stores = storeRepository.getOne(dbStoreId);
					stores.setStockTakeCycle(null);
				}
			}
		} else
			throw new CPException(ErrorStatus.DUPLICATE_RECORD);

		return stockTakeCycle;
	}

	/**
	 * Check if record already exists for given stock take cycle name
	 * 
	 * @param name
	 * @return
	 */
	public boolean isExist(String name) {
		return stockTakeCycleRepository.countByNameIgnoreCase(name) > 0;
	}

	/**
	 * Check if record already exists for given stock take cycle name
	 * 
	 * @param name
	 * @return
	 */
	public boolean isExist(String name, Long ignoreId) {
		return stockTakeCycleRepository.countByNameAndIgnoreId(name, ignoreId) > 0;
	}

	/**
	 * Get all Stock Take Cycle
	 * 
	 * @return
	 */
	public List<StockTakeCycle> getAll() {
		return stockTakeCycleRepository.findAll();
	}

	/**
	 * Get all active Stock Take Cycle List
	 * 
	 * @return
	 */
//	public List<StockTakeCycle> getAllActive() {
//		List<StockTakeCycle> stockCycles = stockTakeCycleRepository.findAllByIsActiveTrue();
//		
//		for (StockTakeCycle stockTakeCycle : stockCycles) {
//			for (Store store : stockTakeCycle.getStores()) {
//				System.out.println(store);
//			}
//		}
//		
//		if(stockCycles.size()>0)
//			return stockCycles;
//		else
//			return null;
//	}

	public Map getAllActive() {
		List<StockTakeCycle> stockCycles = stockTakeCycleRepository.findAllByIsActiveTrue();
		Map<String, Object> cycleData = new HashMap();
		List cycleFinal = new ArrayList<StockTakeCycle>();
		List storeFinal = new ArrayList<Store>();

		for (StockTakeCycle stockTakeCycle : stockCycles) {
			List<Store> store = storeRepository.findByStockTakeCycle_Id(stockTakeCycle.getId());

			cycleFinal.add(stockTakeCycle);
			storeFinal.add(store);

		}
		cycleData.put("cycle", cycleFinal);
		cycleData.put("store", storeFinal);

		if (cycleFinal.size() > 0)
			return cycleData;
		else
			return null;
	}

	/**
	 * Get all active Stock Take Cycle List along with store name
	 * 
	 * @return
	 */
	public List<Object> getAllActiveStockTakeCyclePerStore() {
//		return stockTakeCycleRepository.getAllActiveStockTakeCyclePerStore();
		return null;
	}

	/**
	 * Get active Stock Take Cycle List for the given store
	 * 
	 * @return
	 */
	public List<StockAckerResponse> getActiveStockTakeCyclePerStore(Long storeId) {
//		return stockTakeCycleRepository.getActiveStockTakeCyclePerStore(storeId);
		return null;
	}

	/**
	 * Get active Stock Take Cycle Status List for the given store
	 * 
	 * @return
	 */
	public List getStockTakeCycleStatusPerStore(String storeId, List list) {

//		List<StockAckerResponse> lsForTot = new ArrayList<StockAckerResponse>();
//		List<StockAckerResponse> lsForComp = new ArrayList<StockAckerResponse>();
//		
//		List<String> lsForStatus = new ArrayList<String>();
//		Map hmForTot = new HashMap();
//		Map hmForComp = new HashMap();
//		
//		lsForTot = stockTakeCycleRepository.getStockTakeCycleStatusPerStoreForTot(storeId); //This will work for all scenarios
//		lsForComp = stockTakeCycleRepository.getStockTakeCycleStatusPerStoreForComp(storeId); //This will work for all scenarios
//		
//		System.out.println("lsForTot: "+lsForTot);
//		System.out.println("lsForComp: "+lsForComp);
//		
//		for(int i=0;i<lsForTot.size();i++)
//		{
//			if(lsForTot.get(i) != null)
//			{
//				hmForTot.put(lsForTot.get(i).getStoreId()+"-"+lsForTot.get(i).getCycleId(), lsForTot.get(i).getTotCp());
//				System.out.println(lsForTot.get(i).getStoreId()+"-"+lsForTot.get(i).getCycleId()+"_____"+lsForTot.get(i).getTotCp());
//			}
//		}
//		
//		for(int i=0;i<lsForComp.size();i++)
//		{
//			if(lsForComp.get(i) != null)
//			{
//				hmForComp.put(lsForComp.get(i).getStoreId()+"-"+lsForComp.get(i).getCycleId(), lsForComp.get(i).getTotCompCp());
//				System.out.println(lsForComp.get(i).getStoreId()+"-"+lsForComp.get(i).getCycleId()+"___"+lsForComp.get(i).getTotCompCp());
//			}
//		}
//		
////		System.out.println("output: "+hmForTot.size());
////		System.out.println("output: "+hmForTot.containsKey("2-1"));
////		System.out.println("output: "+hmForTot.get("2-1"));
////		System.out.println("output: "+hmForTot.get("2-4"));
////		
////		System.out.println("output: "+hmForComp.size());
////		System.out.println("output: "+hmForComp.containsKey("2-1"));
////		System.out.println("output: "+hmForComp.get("2-1"));
////		System.out.println("output: "+hmForComp.get("2-4"));
//		
//		for(int i=0;i<list.size();i++)
//		{
//			String strTemp = "";
//			if(hmForComp.containsKey(list.get(i)))
//			{
//				strTemp = "" + hmForComp.get(list.get(i)) + " of ";
//			}
//			else
//			{
//				strTemp = "0 of ";
//			}
//			
//			if(hmForTot.containsKey(list.get(i)))
//			{
//				strTemp = strTemp + hmForTot.get(list.get(i)) + "";
//			}
//			else
//			{
//				strTemp = strTemp + "0";
//			}
//			lsForStatus.add(strTemp);
//		}
//		System.out.println("lsForStatus: "+lsForStatus);
//		return lsForStatus;
		return null;

//		return stockTakeCycleRepository.getStockTakeCycleStatusPerStore(storeId);
	}

	/**
	 * Get active Stock Take Cycle List for the given store
	 * 
	 * @return
	 */
	public List<Object> getAllActiveStoresPerDsmUser(Long id) {
		return stockTakeCycleRepository.getAllActiveStoresPerDsmUser(id);
	}

	public List<StoreInterface> getAllActiveStoresPerDsmUserInterface(Long id, String role) {

		List<StoreInterface> st = new ArrayList<StoreInterface>();
		List<StoreInterface> stFinal = new ArrayList<StoreInterface>();
		List<Integer> listOfIndexToRemove = new ArrayList<Integer>();

		if (role.trim().equalsIgnoreCase(UserRole.ROLE_DSM_USER.toString())) {
//			return stockTakeCycleRepository.getAllActiveStoresPerDsmUserInterface(id);
			st = stockTakeCycleRepository.getAllActiveStoresPerDsmUserInterface(id);
			List<AckStockTakeCycle> ackCycle = ackStockTakeCycleRepository.findAllByIsActiveTrue();
			

			if (ackCycle.size() > 0) {
				for (int i = 0; i < st.size(); i++) {
					int count = 0;
					for (AckStockTakeCycle ackStockTakeCycleOb : ackCycle) {
						if (st.get(i).getId() == ackStockTakeCycleOb.getStore().getId()) {
							count++;
						}
					}
					if (count == 0) {
//						st.remove();
						listOfIndexToRemove.add(i);
					}
				}
				
				for(int j=0;j<st.size();j++)
				{
					if(!listOfIndexToRemove.contains(j))
						stFinal.add(st.get(j));
				}
				
			}
			return stFinal;
		} else {
			return stockTakeCycleRepository.getAllActiveStoresPerRamUserInterface(id);
			
			
//			st = stockTakeCycleRepository.getAllActiveStoresPerRamUserInterface(id);
//			List<AckStockTakeCycle> ackCycle = ackStockTakeCycleRepository.findAllByIsActiveTrue();
//
//			if (ackCycle.size() > 0) {
//				for (int i = 0; i < st.size(); i++) {
//					int count = 0;
//					for (AckStockTakeCycle ackStockTakeCycleOb : ackCycle) {
//						if (st.get(i).getId() == ackStockTakeCycleOb.getStore().getId()) {
//							count++;
//						}
//					}
//					if (count == 0) {
//						listOfIndexToRemove.add(i);
//					}
//				}
//				
//				for(int j=0;j<st.size();j++)
//				{
//					if(!listOfIndexToRemove.contains(j))
//						stFinal.add(st.get(j));
//				}
//			}
		}
//		return stFinal;

	}

	/**
	 * Get All Active Stock Take Cycle for Selected DSM User
	 * 
	 * @return
	 */
	public List<StockAckerResponse> getAllActiveStockTakeCycleDsmUser(Long userId, String role, int sortBy) {

//		List<StockAckerResponse> ls = new ArrayList<StockAckerResponse>();
//		List<String> lsStoreData = new ArrayList<String>();
//		
//		if(role.trim().equalsIgnoreCase(UserRole.ROLE_DSM_USER.toString())) {
//			if(sortBy == 1)
//				ls = stockTakeCycleRepository.getAllActiveStockTakeCycleDsmUserSortedByName(userId);
//			else if(sortBy == 3)
//				ls = stockTakeCycleRepository.getAllActiveStockTakeCycleDsmUserSortedByTakeDate(userId);
//		}
//		else {
//			if(sortBy == 1)
//				ls = stockTakeCycleRepository.getAllActiveStockTakeCycleRamUserByName(userId);
//			else if(sortBy == 3)
//				ls = stockTakeCycleRepository.getAllActiveStockTakeCycleRamUserByStockDate(userId);
//		}
//		
//		System.out.println("Service_ls: "+ls);
//		for(int i=0;i<ls.size();i++)
//		{
//			if(ls.get(i) != null)
//			{
//				lsStoreData.add(ls.get(i).getStoreId()+"-"+ls.get(i).getCycleId());
//			}
//		}
//		
//		System.out.println("size: "+lsStoreData.size());
//		System.out.println("size: "+lsStoreData);
//		
////		ObjectMapper om = new ObjectMapper();
////		try {
////			System.out.println("size1: "+om.writeValueAsString(ls.get(0).getStoreId()+"-"+ls.get(0).getCycleId()));
////		} catch (JsonProcessingException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
//		
//		return ls;
		return null;
	}

	/**
	 * Get All Active Stock Take Cycle Status for Selected DSM User
	 * 
	 * @return
	 */
	public List getAllActiveStockTakeCycleStatusDsmUser(String userId, String role, int sortBy, List list) {

//		List<StockAckerResponse> lsForTot = new ArrayList<StockAckerResponse>();
//		List<StockAckerResponse> lsForComp = new ArrayList<StockAckerResponse>();
//		
//		List<String> lsForStatus = new ArrayList<String>();
//		Map hmForTot = new HashMap();
//		Map hmForComp = new HashMap();
//		
//		if(role.trim().equalsIgnoreCase(UserRole.ROLE_DSM_USER.toString())) {
//			lsForTot = stockTakeCycleRepository.getAllActiveStockTakeCycleStatusDsmUserByNameForTot(userId); //This will work for all scenarios
//			lsForComp = stockTakeCycleRepository.getAllActiveStockTakeCycleStatusDsmUserByNameForComp(userId); //This will work for all scenarios
//		}
//		else
//		{
//			lsForTot = stockTakeCycleRepository.getAllActiveStockTakeCycleStatusRamUserByNameForTot(userId); //This will work for all scenarios
//			lsForComp = stockTakeCycleRepository.getAllActiveStockTakeCycleStatusRamUserByNameForComp(userId); //This will work for all scenarios
//		}
//		System.out.println("lsForTot: "+lsForTot);
//		System.out.println("lsForComp: "+lsForComp);
//		
//		for(int i=0;i<lsForTot.size();i++)
//		{
//			if(lsForTot.get(i) != null)
//			{
//				hmForTot.put(lsForTot.get(i).getStoreId()+"-"+lsForTot.get(i).getCycleId(), lsForTot.get(i).getTotCp());
//				System.out.println(lsForTot.get(i).getStoreId()+"-"+lsForTot.get(i).getCycleId()+"_____"+lsForTot.get(i).getTotCp());
//			}
//		}
//		
//		for(int i=0;i<lsForComp.size();i++)
//		{
//			if(lsForComp.get(i) != null)
//			{
//				hmForComp.put(lsForComp.get(i).getStoreId()+"-"+lsForComp.get(i).getCycleId(), lsForComp.get(i).getTotCompCp());
//				System.out.println(lsForComp.get(i).getStoreId()+"-"+lsForComp.get(i).getCycleId()+"___"+lsForComp.get(i).getTotCompCp());
//			}
//		}
//		
////		System.out.println("output: "+hmForTot.size());
////		System.out.println("output: "+hmForTot.containsKey("2-1"));
////		System.out.println("output: "+hmForTot.get("2-1"));
////		System.out.println("output: "+hmForTot.get("2-4"));
////		
////		System.out.println("output: "+hmForComp.size());
////		System.out.println("output: "+hmForComp.containsKey("2-1"));
////		System.out.println("output: "+hmForComp.get("2-1"));
////		System.out.println("output: "+hmForComp.get("2-4"));
//		
//		for(int i=0;i<list.size();i++)
//		{
//			String strTemp = "";
//			if(hmForComp.containsKey(list.get(i)))
//			{
//				strTemp = "" + hmForComp.get(list.get(i)) + " of ";
//			}
//			else
//			{
//				strTemp = "0 of ";
//			}
//			
//			if(hmForTot.containsKey(list.get(i)))
//			{
//				strTemp = strTemp + hmForTot.get(list.get(i)) + "";
//			}
//			else
//			{
//				strTemp = strTemp + "0";
//			}
//			lsForStatus.add(strTemp);
//		}
//		
//		System.out.println("lsForStatus: "+lsForStatus);
//		
//		return lsForStatus;
		return null;
	}

	/**
	 * Get all Deleted Stock Take Cycle List
	 * 
	 * @return
	 */
	public Map getAllDeletedStockTakeCycle() {
//		return stockTakeCycleRepository.findAllByIsActiveFalseAndIsDeleteTrue();
		List<StockTakeCycle> stockCycles = stockTakeCycleRepository.findAllByIsActiveFalseAndIsDeleteTrue();
		Map<String, Object> cycleData = new HashMap();
		List cycleFinal = new ArrayList<StockTakeCycle>();
		List storeFinal = new ArrayList<Store>();

		for (StockTakeCycle stockTakeCycle : stockCycles) {
			List<Store> store = storeRepository.findByStockTakeCycle_Id(stockTakeCycle.getId());

			cycleFinal.add(stockTakeCycle);
			storeFinal.add(store);

		}
		cycleData.put("cycle", cycleFinal);
		cycleData.put("store", storeFinal);

		if (cycleFinal.size() > 0)
			return cycleData;
		else
			return null;
	}

	/**
	 * Get Stock Take Cycle By stockTakeCycleId
	 * 
	 * @param stockTakeCycleId
	 * @return
	 */
	public Object getStockTakeCycleData(Long stockTakeCycleId) {

		Optional<StockTakeCycle> stockCycle = stockTakeCycleRepository.findById(stockTakeCycleId);

		List<Store> store = storeRepository.findByStockTakeCycle_Id(stockTakeCycleId);

		Map cycleData = new HashMap();
		cycleData.put("cycle", stockCycle);
		cycleData.put("stores", store);

		return cycleData;
	}

	/**
	 * Deletes the record
	 * 
	 * @param stockTakeCycleId
	 * @return
	 * @throws CPException
	 */
	public void deleteStockTakeCycle(Long stockTakeCycleId) throws CPException {

		StockTakeCycle stockTakeCycle = stockTakeCycleRepository.getOne(stockTakeCycleId);
		stockTakeCycle.setIsActive(false);
		stockTakeCycle.setIsDelete(true);
		stockTakeCycleRepository.save(stockTakeCycle);
	}

	/**
	 * Hides the record
	 * 
	 * @param stockTakeCycleId
	 * @throws CPException
	 */
	public void hideStockTakeCycle(Long stockTakeCycleId) throws CPException {

		StockTakeCycle stockTakeCycle = stockTakeCycleRepository.getOne(stockTakeCycleId);

		System.out.println("stockTakeCycle.isDisplay(): " + stockTakeCycle.getIsDisplay());

		stockTakeCycle.setIsDisplay(false);
		stockTakeCycleRepository.save(stockTakeCycle);
	}

	/**
	 * Restores the record
	 * 
	 * @param stockTakeCycleId
	 */
	public void restoreStockTakeCycle(Long stockTakeCycleId) {
		StockTakeCycle stockTakeCycle = stockTakeCycleRepository.getOne(stockTakeCycleId);
		stockTakeCycle.setIsActive(true);
		stockTakeCycle.setIsDelete(false);
		stockTakeCycleRepository.save(stockTakeCycle);
		return;
	}

	/**
	 * Get stock take cycle for given email
	 * 
	 * @param
	 * @return
	 */
	public List<StockTakeCycleResponse> getByUserEmail(String email) {

//		User user = userService.getUserByEmail(email);
//		List<StockTakeCycle> cycles = null;
//		
//		// check if user role is ROLE_SM_USER  else if ROLE_RAM_DSM_USER
//		if(user.getUserLogin().getRole().equals(UserRole.ROLE_SM_USER)) {
//			List<Store> stores = new ArrayList<>();
//			stores.add(user.getStore());
//			cycles = stockTakeCycleRepository.findByIsActiveTrueAndIsDeleteFalseAndStoresIn(stores);
//			
//		} 
//		else if (user.getUserLogin().getRole().equals(UserRole.ROLE_RAM_USER)) {
//			List<Store> stores = storeService.getByBrand(user.getBrand().getId());
//			cycles = stockTakeCycleRepository.findByIsActiveTrueAndIsDeleteFalseAndStoresIn(stores);
//		}
//		
//		List<StockTakeCycleResponse> stoclTakecycleResponse = new ArrayList<>();
//		for(StockTakeCycle cycle : cycles) {
//			stoclTakecycleResponse.add(StockTakeCycleResponse.fromEntity(cycle));
//		}
//		
//		return stoclTakecycleResponse;

		return null;
	}

	public StockTakeCycle saveStockTakeDate(Long stockTakeCycleId, String stockTakeDate) throws ParseException {

		StockTakeCycle stockTakeCycle = stockTakeCycleRepository.getOne(stockTakeCycleId);

		Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(stockTakeDate);

		stockTakeCycle.setStokeTakeDate(date1);
		return stockTakeCycleRepository.save(stockTakeCycle);
	}

	public List<Object> getStockTakeCycleByUser(Long userId) {

//		return stockTakeCycleRepository.getStockTakeCycleByUser(userId);
		return null;
	}
}
