package edu.mayo.phenoportal.client.admin;

import com.smartgwt.client.widgets.tab.Tab;

public class UserTab extends Tab implements AdminTab {

    private final UserListGrid i_userListGrid;

    public UserTab(String title) {
        super(title);

        i_userListGrid = new UserListGrid();
        setPane(i_userListGrid);
    }

    @Override
    public String getTabDescription() {
        return "Displays a list of registered users.  Administrators can double click on a row and edit the data.  "
                + "When finished, click on another row to lose focus.  The User Id is not editable.  "
                + "Changes are made immediately and are not reversible.  You can also delete a user by clicking on the minus icon on the right.";
    }

    @Override
    public void updateContents() {

        // invalidate the cache to force the datasource to fetch the data again.
        i_userListGrid.invalidateCache();
        i_userListGrid.getDataSource().setTestData(null);
        i_userListGrid.getDataSource().invalidateCache();
    }
}
