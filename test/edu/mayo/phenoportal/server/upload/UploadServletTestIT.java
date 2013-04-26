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

}
