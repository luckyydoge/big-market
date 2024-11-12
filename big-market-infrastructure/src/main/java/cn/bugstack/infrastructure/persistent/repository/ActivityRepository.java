package cn.bugstack.infrastructure.persistent.repository;

import cn.bugstack.domain.activity.model.entity.ActivityCountEntity;
import cn.bugstack.domain.activity.model.entity.ActivityEntity;
import cn.bugstack.domain.activity.model.entity.ActivitySkuEntity;
import cn.bugstack.domain.activity.model.valobj.ActivityStateVO;
import cn.bugstack.domain.activity.repository.IActivityRepository;
import cn.bugstack.infrastructure.persistent.dao.IRaffleActivityCountDao;
import cn.bugstack.infrastructure.persistent.dao.IRaffleActivityDao;
import cn.bugstack.infrastructure.persistent.dao.IRaffleActivitySkuDao;
import cn.bugstack.infrastructure.persistent.po.RaffleActivity;
import cn.bugstack.infrastructure.persistent.po.RaffleActivityCount;
import cn.bugstack.infrastructure.persistent.po.RaffleActivitySku;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class ActivityRepository implements IActivityRepository {

    @Resource
    IRaffleActivityDao activityDao;

    @Resource
    IRaffleActivityCountDao activityCountDao;

    @Resource
    IRaffleActivitySkuDao activitySkuDao;


    @Override
    public ActivitySkuEntity queryActivitySku(Long sku) {
        RaffleActivitySku raffleActivitySku = activitySkuDao.queryActivitySku(sku);

        return ActivitySkuEntity.builder()
                .sku(raffleActivitySku.getSku())
                .activityId(raffleActivitySku.getActivityId())
                .stockCountSurplus(raffleActivitySku.getStockCountSurplus())
                .stockCount(raffleActivitySku.getStockCount())
                .activityCountId(raffleActivitySku.getActivityCountId())
                .build();
    }

    @Override
    public ActivityCountEntity queryRaffleActivityCountByActivityCountId(Long activityCountId) {
        RaffleActivityCount raffleActivityCount = activityCountDao.queryRaffleActivityCountByActivityCountId(activityCountId);

        return ActivityCountEntity.builder()
                .activityCountId(raffleActivityCount.getActivityCountId())
                .dayCount(raffleActivityCount.getDayCount())
                .monthCount(raffleActivityCount.getMonthCount())
                .totalCount(raffleActivityCount.getTotalCount())
                .build();
    }

    @Override
    public ActivityEntity queryRaffleActivityByActivityId(Long activityId) {
        RaffleActivity raffleActivity = activityDao.queryRaffleActivityByActivityId(activityId);
        return ActivityEntity.builder()
                .activityDesc(raffleActivity.getActivityDesc())
                .activityId(raffleActivity.getActivityId())
                .activityName(raffleActivity.getActivityName())
                .endDateTime(raffleActivity.getEndDateTime())
                .beginDateTime(raffleActivity.getBeginDateTime())
                .strategyId(raffleActivity.getStrategyId())
                .state(ActivityStateVO.valueOf(raffleActivity.getState()))
                .build();
    }
}
