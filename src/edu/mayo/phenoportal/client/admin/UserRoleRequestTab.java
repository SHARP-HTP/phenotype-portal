package edu.mayo.phenoportal.client.admin;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;

import edu.mayo.phenoportal.client.phenotype.PhenotypeService;
import edu.mayo.phenoportal.client.phenotype.PhenotypeServiceAsync;
import edu.mayo.phenoportal.client.utils.MessageWindow;
import edu.mayo.phenoportal.shared.User;
import edu.mayo.phenoportal.shared.UserRoleRequest;

public class UserRoleRequestTab extends Tab implements AdminTab {

    private final UserRoleRequestListGrid i_userRequestListGrid;
    private final UserRoleRequestForm i_userRequestForm;
    private final HLayout i_buttonLayout;
    private final IButton i_buttonGrant;
    private final IButton i_buttonDeny;

    public UserRoleRequestTab(String title) {
        super(title);

        VLayout mainLayout = new VLayout();
        mainLayout.setWidth100();
        mainLayout.setHeight100();
        mainLayout.setMembersMargin(20);

        i_userRequestListGrid = new UserRoleRequestListGrid();
        // setPane(i_userRequestListGrid);
        mainLayout.addMember(i_userRequestListGrid);

        i_userRequestListGrid.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                ListGridRecord selectedRecord = i_userRequestListGrid.getSelectedRecord();

                // only show the form to grant/deny if the admin hasn't
                // responsed yet.
                if (selectedRecord.getAttribute("RequestGranted").equals("")) {
                    UserRoleRequest userRoleRequest = getUserRoleRequestObject(selectedRecord);
                    getUserData(userRoleRequest);
                } else {
                    setFormVisibility(false);
                }
            }
        });

        // create the form to grant/deny the upgrade request
        i_userRequestForm = new UserRoleRequestForm();
        mainLayout.addMember(i_userRequestForm);

        // Add the buttons
        i_buttonLayout = new HLayout();
        i_buttonLayout.setWidth100();
        i_buttonLayout.setHeight(40);
        i_buttonLayout.setAlign(Alignment.CENTER);
        i_buttonLayout.setMembersMargin(15);

        i_buttonGrant = new IButton("Grant");
        i_buttonGrant.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                updateResponse(1);

                String title = "User Role Updated";
                String message = "An email has been sent to the user to let them know that their request was granted.";
                MessageWindow messageWindow = new MessageWindow(title, message);
                messageWindow.show();
            }
        });

        i_buttonDeny = new IButton("Deny");
        i_buttonDeny.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                updateResponse(0);

                String title = "User Role Updated";
                String message = "An email has been sent to the user to let them know that their request was denied.";
                MessageWindow messageWindow = new MessageWindow(title, message);
                messageWindow.show();
            }
        });

        i_buttonLayout.addMember(i_buttonGrant);
        i_buttonLayout.addMember(i_buttonDeny);
        mainLayout.addMember(i_buttonLayout);

        setPane(mainLayout);
        setFormVisibility(false);
    }

    @Override
    public String getTabDescription() {
        return "Displays a list of registered users that have requested that their role be increased to \"Execute\".  Clicking on a row in the table will display a form that will allow you to grant or deny the request.  "
                + "When clicking on one the buttons, it will send an email to the user indicating they there request was granted or denied.";
    }

    @Override
    public void updateContents() {

        setFormVisibility(false);

        // invalidate the cache to force the datasource to fetch the data again.
        i_userRequestListGrid.invalidateCache();
        i_userRequestListGrid.getDataSource().setTestData(null);
        i_userRequestListGrid.getDataSource().invalidateCache();
    }

    private void setFormVisibility(boolean visible) {

        if (visible) {
            i_userRequestForm.show();
            i_buttonLayout.show();
        } else {
            i_userRequestForm.hide();
            i_buttonLayout.hide();
        }
    }

    private UserRoleRequest getUserRoleRequestObject(ListGridRecord record) {

        UserRoleRequest urr = new UserRoleRequest();
        urr.setId(record.getAttribute("Id"));
        urr.setUserName(record.getAttribute("UserName"));
        urr.setRequestDate(record.getAttribute("RequestDate"));
        urr.setResponseDate(record.getAttribute("ResponseDate"));

        boolean granted = false;

        if (record.getAttribute("RequestGranted") != null
                && record.getAttribute("RequestGranted").length() > 0) {

            int grantedInt = 0;
            try {
                grantedInt = Integer.parseInt(record.getAttribute("RequestGranted"));
            } catch (NumberFormatException nfe) {
            }

            granted = grantedInt == 0 ? false : true;
            urr.setRequestGranted(granted);
        }

        return urr;
    }

    /**
     * Get the User object from the server, then set the form data
     * 
     * @param userRoleRequest
     */
    private void getUserData(final UserRoleRequest userRoleRequest) {

        PhenotypeServiceAsync service = GWT.create(PhenotypeService.class);
        service.getUser(userRoleRequest.getUserName(), new AsyncCallback<User>() {

            @Override
            public void onSuccess(User user) {
                i_userRequestForm.setData(user, userRoleRequest);
                setFormVisibility(true);
            }

            @Override
            public void onFailure(Throwable caught) {
                System.out.println("Failed to get Users - " + caught);
            }
        });
    }

    /**
     * Update the record with the admins grant/deny decision. This update will
     * prompt the UserRoleRequestXmlDS to fire an update request.
     * 
     * @param response
     */
    protected void updateResponse(int response) {
        ListGridRecord record = i_userRequestListGrid.getSelectedRecord();

        long requestDate = System.currentTimeMillis();
        String requestDateStr = new Date(requestDate).toString();

        record.setAttribute("RequestGranted", response + "");
        record.setAttribute("ResponseDate", requestDateStr);

        i_userRequestListGrid.updateData(record);
        setFormVisibility(false);
    }
}
