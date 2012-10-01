package edu.mayo.phenoportal.server.upload;

import edu.mayo.phenoportal.client.upload.ClientUploadItems;
import edu.mayo.phenoportal.client.upload.FileService;
import edu.mayo.phenoportal.server.database.DBConnection;
import edu.mayo.phenoportal.shared.database.UploadColumns;
import edu.mayo.phenoportal.utils.SQLStatements;
import edu.mayo.phenotype.server.BasePhenoportalServlet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileServiceImpl extends BasePhenoportalServlet implements FileService {

    private static final long serialVersionUID = 1L;
    static final Logger lgr = Logger.getLogger(FileServiceImpl.class.getName());

    @Override
    public ClientUploadItems retrieveUploadMetadata(String parentId, String fileName, String version) {

        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        ClientUploadItems clientUploadItems = new ClientUploadItems();
        conn = DBConnection.getDBConnection(getBasePath());

        if (conn != null) {
            try {

                st = conn.prepareStatement(SQLStatements.selectUploadStatement(parentId, fileName,
                        version));

                rs = st.executeQuery();

                while (rs.next()) {
                    clientUploadItems.setId(rs.getString(UploadColumns.ID.colNum()));
                    clientUploadItems.setName(rs.getString(UploadColumns.NAME.colNum()));
                    clientUploadItems.setUser(rs.getString(UploadColumns.USER.colNum()));
                    clientUploadItems.setVersion(rs.getString(UploadColumns.VERSION.colNum()));
                    clientUploadItems.setDescription(rs.getString(UploadColumns.DESCRIPTION.colNum()));
                    clientUploadItems.setInstitution(rs.getString(UploadColumns.INSTITUTION.colNum()));
                    clientUploadItems.setCreateDate(rs.getString(UploadColumns.CREATEDATE.colNum()));
                    clientUploadItems.setComment(rs.getString(UploadColumns.COMMENT.colNum()));
                    clientUploadItems.setStatus(rs.getString(UploadColumns.STATUS.colNum()));
                    clientUploadItems.setAssocLink(rs.getString(UploadColumns.ASSOC_LINK.colNum()));
                    clientUploadItems.setAssocName(rs.getString(UploadColumns.ASSOC_NAME.colNum()));
                    clientUploadItems.setZipFile(rs.getString(UploadColumns.ZIP_FILE.colNum()));
                    clientUploadItems.setHtmlFile(rs.getString(UploadColumns.HTML_FILE.colNum()));
                    clientUploadItems.setXmlFile(rs.getString(UploadColumns.XML_FILE.colNum()));
                    clientUploadItems.setXlsFile(rs.getString(UploadColumns.XLS_FILE.colNum()));
                    clientUploadItems.setDocFile(rs.getString(UploadColumns.WORD_FILE.colNum()));
                }

            } catch (Exception ex) {

                lgr.log(Level.SEVERE,
                        "Failed to retrieve meta data information" + ex.getStackTrace(), ex);

            } finally {
                DBConnection.closeConnection(conn, st, rs);
            }
        }
        return clientUploadItems;
    }

}
