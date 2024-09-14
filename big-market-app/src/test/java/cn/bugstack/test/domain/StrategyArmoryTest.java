package cn.bugstack.test.domain;

import cn.bugstack.domain.model.service.armory.IStrategyArmory;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
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


    @Test
    public void test_strategyAssemble() {
        strategyArmory.assmbleLotteryStrategy(100002L);
    }


    /**
     * 从装配的策略中随机获取奖品ID值
     */
    @Test
    public void test_getAssembleRandomVal() {
        IntStream.range(0, 1000).forEach(i -> {
            Integer id = strategyArmory.getRandomAwardId(100002L);
            Assert.assertNotNull(id);
            log.info("装配100002L: 第i次测试结果：{} - 奖品ID值", id);
        });
    }

    @Test
    public void test_strategyAssemble100001L() {
        strategyArmory.assmbleLotteryStrategy(100001L);
    }

    @Test
    public void test_getAssembleRandomVal100001L() {
        IntStream.range(0, 100000).forEach(i -> {
            Integer id = strategyArmory.getRandomAwardId(100001L);
            Assert.assertNotNull(id);
            log.info("装配100001L: 第i次测试结果：{} - 奖品ID值", id);
        });
    }
}
