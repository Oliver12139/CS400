/**
 * FileManager.java created by mr.zeta0 on MacBook Pro in ATeamProject Apr 19, 2020
 *
 * Author:	Jinhan Zhu, Xunwei Ye (jzhu382@wisc.edu, xye53@wisc.edu)
 * Date:	April 23, 2020
 * 
 * Course:	CS400
 * Semester:	Spring 2020
 * Lecture:	001
 * 
 * IDE:		Eclipse IDE for Java Developers
 * Version: 	2019-06 (4.12.0)
 * Build id:	20190614-1200
 * 
 * Device:	ZHU-MACBOOKPRO
 * OS:		macOS 
 * Version: 	10.14.6
 * OS Build:	N/A
 *
 * List Collaborators: NONE
 *
 * Other Credits: NONE
 *
 * Known Bugs: NONE
 */
package application;

import java.io.*;
import java.util.*;

/**
 * FileManager - Defines required operations to read/write input/output files
 * 
 * @author mr.zeta0, Vivian Ye
 * 
 */
public class FileManager {

	String inputFile;
	String outputFile;

	public FileManager() {
		this.inputFile = "";
		this.outputFile = "";
	}

	public FileManager(String inputFile, String outputFile) {
		this.inputFile = inputFile;
		this.outputFile = outputFile;
	}

