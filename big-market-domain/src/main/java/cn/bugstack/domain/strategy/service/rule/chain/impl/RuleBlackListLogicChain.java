package cn.bugstack.domain.strategy.service.rule.chain.impl;

import cn.bugstack.domain.strategy.repository.IStrategyRepository;
import cn.bugstack.domain.strategy.service.rule.chain.AbstractLogicChain;
import cn.bugstack.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component("rule_blacklist")
public class RuleBlackListLogicChain extends AbstractLogicChain {

    @Resource
    IStrategyRepository repository;

    @Override
    public String ruleModel() {
        return "rule_blacklist";
    }

    @Override
    public Integer logic(String userId, Long strategyId) {
        log.info("抽奖责任链-黑名单开始 userId: {} strategyId: {} ruleModel: {}", userId, strategyId, ruleModel());
        String ruleValue = repository.queryStrategyRuleValue(strategyId, this.ruleModel());
        if (null != ruleValue && !ruleValue.isEmpty()) {
            Integer awardId = Integer.valueOf(ruleValue.split(Constants.COLON)[0]);
            String[] usersInRuleValue = ruleValue.split(Constants.COLON)[1].split(Constants.SPLIT);
            for (String userBlackId : usersInRuleValue) {
                if (userId.equals(userBlackId)) {
                    log.info("抽奖责任链-黑名单接管 userId: {} strategyId: {} ruleModel: {} awardId: {}", userId, strategyId, ruleModel(), awardId);
                    return awardId;
                }
            }
        }
        log.info("抽奖责任链-黑名单放行 userId: {} strategyId: {} ruleModel: {}", userId, strategyId, ruleModel());
        return next().logic(userId, strategyId);
    }
}
