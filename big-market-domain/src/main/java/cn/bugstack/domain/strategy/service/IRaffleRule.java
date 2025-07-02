package cn.bugstack.domain.strategy.service;

import cn.bugstack.domain.strategy.model.valobj.RuleWeightVO;

import java.util.List;
import java.util.Map;

public interface IRaffleRule {
    Map<String, Integer> queryAwardRuleLockCnt(String[] treeIds);

    List<RuleWeightVO> queryAwardRuleWeightByActivityId(Long activityId);
}
