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
                    clientUploadItems.setId(rs.getInt("id"));
                    clientUploadItems.setParentId(rs.getString("parentId"));
                    clientUploadItems.setName(rs.getString("name"));
                    clientUploadItems.setUser(rs.getString("user"));
                    clientUploadItems.setVersion(rs.getString("version"));
                    clientUploadItems.setDescription(rs.getString("description"));
                    clientUploadItems.setInstitution(rs.getString("institution"));
                    clientUploadItems.setCreateDate(rs.getString("creationDate"));
                    clientUploadItems.setComment(rs.getString("comments"));
                    clientUploadItems.setStatus(rs.getString("status"));
                    clientUploadItems.setAssocLink(rs.getString("assocLink"));
                    clientUploadItems.setAssocName(rs.getString("assocName"));
                    clientUploadItems.setZipFile(rs.getString("zipFile"));
                    clientUploadItems.setHtmlFile(rs.getString("htmlFile"));
                    clientUploadItems.setXmlFile(rs.getString("xmlFile"));
                    clientUploadItems.setXlsFile(rs.getString("xlsFile"));
                    clientUploadItems.setDocFile(rs.getString("wordFile"));
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
