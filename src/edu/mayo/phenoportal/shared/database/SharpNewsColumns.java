package edu.mayo.phenoportal.shared.database;

public enum SharpNewsColumns {
    ID(1, "id", "ID"), 
    INFO(2, "information", "Information");

    private final int colNum;
    private final String colName;
    private final String name;

    SharpNewsColumns(int colNum, String colName, String name) {
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
