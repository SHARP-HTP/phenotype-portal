package edu.mayo.phenoportal.client.dashboard;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.CloseClickEvent;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.TabSet;

public class DashboardWindow extends Window {

    private static final String TITLE = "Database Statistics";

    private Button i_closeButton;
    private final TabSet i_tabSet;

    public DashboardWindow() {
        super();

        VLayout layout = new VLayout(5);

        setWidth(800);
        setHeight(550);
        setMargin(5);

        setTitle(TITLE);
        setShowMinimizeButton(false);
        setIsModal(true);
        setShowModalMask(true);
        setCanDragResize(true);
        centerInPage();

        i_tabSet = new DashboardTabSet(true);
        addItem(i_tabSet);

        addCloseClickHandler(new CloseClickHandler() {

            @Override
            public void onCloseClick(CloseClickEvent event) {
                destroy();
            }
        });

        layout.addMember(getButtons());

        addItem(layout);
        show();
    }

    private HLayout getButtons() {

        // Buttons on the bottom
        HLayout buttonLayout = new HLayout();
        buttonLayout.setWidth100();
        buttonLayout.setHeight(25);
        buttonLayout.setLayoutMargin(6);
        buttonLayout.setMembersMargin(5);
        buttonLayout.setAlign(Alignment.RIGHT);

        i_closeButton = new Button("Close");
        i_closeButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                // close the window
                destroy();
            }
        });

        buttonLayout.addMember(i_closeButton);

        return buttonLayout;
    }

    public Button getCloseButton() {
        return i_closeButton;
    }

}
