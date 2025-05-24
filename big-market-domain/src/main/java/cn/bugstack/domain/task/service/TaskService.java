package cn.bugstack.domain.task.service;

import cn.bugstack.domain.task.model.TaskEntity;
import cn.bugstack.domain.task.repository.ITaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class TaskService implements ITaskService{

    private final ITaskRepository taskRepository;

    public TaskService(ITaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public void sendMessage(TaskEntity taskEntity) {
        taskRepository.sendMessage(taskEntity);

    }

    @Override
    public List<TaskEntity> queryNoSentMessageTask() {
        return taskRepository.queryNoSentMessageTask();
    }
}
