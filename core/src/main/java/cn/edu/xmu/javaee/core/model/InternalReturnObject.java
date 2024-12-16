//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.javaee.core.model;

import java.io.Serializable;

public class InternalReturnObject<T> implements Serializable {
    private Integer errno=0;
    private String errmsg="成功";
    private T data;
    public InternalReturnObject(Integer errno, String errmsg) {
        this.errno = errno;
        this.errmsg = errmsg;
    }

    public InternalReturnObject(Integer errno, String errmsg, T data) {
        this.errno = errno;
        this.errmsg = errmsg;
        this.data=data;
    }
    public InternalReturnObject(T data) {
        this.data=data;
    }

    public InternalReturnObject() {
    }

    public void setErrno(Integer errno) { this.errno = errno;}

    public void setErrmsg(String errmsg) { this.errmsg = errmsg;}

    public void setData(T data) { this.data = data;}

    public Integer getErrno() {
        return errno;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public T getData() {
        return data;
    }
}
