package edu.mayo.phenoportal.client.navigation;

import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;


/**
 * SectionStack that contains a SectionStackSection for creating/uploading
 * algorithms and a SectionStackSection for the navigation tree.
 */
public class NavigationSectionStack extends SectionStack {

    private static final String ALGORITHM_TITLE = "Algorithms";
    private static final String TREE_TITLE = "Phenotypes";

    private SectionStackSection i_algorithmsSection;
    private AlgorithmPanel i_algorithmPanel;

    private SectionStackSection i_treeSection;
    private final NavigationTree i_navigationTree;

    public NavigationSectionStack(NavigationTree navigationTree) {
        super();

        i_navigationTree = navigationTree;
        init();

    }

    private void init() {
        setVisibilityMode(VisibilityMode.MULTIPLE);
        setWidth100();
        setHeight100();

        // create the algorithm section
        addAlgorithmsSection();

        // create the tree section
        addTreeSection();
    }

    private void addTreeSection() {
        i_treeSection = new SectionStackSection(TREE_TITLE);
        i_treeSection.setExpanded(true);
        i_treeSection.setCanCollapse(false);

        // add tree to the tree section
        i_treeSection.addItem(i_navigationTree);

        addSection(i_treeSection);
    }

    private void addAlgorithmsSection() {
        i_algorithmsSection = new SectionStackSection(ALGORITHM_TITLE);
        i_algorithmsSection.setExpanded(true);
        i_algorithmsSection.setCanCollapse(true);

        // create the algorithm panel and add it to this section.
        i_algorithmPanel = new AlgorithmPanel();
        i_algorithmsSection.addItem(i_algorithmPanel);

        addSection(i_algorithmsSection);
    }

    public void refreshTreeNavigation() {
        i_navigationTree.refreshTree();
    }

}
