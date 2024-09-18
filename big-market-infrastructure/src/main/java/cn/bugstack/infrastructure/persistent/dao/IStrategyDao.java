package cn.bugstack.infrastructure.persistent.dao;

import cn.bugstack.infrastructure.persistent.po.Award;
import cn.bugstack.infrastructure.persistent.po.Strategy;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IStrategyDao {
    List<Award> queryStrategyList();

    Strategy queryStrategyById(Long strategyId);
}
