package edu.mayo.phenoportal.shared.database;

public enum UploadColumns {
    ID(1, "id", "id"),
    PARENT_ID(2, "parentID", "Category"),
    NAME(3, "name", "Algorithm Name"),
    VERSION(4, "version", "Version"),
    USER(5, "user", "User"),
    DESCRIPTION(6, "description", "Description"),
    INSTITUTION(7,"institution", "Institution"),
    CREATEDATE(8, "creationDate", "Creation Date"),
    COMMENT(9, "comments", "Comments"),
    STATUS(10, "status", "Status"),
    XML_FILE(11, "xmlFile", "XML File"),
    XLS_FILE(12, "xlsFile", "XLS File"),
    HTML_FILE(13, "htmlFile", "HTML File"),
    ZIP_FILE(14, "zipFile", "Zip File"),
    WORD_FILE(15, "wordFile", "Word File"),
    ASSOC_LINK(16,"assocLink", "Associated Link"),
    ASSOC_NAME(17, "assocName", "Associated Name"),
    UPLOAD_DATE(18, "uploadDate", "Upload Date");

    private final int colNum;
    private final String colName;
    private final String name;

    UploadColumns(int colNum, String colName, String name) {
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
