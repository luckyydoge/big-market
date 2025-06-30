package cn.bugstack.domain.strategy.service.rule;

import cn.bugstack.domain.strategy.repository.IStrategyRepository;
import cn.bugstack.domain.strategy.service.IRaffleRule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class StrategyRule implements IRaffleRule {

    private final IStrategyRepository strategyRepository;

   public StrategyRule(IStrategyRepository strategyRepository) {
       this.strategyRepository = strategyRepository;
   }

    @Override
    public Map<String, Integer> queryAwardRuleLockCnt(String[] treeIds) {
       Map<String, Integer> awardRuleLockCntMap = new HashMap<>();
       for (String treeId : treeIds) {
           Integer awardRuleLockCnt = strategyRepository.queryAwardRuleLockCnt(treeId);
           awardRuleLockCntMap.put(treeId, awardRuleLockCnt);
       }
        return awardRuleLockCntMap;
    }
}
