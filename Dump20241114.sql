-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: localhost    Database: ltm
-- ------------------------------------------------------
-- Server version	8.4.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `click`
--

DROP TABLE IF EXISTS `click`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `click` (
  `id` int NOT NULL AUTO_INCREMENT,
  `x` int NOT NULL,
  `y` int NOT NULL,
  `userId` int NOT NULL,
  `gameId` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `gameId_idx` (`gameId`),
  KEY `fk_userId` (`userId`),
  CONSTRAINT `fk_gameId` FOREIGN KEY (`gameId`) REFERENCES `game` (`id`),
  CONSTRAINT `fk_userId` FOREIGN KEY (`userId`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=535 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `click`
--

LOCK TABLES `click` WRITE;
/*!40000 ALTER TABLE `click` DISABLE KEYS */;
INSERT INTO `click` VALUES (438,11,53,48,316),(439,241,103,48,316),(440,32,169,48,316),(441,99,27,48,316),(442,101,28,48,317),(443,13,54,48,317),(444,33,166,48,317),(445,243,106,48,317),(446,209,280,47,317),(447,303,53,52,318),(448,128,165,52,318),(449,133,241,52,318),(450,111,242,52,318),(451,147,31,52,318),(452,311,52,51,319),(453,311,56,52,329),(454,131,162,51,329),(455,106,30,51,331),(456,32,166,51,331),(457,14,54,52,331),(458,240,104,52,331),(459,303,54,51,332),(460,131,162,51,332),(461,149,34,51,332),(462,132,242,52,332),(463,111,242,52,332),(464,315,55,51,333),(465,132,161,51,333),(466,151,34,52,333),(467,111,244,52,333),(468,133,240,51,333),(469,99,26,51,334),(470,36,165,51,334),(471,245,104,51,334),(472,13,55,52,334),(473,211,279,52,334),(474,106,28,51,335),(475,14,55,51,335),(476,241,108,51,335),(477,101,28,52,336),(478,13,54,52,336),(479,244,102,52,336),(480,36,168,52,336),(481,203,280,51,336),(482,309,52,47,337),(483,130,162,47,337),(484,110,241,47,337),(485,240,67,51,338),(486,34,114,47,338),(487,239,68,51,339),(488,64,265,52,339),(489,239,69,47,340),(490,62,262,48,340),(491,304,50,51,341),(492,149,33,48,341),(493,239,69,52,342),(494,153,230,51,342),(495,69,262,51,342),(496,35,114,52,342),(497,33,170,52,343),(498,98,26,52,343),(499,10,55,52,343),(500,210,273,52,343),(501,310,57,51,344),(502,129,160,51,344),(503,132,242,52,344),(504,111,241,52,344),(505,149,34,52,345),(506,305,49,51,345),(507,127,162,51,345),(508,133,241,51,345),(509,111,242,51,345),(510,239,68,52,346),(511,151,230,51,346),(512,34,115,51,346),(513,239,181,52,346),(514,311,49,51,347),(515,237,67,48,348),(516,151,233,52,357),(517,151,232,51,357),(518,63,265,51,357),(519,237,68,51,357),(520,104,29,52,358),(521,107,29,52,359),(522,148,33,51,361),(523,237,68,51,362),(524,151,231,51,362),(525,105,27,51,363),(526,319,57,48,364),(527,131,162,51,364),(528,132,244,51,364),(529,111,244,51,364),(530,35,165,51,366),(531,244,106,47,366),(532,103,30,47,366),(533,199,280,47,366),(534,12,53,51,366);
/*!40000 ALTER TABLE `click` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `game`
--

DROP TABLE IF EXISTS `game`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `game` (
  `id` int NOT NULL AUTO_INCREMENT,
  `userId1` int NOT NULL,
  `userId2` int NOT NULL,
  `pairId` int NOT NULL,
  `score1` int DEFAULT '0',
  `score2` int DEFAULT '0',
  `state` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `userId1_idx` (`userId1`),
  KEY `userId2_idx` (`userId2`),
  KEY `pairId_idx` (`pairId`),
  CONSTRAINT `fk_pairId` FOREIGN KEY (`pairId`) REFERENCES `pair` (`id`),
  CONSTRAINT `fk_userId1` FOREIGN KEY (`userId1`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_userId2` FOREIGN KEY (`userId2`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=367 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `game`
--

LOCK TABLES `game` WRITE;
/*!40000 ALTER TABLE `game` DISABLE KEYS */;
INSERT INTO `game` VALUES (316,47,48,1,0,0,NULL),(317,47,48,1,0,0,0),(318,51,52,3,0,0,0),(319,51,52,3,0,0,NULL),(320,51,52,1,0,0,NULL),(321,47,48,3,0,0,0),(322,52,51,1,0,0,NULL),(323,52,51,3,0,0,NULL),(324,51,52,3,0,0,NULL),(325,51,52,2,0,0,NULL),(326,48,51,3,0,0,NULL),(327,52,51,3,0,0,NULL),(328,52,51,2,0,0,NULL),(329,52,51,3,1,0,1),(330,51,52,2,0,0,NULL),(331,51,52,1,2,0,1),(332,52,51,3,0,0,0),(333,51,52,3,2,0,1),(334,51,52,1,0,0,0),(335,51,52,1,0,0,0),(336,52,51,1,0,0,0),(337,47,48,3,0,0,NULL),(338,51,47,2,1,0,1),(339,52,51,2,0,1,-1),(340,48,47,2,0,1,-1),(341,51,48,3,1,0,1),(342,51,52,2,0,1,-1),(343,51,52,1,0,0,0),(344,51,52,3,2,2,0),(345,51,52,3,4,1,1),(346,51,52,2,2,2,0),(347,51,52,3,0,0,NULL),(348,51,48,2,0,0,NULL),(349,51,52,1,0,0,NULL),(350,51,52,1,0,0,NULL),(351,52,51,3,0,0,NULL),(352,51,52,1,0,0,NULL),(353,51,52,3,0,0,NULL),(354,51,52,3,0,0,NULL),(355,52,51,2,0,0,NULL),(356,51,52,1,0,0,NULL),(357,51,52,2,3,1,1),(358,52,51,1,0,0,NULL),(359,51,52,1,0,0,NULL),(360,51,52,1,0,0,NULL),(361,51,52,3,0,0,NULL),(362,51,48,2,0,0,NULL),(363,51,48,1,1,0,1),(364,51,48,3,3,1,1),(365,51,48,2,0,0,NULL),(366,51,47,1,2,3,-1);
/*!40000 ALTER TABLE `game` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pair`
--

DROP TABLE IF EXISTS `pair`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pair` (
  `id` int NOT NULL AUTO_INCREMENT,
  `image1` varchar(45) NOT NULL,
  `image2` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pair`
--

LOCK TABLES `pair` WRITE;
/*!40000 ALTER TABLE `pair` DISABLE KEYS */;
INSERT INTO `pair` VALUES (1,'jkhk.png','nlhlhlhl.png'),(2,'l\'lk\'lkl\'.png','ljkkjk.png'),(3,'lk\'lk\'.png','lkl.png');
/*!40000 ALTER TABLE `pair` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `point`
--

DROP TABLE IF EXISTS `point`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `point` (
  `id` int NOT NULL AUTO_INCREMENT,
  `minX` int NOT NULL,
  `minY` int NOT NULL,
  `maxX` int NOT NULL,
  `maxY` int NOT NULL,
  `pairId` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `pairId_idx` (`pairId`),
  CONSTRAINT `pairId` FOREIGN KEY (`pairId`) REFERENCES `pair` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `point`
--

LOCK TABLES `point` WRITE;
/*!40000 ALTER TABLE `point` DISABLE KEYS */;
INSERT INTO `point` VALUES (1,10,50,15,60,1),(2,32,165,36,170,1),(3,98,25,107,30,1),(4,195,190,213,280,1),(5,240,100,247,110,1),(6,33,110,36,115,2),(7,58,260,70,265,2),(8,151,230,153,233,2),(9,237,67,241,69,2),(10,225,175,240,185,2),(11,144,30,151,40,3),(12,124,160,135,166,3),(13,110,240,112,245,3),(14,132,240,133,245,3),(15,297,40,319,65,3);
/*!40000 ALTER TABLE `point` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  `win` int DEFAULT '0',
  `tie` int DEFAULT '0',
  `lose` int DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `username_UNIQUE` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=54 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (47,'PINK','202cb962ac59075b964b07152d234b70',13,0,6),(48,'ORAN','202cb962ac59075b964b07152d234b70',5,0,15),(51,'LAM','202cb962ac59075b964b07152d234b70',10,3,5),(52,'NAM','202cb962ac59075b964b07152d234b70',4,3,6),(53,'HUYEN','202cb962ac59075b964b07152d234b70',0,0,0);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-11-14 14:01:08
