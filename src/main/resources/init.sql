/*==============================================================*/
create database if not exists fastWebsite default charset utf8;
/*==============================================================*/
-- ----------------------------
-- Table structure for sys_code
-- ----------------------------
CREATE TABLE IF NOT EXISTS `sys_code` (
  `id` varchar(64) NOT NULL,
  `parent_id` varchar(64) DEFAULT NULL,
  `group_id` varchar(64) DEFAULT NULL,
  `code` varchar(255) DEFAULT NULL,
  `value` varchar(255) DEFAULT NULL,
  `sqeuence` int(11) DEFAULT NULL COMMENT '排序',
  `enable` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*==============================================================*/