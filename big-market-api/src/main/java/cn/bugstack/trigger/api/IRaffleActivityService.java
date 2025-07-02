package cn.bugstack.trigger.api;

import cn.bugstack.trigger.api.dto.RaffleActivityDrawRequestDTO;
import cn.bugstack.trigger.api.dto.RaffleActivityDrawResponseDTO;
import cn.bugstack.trigger.api.dto.UserActivityAccountRequestDTO;
import cn.bugstack.trigger.api.dto.UserActivityAccountResponseDTO;
import cn.bugstack.types.model.Response;
import org.springframework.web.bind.annotation.RequestParam;

public interface IRaffleActivityService {
    Response<Boolean> armory(Long activityId);

    Response<RaffleActivityDrawResponseDTO> draw(RaffleActivityDrawRequestDTO raffleActivityDrawRequestDTO);

    Response<Boolean> calendarSignRebate(@RequestParam String userId);

    Response<Boolean> isCalendarSignRebate(String userId);

    Response<UserActivityAccountResponseDTO> queryUserActivityAccount(UserActivityAccountRequestDTO request);

}
