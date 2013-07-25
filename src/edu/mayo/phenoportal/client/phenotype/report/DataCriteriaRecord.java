package edu.mayo.phenoportal.client.phenotype.report;

import com.smartgwt.client.widgets.grid.ListGridRecord;

public class DataCriteriaRecord extends ListGridRecord {

    public DataCriteriaRecord() {
        super();

    }

    public DataCriteriaRecord(String oid, String description, String version) {
        super();

        setOid(oid);
        setDescription(description);
        setVersion(version);
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

    public void setVersion(String version) {
        setAttribute("version", version);
    }

    public String getVersion() {
        return getAttributeAsString("version");
    }
}
