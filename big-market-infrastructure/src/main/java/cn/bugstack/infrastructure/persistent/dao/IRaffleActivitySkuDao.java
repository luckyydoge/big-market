package cn.bugstack.infrastructure.persistent.dao;

import cn.bugstack.infrastructure.persistent.po.RaffleActivitySku;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IRaffleActivitySkuDao {
    RaffleActivitySku queryActivitySku(Long sku);

    void clearActivitySkuStock(Long sku);

    void updateActivitySkuStock(Long sku);

    List<RaffleActivitySku> querySkuList(RaffleActivitySku raffleActivitySkuReq);
}
