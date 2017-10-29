# MySQL-Front 5.1  (Build 3.57)

/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE */;
/*!40101 SET SQL_MODE='NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES */;
/*!40103 SET SQL_NOTES='ON' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS */;
/*!40014 SET FOREIGN_KEY_CHECKS=0 */;


# Host: localhost    Database: hetuo
# ------------------------------------------------------
# Server version 5.0.45-community-nt-log

#
# Source for table article
#

DROP TABLE IF EXISTS `article`;
CREATE TABLE `article` (
  `id` int(11) NOT NULL auto_increment,
  `subject_eng` char(255) default NULL,
  `subject_chi` char(255) default NULL,
  `time` int(30) default NULL,
  `content_chi` longtext,
  `content_eng` longtext,
  `img` char(255) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

#
# Dumping data for table article
#
LOCK TABLES `article` WRITE;
/*!40000 ALTER TABLE `article` DISABLE KEYS */;

INSERT INTO `article` VALUES (9,'i','开',1286129042,'企','f','from-download1.gif');
INSERT INTO `article` VALUES (10,'fds','9084',1286133059,'風刀霜劍','klfjdl','ico71.gif');
/*!40000 ALTER TABLE `article` ENABLE KEYS */;
UNLOCK TABLES;

#
# Source for table company
#

DROP TABLE IF EXISTS `company`;
CREATE TABLE `company` (
  `id` int(11) NOT NULL auto_increment,
  `subject_eng` char(255) default NULL,
  `subject_chi` char(255) default NULL,
  `time` int(30) default NULL,
  `content_chi` longtext,
  `content_eng` longtext,
  `img` char(255) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

#
# Dumping data for table company
#
LOCK TABLES `company` WRITE;
/*!40000 ALTER TABLE `company` DISABLE KEYS */;

INSERT INTO `company` VALUES (9,'d','快',1286129009,'付','f','btn_down_n.gif');
/*!40000 ALTER TABLE `company` ENABLE KEYS */;
UNLOCK TABLES;

#
# Source for table head
#

DROP TABLE IF EXISTS `head`;
CREATE TABLE `head` (
  `Id` int(11) NOT NULL auto_increment,
  `picname` char(255) default NULL,
  `picpath` char(255) default NULL,
  `serverpath` char(255) default NULL,
  `servername` char(255) default NULL,
  `link` mediumtext,
  `time` char(255) default NULL,
  `sort` double(5,1) default NULL,
  PRIMARY KEY  (`Id`)
) ENGINE=MyISAM AUTO_INCREMENT=34 DEFAULT CHARSET=utf8;

#
# Dumping data for table head
#
LOCK TABLES `head` WRITE;
/*!40000 ALTER TABLE `head` DISABLE KEYS */;

INSERT INTO `head` VALUES (29,'good','06.jpg',NULL,NULL,'www.google.com','1299690602',4);
INSERT INTO `head` VALUES (30,'good','061.jpg',NULL,NULL,'http://www.baidu.com','1299690437',0);
INSERT INTO `head` VALUES (31,'very good','cList.jpg',NULL,NULL,'http://www.google.com','1299690878',0);
INSERT INTO `head` VALUES (32,'nihao','latest-right.jpg',NULL,NULL,'http://www.baidu.com','1299690907',19);
INSERT INTO `head` VALUES (33,'爱国者','rotator_number_c.png',NULL,NULL,'http://www.google.com','1299691059',0);
INSERT INTO `head` VALUES (34,'iloveyou','2.jpg',NULL,NULL,'http://google.com','1299691122',0);
INSERT INTO `head` VALUES (35,'123','112.jpg',NULL,NULL,'http://123','1299691136',0);
/*!40000 ALTER TABLE `head` ENABLE KEYS */;
UNLOCK TABLES;

#
# Source for table product
#

DROP TABLE IF EXISTS `product`;
CREATE TABLE `product` (
  `id` int(11) NOT NULL auto_increment,
  `subject_eng` char(100) default NULL,
  `subject_chi` char(100) default NULL,
  `img1` char(50) default NULL,
  `img2` char(50) default NULL,
  `content_eng` longtext,
  `content_chi` longtext,
  `time` int(30) default NULL,
  `number` char(30) default NULL,
  `trait_eng` longtext,
  `trait_chi` longtext,
  `app_eng` longtext,
  `app_chi` longtext,
  `corporate` tinyint(1) default '0',
  `tv` tinyint(1) default '0',
  `print` tinyint(1) default '0',
  `advertising` tinyint(1) default '0',
  `promotion` tinyint(1) default '0',
  `event` tinyint(1) default '0',
  `premeium` tinyint(1) default '0',
  `video` tinyint(1) default '0',
  `click` int(11) default '0',
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=23 DEFAULT CHARSET=utf8;

#
# Dumping data for table product
#
LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;

INSERT INTO `product` VALUES (13,'my','mz','1282378272_packing.png','ico5.gif','jy我爱你','我是狂野先生',1286123112,'66uu','ty','td','yz','yy',1,0,1,0,1,0,0,1,2);
INSERT INTO `product` VALUES (15,'789','好','ico7.gif','from-download.gif','78678','快结婚',1286123157,'786h','7687','快结婚方法','7f','辅导书',0,0,1,0,0,0,0,1,8);
INSERT INTO `product` VALUES (16,'trw','负担',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,0,0,0,0,0,0,1,9);
INSERT INTO `product` VALUES (17,'fdsa','进口量',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,0,0,0,0,0,0,1,3);
INSERT INTO `product` VALUES (18,'fdsa','负担',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,0,0,0,0,0,0,1,0);
INSERT INTO `product` VALUES (19,'fdsfa','fdsa','fdsa','fdsa','fdsa','fdsfadsfada\r\n',NULL,NULL,NULL,NULL,NULL,NULL,0,0,0,0,0,0,0,1,0);
INSERT INTO `product` VALUES (20,'fdsaf','fdsa',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,0,0,0,0,0,0,1,0);
INSERT INTO `product` VALUES (21,'fdsfad','dsfds',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,0,0,0,0,0,0,1,0);
INSERT INTO `product` VALUES (22,'fdsafdsa','fdafdafds',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,0,0,0,0,0,0,1,0);
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;

#
# Source for table shows
#

DROP TABLE IF EXISTS `shows`;
CREATE TABLE `shows` (
  `id` int(11) NOT NULL auto_increment,
  `subject_eng` char(255) default NULL,
  `subject_chi` char(255) default NULL,
  `time` int(30) default NULL,
  `content_chi` longtext,
  `content_eng` longtext,
  `img` char(255) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

#
# Dumping data for table shows
#
LOCK TABLES `shows` WRITE;
/*!40000 ALTER TABLE `shows` DISABLE KEYS */;

INSERT INTO `shows` VALUES (9,'f','辅',1286129025,'房','f','ico44.gif');
/*!40000 ALTER TABLE `shows` ENABLE KEYS */;
UNLOCK TABLES;

#
# Source for table skill
#

DROP TABLE IF EXISTS `skill`;
CREATE TABLE `skill` (
  `id` int(11) NOT NULL auto_increment,
  `subject_eng` char(255) default NULL,
  `subject_chi` char(255) default NULL,
  `time` int(30) default NULL,
  `content_chi` longtext,
  `content_eng` longtext,
  `img` char(255) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

#
# Dumping data for table skill
#
LOCK TABLES `skill` WRITE;
/*!40000 ALTER TABLE `skill` DISABLE KEYS */;

INSERT INTO `skill` VALUES (8,'123','456',1286125100,'1011112','789','ico3.gif');
INSERT INTO `skill` VALUES (9,'i and you','我和你',1286125085,'你是谁','nishishui','close.png');
INSERT INTO `skill` VALUES (10,'adf','爱上对方',1286128785,'房价肯定还是','fd','ico42.gif');
INSERT INTO `skill` VALUES (11,'kjhf','防恐惧的是',1286128803,'看见繁华的开始','fkdh','ico22.gif');
/*!40000 ALTER TABLE `skill` ENABLE KEYS */;
UNLOCK TABLES;

#
# Source for table user
#

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL auto_increment,
  `user` char(32) default NULL,
  `pw` char(32) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

#
# Dumping data for table user
#
LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;

INSERT INTO `user` VALUES (1,'admin','123');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
