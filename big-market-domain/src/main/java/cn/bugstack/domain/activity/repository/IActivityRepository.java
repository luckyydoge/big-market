package cn.bugstack.domain.activity.repository;

import cn.bugstack.domain.activity.model.entity.ActivityCountEntity;
import cn.bugstack.domain.activity.model.entity.ActivityEntity;
import cn.bugstack.domain.activity.model.entity.ActivitySkuEntity;

public interface IActivityRepository {
    ActivitySkuEntity queryActivitySku(Long sku);

    ActivityCountEntity queryRaffleActivityCountByActivityCountId(Long activityCountId);

    ActivityEntity queryRaffleActivityByActivityId(Long activityId);
}
