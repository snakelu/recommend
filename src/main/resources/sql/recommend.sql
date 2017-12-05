drop table if exists rm_action_score;
CREATE TABLE `rm_action_score` (
  `action_id` smallint(11) NOT NULL,
  `action_score` double DEFAULT NULL,
  PRIMARY KEY (`action_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
INSERT INTO `rm_action_score` VALUES ('1', '0.5');
INSERT INTO `rm_action_score` VALUES ('2', '2');
INSERT INTO `rm_action_score` VALUES ('3', '1.5');
INSERT INTO `rm_action_score` VALUES ('4', '1');
INSERT INTO `rm_action_score` VALUES ('5', '1');
INSERT INTO `rm_action_score` VALUES ('6', '1');
INSERT INTO `rm_action_score` VALUES ('7', '3');

drop table if exists rm_common_config;
CREATE TABLE `rm_common_config` (
  `config_name` varchar(16) NOT NULL COMMENT '配置名称',
  `config_value` varchar(16) DEFAULT NULL COMMENT '配置值',
  PRIMARY KEY (`config_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
INSERT INTO `rm_common_config` VALUES ('add_action_count', '100');

drop table if exists rm_exclude_item;
CREATE TABLE `rm_exclude_item` (
  `imei` varchar(64) NOT NULL COMMENT '设备id',
  `user_id` varchar(12) DEFAULT NULL COMMENT '用户id',
  `item_id` varchar(12) NOT NULL COMMENT '活动id',
  KEY `index_imei` (`imei`) USING BTREE,
  KEY `index_userid` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

drop table if exists rm_imei;
CREATE TABLE `rm_imei` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '推荐系统中设备id',
  `imei` varchar(64) NOT NULL COMMENT '设备id',
  PRIMARY KEY (`id`),
  KEY `index_imei` (`imei`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2052 DEFAULT CHARSET=utf8mb4;

drop table if exists rm_item;
CREATE TABLE `rm_item` (
  `item_id` varchar(12) NOT NULL DEFAULT '' COMMENT '活动id',
  `title` varchar(255) NOT NULL DEFAULT '' COMMENT '活动名称',
  `cate_id` varchar(12) NOT NULL DEFAULT '' COMMENT '活动分类',
  `item_tags` varchar(50) DEFAULT '' COMMENT '活动标签',
  `item_modify_time` datetime DEFAULT NULL COMMENT '活动修改时间',
  PRIMARY KEY (`item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

drop table if exists rm_item_score;
CREATE TABLE `rm_item_score` (
  `imei` varchar(64) NOT NULL COMMENT '设备id',
  `user_id` varchar(12) DEFAULT NULL COMMENT '用户id',
  `item_id` varchar(12) NOT NULL COMMENT '活动id',
  `score` double DEFAULT NULL COMMENT '用户喜好程度得分',
  PRIMARY KEY (`imei`,`item_id`),
  KEY `index_userid` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

drop table if exists rm_simple_item;
CREATE TABLE `rm_simple_item` (
  `item_id` varchar(12) NOT NULL COMMENT '活动id',
  `item_tag` varchar(8) NOT NULL COMMENT '活动标签',
  `item_modify_time` bigint(20) NOT NULL COMMENT '活动修改时间',
  PRIMARY KEY (`item_id`,`item_tag`),
  KEY `index_time` (`item_modify_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

drop table if exists rm_user_action;
CREATE TABLE `rm_user_action` (
  `imei` varchar(64) NOT NULL COMMENT '设备id',
  `user_id` varchar(12) DEFAULT NULL COMMENT '用户id',
  `item_id` varchar(12) NOT NULL COMMENT '活动id',
  `action_type` smallint(6) NOT NULL COMMENT '活动分类',
  `action_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '行为发生时间',
  `rec_request_id` varchar(32) DEFAULT NULL COMMENT '推荐id',
  `ext` varchar(255) DEFAULT NULL COMMENT '额外携带信息',
  KEY `index_imei` (`imei`) USING BTREE,
  KEY `index_userid` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;