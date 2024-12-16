package cn.edu.xmu.oomall.ztoexpress.unit;

import lombok.Data;

@Data
public class ZTOException extends RuntimeException{
    private ZTOReturnNo ZTOReturnNo;
    public ZTOException(ZTOReturnNo ZTOReturnNo, String message) {
        super(message);
        this.ZTOReturnNo = ZTOReturnNo;
    }
    public ZTOException(ZTOReturnNo ZTOReturnNo) {
        super(ZTOReturnNo.getMessage());
        this.ZTOReturnNo = ZTOReturnNo;
    }
}
