package se.flittermou.jsbp.core;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SBParser {
    private NodeList nodelist = null;

    public void parse(File file) {
        final String parentElementName = "artikel";

        if (nodelist == null) {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            try {
                DocumentBuilder documentBuilder = factory.newDocumentBuilder();
                Document parsedDocument = documentBuilder.parse(file);
                nodelist = parsedDocument.getDocumentElement().getElementsByTagName(parentElementName);
            } catch (ParserConfigurationException | SAXException | IOException e) {
                System.err.println(e);
            }
        }
    }

    public void reset() {
        nodelist = null;
        nodelistCounter = 0;
    }

    private int nodelistCounter = 0;

    public Map<String, String> getNextArticle() {
        if (nodelist == null) {
            throw new IllegalArgumentException("Nothing has been parsed yet.");
        }
        Map<String, String> map = new HashMap<>();

        if (nodelistCounter < nodelist.getLength()) {
            Node node = nodelist.item(nodelistCounter);
            NodeList children = node.getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                String content = children.item(i).getTextContent();
                content = content.replaceAll("'", "");
                map.put(children.item(i).getNodeName(), content);
            }
            nodelistCounter++;
        }
        return map;
    }

    public void search(String searchString) {
        if (nodelist == null) {
            throw new IllegalArgumentException("Nothing has been parsed yet.");
        }

        for (int i = 0; i < nodelist.getLength(); i++) {
            Node node = nodelist.item(i);

            if (node.getTextContent().toLowerCase().contains(searchString.toLowerCase())) {
                NodeList children = node.getChildNodes();
                for (int j = 0; j < children.getLength(); j++) {
                    Node child = children.item(j);
                    System.out.print(child.getNodeName() + ": ");
                    System.out.println(child.getTextContent());
                }
            }
        }
    }
}
