package cn.bugstack.trigger.api.dto;

import lombok.Data;

@Data
public class RaffleAwardListRequestDTO {

    // 抽奖策略ID
    private Long strategyId;
    private String userId;
    private Long activityId;
}
