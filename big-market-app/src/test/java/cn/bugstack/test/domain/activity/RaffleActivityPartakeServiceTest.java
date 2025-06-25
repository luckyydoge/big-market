package cn.bugstack.test.domain.activity;

import cn.bugstack.domain.activity.model.entity.ActivityEntity;
import cn.bugstack.domain.activity.model.entity.PartakeRaffleActivityEntity;
import cn.bugstack.domain.activity.model.entity.UserRaffleOrderEntity;
import cn.bugstack.domain.activity.repository.IActivityRepository;
import cn.bugstack.domain.activity.service.IRaffleActivityPartakeService;
import cn.bugstack.domain.award.model.entity.UserAwardRecordEntity;
import cn.bugstack.domain.award.model.valobj.AwardStateVO;
import cn.bugstack.domain.award.service.IAwardService;
import cn.bugstack.domain.strategy.model.entity.RaffleAwardEntity;
import cn.bugstack.domain.strategy.model.entity.RaffleFactorEntity;
import cn.bugstack.domain.strategy.service.IRaffleStrategy;
import cn.bugstack.types.enums.ResponseCode;
import cn.bugstack.types.exception.AppException;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class RaffleActivityPartakeServiceTest {

    @Resource
    IRaffleStrategy raffleStrategy;

    @Resource
    private IRaffleActivityPartakeService raffleActivityPartakeService;

    @Resource
    private IAwardService awardService;

    @Resource
    IActivityRepository activityRepository;

    @Test
    public void test_createOrder() {
        // 请求参数
        PartakeRaffleActivityEntity partakeRaffleActivityEntity = new PartakeRaffleActivityEntity();
        partakeRaffleActivityEntity.setUserId("xiaofuge");
        partakeRaffleActivityEntity.setActivityId(100301L);
        // 调用接口
        UserRaffleOrderEntity userRaffleOrder = raffleActivityPartakeService.createOrder(partakeRaffleActivityEntity);
        log.info("请求参数：{}", JSON.toJSONString(partakeRaffleActivityEntity));
        log.info("测试结果：{}", JSON.toJSONString(userRaffleOrder));
    }

    @Test
    public void test_saveUserAwardRecord() throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            UserAwardRecordEntity userAwardRecordEntity = new UserAwardRecordEntity();
            userAwardRecordEntity.setUserId("xiaofuge");
            userAwardRecordEntity.setActivityId(100301L);
            userAwardRecordEntity.setStrategyId(100006L);
            userAwardRecordEntity.setOrderId(RandomStringUtils.randomNumeric(12));
            userAwardRecordEntity.setAwardId(101);
            userAwardRecordEntity.setAwardTitle("OpenAI 增加使用次数");
            userAwardRecordEntity.setAwardTime(new Date());
            userAwardRecordEntity.setAwardState(AwardStateVO.create);
            awardService.saveUserAwardRecord(userAwardRecordEntity);
            Thread.sleep(500);
        }
        new CountDownLatch(1).await();
    }

    @Test
    public void test_ActivityRaffle() throws InterruptedException {

        String userId = "xiaofuge";
        Long activityId = 100301L;
        ActivityEntity activityEntity = activityRepository.queryRaffleActivityByActivityId(activityId);

        if (userId == null || activityId == null || userId.isEmpty()) {
            log.error("userId and activityId can't be null or empty");
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }
        PartakeRaffleActivityEntity partakeRaffleActivityEntity = PartakeRaffleActivityEntity.builder()
                .activityId(activityId)
                .userId(userId)
                .build();
        UserRaffleOrderEntity userRaffleOrder = raffleActivityPartakeService.createOrder(partakeRaffleActivityEntity);
        RaffleFactorEntity raffleFactorEntity = RaffleFactorEntity.builder()
                .userId(userId)
                .strategyId(activityEntity.getStrategyId())
                .build();
        RaffleAwardEntity raffleAwardEntity = raffleStrategy.performRaffle(raffleFactorEntity);
        UserAwardRecordEntity userAwardRecordEntity = UserAwardRecordEntity.builder()
                .awardId(raffleAwardEntity.getAwardId())
                .activityId(activityId)
                .userId(userId)
                .strategyId(activityEntity.getStrategyId())
                .orderId(userRaffleOrder.getOrderId())
                .awardTime(new Date())
                .awardState(AwardStateVO.create)
                .awardTitle(raffleAwardEntity.getAwardTitle())
                .build();
        awardService.saveUserAwardRecord(userAwardRecordEntity);
    }

    @Test
    public void test_Random(){
        for (int i = 0; i < 10; i++) {
            System.out.println(RandomStringUtils.randomNumeric(12));
        }

    }


}
