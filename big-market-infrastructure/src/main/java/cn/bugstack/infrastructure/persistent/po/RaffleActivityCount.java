package cn.bugstack.infrastructure.persistent.po;

import lombok.Data;

import java.util.Date;

@Data
public class RaffleActivityCount {

  private long id;
  private long activityCountId;
  private long totalCount;
  private long dayCount;
  private long monthCount;
  private Date createTime;
  private Date updateTime;
}
