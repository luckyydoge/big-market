package cn.bugstack.domain.award.service;

import cn.bugstack.domain.award.model.aggregate.UserAwardRecordAggregate;
import cn.bugstack.domain.award.model.entity.TaskEntity;
import cn.bugstack.domain.award.model.entity.UserAwardRecordEntity;
import cn.bugstack.domain.award.model.event.SendAwardMessageEvent;
import cn.bugstack.domain.award.model.valobj.AwardStateVO;
import cn.bugstack.domain.award.repository.IAwardRepository;
import cn.bugstack.types.event.BaseEvent;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AwardService implements IAwardService{

    private final IAwardRepository iAwardRepository;

    private final SendAwardMessageEvent sendAwardMessageEvent;

    public AwardService(IAwardRepository iAwardRepository, SendAwardMessageEvent sendAwardMessageEvent) {
        this.sendAwardMessageEvent = sendAwardMessageEvent;
        this.iAwardRepository = iAwardRepository;
    }

    @Override
    public void saveUserAwardRecord(UserAwardRecordEntity userAwardRecordEntity) {
        SendAwardMessageEvent.SendAwardMessage sendAwardMessage = new SendAwardMessageEvent.SendAwardMessage();
        sendAwardMessage.setAwardTitle(userAwardRecordEntity.getAwardTitle());
        sendAwardMessage.setUserId(userAwardRecordEntity.getUserId());
        sendAwardMessage.setAwardId(userAwardRecordEntity.getAwardId());

        BaseEvent.EventMessage<SendAwardMessageEvent.SendAwardMessage> eventMessage = sendAwardMessageEvent.buildEventMessage(sendAwardMessage);

        TaskEntity taskEntity = TaskEntity.builder()
                .userId(userAwardRecordEntity.getUserId())
                .topic(sendAwardMessageEvent.topic())
                .state(AwardStateVO.create.getCode())
                .messageId(eventMessage.getId())
                .message(JSON.toJSONString(eventMessage))
                .build();

        UserAwardRecordAggregate userAwardRecordAggregate = UserAwardRecordAggregate.builder()
                .taskEntity(taskEntity)
                .userAwardRecordEntity(userAwardRecordEntity)
                .build();

        iAwardRepository.saveUserAwardRecord(userAwardRecordAggregate);



    }
}
