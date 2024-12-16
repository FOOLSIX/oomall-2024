package cn.edu.xmu.oomall.sfexpress.exception;

import cn.edu.xmu.javaee.core.model.ReturnNo;

/**
 * @author Zhouzhe Fan
 * dgn3-004-fzz
 */
public class SFException extends RuntimeException{

    private SFErrorCodeEnum errno;

    public SFException(SFErrorCodeEnum errno, String message) {
        super(message);
        this.errno = errno;
    }

    public SFException(SFErrorCodeEnum errno) {
        super(errno.getErrorDescAndAdvice());
        this.errno = errno;
    }

    public SFErrorCodeEnum getErrno(){
        return this.errno;
    }
}