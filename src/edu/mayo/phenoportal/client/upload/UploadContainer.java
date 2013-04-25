package edu.mayo.phenoportal.client.upload;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.layout.HLayout;

public class UploadContainer extends HLayout {

    protected static final String TAB_BACKGROUND_COLOR = "#f4f4fa";

    private UploadPanel i_uploadPanel;

    public UploadContainer() {
        super();
        init();
    }

    private void init() {
        setWidth100();
        setHeight100();
        setAlign(Alignment.CENTER);
        createUploadPanel();
    }

    private void createUploadPanel() {
        i_uploadPanel = new UploadPanel();
        addMember(i_uploadPanel);

    }

	public UploadPanel getUploadPanel() {
		return i_uploadPanel;
	}

	public void setNameText(String nameText) {
		this.i_uploadPanel.setNameText(nameText);
	}

	public void setVersionText(String versionText) {
		this.i_uploadPanel.setVersionText(versionText);
	}

	public void setInstitutionText(String institutionText) {
		this.i_uploadPanel.setInstitutionText(institutionText);
	}

	public void setDescriptionText(String descriptionText) {
		this.i_uploadPanel.setDescriptionText(descriptionText);
	}

	public void setAssocNameText(String assocNameText) {
		this.i_uploadPanel.setAssocNameText(assocNameText);
	}

	public void setAssocLinkText(String assocLinkText) {
		this.i_uploadPanel.setAssocLinkText(assocLinkText);
	}

	public void clearTextFields() {
		this.i_uploadPanel.clearTextFields();
	}

	public void setNqfFilePath(String path) {
		this.i_uploadPanel.setNqfFilePath(path);
	}

}
