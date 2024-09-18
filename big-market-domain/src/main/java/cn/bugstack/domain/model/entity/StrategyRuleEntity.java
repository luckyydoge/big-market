package cn.bugstack.domain.model.entity;

import cn.bugstack.types.common.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StrategyRuleEntity {
    private Long strategyId;
    private Integer awardId;
    private Short ruleType;
    private String ruleModel;
    private String ruleValue;
    private String ruleDesc;

    /*
      获取权重值
      数据案例；4000:102,103,104,105 5000:102,103,104,105,106,107 6000:102,103,104,105,106,107,108,109
     */
    public Map<String, List<Integer>> getRuleWeightValues() {
        if (!"rule_weight".equals(ruleModel)) {
            return null;
        }
        String[] ruleValueGroups = ruleValue.split(Constants.SPACE);
        Map<String, List<Integer>> resultMap = new HashMap<>();
        for (String ruleValueGroup : ruleValueGroups) {
            if (null == ruleValueGroup || ruleValueGroup.isEmpty()) {
                return resultMap;
            }
            String[] parts = ruleValueGroup.split(Constants.COLON);
            if (parts.length != 2) {
                throw new IllegalArgumentException("rule_weight rule_value invalid input" + ruleValueGroup);
            }
            String[] valuesString = parts[1].split(Constants.SPLIT);
            List<Integer> values = new ArrayList<>();
            for (String value : valuesString) {
                values.add(Integer.parseInt(value));
            }
            resultMap.put(ruleValueGroup, values);
        }
        return resultMap;
    }


}
