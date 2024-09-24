package cn.bugstack.domain.strategy.service.raffle;

import cn.bugstack.domain.strategy.model.entity.RaffleFactorEntity;
import cn.bugstack.domain.strategy.model.entity.RuleActionEntity;
import cn.bugstack.domain.strategy.model.entity.RuleMatterEntity;
import cn.bugstack.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import cn.bugstack.domain.strategy.repository.IStrategyRepository;
import cn.bugstack.domain.strategy.service.AbstractRaffleStrategy;
import cn.bugstack.domain.strategy.service.armory.IStrategyDispatch;
import cn.bugstack.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import cn.bugstack.domain.strategy.service.rule.filter.ILogicFilter;
import cn.bugstack.domain.strategy.service.rule.filter.factory.DefaultLogicFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DefaultRaffleStrategy extends AbstractRaffleStrategy {

    @Resource
    DefaultLogicFactory logicFactory;

    DefaultRaffleStrategy(IStrategyRepository strategyRepository, IStrategyDispatch dispatch, DefaultChainFactory chainFactory) {
        super(strategyRepository, dispatch, chainFactory);
    }

    @Override
    protected RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> doCheckRaffleBeforeLogic(RaffleFactorEntity raffleFactorEntity, String... logics) {

        if (null == logics || logics.length == 0) {
            return RuleActionEntity.<RuleActionEntity.RaffleBeforeEntity>builder()
                    .code(RuleLogicCheckTypeVO.ALLOW.getCode())
                    .info(RuleLogicCheckTypeVO.ALLOW.getInfo())
                    .build();
        }

        Map<String, ILogicFilter<RuleActionEntity.RaffleBeforeEntity>> filterMap = logicFactory.openFilters();

        String ruleBlackList = Arrays.stream(logics)
                .filter(str -> str.contains(DefaultLogicFactory.LogicModel.RULE_BLACKLIST.getCode()))
                .findFirst()
                .orElse(null);

        if (StringUtils.isNotBlank(ruleBlackList)) {
            RuleMatterEntity ruleMatterEntity = RuleMatterEntity.builder()
                    .userId(raffleFactorEntity.getUserId())
                    .strategyId(raffleFactorEntity.getStrategyId())
                    .ruleModel(ruleBlackList)
                    .build();
            ILogicFilter<RuleActionEntity.RaffleBeforeEntity> iLogicFilter = filterMap.get(DefaultLogicFactory.LogicModel.RULE_BLACKLIST.getCode());
            RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> ruleActionEntity = iLogicFilter.filter(ruleMatterEntity);
            if (!RuleLogicCheckTypeVO.ALLOW.getCode().equals(ruleActionEntity.getCode())) {
                return ruleActionEntity;
            }
        }
        RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> ruleActionEntity = null;

        List<String> ruleList = Arrays.stream(logics)
                .filter(str -> !str.equals(DefaultLogicFactory.LogicModel.RULE_BLACKLIST.getCode()))
                .collect(Collectors.toList());

        for (String rule : ruleList) {
            ILogicFilter<RuleActionEntity.RaffleBeforeEntity> iLogicFilter = filterMap.get(rule);
            RuleMatterEntity ruleMatterEntity = RuleMatterEntity.builder()
                    .userId(raffleFactorEntity.getUserId())
                    .strategyId(raffleFactorEntity.getStrategyId())
                    .ruleModel(rule)
                    .build();
            ruleActionEntity = iLogicFilter.filter(ruleMatterEntity);
            log.info("抽奖前规则过滤 userId: {} ruleModel: {} code: {} info: {}", raffleFactorEntity.getUserId(), rule, ruleActionEntity.getCode(), ruleActionEntity.getInfo());
            if (!RuleLogicCheckTypeVO.ALLOW.getCode().equals(ruleActionEntity.getCode())) {
                return ruleActionEntity;
            }
        }
        return ruleActionEntity;
    }

    @Override
    protected RuleActionEntity<RuleActionEntity.RaffleCenterEntity> doCheckRaffleCenterLogic(RaffleFactorEntity raffleFactorEntity, String... logics) {
        if (null == logics || logics.length == 0) {
            return RuleActionEntity.<RuleActionEntity.RaffleCenterEntity>builder()
                    .code(RuleLogicCheckTypeVO.ALLOW.getCode())
                    .info(RuleLogicCheckTypeVO.ALLOW.getInfo())
                    .build();
        }

        Map<String, ILogicFilter<RuleActionEntity.RaffleCenterEntity>> filterMap = logicFactory.openFilters();
        RuleActionEntity<RuleActionEntity.RaffleCenterEntity> ruleActionEntity = null;

        for (String ruleModel : logics) {
            ILogicFilter<RuleActionEntity.RaffleCenterEntity> iLogicFilter = filterMap.get(ruleModel);
            RuleMatterEntity ruleMatterEntity = RuleMatterEntity.builder()
                    .ruleModel(ruleModel)
                    .strategyId(raffleFactorEntity.getStrategyId())
                    .userId(raffleFactorEntity.getUserId())
                    .awardId(raffleFactorEntity.getAwardId())
                    .build();
            ruleActionEntity = iLogicFilter.filter(ruleMatterEntity);
            if (!RuleLogicCheckTypeVO.ALLOW.getCode().equals(ruleActionEntity.getCode())) {
                return ruleActionEntity;
            }
        }

        return ruleActionEntity;
    }
}
