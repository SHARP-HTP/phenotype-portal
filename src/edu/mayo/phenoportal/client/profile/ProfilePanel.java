package edu.mayo.phenoportal.client.profile;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.PasswordItem;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

import edu.mayo.phenoportal.client.Htp;
import edu.mayo.phenoportal.client.authentication.AuthenticationService;
import edu.mayo.phenoportal.client.authentication.AuthenticationServiceAsync;
import edu.mayo.phenoportal.client.authentication.UserRoles;
import edu.mayo.phenoportal.client.datasource.UsersXmlDS;
import edu.mayo.phenoportal.client.phenotype.PhenotypeService;
import edu.mayo.phenoportal.client.phenotype.PhenotypeServiceAsync;
import edu.mayo.phenoportal.client.utils.FormValidators;
import edu.mayo.phenoportal.client.utils.UiHelper;
import edu.mayo.phenoportal.client.utils.UserAddressForm;
import edu.mayo.phenoportal.client.utils.UserCredentialsForm;
import edu.mayo.phenoportal.shared.User;
import edu.mayo.phenoportal.shared.UserRoleRequest;

/**
 * Panel to display the user's profile and allow them to change it.
 */
public class ProfilePanel extends VLayout {

    private static final int WIDTH = 270;
    protected static final int WIDGET_WIDTH = 300;
    private static final int HEIGHT = 175;
    private static final String TITLE = "Profile";

    private static final String CURRENT_ROLE_READ_ONLY_TEXT = "Your current role is <b>'Read Only'</b>. With this role, you can search and browse for phenotypes as well as view phenotype properties.  If you need to upload or execute phenotypes,  then you will need to request to be added to the <b>Execute</b> role.";
    private static final String CURRENT_ROLE_EXECUTE_TEXT = "Your current role is <b>'Execute'</b>. With this role, you can upload and execute phenotypes";
    private static final String CURRENT_ROLE_ADMIN_TEXT = "Your current role is <b>'Admin'</b>. With this role, you can do anything you need to.";

    private User i_currentUser;
    private UserRoleRequest i_userRoleRequest;

    private UserAddressForm i_userAddressform;
    private UserCredentialsForm i_userCredentialsform;
    private DynamicForm i_passwordForm;
    private Label i_titleLabel;

    private PasswordItem i_passwordItem;
    private PasswordItem i_passwordItem2;
    private IButton i_changePwButton;

    private Label i_roleLabel;
    private Label i_requestMadeLabel;
    private IButton i_roleButton;

    private final UsersXmlDS i_usersDS;

    public ProfilePanel() {
        i_usersDS = UsersXmlDS.getInstance();
        init();
    }

    private void init() {
        setWidth100();
        setHeight100();

        // add rounded borders to the layout.
        UiHelper.createLayoutWithBorders(this);

        String title = UiHelper.getFormattedLabelText(TITLE);
        i_titleLabel = UiHelper.createLabel(title, Alignment.LEFT);
        addMember(i_titleLabel);
        addMember(getSeparator());

        createUserInfoSection();
        createPasswordSection();
        createRoleSection();
        createWatchListSection();

        getUserInfo();
    }

