package cn.edu.xmu.oomall.jtexpress.aop;

import cn.edu.xmu.oomall.jtexpress.controller.dto.ReturnResult;
import cn.edu.xmu.oomall.jtexpress.exception.JTException;
import cn.edu.xmu.oomall.jtexpress.exception.ReturnError;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.util.NestedServletException;
import jakarta.validation.ValidationException;


@Aspect
@Component
public class JTExpressControllerAspect {

    private final Logger logger = LoggerFactory.getLogger(JTExpressControllerAspect.class);

    @Around("execution(public * cn.edu.xmu.oomall.jtexpress.controller.JTExpressController.*(..))")
    public Object handleJTException(ProceedingJoinPoint jp) throws Throwable {
        ReturnResult retVal = null;
        try {
            retVal = (ReturnResult) jp.proceed();
        } catch (JsonMappingException jsonMappingException) {
            Throwable cause = jsonMappingException.getCause();
            if (null == cause) cause = jsonMappingException;
            ReturnError error = ReturnError.getByMsg(cause.getMessage());
            logger.info("handleJTException: JTException， errno = {}", error.getMessage());
            retVal = new ReturnResult(error, error.getMessage());
        }catch (JsonParseException jsonParseException)
        {
            logger.info("handleJTException: JTException， errno = {}",ReturnError.ILLEGAL_PARAMETER.getMessage());
            retVal = new ReturnResult(ReturnError.ILLEGAL_PARAMETER);
        }
        catch (ValidationException e) {
            ReturnError error = ReturnError.getByMsg(e.getMessage());
            logger.info("handleJTException: JTException， errno = {}", error.getMessage());
            retVal = new ReturnResult(error, error.getMessage());
        } catch (JTException exception) {
            logger.info("handleJTException: JTException， errno = {}", exception.getErrno());
            retVal = new ReturnResult(exception.getErrno(), exception.getMessage());
        } catch (Exception e) {
            String message = e.getMessage();
            if (message != null && message.contains("bill_code")) message = ReturnError.INVALID_WAYBILL_NO.getMessage();
            ReturnError error = ReturnError.getByMsg(message);
            logger.info("handleJTException: JTException， errno = {}", error.getMessage());
            retVal = new ReturnResult(error, error.getMessage());
        }
        return retVal;
    }


}
