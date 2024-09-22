package cn.bugstack.domain.strategy.model.entity;

import cn.bugstack.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RuleActionEntity <T extends RuleActionEntity.RaffleEntity>{
    private String code = RuleLogicCheckTypeVO.ALLOW.getCode();
    private String info = RuleLogicCheckTypeVO.ALLOW.getInfo();
    private String ruleModel;
    private T data;

    static public class RaffleEntity {}

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    static public class RaffleBeforeEntity extends RaffleEntity {
        private String ruleWeightValueKey;
        private Long strategyId;
        private Integer awardId;
    }
    static public class RaffleCenterEntity extends RaffleEntity {}
    static public class RaffleAfterEntity extends RaffleEntity {}
}
