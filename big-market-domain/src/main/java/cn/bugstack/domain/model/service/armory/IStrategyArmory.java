package cn.bugstack.domain.model.service.armory;

public interface IStrategyArmory {
    boolean assmbleLotteryStrategy(Long strategyId);

    Integer getRandomAwardId(Long strategyId);
}
