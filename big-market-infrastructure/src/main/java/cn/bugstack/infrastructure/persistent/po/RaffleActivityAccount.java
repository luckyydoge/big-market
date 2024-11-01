package cn.bugstack.infrastructure.persistent.po;

import lombok.Data;

import java.util.Date;

@Data
public class RaffleActivityAccount {

  private long id;
  private String userId;
  private long activityId;
  private long totalCount;
  private long totalCountSurplus;
  private long dayCount;
  private long dayCountSurplus;
  private long monthCount;
  private long monthCountSurplus;
  private Date createTime;
  private Date updateTime;
}
