package project3task2client;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * This class is use to wrap all the information to a XML String
 * @author Jiaming
 */
public class XMLWrapper {

    private String xmlString;
    private String xmlSendExample;
    private Document document;

    public XMLWrapper() {
        // use the initDocument function to initialize the document field in this class.
        initDocument();
    }

    private void wrapRequest(RequestType requestType,
                                    String sensorId,
                                    String timeStamp,
                                    String type,
                                    String temperature,
                                    String signature) {

        // get root node
        Element rootElement = this.document.getDocumentElement();
        // get nodes
        NodeList nodes = rootElement.getChildNodes();

        // set all the node values.
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element child = (Element) node;

                switch (child.getTagName()) {
                case "requestType":
                    child.getFirstChild().setNodeValue(
                            requestType.getIndex() + "");
                    break;
                case "sensorID":
                    child.getFirstChild().setNodeValue(sensorId);
                    break;
                case "timeStamp":
                    child.getFirstChild().setNodeValue(timeStamp);
                    break;
                case "type":
                    child.getFirstChild().setNodeValue(type);
                    break;
                case "temperature":
                    child.getFirstChild().setNodeValue(temperature);
                    break;
                case "signature":
                    child.getFirstChild().setNodeValue(signature);
                    break;
                default:
                    break;
                }
                //                System.out.println(child.getTagName());
                //                System.out.println(child.getFirstChild().getNodeValue());
            }
        }

        this.xmlString = docToStr(document);
    }

    private void initDocument() {
        // This is the default xml string
        xmlSendExample =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<request>\n"
                        + "  <requestType>example</requestType>\n"
                        + "  <sensorID>example</sensorID>\n"
                        + "  <timeStamp>example</timeStamp>\n"
                        + "  <type>example</type>\n"
                        + "  <temperature>example</temperature>\n"
                        + "  <signature>example</signature>\n" + "</request>";

        Document sensorMessage = null;

        try {
            // parse the default xml string to a document object
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            sensorMessage = builder.parse(new InputSource(new StringReader(xmlSendExample)));

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

        this.document = sensorMessage;
    }

    public String getXML(RequestType requestType,
                                String sensorId,
                                String timeStamp,
                                String type,
                                String temperature,
                                String signature) {
        // use the wrapRequest to get a new xml string
        // the new string contains all the input information
        wrapRequest(requestType,
                sensorId,
                timeStamp,
                type,
                temperature,
                signature);
        return xmlString;
    }

    // document to string function
    // reference http://stackoverflow.com/questions/2567416/xml-document-to-string
    private static String docToStr(Document doc) {
        try {
            StringWriter sw = new StringWriter();
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,
                    "no");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

            transformer.transform(new DOMSource(doc), new StreamResult(sw));
            return sw.toString();
        } catch (Exception ex) {
            throw new RuntimeException("Error converting to String", ex);
        }
    }

    public static void main(String[] argus) {
        XMLWrapper wrapper = new XMLWrapper();
        String string = wrapper.getXML(RequestType.High,
                "1",
                "1",
                "1",
                "1",
                "1");
        System.out.println(string);

    }

}
