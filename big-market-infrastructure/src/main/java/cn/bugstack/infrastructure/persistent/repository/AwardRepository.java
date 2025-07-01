package cn.bugstack.infrastructure.persistent.repository;


import cn.bugstack.domain.activity.model.valobj.OrderStateVO;
import cn.bugstack.domain.award.model.aggregate.UserAwardRecordAggregate;
import cn.bugstack.domain.award.model.entity.TaskEntity;
import cn.bugstack.domain.award.model.entity.UserAwardRecordEntity;
import cn.bugstack.domain.award.event.SendAwardMessageEvent;
import cn.bugstack.domain.award.repository.IAwardRepository;
import cn.bugstack.infrastructure.event.EventPublisher;
import cn.bugstack.infrastructure.persistent.dao.ITaskDao;
import cn.bugstack.infrastructure.persistent.dao.IUserAwardRecordDao;
import cn.bugstack.infrastructure.persistent.dao.IUserRaffleOrderDao;
import cn.bugstack.infrastructure.persistent.po.Task;
import cn.bugstack.infrastructure.persistent.po.UserAwardRecord;
import cn.bugstack.middleware.db.router.strategy.IDBRouterStrategy;
import cn.bugstack.types.enums.ResponseCode;
import cn.bugstack.types.exception.AppException;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;

@Repository
@Slf4j
public class AwardRepository implements IAwardRepository {

    @Resource
    ITaskDao  taskDao;

    @Resource
    IUserAwardRecordDao  userAwardRecordDao;

    @Resource
    IUserRaffleOrderDao userRaffleOrderDao;

    @Resource
    IDBRouterStrategy dbRouter;

    @Resource
    TransactionTemplate transactionTemplate;

    @Resource
    EventPublisher eventPublisher;

    @Resource
    SendAwardMessageEvent sendAwardMessageEvent;

    @Override
    public void saveUserAwardRecord(UserAwardRecordAggregate userAwardRecordAggregate) {
        TaskEntity taskEntity = userAwardRecordAggregate.getTaskEntity();
        UserAwardRecordEntity userAwardRecordEntity = userAwardRecordAggregate.getUserAwardRecordEntity();
        String orderId = userAwardRecordEntity.getOrderId();
        String userId = userAwardRecordEntity.getUserId();
        Long activityId = userAwardRecordEntity.getActivityId();
        Integer awardId = userAwardRecordEntity.getAwardId();

        Task task = new Task();
        task.setUserId(taskEntity.getUserId());
        task.setState(taskEntity.getState());
        task.setTopic(taskEntity.getTopic());
        task.setMessage(JSON.toJSONString(taskEntity.getMessage()));
        task.setMessageId(taskEntity.getMessageId());

        UserAwardRecord userAwardRecord = new UserAwardRecord();
        userAwardRecord.setAwardState(userAwardRecordEntity.getAwardState().getCode());
        userAwardRecord.setAwardTime(userAwardRecordEntity.getAwardTime());
        userAwardRecord.setAwardId(userAwardRecordEntity.getAwardId());
        userAwardRecord.setAwardTitle(userAwardRecordEntity.getAwardTitle());
        userAwardRecord.setUserId(userAwardRecordEntity.getUserId());
        userAwardRecord.setOrderId(userAwardRecordEntity.getOrderId());
        userAwardRecord.setActivityId(userAwardRecordEntity.getActivityId());
        userAwardRecord.setStrategyId(userAwardRecordEntity.getStrategyId());

        try {
            dbRouter.doRouter(userAwardRecordEntity.getUserId());
            transactionTemplate.execute(status -> {
                try {
                    userRaffleOrderDao.changeOrderStatus(orderId, OrderStateVO.completed.getCode());
                    taskDao.insert(task);
                    userAwardRecordDao.insert(userAwardRecord);
                    return 1;

                } catch (DuplicateKeyException e) {
                    status.setRollbackOnly();
                    log.info("写入中奖记录，唯一索引冲突：userId: {}, orderId: {}, activityId: {}, awardId: {}", userId, orderId, activityId, awardId);
                    throw new AppException(ResponseCode.INDEX_DUP.getCode(), e);
                }
            });
        } finally {
            dbRouter.clear();
        }

        try {
            eventPublisher.publish(sendAwardMessageEvent.topic(), taskEntity.getMessage());
            taskDao.changeState2Completed(task);
        } catch (Exception e) {
            log.info("奖品中奖mq发送失败，userId: {}, messageId: {}, message: {}", userId, taskEntity.getMessageId(), JSON.toJSONString(taskEntity.getMessage()));
            taskDao.changeState2Retry(task);
        }


    }
}
