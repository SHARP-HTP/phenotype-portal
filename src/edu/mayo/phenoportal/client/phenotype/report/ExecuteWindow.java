package edu.mayo.phenoportal.client.phenotype.report;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import edu.mayo.phenoportal.client.Htp;
import edu.mayo.phenoportal.client.core.AlgorithmData;
import edu.mayo.phenoportal.client.events.PhenotypeExecuteCompletedEvent;
import edu.mayo.phenoportal.client.events.PhenotypeExecuteStartedEvent;
import edu.mayo.phenoportal.client.phenotype.PhenotypeService;
import edu.mayo.phenoportal.client.phenotype.PhenotypeServiceAsync;
import edu.mayo.phenoportal.client.utils.MessageWindow;
import edu.mayo.phenoportal.shared.Demographic;
import edu.mayo.phenoportal.shared.DemographicStat;
import edu.mayo.phenoportal.shared.DemographicsCategory;
import edu.mayo.phenoportal.shared.Execution;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExecuteWindow extends Window {

	private final Logger logger = Logger.getLogger(ExecuteWindow.class.getName());

	public ExecuteWindow(final AlgorithmData algorithmData, final Date fromDate, final Date toDate) {
		super();
		setTitle("Value Set Version Selection For Execution");
		setWidth(800);
		setHeight("95%");
		setModalMaskOpacity(90);
		setAlign(Alignment.CENTER);

		setCanDragReposition(true);
		setCanDragResize(true);
		setAnimateMinimize(true);

		HTMLPanel header = new HTMLPanel("<a style='text-align:center;'><b>Select the version of each value set for execution.</b></a>");
		header.setWidth("100%");
		HLayout headerLayout = new HLayout();
		headerLayout.setWidth100();
		headerLayout.setHeight(25);
		headerLayout.setLayoutAlign(Alignment.CENTER);
		headerLayout.setAlign(VerticalAlignment.CENTER);
		headerLayout.addMember(header);

		HTMLPanel dataCriteria = new HTMLPanel("<b>Data Criteria (QDM Data Elements)</b>");
		dataCriteria.setWidth("100%");
		HLayout dataCriteriaLayout = new HLayout();
		dataCriteriaLayout.setWidth100();
		dataCriteriaLayout.setHeight(25);
		dataCriteriaLayout.setLayoutAlign(Alignment.LEFT);
		dataCriteriaLayout.setAlign(VerticalAlignment.CENTER);
		dataCriteriaLayout.setLayoutTopMargin(20);
		dataCriteriaLayout.addMember(dataCriteria);
		DataCriteriaListGrid dataCriteriaListGrid = new DataCriteriaListGrid();
		dataCriteriaListGrid.setGridData(algorithmData);

		HTMLPanel suppDataCriteria = new HTMLPanel("<b>Supplemental Data Elements</b>");
		suppDataCriteria.setWidth("100%");
		HLayout suppDataCriteriaLayout = new HLayout();
		suppDataCriteriaLayout.setWidth100();
		suppDataCriteriaLayout.setHeight(25);
		suppDataCriteriaLayout.setLayoutAlign(Alignment.LEFT);
		suppDataCriteriaLayout.setAlign(VerticalAlignment.CENTER);
		suppDataCriteriaLayout.setLayoutTopMargin(20);
		suppDataCriteriaLayout.addMember(suppDataCriteria);
		SupplementalDataListGrid supplementalDataListGrid = new SupplementalDataListGrid();
		supplementalDataListGrid.setGridData(algorithmData);

		Button cancelButton = new Button("Cancel");
		cancelButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent clickEvent) {
				destroy();
			}
		});

		Button executeButton = new Button("Execute");
		executeButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				executePhenotype(algorithmData, fromDate, toDate);
			}
		});

		HLayout buttonLayout = new HLayout(5);
		buttonLayout.setHeight(25);
		buttonLayout.setMargin(10);
		buttonLayout.setLayoutTopMargin(20);
		buttonLayout.setAlign(Alignment.CENTER);
		buttonLayout.addMember(executeButton);
		buttonLayout.addMember(cancelButton);

		addItem(headerLayout);
		addItem(dataCriteriaLayout);
		addItem(dataCriteriaListGrid);
		addItem(suppDataCriteriaLayout);
		addItem(supplementalDataListGrid);
		addItem(buttonLayout);
	}

	private void executePhenotype(AlgorithmData algorithmData, Date fromDate, Date toDate) {

		Htp.EVENT_BUS.fireEvent(new PhenotypeExecuteStartedEvent());

		PhenotypeServiceAsync phenotypeService = GWT.create(PhenotypeService.class);
		phenotypeService.executePhenotype(algorithmData, fromDate, toDate, Htp
		  .getLoggedInUser().getUserName(), new AsyncCallback<Execution>() {

			@Override
			public void onSuccess(Execution result) {

				boolean printDebug = true;
				if (printDebug) {
					printExecutionResults(result);
				}

				if (result.isError()) {
					SC.warn("An error occurred while executing the algorithm.");
				} else {
					String title = "Phenotype Execution Complete";
					String message = "The Phenotype execution is complete.  You can view the results in the Summary, Demographics and WorkFlow tabs.";
					MessageWindow messageWindow = new MessageWindow(title, message);
					messageWindow.show();
				}

				Htp.EVENT_BUS.fireEvent(new PhenotypeExecuteCompletedEvent(true, result));
			}

			@Override
			public void onFailure(Throwable caught) {
				logger.log(Level.WARNING, "Phenotype execution has failed.", caught);

				String title = "Phenotype Execution Failed";
				String message = "The Phenotype execution has failed. If this continues, please contact support.";
				MessageWindow messageWindow = new MessageWindow(title, message);
				messageWindow.show();

				Htp.EVENT_BUS.fireEvent(new PhenotypeExecuteCompletedEvent(false));
			}

		});
		destroy();
	}

	private void printExecutionResults(Execution result) {
		if (result != null) {
			List<Demographic> demographics = result.getDemographics();
			if (demographics != null) {
				for (int i = 0; i < demographics.size(); i++) {

					Demographic demoIterate = demographics.get(i);
					System.out.println("Type:" + demoIterate.getType());
					for (int k = 0; k < demoIterate.getDemoCategoryList().size(); k++) {
						DemographicsCategory category1 = demoIterate.getDemoCategoryList().get(k);
						List<DemographicStat> statlist1 = category1.getDemoStatList();

						for (int l = 0; l < statlist1.size(); l++) {
							DemographicStat statValue = statlist1.get(l);
							System.out.println("Label" + statValue.getLabel());
							System.out.println("Value" + statValue.getValue());
						}

					}
				}
			}

		}
	}

}