    /**
     * Add the user information - credentials and address.
     */
    private void createUserInfoSection() {

        HLayout formLayout = new HLayout(10);
        formLayout.setWidth100();
        formLayout.setHeight(HEIGHT);
        formLayout.setAlign(Alignment.LEFT);

        // add the user form
        i_userAddressform = new UserAddressForm();
        i_userAddressform.setMinimumHeight();

        i_userCredentialsform = new UserCredentialsForm();
        i_userCredentialsform.setDataSource(i_usersDS);
        i_userCredentialsform.setIdFieldDisabled(true);
        i_userCredentialsform.PasswordFieldsSetVisible(false);

        formLayout.addMember(i_userCredentialsform);
        formLayout.addMember(i_userAddressform);

        addMember(formLayout);

        // add the save button
        IButton saveButton = new IButton("Save");
        saveButton.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

            @Override
            public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
                updateUser();
            }
        });

        HLayout buttonLayout = getButtonLayout();
        buttonLayout.addMember(saveButton);
        addMember(buttonLayout);
    }

    /**
     * Add the change password section
     */
    private void createPasswordSection() {

        TitleOrientation titleOrientation = TitleOrientation.TOP;
        String role = UiHelper.getFormattedLabelText("Change Password");
        Label titleRoleLabel = UiHelper.createLabel(role, Alignment.LEFT);
        addMember(titleRoleLabel);
        addMember(getSeparator());

        i_passwordForm = new DynamicForm();
        i_passwordForm.setMargin(10);

        i_passwordForm.setCellPadding(6);
        i_passwordForm.setWidth(WIDTH);
        i_passwordForm.setHeight(100);
        i_passwordForm.setTitleOrientation(titleOrientation);
        i_passwordForm.setAutoFocus(true);
        i_passwordForm.setNumCols(1);
        i_passwordForm.setAlign(Alignment.LEFT);

        i_passwordItem = new PasswordItem("password");
        i_passwordItem.setTitle("Password");
        i_passwordItem.setWidth(WIDGET_WIDTH);
        i_passwordItem.setRequired(true);
        i_passwordItem.setValidators(
                FormValidators.getInstance().getPasswordLengthRangeValidator(), FormValidators
                        .getInstance().getMatchesValidator());

        i_passwordItem2 = new PasswordItem("password2");
        i_passwordItem2.setTitle("Password Confirm");
        i_passwordItem2.setWidth(WIDGET_WIDTH);
        i_passwordItem2.setRequired(true);
        i_passwordItem2.setValidators(FormValidators.getInstance()
                .getPasswordLengthRangeValidator());
        i_passwordItem2.setWrapTitle(false);

        i_passwordForm.setFields(new FormItem[] { i_passwordItem, i_passwordItem2, });

        // add the change pw button
        i_changePwButton = new IButton("Change Password");
        i_changePwButton.setWidth(160);
        i_changePwButton.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

            @Override
            public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {

                if (i_passwordForm.validate()) {
                    changePassword();
                }
            }
        });

        addMember(i_passwordForm);
        HLayout buttonLayout = getButtonLayout();
        buttonLayout.addMember(i_changePwButton);
        addMember(buttonLayout);
    }

    /**
     * Add the role section.
     */
    private void createRoleSection() {
        String role = UiHelper.getFormattedLabelText("Role");
        Label titleRoleLabel = UiHelper.createLabel(role, Alignment.LEFT);
        addMember(titleRoleLabel);
        addMember(getSeparator());

        i_roleLabel = new Label("");
        i_roleLabel.setWidth100();
        i_roleLabel.setMargin(10);
        i_roleLabel.setHeight(20);
        addMember(i_roleLabel);

        i_requestMadeLabel = new Label("");
        i_requestMadeLabel.setWidth100();
        i_requestMadeLabel.setMargin(10);
        i_requestMadeLabel.setHeight(5);
        addMember(i_requestMadeLabel);

        // add the request button
        i_roleButton = new IButton("Request Execute Role");
        i_roleButton.setWidth(160);
        i_roleButton.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

            @Override
            public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
                requestPermissionUpgrade();
            }
        });

        HLayout buttonLayout = getButtonLayout();
        buttonLayout.addMember(i_roleButton);
        addMember(buttonLayout);
    }

    /**
     * Add the watchlist here.
     */
    private void createWatchListSection() {
        // Watch list
        String watch = UiHelper.getFormattedLabelText("Watch List");
        Label watchLabel = UiHelper.createLabel(watch, Alignment.LEFT);
        addMember(watchLabel);
        addMember(getSeparator());
    }

    private HLayout getButtonLayout() {
        HLayout buttonLayout = new HLayout();
        buttonLayout.setMargin(20);
        buttonLayout.setWidth100();
        buttonLayout.setHeight(20);
        buttonLayout.setAlign(Alignment.LEFT);
        return buttonLayout;
    }

    /**
     * Create a label to use as a visual separator.
     * 
     * @return
     */
    private Label getSeparator() {
        Label seperator = new Label("<hr>");
        seperator.setHeight(5);
        seperator.setWidth100();
        return seperator;
    }

    protected void updateRoleInfo() {

        int currentRole = i_currentUser.getRole();
        boolean requestMade = false;

        if ((i_userRoleRequest.getRequestDate() != null && i_userRoleRequest.getRequestDate()
                .length() > 0)
                && (i_userRoleRequest.getResponseDate() == null || i_userRoleRequest
                        .getResponseDate().length() == 0)) {

            // just use the date (don't show the time).
            String requestDate = getDate(i_userRoleRequest.getRequestDate());

            String requestMadeText = "<i>Your request to upgrade your role is pending.  Your request was submitted on <b>"
                    + requestDate + ".</b></i>";

            i_requestMadeLabel.setContents(requestMadeText);
            requestMade = true;
        }

        // role - Read Only
        if (currentRole == UserRoles.READ) {
            i_roleLabel.setContents(CURRENT_ROLE_READ_ONLY_TEXT);
            if (!requestMade) {
                i_roleButton.show();
            } else {
                i_roleButton.hide();
            }
        }
        // role - Execute
        else if (currentRole == UserRoles.EXECUTE) {
            i_roleLabel.setContents(CURRENT_ROLE_EXECUTE_TEXT);
            i_roleButton.hide();
        }
        // role - Admin
        else if (currentRole == UserRoles.ADMIN) {
            i_roleLabel.setContents(CURRENT_ROLE_ADMIN_TEXT);
            i_roleButton.hide();
        }
    }

    /**
     * Strip off the time stamp part of the date.
     * 
     * @param requestDate
     * @return
     */
    private String getDate(String requestDate) {
        String dateOnly = requestDate;

        try {
            dateOnly = requestDate.substring(0, requestDate.indexOf(' '));
        } catch (Exception e) {
            return requestDate;
        }

        return dateOnly;
    }

    private void getUserInfo() {
        PhenotypeServiceAsync service = GWT.create(PhenotypeService.class);
        service.getUser(Htp.getLoggedInUser().getUserName(), new AsyncCallback<User>() {

            @Override
            public void onSuccess(User user) {
                i_currentUser = user;
                i_userAddressform.setData(i_currentUser);
                i_userCredentialsform.setData(i_currentUser);

                // once we update the current user, get the their role info.
                getUserRoleRequestInfo();
            }

            @Override
            public void onFailure(Throwable caught) {
                System.out.println("Failed to get Users - " + caught);
            }
        });
    }

    private void getUserRoleRequestInfo() {

        PhenotypeServiceAsync service = GWT.create(PhenotypeService.class);
        service.getUserRoleRequest(Htp.getLoggedInUser(), new AsyncCallback<UserRoleRequest>() {

            @Override
            public void onSuccess(UserRoleRequest userRoleRequest) {
                i_userRoleRequest = userRoleRequest;
                updateRoleInfo();
            }

            @Override
            public void onFailure(Throwable caught) {
                System.out.println("Failed to get UserRoleRequest - " + caught);
            }
        });
    }

    private void updateUser() {

        User user = new User();

        // credential form values
        user.setFirstName(i_userCredentialsform.getItem("firstName").getDisplayValue());
        user.setLastName(i_userCredentialsform.getItem("lastName").getDisplayValue());
        user.setEmail(i_userCredentialsform.getItem("email").getDisplayValue());
        user.setUserName(i_userCredentialsform.getItem("userId").getDisplayValue());

        // address form values
        user.setCountryRegion(i_userAddressform.getItem("countryOrRegion").getDisplayValue());
        user.setAddress(i_userAddressform.getItem("street").getDisplayValue());
        user.setCity(i_userAddressform.getItem("city").getDisplayValue());
        user.setState(i_userAddressform.getItem("state").getDisplayValue());
        user.setZipCode(i_userAddressform.getItem("zip").getDisplayValue());
        user.setPhoneNumber(i_userAddressform.getItem("phone").getDisplayValue());

        user.setRole(Htp.getLoggedInUser().getRole());
        user.setEnable(Htp.getLoggedInUser().getEnable());
        user.setRegistrationDate(Htp.getLoggedInUser().getRegistrationDate());

        PhenotypeServiceAsync service = GWT.create(PhenotypeService.class);
        service.updateUser(user, new AsyncCallback<Boolean>() {

            @Override
            public void onSuccess(Boolean result) {
                SC.say("User information has been updated.");
            }

            @Override
            public void onFailure(Throwable caught) {
                SC.say("Failed to update the user information. \n" + caught.toString());
            }
        });
    }

    private void changePassword() {
        User user = new User();

        user.setUserName(i_currentUser.getUserName());
        user.setPassword(i_passwordItem.getDisplayValue());

        AuthenticationServiceAsync service = GWT.create(AuthenticationService.class);
        service.updateUserPassword(user, new AsyncCallback<Boolean>() {

            @Override
            public void onSuccess(Boolean result) {
                SC.say("Your password has been updated.");
            }

            @Override
            public void onFailure(Throwable caught) {
                SC.say("Failed to update the user password. \n" + caught.toString());
            }
        });
    }

    protected void requestPermissionUpgrade() {
        User user = new User();

        user.setUserName(i_currentUser.getUserName());
        user.setRole(i_currentUser.getRole());

        PhenotypeServiceAsync service = GWT.create(PhenotypeService.class);
        service.requestPermissionUpgrade(user, new AsyncCallback<Boolean>() {

            @Override
            public void onSuccess(Boolean result) {
                SC.say("A request has been sent to the administrators.  You will be notified by email if your request is granted.");

                // update the Role section based on the new request/update.
                getUserRoleRequestInfo();
            }

            @Override
            public void onFailure(Throwable caught) {
                SC.say("Failed request a role upgrade. \n" + caught.toString());
            }
        });

    }

}
