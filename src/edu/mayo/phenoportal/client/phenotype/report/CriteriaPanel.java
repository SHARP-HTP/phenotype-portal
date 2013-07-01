package edu.mayo.phenoportal.client.phenotype.report;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import mayo.edu.cts2.editor.client.Cts2Editor;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.HTMLPane;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;

import edu.mayo.phenoportal.client.core.AlgorithmData;
import edu.mayo.phenoportal.client.phenotype.PhenotypeService;
import edu.mayo.phenoportal.client.phenotype.PhenotypeServiceAsync;

/**
 * Panel for holding all contents for the criteria. This panel can be added to
 * any container (tabs, windows,...)
 */
public class CriteriaPanel extends VLayout {

    private static final String TITLE_CRITERIA = "Population Criteria";
    private static final String TITLE_DATA_CRITERIA = "Data criteria";
    private static final String TITLE_SUPPLEMENTAL_DATA_ELEMENTS = "Supplemental Data Elements";

    private AlgorithmData i_algorithmData;
    private HTMLPane i_htmlPane;

    private SectionStack i_criteriaSectionStack;
    private SectionStackSection i_criteriaSection;
    private SectionStackSection i_dataCriteriaSection;
    private SectionStackSection i_supplementalDataElementsSection;

    public CriteriaPanel() {
        super();

        createStack();
    }

    public void createStack() {

        removeMembers(getChildren());

        i_criteriaSectionStack = null;
        i_criteriaSection = null;
        i_dataCriteriaSection = null;
        i_htmlPane = null;

        // create the SectionStack
        i_criteriaSectionStack = new SectionStack();
        i_criteriaSectionStack.setVisibilityMode(VisibilityMode.MULTIPLE);
        i_criteriaSectionStack.setWidth100();
        i_criteriaSectionStack.setHeight100();

        // create the SectionStackSections
        i_criteriaSection = new SectionStackSection(TITLE_CRITERIA);
        i_criteriaSection.setExpanded(true);
        i_htmlPane = new HTMLPane();
        i_htmlPane.setWidth100();
        i_htmlPane.setHeight100();
        i_criteriaSection.addItem(i_htmlPane);

        i_dataCriteriaSection = new SectionStackSection(TITLE_DATA_CRITERIA);
        i_supplementalDataElementsSection = new SectionStackSection(
                TITLE_SUPPLEMENTAL_DATA_ELEMENTS);

        // add the SectionStackSections to the SectionStack
        i_criteriaSectionStack.addSection(i_criteriaSection);
        i_criteriaSectionStack.addSection(i_dataCriteriaSection);
        i_criteriaSectionStack.addSection(i_supplementalDataElementsSection);

        addMember(i_criteriaSectionStack);
    }

    /**
     * Retrieve the new algorithm criteria info from the DB and display it.
     * 
     * @param algorithmData
     */
    public void updateSelection(AlgorithmData algorithmData) {
        i_algorithmData = algorithmData;
        setCriteriaInfo();
    }

    public void setCriteriaInfo() {

        createStack();

        PhenotypeServiceAsync async = (PhenotypeServiceAsync) GWT.create(PhenotypeService.class);

        async.getPopulationCriteria(i_algorithmData, new AsyncCallback<String>() {
            @Override
            public void onSuccess(String result) {
                i_htmlPane.setContents(result);
            }

            @Override
            public void onFailure(Throwable caught) {
                GWT.log("Error getting criteria: " + caught);
            }
        });
        async.getDataCriteriaOids(i_algorithmData, new AsyncCallback<Map<String, String>>() {
            @Override
            public void onSuccess(Map<String, String> result) {
                Cts2Editor editor = new Cts2Editor();
	            List<String> oids = new ArrayList(result.size());
	            oids.addAll(result.keySet());
                i_dataCriteriaSection.addItem(editor.getMainLayout(oids));
            }

            @Override
            public void onFailure(Throwable caught) {
                GWT.log("Error getting criteria: " + caught);
            }
        });
        async.getSupplementalCriteriaOids(i_algorithmData, new AsyncCallback<Map<String, String>>() {
            @Override
            public void onSuccess(Map<String, String> result) {
                Cts2Editor editor = new Cts2Editor();
	            List<String> oids = new ArrayList<String>(result.size());
	            oids.addAll(result.keySet());
                i_supplementalDataElementsSection.addItem(editor.getMainLayout(oids));
            }

            @Override
            public void onFailure(Throwable caught) {
                GWT.log("Error getting criteria: " + caught);
            }
        });
    }

    public HTMLPane getHtmlPane() {
        return i_htmlPane;
    }

}
