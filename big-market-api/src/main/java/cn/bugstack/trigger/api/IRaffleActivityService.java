package cn.bugstack.trigger.api;

import cn.bugstack.trigger.api.dto.RaffleActivityDrawRequestDTO;
import cn.bugstack.trigger.api.dto.RaffleActivityDrawResponseDTO;
import cn.bugstack.types.model.Response;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

public interface IRaffleActivityService {
    Response<Boolean> armory(Long activityId);

    Response<RaffleActivityDrawResponseDTO> draw(RaffleActivityDrawRequestDTO raffleActivityDrawRequestDTO);

    @RequestMapping(value = "calendar_sign_rebate", method = RequestMethod.POST)
    Response<Boolean> calendarSignRebate(@RequestParam String userId);
}
