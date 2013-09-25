package edu.mayo.phenoportal.server.phenotype;

import edu.mayo.phenoportal.utils.ServletUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.xml.sax.SAXParseException;

import javax.net.ssl.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RestExecuter {

    private static RestExecuter s_restExecuter;
    private static String s_restUserId = ServletUtils.getRestUserId();
    private static String s_restPw = ServletUtils.getRestPassword();
    private static String s_restUrl = ServletUtils.getRestUrl();
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
    public static RestExecuter getInstance() {
        if (s_restExecuter == null) {
            s_restExecuter = new RestExecuter();

            s_restExecuter.setHostNameVerifier();
            s_restExecuter.setAuth();
            s_restExecuter.trustSelfSignedSSL();
        }

        return s_restExecuter;
    }

    /**
     * Create (and configure) a {@link HttpURLConnection} from a {@link URL}.
     *
     * @param url the target URL
     * @return the configured connection
     * @throws Exception
     */
    protected HttpURLConnection getConnection(URL url) throws Exception {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        if(connection instanceof HttpsURLConnection){
            ((HttpsURLConnection)connection).setSSLSocketFactory(SSLContext.getDefault().getSocketFactory());
        }

        return connection;
    }

    /*
     * Execute an algorithm and get the location.
     */
    public String createExecution(File xmlFile, String startDate, String endDate, Map<String, String> valueSets) throws Exception {
        String charset = "UTF-8";

        String location = "";
        String valueSetJson = mapToJson(valueSets);
        valueSetJson = valueSetJson == null ? "" : valueSetJson;

        // generate some unique value
        String boundary = Long.toHexString(System.currentTimeMillis());
        String CRLF = "\r\n"; // Line separator required by multipart/form-data.

        URL url = new URL(s_restUrl + "/executor/executions");
        HttpURLConnection connection = this.getConnection(url);

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

            // Send valueSetMap.
            writer.append("--" + boundary).append(CRLF);
            writer.append("Content-Disposition: form-data; name=\"valueSetDefinitions\"").append(CRLF);
            writer.append("Content-Type: text/plain; charset=" + charset).append(CRLF);
            writer.append(CRLF);
            writer.append(valueSetJson).append(CRLF).flush();

            // Send binary file.
            writer.append("--" + boundary).append(CRLF);
            writer.append(
                    "Content-Disposition: form-data; name=\"file\"; filename=\""
                            + xmlFile.getName() + "\"").append(CRLF);
            writer.append("Content-Type: application/xml").append(CRLF);
            writer.append("Content-Transfer-Encoding: binary").append(CRLF);
            writer.append(CRLF).flush();
            InputStream input = null;
            try {
                input = new FileInputStream(xmlFile);
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
        } catch (Exception e) {
            logger.warning("There was an error when attempting to execute the algorithm. Error: "
                    + e.getMessage());
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

        HttpURLConnection connection = this.getConnection(executions);

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
            HttpURLConnection connection = this.getConnection(executions);

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
            SSLContext ctx = SSLContext.getInstance("SSL");
            X509TrustManager tm = new X509TrustManager() {

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) {
                }

                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) {
                }
            };
            ctx.init(null, new TrustManager[] { tm }, new SecureRandom());
            SSLContext.setDefault(ctx);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String mapToJson(Map<String, String> map) {
        String jsonMap = "";
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            jsonMap = objectMapper.writeValueAsString(map);
        }
        catch (Exception e) {
            logger.log(Level.WARNING, "Failed to serialize value set map to json string.", e);
        }
        return jsonMap;
    }

}