package edu.mayo.phenoportal.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.ClosingEvent;
import com.google.gwt.user.client.Window.ClosingHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.NamedFrame;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.VLayout;

import edu.mayo.phenoportal.client.admin.AdminPanel;
import edu.mayo.phenoportal.client.authentication.AuthenticationService;
import edu.mayo.phenoportal.client.authentication.AuthenticationServiceAsync;
import edu.mayo.phenoportal.client.core.ContextAreaPanel;
import edu.mayo.phenoportal.client.core.ContextAreas;
import edu.mayo.phenoportal.client.core.HeaderMenu;
import edu.mayo.phenoportal.client.core.PhenotypePanel;
import edu.mayo.phenoportal.client.core.WelcomePanel;
import edu.mayo.phenoportal.client.events.ContextAreaChangedEvent;
import edu.mayo.phenoportal.client.events.ContextAreaChangedEventHandler;
import edu.mayo.phenoportal.client.events.LogInRequestEvent;
import edu.mayo.phenoportal.client.events.LogInRequestEventHandler;
import edu.mayo.phenoportal.client.events.LogOutRequestEvent;
import edu.mayo.phenoportal.client.events.LogOutRequestEventHandler;
import edu.mayo.phenoportal.client.events.LoggedInEvent;
import edu.mayo.phenoportal.client.events.LoggedInEventHandler;
import edu.mayo.phenoportal.client.events.LoggedOutEvent;
import edu.mayo.phenoportal.client.events.LoginRegistrationCancelEvent;
import edu.mayo.phenoportal.client.events.LoginRegistrationCancelEventHandler;
import edu.mayo.phenoportal.client.events.PhenotypeExecuteCompletedEvent;
import edu.mayo.phenoportal.client.events.PhenotypeExecuteCompletedEventHandler;
import edu.mayo.phenoportal.client.events.PhenotypeExecuteStartedEvent;
import edu.mayo.phenoportal.client.events.PhenotypeExecuteStartedEventHandler;
import edu.mayo.phenoportal.client.events.UploadCanceledEvent;
import edu.mayo.phenoportal.client.events.UploadCanceledEventHandler;
import edu.mayo.phenoportal.client.events.UploadCompletedEvent;
import edu.mayo.phenoportal.client.events.UploadCompletedEventHandler;
import edu.mayo.phenoportal.client.events.UploadStartedEvent;
import edu.mayo.phenoportal.client.events.UploadStartedEventHandler;
import edu.mayo.phenoportal.client.help.UserManual;
import edu.mayo.phenoportal.client.login.LoginPanel;
import edu.mayo.phenoportal.client.navigation.NavigationHeader;
import edu.mayo.phenoportal.client.phenotype.PhenotypeService;
import edu.mayo.phenoportal.client.phenotype.PhenotypeServiceAsync;
import edu.mayo.phenoportal.client.profile.ProfilePanel;
import edu.mayo.phenoportal.client.upload.UploadContainer;
import edu.mayo.phenoportal.client.utils.ModalWindow;
import edu.mayo.phenoportal.client.utils.UiHelper;
import edu.mayo.phenoportal.shared.User;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Htp implements EntryPoint {

    static Logger lgr = Logger.getLogger(Htp.class.getName());
    private static final int NORTH_HEIGHT = 45;

    private VLayout i_overallLayout;
    private HLayout i_mainLayout;
    private HLayout i_northLayout;

    // panel that displays the main area
    private ContextAreaPanel i_contextAreaPanel;

    private WelcomePanel i_welcomePanel;
    private PhenotypePanel i_phenotypePanel;
    private UploadContainer i_uploadContainer;
    private AdminPanel i_adminPanel;
    private UserManual i_userManualPanel;
    private ProfilePanel i_profilePanel;

    private HeaderMenu i_headerMenu;
    private NavigationHeader i_navigationHeader;

    private LoginPanel i_loginPanel;

    private ModalWindow i_busyIndicator;

    private static User i_loggedInUser;

    // Event Bus to capture global events and act upon them.
    public static EventBus EVENT_BUS = GWT.create(SimpleEventBus.class);

    // This is for development mode
    public static boolean DEBUG_MODE = false;

    /**
     * This is the entry point method.
     */
    @Override
    public void onModuleLoad() {

        lgr.log(Level.INFO, "init OnLoadModule()...");
        // get rid of scroll bars, and clear out the window's built-in margin,
        // because we want to take advantage of the entire client area
        Window.enableScrolling(false);
        Window.setMargin("0px");

        // initialize the main layout container
        i_overallLayout = new VLayout();
        i_overallLayout.setWidth100();
        i_overallLayout.setHeight100();

        i_mainLayout = new HLayout();
        i_mainLayout.setWidth100();
        i_mainLayout.setHeight100();
        // i_mainLayout.setBorder("1px dashed green");

        // initialize the North layout container
        i_northLayout = new HLayout();
        i_northLayout.setHeight(NORTH_HEIGHT);

        // add rounded borders to the layout.
        UiHelper.createLayoutWithBorders(i_northLayout);

        // add the Header menu to the nested layout container
        i_headerMenu = new HeaderMenu();
        i_navigationHeader = new NavigationHeader();

        // Layout to add the two headers vertically
        VLayout headerLayout = new VLayout();
        headerLayout.addMember(i_headerMenu);
        headerLayout.addMember(i_navigationHeader);

        // Add the top Navigation Pane
        i_northLayout.addMember(headerLayout);

        i_contextAreaPanel = new ContextAreaPanel();
        i_mainLayout.addMember(i_contextAreaPanel);

        /* Hidden frame for callback */
        NamedFrame callbackFrame = new NamedFrame("uploadCallbackFrame");
        callbackFrame.setHeight("1px");
        callbackFrame.setWidth("1px");
        callbackFrame.setVisible(false);
        i_contextAreaPanel.addMember(callbackFrame);

        // Add the welcome page to the context area panel, initially
        i_contextAreaPanel.setCurrentContextArea(getWelcomePanel());

        // Add the layout to overall layout
        i_overallLayout.addMember(i_northLayout);
        i_overallLayout.addMember(i_mainLayout);

        // Draw the Layout - main layout
        RootLayoutPanel.get().add(i_overallLayout);

        setLoggingProperties();
        createContextAreaChangedEventHandler();
        createLogInRequestEvent();
        createLoggedInEventHandler();
        createLogOutRequestEvent();
        createUploadStartedEvent();
        createUploadCompletedEvent();
        createUploadCanceledEvent();
        createPhenotypeExecuteStartedEventHandler();
        createPhenotypeExecuteCompletedEventHandler();
        createLoginRegistrationCancelEventHandler();

        checkServerForValidSession();
        initWindowClosingConfirmationDialog();
    }

    /**
     * Method to set the property log file upon module load
     */
    private void setLoggingProperties() {

        lgr.log(Level.INFO, "Initializing the logging property file...");
        PhenotypeServiceAsync phenotService = GWT.create(PhenotypeService.class);
        phenotService.initializeLogging(new AsyncCallback<Void>() {
            @Override
            public void onSuccess(Void result) {/* Do Nothing */
            }

            @Override
            public void onFailure(Throwable caught) {/* Do Nothing */
            }
        });
    }

    /**
     * Display a confirmation dialog to leave our site when the user refreshes
     * or goes to another URL.
     */
    protected void initWindowClosingConfirmationDialog() {
        Window.addWindowClosingHandler(new ClosingHandler() {
            @Override
            public void onWindowClosing(ClosingEvent event) {
                // This message doesn't show, but by adding this close handler,
                // we get the default dialog to display and confirm that the
                // user does want to leave our site.
                event.setMessage("Are you sure you want to leave?");
            }
        });
    }

    /**
     * Create a handler to change the context area based on the user's
     * selection.
     */
    private void createContextAreaChangedEventHandler() {

        EVENT_BUS.addHandler(ContextAreaChangedEvent.TYPE, new ContextAreaChangedEventHandler() {

            @Override
            public void onContextAreaChanged(ContextAreaChangedEvent contextAreaChangedEvent) {

                ContextAreas.types selectType;

                selectType = contextAreaChangedEvent.getContextType();

                switch (selectType) {
                    case WELCOME:
                        // update the context area with the welcome panel
                        i_contextAreaPanel.setCurrentContextArea(getWelcomePanel());

                        // refresh the news items (news and SHARP news)
                        // DataSources. This is needed because admin can modify
                        // the news and it needs to then be updtated.
                        ((WelcomePanel) getWelcomePanel()).refreshNews();

                        break;
                    case PHENOTYPES:
                        // update the context area with the phenotype panel
                        i_contextAreaPanel.setCurrentContextArea(getPhenotypePanel());
                        break;
                    case HELP:
                        // update the context area with the user manual panel
                        i_contextAreaPanel.setCurrentContextArea(getUserManualPanel());
                        break;
                    case UPLOAD:
                        // update the context area with the upload panel
                        i_contextAreaPanel.setCurrentContextArea(getUploadContainer());
                        break;
                    case ADMIN:
                        // update the context area with the admin panel
                        i_contextAreaPanel.setCurrentContextArea(getAdminPanel());
                        break;
                    case PROFILE:
                        // update the context area with the profile panel
                        i_contextAreaPanel.setCurrentContextArea(getProfilePanel());
                        break;
                    default:
                        break;
                }
            }
        });
    }

    /**
     * Create a handler to listen for a login request.
     */
    private void createLogInRequestEvent() {
        EVENT_BUS.addHandler(LogInRequestEvent.TYPE, new LogInRequestEventHandler() {

            @Override
            public void onLogInRequest(LogInRequestEvent logInRequestEvent) {

                i_loginPanel = new LoginPanel();

                if (logInRequestEvent.getIsRegister()) {
                    i_loginPanel.setTabRegister();
                } else {
                    i_loginPanel.setTabLogin();
                }
                i_contextAreaPanel.setCurrentContextArea(i_loginPanel);
            }
        });
    }

    /**
     * Create a handler to listen for when a user successfully logs in
     */
    private void createLoggedInEventHandler() {
        EVENT_BUS.addHandler(LoggedInEvent.TYPE, new LoggedInEventHandler() {

            @Override
            public void onLoggedIn(LoggedInEvent loggedInEvent) {
                // save the logged in user info
                i_loggedInUser = loggedInEvent.getUser();

                i_contextAreaPanel.setCurrentContextArea(getWelcomePanel());
            }
        });
    }

    /**
     * Create a handler to listen for a log out request.
     */
    private void createLogOutRequestEvent() {
        EVENT_BUS.addHandler(LogOutRequestEvent.TYPE, new LogOutRequestEventHandler() {

            @Override
            public void onLogOutRequest(LogOutRequestEvent logOutRequestEvent) {
                AuthenticationServiceAsync authenticationService = GWT
                        .create(AuthenticationService.class);

                // terminate the session on the server
                authenticationService.terminateSession(new AsyncCallback<Void>() {

                    @Override
                    public void onSuccess(Void result) {
                        completeLogout();
                    }

                    @Override
                    public void onFailure(Throwable caught) {
                        completeLogout();
                    }

                    private void completeLogout() {
                        i_loggedInUser = null;
                        // Fire the logged out event to finalize the logout
                        Htp.EVENT_BUS.fireEvent(new LoggedOutEvent());

                        // update the context area with the welcome panel
                        i_contextAreaPanel.setCurrentContextArea(getWelcomePanel());
                    }
                });
            }
        });
    }

    /**
     * Listen for when a phenotype execution starts and do anything needed.
     */
    private void createPhenotypeExecuteCompletedEventHandler() {

        EVENT_BUS.addHandler(PhenotypeExecuteCompletedEvent.TYPE,
                new PhenotypeExecuteCompletedEventHandler() {

                    @Override
                    public void onPhenotypeExecuteCompleted(
                            PhenotypeExecuteCompletedEvent phenotypeExecuteCompletedEvent) {
                        // hide the busy indicator.
                        i_busyIndicator.hide();
                    }
                });
    }

    /**
     * Listen for when a user cancels a login or registration.
     */
    private void createLoginRegistrationCancelEventHandler() {
        EVENT_BUS.addHandler(LoginRegistrationCancelEvent.TYPE,
                new LoginRegistrationCancelEventHandler() {

                    @Override
                    public void onCancel(LoginRegistrationCancelEvent cancelEvent) {
                        i_contextAreaPanel.setCurrentContextArea(getWelcomePanel());
                    }
                });
    }

    /**
     * Listen for when a phenotype execution completes and do anything needed.
     */
    private void createPhenotypeExecuteStartedEventHandler() {
        EVENT_BUS.addHandler(PhenotypeExecuteStartedEvent.TYPE,
                new PhenotypeExecuteStartedEventHandler() {

                    @Override
                    public void onPhenotypeExecuteStart(
                            PhenotypeExecuteStartedEvent phenotypeExecuteStartedEvent) {
                        // Set the busy indicator to show while executing the
                        // phenotype.

                        // Need to send in the overall layout so the whole
                        // screen is greyed out.
                        i_busyIndicator = new ModalWindow(i_overallLayout, 40, "#dedede");
                        i_busyIndicator.setLoadingIcon("loading_circle.gif");
                        i_busyIndicator.show("Executing Phenotype...", true);
                    }
                });
    }

    /**
     * Create a handler to listen for a upload started event.
     */
    private void createUploadStartedEvent() {
        EVENT_BUS.addHandler(UploadStartedEvent.TYPE, new UploadStartedEventHandler() {

            @Override
            public void onUploadStarted(UploadStartedEvent uploadStartedEvent) {
                // Set the busy indicator to show while uploading the
                // algorithm.

                // Need to send in the overall layout so the whole
                // screen is greyed out.
                i_busyIndicator = new ModalWindow(i_overallLayout, 40, "#dedede");
                i_busyIndicator.setLoadingIcon("loading_circle.gif");
                i_busyIndicator.show("Uploading Algorithm...", true);
            }
        });
    }

    /**
     * Create a handler to listen for a upload completed event.
     */
    private void createUploadCompletedEvent() {
        EVENT_BUS.addHandler(UploadCompletedEvent.TYPE, new UploadCompletedEventHandler() {

            @Override
            public void onUploadCompleted(UploadCompletedEvent uploadCompletedEvent) {
                i_contextAreaPanel.setCurrentContextArea(getPhenotypePanel());

                // refresh the navigation tree
                i_phenotypePanel.refreshTreeNavigation();

                // hide the busy indicator.
                i_busyIndicator.hide();
            }
        });
    }

    /**
     * Create a handler to listen for a upload cancel button.
     */
    private void createUploadCanceledEvent() {
        EVENT_BUS.addHandler(UploadCanceledEvent.TYPE, new UploadCanceledEventHandler() {

            @Override
            public void onUploadCanceled(UploadCanceledEvent uploadCanceledEvent) {
                i_contextAreaPanel.setCurrentContextArea(getPhenotypePanel());
            }
        });
    }

    /**
     * If the session is valid, then make sure we show the user as logged in.
     * This is called when the user refreshes the browser.
     */
    private void checkServerForValidSession() {

        AuthenticationServiceAsync authenticationService = GWT.create(AuthenticationService.class);

        // test if the session is valid
        authenticationService.isValidSession(new AsyncCallback<User>() {

            @Override
            public void onSuccess(User user) {
                i_loggedInUser = user;
                i_headerMenu.updateUserInfo(user);
                i_navigationHeader.updateUserInfo(user);
            }

            @Override
            public void onFailure(Throwable caught) {

                lgr.log(Level.INFO, "checkServerForValidSession() error" + caught);
            }
        });
    }

    /**
     * Get the instance of the welcome panel. Only create one instance of it.
     * 
     * @return i_welcomePanel
     */
    private Layout getWelcomePanel() {
        if (i_welcomePanel == null) {
            i_welcomePanel = new WelcomePanel();
        }

        return i_welcomePanel;
    }

    /**
     * Get the instance of the phenotype panel. Only create one instance of it.
     * 
     * @return i_welcomePanel
     */
    private Layout getPhenotypePanel() {
        if (i_phenotypePanel == null) {
            i_phenotypePanel = new PhenotypePanel();
        }

        return i_phenotypePanel;
    }

    /**
     * Get the instance of the upload panel. Only create one instance of it.
     * 
     * @return i_uploadContainer
     */
    private Layout getUploadContainer() {
        if (i_uploadContainer == null) {
            i_uploadContainer = new UploadContainer();
        }

        return i_uploadContainer;
    }

    private Layout getUserManualPanel() {
        if (i_userManualPanel == null) {
            i_userManualPanel = new UserManual();
        }

        return i_userManualPanel;
    }

    /**
     * Get the instance of the admin panel. Only create one instance of it.
     * 
     * @return i_adminPanel
     */
    private Layout getAdminPanel() {
        if (i_adminPanel == null) {
            i_adminPanel = new AdminPanel();
        }

        return i_adminPanel;
    }

    /**
     * Always reset the profile panel.
     * 
     * @return
     */
    private Layout getProfilePanel() {
        i_profilePanel = null;
        i_profilePanel = new ProfilePanel();

        return i_profilePanel;
    }

    /**
     * Get the logged in user's User object. Should check for null before using
     * the returned object.
     * 
     * @return
     */
    public static User getLoggedInUser() {
        return i_loggedInUser;
    }

}
