package cn.bugstack.infrastructure.persistent.po;

import lombok.Data;

import java.util.Date;

@Data
public class RaffleActivity {

  private long id;
  private long activityId;
  private String activityName;
  private String activityDesc;
  private Date beginDateTime;
  private Date endDateTime;
  private long stockCount;
  private long stockCountSurplus;
  private long activityCountId;
  private long strategyId;
  private String state;
  private Date createTime;
  private Date updateTime;
}
