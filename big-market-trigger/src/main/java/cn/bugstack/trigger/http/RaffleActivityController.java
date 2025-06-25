package cn.bugstack.trigger.http;

import cn.bugstack.domain.activity.model.entity.ActivityEntity;
import cn.bugstack.domain.activity.model.entity.PartakeRaffleActivityEntity;
import cn.bugstack.domain.activity.model.entity.UserRaffleOrderEntity;
import cn.bugstack.domain.activity.repository.IActivityRepository;
import cn.bugstack.domain.activity.service.IRaffleActivityPartakeService;
import cn.bugstack.domain.activity.service.armory.IActivityArmory;
import cn.bugstack.domain.award.model.entity.UserAwardRecordEntity;
import cn.bugstack.domain.award.model.valobj.AwardStateVO;
import cn.bugstack.domain.award.service.IAwardService;
import cn.bugstack.domain.strategy.model.entity.RaffleAwardEntity;
import cn.bugstack.domain.strategy.model.entity.RaffleFactorEntity;
import cn.bugstack.domain.strategy.service.IRaffleStrategy;
import cn.bugstack.domain.strategy.service.armory.IStrategyArmory;
import cn.bugstack.trigger.api.IRaffleActivityService;
import cn.bugstack.trigger.api.dto.RaffleActivityDrawRequestDTO;
import cn.bugstack.trigger.api.dto.RaffleActivityDrawResponseDTO;
import cn.bugstack.types.enums.ResponseCode;
import cn.bugstack.types.exception.AppException;
import cn.bugstack.types.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;

@RestController
@Slf4j
@CrossOrigin("${app.config.cross-origin}")
@RequestMapping("/api/${app.config.api-version}/raffle/activity")
public class RaffleActivityController implements IRaffleActivityService {

    @Resource
    IActivityArmory activityArmory;

    @Resource
    IStrategyArmory strategyArmory;

    @Resource
    IActivityRepository activityRepository;

    @Resource
    IRaffleStrategy raffleStrategy;

    @Resource
    IRaffleActivityPartakeService raffleActivityPartakeService;

    @Resource
    IAwardService awardService;

    @Override
    @RequestMapping(value = "/armory", method = RequestMethod.GET)
    public Response<Boolean> armory(Long activityId) {
        try {
            log.info("activity warm up starts, activityId:{}", activityId);
            ActivityEntity activityEntity = activityRepository.queryRaffleActivityByActivityId(activityId);
            strategyArmory.assembleLotteryStrategy(activityEntity.getStrategyId());
            activityArmory.assembleActivitySkuByActivityId(activityId);
            Response<Boolean> response = Response.<Boolean>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(true)
                    .build();
            log.info("activity warm up succeeds, activityId:{}", activityId);
            return response;
        } catch (Exception e) {
            Response<Boolean> response = Response.<Boolean>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(false)
                    .build();
            log.error("activity warm up fails, activityId:{}", activityId);
            e.printStackTrace();
            return response;
        }
    }

    @Override
    @RequestMapping(value = "/draw", method = RequestMethod.POST)
    public Response<RaffleActivityDrawResponseDTO> draw(@RequestBody RaffleActivityDrawRequestDTO raffleActivityDrawRequestDTO) {
        try {
            String userId = raffleActivityDrawRequestDTO.getUserId();
            Long activityId = raffleActivityDrawRequestDTO.getActivityId();
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

            return Response.<RaffleActivityDrawResponseDTO>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(RaffleActivityDrawResponseDTO.builder()
                            .awardId(raffleAwardEntity.getAwardId())
                            .awardIndex(raffleAwardEntity.getSort())
                            .awardTitle(raffleAwardEntity.getAwardTitle())
                            .build())
                    .build();
        } catch (AppException e) {
            log.error("raffle fail, userId{}, activityId {}", raffleActivityDrawRequestDTO.getUserId(), raffleActivityDrawRequestDTO.getActivityId());
            return Response.<RaffleActivityDrawResponseDTO>builder()
                    .code(e.getCode())
                    .info(e.getInfo())
                    .build();
        } catch (Exception e) {
            log.error("raffle fail, unknown error, userId{}, activityId {}", raffleActivityDrawRequestDTO.getUserId(), raffleActivityDrawRequestDTO.getActivityId());
            return Response.<RaffleActivityDrawResponseDTO>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }

}
