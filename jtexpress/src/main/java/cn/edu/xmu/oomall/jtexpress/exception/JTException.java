package cn.edu.xmu.oomall.jtexpress.exception;

import cn.edu.xmu.javaee.core.model.ReturnNo;

public class JTException extends RuntimeException{

    private ReturnError errno;

    public JTException(ReturnError errno, String message) {
        super(message);
        this.errno = errno;
    }

    public JTException(ReturnError errno) {
        super(errno.getMessage());
        this.errno = errno;
    }

    public ReturnError getErrno(){
        return this.errno;
    }
}
