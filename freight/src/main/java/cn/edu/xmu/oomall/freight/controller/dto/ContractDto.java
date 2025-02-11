package cn.edu.xmu.oomall.freight.controller.dto;

import cn.edu.xmu.javaee.core.aop.CopyTo;
import cn.edu.xmu.javaee.core.validation.NewGroup;
import cn.edu.xmu.oomall.freight.dao.bo.Contract;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@NoArgsConstructor
@Data
@CopyTo(Contract.class)
public class ContractDto {
    private String account;
    private String secret;
    @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME)
    @NotNull(message = "开始时间不能为空")
    private LocalDateTime beginTime;
    @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME)
    @NotNull(message = "结束时间不能为空")
    private LocalDateTime endTime;
    @NotNull(message = "配送优先级不能为空",groups=NewGroup.class)
    private Integer priority;
    @NotNull(message = "每月配额不能为空")
    private Integer quota;
}
