package cn.edu.xmu.oomall.wechatpay.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

@RestControllerAdvice(basePackages = "cn.edu.xmu.oomall.wechatpay")
public class WeChatPayExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(WeChatPayExceptionHandler.class);

    @ExceptionHandler(WeChatPayException.class)
    @ResponseBody
    Object handleNormalException(WeChatPayException e){
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8");

        logger.debug("WeChatPayException: {}", e.getMessage());
        HashMap<String, String> obj = new HashMap<>(){{
            put("errno", e.getErrno().toString());
            put("errmsg", e.getMessage());
        }};
        return new ResponseEntity<>(obj, headers, HttpStatusCode.valueOf(e.getErrno().getStatus()));
    }

    @Order(Ordered.HIGHEST_PRECEDENCE)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ResponseBody
    Object handleParamError(Exception e) {
        return handleNormalException(new WeChatPayException(WeChatPayReturnNo.PARAM_ERROR));
    }
}