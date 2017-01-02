package project3task2server;

import java.io.IOException;
import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


/**
 *  This Class is used to parse the message receive at the server side from the client
 * @author Jiaming
 */
public class XMLParser {

    private Document document;
    private String requestType;
    private String sensorId;
    private String timeStamp;
    private String type;
    private String temperature;
    private String signature;

    public XMLParser() {
        document = null;
        requestType = "";
        sensorId = "";
        timeStamp = "";;
        type = "";
        temperature = "";
        signature = "";
    }

    public void setdocument(String xmlString) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            this.document = builder.parse(new InputSource(new StringReader(xmlString)));
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    public Temperature parse(String xmlString) {
        setdocument(xmlString);

        // get root node
        Element rootElement = this.document.getDocumentElement();
        // get nodes
        NodeList nodes = rootElement.getChildNodes();

        // get node values
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element child = (Element) node;

                switch (child.getTagName()) {
                    case "requestType":
                        requestType = child.getFirstChild().getNodeValue();
                        break;
                    case "sensorID":
                        sensorId = child.getFirstChild().getNodeValue();
                        break;
                    case "timeStamp":
                        timeStamp = child.getFirstChild().getNodeValue();
                        break;
                    case "type":
                        type = child.getFirstChild().getNodeValue();
                        break;
                    case "temperature":
                        temperature = child.getFirstChild().getNodeValue();
                        break;
                    case "signature":
                        signature = child.getFirstChild().getNodeValue();
                        break;
                    default:
                        break;
                }
            }
        }
        
        // after getting all the values, new an Temperature Object to store all the information
        return new Temperature(requestType, sensorId, timeStamp, type, temperature, signature);
    }

    public static void main(String[] argus) {
    }

}
