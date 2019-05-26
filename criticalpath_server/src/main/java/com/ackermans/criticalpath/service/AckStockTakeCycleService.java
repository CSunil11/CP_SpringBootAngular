package com.ackermans.criticalpath.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ackermans.criticalpath.admin.dto.StoreInterface;
import com.ackermans.criticalpath.aop.service.AuditService;
import com.ackermans.criticalpath.common.dto.CPException;
import com.ackermans.criticalpath.dto.StockTakeCycleResponse;
import com.ackermans.criticalpath.entity.AckCriticalPath;
import com.ackermans.criticalpath.entity.AckStockTakeCycle;
import com.ackermans.criticalpath.entity.CloseDate;
import com.ackermans.criticalpath.entity.CriticalPath;
import com.ackermans.criticalpath.entity.LocationDivision;
import com.ackermans.criticalpath.entity.StockTakeCycle;
import com.ackermans.criticalpath.entity.Store;
import com.ackermans.criticalpath.entity.User;
import com.ackermans.criticalpath.enums.UserRole;
import com.ackermans.criticalpath.repository.AckCriticalPathRepository;
import com.ackermans.criticalpath.repository.AckStockTakeCycleRepository;
import com.ackermans.criticalpath.repository.CriticalPathRepository;
import com.ackermans.criticalpath.repository.LocationDivisionRepository;
import com.ackermans.criticalpath.repository.StockTakeCycleRepository;
import com.ackermans.criticalpath.repository.StoreCloseDateRepository;
import com.ackermans.criticalpath.repository.StoreRepository;
import com.ackermans.criticalpath.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.ackermans.criticalpath.utils.Constants;

@Service
@Transactional
public class AckStockTakeCycleService {

	private final Logger logger = LogManager.getFormatterLogger();

	@Autowired
	private AckStockTakeCycleRepository ackStockTakeCycleRepository;

	@Autowired
	private AckCriticalPathRepository ackCriticalPathRepository;

	@Autowired
	private UserService userService;

	//Used to resolve circular reference issue
	@Lazy
	@Autowired
	private StockTakeResultService stockTakeResultService;

	@Autowired
	private StoreService storeService;

	@Autowired
	private StoreRepository storeRepository;

	@Autowired
	private CriticalPathRepository criticalPathRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private LocationDivisionRepository locationDivisionRepository;

	@Autowired
	private StockTakeCycleRepository stockTakeCycleRepository;

	@Autowired
	private StoreCloseDateRepository storeCloseDateRepository;

	@Autowired
	private PepstoresAPIClient pepstoresAPIClient;
	
	@Autowired
	private AuditService auditService;
	
	private static final String ERROR_ADD_CYCLE = "Your scheduled stock count dates could not be processed. Please try again later.";
	
	private static final String ERROR_DELETE_CYCLE = "Your cycle could not be removed. Please try again later.";
	
	private static final String ERROR_UPDATE_CYCLE = "Your cycle could not be update for the given time. Please try again later.";

	/**
	 * Check if record already exists for given stock take cycle name
	 * 
	 * @param name
	 * @return
	 */
	public boolean isExist(String name) {
		return ackStockTakeCycleRepository.countByNameIgnoreCase(name) > 0;
	}

	/**
	 * Get all Stock Take Cycle
	 * 
	 * @return
	 */
	public List<AckStockTakeCycle> getAll() {
		return ackStockTakeCycleRepository.findAll();
	}

	/**
	 * Get all active Stock Take Cycle List
	 * 
	 * @return
	 */
	public List<AckStockTakeCycle> getAllActive() {
		List<AckStockTakeCycle> ackStockCycles = ackStockTakeCycleRepository.findAllByIsActiveTrue();

		if (ackStockCycles.size() > 0)
			return ackStockCycles;
		else
			return null;
	}

	/**
	 * Get active Stock Take Cycle List for the given store
	 * 
	 * @return
	 */
	public List<Object> getAllActiveStoresPerDsmUser(Long id) {
		return ackStockTakeCycleRepository.getAllActiveStoresPerDsmUser(id);
	}

	public List<StoreInterface> getAllActiveStoresPerDsmUserInterface(Long id, String role) {

		if (role.trim().equalsIgnoreCase(UserRole.ROLE_DSM_USER.toString())) {
			return ackStockTakeCycleRepository.getAllActiveStoresPerDsmUserInterface(id);
		} else {
			return ackStockTakeCycleRepository.getAllActiveStoresPerRamUserInterface(id);
		}
	}

