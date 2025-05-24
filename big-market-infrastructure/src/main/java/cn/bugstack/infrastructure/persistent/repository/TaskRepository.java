package cn.bugstack.infrastructure.persistent.repository;

import cn.bugstack.domain.award.model.event.SendAwardMessageEvent;
import cn.bugstack.domain.task.model.TaskEntity;
import cn.bugstack.domain.task.repository.ITaskRepository;
import cn.bugstack.infrastructure.event.EventPublisher;
import cn.bugstack.infrastructure.persistent.dao.ITaskDao;
import cn.bugstack.infrastructure.persistent.po.Task;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
@Slf4j
public class TaskRepository implements ITaskRepository {

    @Resource
    private ITaskDao taskDao;

    @Resource
    private SendAwardMessageEvent sendAwardMessageEvent;

    @Resource
    private EventPublisher eventPublisher;

    @Override
    public void sendMessage(TaskEntity taskEntity) {

        String userId = taskEntity.getUserId();

        Task task = new Task();
        task.setUserId(taskEntity.getUserId());
        task.setState(taskEntity.getState());
        task.setTopic(taskEntity.getTopic());
        task.setMessage(JSON.toJSONString(taskEntity.getMessage()));
        task.setMessageId(taskEntity.getMessageId());

        try {
            eventPublisher.publish(sendAwardMessageEvent.topic(), taskEntity.getMessage());
            taskDao.changeState2Completed(task);
        } catch (Exception e) {
            log.info("奖品中奖mq发送失败，userId: {}, messageId: {}, message: {}", userId, taskEntity.getMessageId(), JSON.toJSONString(taskEntity.getMessage()));
            taskDao.changeState2Retry(task);
        }
    }

    @Override
    public List<TaskEntity> queryNoSentMessageTask() {
        List<Task> taskList = taskDao.queryNoSentMessageTask();
        List<TaskEntity> taskEntityList = new ArrayList<>(taskList.size());
        taskList.forEach(task -> {
            taskEntityList.add(TaskEntity.builder()
                    .userId(task.getUserId())
                    .topic(task.getTopic())
                    .messageId(task.getMessageId())
                    .message(task.getMessage())
                    .state(task.getState())
                    .build());
        });
        return taskEntityList;
    }
}
