package cn.edu.xmu.oomall.freight.mapper.openfeign.zt;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * 查询物流轨迹返回结果类
 * @Author 李子晴
 * 2023-dgn2-001
 */
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QueryOrderTrackRetObj {
/*
 * 运单号
 * 对应Express-billCode
 */
private String billCode;

/*
 * 扫描类型：收件 、发件、 到件、 派件、 签收、 退件、 问题件、 
 * 转单(在货物运输的过程中，受到政策、地域等条件限制，为提升运输及周转时效，会进行转单操作，会将原本的运单转给其他的快递公司，一般单号也会发生变更)、 
 * ARRIVAL（派件入三方自提柜等）、 SIGNED（派件出三方自提柜等）、DELIVERY（上门派送）、DEPARTURE（派件异常出站）、安检。
 * 转单后扫描类型包括：1（收件）、2（发件）、3（到件）、4（派件）、5（签收）、7（第三方签收）、8（退件）、9（转单）、44（第三方进站）、45（第三方出站）。
 * 可以据此修改运单状态Express-status。
 */
private String scanType;

/*
 * 扫描时间
 */
private Date scanDate;

/*
 * 退改类型（仅在扫描类型是退件时会返回）：1.返回发件网点；2.返回发件人地址；3.修改地址；4.退件扫描；5.问题件拒收；6.问题件改地址；10.限发退回发件人
 */
private String returnType;
}
