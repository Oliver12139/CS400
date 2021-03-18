package application;

/**
 * CheeseFactory.java created by Vivian Ye on MacBook Pro in ATeamProject Apr 23, 2020
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
 * Defines a factory that gets its supplies from multiple farms each year
 * 
 * @author Vivian Ye
 *
 */
public class CheeseFactory {
	private String name;
	private ArrayList<Farm> milkDataFromFarms;
	
	public CheeseFactory() {
		this.name = "";
		this.milkDataFromFarms = new ArrayList<>();
	}

	public CheeseFactory(String name) {
		this.name = name;
		this.milkDataFromFarms = new ArrayList<>();
	}

	public CheeseFactory(String name, List<Farm> milkDataFromFarms) {
		this.name = name;
		this.milkDataFromFarms = new ArrayList<>();
		for (Farm newFarm : milkDataFromFarms) {
			this.milkDataFromFarms.add(newFarm);
		}

	}

	/**
	 * insert a single piece of milk record in the cheese factory
	 * 
	 * @param farmID the farm ID of the milk record to be added
	 * @param date   the date of milk record
	 * @param weight the weight of milk record
	 * 
	 * @return true if the milk record has been correctly inserted, and false
	 *         otherwise
	 */
	public boolean insertSingleData(String farmID, String date, int weight) {
		if (farmID == null || date == null || weight < 0) {
			return false;
		}

		if (!containsFarm(farmID)) {
			Farm newFarm = new Farm(farmID);
			newFarm.insertMilkForDate(date, weight);
			this.milkDataFromFarms.add(newFarm);
			return true;
		} else {
			return this.milkDataFromFarms.get(indexOfFarm(farmID)).insertMilkForDate(date, weight);
		}
	}

	/**
	 * helper method for finding if the farm record is in the cheese factory record
	 * 
	 * @param farmID the ID of farm to be found
	 * 
	 * @return true if the farm record is found, and false otherwise
	 */
	private boolean containsFarm(String farmID) {
		for (int i = 0; i < this.milkDataFromFarms.size(); i++) {
			if (this.milkDataFromFarms.get(i).getFarmID().equals(farmID)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * helper method for getting the index of Farm
	 * 
	 * @param farmID the ID of farm to get the index
	 * 
	 * @return index the index of farm to be found
	 */
	private int indexOfFarm(String farmID) {
		for (int i = 0; i < this.milkDataFromFarms.size(); i++) {
			if (this.milkDataFromFarms.get(i).getFarmID().equals(farmID)) {
				return i;
			}
		}

		return -1;
	}

	/**
	 * edit a single piece of milk record from cheese factory
	 * 
	 * @param farmID the farm ID of mild record to be modified
	 * @param date   the date of milk record to be modified
	 * @param weight the weight of milk record to be modified
	 * 
	 * @return true if the milk recored has been correctly modified, and false
	 *         otherwise
	 */
	public boolean editSingleData(String farmID, String date, int weight) {
		if (farmID == null || date == null || weight < 0 || !containsFarm(farmID)) {
			return false;
		}

		return this.milkDataFromFarms.get(indexOfFarm(farmID)).editMilkForDate(date, weight);
	}

	/**
	 * remove milk record of the specified date and farm ID
	 * 
	 * @param farmID the specified farmID of the milk record to be removed
	 * @param date   the specified date of the milk record to be removed
	 * 
	 * @return true if removing has been done correctly, and false otherwise
	 */
	public boolean removeSingleData(String farmID, String date) {
		if (farmID == null || date == null || !containsFarm(farmID)) {
			return false;
		}

		if (this.milkDataFromFarms.get(indexOfFarm(farmID)).removeMilkForDate(date) == -1) {
			return false;
		}
		
		return true;
	}

	/**
	 * remove farm record specified by the farmID
	 * 
	 * @param farmID ID of the farm to remove record
	 * 
	 * @return true if removing has been done correctly, and false otherwise
	 */
	public boolean removeFarmData(String farmID) {
		if (farmID == null || !containsFarm(farmID)) {
			return false;
		}

		this.milkDataFromFarms.remove(indexOfFarm(farmID));
		return true;
	}

	/**
	 * remove all milk records specified by the date
	 * 
	 * @param date of milk records to be removed
	 * 
	 * @return true if removing has been done correctly, and false otherwise
	 */
	public boolean removeDateData(String date) {
		if (date == null) {
			return false;
		}

		// TODO if the farm has only one record, remove it as well as the farmID

		return true;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Farm> getMilkDataFromFarms() {
		return this.milkDataFromFarms;
	}

	public ArrayList<String> getMilkDataFromFarmsInStringList() {
		ArrayList<DataItem> dataList = getMilkDataFromFarmsInDataItemList();
		ArrayList<String> dataFromFarmsInStringList = new ArrayList<>();

		for (DataItem dataItem : dataList) {
			dataFromFarmsInStringList.add(dataItem.getDataString());
		}

		return dataFromFarmsInStringList;
	}

	public ArrayList<DataItem> getMilkDataFromFarmsInDataItemList() {
		ArrayList<DataItem> dataList = new ArrayList<>();

		for (Farm farm : this.milkDataFromFarms) {
			HashMap<String, Integer> milkData = (HashMap<String, Integer>) farm.getMilkData();
			if (!milkData.isEmpty()) {
				for (Map.Entry<String, Integer> entry : milkData.entrySet()) {
					String dataString = entry.getKey() + "," + farm.getFarmID() + "," + entry.getValue();
					dataList.add(new DataItem(dataString));
				}
			}
		}

		// sort the data item in increasing order of date
		Collections.sort(dataList);

		return dataList;
	}
	
	public void clearData() {
		this.milkDataFromFarms = new ArrayList<>();
	}
	
	public ArrayList<String> getFarmIDList() {
		ArrayList<String> farmIDList = new ArrayList<>();
		for (Farm farm : this.milkDataFromFarms) {
			farmIDList.add(farm.getFarmID());
		}
		return farmIDList;
	}

}
