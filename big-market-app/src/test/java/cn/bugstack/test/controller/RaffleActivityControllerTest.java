package cn.bugstack.test.controller;

import cn.bugstack.trigger.api.IRaffleActivityService;
import cn.bugstack.trigger.api.dto.RaffleActivityDrawRequestDTO;
import cn.bugstack.trigger.api.dto.RaffleActivityDrawResponseDTO;
import cn.bugstack.types.model.Response;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class RaffleActivityControllerTest {

    @Resource
    private IRaffleActivityService raffleActivityService;

    @Test
    public void test_armory() {
        Response response = raffleActivityService.armory(100301L);
        log.info("result:{}", JSON.toJSONString(response));
    }

    @Test
    public void test_draw() {
        RaffleActivityDrawRequestDTO req =  new RaffleActivityDrawRequestDTO();
        req.setUserId("xiaofuge");
        req.setActivityId(100301L);
        Response<RaffleActivityDrawResponseDTO> response = raffleActivityService.draw(req);
        log.info("result: {}", JSON.toJSONString(response));
    }
}
