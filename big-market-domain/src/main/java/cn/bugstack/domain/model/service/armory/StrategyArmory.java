package cn.bugstack.domain.model.service.armory;

import cn.bugstack.domain.model.entity.StrategyAwardEntity;
import cn.bugstack.domain.model.repository.IStrategyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.*;

@Service
@Slf4j
public class StrategyArmory implements IStrategyArmory {
    @Resource
    private IStrategyRepository repository;

    @Override
    public boolean assmbleLotteryStrategy(Long strategyId) {
        // 1. query strategy config
        List<StrategyAwardEntity> strategyAwardEntities = repository.queryStrategyAwardList(strategyId);

        // 2. get the minimum rate
        BigDecimal minimumRate = strategyAwardEntities.stream()
                .map(StrategyAwardEntity::getAwardRate)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);

        // 3. get total award rate
        BigDecimal totalAwardRate = strategyAwardEntities.stream()
                .map(StrategyAwardEntity::getAwardRate)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal rateRange = totalAwardRate.divide(minimumRate, 0, RoundingMode.CEILING);

        List<Integer> strategyAwardSearchTable = new ArrayList<>(rateRange.intValue());
        for (StrategyAwardEntity strategyAwardEntity : strategyAwardEntities) {
            Integer awardId = strategyAwardEntity.getAwardId();
            BigDecimal strategyAwardRate = strategyAwardEntity.getAwardRate();
            for (int i = 0; i < strategyAwardRate.multiply(rateRange).setScale(0, RoundingMode.CEILING).intValue(); i++) {
                strategyAwardSearchTable.add(awardId);
            }
        }

        Collections.shuffle(strategyAwardSearchTable);

        Map<Integer, Integer> shuffleStrategyAwardSearchTable = new LinkedHashMap<>();
        for (int i = 0; i < strategyAwardSearchTable.size(); i++) {
            shuffleStrategyAwardSearchTable.put(i, strategyAwardSearchTable.get(i));
        }

        repository.storeStrategyAwardSearchTable(strategyId, rateRange, shuffleStrategyAwardSearchTable);
        return true;
    }

    @Override
    public Integer getRandomAwardId(Long strategyId) {
        int rateRange = repository.getRageRange(strategyId);
        return repository.getStrategyAwardAssemble(strategyId, new SecureRandom().nextInt(rateRange));
    }


}
