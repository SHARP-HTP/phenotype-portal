package edu.mayo.phenoportal.client.admin;

import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;

import edu.mayo.phenoportal.client.datasource.UsersXmlDS;

public class UserListGrid extends ListGrid {

    private final UsersXmlDS i_usersDS;

    public UserListGrid() {
        super();
        i_usersDS = UsersXmlDS.getInstance();

        setWidth100();
        setHeight100();
        setShowAllRecords(true);
        setDataSource(i_usersDS);

        // This will show the delete icon.
        setCanRemoveRecords(true);
        setWarnOnRemoval(true);
        setWarnOnRemovalMessage("Are you sure you want to remove this user?  This cannot be undone.");
        setAnimateRemoveRecord(true);

        ListGridField userIdField = new ListGridField("UserId", "User Id");
        // Don't allow the userId to be edited.
        userIdField.setCanEdit(false);

        ListGridField firstNameField = new ListGridField("FirstName", "First Name");
        ListGridField lastNameField = new ListGridField("LastName", "Last Name");
        ListGridField emailField = new ListGridField("Email", "Email");
        ListGridField roleField = new ListGridField("Role", "Role");
        ListGridField enabledField = new ListGridField("Enabled", "Enabled");
        ListGridField registrationDateField = new ListGridField("RegistrationDate",
                "Registration Date");

        ListGridField countryField = new ListGridField("countryOrRegion", "Country/Region");
        ListGridField streetField = new ListGridField("Street", "Street");
        ListGridField cityField = new ListGridField("City", "City");
        ListGridField stateField = new ListGridField("State", "State");
        ListGridField zipField = new ListGridField("Zip", "Zip Code");
        ListGridField phoneField = new ListGridField("Phone", "Phone");

        setFields(userIdField, firstNameField, lastNameField, emailField, roleField,
                registrationDateField, enabledField, countryField, streetField, cityField,
                stateField, zipField, phoneField);

        setAutoFetchData(true);
        setCanEdit(true);
        setEditEvent(ListGridEditEvent.DOUBLECLICK);
        setModalEditing(true);
    }

}
