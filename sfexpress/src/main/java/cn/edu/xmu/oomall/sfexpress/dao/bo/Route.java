package cn.edu.xmu.oomall.sfexpress.dao.bo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.oomall.sfexpress.mapper.po.SfexpressRoutePo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * @author Zhouzhe Fan
 * dgn3-004-fzz
 */
@NoArgsConstructor
public class Route {
    @JsonProperty("acceptTime")
    private Timestamp acceptTime;
    @JsonProperty("acceptAddress")
    private String acceptAddress;
    @JsonProperty("opCode")
    private String opCode;

    /**
     * 物流状态，我们系统主要靠物流列表中最新的物流状态来更新物流状态
     */
    private String remark;

    public Timestamp getAcceptTime() {
        return acceptTime;
    }

    public void setAcceptTime(Timestamp acceptTime) {
        this.acceptTime = acceptTime;
    }

    public String getAcceptAddress() {
        return acceptAddress;
    }

    public void setAcceptAddress(String acceptAddress) {
        this.acceptAddress = acceptAddress;
    }

    public String getOpCode() {
        return opCode;
    }

    public void setOpCode(String opCode) {
        this.opCode = opCode;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
