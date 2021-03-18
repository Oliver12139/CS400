package application;

/**
 * DataManager.java created by Vivian Ye on MacBook Pro in ATeamProject Apr 23, 2020
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
 * @author Vivian Ye
 *
 */
public class DataManager {

	public ArrayList<Integer> getAllYears(CheeseFactory factory) {
		ArrayList<DataItem> milkDataFromFarmsInDataItemList = factory.getMilkDataFromFarmsInDataItemList();
		ArrayList<Integer> allYears = new ArrayList<>();

		for (DataItem dataItem : milkDataFromFarmsInDataItemList) {
			if (!allYears.contains(dataItem.getYear())) {
				allYears.add(dataItem.getYear());
			}
		}

		return allYears;
	}

	public ArrayList<String> getFarmReport(String farmID, Integer year, CheeseFactory factory) {
		ArrayList<String> farmReportInStringList = new ArrayList<>();
		ArrayList<DataItem> milkDataFromFarmsInDataItemList = factory.getMilkDataFromFarmsInDataItemList();

		for (Integer month = 1; month < 13; month++) {
			Integer totalWeightAllFarms = 0;
			Integer totalWeightFarm = 0;
			ArrayList<DataItem> getDataFromFarmsInDataItemListForMonth = new ArrayList<>();

			for (DataItem dataItem : milkDataFromFarmsInDataItemList) {
				if (dataItem.getYear().equals(year) && dataItem.getMonth().equals(month)) {
					getDataFromFarmsInDataItemListForMonth.add(dataItem);
					totalWeightAllFarms += dataItem.getWeight();
					if (dataItem.getFarmIDString().equals(farmID)) {
						totalWeightFarm += dataItem.getWeight();
					}
				}
			}

			Double percentOfTotalWeight = (double) totalWeightFarm / (double) totalWeightAllFarms * 100.0;

			System.out.println("farmID " + farmID + " farmWeight " + totalWeightFarm);
			System.out.println(
					"month" + month + " totalWeight " + totalWeightAllFarms + " percent " + percentOfTotalWeight);
			farmReportInStringList.add(month.toString() + "," + totalWeightFarm.toString() + ","
					+ percentOfTotalWeight.toString() + "%");
		}
		return farmReportInStringList;
	}

	public ArrayList<String> getAnnualReport(Integer year, CheeseFactory factory) {
		ArrayList<String> annualReport = new ArrayList<>();
		ArrayList<DataItem> milkDataFromFarmsInDataItemList = factory.getMilkDataFromFarmsInDataItemList();
		HashMap<String, Integer> annualReportForFarms = new HashMap<>();
		ArrayList<String> farmIDList = factory.getFarmIDList();
		Integer totalWeightAllFarms = 0;

		for (String farmID : farmIDList) {
			annualReportForFarms.put(farmID, 0);
		}

		for (DataItem dataItem : milkDataFromFarmsInDataItemList) {
			if (dataItem.getYear().equals(year)) {
				String farmID = dataItem.getFarmIDString();
				Integer prevTotalWeightFarm = annualReportForFarms.get(farmID);
				annualReportForFarms.replace(farmID, (prevTotalWeightFarm + dataItem.getWeight()));
			}
		}

		for (Map.Entry<String, Integer> entry : annualReportForFarms.entrySet()) {
			totalWeightAllFarms += entry.getValue();
		}

		for (Map.Entry<String, Integer> entry : annualReportForFarms.entrySet()) {
			Integer totalWeightFarm = entry.getValue();
			Double percentOfTotalWeight = (double) totalWeightFarm / (double) totalWeightAllFarms * 100.0;
			annualReport.add(entry.getKey() + "," + totalWeightFarm + "," + percentOfTotalWeight.toString() + "%");
		}

		return annualReport;
	}

	public ArrayList<String> getMonthlyReport(Integer year, Integer month, CheeseFactory factory) {
		ArrayList<String> monthlyReport = new ArrayList<>();
		ArrayList<DataItem> milkDataFromFarmsInDataItemList = factory.getMilkDataFromFarmsInDataItemList();
		HashMap<String, Integer> monthlyReportForFarms = new HashMap<>();
		ArrayList<String> farmIDList = factory.getFarmIDList();
		Integer totalWeightAllFarms = 0;

		for (String farmID : farmIDList) {
			monthlyReportForFarms.put(farmID, 0);
		}

		for (DataItem dataItem : milkDataFromFarmsInDataItemList) {
			if (dataItem.getYear().equals(year) && dataItem.getMonth().equals(month)) {
				String farmID = dataItem.getFarmIDString();
				Integer prevTotalWeightFarm = monthlyReportForFarms.get(farmID);
				monthlyReportForFarms.replace(farmID, (prevTotalWeightFarm + dataItem.getWeight()));
			}
		}

		for (Map.Entry<String, Integer> entry : monthlyReportForFarms.entrySet()) {
			totalWeightAllFarms += entry.getValue();
		}

		for (Map.Entry<String, Integer> entry : monthlyReportForFarms.entrySet()) {
			Integer totalWeightFarm = entry.getValue();
			Double percentOfTotalWeight = (double) totalWeightFarm / (double) totalWeightAllFarms * 100.0;
			monthlyReport.add(entry.getKey() + "," + totalWeightFarm + "," + percentOfTotalWeight.toString() + "%");
		}

		return monthlyReport;
	}

	public ArrayList<String> getDateRangeReport(Integer year1, Integer month1, Integer day1, Integer year2,
			Integer month2, Integer day2, CheeseFactory factory) {
		ArrayList<String> dateRangeReport = new ArrayList<>();
		ArrayList<DataItem> milkDataFromFarmsInDataItemList = factory.getMilkDataFromFarmsInDataItemList();
		HashMap<String, Integer> monthlyReportForFarms = new HashMap<>();
		ArrayList<String> farmIDList = factory.getFarmIDList();
		Integer totalWeightAllFarms = 0;

		for (String farmID : farmIDList) {
			monthlyReportForFarms.put(farmID, 0);
		}

		for (DataItem dataItem : milkDataFromFarmsInDataItemList) {
			if (dataItem.inDateRange(year1, month1, day1, year2, month2, day2)) {
				String farmID = dataItem.getFarmIDString();
				Integer prevTotalWeightFarm = monthlyReportForFarms.get(farmID);
				monthlyReportForFarms.replace(farmID, (prevTotalWeightFarm + dataItem.getWeight()));
			}
		}

		for (Map.Entry<String, Integer> entry : monthlyReportForFarms.entrySet()) {
			totalWeightAllFarms += entry.getValue();
		}

		for (Map.Entry<String, Integer> entry : monthlyReportForFarms.entrySet()) {
			Integer totalWeightFarm = entry.getValue();
			Double percentOfTotalWeight = (double) totalWeightFarm / (double) totalWeightAllFarms * 100.0;
			dateRangeReport.add(entry.getKey() + "," + totalWeightFarm + "," + percentOfTotalWeight.toString() + "%");
		}

		return dateRangeReport;
	}

}
