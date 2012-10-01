package edu.mayo.phenoportal.shared.database;

public enum ExecutionColumns {
	ID(1, "id", "Id"),
    USER_NAME (2, "user", "User Name"),
	ALG_NAME(3, "algorithmName", "Algorithm Name"),
	VERSION(4, "version", "Version"),
    CATEGORY_NUM(5, "categoryNum", "Category Number"),
    CATEGORY(6, "categoryId", "Category"),
    START_DATE(7, "startDate", "Start Date"),
    END_DATE(8, "endDate", "End Date"),
    STATUS(9, "status", "Status"),
    ELAPSED_TIME(10, "elapsedTime", "Elapsed Time"),
	DATE_RANGE_FROM(11, "dateRangeFrom", "Date Range From"),
	DATE_RANGE_TO(12, "dateRangeTo", "Date Range To"),
	XML_PATH(13, "xmlPath", "XML Path"),
	IMAGE_PATH(14, "imagePath", "Image Path"),
	BPMN_PATH(15, "bpmnPath", "Bpmn Path"),
	RULES_PATH(16, "rulesPath", "Rules Path");

	private final int colNum;
	private final String colName;
	private final String name;
	    
	ExecutionColumns(int colNum, String colName, String name) { 
		this.colNum = colNum; 
		this.colName = colName; 
		this.name = name; 
	}

    public int colNum() { 
    	return colNum; 
    }
    
    public String colName() { 
    	return colName; 
    }
    
    public String normName() { 
    	return name; 
    }
}
