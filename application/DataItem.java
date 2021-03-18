package application;

public class DataItem implements Comparable<DataItem> {
	private String dataString;

	public DataItem(String dataString) {
		this.dataString = dataString;
	}

	@Override
	public int compareTo(DataItem dataItem2) {
		Integer date1 = getDate();
		Integer date2 = dataItem2.getDate();
		return date1.compareTo(date2);
	}
	
	// check if the data item is correct
	public boolean isCorrect() {
		String[] readDataArray = this.dataString.trim().split(",");
		if (readDataArray.length != 3) {
			return false;
		}

		// check if the date format is correct
		String[] dateArray = readDataArray[0].trim().split("-");
		if (dateArray.length != 3 || !isInteger(dateArray[0]) || !isInteger(dateArray[1])
				|| !isInteger(dateArray[2])) {
			return false;
		}
		
		// check if the date is a valid date
		int year = Integer.parseInt(dateArray[0]);
		int month = Integer.parseInt(dateArray[1]);
		int day = Integer.parseInt(dateArray[2]);
		if (year < 0 || year > 2020 || month < 0 || month > 12 || day < 0 || day > 31) {
			return false;
		}
		
		// check especially for February
		if (month == 2 && day > 29) {
			return false;
		}

		// check if weight is correct
		if (!isInteger(readDataArray[2])) {
			return false;
		}

		return true;
	}
	
	public boolean inDateRange(Integer year1, Integer month1, Integer day1, Integer year2, Integer month2, Integer day2) {
		Integer date = getDate();
		Integer date1 = year1 * 10000 + month1 * 100 + day1 ;
		Integer date2 = year2 * 10000 + month2 * 100 + day2;
		if (date >= date1 && date <= date2) {
			return true;
		} else {
			return false;
		}
	}
	
	// get date
	public Integer getDate() {
		String[] readDataArray = this.dataString.trim().split(",");
		String[] dateArray = readDataArray[0].trim().split("-");
		Integer date = Integer.parseInt(dateArray[0]) * 10000 + Integer.parseInt(dateArray[1]) * 100 + Integer.parseInt(dateArray[2]);
		return date;
	}
	
	public String getDateString() {
		String[] readDataArray = this.dataString.trim().split(",");
		String[] dateArray = readDataArray[0].trim().split("-");
		
		Integer year = Integer.parseInt(dateArray[0]);
		Integer month = Integer.parseInt(dateArray[1]);
		Integer day = Integer.parseInt(dateArray[2]);
		
		String dateString = year.toString() + "-" + month.toString() + "-" + day.toString();
		return dateString;
	}
	
	
	public Integer getYear() {
		String[] readDataArray = this.dataString.trim().split(",");
		String[] dateArray = readDataArray[0].trim().split("-");
		return Integer.parseInt(dateArray[0]);
	}
	
	public Integer getMonth() {
		String[] readDataArray = this.dataString.trim().split(",");
		String[] dateArray = readDataArray[0].trim().split("-");
		return Integer.parseInt(dateArray[1]);
	}
	
	public Integer getDay() {
		String[] readDataArray = this.dataString.trim().split(",");
		String[] dateArray = readDataArray[0].trim().split("-");
		return Integer.parseInt(dateArray[2]);
	}
	
	public String getFarmIDString() {
		String[] readDataArray = this.dataString.trim().split(",");
		return readDataArray[1];
	}
	
	public Integer getWeight() {
		String[] readDataArray = this.dataString.trim().split(",");
		return Integer.parseInt(readDataArray[2].trim());
	}
	
	
	public String getDataString() {
		return this.dataString;
	}
	
	public String getDataNumberDateFormatString() {
		String[] readDataArray = this.dataString.trim().split(","); 
		return getDate().toString() + "," + readDataArray[1] + "," + readDataArray[2];
	}
	

	/**
	 * helper method to check if a String is an integer
	 * 
	 * @param input the String to be checked
	 * 
	 * @return true if it is a integer, and false otherwise
	 */
	private boolean isInteger(String input) {
		try {
			Integer.parseInt(input);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}

