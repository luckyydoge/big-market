CREATE TABLE `raffle_activity`
(
    `id`                  bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `activity_id`         bigint(12)          NOT NULL COMMENT '活动ID',
    `activity_name`       varchar(64)         NOT NULL COMMENT '活动名称',
    `activity_desc`       varchar(128)        NOT NULL COMMENT '活动描述',
    `begin_date_time`     datetime            NOT NULL COMMENT '开始时间',
    `end_date_time`       datetime            NOT NULL COMMENT '结束时间',
    `stock_count`         int(11)             NOT NULL COMMENT '库存总量',
    `stock_count_surplus` int(11)             NOT NULL COMMENT '剩余库存',
    `activity_count_id`   bigint(12)          NOT NULL COMMENT '活动参与次数配置',
    `strategy_id`         bigint(8)           NOT NULL COMMENT '抽奖策略ID',
    `state`               varchar(8)          NOT NULL COMMENT '活动状态',
    `create_time`         datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`         datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_activity_id` (`activity_id`),
    KEY `idx_begin_date_time` (`begin_date_time`),
    KEY `idx_end_date_time` (`end_date_time`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='抽奖活动表';


CREATE TABLE `raffle_activity_count`
(
    `id`                bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `activity_count_id` bigint(12)          NOT NULL COMMENT '活动次数编号',
    `total_count`       int(8)              NOT NULL COMMENT '总次数',
    `day_count`         int(8)              NOT NULL COMMENT '日次数',
    `month_count`       int(8)              NOT NULL COMMENT '月次数',
    `create_time`       datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`       datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_activity_count_id` (`activity_count_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='抽奖活动次数配置表';