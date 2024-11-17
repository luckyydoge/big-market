package cn.bugstack.domain.activity.service.rule;

public interface IActionChainArmory {
    IActionChain next();
    IActionChain appendNext(IActionChain next);
}
