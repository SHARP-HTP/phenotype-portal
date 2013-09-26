package edu.mayo.phenoportal.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class UserRoleRequest implements IsSerializable {

    private String id;
    private String userName;
    private String requestDate;
    private String responseDate;
    private boolean requestGranted;

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName
     *            the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the requestDate
     */
    public String getRequestDate() {
        return requestDate;
    }

    /**
     * @param requestDate
     *            the requestDate to set
     */
    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }

    /**
     * @return the responseDate
     */
    public String getResponseDate() {
        return responseDate;
    }

    /**
     * @param responseDate
     *            the responseDate to set
     */
    public void setResponseDate(String responseDate) {
        this.responseDate = responseDate;
    }

    /**
     * @return the requestGranted
     */
    public boolean isRequestGranted() {
        return requestGranted;
    }

    /**
     * @param requestGranted
     *            the requestGranted to set
     */
    public void setRequestGranted(boolean requestGranted) {
        this.requestGranted = requestGranted;
    }

}
