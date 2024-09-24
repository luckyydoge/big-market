package cn.bugstack.domain.strategy.service.rule.chain.factory;

import cn.bugstack.domain.strategy.model.entity.StrategyEntity;
import cn.bugstack.domain.strategy.repository.IStrategyRepository;
import cn.bugstack.domain.strategy.service.rule.chain.ILogicChain;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class DefaultChainFactory {
    private final IStrategyRepository repository;

    private Map<String, ILogicChain> chains;

    DefaultChainFactory(IStrategyRepository repository, Map<String, ILogicChain> chains) {
        this.repository = repository;
        this.chains = chains;
    }

    public ILogicChain openLogicChain(Long strategyId) {
        StrategyEntity strategyRuleEntity = repository.queryStrategyEntityById(strategyId);
        String[] ruleModels = strategyRuleEntity.ruleModels();
        if (null == ruleModels || ruleModels.length == 0) {
            return chains.get("default");
        }
        ILogicChain chain = chains.get(ruleModels[0]);
        ILogicChain current = chain;

        for (int i = 1; i < ruleModels.length; i++) {
            ILogicChain next = chains.get(ruleModels[i]);
            current = current.appendNext(next);
        }
        current.appendNext(chains.get("default"));
        return chain;

    }
}
