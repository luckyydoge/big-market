package cn.bugstack.domain.strategy.repository;

import cn.bugstack.domain.strategy.model.entity.StrategyAwardEntity;
import cn.bugstack.domain.strategy.model.entity.StrategyEntity;
import cn.bugstack.domain.strategy.model.entity.StrategyRuleEntity;
import cn.bugstack.domain.strategy.model.valobj.RuleTreeVO;
import cn.bugstack.domain.strategy.model.valobj.StrategyAwardRuleModelVO;
import cn.bugstack.domain.strategy.model.valobj.StrategyAwardStockKeyVO;

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

    String queryStrategyRuleValue(Long strategyId, String ruleModel, Integer awardId);

    StrategyAwardRuleModelVO queryStrategyAwardRuleModelVO(Long strategyId, Integer awardId);

    String queryStrategyRuleValue(Long strategyId, String s);

    RuleTreeVO queryRuleTreeVOByTreeId(String treeLock);

    void awardStockConsumeSendQueue(StrategyAwardStockKeyVO strategyAwardStockKeyVO);

    StrategyAwardStockKeyVO takeQueueValue() throws InterruptedException;

    void updateStrategyAwardStock(Long strategyId, Integer awardId);

    Boolean subtractionAwardStock(String cacheKey);

    void cacheStrategyAwardCount(String cacheKey, Integer awardCount);

    StrategyAwardEntity queryStrategyAwardEntity(Long strategyId, Integer awardId);

    Integer queryAwardRuleLockCnt(String treeId);

    Long queryStrategyIdByActivityId(Long activityId);
}
