package cn.edu.xmu.oomall.sfexpress.aop;

import cn.edu.xmu.javaee.core.util.SnowFlakeIdWorker;
import cn.edu.xmu.oomall.sfexpress.controller.vo.ApiResultData;
import cn.edu.xmu.oomall.sfexpress.controller.vo.SFResponseVo;
import cn.edu.xmu.oomall.sfexpress.exception.SFErrorCodeEnum;
import cn.edu.xmu.oomall.sfexpress.exception.SFException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 模拟顺丰全局异常处理器
 * @author Zhouzhe Fan
 * dgn3-004-fzz
 */
@Aspect
@Component
public class SFExpressControllerAspect {

    private final Logger logger = LoggerFactory.getLogger(SFExpressControllerAspect.class);
    private SnowFlakeIdWorker idWorker = new SnowFlakeIdWorker(1L, 1L);

    @Around("execution(public * cn.edu.xmu.oomall.sfexpress.controller.SFExpressController.*(..))")
    public Object handleSFException(ProceedingJoinPoint jp) throws Throwable {
        SFResponseVo retVal = null;
        try {
            retVal = (SFResponseVo) jp.proceed();
        } catch (SFException e) {
            SFErrorCodeEnum error = e.getErrno();
            logger.info("handleSFException: SFException， errno = {}", error.getErrorDescAndAdvice());
            retVal = new SFResponseVo();
            ApiResultData<Object> apiResultData = new ApiResultData<>();
            apiResultData.setSuccess(false);
            apiResultData.setErrorCode(error.getErrorCodeString());
            apiResultData.setErrorMsg(error.getErrorDescAndAdvice());
            retVal.setApiErrorMsg("请求参数错误");
            retVal.setApiResponseID(idWorker.nextId().toString());
            retVal.setApiResultCode("A1001");
            retVal.setApiResultData(apiResultData);
            return retVal;
        } catch (Exception e) {
            Throwable cause = e.getCause();
            if (null == cause) cause = e;
            retVal = new SFResponseVo();
            ApiResultData<Object> apiResultData = new ApiResultData<>();
            apiResultData.setSuccess(false);
            String message = cause.getMessage();
            apiResultData.setErrorMsg(message);
            String errMessage = message.split(", ")[0];
            SFErrorCodeEnum errorCodeEnum = SFErrorCodeEnum.getByErrorChineseDesc(errMessage);
            if (null != errorCodeEnum) {
                apiResultData.setErrorCode(errorCodeEnum.getErrorCodeString());
            }
            retVal.setApiErrorMsg("请求参数错误");
            retVal.setApiResponseID(idWorker.nextId().toString());
            retVal.setApiResultCode("A1001");
            retVal.setApiResultData(apiResultData);
            return retVal;
        }
        return retVal;
    }


}
