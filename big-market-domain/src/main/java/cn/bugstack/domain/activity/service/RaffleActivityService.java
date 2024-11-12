package cn.bugstack.domain.activity.service;

import cn.bugstack.domain.activity.repository.IActivityRepository;
import org.springframework.stereotype.Service;

@Service
public class RaffleActivityService extends AbstractRaffleActivity {
    public RaffleActivityService(IActivityRepository activityRepository) {
        super(activityRepository);
    }
}
