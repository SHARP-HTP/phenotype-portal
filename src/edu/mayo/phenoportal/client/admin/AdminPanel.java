package edu.mayo.phenoportal.client.admin;

import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.events.TabSelectedEvent;
import com.smartgwt.client.widgets.tab.events.TabSelectedHandler;

/**
 * Panel for administration features.
 */
public class AdminPanel extends VLayout {

    private AdminInfoPanel i_infoPanel;
    private AdminTabSet i_adminTabSet;

    public AdminPanel() {
        super();

        init();
    }

    private void init() {

        this.setWidth100();
        this.setHeight100();

        i_infoPanel = new AdminInfoPanel();
        i_adminTabSet = new AdminTabSet();

        // update the info panel description based on the tab selected.
        i_adminTabSet.addTabSelectedHandler(new TabSelectedHandler() {

            @Override
            public void onTabSelected(TabSelectedEvent event) {
                AdminTab selectedTab = (AdminTab) i_adminTabSet.getSelectedTab();
                String description = selectedTab.getTabDescription();
                i_infoPanel.setDescription(description);

                selectedTab.updateContents();
            }
        });

        addMember(i_infoPanel);
        addMember(i_adminTabSet);

        i_adminTabSet.selectInitialTab();
    }

}