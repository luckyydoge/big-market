package cn.bugstack.domain.strategy.service.rule.chain.impl;

import cn.bugstack.domain.strategy.repository.IStrategyRepository;
import cn.bugstack.domain.strategy.service.armory.IStrategyDispatch;
import cn.bugstack.domain.strategy.service.rule.chain.AbstractLogicChain;
import cn.bugstack.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

@Slf4j
@Component("rule_weight")
public class RuleWeightLogicChain extends AbstractLogicChain {

    @Resource
    IStrategyRepository repository;

    @Resource
    IStrategyDispatch dispatch;

    public Long userScore = 0L;

    @Override
    public String ruleModel() {
        return "rule_weight";
    }

    @Override
    public Integer logic(String userId, Long strategyId) {
        log.info("抽奖责任链-权重开始 userId: {} strategyId: {} ruleModel: {}", userId, strategyId, ruleModel());
        String ruleValue = repository.queryStrategyRuleValue(strategyId, userId);
        if (null != ruleValue && !ruleValue.isEmpty()) {
            Map<Long, String> analyticalValueMap = getAnalyticalValue(ruleValue);
            if (!analyticalValueMap.isEmpty()) {
                List<Long> analyticalSortedKeys = new ArrayList<>(analyticalValueMap.keySet());
                Collections.sort(analyticalSortedKeys);

                Long weightValue = analyticalSortedKeys.stream()
                        .filter(key -> userScore >= key)
                        .max(Long::compareTo)
                        .orElse(null);

                if (null != weightValue) {
                    Integer awardId = dispatch.getRandomAwardId(strategyId, analyticalValueMap.get(weightValue));
                    log.info("抽奖责任链-权重接管 userId: {} strategyId: {} ruleModel: {} awardId: {}", userId, strategyId, ruleModel(), awardId);
                    return awardId;
                }
            }

        }
        log.info("抽奖责任链-权重放行 userId: {} strategyId: {} ruleModel: {}", userId, strategyId, ruleModel());
        return next().logic(userId, strategyId);
    }

    private Map<Long, String> getAnalyticalValue(String ruleValueKey) {
        String[] ruleValueGroups = ruleValueKey.split(Constants.SPACE);
        Map<Long, String> ruleValueMap = new HashMap<>();
        for (String ruleValueGroup : ruleValueGroups) {
            String[] parts = ruleValueGroup.split(Constants.COLON);
            if (parts.length != 2) {
                throw new IllegalArgumentException("rule_weight rule_rule invalid input format" + ruleValueKey);
            }
            ruleValueMap.put(Long.parseLong(parts[0]), ruleValueKey);
        }
        return ruleValueMap;

    }
}
