package edu.mayo.phenoportal.shared.database;

public enum UserColumns {
    USER (1, "username", "User Name"), 
    FNAME(2, "fname", "First Name"), 
    LNAME(3, "lname", "Last Name"),
    EMAIL(4, "email", "eMail"), 
    PASS(5, "password", "Password"), 
    ROLE(6, "role", "Role"), 
    ENABLE(7, "enable", "Enable"),
    REGISTRATIONDATE(8, "registrationdate", "Registration Date"),
    COUNTRY_OR_REGION(9, "countryregion", "Country Or Region"),
    STREET_ADDRESS(10, "streetaddress", "Street Address"),
    CITY(11, "city", "City"),
    STATE(12, "state", "State"),
    ZIP(13, "zipcode", "Zip Code"),
    PHONE_NUMBER(14, "phonenumber", "Phone Number");
        
    // column position as found in the database table
    private final int colNum;
    // column name as found in the database table
    private final String colName;
    // display name for the information
    private final String name;
        
    UserColumns(int colNum, String colName, String name) { 
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
