package edu.mayo.phenoportal.client.login;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.KeyNames;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.PasswordItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.KeyUpEvent;
import com.smartgwt.client.widgets.form.fields.events.KeyUpHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;

import edu.mayo.phenoportal.client.Htp;
import edu.mayo.phenoportal.client.authentication.AuthenticationService;
import edu.mayo.phenoportal.client.authentication.AuthenticationServiceAsync;
import edu.mayo.phenoportal.client.authentication.SessionHelper;
import edu.mayo.phenoportal.client.events.LoggedInEvent;
import edu.mayo.phenoportal.client.events.LoginRegistrationCancelEvent;
import edu.mayo.phenoportal.client.utils.MessageWindow;
import edu.mayo.phenoportal.client.utils.ModalWindow;
import edu.mayo.phenoportal.shared.User;

/**
 * Tab for displaying the login dialog
 */
public class TabLogin extends Tab {

    private static final int WIDTH = 350;
    private static final int HEIGHT = 200;

    private static final String TITLE = "Log In";
    private static final String ICON = "person.png";

    private final DynamicForm i_form;
    private final VLayout i_mainLayout;

    private static String i_sessionId;

    public TabLogin() {
        super(TITLE, ICON);

        TitleOrientation titleOrientation = TitleOrientation.LEFT;

        i_mainLayout = new VLayout();
        i_mainLayout.setAlign(Alignment.CENTER);
        i_mainLayout.setWidth100();
        i_mainLayout.setHeight100();
        i_mainLayout.setBackgroundColor(LoginPanel.TAB_BACKGROUND_COLOR);

        HLayout formLayout = new HLayout(30);
        formLayout.setWidth100();
        formLayout.setHeight(HEIGHT);
        // formLayout.setBackgroundColor("green");
        formLayout.setAlign(Alignment.CENTER);

        i_form = new DynamicForm();
        i_form.setMargin(10);
        i_form.setCellPadding(10);
        i_form.setWidth(WIDTH);
        i_form.setHeight(HEIGHT);
        i_form.setTitleOrientation(titleOrientation);
        i_form.setAutoFocus(true);
        i_form.setNumCols(2);
        i_form.setAlign(Alignment.CENTER);

        TextItem userIdItem = new TextItem("userId");
        userIdItem.setSelectOnFocus(true);
        userIdItem.setTitle("User Id");
        userIdItem.setWidth(LoginPanel.WIDGET_WIDTH);
        userIdItem.setRequired(true);

        // if user clicks on the ENTER key, with this button in focus then have
        // this act as clicking the login button.
        userIdItem.addKeyUpHandler(new KeyUpHandler() {

            @Override
            public void onKeyUp(KeyUpEvent event) {
                if (event.getKeyName().equals(KeyNames.ENTER)) {
                    authenticateUser();
                }
            }
        });

        PasswordItem passwordItem = new PasswordItem("password");
        passwordItem.setTitle("Password");
        passwordItem.setWidth(LoginPanel.WIDGET_WIDTH);
        passwordItem.setRequired(true);

        // if user clicks on the ENTER key, with this button in focus then have
        // this act as clicking the login button.
        passwordItem.addKeyUpHandler(new KeyUpHandler() {

            @Override
            public void onKeyUp(KeyUpEvent event) {
                if (event.getKeyName().equals(KeyNames.ENTER)) {
                    authenticateUser();
                }
            }
        });

        i_form.setFields(new FormItem[] { userIdItem, passwordItem });
        formLayout.addMember(i_form);

        HLayout buttonLayout = new HLayout(20);
        buttonLayout.setWidth100();
        buttonLayout.setHeight(30);
        buttonLayout.setAlign(Alignment.CENTER);

        IButton cancelButton = new IButton("Cancel");
        cancelButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                // cancel login
                Htp.EVENT_BUS.fireEvent(new LoginRegistrationCancelEvent());
            }
        });

        IButton loginButton = new IButton("Login");
        loginButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
                authenticateUser();
            }
        });

        buttonLayout.addMember(loginButton);
        buttonLayout.addMember(cancelButton);

        i_mainLayout.addMember(formLayout);
        i_mainLayout.addMember(buttonLayout);
        setPane(i_mainLayout);
    }

    /**
     * Validate the the user entered a user id and pw
     * 
     * @return
     */
    private boolean validateFields() {
        boolean valid = false;

        String userName = i_form.getValueAsString("userId");
        String pw = i_form.getValueAsString("password");

        valid = userName != null && userName.length() > 0 && pw != null && pw.length() > 0;
        if (!valid) {
            String title = "Login Incomplete";
            String message = "Please enter a user id and password.";
            MessageWindow messageWindow = new MessageWindow(title, message);
            messageWindow.show();
        }

        return valid;
    }

    /**
     * Call server to authenticate the user
     */
    private void authenticateUser() {

        if (!validateFields()) {
            return;
        }

        AuthenticationServiceAsync authenticationService = GWT.create(AuthenticationService.class);

        final String userName = i_form.getValueAsString("userId");
        String password = i_form.getValueAsString("password");

        // Set the busy indicator to show while validating user credentials.
        final ModalWindow busyIndicator = new ModalWindow(i_mainLayout, 40, "#dedede");
        busyIndicator.setLoadingIcon("loading_circle.gif");
        busyIndicator.show("Validating Credentials...", true);

        authenticationService.authenticateUser(userName, password, new AsyncCallback<User>() {

            @Override
            public void onSuccess(User user) {
                busyIndicator.hide();

                // if User object is null, then the login failed.
                if (user == null) {
                    String title = "Login Failed";
                    String message = "Invalid Id/Password.  Please try again.";
                    MessageWindow messageWindow = new MessageWindow(title, message);
                    messageWindow.show();

                } else {
                    i_sessionId = user.getSessionId();
                    SessionHelper.storeSessionIdOnClient(user.getSessionId());

                    // let other know that the login was successful
                    Htp.EVENT_BUS.fireEvent(new LoggedInEvent(user));
                    GWT.log("Log in successful for " + userName);
                }
            }

            @Override
            public void onFailure(Throwable caught) {
                busyIndicator.hide();
                GWT.log("Error Authenticating " + userName, caught);

                String title = "Login Failed";
                String message = "Login failed - " + caught.getMessage();
                MessageWindow messageWindow = new MessageWindow(title, message);
                messageWindow.show();
            }
        });

    }

}
