package cn.bugstack.domain.strategy.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RaffleAwardEntity {
    private Integer awardId;
    private String awardConfig;
    private String awardTitle;
    private Integer sort;
}
