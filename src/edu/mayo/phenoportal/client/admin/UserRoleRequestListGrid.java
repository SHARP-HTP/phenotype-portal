package edu.mayo.phenoportal.client.admin;

import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;

import edu.mayo.phenoportal.client.datasource.UserRoleRequestXmlDS;

/**
 * ListGrid to show the requests the users have made to increase their role and
 * if they were granted/denied.
 */
public class UserRoleRequestListGrid extends ListGrid {

    private final UserRoleRequestXmlDS i_userRoleRequestDS;

    public UserRoleRequestListGrid() {
        super();

        i_userRoleRequestDS = UserRoleRequestXmlDS.getInstance();

        setWidth100();
        setHeight(200);
        setShowAllRecords(true);
        setDataSource(i_userRoleRequestDS);

        setAutoFetchData(true);

        ListGridField userNameField = new ListGridField("UserName", "User Name");
        ListGridField requestDateField = new ListGridField("RequestDate", "Request Date");
        ListGridField responseDateField = new ListGridField("ResponseDate", "Response Date");
        ListGridField requestGrantedField = new ListGridField("RequestGranted", "Request Granted");

        setFields(userNameField, requestDateField, requestGrantedField, responseDateField);
    }

}
