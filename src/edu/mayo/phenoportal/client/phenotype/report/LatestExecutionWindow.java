package edu.mayo.phenoportal.client.phenotype.report;

import com.google.gwt.user.client.ui.HTMLPanel;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Autofit;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.layout.HLayout;
import edu.mayo.phenoportal.shared.ValueSet;

import java.util.List;

public class LatestExecutionWindow extends Window {

	public LatestExecutionWindow(List<ValueSet> valueSetList) {
		super();
		setTitle("Last Execution: Value Set Details");
		setWidth(800);
		setHeight(525);
		setModalMaskOpacity(90);
		setAlign(Alignment.CENTER);

		setCanDragReposition(true);
		setCanDragResize(true);
		setAnimateMinimize(true);

		HTMLPanel header = new HTMLPanel("<b>The latest execution was ran with these value sets.</b>");
		header.setWidth("100%");
		HLayout headerLayout = new HLayout();
		headerLayout.setWidth100();
		headerLayout.setHeight(25);
		headerLayout.setLayoutAlign(Alignment.CENTER);
		headerLayout.setAlign(VerticalAlignment.CENTER);
		headerLayout.addMember(header);

		ExecValueSetGrid grid = new ExecValueSetGrid(valueSetList);

		Button cancelButton = new Button("Close");
		cancelButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent clickEvent) {
				destroy();
			}
		});

		HLayout buttonLayout = new HLayout(5);
		buttonLayout.setHeight(25);
		buttonLayout.setMargin(10);
		buttonLayout.setAlign(Alignment.CENTER);
		buttonLayout.addMember(cancelButton);

		addItem(headerLayout);
		addItem(grid);
		addItem(buttonLayout);
	}

	private class ExecValueSetGrid extends ListGrid {
		public ExecValueSetGrid(List<ValueSet> valueSetList) {
			super();
			setWidth100();
			setAutoHeight();
			setMargin(2);
			setShowAllRecords(true);
			setAutoFitData(Autofit.BOTH);
			setShowRecordComponents(true);
			setShowRecordComponentsByCell(true);

			ListGridField descField = new ListGridField("desc", "Description");
			descField.setWidth("70%");
			ListGridField versionField = new ListGridField("version", "Version Executed");
			versionField.setWidth("30%");

			setFields(descField, versionField);

			setLoadingMessage("Loading Value Sets...");

			Record[] records = new Record[valueSetList.size()];
			int i = 0;
			for (ValueSet vs : valueSetList) {
				Record record = new Record();
				record.setAttribute("desc", vs.description != null ? vs.description : "");
				record.setAttribute("version", vs.comment);
				records[i++] = record;
			}
			setData(records);
			redraw();
		}
	}

}
