package cn.edu.xmu.oomall.alipay.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class AlipayReturnObj extends ResponseEntity<Object> {

    /**
     * 无返回体，Http状态码返回204
     */
    public AlipayReturnObj() {
        super(HttpStatus.NO_CONTENT);
    }

    /**
     * 有返回体
     * @param obj 返回体
     */
    public AlipayReturnObj(Object obj) {
        super(obj, HttpStatus.OK);
    }

}