package edu.mayo.phenoportal.shared;

import java.io.Serializable;

public class User implements Serializable {
    // static final Logger logger = Logger.getLogger(User.class.getName());

    private static final long serialVersionUID = 1L;

    private String fname;
    private String lname;
    private String userName;
    private String email;
    private String pass;
    private int role;
    private int enable;
    private String registrationDate;

    private String countryRegion;
    private String address;
    private String city;
    private String state;
    private String zipCode;
    private String phoneNumber;

    private String sessionId;

    public String getFirstName() {
        return fname;
    }

    public String getLastName() {
        return lname;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return pass;
    }

    public int getRole() {
        return role;
    }

    public int getEnable() {
        return enable;
    }

    public String getCountryRegion() {
        return countryRegion;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setFirstName(String fname) {
        this.fname = fname;
    }

    public void setLastName(String lname) {
        this.lname = lname;
    }

    public void setUserName(String user) {
        this.userName = user;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String pass) {
        this.pass = pass;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public void setEnable(int enable) {
        this.enable = enable;
    }

    public void setCountryRegion(String countryRegion) {
        this.countryRegion = countryRegion;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public void setSessionId(String id) {
        this.sessionId = id;
    }

}
