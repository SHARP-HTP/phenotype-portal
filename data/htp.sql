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
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC COMMENT='Holds information for the Phenotype tree';
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `Execution`
--

DROP TABLE IF EXISTS `Execution`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `Execution` (
		`id` varchar(64) NOT NULL,
		`user` varchar(15) NOT NULL,
		`algorithmName` varchar(100) NOT NULL,
		`version` varchar(10) NOT NULL,
		`categoryNum` int(11) default NULL,
		`categoryId` varchar(500) NOT NULL,
		`startDate` varchar(50) NOT NULL,
		`endDate` varchar(50) default NULL,
		`status` varchar(20) default NULL,
		`elapsedTime` varchar(20) default NULL,
		`dateRangeFrom` varchar(50) default NULL,
		`dateRangeTo` varchar(50) default NULL,
		`xmlPath` varchar(400) default NULL,
		`imagePath` varchar(400) default NULL,
		`bpmnPath` varchar(400) default NULL,
		`rulesPath` varchar(400) default NULL,
		`timestamp` timestamp NOT NULL default CURRENT_TIMESTAMP,
		PRIMARY KEY  (`user`,`algorithmName`,`version`,`categoryId`,`startDate`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `Upload`
--

DROP TABLE IF EXISTS `Upload`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `Upload` (
		`id` int(11) unsigned NOT NULL auto_increment,
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
		`type` varchar(50) NOT NULL default "UNKNOWN" COMMENT 'the type of algorithm, NQF2013, NQF2014, ...',
		PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC COMMENT='This table contains the information about the uploaded files';
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
		`registrationdate` varchar(50) NOT NULL default '' COMMENT 'date the user registered',
		`countryregion` varchar(50) NOT NULL default '' COMMENT 'country or region',
		`streetaddress` varchar(50) NOT NULL default '' COMMENT 'street address',
		`city` varchar(50) NOT NULL default '' COMMENT 'city',
		`state` varchar(50) NOT NULL default '' COMMENT 'state or region if not in US',
		`zipcode` varchar(25) NOT NULL default '' COMMENT 'zip code',
		`phonenumber` varchar(25) NOT NULL default '' COMMENT 'phone number',
		PRIMARY KEY  (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='This table contains the user registration information.';
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `UserRoleRequest`
--

DROP TABLE IF EXISTS `UserRoleRequest`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `UserRoleRequest` (
		`id` int(11) unsigned NOT NULL auto_increment,
		`username` varchar(15) NOT NULL default '' COMMENT 'username from the User table',
		`requestdate` varchar(50) NOT NULL default '' COMMENT 'date the user made this request',
		`responsedate` varchar(50) default '' COMMENT 'date the admin responded',
		`requestgranted` tinyint(1) default NULL COMMENT 'admin''s response: 0 not granted, 1 granted ',
		PRIMARY KEY  (`id`),
		KEY `usernameRelationship` (`username`),
		CONSTRAINT `usernameRelationship` FOREIGN KEY (`username`) REFERENCES `User` (`username`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-04-26 21:03:27
