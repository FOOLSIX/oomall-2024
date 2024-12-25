package cn.edu.xmu.oomall.comment.mapper.openfeign.po;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class OrderPo {
    private Long id;
    private Byte status;
    private Long shopId;
    List<Long> productIds;
    private Boolean isCompleted;
}
