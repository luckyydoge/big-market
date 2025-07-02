package cn.bugstack.domain.activity.service;

import cn.bugstack.domain.activity.model.entity.ActivityAccountEntity;
import cn.bugstack.domain.activity.model.entity.SkuRechargeEntity;

public interface IRaffleActivityAccountQuotaService {

    String createOrder(SkuRechargeEntity skuRechargeEntity);

    Integer queryPartakeCnt(Long activityId, String userId);

    ActivityAccountEntity queryActivityAccountEntity(Long activityId, String userId);

    Integer queryRaffleActivityAccountPartakeCount(Long activityId, String userId);
}
