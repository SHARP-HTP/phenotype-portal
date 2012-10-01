package edu.mayo.phenoportal.client.navigation;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.LinkItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

import edu.mayo.phenoportal.client.Htp;
import edu.mayo.phenoportal.client.authentication.UserRoles;
import edu.mayo.phenoportal.client.core.ContextAreas;
import edu.mayo.phenoportal.client.core.ContextAreas.types;
import edu.mayo.phenoportal.client.events.ContextAreaChangedEvent;
import edu.mayo.phenoportal.client.events.LoggedInEvent;
import edu.mayo.phenoportal.client.events.LoggedInEventHandler;
import edu.mayo.phenoportal.client.events.LoggedOutEvent;
import edu.mayo.phenoportal.client.events.LoggedOutEventHandler;
import edu.mayo.phenoportal.shared.User;

/**
 * Header contains the horizontal links to home/phenotypes/help/admin
 */
public class NavigationHeader extends HLayout {

    private static final int HEADER_HEIGHT = 25;
    private static final String BACKGROUND_COLOR = "#b0abdb"; // "#9056b7"; //
                                                              // "#6a5172";

    private DynamicForm i_form;

    private static final String HOME_TITLE = "Home";
    private static final String PHENOTYPES_TITLE = "Phenotypes";
    private static final String HELP_TITLE = "Help";
    private static final String ADMIN_TITLE = "Admin";

    private LinkItem i_homeLinkItem;
    private LinkItem i_phenotypesLinkItem;
    private LinkItem i_helpLinkItem;
    private LinkItem i_adminLinkItem;

    private VLayout i_menuLayout;

    private User i_user;

    public NavigationHeader() {
        super();
        init();
    }

    private void init() {
        setHeight(HEADER_HEIGHT);
        setWidth100();
        setBackgroundColor(BACKGROUND_COLOR);

        setAlign(Alignment.LEFT);

        // add space to the left/beginning.
        HLayout spaceLayout = new HLayout();
        spaceLayout.setWidth(10);
        spaceLayout.setBackgroundColor(BACKGROUND_COLOR);
        addMember(spaceLayout);

        // need vlayout to center the menu items vertically
        i_menuLayout = new VLayout();
        i_menuLayout.setHeight100();
        i_menuLayout.setWidth(800);
        i_menuLayout.setAlign(Alignment.CENTER);

        createMenu(false);

        createLoggedInEventHandler();
        createLoggedOutEventHandler();
    }

    private void createMenu(boolean addAdminLink) {
        i_form = null;
        i_form = new DynamicForm();
        i_form.setLayoutAlign(Alignment.LEFT);
        i_form.setStyleName("htpMenu");

        // set the width of each menu item so the look space evenly.
        i_homeLinkItem = createMenuItem(HOME_TITLE);
        i_homeLinkItem.setWidth(120);

        i_phenotypesLinkItem = createMenuItem(PHENOTYPES_TITLE);
        i_phenotypesLinkItem.setWidth(140);

        i_helpLinkItem = createMenuItem(HELP_TITLE);
        i_helpLinkItem.setWidth(100);

        i_adminLinkItem = createMenuItem(ADMIN_TITLE);
        i_adminLinkItem.setWidth(120);

        i_homeLinkItem.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                // Fire event to change the context area (panel) to what is
                // selected.
                Htp.EVENT_BUS.fireEvent(new ContextAreaChangedEvent(ContextAreas.types.WELCOME));
            }
        });

        i_phenotypesLinkItem.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                Htp.EVENT_BUS.fireEvent(new ContextAreaChangedEvent(ContextAreas.types.PHENOTYPES));
            }
        });

        i_helpLinkItem.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                Htp.EVENT_BUS.fireEvent(new ContextAreaChangedEvent(ContextAreas.types.HELP));
            }
        });

        i_adminLinkItem.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                // Fire event to change the context area (panel) to what is
                // selected.
                Htp.EVENT_BUS.fireEvent(new ContextAreaChangedEvent(ContextAreas.types.ADMIN));
            }
        });

        // determine if we should add the admin link
        if (addAdminLink) {
            // This width should add up to the total of each individual width
            // set above.
            i_form.setWidth(480);
            i_form.setNumCols(4);
            i_form.setFields(i_homeLinkItem, i_phenotypesLinkItem, i_helpLinkItem, i_adminLinkItem);
        } else {
            // This width should add up to the total of each individual width
            // set above.
            i_form.setWidth(360);
            i_form.setNumCols(3);
            i_form.setFields(i_homeLinkItem, i_phenotypesLinkItem, i_helpLinkItem);
        }

        i_menuLayout.addMember(i_form);
        addMember(i_menuLayout);
    }

    private LinkItem createMenuItem(String title) {

        LinkItem linkItem = new LinkItem(title.replace(" ", "_"));
        linkItem.setCellStyle("htpMenu");

        linkItem.setAlign(Alignment.LEFT);
        linkItem.setShowTitle(false);
        linkItem.setTitle(title.replace(" ", "_"));
        linkItem.setLinkTitle(title);

        return linkItem;
    }

    /**
     * Create a handler to update the "admin" link based on the log in state.
     */
    private void createLoggedInEventHandler() {
        Htp.EVENT_BUS.addHandler(LoggedInEvent.TYPE, new LoggedInEventHandler() {

            @Override
            public void onLoggedIn(LoggedInEvent loggedInEvent) {
                i_user = loggedInEvent.getUser();
                updateUserInfo(i_user);
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
                i_user = null;
                i_menuLayout.removeMember(i_form);
                createMenu(false);
            }
        });
    }

    /**
     * If the logged in user has admin authority, then show the Admin link.
     * 
     * @param user
     */
    public void updateUserInfo(User user) {

        if (user != null) {
            int role = user.getRole();
            i_menuLayout.removeMember(i_form);
            createMenu(role == UserRoles.ADMIN);
        }
    }

}
