package cn.edu.xmu.oomall.ztoexpress.aop;

import cn.edu.xmu.oomall.ztoexpress.unit.ZTOException;
import cn.edu.xmu.oomall.ztoexpress.unit.ZTOReturnResult;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static cn.edu.xmu.oomall.ztoexpress.unit.ZTOReturnNo.S202;

@Component
@Aspect
public class ZTOExpressControllerAspect {
    private final Logger logger = LoggerFactory.getLogger(ZTOExpressControllerAspect.class);
    @Around("execution(public * cn.edu.xmu.oomall.ztoexpress.controller.ZTOExpressController.*(..))")
    public ZTOReturnResult handleZTOException(ProceedingJoinPoint jp) throws Throwable{
        ZTOReturnResult ztoReturnResult =null;
        try {
            ztoReturnResult =(ZTOReturnResult) jp.proceed();
        }
        catch (ZTOException e){
            logger.info("handleJTException: JTException， errno = {}", e.getZTOReturnNo());
            ztoReturnResult =new ZTOReturnResult(e.getZTOReturnNo(),e.getMessage());
        }
        catch (Exception e){
            logger.info("handleJTException: Exception， errno = {}", e.getMessage());
            ztoReturnResult = new ZTOReturnResult(S202);
        }
        return ztoReturnResult;

    }
}
