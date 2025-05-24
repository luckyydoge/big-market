package cn.bugstack.domain.task.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskEntity {
    private String topic;
    private String userId;
    private String messageId;
    private String message;
    private String state;
}
