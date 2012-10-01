package edu.mayo.phenoportal.server.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.w3c.dom.Comment;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * @author m099393
 * This Class has helper functions to Parse the XML Document. 
 */
public class XmlHelper {

	public static Node getFirstChildByTagName(Node parent, String tagName) {
		NodeList nodeList = parent.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE
					&& node.getNodeName().equalsIgnoreCase(tagName))
				return node;
		}
		return null;
	}

	public static List<Node> getChildrenByTagName(Node parent, String tagName) {
		List<Node> eleList = new ArrayList<Node>();
		NodeList nodeList = parent.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE
					&& node.getNodeName().equalsIgnoreCase(tagName)) {
				eleList.add(node);
			}

		}
		return eleList;
	}

	/**
	 * this function will go recursively deep to find the node with the name
	 * 
	 * @param n
	 * @param name
	 * @return
	 */
	public static Node getNodeByName(Node n, String name) {
		NodeList l = n.getChildNodes();
		//System.out.println("NodeList length:" + l.getLength());
		if (l == null)
			return null;

		for (int i = 0; i < l.getLength(); i++) {
			if (l.item(i).getNodeName().equals(name))
				return l.item(i);
			else {
				Node n2 = getNodeByName(l.item(i), name);
				if (n2 != null) {
					return n2;
				}
			}
		}
		return null;
	}

	/**
	 * this function is different from above one. Though we still aim at finding
	 * the node with the name. But we don't want to surpass some line. Say, if
	 * the current Node is sourceOf, we don't want to go down to another
	 * sourceOf which is under the current sourceOf.
	 * 
	 * @param n
	 * @param name
	 * @return
	 */

	public static Node getNextHomoSibling(Node aNode) {
		Node nextSibling = aNode;
		while ((nextSibling = nextSibling.getNextSibling()) != null) {
			if (nextSibling.getNodeName().equals(aNode.getNodeName())) {
				return nextSibling;
			}
		}
		return null;
	}

	/**
	 * aims at finding the location of the current node of its kind. the idea is
	 * to count how many previous siblings does it have,
	 * 
	 * @param aNode
	 * @return
	 */
	public static int getLocHomoSibling(Node aNode) {
		int countOfBranch = 0;
		Node preSibling = aNode;
		while ((preSibling = preSibling.getPreviousSibling()) != null) {
			if (preSibling.getNodeName().equals(aNode.getNodeName())) {
				countOfBranch++;
			}
		}
		return countOfBranch;
	}

	public static Node getPreHomoSibling(Node aNode) {
		Node preSibling = aNode;
		while ((preSibling = preSibling.getPreviousSibling()) != null) {
			if (preSibling.getNodeName().equals(aNode.getNodeName())) {
				return preSibling;
			}
		}
		return null;
	}

	/**
	 * 
	 * @param n
	 * @param name
	 * @param sbStrings
	 *            this function aims at grabbing all title strings under current
	 *            sourceOfNode.
	 * @return
	 */
	public static String getNodeStringsByName(Node n, String name,
			StringBuffer sbStrings) {
		NodeList l = n.getChildNodes();
		if (l == null)
			return sbStrings.toString();

		for (int i = 0; i < l.getLength(); i++) {
			if (l.item(i).getNodeName().equals(name)) {
				sbStrings.append(l.item(i).getTextContent() + " ");
			} else {
				getNodeStringsByName(l.item(i), name, sbStrings);
			}
		}
		return sbStrings.toString();
	}

	public static List<Comment> getCommentNode(Node n) {
		List<Comment> commentList = new ArrayList<Comment>();
		NodeList nodeList = n.getChildNodes();
		if (nodeList == null)
			return null;
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node ithNode = nodeList.item(i);
			if (ithNode instanceof Comment) {
				commentList.add((Comment) ithNode);
			}
		}
		return commentList;
	}

	public static String encode(String s) {
		String retStr = s;

		if (s.indexOf("<") >= 0)
			retStr = s.replaceAll("<", "&lt;");
		else if (s.indexOf(">") >= 0)
			retStr = s.replaceAll(">", "&gt;");
		else if (s.indexOf("&") >= 0)
			retStr = s.replaceAll("&", "&amp;");
		else if (s.indexOf("\"") >= 0)
			retStr = s.replaceAll("\"", "&quot;");
		else if (s.indexOf("'") >= 0)
			retStr = s.replaceAll("\'", "&apos;");
		return retStr;
	}

	/**
	 * Returns the string where all non-ascii and <, &, > are encoded as numeric
	 * entities. I.e. "&lt;A &amp; B &gt;" .... (insert result here). The result
	 * is safe to include anywhere in a text field in an XML-string. If there
	 * was no characters to protect, the original string is returned.
	 * 
	 * @param originalUnprotectedString
	 *            original string which may contain characters either reserved
	 *            in XML or with different representation in different encodings
	 *            (like 8859-1 and UFT-8)
	 * @return
	 */
	public static String protectSpecialCharacters(
			String originalUnprotectedString) {
		if (originalUnprotectedString == null) {
			return null;
		}
		boolean anyCharactersProtected = false;

		StringBuffer stringBuffer = new StringBuffer();
		for (int i = 0; i < originalUnprotectedString.length(); i++) {
			char ch = originalUnprotectedString.charAt(i);

			boolean controlCharacter = ch < 32;
			boolean unicodeButNotAscii = ch > 126;
			boolean characterWithSpecialMeaningInXML = ch == '<' || ch == '&'
					|| ch == '>';

			if (characterWithSpecialMeaningInXML || unicodeButNotAscii
					|| controlCharacter) {
				stringBuffer.append("&#" + (int) ch + ";");
				anyCharactersProtected = true;
			} else {
				stringBuffer.append(ch);
			}
		}
		if (anyCharactersProtected == false) {
			return originalUnprotectedString;
		}

		return stringBuffer.toString();
	}

	/**
	 * in this NQF project, the root node is QualityMeasureDocument
	 * 
	 * @param aNode
	 * @param rootNode
	 * @return the distance a node to the rootnode
	 */
	public static int getHeight(Node aNode, Node rootNode) {
		int height = 0;
		if (aNode == null) {
			return 0;
		}
		while (aNode.getParentNode() != rootNode) {
			aNode = aNode.getParentNode();
			height++;
		}
		return height;
	}

	public static List<Node> getNodeListByName(Node node, String name,
			List<Node> finalNodeList) {
		NodeList nodeList = node.getChildNodes();
		System.out.println("child node name: " + node.getNodeName());
		if (nodeList == null)
			return null;

		for (int i = 0; i < nodeList.getLength(); i++) {
			Node candNode = nodeList.item(i);
			if (candNode.getNodeName().equals(name)) {
				finalNodeList.add(candNode);
			}

			getNodeListByName(nodeList.item(i), name, finalNodeList);

		}
		return finalNodeList;
	}

	public static int countOccurence(List<Integer> valueList, int value) {
		int count = 0;
		Iterator<Integer> iterValueList = valueList.iterator();
		while (iterValueList.hasNext()) {
			int vaInList = iterValueList.next();
			if (vaInList == value) {
				count++;
			}
		}
		return count;
	}

}
