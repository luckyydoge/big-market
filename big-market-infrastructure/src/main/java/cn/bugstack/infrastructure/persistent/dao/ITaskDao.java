package cn.bugstack.infrastructure.persistent.dao;

import cn.bugstack.infrastructure.persistent.po.Task;
import cn.bugstack.middleware.db.router.annotation.DBRouter;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ITaskDao {
    void insert(Task task);

    @DBRouter(key = "userId")
    void changeState2Completed(Task task);

    @DBRouter(key = "userId")
    void changeState2Failed(Task task);

    @DBRouter(key = "userId")
    void changeState2Retry(Task task);

    List<Task> queryNoSentMessageTask();
}
