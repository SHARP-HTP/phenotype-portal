package edu.mayo.phenoportal.client.admin;

import com.smartgwt.client.widgets.tab.Tab;

public class CategoryTab extends Tab implements AdminTab {

    public CategoryTab(String title) {
        super(title);
    }

    @Override
    public String getTabDescription() {
        return "Displays the current Phenotype categories and the ability to delete Phenotypes.";
    }

    @Override
    public void updateContents() {

    }
}
