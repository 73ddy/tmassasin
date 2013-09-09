-- phpMyAdmin SQL Dump
-- version 3.5.1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Sep 09, 2013 at 02:50 AM
-- Server version: 5.5.24-log
-- PHP Version: 5.4.3

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `db_tmassasin_jsf`
--

-- --------------------------------------------------------

--
-- Table structure for table `employee`
--

CREATE TABLE IF NOT EXISTS `employee` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(30) NOT NULL,
  `password` varchar(30) NOT NULL,
  `email` varchar(50) NOT NULL,
  `last_modified` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=4 ;

--
-- Dumping data for table `employee`
--

INSERT INTO `employee` (`id`, `username`, `password`, `email`, `last_modified`, `version`) VALUES
(2, 'gauravg', 'iamhacked', 'er.gaurav2k11@gmail.com', '2013-09-08 19:24:44', 2),
(3, 'chalkoini', 'testpass', 'test', '2013-09-08 20:27:58', 0);

-- --------------------------------------------------------

--
-- Table structure for table `time_log`
--

CREATE TABLE IF NOT EXISTS `time_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `entry_time` time DEFAULT NULL,
  `exit_time` time DEFAULT NULL,
  `day_of_log` date NOT NULL,
  `version` int(11) DEFAULT NULL,
  `employee` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_timelog_employ` (`employee`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=4 ;

-- --------------------------------------------------------

--
-- Table structure for table `work_log`
--

CREATE TABLE IF NOT EXISTS `work_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `day_division` int(11) NOT NULL,
  `comment` varchar(1000) NOT NULL,
  `files` varchar(1000) DEFAULT NULL,
  `last_modified` datetime NOT NULL,
  `request` varchar(30) NOT NULL,
  `day_of_log` datetime NOT NULL,
  `version` int(11) DEFAULT NULL,
  `employee` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_worklog_employee` (`employee`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `time_log`
--
ALTER TABLE `time_log`
  ADD CONSTRAINT `time_log_ibfk_1` FOREIGN KEY (`employee`) REFERENCES `employee` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `work_log`
--
ALTER TABLE `work_log`
  ADD CONSTRAINT `work_log_ibfk_1` FOREIGN KEY (`employee`) REFERENCES `employee` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