	/**
	 * Get All Active Stock Take Cycle for Selected DSM User
	 * 
	 * @return
	 */
	public ArrayList<ArrayList<Object>> getAllActiveStockTakeCycleDsmUser(Long userId, String role, int sortBy,
			Long storeId) {

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
		ArrayList<ArrayList<Object>> listOLists = new ArrayList<ArrayList<Object>>();
		if (role.trim().equalsIgnoreCase(UserRole.ROLE_DSM_USER.toString())) {
			if (sortBy == 1) {
				User user = userRepository.findOneByUserLogin_Id(userId);
				List<LocationDivision> listOfld = new ArrayList<LocationDivision>();
				if (user != null) {
					if (role.trim().equalsIgnoreCase(UserRole.ROLE_DSM_USER.toString()))
						listOfld = locationDivisionRepository.findByDivisionalSalesManagers_Id(user.getId());
					

					List<Store> storeListAll = new ArrayList<Store>();

					if (listOfld.size() > 0) {
						for (LocationDivision ldTemp : listOfld) {
							List<Store> storeList = storeRepository
									.findByLocationDivision_IdAndIsActiveTrueOrderByNameAsc(ldTemp.getId());

							storeListAll.addAll(storeList);
						}

						for (int i = 0; i < storeListAll.size(); i++) {
							if (storeListAll.get(i).getId() == storeId) {
								Store st = storeListAll.get(i);
								if (st != null) {
									storeListAll.clear();
									storeListAll.add(st);
								}
							}
						}

						if (storeListAll.size() > 0) {
							List<AckStockTakeCycle> ackStockTakeCycleList = new ArrayList<AckStockTakeCycle>();
							for (Store store : storeListAll) {
								ackStockTakeCycleList = ackStockTakeCycleRepository.findByStore_IdAndIsActiveTrue(store.getId());
								for (AckStockTakeCycle ackStockTakeCycle : ackStockTakeCycleList) {
									ArrayList<Object> singleList = new ArrayList<Object>();
									singleList.add(ackStockTakeCycle.getId().toString());
									singleList.add(store.getId().toString());

									singleList.add(store.getName().toString());
									String stockDt = ackStockTakeCycleRepository
											.findByManualId(ackStockTakeCycle.getId());
									
									String[] stockArray = stockDt.split(",");

									String pattern = "yyyy-MM-dd";
									SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

									String date = simpleDateFormat.format(new Date());
									System.out.println(date);

									String stDate = simpleDateFormat.format(new Date());
									System.out.println(date);

									SimpleDateFormat formatter11 = new SimpleDateFormat("yyyy-MM-dd");
									String date11 = simpleDateFormat.format(new Date());
									Date std = null;
									Date d = null;
									try {

										std = formatter11.parse(stockDt);
										d = formatter11.parse(date11);
										System.out.println("-d-" + d);

									} catch (ParseException e) {
										throw new RuntimeException(e);
									}

									long duration = std.getTime() - d.getTime();

									long diffInDays = TimeUnit.MILLISECONDS.toDays(duration);
									System.out.println("date: " + diffInDays);// date: Thu Mar 14 05:23:06 UTC
																				// 2019__2019-03-30 18:30:00.0___16

									singleList.add(diffInDays + "");
									
									try {
										String datePatternAsDmy = "d MMM yyyy";
										SimpleDateFormat dateFormatAsDmy = new SimpleDateFormat(datePatternAsDmy);
										String formattedDateAsDmy = dateFormatAsDmy.format(ackStockTakeCycle.getStokeTakeDate());
										singleList.add(formattedDateAsDmy);
									} catch (Exception e) {
										singleList.add("");
										logger.debug("Error happening while date format setting: "+e.getMessage());
									}
									
//									singleList.add(ackStockTakeCycle.getStokeTakeDate()); // [1, Sone, 2019-03-30
																							// 18:30:00.0]

									String strTemp = "";
									strTemp = ""
											+ ackCriticalPathRepository
													.countCompAckCpWithStatusisNotDisable(ackStockTakeCycle.getId())
											+ " of " + ackCriticalPathRepository
													.countTotAckCpWithStatusisNotDisable(ackStockTakeCycle.getId());
									singleList.add(strTemp); // Critical path list
									
									if(stockArray.length>1 && stockArray[1] != null)
										singleList.add(""+stockArray[1]); // AM or PM
									else
										singleList.add(""); // AM or PM
									
									Map<String, Object> cycleData = stockTakeResultService.getStockTakeResult(store.getId());
									if(cycleData.size() > 0 && cycleData.get("id") != null) {
										System.out.println(cycleData);
										Object ramStatus = cycleData.get("status_ram");
										if(ramStatus != null) {
											singleList.add(ramStatus+ " by RAM");
										}
										else
										{
											Object dsmStatus = cycleData.get("status_dsm");
											if(dsmStatus != null ) {
												singleList.add(dsmStatus+ " by DSM");
											}else {
												Object smStatus = cycleData.get("status_sm");
												if(smStatus != null) {
													singleList.add(smStatus+ " by SM");
												}else {
													singleList.add("Waiting for SM");
												}
											}
										}
									}
									else {
										singleList.add("");
									}
									singleList.add(store.getCode());
									listOLists.add(singleList);
									System.out.println(listOLists);
								}
							}
						}
					}
				}

			}
		} else {
			if (sortBy == 1) {
				User user = userRepository.findOneByUserLogin_Id(userId);
				if (user != null) {

						List<Store> storeListAll = storeRepository.findByRegionalManagers_IdAndIsActiveTrueOrderByNameAsc(user.getId());

						for (int i = 0; i < storeListAll.size(); i++) {
							if (storeListAll.get(i).getId() == storeId) {
								Store st = storeListAll.get(i);
								if (st != null) {
									storeListAll.clear();
									storeListAll.add(st);
								}
							}
						}

						if (storeListAll.size() > 0) {
							int cycleCount = 0;
							List<AckStockTakeCycle> ackStockTakeCycleList = new ArrayList<AckStockTakeCycle>();

							for (Store store : storeListAll) {
								ackStockTakeCycleList = ackStockTakeCycleRepository.findByStore_IdAndIsActiveTrue(store.getId());
								if (ackStockTakeCycleList.size() > 0) {
									for (AckStockTakeCycle ackStockTakeCycle : ackStockTakeCycleList) {
										
//										AckStockTakeCycle ackStockTakeCycleIfActiveToday = ackStockTakeCycleRepository.findByStoreIdIfStillActive(ackStockTakeCycle.getStore().getId());
										AckStockTakeCycle ackStockTakeCycleIfActiveToday = ackStockTakeCycleRepository.findByStoreIdIfStillActiveAndComplete(ackStockTakeCycle.getStore().getId(),ackStockTakeCycle.getId());
										if(ackStockTakeCycleIfActiveToday != null) {
											cycleCount++;
										}else {
											cycleCount =0;
										}
											
										
										ArrayList<Object> singleList = new ArrayList<Object>();
										singleList.add(ackStockTakeCycle.getId().toString());
										singleList.add(store.getId().toString());

										singleList.add(store.getName().toString());
										
										String stockDtList = ackStockTakeCycleRepository.findByManualId(ackStockTakeCycle.getId());
										
										String[] stockArray = stockDtList.split(",");
										String stockDt = stockArray[0];
										
										String pattern = "yyyy-MM-dd";
										SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

										String date = simpleDateFormat.format(new Date());
										System.out.println(date);

										String stDate = simpleDateFormat.format(new Date());
										System.out.println(date);

										SimpleDateFormat formatter11 = new SimpleDateFormat("yyyy-MM-dd");
										String date11 = simpleDateFormat.format(new Date());
										Date std = null;
										Date d = null;
										try {

											std = formatter11.parse(stockDt);
											d = formatter11.parse(date11);
											System.out.println("-d-" + d);

										} catch (ParseException e) {
											throw new RuntimeException(e);
										}

										long duration = std.getTime() - d.getTime();

										long diffInDays = TimeUnit.MILLISECONDS.toDays(duration);
										System.out.println("date: " + diffInDays);// date: Thu Mar 14 05:23:06 UTC
																					// 2019__2019-03-30 18:30:00.0___16

										singleList.add(diffInDays + "");
										
										try {
											String datePatternAsDmy = "d MMM yyyy";
											SimpleDateFormat dateFormatAsDmy = new SimpleDateFormat(datePatternAsDmy);
											String formattedDateAsDmy = dateFormatAsDmy.format(ackStockTakeCycle.getStokeTakeDate());
											singleList.add(formattedDateAsDmy);
										} catch (Exception e) {
											singleList.add("");
											logger.debug("Error happening while date format setting: "+e.getMessage());
										}
										
//										singleList.add(ackStockTakeCycle.getStokeTakeDate()); // [1, Sone, 2019-03-30
																								// 18:30:00.0]

										String strTemp = "";
										strTemp = ""
												+ ackCriticalPathRepository
														.countCompAckCpWithStatusisNotDisable(ackStockTakeCycle.getId())
												+ " of " + ackCriticalPathRepository
														.countTotAckCpWithStatusisNotDisable(ackStockTakeCycle.getId());
										singleList.add(strTemp); // Critical path list
										singleList.add(stockArray[1]);
										Map<String, Object> cycleData = stockTakeResultService.getStockTakeResult(ackStockTakeCycle.getStore().getId());
										if(cycleData.size() > 0 && cycleData.get("id") != null) {
											System.out.println(cycleData);
											Object ramStatus = cycleData.get("status_ram");
											if(ramStatus != null) {
												singleList.add(ramStatus+ " by RAM");
											}
											else
											{
												Object dsmStatus = cycleData.get("status_dsm");
												if(dsmStatus != null ) {
													singleList.add(dsmStatus+ " by DSM");
												}else {
													Object smStatus = cycleData.get("status_sm");
													if(smStatus != null) {
														singleList.add(smStatus+ " by SM");
													}else {
														singleList.add("Waiting for SM");
													}
												}
											}
										}else {
											singleList.add("");
										}
										singleList.add(store.getCode());
										listOLists.add(singleList);
										System.out.println(listOLists);
									}
									
									//Fetch cycle from Admin side
									if(cycleCount == 0)
									{
										//Fetching cycle from Admin tables
										if (store.getStockTakeCycle() != null) {
											Optional<StockTakeCycle> StockTakeCycle = stockTakeCycleRepository
													.findById(store.getStockTakeCycle().getId());
//											for (Optional<StockTakeCycle> ackStockTakeCycle : StockTakeCycleList) {
											if (StockTakeCycle != null) {
												ArrayList<Object> singleList = new ArrayList<Object>();
												singleList.add(StockTakeCycle.get().getId().toString());
												singleList.add(store.getId().toString());

												singleList.add(store.getName().toString());
												String stockDt = ackStockTakeCycleRepository
														.findByManualId(StockTakeCycle.get().getId());

												String pattern = "yyyy-MM-dd";
												SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

												String date = simpleDateFormat.format(new Date());
												System.out.println(date);

												String stDate = simpleDateFormat.format(new Date());
												System.out.println(date);

												SimpleDateFormat formatter11 = new SimpleDateFormat("yyyy-MM-dd");
												String date11 = simpleDateFormat.format(new Date());
												Date std = null;
												Date d = null;
												long diffInDays = 0;
												if (stockDt != null) {
													try {

														std = formatter11.parse(stockDt);
														d = formatter11.parse(date11);
														System.out.println("-d-" + d);

													} catch (ParseException e) {
														throw new RuntimeException(e);
													}
												
												long duration = std.getTime() - d.getTime();
												
												 diffInDays = TimeUnit.MILLISECONDS.toDays(duration);
												}
												System.out.println("date: " + diffInDays);// date: Thu Mar 14 05:23:06 UTC
																							// 2019__2019-03-30
																							// 18:30:00.0___16

												singleList.add("");
												singleList.add(null); // [1, Sone,
																											// 2019-03-30
																											// 18:30:00.0]

												String strTemp = "";
												strTemp = ""
														+ criticalPathRepository.countCompAckCpWithStatusisNotDisable(
																StockTakeCycle.get().getId())
														+ " of "
														+ criticalPathRepository.countTotAckCpWithStatusisNotDisable(
																StockTakeCycle.get().getId());
												singleList.add(""); // Critical path list
												singleList.add("");	//AM or PM
												Map<String, Object> cycleData = stockTakeResultService.getStockTakeResult(store.getId());
												if(cycleData.size() > 0 && cycleData.get("id") != null) {
													System.out.println(cycleData);
													
													Object ramStatus = cycleData.get("status_ram");
													if(ramStatus != null) {
														singleList.add(ramStatus+ " by RAM");
													}
													else
													{
														Object dsmStatus = cycleData.get("status_dsm");
														if(dsmStatus != null ) {
															singleList.add(dsmStatus+ " by DSM");
														}else {
															Object smStatus = cycleData.get("status_sm");
															if(smStatus != null) {
																singleList.add(smStatus+ " by SM");
															}else {
																singleList.add("Waiting for SM");
															}
														}
													}
													
												}else {
													singleList.add("");
												}
												singleList.add(store.getCode());
												listOLists.add(singleList);
												System.out.println(listOLists);
											}
//											}
										}
									}
									
								} else {
									//Fetching cycle from Admin tables
									if (store.getStockTakeCycle() != null) {
										Optional<StockTakeCycle> StockTakeCycle = stockTakeCycleRepository
												.findById(store.getStockTakeCycle().getId());
//										for (Optional<StockTakeCycle> ackStockTakeCycle : StockTakeCycleList) {
										if (StockTakeCycle != null) {
											ArrayList<Object> singleList = new ArrayList<Object>();
											singleList.add(StockTakeCycle.get().getId().toString());
											singleList.add(store.getId().toString());

											singleList.add(store.getName().toString());
											String stockDt = ackStockTakeCycleRepository
													.findByManualId(StockTakeCycle.get().getId());

											String pattern = "yyyy-MM-dd";
											SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

											String date = simpleDateFormat.format(new Date());
											System.out.println(date);

											String stDate = simpleDateFormat.format(new Date());
											System.out.println(date);

											SimpleDateFormat formatter11 = new SimpleDateFormat("yyyy-MM-dd");
											String date11 = simpleDateFormat.format(new Date());
											Date std = null;
											Date d = null;
											long diffInDays = 0;
											if (stockDt != null) {
												try {

													std = formatter11.parse(stockDt);
													d = formatter11.parse(date11);
													System.out.println("-d-" + d);

												} catch (ParseException e) {
													throw new RuntimeException(e);
												}
											
											long duration = std.getTime() - d.getTime();
											
											 diffInDays = TimeUnit.MILLISECONDS.toDays(duration);
											}
											System.out.println("date: " + diffInDays);// date: Thu Mar 14 05:23:06 UTC
																						// 2019__2019-03-30
																						// 18:30:00.0___16

											singleList.add("");
											singleList.add(null); // [1, Sone,
																										// 2019-03-30
																										// 18:30:00.0]

											String strTemp = "";
											strTemp = ""
													+ criticalPathRepository.countCompAckCpWithStatusisNotDisable(
															StockTakeCycle.get().getId())
													+ " of "
													+ criticalPathRepository.countTotAckCpWithStatusisNotDisable(
															StockTakeCycle.get().getId());
											singleList.add(""); // Critical path list
											singleList.add("");	//AM or PM
											Map<String, Object> cycleData = stockTakeResultService.getStockTakeResult(store.getId());
											if(cycleData.size() > 0 && cycleData.get("id") != null) {
												System.out.println(cycleData);
												
												Object ramStatus = cycleData.get("status_ram");
												if(ramStatus != null) {
													singleList.add(ramStatus+ " by RAM");
												}
												else
												{
													Object dsmStatus = cycleData.get("status_dsm");
													if(dsmStatus != null ) {
														singleList.add(dsmStatus+ " by DSM");
													}else {
														Object smStatus = cycleData.get("status_sm");
														if(smStatus != null) {
															singleList.add(smStatus+ " by SM");
														}else {
															singleList.add("Waiting for SM");
														}
													}
												}
												
											}else {
												singleList.add("");
											}
											singleList.add(store.getCode());
											listOLists.add(singleList);
											System.out.println(listOLists);
										}
//										}
									}

								}

							}
						}
//					}
				}

			}
		}

////				ls = stockTakeCycleRepository.getAllActiveStockTakeCycleDsmUserSortedByName(userId);
//			else if(sortBy == 3)
//				ls = stockTakeCycleRepository.getAllActiveStockTakeCycleDsmUserSortedByTakeDate(userId);

		return listOLists;
//		return null;
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
	public List<AckStockTakeCycle> getAllDeletedStockTakeCycle() {
		return ackStockTakeCycleRepository.findAllByIsActiveFalseAndIsDeleteTrue();
	}

	/**
	 * Get Stock Take Cycle By stockTakeCycleId
	 * 
	 * @param stockTakeCycleId
	 * @return
	 */
	public Object getStockTakeCycleData(Long stockTakeCycleId) {

		Optional<AckStockTakeCycle> stockCycle = ackStockTakeCycleRepository.findById(stockTakeCycleId);

		Store stores = stockCycle.get().getStore();

		if (stores != null)
			return stockCycle;
		else
			return null;
	}

	/**
	 * Deletes the record
	 * 
	 * @param stockTakeCycleId
	 * @return
	 * @throws CPException
	 */
	public void deleteStockTakeCycle(Long stockTakeCycleId) throws CPException {

		AckStockTakeCycle cycle = ackStockTakeCycleRepository.getOne(stockTakeCycleId);
		cycle.setIsActive(false);
		cycle.setIsDelete(true);
		ackStockTakeCycleRepository.save(cycle);
	}

	/**
	 * Hides the record
	 * 
	 * @param stockTakeCycleId
	 * @throws CPException
	 */
	public void hideStockTakeCycle(Long stockTakeCycleId) throws CPException {

		AckStockTakeCycle stockTakeCycle = ackStockTakeCycleRepository.getOne(stockTakeCycleId);

		stockTakeCycle.setIsDisplay(false);
		ackStockTakeCycleRepository.save(stockTakeCycle);
	}

	/**
	 * Restores the record
	 * 
	 * @param stockTakeCycleId
	 */
	public void restoreStockTakeCycle(Long stockTakeCycleId) {
		AckStockTakeCycle stockTakeCycle = ackStockTakeCycleRepository.getOne(stockTakeCycleId);
		stockTakeCycle.setIsActive(true);
		stockTakeCycle.setIsDelete(false);
		ackStockTakeCycleRepository.save(stockTakeCycle);
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

	public AckStockTakeCycle saveStockTakeDate(Long stockTakeCycleId, String stockTakeDate) throws ParseException {

		AckStockTakeCycle stockTakeCycle = ackStockTakeCycleRepository.getOne(stockTakeCycleId);

		Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(stockTakeDate);

		stockTakeCycle.setStokeTakeDate(date1);
		return ackStockTakeCycleRepository.save(stockTakeCycle);
	}

	public List<Object> getStockTakeCycleByUser(Long userId) {

//		return stockTakeCycleRepository.getStockTakeCycleByUser(userId);
		return null;
	}

	public HashMap<String, Object> isActiveCycleAvail(Long userId) {
		HashMap<String, Object> activeCycleComp = new HashMap<String, Object>();
		try {
			User user = userRepository.findOneByUserLogin_Id(userId);
			if (user != null) {
				Store store = user.getStore();
				if (store != null) {
					AckStockTakeCycle activeCycle = ackStockTakeCycleRepository
							.findByStoreIdIfAvailCurrently(store.getId());
					if (activeCycle != null) {
						activeCycleComp.put("storeId", store.getId().toString());
						activeCycleComp.put("storeName", store.getName());
						activeCycleComp.put("cycleId", activeCycle.getId().toString());
						activeCycleComp.put("takeDate", activeCycle.getStokeTakeDate());
						activeCycleComp.put("startDate", activeCycle.getStokeStartDate());
					} else {
						StockTakeCycle cycle = store.getStockTakeCycle();
						if (cycle != null) {
							activeCycleComp.put("storeId", store.getId().toString());
							activeCycleComp.put("storeName", store.getName());
							activeCycleComp.put("cycleId", cycle.getId().toString());
							activeCycleComp.put("takeDate", null);
						}
					}
				}
			}

			// Getting data from Admin tables
//			User user = userRepository.findOneByUserLogin_Id(userId);
//			if(user != null)
//			{
//				Store store = user.getStore();
//				if(store != null)
//				{
//					StockTakeCycle stc = store.getStockTakeCycle();
//				}
//			}

		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
//			return false;
		}
		return activeCycleComp;

	}

	public Object addAckCycle(Long cycleId, String startDate, String stockTakeDate, Long storeId, String time)
			throws ParseException {

		StockTakeCycle StockTakeCycle = stockTakeCycleRepository.getOne(cycleId);

		AckStockTakeCycle cycleAdded = addingAckermanNewCycle(startDate, stockTakeDate, storeId, StockTakeCycle, time);

		List<CriticalPath> criticalpath = criticalPathRepository.findByCycleIdWithStatusisNotDisable(cycleId);

		for (CriticalPath criticalPath2 : criticalpath) {
			AckCriticalPath ackCriticalPath = new AckCriticalPath();

			ackCriticalPath.setCreatedDateTime(new Date());
			ackCriticalPath.setUpdatedDateTime(new Date());
			if (criticalPath2.getIsActive() != null)
				ackCriticalPath.setIsActive(criticalPath2.getIsActive());
			if (criticalPath2.getIsDelete() != null)
				ackCriticalPath.setIsDelete(criticalPath2.getIsDelete());

			if (criticalPath2.getDescription() != null)
				ackCriticalPath.setDescription(criticalPath2.getDescription());
			if (criticalPath2.getLength() != 0)
				ackCriticalPath.setLength(criticalPath2.getLength());
			if (criticalPath2.getStartDay() != 0)
				ackCriticalPath.setStartDay(criticalPath2.getStartDay());
			if (criticalPath2.getTitle() != null)
				ackCriticalPath.setTitle(criticalPath2.getTitle());
			if (criticalPath2.getCreatedBy() != null)
				ackCriticalPath.setCreatedBy(criticalPath2.getCreatedBy());
			if (criticalPath2.getStatus() != null)
				ackCriticalPath.setStatus(criticalPath2.getStatus());

			if (cycleAdded.getId() != null)
				ackCriticalPath.setCycle(cycleAdded);
			ackCriticalPathRepository.save(ackCriticalPath);
		}

		try {

			sendXmlToPepstoreApiClient(cycleId, stockTakeDate, storeId, time);

		} catch (Exception e) {
			logger.debug("Error while Sending XML to pepstoreAPiClient: " + e.getMessage());
		}

		return cycleAdded;
	}

	private boolean sendXmlToPepstoreApiClient(Long cycleId, String stockTakeDate, Long storeId, String time) {

		Long companyTpId = null;
		try {
			companyTpId = storeRepository.findById(storeId).get().getBrand().getThirdPartyId();
		} catch (Exception e) {
			
		}
		
		String branchCode = null;
		try {
			branchCode = storeRepository.findById(storeId).get().getCode().trim();
		} catch (Exception e) {
		}
		
		Long ramTpId = null;
		try {
			 ramTpId = storeRepository.findById(storeId).get().getRegionalManagers().getThirdPartyId();
		} catch (Exception e) {
		}
		
		Long dsmTpId = null;
		try {
			 dsmTpId = storeRepository.findById(storeId).get().getLocationDivision().getDivisionalSalesManagers()
					.getThirdPartyId();
		} catch (Exception e) {
		}

		return pepstoresAPIClient.sendCycleCompletionDate(companyTpId, branchCode, ramTpId, dsmTpId, stockTakeDate, time);
	}
	
	private boolean sendXmlToPepstoreApiClientForTimeUpdate(Long cycleId, String stockTakeDate, Long storeId, String time) {

		Long companyTpId = null;
		try {
			companyTpId = storeRepository.findById(storeId).get().getBrand().getThirdPartyId();
		} catch (Exception e) {
			
		}
		
		String branchCode = null;
		try {
			branchCode = storeRepository.findById(storeId).get().getCode().trim();
		} catch (Exception e) {
		}
		
		Long ramTpId = null;
		try {
			 ramTpId = storeRepository.findById(storeId).get().getRegionalManagers().getThirdPartyId();
		} catch (Exception e) {
		}
		
		Long dsmTpId = null;
		try {
			 dsmTpId = storeRepository.findById(storeId).get().getLocationDivision().getDivisionalSalesManagers()
					.getThirdPartyId();
		} catch (Exception e) {
		}

		return pepstoresAPIClient.sendCycleWhileUpdateForTime(companyTpId, branchCode, ramTpId, dsmTpId, stockTakeDate, time);
	}


	private AckStockTakeCycle addingAckermanNewCycle(String startDate, String stockTakeDate, Long storeId,
			StockTakeCycle StockTakeCycle, String time) throws ParseException {
		AckStockTakeCycle ackStockTakeCycle = new AckStockTakeCycle();

		ackStockTakeCycle.setCreatedDateTime(new Date());
		ackStockTakeCycle.setUpdatedDateTime(new Date());

		if (StockTakeCycle.getCreatedBy() != null)
			ackStockTakeCycle.setCreatedBy(StockTakeCycle.getCreatedBy());

		if (StockTakeCycle.getIsActive() != null)
			ackStockTakeCycle.setIsActive(StockTakeCycle.getIsActive());

		if (StockTakeCycle.getIsDelete() != null)
			ackStockTakeCycle.setIsDelete(StockTakeCycle.getIsDelete());

		if (StockTakeCycle.getDoNotCount() != null)
			ackStockTakeCycle.setDoNotCount(StockTakeCycle.getDoNotCount());

		if (StockTakeCycle.getIsDisplay() != null)
			ackStockTakeCycle.setIsDisplay(StockTakeCycle.getIsDisplay());

		if (StockTakeCycle.getLength() != null)
			ackStockTakeCycle.setLength(StockTakeCycle.getLength());

		if (StockTakeCycle.getName() != null)
			ackStockTakeCycle.setName(StockTakeCycle.getName());

		if (storeId != null) {
			Store store = storeRepository.getOne(storeId);
			ackStockTakeCycle.setStore(store);
		}
		
		if (startDate != null) {
			Date date1 = new SimpleDateFormat("MM-dd-yyyy").parse(startDate);
			ackStockTakeCycle.setStokeStartDate(date1);
		}

		if (stockTakeDate != null) {
			Date date1 = new SimpleDateFormat("MM-dd-yyyy").parse(stockTakeDate);
			ackStockTakeCycle.setStokeTakeDate(date1);
		}

//		if (startDate != null) {
//			Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
//			ackStockTakeCycle.setStokeStartDate(date1);
//		}
//
//		if (stockTakeDate != null) {
//			Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(stockTakeDate);
//			ackStockTakeCycle.setStokeTakeDate(date1);
//		}

		ackStockTakeCycle.setTime(time);

		ackStockTakeCycle.setIsCompleted(false);

		ackStockTakeCycle = ackStockTakeCycleRepository.save(ackStockTakeCycle);

		return ackStockTakeCycle;
	}

	public Date calculateStartDate(Long stockTakeCycleId, String stockTakeDate, Long storeId) throws ParseException {

		AckStockTakeCycle stockTakeCycle = ackStockTakeCycleRepository.getOne(stockTakeCycleId);
		int cycleLength = Integer.parseInt(stockTakeCycle.getLength());
		int taskLength = cycleLength - 1;
		Date stockTakeDt = new SimpleDateFormat("dd/MM/yyyy").parse(stockTakeDate);

		Calendar stockDate = Calendar.getInstance();
		stockDate.setTime(stockTakeDt);

		Calendar startdt = Calendar.getInstance();
		startdt.setTime(stockTakeDt);

		startdt.add(Calendar.DAY_OF_MONTH, -taskLength);
		System.out.println(startdt.getTime());

		List<Object> doNotCountList = new ArrayList<Object>();
		List<String> remainingList = new ArrayList<String>();

		calculateList(startdt, stockDate, stockTakeCycleId, storeId, doNotCountList, remainingList);

		while (remainingList.size() < cycleLength) {
			calculateStartDate(stockTakeCycleId, storeId, cycleLength, stockDate, startdt, doNotCountList,
					remainingList);
		}

		Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse(remainingList.get(0));
		System.out.println("list out  =========" + startDate);
		return startDate;

//		getStartDate(remainingList,startdt, stockDate ,  stockTakeCycleId , storeId, doNotCountList);

	}

	private void calculateStartDate(Long stockTakeCycleId, Long storeId, int cycleLength, Calendar stockDate,
			Calendar startdt, List<Object> doNotCountList, List<String> remainingList) throws ParseException {
		int removedays = cycleLength + doNotCountList.size();

		startdt.add(Calendar.DAY_OF_MONTH, -removedays);
		calculateList(startdt, stockDate, stockTakeCycleId, storeId, doNotCountList, remainingList);
	}

	public void calculateList(Calendar startdt, Calendar stockDate, Long stockTakeCycleId, Long storeId,
			List<Object> doNotCountList, List<String> remainingList) throws ParseException {

		doNotCountList.clear();
		remainingList.clear();

		Calendar start = startdt;
		System.out.println("start date ------------ " + start.getTime());
		List<String> daysOfList = new ArrayList<String>();
		daysOfList.add("");
		daysOfList.add("Sunday");
		daysOfList.add("Monday");
		daysOfList.add("Tuesday");
		daysOfList.add("Wednesday");
		daysOfList.add("Thursday");
		daysOfList.add("Friday");
		daysOfList.add("Saturday");
		List<Integer> daysIndexList = new ArrayList<Integer>();

		AckStockTakeCycle stockTakeCycle = ackStockTakeCycleRepository.getOne(stockTakeCycleId);
		int cycleLength = Integer.parseInt(stockTakeCycle.getLength());

		String donotCountDays = stockTakeCycle.getDoNotCount();
		List<String> donotCountDaystemp = new ArrayList<String>();
		if (donotCountDays != null) {
			donotCountDaystemp = Arrays.asList(donotCountDays.split(", "));
		}

		for (int i = 0; i < daysOfList.size(); i++) {
			for (int j = 0; j < donotCountDaystemp.size(); j++) {
				if (daysOfList.get(i).equals(donotCountDaystemp.get(j))) {
					daysIndexList.add(i);
				}
			}
		}

		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");

		List<Object> closeDateList = storeCloseDateRepository.closedDateByStoreId(storeId, startdt, stockDate);

		stockDate.add(Calendar.DATE, 1);
		Calendar startDate = startdt;

		while (stockDate.after(startdt)) {
			for (int i = 0; i < daysIndexList.size(); i++) {
				if (startdt.get(Calendar.DAY_OF_WEEK) == daysIndexList.get(i)) {
					String formatted = format1.format(startdt.getTime());
					doNotCountList.add(formatted);
				}
			}
			String formatted = format1.format(startdt.getTime());
			remainingList.add(formatted);
			startdt.add(Calendar.DATE, 1);
		}
		stockDate.add(Calendar.DATE, -1);

		for (Object CloseDate : closeDateList) {
			if (!doNotCountList.contains(CloseDate))
				doNotCountList.add(CloseDate);
		}

		for (int i = 0; i < doNotCountList.size(); i++) {
			if (remainingList.contains(doNotCountList.get(i)))
				remainingList.remove(doNotCountList.get(i));
		}

		System.out.println(doNotCountList);
		System.out.println(remainingList);

	}

//	private Date getStartDate(List<String> remainingList, Calendar startDate, Calendar stockDate, Long stockTakeCycleId,
//			Long storeId, List<Object> doNotCountList) throws ParseException {
//		StockTakeCycle stockTakeCycle = stockTakeCycleRepository.getOne(stockTakeCycleId);
//		int cycleLength = Integer.parseInt(stockTakeCycle.getLength());
//
//		if (remainingList.size() < cycleLength) {
//			int removedays = cycleLength + doNotCountList.size();
//
//			startDate.add(Calendar.DAY_OF_MONTH, -removedays);
////			calculateList(startDate, stockDate,stockTakeCycleId , storeId);
//
//		} else {
//			System.out.println("list =========" + remainingList.get(0));
//
//			Date stockTakeDt = new SimpleDateFormat("yyyy-MM-dd").parse(remainingList.get(0));
//			System.out.println("list out  =========" + stockTakeDt);
//			return stockTakeDt;
//		}
//
//		Date stockTakeDt = new SimpleDateFormat("yyyy-MM-dd").parse(remainingList.get(0));
//		System.out.println("list out  =========" + stockTakeDt);
//		return stockTakeDt;
//	}

	public List<String> getAllDateList(Long stockTakeCycleId, String stockTakeDate) throws ParseException {

		AckStockTakeCycle ackStockTakeCycle = ackStockTakeCycleRepository.getOne(stockTakeCycleId);
		int cycleLength = Integer.parseInt(ackStockTakeCycle.getLength());
		int taskLength = cycleLength - 1;
		Date stockTakeDt = new SimpleDateFormat("MM-dd-yyyy").parse(stockTakeDate);

		Calendar stockDate = Calendar.getInstance();
		stockDate.setTime(stockTakeDt);

		Calendar startdt = Calendar.getInstance();
		startdt.setTime(stockTakeDt);

		startdt.add(Calendar.DAY_OF_MONTH, -taskLength);
		System.out.println(startdt.getTime());

		List<Object> doNotCountList = new ArrayList<Object>();
		List<String> remainingList = new ArrayList<String>();

		calculateDateList(startdt, stockDate, stockTakeCycleId, ackStockTakeCycle.getStore().getId(), doNotCountList,
				remainingList);

		while (remainingList.size() < cycleLength) {
			calculateAllDateList(stockTakeCycleId, ackStockTakeCycle.getStore().getId(), cycleLength, stockDate,
					startdt, doNotCountList, remainingList);
		}

//			Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse(remainingList.get(0));
//			System.out.println("list out  =========" + startDate);
		return remainingList;
	}

	private void calculateAllDateList(Long stockTakeCycleId, Long storeId, int cycleLength, Calendar stockDate,
			Calendar startdt, List<Object> doNotCountList, List<String> remainingList) throws ParseException {
		int removedays = cycleLength + doNotCountList.size();

		startdt.add(Calendar.DAY_OF_MONTH, -removedays);
		calculateDateList(startdt, stockDate, stockTakeCycleId, storeId, doNotCountList, remainingList);
	}

	public void calculateDateList(Calendar startdt, Calendar stockDate, Long stockTakeCycleId, Long storeId,
			List<Object> doNotCountList, List<String> remainingList) throws ParseException {

		doNotCountList.clear();
		remainingList.clear();

		Calendar start = startdt;
		System.out.println("start date ------------ " + start.getTime());
		List<String> daysOfList = new ArrayList<String>();
		daysOfList.add("");
		daysOfList.add("Sunday");
		daysOfList.add("Monday");
		daysOfList.add("Tuesday");
		daysOfList.add("Wednesday");
		daysOfList.add("Thursday");
		daysOfList.add("Friday");
		daysOfList.add("Saturday");
		List<Integer> daysIndexList = new ArrayList<Integer>();

		AckStockTakeCycle ackStockTakeCycle = ackStockTakeCycleRepository.getOne(stockTakeCycleId);
		int cycleLength = Integer.parseInt(ackStockTakeCycle.getLength());

		String donotCountDays = ackStockTakeCycle.getDoNotCount();
		List<String> donotCountDaystemp = new ArrayList<String>();
		if (donotCountDays != null) {
			donotCountDaystemp = Arrays.asList(donotCountDays.split(", "));
		}

		for (int i = 0; i < daysOfList.size(); i++) {
			for (int j = 0; j < donotCountDaystemp.size(); j++) {
				if (daysOfList.get(i).equals(donotCountDaystemp.get(j))) {
					daysIndexList.add(i);
				}
			}
		}

		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");

		List<Object> closeDateList = storeCloseDateRepository.closedDateByStoreId(storeId, startdt, stockDate);

		stockDate.add(Calendar.DATE, 1);
		Calendar startDate = startdt;

		while (stockDate.after(startdt)) {
			for (int i = 0; i < daysIndexList.size(); i++) {
				if (startdt.get(Calendar.DAY_OF_WEEK) == daysIndexList.get(i)) {
					String formatted = format1.format(startdt.getTime());
					doNotCountList.add(formatted);
				}
			}
			String formatted = format1.format(startdt.getTime());
			remainingList.add(formatted);
			startdt.add(Calendar.DATE, 1);
		}
		stockDate.add(Calendar.DATE, -1);

		for (Object CloseDate : closeDateList) {
			if (!doNotCountList.contains(CloseDate))
				doNotCountList.add(CloseDate);
		}

		for (int i = 0; i < doNotCountList.size(); i++) {
			if (remainingList.contains(doNotCountList.get(i)))
				remainingList.remove(doNotCountList.get(i));
		}

		System.out.println(doNotCountList);
		System.out.println(remainingList);

	}

	public Object calculateMultipleStartDate(List storeList) throws ParseException {
		System.out.println(storeList);
		List<ArrayList<Object>> startDateList = new ArrayList<ArrayList<Object>>();
		for(int i=0;i<storeList.size();i++) {
			HashMap<String, String> json =  (HashMap<String, String>) storeList.get(i);
			System.out.println(json.get("storeName"));
			int cycleLength;
			String flag;
			if(json.get("takeDate") == null) {
				StockTakeCycle stockTakeCycle = stockTakeCycleRepository.getOne(Long.parseLong(json.get("cycleId")));
				 cycleLength = Integer.parseInt(stockTakeCycle.getLength());
				 flag ="A";
			}else {
				AckStockTakeCycle ackStockTakeCycle = ackStockTakeCycleRepository.getOne(Long.parseLong(json.get("cycleId")));
				 cycleLength = Integer.parseInt(ackStockTakeCycle.getLength());
				 flag = "K";
			}
		
			
			int taskLength = cycleLength - 1;
			Date stockTakeDt = null;
			try {
				stockTakeDt = new SimpleDateFormat("MM-dd-yyyy").parse(json.get("stockTakeDateFormatted"));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Calendar stockDate = Calendar.getInstance();
			stockDate.setTime(stockTakeDt);

			Calendar startdt = Calendar.getInstance();
			startdt.setTime(stockTakeDt);

			startdt.add(Calendar.DAY_OF_MONTH, -taskLength);
			System.out.println(startdt.getTime());

			List<Object> doNotCountList = new ArrayList<Object>();
			List<String> remainingList = new ArrayList<String>();

			calculateMultipleStartDateList(startdt, stockDate,json, doNotCountList, remainingList);

			while (remainingList.size() < cycleLength) {
				calculateMultipleStartDate(json, cycleLength, stockDate, startdt, doNotCountList,
						remainingList);
			}
			List<Object> tempList = new ArrayList<Object>();
			Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse(remainingList.get(0));
			SimpleDateFormat format1 = new SimpleDateFormat("MM-dd-yyyy");
			String formattedStartDt = format1.format(startDate.getTime());
			tempList.add(json.get("storeName"));
			tempList.add(formattedStartDt);
			tempList.add(json.get("stockTakeDateFormatted"));
			tempList.add(flag);
			tempList.add(json.get("storeId"));
			tempList.add(json.get("cycleId"));
			startDateList.add((ArrayList<Object>) tempList);
			System.out.println("list out  =========" + startDate);
//			return startDate;			
		}
		return startDateList;
	}
	
	private void calculateMultipleStartDate(HashMap<String, String> json, int cycleLength, Calendar stockDate,
			Calendar startdt, List<Object> doNotCountList, List<String> remainingList) throws ParseException {
		int removedays = cycleLength + doNotCountList.size();

		startdt.add(Calendar.DAY_OF_MONTH, -removedays);
		calculateMultipleStartDateList(startdt, stockDate,  json, doNotCountList, remainingList);
	}

	public void calculateMultipleStartDateList(Calendar startdt, Calendar stockDate, HashMap<String, String> json,
			List<Object> doNotCountList, List<String> remainingList) throws ParseException {

		doNotCountList.clear();
		remainingList.clear();

		Calendar start = startdt;
		System.out.println("start date ------------ " + start.getTime());
		List<String> daysOfList = new ArrayList<String>();
		daysOfList.add("");
		daysOfList.add("Sunday");
		daysOfList.add("Monday");
		daysOfList.add("Tuesday");
		daysOfList.add("Wednesday");
		daysOfList.add("Thursday");
		daysOfList.add("Friday");
		daysOfList.add("Saturday");
		List<Integer> daysIndexList = new ArrayList<Integer>();
		int cycleLength;
		String donotCountDays;
		
		if(json.get("takeDate") == null) {
			StockTakeCycle stockTakeCycle = stockTakeCycleRepository.getOne(Long.parseLong(json.get("cycleId")));
			 cycleLength = Integer.parseInt(stockTakeCycle.getLength());
			 donotCountDays = stockTakeCycle.getDoNotCount();
		}else {
			AckStockTakeCycle ackStockTakeCycle = ackStockTakeCycleRepository.getOne(Long.parseLong(json.get("cycleId")));
			 cycleLength = Integer.parseInt(ackStockTakeCycle.getLength());
			 donotCountDays = ackStockTakeCycle.getDoNotCount();
		}
//		StockTakeCycle stockTakeCycle = stockTakeCycleRepository.getOne(Long.parseLong(json.get("cycleId")));
//		int cycleLength = Integer.parseInt(stockTakeCycle.getLength());

//		String donotCountDays = stockTakeCycle.getDoNotCount();
		List<String> donotCountDaystemp = new ArrayList<String>();
		if (donotCountDays != null) {
			donotCountDaystemp = Arrays.asList(donotCountDays.split(", "));
		}

		for (int i = 0; i < daysOfList.size(); i++) {
			for (int j = 0; j < donotCountDaystemp.size(); j++) {
				if (daysOfList.get(i).equals(donotCountDaystemp.get(j))) {
					daysIndexList.add(i);
				}
			}
		}

		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");

		List<Object> closeDateList = storeCloseDateRepository.closedDateByStoreId(Long.parseLong(json.get("storeId")), startdt, stockDate);

		stockDate.add(Calendar.DATE, 1);
		Calendar startDate = startdt;

		while (stockDate.after(startdt)) {
			for (int i = 0; i < daysIndexList.size(); i++) {
				if (startdt.get(Calendar.DAY_OF_WEEK) == daysIndexList.get(i)) {
					String formatted = format1.format(startdt.getTime());
					doNotCountList.add(formatted);
				}
			}
			String formatted = format1.format(startdt.getTime());
			remainingList.add(formatted);
			startdt.add(Calendar.DATE, 1);
		}
		stockDate.add(Calendar.DATE, -1);

		for (Object CloseDate : closeDateList) {
			if (!doNotCountList.contains(CloseDate))
				doNotCountList.add(CloseDate);
		}

		for (int i = 0; i < doNotCountList.size(); i++) {
			if (remainingList.contains(doNotCountList.get(i)))
				remainingList.remove(doNotCountList.get(i));
		}

		System.out.println(doNotCountList);
		System.out.println(remainingList);

	}

	public Object addMultipleStartDate(List<Object> selectedStoreList) throws ParseException {
		for(int i=0;i<selectedStoreList.size();i++) {
			List<String> selectedStoreListObj = (List<String>) selectedStoreList.get(i);
			
			String startDate = selectedStoreListObj.get(1);
			String stockTakeDate = selectedStoreListObj.get(2);
			Long storeId = Long.parseLong(selectedStoreListObj.get(4));
			String time = selectedStoreListObj.get(6);
			Long cycleId = Long.parseLong(selectedStoreListObj.get(5));
			
			if(sendXmlToPepstoreApiClient(cycleId, stockTakeDate, storeId, time)) {
			if(selectedStoreListObj.get(3).equals("A")) {
				StockTakeCycle StockTakeCycle = stockTakeCycleRepository.getOne(cycleId);
			
				
				AckStockTakeCycle cycleAdded = addingAckermanNewCycle(startDate, stockTakeDate, storeId, StockTakeCycle, time);
				
				List<CriticalPath> criticalpath = criticalPathRepository.findByCycleIdWithStatusisNotDisable(cycleId);

				for (CriticalPath criticalPath2 : criticalpath) {
					AckCriticalPath ackCriticalPath = new AckCriticalPath();

					ackCriticalPath.setCreatedDateTime(new Date());
					ackCriticalPath.setUpdatedDateTime(new Date());
					if (criticalPath2.getIsActive() != null)
						ackCriticalPath.setIsActive(criticalPath2.getIsActive());
					if (criticalPath2.getIsDelete() != null)
						ackCriticalPath.setIsDelete(criticalPath2.getIsDelete());

					if (criticalPath2.getDescription() != null)
						ackCriticalPath.setDescription(criticalPath2.getDescription());
					if (criticalPath2.getLength() != 0)
						ackCriticalPath.setLength(criticalPath2.getLength());
					if (criticalPath2.getStartDay() != 0)
						ackCriticalPath.setStartDay(criticalPath2.getStartDay());
					if (criticalPath2.getTitle() != null)
						ackCriticalPath.setTitle(criticalPath2.getTitle());
					if (criticalPath2.getCreatedBy() != null)
						ackCriticalPath.setCreatedBy(criticalPath2.getCreatedBy());
					if (criticalPath2.getStatus() != null)
						ackCriticalPath.setStatus(criticalPath2.getStatus());

					if (cycleAdded.getId() != null)
						ackCriticalPath.setCycle(cycleAdded);
					ackCriticalPathRepository.save(ackCriticalPath);
				}
				
				try {
					auditService.generateAuditMessageForAddCycle(cycleAdded.getName());
				} catch (Exception e) {
					logger.debug("Error in addMultipleStartDate while call audit service :  "+e.getMessage());
				}
			}
			else {
					AckStockTakeCycle ackStockTakeCycle = ackStockTakeCycleRepository.findById(cycleId).get();
				
				if (startDate != null) {
					Date date1 = new SimpleDateFormat("MM-dd-yyyy").parse(startDate);
					ackStockTakeCycle.setStokeStartDate(date1);
				}

				if (stockTakeDate != null) {
					Date date1 = new SimpleDateFormat("MM-dd-yyyy").parse(stockTakeDate);
					ackStockTakeCycle.setStokeTakeDate(date1);
				}
				
				ackStockTakeCycle.setTime(time);
				ackStockTakeCycleRepository.save(ackStockTakeCycle);
				
				try {
					auditService.generateAuditMessageForAddCycle(ackStockTakeCycle.getName());
				} catch (Exception e) {
					logger.debug("Error in addMultipleStartDate while call audit service :  "+e.getMessage());
				}
			}
		}
		else {
			logger.debug("Error while Your scheduled stock count dates could not be processed: ");
			return ERROR_ADD_CYCLE;
			}
		}
		return null;
	}

	/**
	 * Delete old and create new stock take
	 * @param request
	 * @throws ParseException
	 */
	public Object deleteAndCreateNewStockTake(JsonNode request) throws ParseException {
		
		String date = request.get("newStockDate").asText();
		Date stockTakeDtFromRequestFmt = new SimpleDateFormat("dd/MM/yyyy").parse(date);
		String timeFromRequest  = request.get("newTime").asText();
		Long stockTakeCycleIdFromReq = request.get("id").asLong();
		
		AckStockTakeCycle ackStockTakeCycleOld  = ackStockTakeCycleRepository.findById(stockTakeCycleIdFromReq).get();
		
		long store_id = ackStockTakeCycleOld.getStore().getId();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

		String stockDateFromReqAsString = simpleDateFormat.format(stockTakeDtFromRequestFmt);
		String stockTakeDateOld = simpleDateFormat.format(ackStockTakeCycleOld.getStokeTakeDate());

		Date startDateCalculatedFromNewStockDate = calculateStartDate(stockTakeCycleIdFromReq, stockDateFromReqAsString, store_id);
		boolean delCheck = false;
		
		if (sendXmlToPepstoreApiClientForDelete(stockTakeDateOld, store_id, ackStockTakeCycleOld.getTime())) {

			// Delete old ack_stock_take_date
			ackStockTakeCycleOld.setIsActive(false);
			ackStockTakeCycleOld.setIsDelete(true);
			ackStockTakeCycleRepository.save(ackStockTakeCycleOld);

			List<AckCriticalPath> ackCpListOld = ackCriticalPathRepository.findByCycle_Id(stockTakeCycleIdFromReq);

			// Create new ack_critical_path similar to old one
			if (!ackCpListOld.isEmpty()) {
				for (AckCriticalPath ackSingleCpObjOld : ackCpListOld) {

					// delete old ack_critical_path
					ackSingleCpObjOld.setIsActive(false);
					ackSingleCpObjOld.setIsDelete(true);
					ackCriticalPathRepository.save(ackSingleCpObjOld);
				}
			}
			delCheck = true;
		}
		else {
			logger.debug("Error while cycle could not be removed. Please try again later for store_id: "+store_id);
			delCheck = false;
			return ERROR_DELETE_CYCLE;
		}
			

			//Create new Cycle after deleting the old one 
			if(delCheck && sendXmlToPepstoreApiClient(stockTakeCycleIdFromReq, stockDateFromReqAsString, store_id, timeFromRequest)) {
				
				AckStockTakeCycle ackStockCycleNew = new AckStockTakeCycle();
				ackStockCycleNew.setStokeTakeDate(stockTakeDtFromRequestFmt);
				ackStockCycleNew.setTime(timeFromRequest);
				
				//Create new cycle and save data from the older cycle
				ackStockCycleNew.setDoNotCount(ackStockTakeCycleOld.getDoNotCount());
				ackStockCycleNew.setIsDisplay(ackStockTakeCycleOld.getIsDisplay());
				ackStockCycleNew.setLength(ackStockTakeCycleOld.getLength());
				ackStockCycleNew.setName(ackStockTakeCycleOld.getName());
				ackStockCycleNew.setStokeStartDate(startDateCalculatedFromNewStockDate);
				ackStockCycleNew.setCreatedBy(ackStockTakeCycleOld.getCreatedBy());
				ackStockCycleNew.setStore(ackStockTakeCycleOld.getStore());
				ackStockCycleNew.setIsCompleted(ackStockTakeCycleOld.getIsCompleted());
				AckStockTakeCycle newAckStockTakeObj = ackStockTakeCycleRepository.save(ackStockCycleNew);
				
				List<AckCriticalPath> ackCpListOld = ackCriticalPathRepository.findByCycle_Id(stockTakeCycleIdFromReq);

				// Create new ack_critical_path similar to old one
				if (newAckStockTakeObj != null && !ackCpListOld.isEmpty()) {
					for (AckCriticalPath ackSingleCpObjOld : ackCpListOld) {
						
						try {
							//Create new CP and save data from old CP
							AckCriticalPath ackCpObjNew = new AckCriticalPath();
							ackCpObjNew.setDescription(ackSingleCpObjOld.getDescription());
							ackCpObjNew.setLength(ackSingleCpObjOld.getLength());
							ackCpObjNew.setStartDay(ackSingleCpObjOld.getStartDay());
							ackCpObjNew.setTitle(ackSingleCpObjOld.getTitle());
							ackCpObjNew.setCreatedBy(ackSingleCpObjOld.getCreatedBy());
							ackCpObjNew.setCycle(newAckStockTakeObj);
							ackCpObjNew.setStatus(ackSingleCpObjOld.getStatus());
							ackCriticalPathRepository.save(ackCpObjNew);
						} catch (Exception e) {
							e.printStackTrace();
							logger.debug("Error while Creating new critical path for new stock_cycle_id: "+newAckStockTakeObj.getId()+", error: "+e.getMessage());
						}
						
					}
				}
			}
			else {
				logger.debug("Error while cycle creation. Please try again later for store_id: "+store_id);
				return ERROR_ADD_CYCLE;
			}

		return null;
	}
	
	private boolean sendXmlToPepstoreApiClientForDelete(String stockTakeDate, Long storeId, String time) {

		Long companyTpId = null;
		try {
			 companyTpId = storeRepository.findById(storeId).get().getBrand().getThirdPartyId();
		} catch (Exception e) {
		}
		
		String branchCode = null;
		try {
			 branchCode = storeRepository.findById(storeId).get().getCode().trim();
		} catch (Exception e) {
		}
		
		Long ramTpId = null;
		try {
			ramTpId = storeRepository.findById(storeId).get().getRegionalManagers().getThirdPartyId();
		} catch (Exception e) {
		}
		
		Long dsmTpId = null;
		try {
			dsmTpId = storeRepository.findById(storeId).get().getLocationDivision().getDivisionalSalesManagers()
					.getThirdPartyId();
		} catch (Exception e) {
		}

		return pepstoresAPIClient.sendDeleteCycle(companyTpId, branchCode, ramTpId, dsmTpId, stockTakeDate, time);
	}
	
	public String getUpcomingCycleStartDate(Long storeId) {
		
		String dateToReturn = "";
		List<AckStockTakeCycle> listOfCycle = ackStockTakeCycleRepository.findByStore_IdAndIsActiveTrueOrderByStokeStartDateAsc(storeId);
		if(listOfCycle.size() > 0) {
			for (AckStockTakeCycle ackStockTakeCycle : listOfCycle) {
				if(ackStockTakeCycle.getStokeStartDate().after(new Date())) {
					
					logger.debug("list: "+ackStockTakeCycle.getStokeStartDate());
					
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
					dateToReturn = simpleDateFormat.format(ackStockTakeCycle.getStokeStartDate());
				}
			}
			
		}
		
		return dateToReturn;
	}

	public  List<Date> getStoreStockTakeDate(List<String> storeSelectedList) {
		Date stDate = null;
		List<Date> resultDateList = new ArrayList<Date>();
		try {
			for (String storeId : storeSelectedList) {
				List<AckStockTakeCycle> ackCycleList = ackStockTakeCycleRepository.findByStore_IdAndIsActiveTrue(Long.parseLong(storeId));
				String doNotCount = null;
				String cycleLength = null ;
				List<CloseDate> storeCloseDate = new ArrayList<CloseDate>();
				if(ackCycleList.size() > 0) {
					AckStockTakeCycle ackCycleObj = ackCycleList.get(0);
					doNotCount = ackCycleObj.getDoNotCount();
					cycleLength = ackCycleObj.getLength();
				} else {
					StockTakeCycle stockCycleObj = storeRepository.findById(Long.parseLong(storeId)).get().getStockTakeCycle();
					if(stockCycleObj != null) {
						doNotCount = stockCycleObj.getDoNotCount();
						cycleLength = stockCycleObj.getLength();
					}
				}
				storeCloseDate = storeCloseDateRepository.findByStores_Id(Long.parseLong(storeId));
				List<String> donotCountDaysList = new ArrayList<String>();
				if (doNotCount != null) {
					donotCountDaysList = Arrays.asList(doNotCount.split(", "));
				}
				stDate = getMinPossibleStockTakeDate(cycleLength, donotCountDaysList, storeCloseDate,
						Long.parseLong(storeId));
				if (stDate != null && !resultDateList.contains(stDate)) {
					resultDateList.add(stDate);
				}
				
			}
		} catch (Exception e) {
			logger.debug(e.getMessage());
		}
		return resultDateList;
	}

	private Date getMinPossibleStockTakeDate(String cycleLength, List<String> doNotCount, List<CloseDate> storeCloseDate, Long storeId) {
		
		List<Object> resultList = new ArrayList<Object>();
		List<String> daysOfList = new ArrayList<String>();
		List<Integer> daysIndexList = new ArrayList<Integer>();
		
		daysOfList.add("");
		daysOfList.add("Sunday");
		daysOfList.add("Monday");
		daysOfList.add("Tuesday");
		daysOfList.add("Wednesday");
		daysOfList.add("Thursday");
		daysOfList.add("Friday");
		daysOfList.add("Saturday");
		
		for (int i = 0; i < daysOfList.size(); i++) {
			for (int j = 0; j < doNotCount.size(); j++) {
				if (daysOfList.get(i).equals(doNotCount.get(j))) {
					daysIndexList.add(i);
				}
			}
		}
		
		Calendar stockDate = Calendar.getInstance();
		stockDate.setTime(new Date());

		Calendar startdt = Calendar.getInstance();
		startdt.setTime(new Date());
		
		startdt.add(Calendar.DAY_OF_MONTH, 1);
		logger.debug("startdt: "+startdt.getTime());

		stockDate.add(Calendar.DAY_OF_MONTH, Integer.parseInt(cycleLength)+1);
		logger.debug("stockDate: "+stockDate.getTime());
		
		List<Object> doNotCountList = new ArrayList<Object>();
		List<String> remainingList = new ArrayList<String>();
		
		logger.debug("remainingList: "+remainingList);
		logger.debug("doNotCountList: "+doNotCountList);

		calcStockTakeDateList(startdt, stockDate, Integer.parseInt(cycleLength), storeId, daysIndexList, doNotCountList,
				remainingList);

		while (remainingList.size() < Integer.parseInt(cycleLength)) {
			try {
				calcStockTakeDate(startdt, stockDate, Integer.parseInt(cycleLength), storeId, daysIndexList, doNotCountList,
						remainingList);
			} catch (NumberFormatException | ParseException e) {
				e.printStackTrace();
			}
		}
		
		Date stockTakeDate= null;
		try {
			if(remainingList.size() > Integer.parseInt(cycleLength) || remainingList.size() == Integer.parseInt(cycleLength))
				stockTakeDate = new SimpleDateFormat("yyyy-MM-dd").parse(remainingList.get(Integer.parseInt(cycleLength)-1));
		} catch (ParseException e) {
			e.printStackTrace();
			try {
				calcStockTakeDate(startdt, stockDate, Integer.parseInt(cycleLength), storeId, daysIndexList, doNotCountList,
						remainingList);
			} catch (NumberFormatException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return stockTakeDate;
	}
	
	private void calcStockTakeDate(Calendar startdt, Calendar stockDate, int cycleLength, Long storeId,
			List<Integer> daysIndexList, List<Object> doNotCountList, List<String> remainingList) throws ParseException {
		
		int removedays =  doNotCountList.size();
		stockDate.add(Calendar.DAY_OF_MONTH, removedays);
		logger.debug("=========>"+stockDate.getTime()+"__"+startdt.getTime()+"__"+doNotCountList);
		calcStockTakeDateList(startdt, stockDate, cycleLength, storeId, daysIndexList, doNotCountList,
				remainingList);
	}

	private void calcStockTakeDateList(Calendar startdt, Calendar stockDate, int cycleLength, Long storeId,
			List<Integer> daysIndexList, List<Object> doNotCountList, List<String> remainingList) {

		doNotCountList.clear();
		remainingList.clear();

		List<Object> closeDateList = storeCloseDateRepository.closedDateByStoreId(storeId, startdt, stockDate);
		logger.debug("closedDate: "+closeDateList);
		logger.debug("closedDate: "+closeDateList.size());

		Calendar startDate = Calendar.getInstance();
		startDate.setTime(new Date());
		startDate.add(Calendar.DAY_OF_MONTH, 1);
		
		while (stockDate.after(startDate)) {
			for (int i = 0; i < daysIndexList.size(); i++) {
				if (startDate.get(Calendar.DAY_OF_WEEK) == daysIndexList.get(i)) {
					String formatted = Constants.dateFormatYmd.format(startDate.getTime());
					doNotCountList.add(formatted);
				}
			}
			String formatted = Constants.dateFormatYmd.format(startDate.getTime());
			remainingList.add(formatted);
			startDate.add(Calendar.DATE, 1);
		}

		for (Object CloseDate : closeDateList) {
			if (!doNotCountList.contains(CloseDate))
				doNotCountList.add(CloseDate);
		}

		for (int i = 0; i < doNotCountList.size(); i++) {
			if (remainingList.contains(doNotCountList.get(i)))
				remainingList.remove(doNotCountList.get(i));
		}
		
		logger.debug("doNotCountFinalList: "+doNotCountList);
		logger.debug("doNotCountFinalList: "+doNotCountList.size());
		logger.debug("remainingListFinalList: "+remainingList);
		logger.debug("remainingListFinalList: "+remainingList.size());
		logger.debug("=======");
	}
	
	/**
	 * edit stock take
	 * @param request
	 * @throws ParseException
	 */
	public Object editNewStockTake(JsonNode request) throws ParseException {
		
		String timeFromReq  = request.get("newEditTime").asText();
		Long cycleIdFromReq = request.get("id").asLong();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		
		AckStockTakeCycle ackStockTakeCycleObj  = ackStockTakeCycleRepository.findById(cycleIdFromReq).get();
		try {
			if(ackStockTakeCycleObj != null) {
				String ackStockTakeDate = simpleDateFormat.format(ackStockTakeCycleObj.getStokeTakeDate());
				
				if(sendXmlToPepstoreApiClientForTimeUpdate(ackStockTakeCycleObj.getId(), ackStockTakeDate, ackStockTakeCycleObj.getStore().getId(), timeFromReq)) {
					ackStockTakeCycleObj.setTime(timeFromReq);
					ackStockTakeCycleRepository.save(ackStockTakeCycleObj);
					return ackStockTakeCycleObj;	
				}
				else
				{
					logger.debug("Error while Your cycle could not be update for the given time for store_id: "+ackStockTakeCycleObj.getStore().getId());
					return ERROR_UPDATE_CYCLE;			
				}
			}
			else
			{
				logger.debug("Error while Your cycle could not be update for the given time for store_id: ");
				return ERROR_UPDATE_CYCLE;			
			}
		} catch (Exception e) {
			logger.debug("Error while Your cycle could not be update for the given time for store_id: ");
			return ERROR_UPDATE_CYCLE;
		}
	}
}
