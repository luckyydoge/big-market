package cn.bugstack.infrastructure.persistent.po;

import lombok.Data;

import java.util.Date;

@Data
public class RaffleActivityOrder {

  private long id;
  private String userId;
  private long activityId;
  private String activityName;
  private long strategyId;
  private String orderId;
  private Date orderTime;
  private String state;
  private Date createTime;
  private Date updateTime;
}
