package cn.bugstack.test;

import cn.bugstack.infrastructure.persistent.redis.IRedisService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Map;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApiTest {

    @Resource
    IRedisService redisService;

    @Test
    public void test() {
        log.info("测试完成");
    }

    @Test
    public void test1() {
        Map<Object, Object> map = redisService.getMap("test");
        map.put(1, 101);
        map.put(2, 102);
        map.put(3, 101);
        map.put(4, 101);
        map.put(5, 101);
        map.put(6, 103);
        map.put(7, 104);
        log.info("test result: {}", redisService.getFromMap("test", 2).toString());
    }


}
