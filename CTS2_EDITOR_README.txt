How to use the cts2Editor in the HTP client (Both SmartGWT)

   * cts2Editor can be found here:

      * https://github.com/cts2/cts2-valueset-editor.git
   * Get the cts2Editor from GitHub
   * Compile cts2Editor project
   * Export project as a jar

      * Only export the /src and /lib directories.  
      * Export the source files too
   * Copy the jar to the HTP project

      * /lib
      * /war/WEB-INF/lib 
   * Copy the /war/data/images to /war/data/images in the new project
   * Update Htp project properties
      * project properties->Java Build Path -> add Jars… - select the cts2Editor.jar
      
   * Update Htp.gwt.xml file

      * <!-- add the CTS2 Editor *** PATH TO the gwt.xml file-->
		<inheritsname="mayo.edu.cts2.editor.Cts2Editor"/>
   	  * Add to /war/WEB-INF/web.xml

      * <!-- servlet for cts2Editor -->
		<servlet>
    		<servlet-name>Cts2EditorServlet</servlet-name>
    		<servlet-class>mayo.edu.cts2.editor.server.Cts2EditorServiceImpl</servlet-class>
		</servlet>
 
		<servlet-mapping>
		    <servlet-name>Cts2EditorServlet</servlet-name>
		    <url-pattern>/htp/cts2Editor</url-pattern>
		</servlet-mapping>
   * Update the criteria tab to use the cts2Editor


