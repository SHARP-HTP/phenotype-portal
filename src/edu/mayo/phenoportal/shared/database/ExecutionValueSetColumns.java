package edu.mayo.phenoportal.shared.database;

public enum ExecutionValueSetColumns {

	EXECUTION_ID("executionId", "Execution Id"),
	VALUE_SET("valueSet", "Value Set"),
	VERSION("version", "Version");

	private final String colName;
	private final String name;

	ExecutionValueSetColumns(String colName, String name) {
		this.colName = colName;
		this.name = name;
	}

	public String getColumnName() {
		return colName;
	}

	public String getDisplayName() {
		return name;
	}
}
