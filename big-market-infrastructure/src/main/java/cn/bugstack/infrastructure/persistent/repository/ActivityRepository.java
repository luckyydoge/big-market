package cn.bugstack.infrastructure.persistent.repository;

import cn.bugstack.domain.activity.model.aggregate.CreateOrderAggregate;
import cn.bugstack.domain.activity.model.entity.ActivityCountEntity;
import cn.bugstack.domain.activity.model.entity.ActivityEntity;
import cn.bugstack.domain.activity.model.entity.ActivityOrderEntity;
import cn.bugstack.domain.activity.model.entity.ActivitySkuEntity;
import cn.bugstack.domain.activity.model.valobj.ActivityStateVO;
import cn.bugstack.domain.activity.repository.IActivityRepository;
import cn.bugstack.infrastructure.persistent.dao.*;
import cn.bugstack.infrastructure.persistent.po.*;
import cn.bugstack.middleware.db.router.strategy.IDBRouterStrategy;
import cn.bugstack.types.enums.ResponseCode;
import cn.bugstack.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;

@Repository
@Slf4j
public class ActivityRepository implements IActivityRepository {

    @Resource
    IRaffleActivityDao activityDao;

    @Resource
    IRaffleActivityOrderDao raffleActivityOrderDao;

    @Resource
    IRaffleActivityAccountDao raffleActivityAccountDao;

    @Resource
    IRaffleActivityCountDao activityCountDao;

    @Resource
    IRaffleActivitySkuDao activitySkuDao;

    @Resource
    IDBRouterStrategy dbRouter;

    @Resource
    TransactionTemplate transactionTemplate;


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

    @Override
    public void doSaveOrder(CreateOrderAggregate createOrderAggregate) {
        try {
// 订单对象
            ActivityOrderEntity activityOrderEntity = createOrderAggregate.getActivityOrderEntity();
            RaffleActivityOrder raffleActivityOrder = new RaffleActivityOrder();
            raffleActivityOrder.setUserId(activityOrderEntity.getUserId());
            raffleActivityOrder.setSku(activityOrderEntity.getSku());
            raffleActivityOrder.setActivityId(activityOrderEntity.getActivityId());
            raffleActivityOrder.setActivityName(activityOrderEntity.getActivityName());
            raffleActivityOrder.setStrategyId(activityOrderEntity.getStrategyId());
            raffleActivityOrder.setOrderId(activityOrderEntity.getOrderId());
            raffleActivityOrder.setOrderTime(activityOrderEntity.getOrderTime());
            raffleActivityOrder.setTotalCount(activityOrderEntity.getTotalCount());
            raffleActivityOrder.setDayCount(activityOrderEntity.getDayCount());
            raffleActivityOrder.setMonthCount(activityOrderEntity.getMonthCount());
            raffleActivityOrder.setTotalCount(createOrderAggregate.getTotalCount());
            raffleActivityOrder.setDayCount(createOrderAggregate.getDayCount());
            raffleActivityOrder.setMonthCount(createOrderAggregate.getMonthCount());
            raffleActivityOrder.setState(activityOrderEntity.getState().getCode());
            raffleActivityOrder.setOutBusinessNo(activityOrderEntity.getOutBusinessNo());

            // 账户对象
            RaffleActivityAccount raffleActivityAccount = new RaffleActivityAccount();
            raffleActivityAccount.setUserId(createOrderAggregate.getUserId());
            raffleActivityAccount.setActivityId(createOrderAggregate.getActivityId());
            raffleActivityAccount.setTotalCount(createOrderAggregate.getTotalCount());
            raffleActivityAccount.setTotalCountSurplus(createOrderAggregate.getTotalCount());
            raffleActivityAccount.setDayCount(createOrderAggregate.getDayCount());
            raffleActivityAccount.setDayCountSurplus(createOrderAggregate.getDayCount());
            raffleActivityAccount.setMonthCount(createOrderAggregate.getMonthCount());
            raffleActivityAccount.setMonthCountSurplus(createOrderAggregate.getMonthCount());

            dbRouter.doRouter(createOrderAggregate.getUserId());
            transactionTemplate.execute(status -> {
                try{
                    raffleActivityOrderDao.insert(raffleActivityOrder);
                    int count = raffleActivityAccountDao.updateAccountQuota(raffleActivityAccount);
                    if (count == 0) {
                        raffleActivityAccountDao.insert(raffleActivityAccount);
                    }
                    return 1;
                } catch (DuplicateKeyException e) {
                    status.setRollbackOnly();
                    log.error("写入订单记录，唯一索引冲突 userId: {} activityId: {} sku: {}", activityOrderEntity.getUserId(), activityOrderEntity.getActivityId(), activityOrderEntity.getSku(), e);
                    throw new AppException(ResponseCode.INDEX_DUP.getCode());
                }
            });
        } finally {
            dbRouter.clear();
        }
    }
}
