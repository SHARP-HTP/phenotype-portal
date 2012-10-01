package edu.mayo.phenoportal.server.upload;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class UploadServletTestIT {

    private static UploadServlet uploadServlet;
    private UploadItems uploadItems;

    @BeforeClass
    public static void setupClass() {
        uploadServlet = new UploadServlet();
    }

    @Before
    public void setupTest() {
        uploadItems = new UploadItems();
    }

    @Test
    public void validateVersionTest() {
        assertTrue(uploadServlet.validateVersion("1"));
        assertTrue(uploadServlet.validateVersion("1.1"));
        assertTrue(uploadServlet.validateVersion("1.1.1"));
        assertFalse(uploadServlet.validateVersion("1."));
        assertFalse(uploadServlet.validateVersion("1.1."));
        assertFalse(uploadServlet.validateVersion("1.1."));
        assertFalse(uploadServlet.validateVersion("1..1"));
        assertFalse(uploadServlet.validateVersion("1.1..1"));
        assertFalse(uploadServlet.validateVersion("1x1"));
        assertFalse(uploadServlet.validateVersion("1.1x1"));
    }

    @Test
    public void getPrefixTest() {
        uploadItems.setVersion("1.2");
        uploadItems.setId("123");

        assertEquals("NQF_0001_123_1.2",
                uploadServlet.getPrefix("NQF_0001_HHS_Updated_Dec_2011", uploadItems));
        assertEquals("NQF_1_123_1.2",
                uploadServlet.getPrefix("NQF_1_HHS_Updated_Dec_2011", uploadItems));
        assertEquals("Diabetes F_123_1.2",
                uploadServlet.getPrefix("Diabetes Foot Exam", uploadItems));
        assertEquals("Test_123_1.2", uploadServlet.getPrefix("Test", uploadItems));
        assertEquals("TenLetters_123_1.2", uploadServlet.getPrefix("TenLetters", uploadItems));
        assertEquals("UndrScore_123_1.2", uploadServlet.getPrefix("UndrScore_", uploadItems));
    }

}
