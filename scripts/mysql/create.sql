
CREATE TABLE `DYNAMO_GEAR` (
  `dynamo_gear_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `table_name` varchar(32) COLLATE utf8_bin NOT NULL,
  `start_time` time DEFAULT NULL,
  `end_time` time DEFAULT NULL,
  `tps` bigint(20) DEFAULT NULL,
  `valid` tinyint(4) DEFAULT '1',
  `updated_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`dynamo_gear_id`),
  UNIQUE KEY `table_name` (`table_name`,`start_time`),
  KEY `start_time` (`start_time`,`end_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `DYNAMO_GEAR_HIS` (
  `his_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `dynamo_gear_id` bigint(20) NOT NULL,
  `table_name` varchar(32) COLLATE utf8_bin NOT NULL,
  `start_time` datetime DEFAULT NULL,
  `end_time` datetime DEFAULT NULL,
  `curr_write_tps` bigint(20) DEFAULT NULL,
  `expected_write_tps` bigint(20) DEFAULT NULL,
  `changed_write_tps` bigint(20) DEFAULT NULL,
  `updated_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`his_id`),
  KEY `dynamo_gear_id` (`dynamo_gear_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_bin;