package cn.bugstack.domain.award.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AwardStateVO {
    create("create", "创建"),
    retry("retry", "重试"),
    success("success", "成功"),
    fail("fail", "失败"),
    ;

    private final String code;
    private final String desc;
}
