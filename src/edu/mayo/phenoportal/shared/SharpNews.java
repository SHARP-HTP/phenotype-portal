package edu.mayo.phenoportal.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class SharpNews implements IsSerializable {

    private String i_id;
    private String i_information;

    public String getId() {
        return i_id;
    }

    public String getInformation() {
        return i_information;
    }

    public void setId(String id) {
        i_id = id;
    }

    public void setInformation(String information) {
        i_information = information;
    }

}
