package edu.mayo.phenoportal.client.phenotype.report;

import com.smartgwt.client.widgets.grid.ListGridRecord;

public class DataCriteriaRecord extends ListGridRecord {

    public DataCriteriaRecord() {
        super();

    }

    public DataCriteriaRecord(String oid, String description) {
        super();

        setOid(oid);
        setDescription(description);

    }

    public void setOid(String oid) {
        setAttribute("oid", oid);
    }

    public String getOid() {
        return getAttributeAsString("oid");
    }

    public void setDescription(String description) {
        setAttribute("description", description);
    }

    public String getDescription() {
        return getAttributeAsString("description");
    }

}
