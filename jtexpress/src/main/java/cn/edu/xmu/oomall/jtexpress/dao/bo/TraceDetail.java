package cn.edu.xmu.oomall.jtexpress.dao.bo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.oomall.jtexpress.mapper.po.TraceDetailPo;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@CopyFrom({TraceDetailPo.class})
public class TraceDetail {

    @JsonIgnore
    private Long id;
    private String billCode;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime scanTime;
    private String desc;
    private String scanType;
    private String problemType;
    private String staffName;
    private String staffContact;
    private Long scanNetworkId;
    private Long nextNetworkId;
    private Integer rebackStatus;
    private String networkType;
    private String signByOthersType;
    private String signByOthersName;
    private String signByOthersTel;
    private String pickCode;

    /**
     * 扫描类型，注释掉的类型是模拟中使用不到的
     */
    public enum ScanType {
        EXPRESS_COLLECTION(1, "快件揽收"),
        EXPRESS_SIGN(2, "快件签收"),
        PROBLEM_SCAN(3,"问题件扫描");
//
//        WAREHOUSE_ENTRY(2,"入仓扫描（停用）"),
//
//        DISPATCH_SCAN(3,"发件扫描"),
//
//        ARRIVAL_SCAN(4,"到件扫描"),
//
//        WAREHOUSE_EXIT(5,"出仓扫描"),
//
//        WAREHOUSE_ENTRY_NEW(6,"入库扫描"),
//
//        AGENT_COLLECTION(7,"代理点收入扫描"),
//
//        EXPRESS_RETRIEVAL(8,"快件取出扫描"),
//
//        WAREHOUSE_EXIT_NEW(9,"出库扫描"),
//

//
//        SECURITY_SCAN(12,"安检扫描");

        private final int code;
        private final String description;

        ScanType(int code, String description) {
            this.code = code;
            this.description = description;
        }

        public int getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }
    }

    /**
     * 问题类型，注释掉的状态是模拟中使用不到的
     */
    public enum ProblemType {

        CUSTOMER_REJECTION(1, "客户拒收"),
        RETURN_PACKAGE_NETWORK(2, "退回件-网点"),
        PACKAGE_EXCEPTION_NETWORK(3, "包裹异常-网点"),
        PACKAGE_EXCEPTION(4, "包裹异常"),
        PACKAGE_EXCEPTION_CENTER(5, "包裹异常-中心"),
        RETURN_PACKAGE_CENTER(6, "退回件-中心");

//        CUSTOMER_CANCEL_NETWORK(7,"客户取消寄件-网点"),
//
//        CHANGE_DELIVERY_ADDRESS(8,"更改派送地址"),
//
//        EXCEPTION_REMINDER(9,"异常件提醒"),
//
//        UNABLE_TO_CONTACT_RECIPIENT(10,"收件人联系不上"),
//
//        MULTIPLE_FAILED_DELIVERY_ATTEMPTS(11,"多次派件失败"),
//
//        RECIPIENT_CONTACT_ERROR(12,"收件人信息错误"),
//
//        CHANGE_DELIVERY_TIME(13,"更改派送时间"),
//
//        INCOMPLETE_RECIPIENT_ADDRESS(15,"收件地址不详"),
//
//        INCORRECT_RECIPIENT_ADDRESS(16,"收件地址错误"),
//
//        PACKAGE_STORED_AT_NETWORK(17,"包裹存放至网点"),
//
//        PROHIBITED_RECIPIENT_ADDRESS(19,"收件地址禁止"),
//
//        PACKAGE_TEMPORARILY_STORED_NETWORK(21,"包裹暂存网点"),
//
//        PACKAGE_DELAY_HOLIDAY(23,"包裹延迟-节假日"),
//
//        CUSTOMER_CANCEL_CENTER(26,"客户取消寄件-中心");

        private final int code;
        private final String description;

        ProblemType(int code, String description) {
            this.code = code;
            this.description = description;
        }

        public static final Map<Integer,String>problemTypeMap=new HashMap<>(){
            {
                for(ProblemType problemType:ProblemType.values())
                {
                    put(problemType.code, problemType.description);
                }
            }
        };
        public int getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getScanTime() {
        return scanTime;
    }

    public void setScanTime(LocalDateTime scanTime) {
        this.scanTime = scanTime;
    }

    public String getBillCode() {
        return billCode;
    }

    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getScanType() {
        return scanType;
    }

    public void setScanType(String scanType) {
        this.scanType = scanType;
    }

    public String getProblemType() {
        return problemType;
    }

    public void setProblemType(String problemType) {
        this.problemType = problemType;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getStaffContact() {
        return staffContact;
    }

    public void setStaffContact(String staffContact) {
        this.staffContact = staffContact;
    }

    public Long getScanNetworkId() {
        return scanNetworkId;
    }

    public void setScanNetworkId(Long scanNetworkId) {
        this.scanNetworkId = scanNetworkId;
    }

    public Long getNextNetworkId() {
        return nextNetworkId;
    }

    public void setNextNetworkId(Long nextNetworkId) {
        this.nextNetworkId = nextNetworkId;
    }

    public Integer getRebackStatus() {
        return rebackStatus;
    }

    public void setRebackStatus(Integer rebackStatus) {
        this.rebackStatus = rebackStatus;
    }

    public String getNetworkType() {
        return networkType;
    }

    public void setNetworkType(String networkType) {
        this.networkType = networkType;
    }

    public String getSignByOthersType() {
        return signByOthersType;
    }

    public void setSignByOthersType(String signByOthersType) {
        this.signByOthersType = signByOthersType;
    }

    public String getSignByOthersName() {
        return signByOthersName;
    }

    public void setSignByOthersName(String signByOthersName) {
        this.signByOthersName = signByOthersName;
    }

    public String getSignByOthersTel() {
        return signByOthersTel;
    }

    public void setSignByOthersTel(String signByOthersTel) {
        this.signByOthersTel = signByOthersTel;
    }

    public String getPickCode() {
        return pickCode;
    }

    public void setPickCode(String pickCode) {
        this.pickCode = pickCode;
    }
}
