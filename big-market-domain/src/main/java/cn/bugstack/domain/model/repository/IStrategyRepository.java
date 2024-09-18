package cn.bugstack.domain.model.repository;

import cn.bugstack.domain.model.entity.StrategyAwardEntity;
import cn.bugstack.domain.model.entity.StrategyEntity;
import cn.bugstack.domain.model.entity.StrategyRuleEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface IStrategyRepository {
    List<StrategyAwardEntity> queryStrategyAwardList(Long strategyId);

    void storeStrategyAwardSearchTable(String key, BigDecimal rateRange, Map<Integer, Integer> shuffleStrategyAwardSearchTable);

    int getRageRange(Long strategyId);


    StrategyEntity queryStrategyEntityById(Long strategyId);

    StrategyRuleEntity queryStrategyRule(Long strategyId, String ruleModel);

    int getRageRange(String key);

    Integer getStrategyAwardAssemble(String key, int rate);
}
