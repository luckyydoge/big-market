package cn.bugstack.trigger.job;

import cn.bugstack.domain.task.model.TaskEntity;
import cn.bugstack.domain.task.service.ITaskService;
import cn.bugstack.middleware.db.router.strategy.IDBRouterStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@Component
public class SendAwardMessageTaskJob {

    private final ITaskService taskService;

    private final ThreadPoolExecutor  threadPoolExecutor;

    private final IDBRouterStrategy dbRouter;

    public SendAwardMessageTaskJob(ITaskService taskService, ThreadPoolExecutor threadPoolExecutor, IDBRouterStrategy dbRouter) {
        this.taskService = taskService;
        this.dbRouter = dbRouter;
        this.threadPoolExecutor = threadPoolExecutor;
    }

    @Scheduled(cron = "0/5 * * * * *")
    public void exec() {
        try {
            int dbCount = dbRouter.dbCount();
            for (int dbIdx = 1; dbIdx <= dbCount; dbIdx++) {
                int finalDbIdx = dbIdx;
                threadPoolExecutor.execute(() -> {
                    try {
                        dbRouter.setDBKey(finalDbIdx);
                        dbRouter.setTBKey(0);
                        List<TaskEntity> taskList = taskService.queryNoSentMessageTask();
                        if (taskList.isEmpty()) {
                            return;
                        }

                        taskList.forEach(task -> {
                            threadPoolExecutor.execute(() -> taskService.sendMessage(task));
                        });
                    } finally {
                        dbRouter.clear();
                    }
                });

            }
        } finally {
            dbRouter.clear();
        }
    }



}
