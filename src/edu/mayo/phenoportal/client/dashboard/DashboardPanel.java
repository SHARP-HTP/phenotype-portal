package edu.mayo.phenoportal.client.dashboard;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.TabSet;

import edu.mayo.phenoportal.client.Htp;

public class DashboardPanel extends VLayout {

    private static String BACKGROUND_COLOR = "#b0abdb";
    private TabSet i_tabSet;

    public DashboardPanel() {
        super();
        init();
    }

    private void init() {
        setWidth100();

        // take the remaining height available
        setHeight("*");
        setBackgroundColor(BACKGROUND_COLOR);

        // if we are in development/debug mode, then don't show the graphs.
        // The graphs cause javascript exceptions.
        if (!Htp.DEBUG_MODE) {
            i_tabSet = new DashboardTabSet(false);
            addMember(i_tabSet);
            addMember(createEnlargeButtonLayout());
        }
    }

    private Canvas createEnlargeButtonLayout() {

        HLayout buttonLayout = new HLayout();
        buttonLayout.setWidth100();
        buttonLayout.setHeight(25);
        buttonLayout.setLayoutMargin(3);
        buttonLayout.setMembersMargin(3);
        buttonLayout.setAlign(Alignment.RIGHT);

        IButton enlargeButton = new IButton("Open in New Window");
        enlargeButton.setIcon("arrow_out.png");
        enlargeButton.setWidth(170);
        enlargeButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                DashboardWindow dw = new DashboardWindow();
                dw.show();
            }
        });

        buttonLayout.addMember(enlargeButton);
        return buttonLayout;
    }
}
