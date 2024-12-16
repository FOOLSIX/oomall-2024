package cn.edu.xmu.oomall.sfexpress.dao;

import cn.edu.xmu.oomall.sfexpress.exception.SFErrorCodeEnum;
import cn.edu.xmu.oomall.sfexpress.exception.SFException;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Zhouzhe Fan
 * dgn3-004-fzz
 */
public enum ExpressStatusEnum {
    UNCOMPLETED(0, "未完成"),
    COMPLETED(1, "已完成"),
    CANCELED(2, "已取消");

    private final int code;
    private final String description;

    private static final Map<AbstractMap.SimpleEntry<Integer, Integer>, SFErrorCodeEnum> map = new HashMap<>() {
        {
            //已完成状态不可以更改
            put(new SimpleEntry<>(COMPLETED.code, UNCOMPLETED.code), SFErrorCodeEnum.E8016);
            put(new SimpleEntry<>(COMPLETED.code, CANCELED.code), SFErrorCodeEnum.E8016);
            put(new SimpleEntry<>(COMPLETED.code, COMPLETED.code), SFErrorCodeEnum.E8016);

            //已取消状态不可以更改
            put(new SimpleEntry<>(CANCELED.code, UNCOMPLETED.code), SFErrorCodeEnum.E8016);
            put(new SimpleEntry<>(CANCELED.code, COMPLETED.code), SFErrorCodeEnum.E8016);
            put(new SimpleEntry<>(CANCELED.code, CANCELED.code), SFErrorCodeEnum.E8016);

        }
    };

    ExpressStatusEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static SFErrorCodeEnum getByStatus(int origin, int target) {
        return map.get(new AbstractMap.SimpleEntry<>(origin, target));
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
