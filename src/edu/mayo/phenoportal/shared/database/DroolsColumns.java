package edu.mayo.phenoportal.shared.database;

public enum DroolsColumns {

    ID(1, "id", "Id"),
	GUVNOR_ID(2, "guvnorId", "Guvnor Id"),
	PARENT_ID(3, "parentId", "Parent Id"),
	BPMN_PATH(4, "bpmnPath", "BPMN Path File"),
	IMAGE_PATH(5, "imagePath", "Image Path"),
	RULES_PATH(6, "rulesPath", "Rules Path"),
	TITLE(7, "title", "Title"),
	COMMENT(8, "comment", "Comment"),
	USERNAME(9, "username", "Username"),
	EDITABLE(10, "editable", "Editable"),
	CREATED_DATE(11, "createdDate", "Created Date"),
	CATEGORY_ID(12, "categoryId", "Category Id");

    private final int colNum;
    private final String colName;
    private final String name;

    DroolsColumns(int colNum, String colName, String name) {
        this.colNum = colNum;
        this.colName = colName;
        this.name = name;
    }

    public int getColNum() {
        return colNum;
    }

    public String getColName() {
        return colName;
    }

    public String getName() {
        return name;
    }

}
