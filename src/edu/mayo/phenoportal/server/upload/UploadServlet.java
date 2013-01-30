package edu.mayo.phenoportal.server.upload;

import com.google.common.io.ByteStreams;
import com.google.common.io.Files;
import edu.mayo.phenoportal.server.database.DBConnection;
import edu.mayo.phenoportal.server.exception.PhenoportalFileException;
import edu.mayo.phenoportal.server.utils.MimeUtils;
import edu.mayo.phenoportal.shared.database.UploadColumns;
import edu.mayo.phenoportal.utils.SQLStatements;
import edu.mayo.phenotype.server.BasePhenoportalHttpServlet;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class UploadServlet extends BasePhenoportalHttpServlet {

    private static final long serialVersionUID = 3457906406134591883L;
    private static String ALGORITHM_PATH = null;
    private static String MAT_UPLOAD_PATH = "/mat/zips";
    private static Logger logger = Logger.getLogger(UploadServlet.class.getName());

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        doPost(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        if (ServletFileUpload.isMultipartContent(request)) {
            handleMultipartRequest(request, response);
        } else {
            handleRequest(request, response);
        }
    }

    private void handleMultipartRequest(HttpServletRequest request, HttpServletResponse response) {
        processUploadAlgorithm(request, response);
    }

    private void handleRequest(HttpServletRequest request, HttpServletResponse response) {
        processDownloadAlgorithm(request, response);
    }

    private void processUploadAlgorithm(HttpServletRequest request, HttpServletResponse response) {
        UploadItems uploadItems = new UploadItems();

        if (ALGORITHM_PATH == null) {
            ALGORITHM_PATH = getAlgorithmPath(request);
        }

        try {
            FileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);
            List<FileItem> items = upload.parseRequest(request);

	        for (FileItem item : items) {
		        if (item.isFormField()) {
			        processFormField(item, uploadItems);
		        }
		        else {
			        uploadItems.addInputFileItem(item);
		        }
	        }

            boolean valid;
            if (valid = validateMetadata(uploadItems)) {
                valid = processFiles(uploadItems, request);
            }

            response.setContentType("text/html");
            response.getWriter().print(
                    String.format("<script type=\"text/javascript\">\n"
                            + "    window.onload = top.uploadStatus(%b, \"%s\"); \n" + "</script>",
                            valid, uploadItems.getMessages()));
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (FileUploadException fue) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            logger.log(Level.WARNING, fue.getMessage(), fue);
        } catch (IOException ioe) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            logger.log(Level.WARNING, ioe.getMessage(), ioe);
        }
    }

    private void processFormField(FileItem item, UploadItems uploadItems) {
        String fieldName = item.getFieldName();

        if (fieldName.equals(UploadColumns.USER.colName())) {
            uploadItems.setUser(item.getString());
        } else if (fieldName.equals(UploadColumns.NAME.colName())) {
            uploadItems.setName(item.getString());
        } else if (fieldName.equals(UploadColumns.VERSION.colName())) {
            uploadItems.setVersion(item.getString());
        } else if (fieldName.equals(UploadColumns.STATUS.colName())) {
            uploadItems.setStatus(item.getString());
        } else if (fieldName.equals(UploadColumns.INSTITUTION.colName())) {
            uploadItems.setInstitution(item.getString());
        } else if (fieldName.equals(UploadColumns.CREATEDATE.colName())) {
            uploadItems.setCreateDate(createDate(item.getString()));
        } else if (fieldName.equals(UploadColumns.DESCRIPTION.colName())) {
            uploadItems.setDescription(item.getString());
        } else if (fieldName.equals(UploadColumns.COMMENT.colName())) {
            uploadItems.setComment(item.getString());
        } else if (fieldName.equals(UploadColumns.ASSOC_LINK.colName())) {
            uploadItems.setAssocLink(item.getString());
        } else if (fieldName.equals(UploadColumns.ASSOC_NAME.colName())) {
            uploadItems.setAssocName(item.getString());
        } else if (fieldName.equals(UploadColumns.ID.colName())) {
            uploadItems.setId(item.getString());
        } else {
            logger.log(Level.INFO, String.format("Ignoring form field '%s'.\n", fieldName));
        }
    }

    private Date createDate(String dateStr) {
        /* Date will come in from SmartGWT as: $$DATE$$:yyyy-MM-dd HH:mm:ss */
        Date date = null;

        if (dateStr != null && !dateStr.trim().equals("")) {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-DD HH:mm:ss");
                date = dateFormat.parse(dateStr.substring(9));
            } catch (ParseException pe) {
                logger.log(Level.WARNING, "Unable to parse the date string.", pe);
            }
        }
        return date;
    }

    private boolean processFiles(UploadItems uploadItems, HttpServletRequest request) {
        boolean success = true;

        List<FileItem> fileItems = uploadItems.getInputFileItems();

        try {
            for (FileItem fileItem : fileItems) {
                File tmpFile;
                tmpFile = File.createTempFile(UUID.randomUUID().toString(), fileItem.getName());
                ByteStreams.copy(fileItem.getInputStream(), new FileOutputStream(tmpFile));
                String mimeType = MimeUtils.getMimeType(tmpFile);

                if (MimeUtils.isWordFile(mimeType)) {
                    if (uploadItems.getDocFile() == null) {
                        uploadItems.setDocFile(File.createTempFile(UUID.randomUUID().toString(),
                                ".word_tmp"));
                        ByteStreams.copy(fileItem.getInputStream(), new FileOutputStream(
                                uploadItems.getDocFile()));
                    } else {
                        success = false;
                        uploadItems.addMessage("Two word documents detected.");
                    }
                } else if (MimeUtils.isZipFile(mimeType)) {
                    uploadItems.setPrefix(getPrefix(fileItem.getName(), uploadItems));
                    success = extractZip(fileItem.getInputStream(), uploadItems);

                } else {
                    logger.info("Unsupported file.");
                }
            }
			if (success) {
                success = validateUploadFile(uploadItems);
			}
            if (success) {
                success = persistUploadFile(request, uploadItems);
            }

        } catch (IOException ioe) {
            success = false;
            logger.log(Level.WARNING, "Error occurred while processing the files.", ioe);
        } catch (PhenoportalFileException pfe) {
            success = false;
            logger.log(Level.WARNING, "Error occurred while processing the files.", pfe);
        }
        return success;
    }

    public String getPrefix(String filename, UploadItems uploadItems) {
        StringBuilder prefix = new StringBuilder();
        Pattern pattern = Pattern.compile("^NQF_\\d+_.*$");

        if (pattern.matcher(filename).matches()) {
            prefix.append(filename.substring(0, filename.indexOf('_', 4)));
        } else {
            prefix.append(filename.substring(0, Math.min(10, filename.length())));
        }

        if (prefix.charAt(prefix.length() - 1) != '_') {
            prefix.append('_');
        }

        String id = uploadItems.getId();
        id = id == null ? "" : id;

        String version = uploadItems.getVersion();
        version = version == null ? "" : version;

        prefix.append(String.format("%s_%s", id, version));

        return prefix.toString();
    }

    private boolean extractZip(InputStream inputStream, UploadItems uploadItems)
            throws IOException, PhenoportalFileException {
        if (uploadItems.getZipFile() == null) {
            uploadItems.setZipFile(File.createTempFile(UUID.randomUUID().toString(), ".zip_tmp"));
            ByteStreams.copy(inputStream, new FileOutputStream(uploadItems.getZipFile()));
        } else {
            uploadItems.addMessage("Two zip files detected.");
            return false;
        }

        boolean success = true;
        List<File> extractedFiles = new ArrayList<File>();
        ZipInputStream zipIn = new ZipInputStream(new FileInputStream(uploadItems.getZipFile()));
        ZipEntry entry;

        while ((entry = zipIn.getNextEntry()) != null && success) {
            if (!entry.isDirectory()) {
                int i = entry.getName().lastIndexOf(".");
                File tempFile = File.createTempFile(UUID.randomUUID().toString(), entry.getName()
                        .substring(i));
                OutputStream outStream = new FileOutputStream(tempFile);
                int bytesRead;
                byte data[] = new byte[1024];
                while ((bytesRead = zipIn.read(data)) > 0) {
                    outStream.write(data, 0, bytesRead);
                }

                outStream.flush();
                outStream.close();
                extractedFiles.add(tempFile);
            }
        }
        zipIn.close();

        return processExtractedFiles(extractedFiles, uploadItems);
    }

    private boolean processExtractedFiles(List<File> files, UploadItems uploadItems)
            throws PhenoportalFileException {
        boolean success = true;
        for (File file : files) {
            if (!file.isDirectory() && success) {
                success = setFile(file, uploadItems);
            }
        }
        return success;
    }

    private boolean setFile(File file, UploadItems uploadItems) throws PhenoportalFileException {
        boolean success = true;
        String mimeType = MimeUtils.getMimeType(file);

        if (MimeUtils.isHtmlFile(mimeType)) {
            if (uploadItems.getHtmlFile() == null)
                uploadItems.setHtmlFile(file);
            else {
                // success = false;
                // messages.append("Two html files detected in the zip.");
                logger.info("Two html files detected in the zip.");
            }
        } else if (MimeUtils.isXmlFile(mimeType)) {
            if (uploadItems.getXmlFile() == null)
                uploadItems.setXmlFile(file);
            else {
                // success = false;
                // messages.append("Two XML files detected in the zip.");
                logger.info("Two XML files detected in the zip.");
            }
        } else if (MimeUtils.isXlsFile(mimeType)) {
            if (uploadItems.getXlsFile() == null)
                uploadItems.setXlsFile(file);
            else {
                // success = false;
                // messages.append("Two XLS files detected in the zip.");
                logger.info("Two XLS files detected in the zip.");
            }
        } else if (MimeUtils.isZipFile(mimeType)) {
            if (uploadItems.getZipFile() == null)
                uploadItems.setZipFile(file);
            else {
                // success = false;
                // messages.append("Two zip files detected.");
                logger.info("Two zip files detected");
            }
        } else {
            logger.info(String.format("Unused file of type '%s' found in the zip file.", mimeType));
        }
        return success;
    }

    private boolean validateMetadata(UploadItems uploadItems) {
        /* Required: Name, User, Version, Status, Category Id */
        boolean valid = true;
        if (uploadItems.getName() == null || uploadItems.getName().trim().equals("")) {
            valid = false;
            uploadItems.addMessage("Algorithm name is required.");
        }

        if (!validateVersion(uploadItems.getVersion())) {
            valid = false;
            uploadItems.addMessage("Version number is required.");
        }

        if (uploadItems.getStatus() == null || uploadItems.getStatus().trim().equals("")) {
            valid = false;
            uploadItems.addMessage("Status is required.");
        }

        if (uploadItems.getUser() == null || uploadItems.getUser().trim().equals("")) {
            valid = false;
            uploadItems.addMessage("User is required.");
        }

        if (uploadItems.getId() == null || uploadItems.getId().trim().equals("")) {
            valid = false;
            uploadItems.addMessage("Category is required.");
        }

        if (uploadItems.getDescription() == null || uploadItems.getDescription().trim().equals("")) {
            valid = false;
            uploadItems.addMessage("Description is required.");
        }

        return valid;
    }

    private boolean validateUploadFile(UploadItems uploadItems) {
        /* Required: Zip, XML, HTML, XLS */
        boolean valid = true;

        if (uploadItems.getZipFile() == null) {
            valid = false;
            uploadItems.addMessage("Zip file is required.");
        }

        if (uploadItems.getXmlFile() == null) {
            valid = false;
            uploadItems.addMessage("The zip file must contain a valid XML file.");
        }

        if (uploadItems.getHtmlFile() == null) {
            valid = false;
            uploadItems.addMessage("The zip file must contain a valid HTML file.");
        }

        if (uploadItems.getXlsFile() == null) {
            valid = false;
            uploadItems.addMessage("The zip file must contain a valid XLS file.");
        }

        if (valid)
            logger.info(String.format(
                    "Algorithm uploaded: Name: %sVersion: %sXML: %sHTML: %sXLS: %s", uploadItems
                            .getName(), uploadItems.getVersion(), uploadItems.getXmlFile()
                            .getName(), uploadItems.getHtmlFile().getName(), uploadItems
                            .getXlsFile().getName()));

        return valid;
    }

    /*
     * Valid Version: Matches 1 or more digits followed by 0 or more groups that
     * start with a . and at least 1 digit.
     */
    public boolean validateVersion(final String version) {
        Pattern pattern = Pattern.compile("^\\d+(?:\\.\\d+)*$");
        return pattern.matcher(version).matches();
    }

    private boolean persistUploadFile(HttpServletRequest request, UploadItems uploadItems) {
        boolean success;
        /* Move files to algorithm directory. */
        String destPath = ALGORITHM_PATH + "/" + uploadItems.getPrefix();

        logger.info("Persisting files to file system @ " + destPath);

        try {
            if (new File(destPath).mkdirs()) {

                File newZip = new File(destPath + "/" + uploadItems.getPrefix() + ".zip");
                Files.copy(uploadItems.getZipFile(), newZip);
                uploadItems.setZipFile(newZip);

                File newXml = new File(destPath + "/" + uploadItems.getPrefix() + ".xml");
                Files.copy(uploadItems.getXmlFile(), newXml);
                uploadItems.setXmlFile(newXml);

                File newHtml = new File(destPath + "/" + uploadItems.getPrefix() + ".html");
                Files.copy(uploadItems.getHtmlFile(), newHtml);
                uploadItems.setHtmlFile(newHtml);

                File newXls = new File(destPath + "/" + uploadItems.getPrefix() + ".xls");
                Files.copy(uploadItems.getXlsFile(), newXls);
                uploadItems.setXlsFile(newXls);

                if (uploadItems.getDocFile() != null) {
                    File newDoc = new File(destPath + "/" + uploadItems.getPrefix() + ".doc");
                    Files.copy(uploadItems.getDocFile(), newDoc);
                    uploadItems.setDocFile(newDoc);
                }

                /* Add metadata to database. */
                success = insertUploadMetadata(request, uploadItems);
	            /* Add value sets to CTS2 service */
	            if (success) {
		            success = insertValueSets(uploadItems, request);
	            }
            } else {
                success = false;
                uploadItems.addMessage("An error occurred while writing the files to the disk.");
                logger.log(Level.WARNING, "Unable to create algorithm directory " + destPath + ".");
            }
        } catch (IOException ioe) {
            success = false;
            logger.log(Level.WARNING, "An error occurred while persisting the files.", ioe);
        }

        return success;

    }

    public boolean insertUploadMetadata(HttpServletRequest request, UploadItems uploadItems) {

        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        boolean isSuccessful = false;
        conn = DBConnection.getDBConnection(getBasePath(request));
        int numSibs = -1;

        if (conn != null) {
            try {
                // insert the algorithm
                String prefix = uploadItems.getPrefix() + "/";
                st = conn.prepareStatement(SQLStatements.insertUploadStatement(uploadItems));

                st.setString(1, uploadItems.getId());
                st.setString(2, uploadItems.getName());
                st.setString(3, uploadItems.getUser());
                st.setString(4, uploadItems.getVersion());
                st.setString(5, uploadItems.getDescription());
                st.setString(6, uploadItems.getInstitution());
                st.setDate(7, new java.sql.Date(uploadItems.getCreateDate().getTime()));
                st.setString(8, uploadItems.getComment());
                st.setString(9, prefix + uploadItems.getXmlFile().getName());
                st.setString(10, prefix + uploadItems.getXlsFile().getName());
                st.setString(11, prefix + uploadItems.getHtmlFile().getName());
                st.setString(12, prefix + uploadItems.getZipFile().getName());
                st.setString(13, uploadItems.getDocFile() != null ? prefix
                        + uploadItems.getDocFile().getName() : "");
                st.setString(14, uploadItems.getStatus());
                st.setString(15, uploadItems.getAssocLink());
                st.setString(16, uploadItems.getAssocName());
                st.setDate(17, new java.sql.Date(System.currentTimeMillis()));

                st.execute();

                // count how many algorithms in the upload table have the same
                // parent
                String currCategoryId = uploadItems.getId();
                st = conn.prepareStatement(SQLStatements.countAlgorithmSiblings(currCategoryId));
                rs = st.executeQuery();
                while (rs.next()) {
                    numSibs = rs.getInt(1);
                }

                while (!currCategoryId.equals("0")) { // stop when reach root
                    // update the leaf category parent with the number of
                    // children algorithms
                    st = conn.prepareStatement(SQLStatements.updateCategoryParentCount(numSibs,
                            currCategoryId));
                    st.execute();

                    // get the parent of the Category
                    String categoryParentId = "-1";
                    st = conn.prepareStatement(SQLStatements
                            .selectCategoryParentStatement(currCategoryId));
                    rs = st.executeQuery();
                    while (rs.next()) {
                        categoryParentId = rs.getString(1);
                    }

                    // sum up the num algorithms for each sibling category under
                    // that parent
                    st = conn.prepareStatement(SQLStatements
                            .selectCategorySiblingsStatement(categoryParentId));
                    rs = st.executeQuery();
                    numSibs = 0;
                    while (rs.next()) {
                        numSibs = numSibs + rs.getInt(1);
                    }

                    // take a step up the tree - the parent is now current
                    currCategoryId = categoryParentId;
                }
                isSuccessful = true;

            } catch (Exception ex) {
                isSuccessful = false;
                uploadItems.addMessage("Failed to write the metadata to the database.");
                logger.log(Level.SEVERE,
                        "Failed to insert meta data information. Error: " + ex.getMessage(), ex);
                if (ex.getMessage().contains("Duplicate entry")) {
                    uploadItems.addMessage("This file has already been uploaded.");
                }
            } finally {
                DBConnection.closeConnection(conn, st, rs);
            }
        }
        return isSuccessful;
    }

	private boolean insertValueSets(UploadItems uploadItems, HttpServletRequest request) throws IOException {
		boolean success;
		File zipFile = uploadItems.getZipFile();
		String url = getValueSetServiceUrl(request) + MAT_UPLOAD_PATH;
		HttpClient client = new DefaultHttpClient();
		client.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION,
		  HttpVersion.HTTP_1_1);
		HttpContext context = new BasicHttpContext();
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials(
		  AuthScope.ANY,
		  new UsernamePasswordCredentials(getValueSetServiceUser(request), getValueSetServicePassword(request)));
		context.setAttribute(ClientContext.CREDS_PROVIDER, credsProvider);
		int status = -1;
		HttpResponse response = null;

		try {
			HttpPost post = new HttpPost(url);
			MultipartEntity entity = new MultipartEntity(
			  HttpMultipartMode.BROWSER_COMPATIBLE);
			entity.addPart("zipType", new StringBody("single", "text/plain",
			  Charset.forName("UTF-8")));
			entity.addPart("file", new FileBody(zipFile, "application/zip"));
			post.setEntity(entity);

			client.execute(post, context);
			success = true;

		} catch (IOException ioe) {
			logger.log(
			  Level.WARNING,
			  "An error occurred while uploading the MAT value sSets to the CTS2 service.",
			  ioe);
			success = false;
		} finally {
			client.getConnectionManager().shutdown();
		}

		return success;
	}

    private void processDownloadAlgorithm(HttpServletRequest request, HttpServletResponse response) {

        String zipPath = request.getParameter("ZipFilePath");

        File algorithmFile = new File(getAlgorithmPath(request) + "/" + zipPath);
        logger.info("Download requested: " + algorithmFile.getAbsolutePath());

        if (algorithmFile.exists()) {
            logger.info("Sending file " + algorithmFile.getAbsolutePath());
            response.setContentType("application/octet-stream");
            response.setContentLength((int) algorithmFile.length());
            response.setHeader("Content-Disposition",
                    "attachment; filename=\"" + algorithmFile.getName() + "\"");

            try {
                OutputStream out = response.getOutputStream();
                Files.copy(algorithmFile, out);
                out.flush();
                out.close();
            } catch (IOException ioe) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                logger.log(Level.WARNING,
                        "Unable to write the file to the response output stream.", ioe);
            }

            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            logger.log(Level.WARNING,
                    String.format("Requested algorithm file (%s) was not found.", zipPath));
        }
    }

}
