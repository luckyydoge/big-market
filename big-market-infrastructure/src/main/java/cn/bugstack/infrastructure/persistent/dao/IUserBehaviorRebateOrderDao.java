package cn.bugstack.infrastructure.persistent.dao;

import cn.bugstack.infrastructure.persistent.po.UserBehaviorRebateOrder;
import cn.bugstack.middleware.db.router.annotation.DBRouter;
import cn.bugstack.middleware.db.router.annotation.DBRouterStrategy;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
@DBRouterStrategy(splitTable = true)
public interface IUserBehaviorRebateOrderDao {
    @DBRouter(key = "userId")
    void insert(UserBehaviorRebateOrder userBehaviorRebateOrder);

    @DBRouter(key = "userId")
    List<UserBehaviorRebateOrder> queryOrderByFactor(UserBehaviorRebateOrder req);
}
