-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jan 30, 2026 at 01:00 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `chatterbox_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `messages`
--

CREATE TABLE IF NOT EXISTS `messages` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sender` varchar(50) NOT NULL,
  `msg` text NOT NULL,
  `msg_time` timestamp NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=474 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Triggers `messages`
--
DELIMITER $$
CREATE TRIGGER `auto_backup_before_delete` BEFORE DELETE ON `messages` FOR EACH ROW INSERT INTO messages_backup(original_id, sender,msg,msg_time)
VALUES(OLD.id,OLD.sender,OLD.msg,OLD.msg_time)
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `messages_backup`
--

CREATE TABLE IF NOT EXISTS `messages_backup` (
  `backup_id` int(11) NOT NULL AUTO_INCREMENT,
  `original_id` int(11) DEFAULT NULL,
  `sender` varchar(255) DEFAULT NULL,
  `msg` text DEFAULT NULL,
  `msg_time` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `deleted_at` timestamp NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`backup_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE IF NOT EXISTS `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `fullname` varchar(100) DEFAULT NULL,
  `username` varchar(50) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `password` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `fullname`, `username`, `email`, `password`) VALUES
(5, 'jay', 'jay', 'jay@xyz.com', 'bhendi'),
(6, 'anuj chauhan', 'anuj', 'xyz@gmail.com', 'anju'),
(9, 'Adwait', 'admin', 'admin@chatterbox.com', 'whyadmin'),
(12, 'rohit ingle', 'rohit', 'rohit@chatterbox.com', 'balya'),
(13, 'archana', 'archana', 'archna@chatterbox.com', '1234'),
(14, 'babu', 'babu', 'babu@chatterbox.com', 'babu'),
(15, 'manu', 'manu', 'manu@chatterbox.com', 'jayhanuman');

DELIMITER $$
--
-- Events
--
CREATE DEFINER=`root`@`localhost` EVENT `auto_delete_history` ON SCHEDULE EVERY 1 MINUTE STARTS '2026-01-12 22:59:55' ON COMPLETION NOT PRESERVE ENABLE DO DELETE FROM messages WHERE msg_time < NOW() -INTERVAL 3 MINUTE$$

DELIMITER ;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
