package com.infomancers.tests;

import com.infomancers.collections.iterators.TreeIterators;
import com.infomancers.collections.tree.DOMAdapter;
import com.infomancers.collections.tree.TreeAdapter;
import junit.framework.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: aviadbd
 * Date: Jul 20, 2007
 * Time: 12:52:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class TreeTests {

    @Test
    public void simpleDomPrefix() throws ParserConfigurationException, IOException, SAXException {
        String xml = "<root><aviad>Hello!</aviad></root>";

        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes()));

        TreeAdapter adapter = new DOMAdapter(doc);

        Iterator<Object> it = TreeIterators.prefixIterator(adapter).iterator();

        it.next(); // skipping root, which has no text

        Assert.assertEquals("root", ((Node) it.next()).getNodeName());
        Assert.assertEquals("aviad", ((Node) it.next()).getNodeName());
        Assert.assertEquals("Hello!", ((Node) it.next()).getTextContent());
        Assert.assertFalse("Too many elements", it.hasNext());
    }

    @Test
    public void simpleDomPostfix() throws ParserConfigurationException, IOException, SAXException {
        String xml = "<root><aviad>Hello!</aviad></root>";

        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes()));

        TreeAdapter adapter = new DOMAdapter(doc);

        Iterator<Object> it = TreeIterators.postfixIterator(adapter).iterator();


        Assert.assertEquals("Hello!", ((Node) it.next()).getTextContent());
        Assert.assertEquals("aviad", ((Node) it.next()).getNodeName());
        Assert.assertEquals("root", ((Node) it.next()).getNodeName());

        it.next(); // skipping root, which has no text
        Assert.assertFalse("Too many elements", it.hasNext());
    }

    @Test
    public void intermediateDomPrefix() throws ParserConfigurationException, IOException, SAXException {
        String xml = "<root><people><person>Aviad</person><person>Joanna</person></people>" +
                "<cars><car>Sirion</car></cars></root>";

        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes()));

        TreeAdapter adapter = new DOMAdapter(doc);

        Iterator<Object> it = TreeIterators.prefixIterator(adapter).iterator();

        it.next(); // skipping root, which has no text

        Assert.assertEquals("root", ((Node) it.next()).getNodeName());
        Assert.assertEquals("people", ((Node) it.next()).getNodeName());
        Assert.assertEquals("First person", "person", ((Node) it.next()).getNodeName());
        Assert.assertEquals("Aviad", ((Node) it.next()).getTextContent());
        Assert.assertEquals("Second person", "person", ((Node) it.next()).getNodeName());
        Assert.assertEquals("Joanna", ((Node) it.next()).getTextContent());
        Assert.assertEquals("cars", ((Node) it.next()).getNodeName());
        Assert.assertEquals("car", ((Node) it.next()).getNodeName());
        Assert.assertEquals("Sirion", ((Node) it.next()).getTextContent());

        Assert.assertFalse("Too many elements", it.hasNext());
    }
}
