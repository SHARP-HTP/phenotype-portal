package edu.mayo.phenoportal.shared;

import java.io.Serializable;

public class SharpNews implements Serializable {

    private static final long serialVersionUID = 1L;

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
