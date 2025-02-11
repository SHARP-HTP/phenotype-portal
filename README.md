Imported from svn @ http://sourceforge.net/p/sharpn/htp/code/HEAD/tree/trunk/phenotype-portal/

# PhenotypePortal.org #
## What is the Phenotype Portal? ##

Phenotyping is the process of identifying a cohort of patients based on certain diseases, symptoms or clinical findings. The Phenotype Portal is a tool funded by the SHARPn Project from the Office of the National Coordinator (ONC). It will enable clinicians and investigators to identify patient cohorts using electronic health record (EHR) data by leveraging informatics-based phenotyping processes. In turn, these cohorts will facilitate clinical trial enrollment, outcomes research, and inform clinical decision support. Currently, the field has various barriers in technological research and tool development, and Phenotype Portal is the first such platform for generating and executing Meaningful Use standards-based phenotyping algorithms that can be shared across multiple institutions and investigators. 

Traditionally, a patient's medical information is stored inconsistently and in multiple locations, both electronically and non-electronically. The Phenotype Portal will work towards creating a unified framework for normalizing and standardizing clinical data, which will allow for the exchange of patient information among care providers, government agencies, insurers and other stake holders. 

For technical information about this project including source code downloads and installation instructions, please visit the <a class="htpLinks" href="http://docs.phenotypeportal.org">QDM Phenotyping Documentation</a> page.

# PhenotypePortal Installation Guide #

## Requirements ##
+ [UTS](https://uts.nlm.nih.gov/home.html) credentials and license code
+ JDK 1.6+
+ [GWT 2.4.0](http://www.gwtproject.org/versions.html)
+ [MySQL 5.1+](https://dev.mysql.com/downloads/mysql/)
+ [Ant](http://ant.apache.org/bindownload.cgi)
+ Java Servlet Container ([Tomcat 7](http://tomcat.apache.org/download-70.cgi))
+ [Git](http://git-scm.com/book/en/Getting-Started-Installing-Git)
+ [CTS2 Framework](http://informatics.mayo.edu/cts2/framework/download/) + [mat-service](https://github.com/suesse/mat-service/releases/tag/htp-release) (installation outlined below)

## Installation ##
### Download Source ###
  ```$ git clone https://github.com/SHARP-HTP/phenotype-portal.git```

### Configure Database ###
1. Create ‘htpdb’ database in MySQL: ```mysql> create database htpdb;```
2. Import database schema: ```phenotype-portal$ mysql htpdb < data/htp.sql```
3. Import initial data: ```phenotype-portal$ mysql htpdb < data/htp.data.sql```

### Configure properties ###
1. Database: ```phenotype-portal/src/edu/mayo/phenoportal/utils/database.properties```
2. WebApp: ```phenotype-portal/src/edu/mayo/phenoportal/utils/Startup.properties```
3. Create directories: Create a new directory ```tomcat7/data/algorithms``` for the algorithms and another ```tomcat7/data/executions``` for the executions to be stored. This is where the algorithmsPath and executionResultsPath properties in the Startup.properties file will point to.

### Build Source ###
1. Edit ```phenotype-portal/build.properties``` to reflect your development environment.
2. Build: ```phenotype-portal$ ant clean war```

### Deploy ###
Deploy ```phenotype-portal/dist/htp.war``` to Java Servlet Container.
### Test the service ###
Navigate to ```http://localhost:8080/htp```
## CTS2 Framework + mat-service Installation ##
Follow the guide on the website to install the CTS2 Development Framework: ```http://informatics.mayo.edu/cts2/framework/```

### Load the plugin-util bundle 0.8.4 ###
Download [plugin-util-0.8.4](http://informatics.mayo.edu/maven/content/repositories/releases/edu/mayo/cts2/framework/plugin-util/0.8.4/plugin-util-0.8.4.jar): ```http://informatics.mayo.edu/maven/content/repositories/releases/edu/mayo/cts2/framework/plugin-util/0.8.4/plugin-util-0.8.4.jar```

Install the plugin-util bundle in CTS2 framework console.

### Load the mat-service bundle 0.8.2 ###
Download [mat-service-0.8.2](https://github.com/suesse/mat-service/releases/download/htp-release/mat-service-0.8.2.jar): ```https://github.com/suesse/mat-service/releases/download/htp-release/mat-service-0.8.2.jar```

Install the mat-service bundle in CTS2 framework console. ￼

### Configure CTS2 Framework ###
#### Configure ServerContext (Example) ####
    Server Root: http://localhost:8080
    Webapp Name: cts2

#### Configure MAT Zip Output Plugin (Example) ####
    UTS Service Name: http://umlsks.nlm.nih.gov
    UTS Username: myUmlsUsername
    UTS Password: myPassword
    UTS Auth Service Url: https://uts-ws.nlm.nih.gov/restful/IsValidUMLSUser
    UTS License Code: NLM-MyLicenseCode
    UTS UMLS Release: 2012AA
    JDBC URL: jdbc:mysql://127.0.0.1:3306/valuesets
    JDBC Driver Class: com.mysql.jdbc.Driver
    JDBC Username: myDbUser
    JDBC Password: myDbPass
    NamespaceService URL: http://informatics.mayo.edu/cts2/services/bioportal-rdf/
    UriResolutionService URL: https://informatics.mayo.edu/cts2/services/uriresolver
    Security Check URL: /j_spring_security_check
    Hibernate SQL Dialect: org.hibernate.dialect.MySQLDialect

#### Start the mat-service bundle ####
#### Test the service ####
Navigate to ```http://localhost:8080/cts2/valuesets```

### Load the NQF2014 Value Sets ###
**Note:** This requires 4Gb of heap space dedicated to the Tomcat JVM.

#### Get Value Sets from [USHIK](http://ushik.ahrq.gov) ####
[Meaningful Use Value Sets](http://ushik.ahrq.gov/DownloadFileExport?fileName=Stage2/ValueSets/20130614/meaningful_use_2014_extract_xlsx_20130614.zip)

#### Load Value Sets ####
Navigate to ```http://localhost:8080/upload/nqf2014.html``` and upload the valueset zip file.