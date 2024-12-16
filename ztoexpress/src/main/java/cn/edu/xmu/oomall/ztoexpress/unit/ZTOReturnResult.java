package cn.edu.xmu.oomall.ztoexpress.unit;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ZTOReturnResult {
    private String message;
    private boolean status;
    private String statusCode;
    private Object data;
    public ZTOReturnResult(ZTOReturnNo ZTOReturnNo){
        this.data=null;
        this.message= ZTOReturnNo.getMessage();
        this.status= ZTOReturnNo.getStatus();
        this.statusCode= ZTOReturnNo.getStatusCode();
    }
    public ZTOReturnResult(ZTOReturnNo ZTOReturnNo, Object data){
        this.data=data;
        this.message= ZTOReturnNo.getMessage();
        this.status= ZTOReturnNo.getStatus();
        this.statusCode= ZTOReturnNo.getStatusCode();
    }
    public ZTOReturnResult(ZTOReturnNo ZTOReturnNo, String message, Object data){
        this.data=data;
        this.message=message;
        this.status= ZTOReturnNo.getStatus();
        this.statusCode= ZTOReturnNo.getStatusCode();
    }
    public ZTOReturnResult(ZTOReturnNo ZTOReturnNo, String message){
        this.data=null;
        this.message=message;
        this.status= ZTOReturnNo.getStatus();
        this.statusCode= ZTOReturnNo.getStatusCode();
    }
}
