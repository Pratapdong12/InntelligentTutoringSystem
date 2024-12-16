-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Generation Time: Dec 16, 2024 at 09:20 PM
-- Server version: 8.0.31
-- PHP Version: 8.1.13

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `area_learning`
--

-- --------------------------------------------------------

--
-- Table structure for table `question`
--

DROP TABLE IF EXISTS `question`;
CREATE TABLE IF NOT EXISTS `question` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `answer` varchar(255) DEFAULT NULL,
  `level` int NOT NULL,
  `question` varchar(255) DEFAULT NULL,
  `iriid` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `question`
--

INSERT INTO `question` (`id`, `answer`, `level`, `question`, `iriid`) VALUES
(1, '25', 1, 'What is the area of a triangle with base 10 and height 5?', NULL),
(2, '16', 1, 'What is the area of a square with side 4?', NULL),
(3, '18', 1, 'What is the area of a rectangle with length 6 and width 3?', NULL),
(4, '153.94', 1, 'What is the area of a circle with radius 7?', NULL),
(5, '6', 1, 'What is the area of a triangle with base 6 and height 2?', NULL),
(6, '63', 2, 'What is the area of a triangle with base 14 and height 9?', NULL),
(7, '64', 2, 'What is the area of a square with side 8?', NULL),
(8, '60', 2, 'What is the area of a rectangle with length 12 and width 5?', NULL),
(9, '314.16', 2, 'What is the area of a circle with radius 10?', NULL),
(10, '63', 2, 'What is the area of a triangle with base 18 and height 7?', NULL),
(11, '150', 3, 'What is the area of a triangle with base 20 and height 15?', NULL),
(12, '225', 3, 'What is the area of a square with side 15?', NULL),
(13, '240', 3, 'What is the area of a rectangle with length 20 and width 12?', NULL),
(14, '1256.64', 3, 'What is the area of a circle with radius 20?', NULL),
(15, '120', 3, 'What is the area of a triangle with base 24 and height 10?', NULL),
(16, '24', 1, 'What is the area of a triangle with base 8 and height 6?', NULL),
(17, '25', 1, 'What is the area of a square with side 5?', NULL),
(18, '28', 1, 'What is the area of a rectangle with length 7 and width 4?', NULL),
(19, '50.24', 1, 'What is the area of a circle with radius 4?', NULL),
(20, '13.5', 1, 'What is the area of a triangle with base 9 and height 3?', NULL),
(21, '60', 2, 'What is the area of a triangle with base 15 and height 8?', NULL),
(22, '100', 2, 'What is the area of a square with side 10?', NULL),
(23, '54', 2, 'What is the area of a rectangle with length 9 and width 6?', NULL),
(24, '452.39', 2, 'What is the area of a circle with radius 12?', NULL),
(25, '40', 2, 'What is the area of a triangle with base 16 and height 5?', NULL),
(26, '108', 3, 'What is the area of a triangle with base 18 and height 12?', NULL),
(27, '400', 3, 'What is the area of a square with side 20?', NULL),
(28, '375', 3, 'What is the area of a rectangle with length 25 and width 15?', NULL),
(29, '1017.88', 3, 'What is the area of a circle with radius 18?', NULL),
(30, '210', 3, 'What is the area of a triangle with base 30 and height 14?', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `student`
--

DROP TABLE IF EXISTS `student`;
CREATE TABLE IF NOT EXISTS `student` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `current_level` int NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `student`
--

INSERT INTO `student` (`id`, `current_level`, `password`, `username`, `first_name`, `last_name`) VALUES
(23, 3, '12345', 'pratap.dong', 'Pratap', 'Dong'),
(24, 3, '12345', 'e.hazard', 'Eden', 'Hazard');

-- --------------------------------------------------------

--
-- Table structure for table `test_result`
--

DROP TABLE IF EXISTS `test_result`;
CREATE TABLE IF NOT EXISTS `test_result` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `level` int NOT NULL,
  `score` int NOT NULL,
  `student_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK7ar2v5mp1uyo2uuvun38gvf12` (`student_id`)
) ENGINE=InnoDB AUTO_INCREMENT=58 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `test_result`
--

INSERT INTO `test_result` (`id`, `level`, `score`, `student_id`) VALUES
(50, 1, 10, 23),
(51, 2, 10, 23),
(52, 3, 10, 23),
(53, 1, 10, 24),
(54, 2, 10, 24),
(55, 1, 10, 23),
(56, 2, 0, 23),
(57, 2, 10, 23);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `test_result`
--
ALTER TABLE `test_result`
  ADD CONSTRAINT `FK7ar2v5mp1uyo2uuvun38gvf12` FOREIGN KEY (`student_id`) REFERENCES `student` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
