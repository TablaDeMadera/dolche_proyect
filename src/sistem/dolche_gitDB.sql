-- MariaDB dump 10.17  Distrib 10.4.8-MariaDB, for Win64 (AMD64)
--
-- Host: localhost    Database: dolche
-- ------------------------------------------------------
-- Server version	10.4.8-MariaDB

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
-- Table structure for table `alarm`
--

DROP TABLE IF EXISTS `alarm`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `alarm` (
  `idalarm` int(11) NOT NULL AUTO_INCREMENT,
  `date` date NOT NULL,
  `parameter` varchar(45) NOT NULL,
  `low_limit` float DEFAULT NULL,
  `high_limit` float DEFAULT NULL,
  `description` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`idalarm`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `alarm`
--

LOCK TABLES `alarm` WRITE;
/*!40000 ALTER TABLE `alarm` DISABLE KEYS */;
INSERT INTO `alarm` VALUES (1,'2020-11-03','brix',0.1,1.9,'dulzura de mermelada');
/*!40000 ALTER TABLE `alarm` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `kpi`
--

DROP TABLE IF EXISTS `kpi`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `kpi` (
  `idKPI` int(11) NOT NULL AUTO_INCREMENT,
  `date_kpi` date NOT NULL,
  `min_brix` float NOT NULL,
  `min_ph` float NOT NULL,
  `min_consistencia` float NOT NULL,
  `min_viscocidad` float NOT NULL,
  `min_acidez` float NOT NULL,
  `max_brix` float NOT NULL,
  `max_ph` float NOT NULL,
  `max_consistencia` float NOT NULL,
  `max_viscocidad` float NOT NULL,
  `max_acidez` float NOT NULL,
  `prom_brix` float NOT NULL,
  `prom_ph` float NOT NULL,
  `prom_consistencia` float NOT NULL,
  `prom_viscocidad` float NOT NULL,
  `prom_acidez` float NOT NULL,
  `desv_brix` float NOT NULL,
  `desv_ph` float NOT NULL,
  `desv_consistencia` float NOT NULL,
  `desv_viscocidad` float NOT NULL,
  `desv_acidez` float NOT NULL,
  PRIMARY KEY (`idKPI`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `kpi`
--

LOCK TABLES `kpi` WRITE;
/*!40000 ALTER TABLE `kpi` DISABLE KEYS */;
INSERT INTO `kpi` VALUES (1,'2020-09-13',1.1,1.2,1.3,1.4,1.5,2.1,2.2,2.3,2.4,2.5,3.1,3.2,3.3,3.4,3.5,4.1,4.2,4.3,4.4,4.5);
/*!40000 ALTER TABLE `kpi` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `register`
--

DROP TABLE IF EXISTS `register`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `register` (
  `idregister` int(11) NOT NULL AUTO_INCREMENT,
  `fecha_analisis` date NOT NULL,
  `clave` varchar(45) NOT NULL,
  `fecha_produccion` int(11) NOT NULL,
  `no_cocinada` int(11) DEFAULT NULL,
  `espec` varchar(45) DEFAULT NULL,
  `brix` float DEFAULT NULL,
  `ph` float DEFAULT NULL,
  `consistencia` float DEFAULT NULL,
  `apariencia` varchar(45) DEFAULT NULL,
  `viscosidad` float DEFAULT NULL,
  `acidez` float DEFAULT NULL,
  `observaciones` varchar(45) DEFAULT NULL,
  `estatus_fq` varchar(45) DEFAULT NULL,
  `estatus_funcional` varchar(45) DEFAULT NULL,
  `coliformes` varchar(45) DEFAULT NULL,
  `cuenta_estandar` varchar(45) DEFAULT NULL,
  `hongos` varchar(45) DEFAULT NULL,
  `levaduras` varchar(45) DEFAULT NULL,
  `estatus_micro` varchar(45) DEFAULT NULL,
  `estatus_final` varchar(45) DEFAULT NULL,
  `USER_iduser` int(11) NOT NULL,
  PRIMARY KEY (`idregister`,`USER_iduser`),
  KEY `USER_iduser` (`USER_iduser`),
  CONSTRAINT `REGISTER_ibfk_1` FOREIGN KEY (`USER_iduser`) REFERENCES `user` (`iduser`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `register`
--

LOCK TABLES `register` WRITE;
/*!40000 ALTER TABLE `register` DISABLE KEYS */;
INSERT INTO `register` VALUES (1,'2020-01-01','HGT',20190102,2,'-',1.2,2.3,3.4,'BUENA',897653,3.2,'para mermelada','BUENO','CUMPLE','<10','<10','<10','<10','BUENO','BUENO',1),(2,'2020-05-12','HGJS',20200408,89,'-',542,2.1,7.2,'GELATINOSA',7893.2,5.2,'AGRAGAR AZUCAR','REVISAR','FUNCIONA','<10','<5','<23','<32','ACEPTABLE','REPROCESAR',1),(3,'2020-05-12','KIY',20200907,99,'A HORNO',7.1,1.2,4.1,'CLARA',9832.1,1,'MAS FERMENTAR','BUENO','FUNCIONA','<10','<4','<25','<2','ACEPTABLE','ACEPTADO',1),(4,'2020-05-12','HAS',20190911,32,'-',2.3,21.1,2.3,'HORRIBLE',7865.2,43.1,'NINGUNA','BUENO','FUNCIONAL','<45','<12','<78','<10','BUENO','APROBADO',4);
/*!40000 ALTER TABLE `register` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sesion`
--

DROP TABLE IF EXISTS `sesion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sesion` (
  `idsesion` int(11) NOT NULL AUTO_INCREMENT,
  `date_init` datetime NOT NULL,
  `date_finish` datetime NOT NULL,
  `USER_iduser` int(11) NOT NULL,
  PRIMARY KEY (`idsesion`,`USER_iduser`),
  KEY `USER_iduser` (`USER_iduser`),
  CONSTRAINT `SESION_ibfk_1` FOREIGN KEY (`USER_iduser`) REFERENCES `user` (`iduser`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sesion`
--

LOCK TABLES `sesion` WRITE;
/*!40000 ALTER TABLE `sesion` DISABLE KEYS */;
INSERT INTO `sesion` VALUES (1,'2020-03-18 00:00:00','2020-03-19 00:00:00',1);
/*!40000 ALTER TABLE `sesion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `iduser` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  `privilege` int(11) NOT NULL,
  PRIMARY KEY (`iduser`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'Mario','123',1),(2,'Jose','123',3),(3,'Lupe','123',2),(4,'God','123',7),(5,'Luis','123',2),(6,'AMiguel','123',7),(7,'ARafael','123',7),(8,'AGabriel','123',7),(9,'AChamuel','123',7),(10,'AUriel','123',7),(11,'AZadquiel','123',7),(12,'AJofiel','123',7),(13,'Berta','123',1);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'dolche'
--

--
-- Dumping routines for database 'dolche'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-05-13 13:36:04
