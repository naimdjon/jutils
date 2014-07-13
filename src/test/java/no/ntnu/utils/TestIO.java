package no.ntnu.utils;

import org.junit.Test;

import java.io.File;

import static junit.framework.Assert.assertTrue;
import static no.ntnu.utils.IO.dirsInUserHomeDir;

/**
 * Created by IntelliJ IDEA.
 * User: takhirov
 * Date: 3/17/12
 * Time: 11:07 PM
 */
public class TestIO {

    @Test
    public void testCheckDirsAreCorrectlyCreated() throws Exception{
        dirsInUserHomeDir("thisistest/test1/test2");
        File file = new File(System.getProperty("user.home"));
        File test=new File(file.getAbsolutePath().concat(File.separator).concat("thisistest/test1/test2"));
        assertTrue(test.exists());
        assertTrue(test.isDirectory());
        File fff = new File(System.getProperty("user.home"), "thisistest");
        IO.deleteR(fff);
    }
}
