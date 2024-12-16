package cn.edu.xmu.oomall.alipay.util;

import cn.edu.xmu.oomall.alipay.exception.AlipayBusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(basePackages = "cn.edu.xmu.oomall.alipay")
public class AlipayExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(AlipayExceptionHandler.class);

    @ExceptionHandler(AlipayBusinessException.class)
    public ResponseEntity<String> handleException(AlipayBusinessException e) {
        logger.error("AlipayBusinessException: " + e.getMessage());
        return ResponseEntity.status(400).body(e.getAlipayReturnNo().toString());
    }


}
