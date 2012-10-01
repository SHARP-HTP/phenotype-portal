-- MySQL dump 10.11
--
-- Host: localhost    Database: htp
-- ------------------------------------------------------
-- Server version	5.0.27

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `Category`
--

DROP TABLE IF EXISTS `Category`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `Category` (
  `id` int(11) unsigned NOT NULL auto_increment COMMENT 'category id for this entry',
  `Name` varchar(150) NOT NULL COMMENT 'name of category',
  `ParentId` int(10) NOT NULL COMMENT 'parent category''s category id',
  `Count` int(10) default NULL COMMENT 'how many algorithms uploaded for this category',
  `Level` int(10) default NULL COMMENT 'gp-1, parent-2, child-3',
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=454 DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC COMMENT='Holds information for the Phenotype tree';
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `Execution`
--

DROP TABLE IF EXISTS `Execution`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `Execution` (
  `id` varchar(64) NOT NULL COMMENT 'the unique id of the execution',
  `user` varchar(15) NOT NULL COMMENT 'the user who executed the algorithm',
  `algorithmName` varchar(100) NOT NULL COMMENT 'the name of the algorithm',
  `version` varchar(10) NOT NULL COMMENT 'the version of the algorithm',
  `categoryNum` int(11) default NULL COMMENT 'the id of the category the algorithm is in',
  `categoryId` varchar(500) NOT NULL COMMENT 'the path (description) of the category the algorith is in',
  `startDate` varchar(50) NOT NULL COMMENT 'the date the algorithm was executed on',
  `endDate` varchar(50) default NULL COMMENT 'the date the algorithm finished executed on',
  `status` varchar(20) default NULL COMMENT 'the execution status (ERROR, COMPLETE)',
  `elapsedTime` varchar(20) default NULL COMMENT 'the amount of time it took the algorithm to execute (uom is attached)',
  `dateRangeFrom` varchar(50) default NULL COMMENT 'the from date of the execution',
  `dateRangeTo` varchar(50) default NULL COMMENT 'the to date of the execution',
  `xmlPath` varchar(400) default NULL COMMENT 'the path to the returned xml file',
  `imagePath` varchar(400) default NULL COMMENT 'the path to the returned image file',
  `bpmnPath` varchar(400) default NULL COMMENT 'the path to the returned bpmn file',
  `rulesPath` varchar(400) default NULL COMMENT 'the path to the returned rules file',
  `timestamp` timestamp NOT NULL default CURRENT_TIMESTAMP COMMENT 'the timestamp of when the execution  record was entered to the database',
  PRIMARY KEY  (`user`,`algorithmName`,`version`,`categoryId`,`startDate`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `News`
--

DROP TABLE IF EXISTS `News`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `News` (
  `id` int(11) unsigned NOT NULL auto_increment,
  `date` varchar(50) NOT NULL default '',
  `information` varchar(500) NOT NULL default '',
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=11111212 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `SharpNews`
--

DROP TABLE IF EXISTS `SharpNews`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `SharpNews` (
  `id` int(11) unsigned NOT NULL auto_increment,
  `information` varchar(500) NOT NULL default '',
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=22 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `Upload`
--

DROP TABLE IF EXISTS `Upload`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `Upload` (
  `parentID` varchar(10) NOT NULL COMMENT 'to which this belongs',
  `name` varchar(100) NOT NULL COMMENT 'user input name for algorithm',
  `version` varchar(10) NOT NULL COMMENT 'multiple versions of algorithm may be uploaded',
  `user` varchar(50) NOT NULL COMMENT 'user who uploaded the file',
  `description` varchar(1000) default NULL COMMENT 'description of the algorithm''s purpose',
  `institution` varchar(50) default NULL COMMENT 'institution of algorithm creation',
  `creationDate` varchar(50) default NULL COMMENT 'date algorithm created/revised',
  `comments` varchar(1000) NOT NULL COMMENT 'any additional information',
  `status` varchar(20) default NULL COMMENT 'final, testing, under development',
  `xmlFile` varchar(400) default NULL COMMENT 'the uploaded xml file',
  `xlsFile` varchar(400) default NULL COMMENT 'the uploaded xsl file',
  `htmlFile` varchar(400) default NULL COMMENT 'the uploaded html file',
  `zipFile` varchar(400) default NULL COMMENT 'the original uploaded file',
  `wordFile` varchar(400) default NULL COMMENT 'optional word algorithm desc file',
  `assocLink` varchar(200) default NULL COMMENT 'optional link to pheKB site',
  `assocName` varchar(100) default NULL COMMENT 'name of optional link to pheKB site',
  `uploadDate` varchar(50) default NULL,
  PRIMARY KEY  (`parentID`,`name`,`version`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC COMMENT='This table contains the information about the uploaded files';
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `User`
--

DROP TABLE IF EXISTS `User`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `User` (
  `username` varchar(15) NOT NULL COMMENT 'user created identity',
  `fname` varchar(25) NOT NULL COMMENT 'user''s real first name',
  `lname` varchar(25) NOT NULL COMMENT 'user''s real last name',
  `email` varchar(50) NOT NULL COMMENT 'user''s email address',
  `password` varchar(75) NOT NULL COMMENT 'obfuscated password',
  `role` int(1) NOT NULL default '3' COMMENT '1-admin, 2-execute, 3-read',
  `enable` int(1) NOT NULL default '1' COMMENT '1-enabled, 2-disable',
  PRIMARY KEY  (`username`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COMMENT='This table contains the user registration information.';
SET character_set_client = @saved_cs_client;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2012-08-31  4:19:42
