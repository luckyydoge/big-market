package cn.bugstack.domain.activity.service;

import cn.bugstack.domain.activity.model.entity.ActivityOrderEntity;
import cn.bugstack.domain.activity.model.entity.ActivityShopCartEntity;

public interface IRaffleOrder {

    ActivityOrderEntity createRaffleActivityOrder(ActivityShopCartEntity shopCartEntity);
}
