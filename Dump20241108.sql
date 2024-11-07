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
) ENGINE=InnoDB AUTO_INCREMENT=316 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `click`
--

LOCK TABLES `click` WRITE;
/*!40000 ALTER TABLE `click` DISABLE KEYS */;
INSERT INTO `click` VALUES (251,239,68,15,242),(252,152,231,15,242),(253,240,185,15,242),(254,36,115,15,242),(255,63,264,15,242),(256,305,49,1,243),(257,133,241,1,243),(258,111,244,1,243),(259,131,164,1,243),(260,144,33,15,243),(261,311,58,1,244),(262,127,165,1,244),(263,132,242,1,244),(264,111,241,1,244),(265,144,35,15,244),(266,304,54,15,247),(267,134,162,15,247),(268,111,243,15,247),(269,150,36,15,247),(270,133,241,15,247),(271,240,69,1,248),(272,36,114,1,248),(273,238,184,1,248),(274,151,232,1,248),(275,61,263,1,248),(276,238,68,1,250),(277,151,233,1,250),(278,62,265,1,250),(279,36,113,1,250),(280,237,183,1,250),(281,238,67,1,251),(282,151,231,1,251),(283,63,262,1,251),(284,36,115,1,251),(285,235,185,1,251),(286,300,53,1,253),(287,131,163,1,253),(288,132,241,1,253),(289,112,242,1,253),(290,150,36,1,253),(291,310,53,1,254),(292,130,163,1,254),(293,111,243,1,254),(294,133,240,1,254),(295,147,36,1,254),(296,240,67,1,255),(297,151,232,1,255),(298,35,115,1,255),(299,58,264,1,255),(300,234,185,1,255),(301,239,67,1,256),(302,153,233,1,256),(303,62,265,1,256),(304,36,115,1,256),(305,236,185,1,256),(306,239,69,15,257),(307,152,233,15,257),(308,59,265,15,257),(309,36,115,15,257),(310,234,178,15,257),(311,304,48,1,259),(312,132,242,1,259),(313,112,243,1,259),(314,128,161,1,259),(315,148,32,1,259);
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
) ENGINE=InnoDB AUTO_INCREMENT=260 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `game`
--

LOCK TABLES `game` WRITE;
/*!40000 ALTER TABLE `game` DISABLE KEYS */;
INSERT INTO `game` VALUES (241,1,15,1,0,0,1),(242,1,15,2,0,0,0),(243,1,15,3,0,0,0),(244,1,15,3,0,4,0),(245,1,15,1,0,0,NULL),(246,15,1,1,0,0,NULL),(247,15,1,3,0,5,0),(248,1,15,2,0,5,0),(249,1,15,1,0,0,NULL),(250,15,1,2,0,5,-1),(251,1,15,2,5,0,1),(252,1,15,1,0,0,NULL),(253,15,1,3,0,5,-1),(254,1,15,3,5,0,1),(255,1,15,2,0,0,0),(256,1,15,2,5,0,1),(257,1,15,2,0,5,-1),(258,15,1,1,0,0,NULL),(259,1,15,3,5,0,1);
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
INSERT INTO `point` VALUES (1,10,50,15,60,1),(2,32,165,36,170,1),(3,98,25,107,30,1),(4,195,280,213,190,1),(5,240,100,247,110,1),(6,33,110,36,115,2),(7,58,260,70,265,2),(8,151,230,153,233,2),(9,237,67,241,69,2),(10,225,175,240,185,2),(11,144,30,151,40,3),(12,124,160,135,166,3),(13,110,240,112,245,3),(14,132,240,133,245,3),(15,297,40,319,65,3);
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
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'lam','123',2,2,1),(15,'nam','123',1,2,2),(16,'CrazyCatLover','123',65,9,53),(17,'DancingDino','123',33,18,92),(18,'NinjaTurtlePower','123',86,48,17),(19,'SillyPanda','123',99,52,81),(20,'WackyWombat','123',17,28,68),(21,'FunkyMonkey','123',16,23,25),(22,'SneakySquirrel','123',99,71,32),(23,'BouncingBunny','123',39,32,100),(24,'JollyGiraffe','123',20,12,41),(25,'CheekyChipmunk','123',66,1,36);
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

-- Dump completed on 2024-11-08  1:23:34
