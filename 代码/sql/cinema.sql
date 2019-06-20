-- MySQL dump 10.13  Distrib 5.7.17, for macos10.12 (x86_64)
--
-- Host: 47.101.183.63    Database: cinema
-- ------------------------------------------------------
-- Server version	5.7.24

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
-- Table structure for table `activity`
--

SET @@session.sql_mode ='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION';

DROP TABLE IF EXISTS `activity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `activity` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `activity_name` varchar(45) NOT NULL,
  `a_description` varchar(255) NOT NULL,
  `end_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `coupon_id` int(11) DEFAULT NULL,
  `start_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activity`
--

LOCK TABLES `activity` WRITE;
/*!40000 ALTER TABLE `activity` DISABLE KEYS */;
INSERT INTO `activity` VALUES (2,'春季外卖节','春季外卖节','2019-04-23 17:55:59',5,'2019-04-20 17:55:59'),(3,'春季外卖节','春季外卖节','2019-04-23 17:55:59',6,'2019-04-20 17:55:59'),(4,'测试活动','测试活动','2019-04-26 16:00:00',8,'2019-04-20 16:00:00');
/*!40000 ALTER TABLE `activity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `activity_movie`
--

DROP TABLE IF EXISTS `activity_movie`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `activity_movie` (
  `activity_id` int(11) DEFAULT NULL,
  `movie_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activity_movie`
--

LOCK TABLES `activity_movie` WRITE;
/*!40000 ALTER TABLE `activity_movie` DISABLE KEYS */;
INSERT INTO `activity_movie` VALUES (2,10),(2,11),(2,16),(4,10);
/*!40000 ALTER TABLE `activity_movie` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `coupon`
--

DROP TABLE IF EXISTS `coupon`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `coupon` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(45) DEFAULT NULL,
  `target_amount` float DEFAULT NULL,
  `discount_amount` float DEFAULT NULL,
  `start_time` timestamp NOT NULL ON UPDATE CURRENT_TIMESTAMP,
  `end_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `coupon`
--

LOCK TABLES `coupon` WRITE;
/*!40000 ALTER TABLE `coupon` DISABLE KEYS */;
INSERT INTO `coupon` VALUES (1,'测试优惠券','春季电影节',20,5,'2019-04-20 17:47:54','2019-04-23 17:47:59'),(5,'测试优惠券','品质联盟',30,4,'2019-04-20 21:14:46','2019-04-24 21:14:51'),(6,'春节电影节优惠券','电影节优惠券',50,10,'2019-04-20 21:15:11','2019-04-21 21:14:56'),(8,'测试优惠券','123',100,99,'2019-04-20 16:00:00','2019-04-26 16:00:00');
/*!40000 ALTER TABLE `coupon` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `coupon_user`
--

DROP TABLE IF EXISTS `coupon_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `coupon_user` (
  `coupon_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `coupon_user`
--

LOCK TABLES `coupon_user` WRITE;
/*!40000 ALTER TABLE `coupon_user` DISABLE KEYS */;
INSERT INTO `coupon_user` VALUES (8,15),(5,15),(8,15),(6,15),(5,15),(8,15),(6,15);
/*!40000 ALTER TABLE `coupon_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hall`
--

DROP TABLE IF EXISTS `hall`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hall` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `column` int(11) DEFAULT NULL,
  `row` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hall`
--

LOCK TABLES `hall` WRITE;
/*!40000 ALTER TABLE `hall` DISABLE KEYS */;
INSERT INTO `hall` VALUES (1,'1号厅',10,5),(2,'2号厅',12,8);
/*!40000 ALTER TABLE `hall` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `movie`
--

DROP TABLE IF EXISTS `movie`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `movie` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `poster_url` varchar(255) DEFAULT NULL,
  `director` varchar(255) DEFAULT NULL,
  `screen_writer` varchar(255) DEFAULT NULL,
  `starring` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `country` varchar(255) DEFAULT NULL,
  `language` varchar(255) DEFAULT NULL,
  `length` int(11) NOT NULL,
  `start_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `name` varchar(255) NOT NULL,
  `description` text,
  `status` int(11) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `movie`
--

LOCK TABLES `movie` WRITE;
/*!40000 ALTER TABLE `movie` DISABLE KEYS */;
INSERT INTO `movie` VALUES (10,'http://n.sinaimg.cn/translate/640/w600h840/20190312/ampL-hufnxfm4278816.jpg','大森贵弘 /伊藤秀樹','','神谷浩史 /井上和彦 /高良健吾 /小林沙苗 /泽城美雪','动画',NULL,NULL,120,'2019-04-14 14:54:31','夏目友人帐','在人与妖怪之间过着忙碌日子的夏目，偶然与以前的同学结城重逢，由此回忆起了被妖怪缠身的苦涩记忆。此时，夏目认识了在归还名字的妖怪记忆中出现的女性·津村容莉枝。和玲子相识的她，现在和独子椋雄一同过着平稳的生活。夏目通过与他们的交流，心境也变得平和。但这对母子居住的城镇，却似乎潜伏着神秘的妖怪。在调查此事归来后，寄生于猫咪老师身体的“妖之种”，在藤原家的庭院中，一夜之间就长成树结出果实。而吃掉了与自己形状相似果实的猫咪老师，竟然分裂成了3个',0),(11,'','安娜·波顿',NULL,'布利·拉尔森','动作/冒险/科幻',NULL,NULL,120,'2019-04-16 14:55:31','惊奇队长','漫画中的初代惊奇女士曾经是一名美国空军均情报局探员，暗恋惊奇先生。。。',0),(12,'','1',NULL,'1','1',NULL,NULL,120,'2019-04-16 14:57:31','1','1',0),(13,'2','2',NULL,'2','2',NULL,NULL,120,'2019-04-16 14:52:31','2','2',0),(14,'','2',NULL,'2','2',NULL,NULL,120,'2019-04-18 13:23:15','2','2',1),(15,'1','1','1','1','1','1','1',111,'2019-04-16 15:00:24','nnmm,,,','1',0),(16,'https://img3.doubanio.com/view/photo/s_ratio_poster/public/p2549523952.webp','林孝谦','abcˆ','陈意涵','爱情','大陆',NULL,123,'2019-04-18 13:23:15','比悲伤更悲伤的故事','唱片制作人张哲凯(刘以豪)和王牌作词人宋媛媛(陈意涵)相依为命，两人自幼身世坎坷只有彼此为伴，他们是亲人、是朋友，也彷佛是命中注定的另一半。父亲罹患遗传重症而被母亲抛弃的哲凯，深怕自己随时会发病不久人世，始终没有跨出友谊的界线对媛媛展露爱意。眼见哲凯的病情加重，他暗自决定用剩余的生命完成他们之间的终曲，再为媛媛找个可以托付一生的好男人。这时，事业有 成温柔体贴的医生(张书豪)适时的出现让他成为照顾媛媛的最佳人选，二人按部就班发展着关系。一切看似都在哲凯的计划下进行。然而，故事远比这里所写更要悲伤......',1);
/*!40000 ALTER TABLE `movie` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `movie_like`
--

DROP TABLE IF EXISTS `movie_like`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `movie_like` (
  `movie_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `like_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`movie_id`,`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `movie_like`
--

LOCK TABLES `movie_like` WRITE;
/*!40000 ALTER TABLE `movie_like` DISABLE KEYS */;
INSERT INTO `movie_like` VALUES (10,12,'2019-03-25 02:40:19'),(11,1,'2019-03-22 09:38:12'),(11,2,'2019-03-23 09:38:12'),(11,3,'2019-03-22 08:38:12'),(12,1,'2019-03-23 09:48:46'),(12,3,'2019-03-25 06:36:22'),(14,1,'2019-03-23 09:38:12'),(16,12,'2019-03-23 15:27:48');
/*!40000 ALTER TABLE `movie_like` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `schedule`
--

DROP TABLE IF EXISTS `schedule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `schedule` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `hall_id` int(11) NOT NULL,
  `movie_id` int(11) NOT NULL,
  `start_time` timestamp NOT NULL ON UPDATE CURRENT_TIMESTAMP,
  `end_time` timestamp NOT NULL,
  `fare` double NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=69 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `schedule`
--

LOCK TABLES `schedule` WRITE;
/*!40000 ALTER TABLE `schedule` DISABLE KEYS */;
INSERT INTO `schedule` VALUES (20,1,12,'2019-04-13 17:00:00','2019-04-13 18:00:00',20.5),(21,1,10,'2019-04-11 12:00:00','2019-04-11 13:00:00',90),(27,1,11,'2019-04-17 18:01:00','2019-04-17 20:01:00',20.5),(28,1,11,'2019-04-19 16:00:00','2019-04-19 18:00:00',20.5),(30,1,11,'2019-04-18 18:01:00','2019-04-18 20:01:00',20.5),(31,1,11,'2019-04-12 16:00:00','2019-04-12 18:00:00',20.5),(32,1,11,'2019-04-12 20:00:00','2019-04-12 22:00:00',20.5),(37,1,11,'2019-04-15 00:00:00','2019-04-15 02:00:00',20.5),(38,1,11,'2019-04-14 17:00:00','2019-04-14 19:00:00',20.5),(40,1,10,'2019-04-10 16:00:00','2019-04-10 18:00:00',20.5),(41,1,11,'2019-04-10 19:00:00','2019-04-10 21:00:00',20.5),(42,1,11,'2019-04-10 22:00:00','2019-04-11 00:00:00',20.5),(43,1,10,'2019-04-11 01:00:00','2019-04-11 03:00:00',20.5),(44,2,10,'2019-04-11 01:00:00','2019-04-11 03:00:00',20.5),(45,2,10,'2019-04-10 22:00:00','2019-04-11 00:00:00',20.5),(46,2,11,'2019-04-10 19:00:00','2019-04-10 21:00:00',20.5),(47,2,11,'2019-04-10 16:00:00','2019-04-10 18:00:00',20.5),(48,2,10,'2019-04-11 13:00:00','2019-04-11 15:59:00',20.5),(50,1,10,'2019-04-15 16:00:00','2019-04-15 19:00:00',2),(51,1,10,'2019-04-17 05:00:00','2019-04-17 07:00:00',9),(52,1,10,'2019-04-18 05:00:00','2019-04-18 07:00:00',9),(53,1,16,'2019-04-19 07:00:00','2019-04-19 10:00:00',9),(54,1,16,'2019-04-16 19:00:00','2019-04-16 22:00:00',9),(55,1,15,'2019-04-17 23:00:00','2019-04-18 01:00:00',9),(56,2,10,'2019-04-19 13:00:00','2019-04-19 15:59:00',20.5),(57,2,10,'2019-04-20 13:00:00','2019-04-20 15:59:00',20.5),(58,2,10,'2019-04-21 13:00:00','2019-04-21 15:59:00',20.5),(61,1,13,'2019-04-20 11:00:00','2019-04-20 13:00:00',25),(62,1,11,'2019-04-20 08:00:00','2019-04-20 10:00:00',25),(63,2,15,'2019-04-20 16:01:30','2019-04-21 05:30:00',30),(64,1,16,'2019-04-22 02:00:00','2019-04-22 05:30:00',30),(65,1,10,'2019-04-23 02:00:00','2019-04-23 05:30:00',30),(66,2,13,'2019-04-21 07:31:29','2019-04-16 15:59:00',20.5),(67,2,10,'2019-04-25 13:00:00','2019-04-25 15:59:00',20.5),(68,2,10,'2019-04-26 13:00:00','2019-04-26 15:59:00',20.5);
/*!40000 ALTER TABLE `schedule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ticket`
--

DROP TABLE IF EXISTS `ticket`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ticket` (
  `user_id` int(11) DEFAULT NULL,
  `schedule_id` int(11) DEFAULT NULL,
  `column_index` int(11) DEFAULT NULL,
  `row_index` int(11) DEFAULT NULL,
  `state` tinyint(4) DEFAULT NULL,
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=63 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ticket`
--

LOCK TABLES `ticket` WRITE;
/*!40000 ALTER TABLE `ticket` DISABLE KEYS */;
INSERT INTO `ticket` VALUES (12,50,5,3,2,1,'2019-04-23 13:50:52'),(12,50,5,3,2,2,'2019-04-23 13:50:52'),(12,50,5,3,2,3,'2019-04-23 13:50:52'),(12,50,5,3,2,4,'2019-04-23 13:50:52'),(12,50,5,3,0,5,'2019-04-23 13:50:52'),(15,50,4,3,0,6,'2019-04-23 13:50:52'),(15,58,0,0,1,15,'2019-04-23 13:50:52'),(15,58,2,0,1,16,'2019-04-23 13:50:52'),(15,58,1,1,1,17,'2019-04-23 13:50:52'),(15,58,11,7,1,18,'2019-04-23 13:50:52'),(13,50,4,2,1,19,'2019-04-23 13:50:52'),(15,66,3,2,1,20,'2019-04-23 13:50:52'),(12,50,1,1,1,21,'2019-04-23 13:50:52'),(13,50,4,3,1,22,'2019-04-23 13:50:52'),(15,50,2,2,1,23,'2019-04-23 13:50:52'),(15,58,0,7,0,24,'2019-04-23 13:50:52'),(15,58,5,4,0,25,'2019-04-23 13:50:52'),(15,58,6,4,0,26,'2019-04-23 13:50:52'),(15,58,6,2,0,27,'2019-04-23 13:50:52'),(15,58,7,2,0,28,'2019-04-23 13:50:52'),(15,58,0,4,0,29,'2019-04-23 13:50:52'),(15,58,0,3,0,30,'2019-04-23 13:50:52'),(15,58,0,2,0,31,'2019-04-23 13:50:52'),(15,58,10,0,0,32,'2019-04-23 13:50:52'),(15,58,11,0,0,33,'2019-04-23 13:50:52'),(15,58,8,0,0,34,'2019-04-23 13:50:52'),(15,58,9,0,0,35,'2019-04-23 13:50:52'),(15,58,5,0,0,36,'2019-04-23 13:50:52'),(15,58,6,0,0,37,'2019-04-23 13:50:52'),(15,58,6,7,0,38,'2019-04-23 13:50:52'),(15,58,7,7,0,39,'2019-04-23 13:50:52'),(15,58,8,7,0,40,'2019-04-23 13:50:52'),(15,58,11,4,0,41,'2019-04-23 13:50:52'),(15,58,11,5,0,42,'2019-04-23 13:50:52'),(15,58,9,6,0,43,'2019-04-23 13:50:52'),(15,58,10,6,0,44,'2019-04-23 13:50:52'),(15,58,11,6,0,45,'2019-04-23 13:50:52'),(15,58,3,5,1,46,'2019-04-23 13:50:52'),(15,58,4,5,1,47,'2019-04-23 13:50:52'),(15,58,5,5,1,48,'2019-04-23 13:50:52'),(15,58,11,2,0,49,'2019-04-23 13:50:52'),(15,58,11,3,0,50,'2019-04-23 13:50:52'),(15,58,9,4,0,51,'2019-04-23 13:50:52'),(15,58,9,3,1,52,'2019-04-23 13:50:52'),(15,58,10,3,1,53,'2019-04-23 13:50:52'),(15,65,7,4,0,54,'2019-04-23 13:50:52'),(15,65,8,4,0,55,'2019-04-23 13:50:52'),(15,65,9,4,0,56,'2019-04-23 13:50:52'),(15,65,7,3,0,57,'2019-04-23 13:50:52'),(15,65,8,3,0,58,'2019-04-23 13:50:52'),(15,65,9,3,0,59,'2019-04-23 13:50:52'),(15,65,0,0,1,60,'2019-04-23 13:50:52'),(15,65,0,1,1,61,'2019-04-23 13:50:52'),(15,65,0,2,1,62,'2019-04-23 13:50:52');
/*!40000 ALTER TABLE `ticket` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id_uindex` (`id`),
  UNIQUE KEY `user_username_uindex` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'testname','123456'),(3,'test','123456'),(5,'test1','123456'),(7,'test121','123456'),(8,'root','123456'),(10,'roottt','123123'),(12,'zhourui','123456'),(13,'abc123','abc123'),(15,'dd','123');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `view`
--

DROP TABLE IF EXISTS `view`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `view` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `day` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `view`
--

LOCK TABLES `view` WRITE;
/*!40000 ALTER TABLE `view` DISABLE KEYS */;
INSERT INTO `view` VALUES (1,7);
/*!40000 ALTER TABLE `view` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vip_card`
--

DROP TABLE IF EXISTS `vip_card`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `vip_card` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `balance` float DEFAULT NULL,
  `join_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `vip_card_user_id_uindex` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vip_card`
--

LOCK TABLES `vip_card` WRITE;
/*!40000 ALTER TABLE `vip_card` DISABLE KEYS */;
INSERT INTO `vip_card` VALUES (1,15,375,'2019-04-21 13:54:38'),(2,12,660,'2019-04-17 18:47:42');
/*!40000 ALTER TABLE `vip_card` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'cinema'
--

--
-- Dumping routines for database 'cinema'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-04-24 21:20:52