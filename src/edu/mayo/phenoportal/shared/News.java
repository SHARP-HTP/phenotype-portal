package edu.mayo.phenoportal.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Class to encapsulate a news item.
 */
public class News implements IsSerializable {

    private String i_id;
    private String i_date;
    private String i_information;

    public String getId() {
        return i_id;
    }

    public String getDate() {
        return i_date;
    }

    public String getInformation() {
        return i_information;
    }

    public void setId(String id) {
        i_id = id;
    }

    public void setDate(String date) {
        i_date = date;
    }

    public void setInformation(String information) {
        i_information = information;
    }

}
