package edu.mayo.phenoportal.client.core;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.LinkItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

import edu.mayo.phenoportal.client.Htp;
import edu.mayo.phenoportal.client.authentication.SessionHelper;
import edu.mayo.phenoportal.client.events.ContextAreaChangedEvent;
import edu.mayo.phenoportal.client.events.LogInRequestEvent;
import edu.mayo.phenoportal.client.events.LogOutRequestEvent;
import edu.mayo.phenoportal.client.events.LoggedInEvent;
import edu.mayo.phenoportal.client.events.LoggedInEventHandler;
import edu.mayo.phenoportal.client.events.LoggedOutEvent;
import edu.mayo.phenoportal.client.events.LoggedOutEventHandler;
import edu.mayo.phenoportal.shared.User;

/**
 * Header contains the Sharp logo and login/register LinkItem Form.
 */
public class HeaderMenu extends HLayout {

    static Logger lgr = Logger.getLogger(HeaderMenu.class.getName());
    private static final String BACKGROUND_COLOR = "#6056b7";
    private static final int HEADER_HEIGHT = 60;
    private static final String TEXT_LOG_IN = "Log In";
    private static final String TEXT_LOG_OUT = "Log Out";
    private static final String TEXT_REGISTER = "Register";
    private static final String SHARP_URL = "http://informatics.mayo.edu/sharp/index.php/HTP";

    private DynamicForm i_form;
    private LinkItem i_userNameLinkItem;
    private LinkItem i_loginLinkItem;
    private LinkItem i_logoutLinkItem;
    private LinkItem i_registerLinkItem;
    private LinkItem i_seperatorLinkItem;

    private final HLayout i_menuLayout;
    private final VLayout i_vlayout;

