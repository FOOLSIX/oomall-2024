package cn.edu.xmu.oomall.alipay.exception;


import cn.edu.xmu.oomall.alipay.util.AlipayReturnNo;

public class AlipayBusinessException extends RuntimeException{

    private AlipayReturnNo errno;

    public AlipayBusinessException(AlipayReturnNo errno, String message) {
        super(message);
        this.errno = errno;
    }

    public AlipayBusinessException(AlipayReturnNo errno) {
        super(errno.getMessage());
        this.errno = errno;
    }

    public AlipayReturnNo getAlipayReturnNo(){
        return this.errno;
    }
}
