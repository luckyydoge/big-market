package cn.bugstack.test.controller;

import cn.bugstack.trigger.api.IRaffleActivityService;
import cn.bugstack.types.model.Response;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import static java.lang.Thread.sleep;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class RaffleRebateControllerTest {

    @Resource
    private IRaffleActivityService raffleActivityService;

    @Test
    public void test_calendarSignRebate(){
        Response<Boolean> response = raffleActivityService.calendarSignRebate("zjw");
        log.info("测试结果：{}", JSON.toJSONString(response));
        try {
            sleep(3000);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
