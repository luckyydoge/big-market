package cn.bugstack.infrastructure.persistent.po;

import lombok.Data;

import java.util.Date;

@Data
public class RaffleActivityAccountFlow {

  private long id;
  private String userId;
  private long activityId;
  private long totalCount;
  private long dayCount;
  private long monthCount;
  private String flowId;
  private String flowChannel;
  private String bizId;
  private Date createTime;
  private Date updateTime;
}
