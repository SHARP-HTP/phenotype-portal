package edu.mayo.phenoportal.server.phenotype;

import com.google.common.io.Files;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.mayo.phenoportal.utils.ServletUtils;

public class ImageServlet extends HttpServlet {

    private static final long serialVersionUID = -5351120670450446657L;
    private final Logger logger = Logger.getLogger(ImageServlet.class.getName());

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doPost(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String imageId = request.getParameter("id");
        response.setContentType("image/png");

        try {
            OutputStream out = response.getOutputStream();
            Files.copy(new File(ServletUtils.getExecutionResultsPath(request) + File.separator
                    + imageId), out);
            out.flush();
            out.close();
        } catch (IOException ioe) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            logger.log(Level.WARNING, "Unable to write the file to the response output stream.",
                    ioe);
        }

        response.setStatus(HttpServletResponse.SC_OK);
    }

}
