package edu.mayo.phenoportal.shared.database;

public enum UserRoleRequestColumns {
    ID(1, "id", "ID"), USERNAME(2, "username", "User Name"), REQUESTDATE(3, "requestdate",
            "Request Date"), RESPONSEDATE(4, "responsedate", "Response Date"), REQUESTGRANTED(5,
            "requestgranted", "Request Granted");

    // column position as found in the database table
    private final int colNum;
    // column name as found in the database table
    private final String colName;
    // display name for the information
    private final String name;

    UserRoleRequestColumns(int colNum, String colName, String name) {
        this.colNum = colNum;
        this.colName = colName;
        this.name = name;
    }

    /**
     * @return the colNum
     */
    public int colNum() {
        return colNum;
    }

    /**
     * @return the colName
     */
    public String colName() {
        return colName;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

}
