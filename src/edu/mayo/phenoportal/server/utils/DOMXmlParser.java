package edu.mayo.phenoportal.server.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import edu.mayo.phenoportal.server.upload.FileServiceImpl;
import edu.mayo.phenoportal.shared.Demographic;
import edu.mayo.phenoportal.shared.DemographicStat;
import edu.mayo.phenoportal.shared.DemographicsCategory;

/**
 * @author m099393 This class is used to parse the xml file returned back from
 *         server
 */
public class DOMXmlParser {

    private static final String DEMOGRAPHIC_TYPE = "DemographicType";
    private static final String DEMOGRAPHIC_CATEGORY = "DemographicCategory";
    private static final String DEMOGRAPHIC_STATISTICS = "DemographicStat";
    private static final String LABEL = "label";
    private static final String VALUE = "value";
    private static final String NAME = "name";

    /**
     * Parse the xml from a File.
     * 
     * @param file
     * @return
     */
    public List<Demographic> parseXmlFromFile(File file) {

        Document doc = null;
        try {

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();
        } catch (Exception ex) {
            Logger lgr = Logger.getLogger(FileServiceImpl.class.getName());
            lgr.log(Level.SEVERE, "Failed to prase xml file" + ex.getStackTrace(), ex);
        }

        return getDemographics(doc);

    }

    /**
     * Parse the xml from a String.
     * 
     * @param xml
     * @return
     */
    public List<Demographic> parseXmlFromString(String xml) {
        Document doc = null;

        try {

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            ByteArrayInputStream bais = new ByteArrayInputStream(xml.getBytes("UTF-8"));

            doc = dBuilder.parse(bais);
            doc.getDocumentElement().normalize();
        } catch (Exception ex) {
            Logger lgr = Logger.getLogger(FileServiceImpl.class.getName());
            lgr.log(Level.SEVERE, "Failed to parse xml from string" + ex.getStackTrace(), ex);
        }

        return getDemographics(doc);

    }

    /**
     * Method to Parse the Xml file from server
     * 
     * @return
     */
    public List<Demographic> getDemographics(Document doc) {
        List<Demographic> demographicList = new ArrayList<Demographic>();

        if (doc == null) {
            return null;
        }

        try {

            // Retrieve the DemographicType by the tag name.
            NodeList typeList = doc.getElementsByTagName(DEMOGRAPHIC_TYPE);

            // iterate through the demographicType
            for (int tempType = 0; tempType < typeList.getLength(); tempType++) {

                int totalCategoryValue = 0;
                Node typeValue = typeList.item(tempType);

                // Get the type element.
                Node typeNode = XmlHelper.getFirstChildByTagName(typeValue, "type");

                // Get all the children by tag name: DemographicCategory into a
                // list
                List<Node> demoCatNodeList = XmlHelper.getChildrenByTagName(typeValue,
                        DEMOGRAPHIC_CATEGORY);

                // Crate the DemographicCategory list
                List<DemographicsCategory> demoCatList = new ArrayList<DemographicsCategory>();

                int categoryNodelength = demoCatNodeList.size();

                // Iterate over the children of DemographicCategory
                for (int j = 0; j < demoCatNodeList.size(); j++) {

                    // Create the DemographicsCategory object
                    DemographicsCategory demoCat = new DemographicsCategory();

                    Node ithDemoCatNode = demoCatNodeList.get(j);

                    // Get children by tag name - name
                    Node nameNode = XmlHelper.getFirstChildByTagName(ithDemoCatNode, NAME);

                    demoCat.setName(nameNode.getTextContent());

                    // Get all the children by the tag name DemographicStat
                    List<Node> demoStatsNodeList = XmlHelper.getChildrenByTagName(ithDemoCatNode,
                            DEMOGRAPHIC_STATISTICS);

                    // Create the demograpraphicsStatistics list
                    List<DemographicStat> statList = new ArrayList<DemographicStat>();

                    // Iterate over the demographicStat
                    for (int i = 0; i < demoStatsNodeList.size(); i++) {
                        DemographicStat demoStat = new DemographicStat();

                        // Retrieve label
                        Node labelNode = XmlHelper.getNodeByName(demoStatsNodeList.get(i), LABEL);

                        // Retrieve Value
                        Node valueNode = XmlHelper.getNodeByName(demoStatsNodeList.get(i), VALUE);

                        int value = Integer.parseInt(valueNode.getTextContent());

                        totalCategoryValue = totalCategoryValue + value;

                        // Create the DemographicStat object and set label and
                        // value
                        demoStat.setLabel(labelNode.getTextContent());
                        demoStat.setValue(Integer.parseInt(valueNode.getTextContent()));
                        // add the label and value to demoStat
                        statList.add(demoStat);
                    }

                    // Set the DemographicStat list of DemographicCategory
                    // object
                    demoCat.setDemoStatList(statList);
                    demoCatList.add(demoCat);

                }

                // Create and set the demographics object
                Demographic demographic = new Demographic();
                demographic.setType(typeNode.getTextContent());

                // Calculate the total population
                int totalValue = totalCategoryValue / categoryNodelength;

                demographic.setTotal(totalValue);
                demographic.setDemoCategoryList(demoCatList);
                demographicList.add(demographic);

            }

        }

        catch (Exception ex) {
            Logger lgr = Logger.getLogger(FileServiceImpl.class.getName());
            lgr.log(Level.SEVERE, "Failed to return demographics" + ex.getStackTrace(), ex);
        }
        return demographicList;
    }

}