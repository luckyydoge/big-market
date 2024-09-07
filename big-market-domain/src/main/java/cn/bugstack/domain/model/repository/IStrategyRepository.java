package cn.bugstack.domain.model.repository;

import cn.bugstack.domain.model.entity.StrategyAwardEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface IStrategyRepository {
    List<StrategyAwardEntity> queryStrategyAwardList(Long strategyId);

    void storeStrategyAwardSearchTable(Long strategyId, BigDecimal rateRange, Map<Integer, Integer> shuffleStrategyAwardSearchTable);

    int getRageRange(Long strategyId);

    Integer getStrategyAwardAssemble(Long strategyId, int rate);
}
