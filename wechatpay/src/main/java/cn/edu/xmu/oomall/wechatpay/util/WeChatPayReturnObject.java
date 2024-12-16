package cn.edu.xmu.oomall.wechatpay.util;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * @author maguoqi
 * @date 2023/12/11
 */
@Getter
public class WeChatPayReturnObject extends ResponseEntity<Object> {

    /**
     * 无返回体，Http状态码返回204
     */
    public WeChatPayReturnObject() {
        super(HttpStatus.NO_CONTENT);
    }

    /**
     * 有返回体
     * @param obj 返回体
     */
    public WeChatPayReturnObject(Object obj) {
        super(obj, HttpStatus.OK);
    }

}
