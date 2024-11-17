package cn.bugstack.test.infrastructure;

import cn.bugstack.infrastructure.persistent.dao.IStrategyRuleDao;
import cn.bugstack.infrastructure.persistent.po.Award;
import cn.bugstack.infrastructure.persistent.po.StrategyRule;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class StrategyruleDaoTest {

    @Resource
    private IStrategyRuleDao strategyRuleDao;

    @Test
    public void test_queryAwardList() {
        List<Award> awards = strategyRuleDao.queryStrategyRuleList();
        log.info("测试结果：{}", JSON.toJSONString(awards));
    }

    @Test
    public void test_queryStrategyRuleValueTest() {
        StrategyRule strategyRule = new StrategyRule();
        strategyRule.setRuleModel("rule_blacklist");
        strategyRule.setStrategyId(100001L);
        String ruleValue = strategyRuleDao.queryStrategyRuleValue(strategyRule);
        log.info("{}", ruleValue);
    }

}