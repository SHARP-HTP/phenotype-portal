package edu.mayo.phenoportal.client.login;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;

import edu.mayo.phenoportal.client.Htp;
import edu.mayo.phenoportal.client.authentication.AuthenticationService;
import edu.mayo.phenoportal.client.authentication.AuthenticationServiceAsync;
import edu.mayo.phenoportal.client.events.LoginRegistrationCancelEvent;
import edu.mayo.phenoportal.client.utils.MessageWindow;
import edu.mayo.phenoportal.client.utils.UserAddressForm;
import edu.mayo.phenoportal.client.utils.UserCredentialsForm;
import edu.mayo.phenoportal.shared.User;

public class TabRegister extends Tab {

    // private static final int WIDTH = 400;
    private static final int HEIGHT = 250;

    private static final String TITLE = "Register";
    private static final String ICON = "register.png";

    private static final String TITLE_SUCCESS = "Registration Success";
    private static final String REGISTRATION_MSG_SUCCESS = "Your registration has been submitted.  You will recieve an email when your registration has been accepted and enabled.";

    private static final String TITLE_ERROR = "Registration Error";
    private static final String REGISTRATION_MSG_ERROR = "The User Id is not unique.  Please enter a different User Id.";

    private final DynamicForm i_userAddressform;
    private final DynamicForm i_credentialform;

    public TabRegister() {
        super(TITLE, ICON);
        setWidth(100);

        i_userAddressform = new UserAddressForm(150, true);
        i_credentialform = new UserCredentialsForm(150, true);

        // put the form on in a vlayout and hlayout to center it
        VLayout vlayout = new VLayout();
        vlayout.setAlign(Alignment.CENTER);
        vlayout.setWidth100();
        vlayout.setHeight100();
        vlayout.setBackgroundColor(LoginPanel.TAB_BACKGROUND_COLOR);

        HLayout formLayout = new HLayout(30);
        formLayout.setWidth100();
        formLayout.setHeight(HEIGHT);
        formLayout.setAlign(Alignment.CENTER);

        HLayout buttonLayout = new HLayout(20);
        buttonLayout.setWidth100();
        buttonLayout.setHeight(30);
        buttonLayout.setAlign(Alignment.CENTER);

        IButton cancelButton = new IButton("Cancel");
        cancelButton.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

            @Override
            public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
                // cancel login
                Htp.EVENT_BUS.fireEvent(new LoginRegistrationCancelEvent());
            }
        });

        IButton registerButton = new IButton("Register");
        registerButton.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

            @Override
            public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
                boolean validUserInfo = i_credentialform.validate();
                boolean validAddressInfo = i_userAddressform.validate();

                if (validUserInfo && validAddressInfo) {
                    registerUser();
                }
            }
        });

        buttonLayout.addMember(registerButton);
        buttonLayout.addMember(cancelButton);

        formLayout.addMember(i_credentialform);
        formLayout.addMember(i_userAddressform);

        vlayout.addMember(formLayout);
        vlayout.addMember(buttonLayout);

        setPane(vlayout);
    }

    /**
     * Create the user by making an RCP call
     */
    private void registerUser() {

        User user = new User();

        // credential form values
        user.setFirstName(i_credentialform.getItem("firstName").getDisplayValue());
        user.setLastName(i_credentialform.getItem("lastName").getDisplayValue());
        user.setEmail(i_credentialform.getItem("email").getDisplayValue());
        user.setUserName(i_credentialform.getItem("userId").getDisplayValue());
        user.setPassword(i_credentialform.getItem("password").getDisplayValue());

        // address form values
        user.setCountryRegion(i_userAddressform.getItem("countryOrRegion").getDisplayValue());
        user.setAddress(i_userAddressform.getItem("street").getDisplayValue());
        user.setCity(i_userAddressform.getItem("city").getDisplayValue());
        user.setState(i_userAddressform.getItem("state").getDisplayValue());
        user.setZipCode(i_userAddressform.getItem("zip").getDisplayValue());
        user.setPhoneNumber(i_userAddressform.getItem("phone").getDisplayValue());

        AuthenticationServiceAsync authenticationService = GWT.create(AuthenticationService.class);
        authenticationService.registerUser(user, new AsyncCallback<Boolean>() {

            @Override
            public void onSuccess(Boolean success) {

                if (success) {
                    MessageWindow messageWindow = new MessageWindow(TITLE_SUCCESS,
                            REGISTRATION_MSG_SUCCESS);
                    messageWindow.show();
                } else {
                    MessageWindow messageWindow = new MessageWindow(TITLE_ERROR,
                            REGISTRATION_MSG_ERROR);
                    messageWindow.show();
                }
            }

            @Override
            public void onFailure(Throwable caught) {
                // UserId is not unique.
                // Could also be DB connection failure.
                SC.say(TITLE_ERROR, REGISTRATION_MSG_ERROR);
            }
        });
    }
}