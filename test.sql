/*
SQLyog Ultimate v8.32 
MySQL - 5.5.53 
*********************************************************************
*/
/*!40101 SET NAMES utf8 */;


create database bilibili;
use bilibili;

create table `favlist` (
	`aid` int NOT NULL PRIMARY KEY AUTO_INCREMENT,
	`video_title` varchar (100),
	`video_link` varchar (100),
	`video_pubdate` varchar (50),
	`video_author` varchar (50),
	`video_type` varchar (32) default 'ordinary',
	`fav_time` varchar (32),
	`coin_tag` int default 0
) default charset=utf8;



   create table `HistoryVideo` (
            
            `aid` int NOT NULL PRIMARY KEY AUTO_INCREMENT,
	`view_time` varchar (32),
	`video_partition` varchar (32),
	`view_progress` varchar (32),
	`video_title` varchar (100),
	`video_link` varchar (100),
	`video_author` varchar (50),
	`coin_tag` int default 0
            ) default charset=utf8;
