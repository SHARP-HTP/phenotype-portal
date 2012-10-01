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

        // layout to hold the upload panel
        i_uploadPanel = new UploadPanel();
        addMember(i_uploadPanel);

    }

}
