package edu.mayo.phenoportal.client.admin;

public interface AdminTab {

    /**
     * Provide a description of what this tab does.
     * 
     * @return
     */
    public String getTabDescription();

    /**
     * Provide a way to update the contents of the tab when it's selected.
     */
    public void updateContents();
}