	public boolean readfile(String inputFile, CheeseFactory factory) {
		this.inputFile = inputFile;
		File file = new File(inputFile);

		BufferedReader bufferedReader = null;
		try {
			bufferedReader = new BufferedReader(new FileReader(file));
			String line = "";
			int count = 0;
			while ((line = bufferedReader.readLine()) != null) {
				if (count != 0) {
					DataItem dataItem = new DataItem(line);
					if (!dataItem.isCorrect()) {
						return false;
					}

					if (!factory.insertSingleData(dataItem.getFarmIDString(), dataItem.getDateString(),
							dataItem.getWeight())) {
						return false;
					}
				}
				count++;

			}

			System.out.println("Read Item" + count);
			System.out.println("In FileManager, factory has " + factory.getMilkDataFromFarms().size());
			return true;
		} catch (Exception e) {
			return false;
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (Exception e) {
					return false;
				}
			}
		}
	}

	/**
	 * Write cheese factory data back to a csv file
	 * 
	 * @param outputFile the file to be written
	 * @param factory    the data of which cheese factory to be written
	 * 
	 * @return true if data has been successfully written, and false otherwise
	 */
	public boolean writeDataToFile(String outputFile, CheeseFactory factory) {
		this.outputFile = outputFile;
		ArrayList<Farm> farmList = (ArrayList<Farm>) factory.getMilkDataFromFarms();
		ArrayList<DataItem> dataList = new ArrayList<>();
		FileWriter csvWriter = null;
		try {
			csvWriter = new FileWriter(outputFile);
			csvWriter.append("date");
			csvWriter.append(",");
			csvWriter.append("farm_id");
			csvWriter.append(",");
			csvWriter.append("weight");
			csvWriter.append("\n");

			for (int i = 0; i < farmList.size(); i++) {
				HashMap<String, Integer> milkData = (HashMap<String, Integer>) farmList.get(i).getMilkData();
				if (!milkData.isEmpty()) {
					for (Map.Entry<String, Integer> entry : milkData.entrySet()) {
						String dataString = entry.getKey() + "," + farmList.get(i).getFarmID() + "," + entry.getValue();
						dataList.add(new DataItem(dataString));
					}
				}
			}

			// sort the data item in increasing order of date
			Collections.sort(dataList);

			// write all data items to csv file
			for (DataItem dataItem : dataList) {
				csvWriter.append(dataItem.getDataString());
				csvWriter.append("\n");
			}
			return true;
		} catch (Exception e) {
			return false;
		} finally {
			if (csvWriter != null) {
				try {
					csvWriter.flush();
					csvWriter.close();
				} catch (Exception e) {
					return false;
				}
			}
		}
	}

	public boolean writeFarmReportToFile(String outputFile, String farmId, String year,
			ArrayList<String> farmReportStringList, CheeseFactory factory) {
		this.outputFile = outputFile;
		FileWriter csvWriter = null;
		try {
			csvWriter = new FileWriter(outputFile);
			csvWriter.append(farmId + "'s " + year + " farm report");
			csvWriter.append("\n\n");
			csvWriter.append("month");
			csvWriter.append(",");
			csvWriter.append("total");
			csvWriter.append(",");
			csvWriter.append("percent of total of all farms");
			csvWriter.append("\n");

			for (String dataString : farmReportStringList) {
				csvWriter.append(dataString);
				csvWriter.append("\n");
			}
			return true;
		} catch (Exception e) {
			return false;
		} finally {
			if (csvWriter != null) {
				try {
					csvWriter.flush();
					csvWriter.close();
				} catch (Exception e) {
					return false;
				}
			}
		}
	}

	public boolean writeAnnualReportToFile(String outputFile, String year, ArrayList<String> annualReportStringList,
			CheeseFactory factory) {
		this.outputFile = outputFile;
		FileWriter csvWriter = null;
		try {
			csvWriter = new FileWriter(outputFile);
			csvWriter.append(year + "'s annual report");
			csvWriter.append("\n\n");
			csvWriter.append("farm_id");
			csvWriter.append(",");
			csvWriter.append("total");
			csvWriter.append(",");
			csvWriter.append("percent of total of all farms");
			csvWriter.append("\n");

			for (String dataString : annualReportStringList) {
				csvWriter.append(dataString);
				csvWriter.append("\n");
			}
			return true;
		} catch (Exception e) {
			return false;
		} finally {
			if (csvWriter != null) {
				try {
					csvWriter.flush();
					csvWriter.close();
				} catch (Exception e) {
					return false;
				}
			}
		}
	}

	public boolean writeMonthlyReportToFile(String outputFile, String year, String month,
			ArrayList<String> monthlyReportStringList, CheeseFactory factory) {
		this.outputFile = outputFile;
		FileWriter csvWriter = null;
		try {
			csvWriter = new FileWriter(outputFile);
			csvWriter.append(year + "-" + month + "'s monthly report");
			csvWriter.append("\n\n");
			csvWriter.append("farm_id");
			csvWriter.append(",");
			csvWriter.append("total");
			csvWriter.append(",");
			csvWriter.append("percent of total of all farms");
			csvWriter.append("\n");

			for (String dataString : monthlyReportStringList) {
				csvWriter.append(dataString);
				csvWriter.append("\n");
			}
			return true;
		} catch (Exception e) {
			return false;
		} finally {
			if (csvWriter != null) {
				try {
					csvWriter.flush();
					csvWriter.close();
				} catch (Exception e) {
					return false;
				}
			}
		}
	}

	public boolean writeDateRangeReportToFile(String outputFile, String startDate, String endDate, ArrayList<String> dateRangeReportStringList,
			CheeseFactory factory) {
		this.outputFile = outputFile;
		FileWriter csvWriter = null;
		try {
			csvWriter = new FileWriter(outputFile);
			csvWriter.append(startDate + " to " + endDate + "'s date range report");
			csvWriter.append("\n\n");
			csvWriter.append("farm_id");
			csvWriter.append(",");
			csvWriter.append("total");
			csvWriter.append(",");
			csvWriter.append("percent of total of all farms");
			csvWriter.append("\n");

			for (String dataString : dateRangeReportStringList) {
				csvWriter.append(dataString);
				csvWriter.append("\n");
			}
			return true;
		} catch (Exception e) {
			return false;
		} finally {
			if (csvWriter != null) {
				try {
					csvWriter.flush();
					csvWriter.close();
				} catch (Exception e) {
					return false;
				}
			}
		}
	}

	public void setInputFile(String inputFile) {
		this.inputFile = inputFile;
	}

	public void setOutputFile(String outputFile) {
		this.outputFile = outputFile;
	}

	public String getInputFile() {
		return this.inputFile;
	}

	public String getOutputFile() {
		return this.outputFile;
	}

	public void setInputOutputFile(String inputFile, String outputFile) {
		this.inputFile = inputFile;
		this.outputFile = outputFile;
	}

//	public CheeseFactory getFactory() {
//		return this.factory;
//	}
}
