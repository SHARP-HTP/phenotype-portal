package edu.mayo.phenoportal.utils;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import edu.mayo.phenoportal.server.database.DBConnection;

/**
 * This class inserts data into the Category table for use in the hierarchy tree
 * The original Word doc file has to be modified for this program. Change the
 * list formatting to use Article and Section instead of the bullet points
 * because the first and third level bullets are the same when converted to
 * text. Did remove the introduction portion of the file and start right at
 * first grandparent. Save the file as text. Did look at using poi, but all
 * formatting was removed so could not distinguish hierarchy.
 */
public class CategoryTableCreator extends RemoteServiceServlet {

    private static ArrayList<String> i_inserts = new ArrayList<String>();
    private static String i_fileLocation = "data/PhenoPortal-Ontology-Tree-V3.txt";

    public static void main(String[] args) {

        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        conn = DBConnection.getDBConnection("war/");

        try {
            conn.setAutoCommit(false);
            st = conn.createStatement();

            getInsertStatements();
            for (Iterator<String> iterator = i_inserts.iterator(); iterator.hasNext();) {
                String insert = iterator.next();
                System.out.println(insert);
                st.addBatch(insert);
            }

            st.executeBatch();
            conn.commit();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection(conn, st, rs);
        }
    }

    static private void getInsertStatements() {
        boolean hasParent = false;
        int totalCategories = 0;
        int currGPID = 0;
        int currPID = 0;
        int parentID = 0;
        int level = 0;

        try {
            FileInputStream fstream = new FileInputStream(i_fileLocation);
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            // Read File Line By Line
            while ((strLine = br.readLine()) != null) {

                totalCategories++;
                if (strLine.startsWith("Article")) {
                    strLine = strLine.substring(strLine.indexOf(" ", 8) + 1);
                    currGPID = totalCategories;
                    parentID = 0;
                    hasParent = true;
                    level = 1;
                } else if (strLine.contains("Section")) {
                    strLine = strLine.substring(strLine.indexOf(" ", 8) + 1);
                    currPID = totalCategories;
                    parentID = currGPID;
                    level = 2;
                } else {
                    strLine = strLine.substring(strLine.indexOf(" ") + 1);
                    parentID = currPID;
                    level = 3;
                }

                if (strLine != null && hasParent) {
                    i_inserts.add("INSERT INTO Category(id, Name, ParentId, Count, Level) "
                            + "VALUES('" + totalCategories + "', '" + strLine + "','" + parentID
                            + "','0','" + level + "');");
                }
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
