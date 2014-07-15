package no.ntnu.utils;


import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import static no.ntnu.utils.Col.newList;
import static org.w3c.dom.Node.*;

public class XPathUtils {
    private static ThreadLocal<XPath> xpathThreadLocal = new ThreadLocal<XPath>();

    public static XPath getXPath() {
        XPath xPath = xpathThreadLocal.get();
        if (xPath != null) return xPath;
        xPath = XPathFactory.newInstance().newXPath();
        xpathThreadLocal.set(xPath);
        return xPath;
    }

    public static Node selectSingleNode(Node contextNode, String str) throws XPathRuntimeException {
        try {
            return (Node) getXPath().compile(str).evaluate(contextNode, XPathConstants.NODE);
            //return XPathAPI.selectSingleNode(contextNode, str);
        } catch (Exception e) {
            e.printStackTrace();
            throw new XPathRuntimeException(e);
        }
    }

    public static String nodeVal(Node n) {
        String val = n.getNodeValue();
        if (val == null || val.length() < 1)
            val = eval(n, ".");
        return val;
    }

    public static void addNodes(final Node n, String expr, final Collection<String> col) {
        iterate(n, expr, new Iterator() {
            public void processNext(Node n) {
                col.add(nodeVal(n));
            }
        });
    }

    public static String eval(final Node n) {
        String val = n.getNodeValue();
        if (val == null) val = eval(n, ".");
        return val;
    }

    public static void main(String[] args) {
        Document doc = XML.getDoc("<x><x>1</x><x>2</x><x>3</x><x>4</x><x>5</x><x>6</x><x>7</x><x>8</x></x>");
        iterate(doc, "/x/x", new Iterator() {
            public void processNext(Node n) {
                System.out.println("x=" + eval(n, "."));
            }
        });


    }

    public static void iterate(Node contextNode, String str, Iterator iterator) throws XPathRuntimeException {
        try {
            NodeList l = (NodeList) getXPath().compile(str).evaluate(contextNode, XPathConstants.NODESET);
            for (int i = 0; i < l.getLength(); i++) {
                Node n = l.item(i);
                if (n.getNodeType() == Node.ELEMENT_NODE)
                    iterator.processNext(n);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new XPathRuntimeException(e);
        }
    }

    public static String eval(Node contextNode, String str) throws XPathRuntimeException {
        try {
            return (String) getXPath().compile(str).evaluate(contextNode, XPathConstants.STRING);
            //return XPathAPI.eval(contextNode, str).str();
        } catch (Exception e) {
            e.printStackTrace();
            throw new XPathRuntimeException(e);
        }
    }


    public static String evalLowerCase(Node contextNode, String str) throws XPathRuntimeException {
        String s = eval(contextNode, str);
        if (s != null) s = s.toLowerCase();
        return s;
    }


    public static long evalLong(Node contextNode, String str) throws XPathRuntimeException {
        return Long.parseLong(eval(contextNode, str));
    }

    public static float evalFloat(Node contextNode, String str) throws XPathRuntimeException {
        String s = eval(contextNode, str);
        if (s == null || s.length() <= 0) s = "0";
        return Float.parseFloat(s.replaceAll(",", "."));
    }


    public static boolean evalBoolean(Node contextNode, String str) throws XPathRuntimeException {
        return Boolean.valueOf(eval(contextNode, str));
    }

    public static boolean evalBool(Node contextNode, String str) throws XPathRuntimeException {
        final String s = eval(contextNode, str);
        return s != null && (s.trim().equals("1") || s.trim().equals("true") || s.trim().equals("on"));
    }


    public static int evalInt(Node contextNode, String str) throws XPathRuntimeException {
        String s = eval(contextNode, str);
        if (s == null || s.length() <= 0) s = "-1";
        return Integer.parseInt(s);
    }


    public static String getNodeTreeAsString(final Node n) {
        try {
            final Transformer transfor = TransformerFactory.newInstance().newTransformer();
            final java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
            transfor.setOutputProperty("omit-xml-declaration", "yes");
            transfor.transform(new DOMSource(n), new StreamResult(out));
            return out.toString();
        } catch (TransformerException e) {
            e.printStackTrace();
            throw new XPathRuntimeException(e);
        }
    }

    public static Collection<String> selectNodeValues(final Node contextNode, final String str) throws XPathRuntimeException {
        final Collection<String> nodes = new HashSet<String>();
        iterate(contextNode, str, new Iterator() {
            public void processNext(Node n) {
                nodes.add(nodeVal(n));
            }
        });
        return nodes;
    }

    public static List<Node> nodeList(final Node contextNode, final String str) throws XPathRuntimeException {
        try {
            List<Node> retval = newList();
            NodeList list = (NodeList) getXPath().compile(str).evaluate(contextNode, XPathConstants.NODESET);
            for (int i = 0; i < list.getLength(); i++) {
                Node n = list.item(i);
                short nodeType = n.getNodeType();
                if (nodeType == DOCUMENT_TYPE_NODE ||
                        nodeType == ELEMENT_NODE ||
                        nodeType == TEXT_NODE ||
                        nodeType == DOCUMENT_NODE)
                    retval.add(n);
            }
            return retval;
        } catch (Exception e) {
            e.printStackTrace();
            throw new XPathRuntimeException(e);
        }
    }


    public static NodeList selectNodeList(final Node contextNode, final String str) throws XPathRuntimeException {
        try {
            return (NodeList) getXPath().compile(str).evaluate(contextNode, XPathConstants.NODESET);
            //return XPathAPI.selectNodeList(contextNode, str);
        } catch (Exception e) {
            e.printStackTrace();
            throw new XPathRuntimeException(e);
        }
    }


    public interface Iterator {
        public void processNext(final Node n);
    }
}
