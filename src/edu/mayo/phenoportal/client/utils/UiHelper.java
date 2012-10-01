package edu.mayo.phenoportal.client.utils;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.Layout;

/**
 * Helper class for common UI features.
 */
public class UiHelper {

    /**
     * Set the Layout to have a border.
     * 
     * @param layout
     * @return
     */
    public static Layout createLayoutWithBorders(Layout layout) {

        layout.setShowEdges(true);
        layout.setEdgeShowCenter(true);

        layout.setEdgeImage("corners/glow_35.png");
        layout.setEdgeSize(10);
        layout.setEdgeOffset(10);

        return layout;

    }

    /**
     * Create a Label with a border.
     * 
     * @param layout
     * @return
     */
    public static Label createLabelWithBorders(Label label) {
        if (label == null) {
            label = new Label();
        }
        label.setShowEdges(true);
        label.setEdgeShowCenter(true);
        label.setEdgeImage("corners/glow_35.png");
        label.setEdgeSize(10);
        label.setEdgeOffset(10);
        return label;
    }

    /**
     * Create a label.
     * 
     * @param textToShow
     * @return
     */
    public static Label createLabel(String textToShow, Alignment alignment) {

        Label label = new Label();
        label.setHeight(40);
        label.setWidth100();
        label.setPadding(0);
        label.setMargin(10);

        label.setAlign(alignment);
        label.setValign(VerticalAlignment.CENTER);
        label.setContents(textToShow);
        label.setWrap(false);

        return label;
    }

    public static String getFormattedLabelText(String title) {
        // add css inline here
        String titleWithDecoration = "<b style=\"color: #51524e;font-family: Arial,Helvetica,sans-serif;font-size: 20px;font-weight:bold;text-decoration:none\">"
                + title + "</b>";

        return titleWithDecoration;
    }

}
