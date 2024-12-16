//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.javaee.core.model;

import cn.edu.xmu.javaee.core.model.dto.UserDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 常量定义
 */
public interface Constants {
    /**
     * 时间格式定义
     */
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    /**
     * 默认结束时间
     */
    public static final LocalDateTime END_TIME = LocalDateTime.now();

    public static final LocalDateTime BEGIN_TIME = END_TIME.minusMonths(1L);

    /**
     * 查询结果最大返回值
     */
    public static final int MAX_RETURN = 1000;

    /**
     * 平台用户的shopId
     */
    public static final Long PLATFORM = 0L;

    /**
     * 无商铺的后台管理用户
     */
    public static final Long NOSHOP = -1L;

    /**
     * 更新时ID不存在
     */
    public static final Long IDNOTEXIST = -1L;


    /**
     * 系统用户
     */
    public static final UserDto SYSTEM = UserDto.builder().id(0L).name("system").build();
}
