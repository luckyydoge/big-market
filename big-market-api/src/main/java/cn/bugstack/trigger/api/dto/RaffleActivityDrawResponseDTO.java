package cn.bugstack.trigger.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RaffleActivityDrawResponseDTO {
    private String awardTitle;
    private Integer awardId;
    private Integer awardIndex;
}
