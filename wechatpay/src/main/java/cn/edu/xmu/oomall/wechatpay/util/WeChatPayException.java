package cn.edu.xmu.oomall.wechatpay.util;

import lombok.Getter;

@Getter
public class WeChatPayException extends RuntimeException {

    private final WeChatPayReturnNo errno;

    public WeChatPayException(WeChatPayReturnNo errno, String message) {
        super(message);
        this.errno = errno;
    }

    public WeChatPayException(WeChatPayReturnNo errno) {
        super(errno.getMessage());
        this.errno = errno;
    }

}
