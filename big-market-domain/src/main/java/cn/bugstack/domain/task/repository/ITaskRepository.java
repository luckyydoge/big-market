package cn.bugstack.domain.task.repository;

import cn.bugstack.domain.task.model.TaskEntity;

import java.util.List;

public interface ITaskRepository {
    void sendMessage(TaskEntity taskEntity);

    List<TaskEntity> queryNoSentMessageTask();
}
