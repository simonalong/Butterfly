CREATE TABLE `neo_uuid_generator` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `namespace` varchar(128) DEFAULT '' COMMENT '命名空间',
  `work_id` int(16) NOT NULL DEFAULT '0' COMMENT '工作id',
  `last_expire_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '下次失效时间',
  `uid` varchar(128) DEFAULT '0' COMMENT '本次启动唯一id',
  `ip` bigint(20) NOT NULL DEFAULT '0' COMMENT 'ip',
  `process_id` varchar(128) NOT NULL DEFAULT '0' COMMENT '进程id',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_name_work` (`namespace`,`work_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
