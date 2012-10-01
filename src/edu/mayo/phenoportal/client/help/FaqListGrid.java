package edu.mayo.phenoportal.client.help;

import com.smartgwt.client.types.ExpansionMode;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * ListGrid to display the FAQ.
 */
public class FaqListGrid extends ListGrid {

    public FaqListGrid() {
        super();

        setWidth100();
        setHeight100();

        setCanExpandRecords(true);
        setExpansionMode(ExpansionMode.DETAIL_FIELD);

        // This is the field that is displayed when expanded.
        setDetailField("answer");

        ListGridField nameField = new ListGridField("title", "FAQ");
        setFields(nameField);

        setData(FaqData.getRecords());
        draw();
    }

    @Override
    protected Canvas getExpansionComponent(ListGridRecord record) {
        Canvas canvas = super.getExpansionComponent(record);
        canvas.setMargin(15);
        return canvas;
    }

}
