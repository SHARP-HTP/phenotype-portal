package edu.mayo.phenoportal.client.phenotype.report;

import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.HTMLPane;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;

import edu.mayo.phenoportal.client.core.AlgorithmData;

/**
 * Tab for displaying the Criteria info for the selected phenotype.
 */
public class CriteriaTab extends Tab implements ReportTab {

    private AlgorithmData i_algorithmData;
    private final VLayout i_criteriaLayout;
    // private CriteriaPanel i_criteriaPanel;
    private final CriteriaHTMLPage i_criteriaHTMLPage;
    private final DataCriteriaListGrid i_dataCriteriaListGrid;
    private final SupplementalDataListGrid i_supplementalDataListGrid;

    public CriteriaTab(String title) {
        super(title);

        // overall layout that holds everything in the criteria tab.
        i_criteriaLayout = new VLayout();
        i_criteriaLayout.setWidth100();
        i_criteriaLayout.setHeight100();

        i_criteriaHTMLPage = new CriteriaHTMLPage();
        i_dataCriteriaListGrid = new DataCriteriaListGrid();
        i_supplementalDataListGrid = new SupplementalDataListGrid();

        HTMLPane dataCriteriaTitle = getHTMLTitle("Data Criteria (QDM Data Elements)");
        HTMLPane supplementalDataTitle = getHTMLTitle("Supplemental Data Elements");

        i_criteriaLayout.addMember(i_criteriaHTMLPage);
        i_criteriaLayout.addMember(dataCriteriaTitle);
        i_criteriaLayout.addMember(i_dataCriteriaListGrid);
        i_criteriaLayout.addMember(supplementalDataTitle);
        i_criteriaLayout.addMember(i_supplementalDataListGrid);

        setPane(i_criteriaLayout);
    }

    /**
     * Retrieve the new algorithm criteria info from the DB and display it.
     * 
     * @param algorithmData
     */
    public void updateSelection(AlgorithmData algorithmData) {
        i_algorithmData = algorithmData;
        i_criteriaHTMLPage.udpateHTMLPage(i_algorithmData);
        i_dataCriteriaListGrid.update(i_algorithmData);
        i_supplementalDataListGrid.update(i_algorithmData);
    }

    @Override
    public void clearTab() {
        // i_criteriaPanel.getHtmlPane().setContents("");
    }

    private HTMLPane getHTMLTitle(String title) {
        HTMLPane htmlTitle = new HTMLPane();
        htmlTitle.setContents("</hr><div></br> <b>" + title + "<b></div>");
        htmlTitle.setWidth100();
        htmlTitle.setHeight100();
        htmlTitle.setOverflow(Overflow.VISIBLE);

        return htmlTitle;
    }
}
