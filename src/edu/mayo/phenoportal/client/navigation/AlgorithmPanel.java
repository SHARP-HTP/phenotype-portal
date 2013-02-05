package edu.mayo.phenoportal.client.navigation;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.user.client.Window;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;

import edu.mayo.phenoportal.client.Htp;
import edu.mayo.phenoportal.client.core.ContextAreas;
import edu.mayo.phenoportal.client.events.ContextAreaChangedEvent;
import edu.mayo.phenoportal.client.events.LoggedInEvent;
import edu.mayo.phenoportal.client.events.LoggedInEventHandler;
import edu.mayo.phenoportal.client.events.LoggedOutEvent;
import edu.mayo.phenoportal.client.events.LoggedOutEventHandler;
import edu.mayo.phenoportal.shared.User;

public class AlgorithmPanel extends VLayout {

    static Logger lgr = Logger.getLogger(AlgorithmPanel.class.getName());
    private static final String UPLOAD_TITLE = "Upload Phenotype";
    private static final String CREATE_TITLE = "Create Phenotype";

    private static final int BUTTON_WIDTH = 175;

    private static final String CREATE_URL = "https://www.emeasuretool.cms.gov/MeasureAuthoringTool/Login.html";

    private IButton i_createButton;
    private IButton i_uploadButton;

    public AlgorithmPanel() {
        super();

        init();

        createLoggedInEventHandler();
        createLoggedOutEventHandler();
    }

    private void init() {

        setMargin(25);
        setMembersMargin(15);

        i_createButton = new IButton(CREATE_TITLE);
        i_createButton.setWidth(BUTTON_WIDTH);
        i_createButton.setIcon("createPhenotype.png");
        i_createButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                Window.open(CREATE_URL, "mat", null);
            }
        });

        i_uploadButton = new IButton(UPLOAD_TITLE);
        i_uploadButton.setWidth(BUTTON_WIDTH);
        i_uploadButton.setIcon("uploadPhenotype.png");
        i_uploadButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                try {
                    Htp.EVENT_BUS.fireEvent(new ContextAreaChangedEvent(ContextAreas.types.UPLOAD));
                } catch (Exception e) {
                    lgr.log(Level.INFO, e.getMessage());
                    e.printStackTrace();
                }
            }
        });

        setEnablementBasedOnUserLoggedIn();

        addMember(i_createButton);
        addMember(i_uploadButton);
    }

    private void setEnablementBasedOnUserLoggedIn() {
        User user = Htp.getLoggedInUser();

        // Admin = 1, Execute = 2
        if (user != null && user.getRole() <= 2) {
            i_uploadButton.setDisabled(false);
        } else {
            i_uploadButton.setDisabled(true);
        }

    }

    /**
     * Create a handler to change Logged in/out button based on the log in state
     */
    private void createLoggedInEventHandler() {
        Htp.EVENT_BUS.addHandler(LoggedInEvent.TYPE, new LoggedInEventHandler() {

            @Override
            public void onLoggedIn(LoggedInEvent loggedInEvent) {
                setEnablementBasedOnUserLoggedIn();
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
                setEnablementBasedOnUserLoggedIn();
            }
        });
    }
}
