package cn.edu.xmu.oomall.freight.dao.bo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.javaee.core.aop.CopyTo;
import cn.edu.xmu.javaee.core.model.bo.OOMallObject;
import cn.edu.xmu.oomall.freight.mapper.po.LogisticsPo;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author fan ninghan
 * 2023-dng3-008
 */

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString(callSuper = true, doNotUseGetters = true)
@CopyFrom({LogisticsPo.class})
@CopyTo({LogisticsPo.class})
public class Logistics extends OOMallObject implements Serializable {
    private String name;

    private String appId;

    private String appAccount;

    private String snPattern;

    private String secret;

    private String logisticsClass;

    @Override
    public void setGmtCreate(LocalDateTime gmtCreate) {
        super.gmtCreate = gmtCreate;
    }

    @Override
    public void setGmtModified(LocalDateTime gmtModified) {
        super.gmtModified = gmtModified;
    }
}
