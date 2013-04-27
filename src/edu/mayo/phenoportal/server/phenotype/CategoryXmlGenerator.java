package edu.mayo.phenoportal.server.phenotype;

import org.w3c.dom.Element;
import org.w3c.dom.Text;

import edu.mayo.phenoportal.server.utils.DOMXmlGenerator;

public class CategoryXmlGenerator extends DOMXmlGenerator {

	public static final String PHENOTYPE = "phenotype";
	public static final String ROOT = "List";
	public static final String CATEGORY_ID = "CategoryId";
	public static final String PHENOTYPE_NAME = "Name";
	public static final String PARENT_ID = "ParentId";
	public static final String PHENOTYPE_COUNT = "Count";
	public static final String PHENOTYPE_LEVEL = "Level";
	public static final String ID = "Id";
	public static final String ALGORITHM_NAME = "AlgoName";
	public static final String ALGORITHM_VERSION = "AlgoVersion";
	public static final String ALGORITHM_USER = "AlgoUser";
	public static final String ALGORITHM_DESCRIPTION = "AlgoDesc";
	
	/**
	 * The real workhorse which creates the XML structure and also Creates the
	 * parent element for XML
	 */

	public void createPhenotypeCategoriesDOMTree(int id, String name,
			int parentId, int count, int level) {

		Element phenotypeElement = i_document.createElement(PHENOTYPE);

		// categoryId
		Element categoryIDElement = i_document.createElement(CATEGORY_ID);
		Text categoryIdText = i_document.createTextNode(Integer.toString(id));
		categoryIDElement.appendChild(categoryIdText);
		phenotypeElement.appendChild(categoryIDElement);

		// PhenotypeName
		Element phenotypeNameElement = i_document.createElement(PHENOTYPE_NAME);
		Text phenotypeNameText = i_document.createTextNode(name);
		phenotypeNameElement.appendChild(phenotypeNameText);
		phenotypeElement.appendChild(phenotypeNameElement);

		// ParentId

		Element parentIdElement = i_document.createElement(PARENT_ID);
		Text parentIdText = i_document.createTextNode(Integer.toString(parentId));
		parentIdElement.appendChild(parentIdText);
		phenotypeElement.appendChild(parentIdElement);

		// PhenotypCount

		Element phenotypeCountElement = i_document.createElement(PHENOTYPE_COUNT);
		Text phenotypeCountText = i_document.createTextNode(Integer
				.toString(count));
		phenotypeCountElement.appendChild(phenotypeCountText);
		phenotypeElement.appendChild(phenotypeCountElement);

		// PhenotypeLabel

		Element phenotypeLabelElement = i_document.createElement(PHENOTYPE_LEVEL);
		Text phenotypeLabelText = i_document.createTextNode(Integer
				.toString(level));
		phenotypeLabelElement.appendChild(phenotypeLabelText);
		phenotypeElement.appendChild(phenotypeLabelElement);

		i_rootElement.appendChild(phenotypeElement);
		// return phenotypeElement;

	}

	public void createPhenotypeAlgorithmsDOMTree(int algorithmId, String categoryId, String parentId,
			int count, int level, String algorithmName, String algorithmDesc,
			String algorithmUser, String algorithmVersion) {

		Element phenotypeElement = i_document.createElement(PHENOTYPE);

		Element phenotypeIdElement = i_document.createElement("AlgoId");
		Text phenotypeIdText = i_document.createTextNode(Integer
		  .toString(algorithmId));
		phenotypeIdElement.appendChild(phenotypeIdText);
		phenotypeElement.appendChild(phenotypeIdElement);

		// categoryId
		Element categoryIDElement = i_document.createElement(CATEGORY_ID);
		Text categoryIdText = i_document.createTextNode(categoryId);
		categoryIDElement.appendChild(categoryIdText);
		phenotypeElement.appendChild(categoryIDElement);

		// Algorithm Name
		Element algorithmNameElement = i_document.createElement(PHENOTYPE_NAME);
		Text algorithmNameText = i_document.createTextNode(algorithmName);
		algorithmNameElement.appendChild(algorithmNameText);
		phenotypeElement.appendChild(algorithmNameElement);

		// ParentId
		Element parentIdElement = i_document.createElement(PARENT_ID);
		Text parentIdText = i_document.createTextNode(parentId);
		parentIdElement.appendChild(parentIdText);
		phenotypeElement.appendChild(parentIdElement);

		// PhenotypCount

		Element phenotypeCountElement = i_document.createElement(PHENOTYPE_COUNT);
		Text phenotypeCountText = i_document.createTextNode(Integer
				.toString(count));
		phenotypeCountElement.appendChild(phenotypeCountText);
		phenotypeElement.appendChild(phenotypeCountElement);

		// PhenotypeLevel

		Element phenotypeLabelElement = i_document.createElement(PHENOTYPE_LEVEL);
		Text phenotypeLabelText = i_document.createTextNode(Integer
				.toString(level));
		phenotypeLabelElement.appendChild(phenotypeLabelText);
		phenotypeElement.appendChild(phenotypeLabelElement);

		// Algorithm Version

		Element algorithmVersionElement = i_document
				.createElement(ALGORITHM_VERSION);
		Text algorithmVersionText = i_document.createTextNode(algorithmVersion);
		algorithmVersionElement.appendChild(algorithmVersionText);
		phenotypeElement.appendChild(algorithmVersionElement);

		// Algorithm User

		Element algorithmUserElement = i_document.createElement(ALGORITHM_USER);
		Text algorithmUserText = i_document.createTextNode(algorithmUser);
		algorithmUserElement.appendChild(algorithmUserText);
		phenotypeElement.appendChild(algorithmUserElement);

		// Algorithm Description

		Element algorithmDescriptionElement = i_document
				.createElement(ALGORITHM_DESCRIPTION);
		Text algorithmDescriptionText = i_document.createTextNode(algorithmDesc);
		algorithmDescriptionElement.appendChild(algorithmDescriptionText);
		phenotypeElement.appendChild(algorithmDescriptionElement);

		i_rootElement.appendChild(phenotypeElement);
		// return phenotypeElement;

	}
}