    public HeaderMenu() {
        super();

        lgr.log(Level.INFO, "init HeaderMenu()...");

        // initialize the layout container
        setHeight(HEADER_HEIGHT);
        setBackgroundColor(BACKGROUND_COLOR);

        Img sharpLogo = new Img("sharp_logo.png", 182, HEADER_HEIGHT);
        sharpLogo.setCanHover(true);
        sharpLogo.setShowHover(true);

        Img phenoportalImg = new Img("phenoportal.png", 355, HEADER_HEIGHT);
        HLayout portalImgLayout = new HLayout();
        portalImgLayout.setWidth(400);
        portalImgLayout.setHeight(HEADER_HEIGHT);
        portalImgLayout.setAlign(Alignment.RIGHT);
        portalImgLayout.setAlign(VerticalAlignment.BOTTOM);

        portalImgLayout.addMember(phenoportalImg);

        // set the mouse over text of the image
        sharpLogo.setPrompt("Go to the SHARPn Project");
        sharpLogo.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

            @Override
            public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
                com.google.gwt.user.client.Window.open(SHARP_URL, "sharp", null);
            }
        });

        // Create hLayout to add welcome menu
        HLayout welcomeMenuLayout = new HLayout();
        welcomeMenuLayout.setAlign(Alignment.RIGHT);

        i_vlayout = new VLayout();
        i_vlayout.setWidth(225);

        // need i_vlayout to center the menu items vertically
        i_menuLayout = new HLayout();
        i_menuLayout.setMargin(10);
        i_menuLayout.setMembersMargin(10);
        i_menuLayout.setHeight(20);
        i_menuLayout.setWidth(400);
        i_menuLayout.setAlign(Alignment.RIGHT);

        createLinkItems(null);

        welcomeMenuLayout.addMember(i_vlayout);

        addMember(sharpLogo);
        addMember(portalImgLayout);
        addMember(welcomeMenuLayout);

        createLoggedInEventHandler();
        createLoggedOutEventHandler();
    }

    private void createLinkItems(User user) {
        i_form = null;
        i_form = new DynamicForm();
        i_form.setLayoutAlign(Alignment.RIGHT);
        i_form.setStyleName("htpMenu");

        String userName = user != null ? user.getUserName() : "";

        i_userNameLinkItem = createMenuItem(userName);
        i_userNameLinkItem.setWidth(200);
        i_userNameLinkItem.setAlign(Alignment.RIGHT);
        i_userNameLinkItem.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                try {
                    Htp.EVENT_BUS
                            .fireEvent(new ContextAreaChangedEvent(ContextAreas.types.PROFILE));
                } catch (Exception e) {
                    lgr.log(Level.INFO, e.getMessage());
                    e.printStackTrace();
                }
            }
        });

        i_loginLinkItem = createMenuItem(TEXT_LOG_IN);
        i_loginLinkItem.setWidth(80);
        i_loginLinkItem.setAlign(Alignment.RIGHT);
        i_loginLinkItem.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                // Fire event to let listeners know that that a user is
                // requesting to log in
                Htp.EVENT_BUS.fireEvent(new LogInRequestEvent(false));
            }
        });

        i_logoutLinkItem = createMenuItem(TEXT_LOG_OUT);
        i_logoutLinkItem.setWidth(100);
        i_logoutLinkItem.setAlign(Alignment.LEFT);
        i_logoutLinkItem.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                // Fire event to let listeners know that that a user is
                // requesting to log in
                Htp.EVENT_BUS.fireEvent(new LogOutRequestEvent());
            }
        });

        i_registerLinkItem = createMenuItem(TEXT_REGISTER);
        i_registerLinkItem.setWidth(110);
        i_registerLinkItem.setAlign(Alignment.LEFT);
        i_registerLinkItem.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                // Fire event to let listeners know that that a user is
                // requesting to register
                Htp.EVENT_BUS.fireEvent(new LogInRequestEvent(true));
            }
        });

        i_seperatorLinkItem = createMenuItem(" | ");
        i_seperatorLinkItem.setWidth(10);
        i_seperatorLinkItem.setAlign(Alignment.CENTER);
        i_seperatorLinkItem.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                // do nothing
            }
        });

        // create the links based on whether or not the user is logged in.
        i_form.setNumCols(3);

        if (user == null) {
            i_form.setFields(i_loginLinkItem, i_seperatorLinkItem, i_registerLinkItem);
        } else {
            i_form.setFields(i_userNameLinkItem, i_seperatorLinkItem, i_logoutLinkItem);
        }

        // add the form into the main layout
        i_menuLayout.addMember(i_form);
        i_vlayout.addMember(i_menuLayout);
    }

    /**
     * Update the header with the current user logged in if the User passed in
     * is not null.
     * 
     * @param user
     */
    public void updateUserInfo(User user) {

        if (user != null) {
            i_menuLayout.removeMember(i_form);
            createLinkItems(user);
        }
    }

    /**
     * Create a handler to change Logged in/out button based on the log in state
     */
    private void createLoggedInEventHandler() {
        Htp.EVENT_BUS.addHandler(LoggedInEvent.TYPE, new LoggedInEventHandler() {

            @Override
            public void onLoggedIn(LoggedInEvent loggedInEvent) {
                updateUserInfo(loggedInEvent.getUser());

            }
        });
    }

    /**
     * Create a handler to change Logged in/out button based on the log in state
     */
    private void createLoggedOutEventHandler() {
        Htp.EVENT_BUS.addHandler(LoggedOutEvent.TYPE, new LoggedOutEventHandler() {

            @Override
            public void onLoggedOut(LoggedOutEvent loggedOutEvent) {
                i_menuLayout.removeMember(i_form);
                createLinkItems(null);

                SessionHelper.removeSessionIdOnClient();
            }
        });
    }

    private LinkItem createMenuItem(String title) {

        LinkItem linkItem = new LinkItem(title.replace(" ", "_"));
        linkItem.setCellStyle("htpLinks");
        linkItem.setTextBoxStyle("htpLinks");

        linkItem.setShowTitle(false);
        linkItem.setTitle(title.replace(" ", "_"));
        linkItem.setLinkTitle(title);

        return linkItem;
    }
}
