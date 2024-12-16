package cn.edu.xmu.oomall.jtexpress.controller.dto;

import cn.edu.xmu.oomall.jtexpress.exception.ReturnError;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReturnResult {


    int code;
    String msg;
    Object data;

    public ReturnResult(ReturnError returnError,String massage)
    {
        this.code=returnError.getCode();
        this.msg=massage;
    }
    public ReturnResult(ReturnError returnError,Object data)
    {
        this.code=returnError.getCode();
        this.msg=returnError.getMessage();
        this.data=data;
    }
    public ReturnResult(ReturnError returnError)
    {
        this.code=returnError.getCode();
        this.msg=returnError.getMessage();
    }
    public ReturnResult(ReturnError returnError, String msg, Object data) {
        this.code = returnError.getCode();
        this.msg = msg;
        this.data = data;
    }

}
