package cn.edu.xmu.javaee.core.aop;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.util.JacksonUtil;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Aspect
@Component
@Slf4j
public class OpenFeignMapperAspect {

    @Around("cn.edu.xmu.javaee.core.aop.CommonPointCuts.openFeignMapperMethods()")
    public Object handleFeignMapperReturn(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result;

        try {
            // 执行目标方法（即 Feign 调用）
            result = joinPoint.proceed();

            // Feign 请求成功返回（状态码200或201）
            if (Objects.nonNull(result)) {
                // 如果返回的是 InternalReturnObject 类型，直接获取 errno
                if (result instanceof InternalReturnObject<?> internalReturnObject) {

                    // 如果 errno 不为 0，抛出 BusinessException
                    if (internalReturnObject.getErrno() != ReturnNo.OK.getErrNo()) {
                        String errmsg = internalReturnObject.getErrmsg();
                        log.error("HTTP code is 200/201 but InternalReturnObject error: errno={}, errmsg={}", internalReturnObject.getErrno(), errmsg);
                        throw new BusinessException(ReturnNo.getReturnNoByCode(internalReturnObject.getErrno()), errmsg != null ? errmsg : "未知错误");
                    }
                } else {
                    // 如果不是 InternalReturnObject 类型，可以按需处理
                    log.warn("Unexpected response type: {}", result.getClass().getName());
                }
            }
        } catch (FeignException e) {
            // 捕获 FeignException
            log.error("FeignException occurred: {}", e.getMessage());

            // 解析 FeignException 的响应体，提取 errno 和 errmsg
            String responseBody = e.contentUTF8();  // 获取响应体的内容
            log.debug("FeignException response body: {}", responseBody);

            // 解析 JSON 内容
            // 这里假设返回的 JSON 是类似 {"errno":4, "errmsg":"运费模板对象(id=9999)不存在"} 格式
            // 使用 JacksonUtil 解析 JSON 响应体中的 errno 和 errmsg
            Integer errno = JacksonUtil.parseInteger(responseBody, "errno");
            if (Objects.isNull(errno)) {
                log.error("Error: errno is null in FeignException response");
                throw new BusinessException(ReturnNo.INTERNAL_SERVER_ERR, "服务器错误");
            }

            String errmsg = JacksonUtil.parseString(responseBody, "errmsg");
            // 根据 errno 判断并抛出对应的 BusinessException
            throw new BusinessException(ReturnNo.getReturnNoByCode(errno), errmsg);

        } catch (Exception e) {
            // 捕获其他未预期的异常
            log.error("Unexpected exception occurred: {}", e.getMessage());
            throw e;  // 可以根据需要重新抛出或封装为自定义异常
        }

        return result;  // 返回结果
    }
}
