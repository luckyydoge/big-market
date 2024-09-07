package cn.bugstack.infrastructure.persistent.po;

import lombok.Data;

import java.util.Date;

@Data
public class StrategyRule {
    private Long id;
    private Integer strategyId;
    private Integer awardId;
    private Short ruleType;
    private String ruleModel;
    private String ruleValue;
    private String ruleDesc;
    private Date createTime;
    private Date updateTime;
}