package application;
/**
 * Farm.java created by Vivian Ye on MacBook Pro in ATeamProject Apr 23, 2020
 *
 * Author:	Xunwei Ye (xye53@wisc.edu)
 * Date:	Apr 23. 2020
 * 
 * Course:	CS400
 * Semester:	Spring 2020
 * Lecture:	001
 * 
 * IDE:		Eclipse IDE for Java Developers
 * Version: 	2019-06 (4.12.0)
 * Build id:	20190614-1200
 * 
 * Device:	binhaochen's Macbook Pro
 * OS:		macOS 
 * Version: 	10.15.3
 * OS Build:	N/A
 *
 * List Collaborators: NONE
 *
 * Other Credits: NONE
 *
 * Known Bugs: NONE
 */

import java.util.*;
/**
 * 
 * Defines a farm and its constituent milk supplies
 * 
 * @author Vivian Ye
 *
 */
public class Farm {
	
	private String farmID;
	private String owner;
	private HashMap<String, Integer> milkData;

	public Farm() {
		this.farmID = "";
		this.owner = "";
		this.milkData = new HashMap<>();
	}

	public Farm(String farmID, String owner) {
		this.farmID = farmID;
		this.owner = owner;
		this.milkData = new HashMap<>();
	}
	
	public Farm(String farmID) {
		this.farmID = farmID;
		this.owner = "";
		this.milkData = new HashMap<>();
	}

	/**
	 * insert a piece of milk record into the farm record
	 * 
	 * @param date the date of milk record to be inserted
	 * @param weight the weight of milk record to be inserted
	 * 
	 * @return true if insertion operation is done successfully, and false otherwise
	 */
	public boolean insertMilkForDate(String date, int weight) {
		if (date == null || weight < 0 || this.milkData.containsKey(date)) {
			return false;
		} 
		
		this.milkData.put(date, weight);
		return true;
	}
	
	/**
	 * edit a piece of milk record from the farm record
	 * 
	 * @param date the date of milk record to be edited 
	 * @param weight the weight of milk record to be edited 
	 * 
	 * @return true if edit operation is done successfully, and false otherwise
	 */
	public boolean editMilkForDate(String date, int weight) {
		if (date == null || weight < 0 || !this.milkData.containsKey(date)) {
			return false;
		}
		
		this.milkData.replace(date, weight);
		return true;
	}
	
	/**
	 * remove a piece of milk record specified by the date from farm record
	 * 
	 * @param date the date of milk record to be removed 
	 * 
	 * @return the weight previously associated with the date of removed record, if the date 
	 * cannot be found in Farm record, return -1 
	 */
	public int removeMilkForDate(String date) {
		if (date == null || !this.milkData.containsKey(date)) {
			return -1;
		}

		return this.milkData.remove(date);
	}
	
	/**
	 * Clear all the data associated with the farm
	 * 
	 * @return the removed farm record
	 */
	public Map<String, Integer> clearData() {
		Map<String,Integer> tempMap = this.milkData;
		this.milkData.clear();
		return tempMap;
	}
	

	public void setFarmID(String farmID) {
		this.farmID = farmID;
	}

	public String getFarmID() {
		return this.farmID;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getOwner() {
		return this.owner;
	}
	
	public Map<String, Integer> getMilkData() {
		return this.milkData;
	}
}
