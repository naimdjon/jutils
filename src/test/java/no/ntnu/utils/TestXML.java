package no.ntnu.utils;

import org.junit.Test;

import static no.ntnu.utils.XML.getDoc;
import static org.junit.Assert.assertEquals;

/**
 * Created by NT
 * User: takhirov
 * Date: 3/17/12
 * Time: 4:29 PM
 */

public class TestXML {


    @Test
    public void testReturnsSubnode() throws Exception{
        assertEquals("c", getDoc("<a><b><c><d>test</d></c></b></a>", "c").getChildNodes().item(0).getNodeName());
    }
}
