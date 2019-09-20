

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_redpack
-- ----------------------------
DROP TABLE IF EXISTS `t_redpack`;
CREATE TABLE `t_redpack` (
  `id` int(11) NOT NULL,
  `count` int(5) DEFAULT NULL,
  `price` decimal(8,2) DEFAULT NULL,
  `retainPrice` decimal(8,2) DEFAULT NULL,
  `createDate` date DEFAULT NULL,
  `prcDate` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for t_redpack_log
-- ----------------------------
DROP TABLE IF EXISTS `t_redpack_log`;
CREATE TABLE `t_redpack_log` (
  `id` int(11) NOT NULL,
  `uid` int(11) DEFAULT NULL,
  `rid` int(11) DEFAULT NULL,
  `robMoney` decimal(8,2) DEFAULT NULL,
  `allMoney` decimal(8,2) DEFAULT NULL,
  `createDate` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` int(32) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(55) CHARACTER SET latin1 DEFAULT NULL,
  `age` int(4) DEFAULT NULL,
  `gender` varchar(10) DEFAULT '0',
  `address` varchar(55) DEFAULT NULL,
  `score` decimal(8,2) DEFAULT '0.00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4;

SET FOREIGN_KEY_CHECKS = 1;
