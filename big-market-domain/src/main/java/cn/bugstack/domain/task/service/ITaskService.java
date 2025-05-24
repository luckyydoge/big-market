package cn.bugstack.domain.task.service;

import cn.bugstack.domain.task.model.TaskEntity;

import java.util.List;

public interface ITaskService {
    void sendMessage(TaskEntity taskEntity);

    List<TaskEntity> queryNoSentMessageTask();
}
