package cn.edu.xmu.oomall.comment.mapper.openfeign.po;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class OrderItem {
    private Long id;
    private Long quantity;
}
