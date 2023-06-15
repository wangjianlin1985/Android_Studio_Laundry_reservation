/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50051
Source Host           : localhost:3306
Source Database       : wash_db

Target Server Type    : MYSQL
Target Server Version : 50051
File Encoding         : 65001

Date: 2017-12-05 21:49:50
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `admin`
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin` (
  `username` varchar(20) NOT NULL,
  `password` varchar(20) default NULL,
  PRIMARY KEY  (`username`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES ('a', 'a');

-- ----------------------------
-- Table structure for `mealevaluate`
-- ----------------------------
DROP TABLE IF EXISTS `mealevaluate`;
CREATE TABLE `mealevaluate` (
  `evaluateId` int(11) NOT NULL auto_increment,
  `washMealObj` int(11) default NULL,
  `evaluateContent` varchar(2000) default NULL,
  `userObj` varchar(20) default NULL,
  `evaluateTime` varchar(20) default NULL,
  PRIMARY KEY  (`evaluateId`),
  KEY `FK38DB059C107609D9` (`washMealObj`),
  KEY `FK38DB059CC80FC67` (`userObj`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of mealevaluate
-- ----------------------------
INSERT INTO `mealevaluate` VALUES ('1', '1', '洗的很干净哦', 'user1', '2017-12-01 01:14:53');
INSERT INTO `mealevaluate` VALUES ('2', '1', '下次还会来哦！', 'user1', '2017-12-05 18:15:56');

-- ----------------------------
-- Table structure for `orderinfo`
-- ----------------------------
DROP TABLE IF EXISTS `orderinfo`;
CREATE TABLE `orderinfo` (
  `orderId` int(11) NOT NULL auto_increment,
  `washMealObj` int(11) default NULL,
  `orderCount` int(11) default NULL,
  `userObj` varchar(20) default NULL,
  `telephone` varchar(20) default NULL,
  `orderTime` varchar(20) default NULL,
  `orderState` int(11) default NULL,
  `memo` varchar(20) default NULL,
  PRIMARY KEY  (`orderId`),
  KEY `FK601628FC107609D9` (`washMealObj`),
  KEY `FK601628FCE83E3F68` (`orderState`),
  KEY `FK601628FCC80FC67` (`userObj`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of orderinfo
-- ----------------------------
INSERT INTO `orderinfo` VALUES ('1', '1', '2', 'user1', '13958342342', '2017-11-30 15:13:14', '2', '马上就送来！');
INSERT INTO `orderinfo` VALUES ('2', '2', '2', 'user1', '13983423432', '2017-12-05 17:46:25', '1', '我有2件衣服要洗');

-- ----------------------------
-- Table structure for `orderstate`
-- ----------------------------
DROP TABLE IF EXISTS `orderstate`;
CREATE TABLE `orderstate` (
  `orderStateId` int(11) NOT NULL auto_increment,
  `stateName` varchar(20) default NULL,
  PRIMARY KEY  (`orderStateId`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of orderstate
-- ----------------------------
INSERT INTO `orderstate` VALUES ('1', '用户已下单');
INSERT INTO `orderstate` VALUES ('2', '店铺工作中');
INSERT INTO `orderstate` VALUES ('3', '店铺已经洗好');
INSERT INTO `orderstate` VALUES ('4', '用户交易完成');

-- ----------------------------
-- Table structure for `userinfo`
-- ----------------------------
DROP TABLE IF EXISTS `userinfo`;
CREATE TABLE `userinfo` (
  `user_name` varchar(20) NOT NULL,
  `password` varchar(20) default NULL,
  `name` varchar(20) default NULL,
  `sex` varchar(4) default NULL,
  `birthDate` datetime default NULL,
  `userPhoto` varchar(50) default NULL,
  `telephone` varchar(20) default NULL,
  `address` varchar(80) default NULL,
  `latitude` float(9,6) default NULL,
  `longitude` float(9,6) default NULL,
  `regTime` varchar(20) default NULL,
  PRIMARY KEY  (`user_name`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of userinfo
-- ----------------------------
INSERT INTO `userinfo` VALUES ('user1', '123', '双鱼林', '男', '2017-11-08 00:00:00', 'upload/90F9147F8E1CD47E4F40FA953E3D17EC.jpg', '13573598343', '成都理工大学理工东苑13号', '30.688467', '104.159599', '2017-11-29 14:15:15');
INSERT INTO `userinfo` VALUES ('bhgb', '155', 'vv', 'vg', '2017-12-05 00:00:00', 'upload/noimage.jpg', 'ggnd', 'hg', '30.687792', '104.146172', '--');

-- ----------------------------
-- Table structure for `washclass`
-- ----------------------------
DROP TABLE IF EXISTS `washclass`;
CREATE TABLE `washclass` (
  `classId` int(11) NOT NULL auto_increment,
  `className` varchar(50) default NULL,
  `classDesc` varchar(2000) default NULL,
  PRIMARY KEY  (`classId`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of washclass
-- ----------------------------
INSERT INTO `washclass` VALUES ('1', '学生干洗店', '面对大学生的干洗店');
INSERT INTO `washclass` VALUES ('2', '水洗店', '可以用水洗的');

-- ----------------------------
-- Table structure for `washmeal`
-- ----------------------------
DROP TABLE IF EXISTS `washmeal`;
CREATE TABLE `washmeal` (
  `mealId` int(11) NOT NULL auto_increment,
  `mealName` varchar(60) default NULL,
  `introduce` varchar(2000) default NULL,
  `price` float default NULL,
  `mealPhoto` varchar(50) default NULL,
  `publishDate` datetime default NULL,
  `washShopObj` varchar(20) default NULL,
  PRIMARY KEY  (`mealId`),
  KEY `FK22DE172253C3D8F9` (`washShopObj`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of washmeal
-- ----------------------------
INSERT INTO `washmeal` VALUES ('1', '男生上衣干洗套餐', '学生冬季男生上衣干洗一件', '25', 'upload/63518EFC4DA71B7ED3B7A9402373F5CA.jpg', '2017-11-30 00:00:00', 'shop001');
INSERT INTO `washmeal` VALUES ('2', '冬季衣服水洗套餐', '水洗来这里', '20', 'upload/noimage.jpg', '2017-12-06 00:00:00', 'shop002');
INSERT INTO `washmeal` VALUES ('3', '111', '222', '33', 'upload/noimage.jpg', '2017-12-05 00:00:00', 'shop001');

-- ----------------------------
-- Table structure for `washshop`
-- ----------------------------
DROP TABLE IF EXISTS `washshop`;
CREATE TABLE `washshop` (
  `shopUserName` varchar(20) NOT NULL,
  `password` varchar(20) default NULL,
  `shopName` varchar(50) default NULL,
  `washClassObj` int(11) default NULL,
  `shopPhoto` varchar(50) default NULL,
  `telephone` varchar(20) default NULL,
  `addDate` datetime default NULL,
  `address` varchar(80) default NULL,
  `latitude` float(9,6) default NULL,
  `longitude` float(9,6) default NULL,
  PRIMARY KEY  (`shopUserName`),
  KEY `FK22E0DE5561A5FA75` (`washClassObj`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of washshop
-- ----------------------------
INSERT INTO `washshop` VALUES ('shop001', '123', '理工大学清之源干洗', '1', 'upload/C4662DD8EF4ED38290794CB5772C93A8.jpg', '028-83439834', '2017-11-30 00:00:00', '成都理工大学桐荫路附近', '30.681574', '104.152695');
INSERT INTO `washshop` VALUES ('shop002', '123', '大学松林宿舍洗衣', '2', 'upload/noimage.jpg', '028-83493224', '2017-12-06 00:00:00', '成都二仙桥东路15号附66号3栋115号', '30.679602', '104.158546');
