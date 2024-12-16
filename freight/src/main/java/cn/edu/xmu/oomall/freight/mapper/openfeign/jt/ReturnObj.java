package cn.edu.xmu.oomall.freight.mapper.openfeign.jt;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 接口返回数据类
 *
 * @author 徐森彬
 * 2023-dgn2-003
 */
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReturnObj<T> {
    /**
     * 返回码
     */
    String code;

    /**
     * 描述
     */
    String msg;

    /**
     * 返回的业务数据
     */
    T data;

}
