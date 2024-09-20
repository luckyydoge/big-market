package cn.bugstack.test.domain;

import cn.bugstack.domain.strategy.service.armory.IStrategyArmory;
import cn.bugstack.domain.strategy.service.armory.IStrategyDispatch;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.stream.IntStream;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class StrategyArmoryTest {

    @Autowired
    private IStrategyArmory strategyArmory;

    @Autowired
    private IStrategyDispatch strategyDispatch;


    @Before
    public void test_strategyAssemble() {
//        strategyArmory.assmbleLotteryStrategy(100002L);
        strategyArmory.assembleLotteryStrategy(100001L);
    }


    /**
     * 从装配的策略中随机获取奖品ID值
     */
    @Test
    public void test_getAssembleRandomVal() {
        IntStream.range(0, 1000).forEach(i -> {
            Integer id = strategyDispatch.getRandomAwardId(100002L);
            Assert.assertNotNull(id);
            log.info("装配100002L: 第i次测试结果：{} - 奖品ID值", id);
        });
    }


    @Test
    public void test_getAssembleRandomVal100001L() {
        IntStream.range(0, 1000).forEach(i -> {
            Integer id = strategyDispatch.getRandomAwardId(100001L);
            Assert.assertNotNull(id);
            log.info("装配100001L: 第i次测试结果：{} - 奖品ID值", id);
        });
    }

    /**
     * 根据策略ID+权重值，从装配的策略中随机获取奖品ID值
     */
    @Test
    public void test_getRandomAwardId_ruleWeightValue() {
        log.info("测试结果：{} - 4000 策略配置", strategyDispatch.getRandomAwardId(100001L, "4000:102,103,104,105"));
        log.info("测试结果：{} - 5000 策略配置", strategyDispatch.getRandomAwardId(100001L, "5000:102,103,104,105,106,107"));
        log.info("测试结果：{} - 6000 策略配置", strategyDispatch.getRandomAwardId(100001L, "6000:102,103,104,105,106,107,108,109"));
    }
}
