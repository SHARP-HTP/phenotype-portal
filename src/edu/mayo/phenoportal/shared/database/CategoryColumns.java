package edu.mayo.phenoportal.shared.database;

public enum CategoryColumns {
	ID (1, "id", "ID"),
	NAME(2, "name", "Name"),
	PARENTID(3, "parentid", "Parent ID"),
	COUNT(4, "count", "Count"),
	LEVEL(5, "level", "Level");
	
	// column position as found in the database table
	private final int colNum;
	// column name as found in the database table
	private final String colName;
	// display name for the information
	private final String name;

	CategoryColumns(int colNum, String colName, String name) {
		this.colNum = colNum;
		this.colName = colName;
		this.name = name;
	}

	/**
	 * @return the colNum
	 */
	public int getColNum() {
		return colNum;
	}

	/**
	 * @return the colName
	 */
	public String getColName() {
		return colName;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	
}
