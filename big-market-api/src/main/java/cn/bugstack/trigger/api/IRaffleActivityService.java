package cn.bugstack.trigger.api;

import cn.bugstack.trigger.api.dto.RaffleActivityDrawRequestDTO;
import cn.bugstack.trigger.api.dto.RaffleActivityDrawResponseDTO;
import cn.bugstack.types.model.Response;

public interface IRaffleActivityService {
    Response<Boolean> armory(Long activityId);

    Response<RaffleActivityDrawResponseDTO> draw(RaffleActivityDrawRequestDTO raffleActivityDrawRequestDTO);
}
