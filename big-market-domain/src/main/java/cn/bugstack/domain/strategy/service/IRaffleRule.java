package cn.bugstack.domain.strategy.service;

import java.util.Map;

public interface IRaffleRule {
    Map<String, Integer> queryAwardRuleLockCnt(String[] treeIds);
}
