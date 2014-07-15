package no.ntnu.utils;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;

import static java.lang.Math.min;
import static no.ntnu.utils.Clazz.*;
import static no.ntnu.utils.Clazz.instance;
import static no.ntnu.utils.IO.*;
import static no.ntnu.utils.StringUtils.cat;


public class XML {


    public static String cleanWithHtmlCleaner(String xml) {
        Object htmlCleaner = instantiate("org.htmlcleaner.HtmlCleaner");
        Object props = invoke(htmlCleaner, "getProperties");
        invoke(props, "setOmitHtmlEnvelope", true);
        invoke(props, "setOmitComments", true);
        Object tagNode = invoke(htmlCleaner, "clean", xml);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Object serializer = instance("org.htmlcleaner.SimpleXmlSerializer", new Arguments(props));
        invoke(serializer, "writeToStream", tagNode, output);
        return output.toString();
    }

    public static Document getSafeDoc(File file) throws RuntimeException {
        try {
            return getDoc(file);
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Document getDoc(File file) throws RuntimeException {
        return getDoc(readFileStr(file));
    }

    public static Document getDocFromURL(String url) throws RuntimeException {
        return getDoc(readUrl(url));
    }

    public static Document getDoc(String content, String childNode) throws RuntimeException {
        String s = cat(childNode, ">");
        //String parseContent = content.substring(content.indexOf(cat("<", s)), content.indexOf(cat("</", s)) + childNode.length() + 3);
        //if(D.VERBOSE)D.d(parseContent);
        //Document doc = getDoc(parseContent);
        //return doc;
        int start = content.indexOf(cat("<", s));
        int end = content.indexOf(cat("</", s)) + childNode.length() + 3;
        if (min(start, end) < 0) return null;
        return getDoc(content.substring(start, end));
    }

    public static Document getDoc(String content) throws RuntimeException {
        try {
            return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(bais(content)));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static String prettyPrint(String doc) throws RuntimeException {
        return serialize(doc);
    }

    public static String serialize(String doc) throws RuntimeException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        serialize(getDoc(doc), baos);
        return baos.toString();
    }

    public static String serializeNoDeclaration(String doc) throws RuntimeException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        serialize(getDoc(doc), baos, true);
        return baos.toString();
    }


    public static Document getDocFromFile(String filename) throws XPathRuntimeException {
        Document doc;
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setValidating(false);
            dbf.setFeature("http://xml.org/sax/features/namespaces", false);
            dbf.setFeature("http://xml.org/sax/features/validation", false);
            dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
            dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            DocumentBuilder db = dbf.newDocumentBuilder();
            doc = db.parse(new InputSource(new FileReader(filename)));
        } catch (Exception se) {
            se.printStackTrace();
            throw new XPathRuntimeException(se);
        }
        return doc;
    }/*
    public static Document getDocFromFile(String filename) throws XPathRuntimeException {
        Document doc;
        try {
            DOMParser parser = new DOMParser();
            parser.setFeature("http://apache.org/xml/features/validation/dynamic", false);//typically validate on import to make sure content valid against DTD (must set DTD as URL in XML doc)
            parser.setFeature("http://apache.org/xml/features/validation/schema", false);
            parser.parse(new InputSource(new FileReader(filename)));
            doc = parser.getDocument();
        } catch (SAXException se) {
            se.printStackTrace();
            throw new XPathRuntimeException(se);
        } catch (IOException ioe) {
            throw new XPathRuntimeException(ioe);
        }
        return doc;
    }*/

    public static void serialize(Document doc, OutputStream out) throws RuntimeException {
        serialize(doc, out, false);
    }

    public static void main(String[] args) {
        String s = "<x><y><z/></y></x>";
        serialize(getDoc(s));
    }

    public static String toString(Node node) throws RuntimeException {
        return serialize(node);
    }

    public static String serialize(Node node) throws RuntimeException {
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
//initialize StreamResult with File object to save to file
            StreamResult result = new StreamResult(new StringWriter());
            DOMSource source = new DOMSource(node);
            transformer.transform(source, result);
            String xmlString = result.getWriter().toString();
            return xmlString;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static void serialize(Document doc, OutputStream out, boolean omitXMLDeclaration) throws RuntimeException {
        TransformerFactory tfactory = TransformerFactory.newInstance();
        Transformer serializer;
        try {
            serializer = tfactory.newTransformer();
            //Setup indenting to "pretty print"
            serializer.setOutputProperty(OutputKeys.INDENT, "yes");
            if (omitXMLDeclaration)
                serializer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            serializer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            serializer.transform(new DOMSource(doc), new StreamResult(out));
        } catch (TransformerException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    public static String removeNameSpace(String xml) {
        return xml.replaceAll(" xmlns([a-zA-Z0-9-!\"#$%&'()*+,./:;<=>?@_`{|}~]*)\"", "");
    }

    public static String removeProcessingInstrAndNS(String s) {
        return removeNameSpace(removeProcessingInstr(s));
    }

    public static String removeProcessingInstr(String s) {
        return s.replaceFirst("<\\?xml[^>]*>", "");
    }
}
