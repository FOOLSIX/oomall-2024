package cn.edu.xmu.oomall.freight.mapper.openfeign.zt;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * 汇总信息类
 * @Author 李子晴
 * 2023-dgn2-001
 */

 @NoArgsConstructor
 @Data
 @JsonInclude(JsonInclude.Include.NON_NULL)
public class SummaryInfo {

    /*
     * 取件开始时间
     */
       private Date startTime;

    /*
     * 取件截止时间
     */
       private Date endTime;
    
}
