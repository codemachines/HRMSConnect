-- phpMyAdmin SQL Dump
-- version 4.8.5
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Apr 19, 2019 at 12:31 PM
-- Server version: 10.3.14-MariaDB
-- PHP Version: 7.3.2

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `id9096249_hrmsdb`
--

-- --------------------------------------------------------

--
-- Table structure for table `employee`
--

CREATE TABLE `employee` (
  `id` int(5) NOT NULL,
  `emp_id` varchar(10) NOT NULL,
  `emp_name` varchar(50) NOT NULL,
  `emp_email` varchar(50) NOT NULL,
  `emp_password` varchar(16) NOT NULL,
  `emp_designation` varchar(50) NOT NULL,
  `emp_mobile` varchar(15) NOT NULL,
  `emp_gender` varchar(7) NOT NULL,
  `emp_profile` varchar(100) NOT NULL,
  `balance` int(5) NOT NULL DEFAULT 50,
  `emp_address` varchar(200) NOT NULL,
  `emp_caddress` varchar(200) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `employee`
--

INSERT INTO `employee` (`id`, `emp_id`, `emp_name`, `emp_email`, `emp_password`, `emp_designation`, `emp_mobile`, `emp_gender`, `emp_profile`, `balance`, `emp_address`, `emp_caddress`) VALUES
(3, 'emp001', 'Jay Dave', 'jay28810@gmail.com', 'jay123456', 'developer', '8238423624', 'Male', 'item null', 25, 'Ahmedabad', 'Ahmedabad'),
(4, 'e002', 'kunal Lodaya', 'simonanthony868@gmail.com', 'kunal123', 'accountant', '8200014704', 'Male', 'item null', 50, 'h/402, vrundavan vatika, nr alok 5, swaminarayan road, vastral, ahmedabad-382418', 'ganpat university, ganesh meridian, nr kargil petrol pump, sola, ahmedabad-380061'),
(5, 'e003', 'shivam dave', 'shivamdave662@gmail.com', '2452535455', 'developer', '6355631310', 'Male', 'item null', 50, '04 harinivas apartment jalaram khaman house near sai baba Temple daxini road maninagar', 'ganpat university, ganesh meridian,  near kargil petrol pump, sola, Ahmedabad - 380061'),
(7, 'e005', 'utkarsh umangbhai  sonvane', 'utkarshsonvane103@gmail.com', 'om989811', 'accountant', '9099553192', 'Male', 'item null', 50, 'a-4/103nirman complex r.c technical road ghatalodia Ahmedabad', 'Ganpat University,ganesh meridian, near kargil petrol pump, sola road, Ahmedabad-61'),
(10, 'e006', 'ujjwalsonvane', 'shivsonvane44@gmail.com', 'dawood', 'developer', '9099875536', 'Male', 'item null', 50, 'D/364 highcourt colony ,vasant nagar township', 'ganpat university, Ganesh meridian, near kargil petrol pump, Sola road , Ahmedabad-61'),
(12, 'emp#002', 'Aneri Thakore', 'aneri.190711@gmail.com', '123456', 'Sales Rep', '9825394455', 'Female', 'item null', 50, 'Ahmedabad', 'Ahmedabad'),
(13, '1', 'Nirav', 'niravk.naik@gmail.com', 'Nirav@1712', '', '7779094524', 'Male', 'item null', 50, 'a/14', '716');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `employee`
--
ALTER TABLE `employee`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `emp_email` (`emp_email`),
  ADD UNIQUE KEY `emp_mobile` (`emp_mobile`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `employee`
--
ALTER TABLE `employee`
  MODIFY `id` int(5) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
