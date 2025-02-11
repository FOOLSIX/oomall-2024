package cn.edu.xmu.oomall.freight.controller.vo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.javaee.core.model.vo.IdNameTypeVo;
import cn.edu.xmu.oomall.freight.dao.bo.Contract;
import cn.edu.xmu.oomall.freight.dao.bo.Express;
import cn.edu.xmu.oomall.freight.dao.bo.Region;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
/**
 * 2023-dgn3-009
 * @author huangzian
 */
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@CopyFrom(Express.class)
public class ExpressVo {
    private Long id;
    private String billCode;
    private IdNameTypeVo logistics;
    private WarehouseVo shipper;
    private WarehouseVo receiver;
    private Byte status;
    private IdNameTypeVo creator;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;
    private IdNameTypeVo modifier;

    public ExpressVo(Express express){
        super();
        CloneFactory.copy(this, express);
        Region region = express.getSendRegion();
        IdNameTypeVo idNameTypeVo = new IdNameTypeVo();
        idNameTypeVo.setId(region.getId());
        idNameTypeVo.setName(region.getName());
        this.creator = IdNameTypeVo.builder().id(express.getCreatorId()).name(express.getCreatorName()).build();
        this.modifier = IdNameTypeVo.builder().id(express.getModifierId()).name(express.getModifierName()).build();
        this.shipper = WarehouseVo.builder().name(express.getSendName()).senderMobile(express.getSendMobile())
                .address(express.getSendAddress()).region(idNameTypeVo).build();
        this.receiver = WarehouseVo.builder().name(express.getReceivName()).senderMobile(express.getReceivMobile())
                .address(express.getReceivAddress()).region(idNameTypeVo).build();
        this.logistics = IdNameTypeVo.builder().id(express.getContractId())
                .name(express.getContract().getLogistics().getName()).build();
    }
}
