package cn.bugstack.domain.model.service.armory;

import cn.bugstack.domain.model.entity.StrategyAwardEntity;
import cn.bugstack.domain.model.entity.StrategyEntity;
import cn.bugstack.domain.model.entity.StrategyRuleEntity;
import cn.bugstack.domain.model.repository.IStrategyRepository;
import cn.bugstack.types.enums.ResponseCode;
import cn.bugstack.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.*;

@Service
@Slf4j
public class StrategyArmory implements IStrategyArmory, IStrategyDispatch {
    @Resource
    private IStrategyRepository repository;

    @Override
    public boolean assmbleLotteryStrategy(Long strategyId) {
        List<StrategyAwardEntity> strategyAwardEntities = repository.queryStrategyAwardList(strategyId);
        assmbleLotteryStrategy(String.valueOf(strategyId), strategyAwardEntities);

        StrategyEntity strategyEntity = repository.queryStrategyEntityById(strategyId);
        String ruleWeight = strategyEntity.getRuleWeight();
        if (null == ruleWeight) {
            return true;
        }

        StrategyRuleEntity strategyRuleEntity = repository.queryStrategyRule(strategyId, ruleWeight);
        if (null == strategyRuleEntity) {
            throw new AppException(ResponseCode.STRATEGY_RULE_WEIGHT_IS_NULL.getCode(), ResponseCode.STRATEGY_RULE_WEIGHT_IS_NULL.getInfo());
        }

        Map<String, List<Integer>> ruleWeightValueMap = strategyRuleEntity.getRuleWeightValues();
        Set<String> keySet = ruleWeightValueMap.keySet();

        for (String key : keySet) {
            List<Integer> valueList = ruleWeightValueMap.get(key);
            ArrayList<StrategyAwardEntity> strategyAwardEntitiesClone = new ArrayList<>(strategyAwardEntities);

            strategyAwardEntitiesClone.removeIf(entity ->
                !valueList.contains(entity.getAwardId())
            );

            assmbleLotteryStrategy(String.valueOf(strategyId).concat("_").concat(key), strategyAwardEntitiesClone);
        }
        return true;

    }

    public boolean assmbleLotteryStrategy(String key, List<StrategyAwardEntity> strategyAwardEntities) {
        // 1. get the minimum rate
        BigDecimal minimumRate = strategyAwardEntities.stream()
                .map(StrategyAwardEntity::getAwardRate)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO)
                .stripTrailingZeros();

        // 2. get total award rate
        BigDecimal totalAwardRate = strategyAwardEntities.stream()
                .map(StrategyAwardEntity::getAwardRate)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

//        BigDecimal rateRange = totalAwardRate.divide(minimumRate, 0, RoundingMode.CEILING);
        BigDecimal rateMultiple = BigDecimal.valueOf(Math.pow(10, minimumRate.scale()));
        BigDecimal rateRange = totalAwardRate.multiply(rateMultiple);

        List<Integer> strategyAwardSearchTable = new ArrayList<>(rateRange.intValue());
        for (StrategyAwardEntity strategyAwardEntity : strategyAwardEntities) {
            Integer awardId = strategyAwardEntity.getAwardId();
            BigDecimal strategyAwardRate = strategyAwardEntity.getAwardRate();
            for (int i = 0; i < strategyAwardRate.multiply(rateMultiple).intValue(); i++) {
                strategyAwardSearchTable.add(awardId);
            }
        }

        Collections.shuffle(strategyAwardSearchTable);

        Map<Integer, Integer> shuffleStrategyAwardSearchTable = new LinkedHashMap<>();
        for (int i = 0; i < strategyAwardSearchTable.size(); i++) {
            shuffleStrategyAwardSearchTable.put(i, strategyAwardSearchTable.get(i));
        }

        repository.storeStrategyAwardSearchTable(key, rateRange, shuffleStrategyAwardSearchTable);
        return true;

    }

    @Override
    public Integer getRandomAwardId(Long strategyId) {
        int rateRange = repository.getRageRange(strategyId);
        return repository.getStrategyAwardAssemble(String.valueOf(strategyId), new SecureRandom().nextInt(rateRange));
    }

    @Override
    public Integer getRandomAwardId(Long strategyId, String ruleWeightValue) {
        String key = String.valueOf(strategyId).concat("_").concat(ruleWeightValue);
        int rateRange = repository.getRageRange(key);
        return repository.getStrategyAwardAssemble(key, new SecureRandom().nextInt(rateRange));
    }


}
