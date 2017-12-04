-- MySQL dump 10.13  Distrib 5.7.20, for Win64 (x86_64)
--
-- Host: localhost    Database: loanapound
-- ------------------------------------------------------
-- Server version	5.7.20-log

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
-- Table structure for table `applicants`
--

DROP TABLE IF EXISTS `applicants`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `applicants` (
  `user_id` smallint(5) unsigned NOT NULL,
  `email_address` varbinary(40) DEFAULT NULL,
  `last_cs_update` datetime DEFAULT NULL,
  UNIQUE KEY `unique_applicants` (`user_id`),
  CONSTRAINT `applicants_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `applicants`
--

LOCK TABLES `applicants` WRITE;
/*!40000 ALTER TABLE `applicants` DISABLE KEYS */;
INSERT INTO `applicants` VALUES (2,'²”%ýD‘dö\Åa\0ñ\ãƒ.„&\Õ[ð¢p\Äô\ÇN',NULL),(3,'²”%ýD‘dö\Åa\0ñ\ãƒ.„&\Õ[ð¢p\Äô\ÇN',NULL);
/*!40000 ALTER TABLE `applicants` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mortgage_applications`
--

DROP TABLE IF EXISTS `mortgage_applications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mortgage_applications` (
  `application_id` smallint(5) unsigned NOT NULL AUTO_INCREMENT,
  `applicant_id` smallint(5) unsigned NOT NULL,
  `mortgage_id` smallint(5) unsigned NOT NULL,
  `loan_value` double DEFAULT NULL,
  `deposit_amount` double DEFAULT NULL,
  `first_time_buyer` tinyint(1) DEFAULT NULL,
  `credit_score_used` int(11) DEFAULT NULL,
  `application_status` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`application_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mortgage_applications`
--

LOCK TABLES `mortgage_applications` WRITE;
/*!40000 ALTER TABLE `mortgage_applications` DISABLE KEYS */;
/*!40000 ALTER TABLE `mortgage_applications` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mortgages`
--

DROP TABLE IF EXISTS `mortgages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mortgages` (
  `mortgage_id` smallint(5) unsigned NOT NULL AUTO_INCREMENT,
  `lender` varchar(20) NOT NULL,
  `repayment_months` smallint(6) NOT NULL,
  `minimum_value` double NOT NULL,
  `maximum_value` double NOT NULL,
  `interest_rate` double NOT NULL,
  `min_deposit_is_percent` tinyint(1) NOT NULL,
  `minimum_deposit` double NOT NULL,
  `minimum_credit_score` smallint(6) DEFAULT NULL,
  `fees` double NOT NULL,
  `rule` varchar(20) NOT NULL,
  `first_time_buyer_only` tinyint(1) NOT NULL,
  PRIMARY KEY (`mortgage_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1004 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mortgages`
--

LOCK TABLES `mortgages` WRITE;
/*!40000 ALTER TABLE `mortgages` DISABLE KEYS */;
INSERT INTO `mortgages` VALUES (1000,'HSBC',360,80000,200000,5.4,1,15,500,1200,'lowest',0),(1001,'Nationwide',360,80000,220000,5.8,0,50000,560,8000,'highest',0),(1002,'Barclays',300,100000,300000,5.1,0,50000,630,8000,'average',0),(1003,'NatWest',240,80000,180000,6,0,50000,560,8000,'creditsite',0);
/*!40000 ALTER TABLE `mortgages` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `personal_loan_applications`
--

DROP TABLE IF EXISTS `personal_loan_applications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `personal_loan_applications` (
  `application_id` smallint(5) unsigned NOT NULL AUTO_INCREMENT,
  `applicant_id` smallint(5) unsigned NOT NULL,
  `loan_id` smallint(5) unsigned NOT NULL,
  `loan_value` double DEFAULT NULL,
  `credit_score_used` int(11) DEFAULT NULL,
  `application_status` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`application_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `personal_loan_applications`
--

LOCK TABLES `personal_loan_applications` WRITE;
/*!40000 ALTER TABLE `personal_loan_applications` DISABLE KEYS */;
/*!40000 ALTER TABLE `personal_loan_applications` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `personal_loans`
--

DROP TABLE IF EXISTS `personal_loans`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `personal_loans` (
  `loan_id` smallint(5) unsigned NOT NULL AUTO_INCREMENT,
  `lender` varchar(20) NOT NULL,
  `repayment_months` smallint(6) NOT NULL,
  `minimum_value` double NOT NULL,
  `maximum_value` double NOT NULL,
  `interest_rate` double NOT NULL,
  `rule` varchar(20) NOT NULL,
  `minimum_credit_score` smallint(6) DEFAULT NULL,
  PRIMARY KEY (`loan_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2004 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `personal_loans`
--

LOCK TABLES `personal_loans` WRITE;
/*!40000 ALTER TABLE `personal_loans` DISABLE KEYS */;
INSERT INTO `personal_loans` VALUES (2000,'HSBC',24,4000,10000,8,'lowest',588),(2001,'Nationwide',24,5000,12000,7.8,'highest',588),(2002,'Barclays',12,3000,8000,7.5,'average',610),(2003,'NatWest',36,6000,18000,7.8,'creditsite2',560);
/*!40000 ALTER TABLE `personal_loans` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `user_id` smallint(5) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL,
  `password` varbinary(40) NOT NULL,
  `user_type` varchar(1) NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `user_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'admin','„¿\æÉ¢-f5>ª¦\Å-','A'),(2,'applicant','„¿\æÉ¢-f5>ª¦\Å-','B'),(3,'applicant_cs','„¿\æÉ¢-f5>ª¦\Å-','B'),(4,'underwriter','„¿\æÉ¢-f5>ª¦\Å-','C');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary table structure for view `v_administrators`
--

DROP TABLE IF EXISTS `v_administrators`;
/*!50001 DROP VIEW IF EXISTS `v_administrators`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `v_administrators` AS SELECT 
 1 AS `user_id`,
 1 AS `name`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary table structure for view `v_applicants`
--

DROP TABLE IF EXISTS `v_applicants`;
/*!50001 DROP VIEW IF EXISTS `v_applicants`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `v_applicants` AS SELECT 
 1 AS `user_id`,
 1 AS `name`,
 1 AS `email_address`,
 1 AS `last_cs_update`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary table structure for view `v_mortgages`
--

DROP TABLE IF EXISTS `v_mortgages`;
/*!50001 DROP VIEW IF EXISTS `v_mortgages`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `v_mortgages` AS SELECT 
 1 AS `mortgage_id`,
 1 AS `lender`,
 1 AS `repayment_months`,
 1 AS `minimum_value`,
 1 AS `maximum_value`,
 1 AS `interest_rate`,
 1 AS `min_deposit_is_percent`,
 1 AS `minimum_deposit`,
 1 AS `minimum_credit_score`,
 1 AS `fees`,
 1 AS `rule`,
 1 AS `first_time_buyer_only`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary table structure for view `v_personal_loans`
--

DROP TABLE IF EXISTS `v_personal_loans`;
/*!50001 DROP VIEW IF EXISTS `v_personal_loans`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `v_personal_loans` AS SELECT 
 1 AS `loan_id`,
 1 AS `lender`,
 1 AS `repayment_months`,
 1 AS `minimum_value`,
 1 AS `maximum_value`,
 1 AS `interest_rate`,
 1 AS `rule`,
 1 AS `minimum_credit_score`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary table structure for view `v_underwriters`
--

DROP TABLE IF EXISTS `v_underwriters`;
/*!50001 DROP VIEW IF EXISTS `v_underwriters`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `v_underwriters` AS SELECT 
 1 AS `user_id`,
 1 AS `name`*/;
SET character_set_client = @saved_cs_client;

--
-- Final view structure for view `v_administrators`
--

/*!50001 DROP VIEW IF EXISTS `v_administrators`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`lucasurry`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `v_administrators` AS (select `u`.`user_id` AS `user_id`,`u`.`name` AS `name` from `users` `u` where (`u`.`user_type` = 'A')) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `v_applicants`
--

/*!50001 DROP VIEW IF EXISTS `v_applicants`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`lucasurry`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `v_applicants` AS (select `u`.`user_id` AS `user_id`,`u`.`name` AS `name`,`a`.`email_address` AS `email_address`,`a`.`last_cs_update` AS `last_cs_update` from (`users` `u` join `applicants` `a` on((`u`.`user_id` = `a`.`user_id`))) where (`u`.`user_type` = 'B')) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `v_mortgages`
--

/*!50001 DROP VIEW IF EXISTS `v_mortgages`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`lucasurry`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `v_mortgages` AS (select `mort`.`mortgage_id` AS `mortgage_id`,`mort`.`lender` AS `lender`,`mort`.`repayment_months` AS `repayment_months`,`mort`.`minimum_value` AS `minimum_value`,`mort`.`maximum_value` AS `maximum_value`,`mort`.`interest_rate` AS `interest_rate`,`mort`.`min_deposit_is_percent` AS `min_deposit_is_percent`,`mort`.`minimum_deposit` AS `minimum_deposit`,`mort`.`minimum_credit_score` AS `minimum_credit_score`,`mort`.`fees` AS `fees`,`mort`.`rule` AS `rule`,`mort`.`first_time_buyer_only` AS `first_time_buyer_only` from `mortgages` `mort`) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `v_personal_loans`
--

/*!50001 DROP VIEW IF EXISTS `v_personal_loans`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`lucasurry`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `v_personal_loans` AS (select `loan`.`loan_id` AS `loan_id`,`loan`.`lender` AS `lender`,`loan`.`repayment_months` AS `repayment_months`,`loan`.`minimum_value` AS `minimum_value`,`loan`.`maximum_value` AS `maximum_value`,`loan`.`interest_rate` AS `interest_rate`,`loan`.`rule` AS `rule`,`loan`.`minimum_credit_score` AS `minimum_credit_score` from `personal_loans` `loan`) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `v_underwriters`
--

/*!50001 DROP VIEW IF EXISTS `v_underwriters`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`lucasurry`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `v_underwriters` AS (select `u`.`user_id` AS `user_id`,`u`.`name` AS `name` from `users` `u` where (`u`.`user_type` = 'C')) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-12-04  1:55:51
