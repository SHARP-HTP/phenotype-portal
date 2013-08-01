package edu.mayo.phenoportal.client.phenotype.report;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.URL;
import com.smartgwt.client.types.ImageStyle;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.CloseClickEvent;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import edu.mayo.phenoportal.client.phenotype.PhenotypeService;
import edu.mayo.phenoportal.client.phenotype.PhenotypeServiceAsync;
import edu.mayo.phenoportal.shared.Image;

/**
 * Tab for displaying the Criteria info for the selected phenotype.
 */
public class WorkflowTab extends Tab implements ReportTab {

    private Canvas imagePanel;
    private Layout mainPanel;
    private Img wfImage;

    public WorkflowTab(String title) {
        super(title);
        createMainPanel();
    }

    private void createMainPanel() {
        wfImage = new Img();

        imagePanel = new Canvas();
        imagePanel.setWidth100();
        imagePanel.setHeight100();
        imagePanel.addChild(wfImage);

	    mainPanel = new VLayout();
	    mainPanel.addMember(imagePanel);

        setPane(mainPanel);
    }


    public void insertImage(Image image) {
        clearTab();

        if (image != null && image.getImagePath() != null) {
            wfImage = new Img();
            wfImage.setImageType(ImageStyle.NORMAL);
	        wfImage.setSrc(URL.encode(GWT.getModuleBaseURL() + "images?id=" + image.getImagePath()));
            wfImage.setDefaultWidth(image.getWidth());
            wfImage.setDefaultHeight(image.getHeight());
            imagePanel.setContents("");

            imagePanel.addChild(wfImage);
        } else {
            imagePanel.setContents("<b>There is no workflow image for this algorithm</b>");
        }
        imagePanel.markForRedraw();
    }

    @Override
    public void clearTab() {
        if (wfImage != null) {
            imagePanel.removeChild(wfImage);
            wfImage = null;
        }
    }

}
