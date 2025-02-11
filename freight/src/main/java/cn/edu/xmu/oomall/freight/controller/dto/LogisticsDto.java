package cn.edu.xmu.oomall.freight.controller.dto;

import cn.edu.xmu.javaee.core.aop.CopyTo;
import cn.edu.xmu.oomall.freight.dao.bo.Logistics;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@CopyTo({Logistics.class})
public class LogisticsDto {

    @NotBlank
    String name;

    @NotBlank
    String appId;

    @NotBlank
    String appAccount;

    @NotBlank
    String secret;

    @NotBlank
    String snPattern;

    @NotBlank
    String logisticsClass;
}
