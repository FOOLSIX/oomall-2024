package cn.edu.xmu.oomall.freight.controller.vo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.javaee.core.model.vo.IdNameTypeVo;
import cn.edu.xmu.oomall.freight.dao.bo.Express;
import cn.edu.xmu.oomall.freight.dao.bo.Logistics;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@CopyFrom({Logistics.class})
public class LogisticsVo {
    private Long id;

    private String name;

    private String appId;

    private String appAccount;

    private String snPattern;

    private String secret;

    private String logisticsClass;

    private IdNameTypeVo creator;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

    private IdNameTypeVo modifier;
}
