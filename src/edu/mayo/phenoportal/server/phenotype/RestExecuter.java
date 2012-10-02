package edu.mayo.phenoportal.server.phenotype;

import edu.mayo.phenotype.server.BasePhenoportalServlet;
import org.apache.commons.io.FileUtils;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.xml.sax.SAXParseException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLConnection;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RestExecuter extends BasePhenoportalServlet {

    private static final long serialVersionUID = 1L;

    private static RestExecuter s_restExecuter;
    private static String s_restUserId;
    private static String s_restPw;
    private static String s_restUrl;
    private static Logger logger = Logger.getLogger(RestExecuter.class.getName());

    public static final String STATUS_COMPLETE = "COMPLETE";
    public static final String STATUS_FAILED = "FAILED";
    public static final String STATUS_ERROR = "ERROR";

    // private
    private RestExecuter() {

    }

    /**
     * Set the Secure options on the new instance.
     * 
     * @return
     */
    public static RestExecuter getInstance(String path) {
        if (s_restExecuter == null) {
            s_restExecuter = new RestExecuter();

            s_restExecuter.setConnectionInfo(path);
            s_restExecuter.setHostNameVerifier();
            s_restExecuter.setAuth();
            s_restExecuter.trustSelfSignedSSL();
        }

        return s_restExecuter;
    }

    /*
     * Execute an algorithm and get the location.
     */
    public String createExecution(File zipFile, String startDate, String endDate) throws Exception {
        String charset = "UTF-8";

        String location = "";

        // generate some unique value
        String boundary = Long.toHexString(System.currentTimeMillis());
        String CRLF = "\r\n"; // Line separator required by multipart/form-data.

        URL url = new URL(s_restUrl + "/executions");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // infinite timeout on the connection
        connection.setConnectTimeout(0);
        connection.setDoOutput(true);
        connection.setInstanceFollowRedirects(false);
        connection.setRequestProperty("Accept", "application/xml");
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

        PrintWriter writer = null;
        try {
            OutputStream output = connection.getOutputStream();
            // true = autoFlush,important!
            writer = new PrintWriter(new OutputStreamWriter(output, charset), true);

            // Send start date.
            writer.append("--" + boundary).append(CRLF);
            writer.append("Content-Disposition: form-data; name=\"startDate\"").append(CRLF);
            writer.append("Content-Type: text/plain; charset=" + charset).append(CRLF);
            writer.append(CRLF);
            writer.append(startDate).append(CRLF).flush();

            // Send end date.
            writer.append("--" + boundary).append(CRLF);
            writer.append("Content-Disposition: form-data; name=\"endDate\"").append(CRLF);
            writer.append("Content-Type: text/plain; charset=" + charset).append(CRLF);
            writer.append(CRLF);
            writer.append(endDate).append(CRLF).flush();

            // Send binary file.
            writer.append("--" + boundary).append(CRLF);
            writer.append(
                    "Content-Disposition: form-data; name=\"file\"; filename=\""
                            + zipFile.getName() + "\"").append(CRLF);
            writer.append("Content-Type: application/zip").append(CRLF);
            writer.append("Content-Transfer-Encoding: binary").append(CRLF);
            writer.append(CRLF).flush();
            InputStream input = null;
            try {
                input = new FileInputStream(zipFile);
                byte[] buffer = new byte[1024];
                for (int length = 0; (length = input.read(buffer)) > 0;) {
                    output.write(buffer, 0, length);
                }
                output.flush(); // Important! Output cannot be closed. Close of
                                // writer will close output as well.
            } finally {
                if (input != null) {
                    input.close();
                }
            }
            writer.append(CRLF).flush(); // CRLF is important! It indicates end
                                         // of binary boundary.
            // End of multipart/form-data.
            writer.append("--" + boundary + "--").append(CRLF).flush();

            Map<String, List<String>> headerfields = connection.getHeaderFields();

            location = headerfields.get("Location").get(0);
            location = s_restUrl + "/" + location;

        } finally {
            if (writer != null) {
                writer.close();
            }
            connection.disconnect();
        }

        return location;
    }

    /**
     * Get the status of the execution
     * 
     * @param url
     * @return
     * @throws Exception
     */
    public String pollStatus(String url) throws Exception {
        URL executions = new URL(url);

        URLConnection connection = executions.openConnection();
        connection.setRequestProperty("Accept", "application/xml");
        InputStream in = connection.getInputStream();

        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.parse(in);

        Attr status = (Attr) doc.getElementsByTagName("execution").item(0).getAttributes()
                .getNamedItem("status");

        String executionStatus = status.getValue();

        in.close();
        return executionStatus;
    }

    /**
     * Get the xml result of the execution.
     * 
     * @param url
     * @return
     * @throws Exception
     */
    public String getXml(String url) throws Exception {

        URL executions = new URL(url);
        InputStream in = null;
        String resultStr = "";

        try {
            URLConnection connection = executions.openConnection();
            connection.setRequestProperty("Accept", "application/xml");
            in = connection.getInputStream();

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(in);

            TransformerFactory transFactory = TransformerFactory.newInstance();
            Transformer trans = transFactory.newTransformer();
            trans.setOutputProperty(OutputKeys.METHOD, "xml");
            trans.setOutputProperty(OutputKeys.INDENT, "yes");

            StringWriter sw = new StringWriter();
            StreamResult result = new StreamResult(sw);
            DOMSource source = new DOMSource(doc.getDocumentElement());

            trans.transform(source, result);

            resultStr = sw.toString();
        } catch (SAXParseException spe) {
            logger.log(
                    Level.SEVERE,
                    "Error getting XML and parsing it.  Probably 0 length from server."
                            + spe.getStackTrace(), spe);
        } finally {
            if (in != null) {
                in.close();
            }
        }

        return resultStr;
    }

    /**
     * Get the xml result of the execution.
     * 
     * @param url
     * @return
     * @throws Exception
     */
    public static String getImage(String url, String storeLocation, String fileName)
            throws Exception {
        URL executions = new URL(url);

        URLConnection connection = executions.openConnection();
        connection.setRequestProperty("Accept", "image/png");
        InputStream in = connection.getInputStream();

        fileName = fileName + ".png";
        File tempFile = new File(storeLocation + fileName);

        logger.log(Level.INFO, "GetImage path: " + tempFile.getAbsolutePath());

        OutputStream out = new BufferedOutputStream(new FileOutputStream(tempFile));
        for (int b; (b = in.read()) != -1;) {
            out.write(b);
        }

        out.flush();
        out.close();
        in.close();

        return fileName;
    }

    /**
     * Gets the bpmn result of the execution.
     * 
     * @param url
     * @param storeLocation
     * @param fileName
     * @return
     * @throws Exception
     */
    /* TODO: Use the actual returned .bpmn file. */
    public static String getBpmn(String url, String storeLocation, String fileName)
            throws Exception {
//        File incoming = new File(
//                "/Users/m091355/Dropbox/Mayo/Projects/Sharp/HTP/Drools Response/Disease.bpmn");
//
//        fileName = fileName + ".bpmn";
//        File tempFile = new File(storeLocation + fileName);
//        FileUtils.copyFile(incoming, tempFile);

        return null;
    }

    private void setHostNameVerifier() {
        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {

            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
    }

    private void setAuth() {
        java.net.Authenticator.setDefault(new java.net.Authenticator() {

            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(s_restUserId, s_restPw.toCharArray());
            }
        });
    }

    private void trustSelfSignedSSL() {
        try {
            SSLContext ctx = SSLContext.getInstance("TLS");
            X509TrustManager tm = new X509TrustManager() {

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException {
                }

                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException {
                }
            };
            ctx.init(null, new TrustManager[] { tm }, null);
            SSLContext.setDefault(ctx);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Read in the User/PW for the Rest service
     */
    private void setConnectionInfo(String path) {

        FileInputStream in = null;
        Properties starupProps = new Properties();

        try {
            in = new FileInputStream(path + "data/Startup.properties");
            starupProps.load(in);

            s_restUserId = starupProps.getProperty("restUserId");
            s_restPw = starupProps.getProperty("restPassword");
            s_restUrl = starupProps.getProperty("restUrl");

        } catch (FileNotFoundException ex) {
            logger.log(
                    Level.SEVERE,
                    "Startup property file not found to read the username and password"
                            + ex.getMessage(), ex);

        } catch (IOException ex) {
            logger.log(Level.SEVERE,
                    "I/O exception while readfing the credentials " + ex.getMessage(), ex);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                logger.log(Level.SEVERE, "Exception closing the connection" + ex.getMessage(), ex);
            }
        }
    }

}