package cn.edu.xmu.oomall.freight.mapper.openfeign.jt;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 物流轨迹信息类
 *
 * @author 徐森彬
 * 2023-dgn2-003
 */
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TraceDetail {
    /**
     * 扫描时间
     * 时间格式 yyyy-MM-dd HH:mm:ss
     */
    String scanTime;

    /*
      修改express的逻辑：
        scanType   	快件揽收                	            >>1在途
        scanType   	快件签收                	            >>2签收
        problemType	客户拒收                	            >>4拒收
        problemType	包裹异常/包裹异常-网点/包裹异常-中心	    >>6丢失
        problemType	退回件-网点/退回件-中心       	        >>5已退回
       另外：物流轨迹长时间不改变                             >>6丢失
     */

    /**
     * 扫描类型
     * 1、快件揽收
     * 2、入仓扫描（停用）
     * 3、发件扫描
     * 4、到件扫描
     * 5、出仓扫描
     * 6、入库扫描
     * 7、代理点收入扫描
     * 8、快件取出扫描
     * 9、出库扫描
     * 10、快件签收
     * 11、问题件扫描
     * 12、安检扫描
     */
    String scanType;

    /**
     * 问题类型
     * A1、客户取消寄件-网点
     * A2、客户拒收
     * A3、更改派送地址
     * A4、退回件-网点
     * A7、异常件提醒
     * A8、包裹异常-网点
     * A9、收件人联系不上
     * A10、收件人联系不上
     * A11、多次派件失败
     * A12、收件人信息错误
     * A13、更改派送时间
     * A14、客户拒收
     * A15、收件地址不详
     * A16、收件地址错误
     * A17、包裹存放至网点
     * A18、包裹存放至网点
     * A19、收件地址禁止
     * A20、包裹异常
     * A21、包裹暂存网点
     * A23、包裹延迟-节假日
     * A24、包裹异常-中心
     * A25、退回件-中心
     * A26、客户取消寄件-中心
     */
    String problemType;
}
