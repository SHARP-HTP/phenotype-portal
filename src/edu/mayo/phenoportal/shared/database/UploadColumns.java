package edu.mayo.phenoportal.shared.database;

public enum UploadColumns {
    ID(1, "parentID", "Category"), 
    NAME(2, "name", "Algorithm Name"), 
    VERSION(3, "version", "Version"), 
    USER(4, "user", "User"), 
    DESCRIPTION(5, "description", "Description"), 
    INSTITUTION(6,"institution", "Institution"), 
    CREATEDATE(7, "creationDate", "Creation Date"), 
    COMMENT(8, "comments", "Comments"), 
    STATUS(9, "status", "Status"), 
    XML_FILE(10, "xmlFile", "XML File"), 
    XLS_FILE(11, "xlsFile", "XLS File"), 
    HTML_FILE(12, "htmlFile", "HTML File"), 
    ZIP_FILE(13, "zipFile", "Zip File"), 
    WORD_FILE(14, "wordFile", "Word File"), 
    ASSOC_LINK(15,"assocLink", "Associated Link"), 
    ASSOC_NAME(16, "assocName", "Associated Name"), 
    UPLOAD_DATE(17, "uploadDate", "Upload Date");

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
